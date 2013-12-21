package com.lateensoft.pathfinder.toolkit.character;

import com.lateensoft.pathfinder.toolkit.PTBasePageFragment;
import com.lateensoft.pathfinder.toolkit.PTMainActivity;
import com.lateensoft.pathfinder.toolkit.PTNavDrawerAdapter;
import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.datahelpers.PTDatabaseManager;
import com.lateensoft.pathfinder.toolkit.datahelpers.PTSharedPreferences;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

//This is a base class for all fragments in the character sheet activity
public abstract class PTCharacterSheetFragment extends PTBasePageFragment {
	private static final String TAG = PTCharacterSheetFragment.class.getSimpleName();
	
	private final int MENU_ITEM_CHARACTER_LIST = 0;
	private final int MENU_ITEM_NEW_CHARACTER = 1;
	private final int MENU_ITEM_DELETE_CHARACTER = 2;
	private final int MENU_ITEM_AUTOFILL = 3;
	
	public static final int STR_KEY = 0;
	public static final int DEX_KEY = 1;
	public static final int CON_KEY = 2;
	public static final int INT_KEY = 3;
	public static final int WIS_KEY = 4;
	public static final int CHA_KEY = 5;
	
	public static final int FORT_KEY = 0;
	public static final int REF_KEY = 1;
	public static final int WILL_KEY = 2;
	
	public static final int TAB_INDEX_FLUFF = 0;
	public static final int TAB_INDEX_ABILITIES = 2;
	
	public PTCharacter mCharacter;

	private PTDatabaseManager mSQLManager;

	private int mDialogMode;
	private int mCharacterSelectedInDialog;
	
	private OnClickListener mCharacterClickListener;
	
	private boolean mIsWaitingForResult = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSQLManager = new PTDatabaseManager(getActivity());
		loadCurrentCharacter();
		mCharacterSelectedInDialog = mCharacter.mID;
		
		setupCharacterClickListener();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	public void updateTitle() {
		setTitle(mCharacter.getName());
		setSubtitle(getFragmentTitle());
	}

	@Override
	public void onPause() {
		updateCharacterDatabase();
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (!mIsWaitingForResult) {
			loadCurrentCharacter();
			updateFragmentUI();
			updateTitle();
		}
		mIsWaitingForResult = false;
		Log.d(TAG, "resume");
	}
	
	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		super.startActivityForResult(intent, requestCode);
		mIsWaitingForResult = true;
	}

	/**
	 * Load the currently set character in shared prefs If there is no character
	 * set in user prefs, it automatically generates a new one.
	 */
	public void loadCurrentCharacter() {
		int currentCharacterID = PTSharedPreferences.getSharedInstance().getSelectedCharacter();

		if (currentCharacterID == -1) { // There was no current character set in
										// shared prefs
			Log.v(TAG, "Default character add");
			addNewCharacter();
		} else {
			Log.v(TAG, "Loading character");
			mCharacter = mSQLManager.getCharacter(currentCharacterID);
			if (getRootView() != null) {
				updateFragmentUI();
			}

		}
		Log.v(TAG, "Loaded character: " + mCharacter.mID);
	}

	/**
	 * Generates a new character and sets it to the current character. Reloads
	 * the fragments.
	 */
	public void addNewCharacter() {
		mCharacter = mSQLManager.addNewCharacter("New Adventurer",
				getActivity().getApplicationContext());
		PTSharedPreferences.getSharedInstance().setSelectedCharacter(mCharacter.mID);
		performUpdateReset();
		Log.v(TAG, "Added new character");
	}

	/**
	 * Deletes the current character and loads the first in the list, or creates
	 * a new blank one, if there was only one.
	 */
	public void deleteCurrentCharacter() {
		int currentCharacterIndex = 0;
		int currentCharacterID = mCharacter.mID;
		int characterIDs[] = mSQLManager.getCharacterIDs();

		for (int i = 0; i < characterIDs.length; i++) {
			if (currentCharacterID == characterIDs[i])
				currentCharacterIndex = i;
		}

		if (characterIDs.length == 1) {
			addNewCharacter();
		} else if (currentCharacterIndex == 0) {
			PTSharedPreferences.getSharedInstance().setSelectedCharacter(characterIDs[1]);
			loadCurrentCharacter();
		} else {
			PTSharedPreferences.getSharedInstance().setSelectedCharacter(characterIDs[0]);
			loadCurrentCharacter();
		}

		mSQLManager.deleteCharacter(currentCharacterID);
		Log.v(TAG, "Deleted current character: " + currentCharacterID);
	}

	public void updateCharacterDatabase() {
		mSQLManager.updateCharacter(mCharacter);
		updateTitle();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {

			case MENU_ITEM_CHARACTER_LIST: // Tapped character list button
				mDialogMode = MENU_ITEM_CHARACTER_LIST;
				showCharacterDialog();
				break;
			case MENU_ITEM_NEW_CHARACTER:
				// Add new character
				mDialogMode = MENU_ITEM_NEW_CHARACTER;
				showCharacterDialog();
				break;
			case MENU_ITEM_DELETE_CHARACTER:
				// Delete character
				mDialogMode = MENU_ITEM_DELETE_CHARACTER;
				showCharacterDialog();
				break;

			case MENU_ITEM_AUTOFILL:
				autoFill();
				updateFragmentUI();
				break;
			}
		

		return super.onOptionsItemSelected(item);
	}

	private void autoFill() {
		mCharacter.getCombatStatSet().setACDexMod(
				mCharacter.getAbilitySet().getAbilityScore(DEX_KEY)
						.getModifier());
		mCharacter.getCombatStatSet().setInitDexMod(
				mCharacter.getAbilitySet().getAbilityScore(DEX_KEY)
						.getModifier());
		mCharacter.getCombatStatSet().setCMDDexMod(
				mCharacter.getAbilitySet().getAbilityScore(DEX_KEY)
						.getModifier());
		mCharacter
				.getSaveSet()
				.getSave(REF_KEY)
				.setAbilityMod(
						mCharacter.getAbilitySet().getAbilityScore(DEX_KEY)
								.getModifier());

		mCharacter.getCombatStatSet().setStrengthMod(
				mCharacter.getAbilitySet().getAbilityScore(STR_KEY)
						.getModifier());

		mCharacter
				.getSaveSet()
				.getSave(FORT_KEY)
				.setAbilityMod(
						mCharacter.getAbilitySet().getAbilityScore(CON_KEY)
								.getModifier());

		mCharacter
				.getSaveSet()
				.getSave(WILL_KEY)
				.setAbilityMod(
						mCharacter.getAbilitySet().getAbilityScore(WIS_KEY)
								.getModifier());

		int key;

		for (int i = 0; i < mCharacter.getSkillSet().getSkills().length; i++) {
			key = mCharacter.getSkillSet().getSkill(i).getKeyAbilityKey();
			mCharacter
					.getSkillSet()
					.getSkill(i)
					.setAbilityMod(
							mCharacter.getAbilitySet().getAbilityScore(key)
									.getModifier());
		}
		
		Toast.makeText(getActivity(), 
				getString(R.string.autofill_toast_text),
				Toast.LENGTH_SHORT).show();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Initialize the global menu items

		MenuItem characterListItem = menu.add(Menu.NONE,
				MENU_ITEM_CHARACTER_LIST, Menu.NONE,
				R.string.menu_item_character_list);
		characterListItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		characterListItem.setIcon(R.drawable.ic_menu_character_list);

		MenuItem newCharacterItem = menu.add(Menu.NONE,
				MENU_ITEM_NEW_CHARACTER, Menu.NONE,
				R.string.menu_item_new_character);
		newCharacterItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

		MenuItem deleteCharacterItem = menu.add(Menu.NONE,
				MENU_ITEM_DELETE_CHARACTER, Menu.NONE,
				R.string.menu_item_delete_character);
		deleteCharacterItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

		MenuItem autoFillItem = menu.add(Menu.NONE, MENU_ITEM_AUTOFILL,
				Menu.NONE, R.string.auto_fill);
		autoFillItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		autoFillItem.setIcon(R.drawable.ic_menu_autofill);

		super.onCreateOptionsMenu(menu);
		return true;
	}

	private void showCharacterDialog() {
		mCharacterSelectedInDialog = mCharacter.mID; // actual current character

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		

		switch (mDialogMode) {
		case MENU_ITEM_CHARACTER_LIST:
			builder.setTitle(getString(R.string.select_character_dialog_header));

			String[] characterList = mSQLManager.getCharacterNames();
			int characterIDs[] = mSQLManager.getCharacterIDs();
			int currentCharacterIndex = 0;

			for (int i = 0; i < characterIDs.length; i++) {
				if (mCharacterSelectedInDialog == characterIDs[i])
					currentCharacterIndex = i;
			}

			builder.setSingleChoiceItems(characterList, currentCharacterIndex,
					mCharacterClickListener)
					.setPositiveButton(R.string.ok_button_text, mCharacterClickListener)
					.setNegativeButton(R.string.cancel_button_text, mCharacterClickListener);
			break;

		case MENU_ITEM_NEW_CHARACTER:
			builder.setTitle(getString(R.string.menu_item_new_character));
			builder.setMessage("Create new character?")
					.setPositiveButton(R.string.ok_button_text, mCharacterClickListener)
					.setNegativeButton(R.string.cancel_button_text, mCharacterClickListener);
			break;

		case MENU_ITEM_DELETE_CHARACTER:
			builder.setTitle(getString(R.string.menu_item_delete_character));
			builder.setMessage(
					getString(R.string.delete_character_dialog_message_1)
							+ mCharacter.getName()
							+ getString(R.string.delete_character_dialog_message_2))
					.setPositiveButton(R.string.delete_button_text, mCharacterClickListener)
					.setNegativeButton(R.string.cancel_button_text, mCharacterClickListener);
			break;

		}
		AlertDialog alert = builder.create();
		alert.show();
	}

	private void setupCharacterClickListener() {
		mCharacterClickListener = new OnClickListener() {
			// Click method for the character selection dialog
			public void onClick(DialogInterface dialogInterface, int selection) {
				switch (selection) {
				case DialogInterface.BUTTON_POSITIVE:
					performPositiveDialogAction();
					break;
				case DialogInterface.BUTTON_NEGATIVE:
					break;
				default:
					// Set the currently selected character in the dialog
					mCharacterSelectedInDialog = mSQLManager.getCharacterIDs()[selection];
					Log.v(TAG, "Selected character " + mCharacterSelectedInDialog);
					break;

				}
			}
		};
	}

	/**
	 * Called when dialog positive button is tapped
	 */
	public void performPositiveDialogAction() {
		switch (mDialogMode) {
		case MENU_ITEM_CHARACTER_LIST:
			// Check if "currently selected" character is the same as saved one
			if (mCharacterSelectedInDialog != mCharacter.mID) {
				performUpdateReset();

				PTSharedPreferences.getSharedInstance()
						.setSelectedCharacter(mCharacterSelectedInDialog);
				loadCurrentCharacter();
			}
			break;

		case MENU_ITEM_NEW_CHARACTER:
			performUpdateReset();
			addNewCharacter();
			break;

		case MENU_ITEM_DELETE_CHARACTER:
			deleteCurrentCharacter();
			break;

		}

	}

	/**
	 * Depending on the use, this forces the current tab to save its values to
	 * mCharacter, and updates them. Ends with current tab set to Fluff.
	 */
	public void performUpdateReset() {
		updateCharacterDatabase();
		((PTMainActivity) getActivity()).showView(PTNavDrawerAdapter.FLUFF_ID);
	}

	public PTCharacter getCurrentCharacter() {
		return mCharacter;
	}

	public void setCurrentCharacter(PTCharacter character) {
		if (character != null)
			mCharacter = character;
	}

	/**
	 * Refreshes the UI. Is automatically called onResume
	 */
	public abstract void updateFragmentUI();
	
	public abstract String getFragmentTitle();

}
