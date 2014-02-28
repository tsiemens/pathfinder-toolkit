package com.lateensoft.pathfinder.toolkit.views.character;

import java.util.List;
import java.util.Map.Entry;

import com.lateensoft.pathfinder.toolkit.PTMainActivity;
import com.lateensoft.pathfinder.toolkit.PTSharedPreferences;
import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.adapters.PTNavDrawerAdapter;
import com.lateensoft.pathfinder.toolkit.db.repository.PTCharacterRepository;
import com.lateensoft.pathfinder.toolkit.model.character.PTCharacter;
import com.lateensoft.pathfinder.toolkit.utils.EntryUtils;
import com.lateensoft.pathfinder.toolkit.views.PTBasePageFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

//This is a base class for all fragments in the character sheet activity
public abstract class PTCharacterSheetFragment extends PTBasePageFragment {
	private static final String TAG = PTCharacterSheetFragment.class.getSimpleName();
	
	public static final int TAB_INDEX_FLUFF = 0;
	public static final int TAB_INDEX_ABILITIES = 2;
	
	public long m_currentCharacterID;

	private PTCharacterRepository m_characterRepo;
	
	private int m_dialogMode;
	private long m_characterSelectedInDialog;
	
	private OnClickListener m_characterClickListener;
	
	private boolean m_isWaitingForResult = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		m_characterRepo = new PTCharacterRepository();
		
		setupCharacterSelectionDialogClickListener();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

    @Override
	public void updateTitle() {
		setTitle(m_characterRepo.queryName(m_currentCharacterID));
		setSubtitle(getFragmentTitle());
	}

	@Override
	public void onPause() {
		updateDatabase();
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (!m_isWaitingForResult) {
			loadCurrentCharacter();
			updateTitle();
		}
		m_isWaitingForResult = false;
	}
	
	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		super.startActivityForResult(intent, requestCode);
		m_isWaitingForResult = true;
	}

	/**
	 * Load the currently set character in shared prefs If there is no character
	 * set in user prefs, it automatically generates a new one.
	 */
	public void loadCurrentCharacter() {
		long currentCharacterID = PTSharedPreferences.getInstance()
				.getLong(PTSharedPreferences.KEY_LONG_SELECTED_CHARACTER_ID, -1);

		if (currentCharacterID == -1) { 
			// There was no current character set in shared prefs
			Log.v(TAG, "Default character add");
			addNewCharacter();
		} else {
			m_currentCharacterID = currentCharacterID;
			loadFromDatabase();
			if (getRootView() != null) {
				updateFragmentUI();
			}

		}
	}

	/**
	 * Generates a new character and sets it to the current character. Reloads
	 * the fragments.
	 */
	public void addNewCharacter() {
		PTCharacter newChar = new PTCharacter("New Adventurer", getActivity());
		long id = m_characterRepo.insert(newChar);
		if (id != -1) {
			PTSharedPreferences.getInstance().putLong(
					PTSharedPreferences.KEY_LONG_SELECTED_CHARACTER_ID, id);
			Log.i(TAG, "Added new character");
		} else {
			Log.e(TAG, "Error occurred creating new character");
			Toast.makeText(getActivity(), "Error creating new character. Please contact developers for support if issue persists.", Toast.LENGTH_LONG).show();
		}
		performUpdateReset();
	}

	/**
	 * Deletes the current character and loads the first in the list, or creates
	 * a new blank one, if there was only one.
	 */
	public void deleteCurrentCharacter() {
		int currentCharacterIndex = 0;
		List<Entry<Long, String>> characters = m_characterRepo.queryList();
		long characterForDeletion = m_currentCharacterID;

		for (int i = 0; i < characters.size(); i++) {
			if (characterForDeletion == characters.get(i).getKey().longValue()){
				currentCharacterIndex = i;
				break;
			}
		}

		if (characters.size() == 1) {
			addNewCharacter();
		} else {
			int charToSelect = (currentCharacterIndex == 0) ? 1 : 0;
			PTSharedPreferences.getInstance().putLong(
					PTSharedPreferences.KEY_LONG_SELECTED_CHARACTER_ID, characters.get(charToSelect).getKey().longValue());
			loadCurrentCharacter();
		}

		m_characterRepo.delete(characterForDeletion);
		Log.i(TAG, "Deleted current character: " + characterForDeletion);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
				case R.id.mi_character_list: 
					// Character list button
					m_dialogMode = R.id.mi_character_list;
					showCharacterDialog();
					break;
				case R.id.mi_new_character:
					// Add new character
					m_dialogMode = R.id.mi_new_character;
					showCharacterDialog();
					break;
				case R.id.mi_delete_character:
					// Delete character
					m_dialogMode = R.id.mi_delete_character;
					showCharacterDialog();
					break;
			}
		

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.character_sheet_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	private void showCharacterDialog() {
		m_characterSelectedInDialog = m_currentCharacterID; // current character

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		

		switch (m_dialogMode) {
		case R.id.mi_character_list:
			builder.setTitle(getString(R.string.select_character_dialog_header));

			List<Entry<Long, String>> characterEntries = m_characterRepo.queryList();
			String[] characterNames = EntryUtils.valueArray(characterEntries);
			int currentCharacterIndex = 0;

			for (int i = 0; i < characterEntries.size(); i++) {
				if (m_characterSelectedInDialog == characterEntries.get(i).getKey().longValue())
					currentCharacterIndex = i;
			}

			builder.setSingleChoiceItems(characterNames, currentCharacterIndex,
					m_characterClickListener)
					.setPositiveButton(R.string.ok_button_text, m_characterClickListener)
					.setNegativeButton(R.string.cancel_button_text, m_characterClickListener);
			break;

		case R.id.mi_new_character:
			builder.setTitle(getString(R.string.menu_item_new_character));
			builder.setMessage(getString(R.string.new_character_dialog_message))
					.setPositiveButton(R.string.ok_button_text, m_characterClickListener)
					.setNegativeButton(R.string.cancel_button_text, m_characterClickListener);
			break;

		case R.id.mi_delete_character:
			builder.setTitle(getString(R.string.menu_item_delete_character));
			builder.setMessage(
					getString(R.string.delete_character_dialog_message_1)
							+ m_characterRepo.queryName(m_currentCharacterID)
							+ getString(R.string.delete_character_dialog_message_2))
					.setPositiveButton(R.string.delete_button_text, m_characterClickListener)
					.setNegativeButton(R.string.cancel_button_text, m_characterClickListener);
			break;

		}
		AlertDialog alert = builder.create();
		alert.show();
	}

	private void setupCharacterSelectionDialogClickListener() {
		m_characterClickListener = new OnClickListener() {
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
					m_characterSelectedInDialog = m_characterRepo.queryList().get(selection).getKey();
					Log.v(TAG, "Selected character " + m_characterSelectedInDialog);
					break;

				}
			}
		};
	}

	/**
	 * Called when dialog positive button is tapped
	 */
	public void performPositiveDialogAction() {
		switch (m_dialogMode) {
		case R.id.mi_character_list:
			// Check if "currently selected" character is the same as saved one
			if (m_characterSelectedInDialog != m_currentCharacterID) {
				updateDatabase();

				PTSharedPreferences.getInstance().putLong(
						PTSharedPreferences.KEY_LONG_SELECTED_CHARACTER_ID, m_characterSelectedInDialog);
				loadCurrentCharacter();
				updateTitle();
			}
			break;

		case R.id.mi_new_character:
			performUpdateReset();
			addNewCharacter();
			break;

		case R.id.mi_delete_character:
			deleteCurrentCharacter();
			break;

		}

	}

	/**
	 * Depending on the use, this forces the current tab to save its values to
	 * mCharacter, and updates them. Ends with current tab set to Fluff.
	 */
	public void performUpdateReset() {
		((PTMainActivity) getActivity()).showView(PTNavDrawerAdapter.FLUFF_ID);
	}

	public long getCurrentCharacterID() {
		return m_currentCharacterID;
	}
	
	public PTCharacterRepository getCharacterRepo() {
		return m_characterRepo;
	}

	/**
	 * Refreshes the UI. Is automatically called onResume
	 */
	public abstract void updateFragmentUI();
	
	/**
	 * @return The title of the fragment instance
	 */
	public abstract String getFragmentTitle();
	
	/**
	 * Called to have the subclass update any relevant parts of the database.
	 * Called onPause, among other areas.
	 */
	public abstract void updateDatabase();
	
	/**
	 * Called to notify the base class that it should load its data from the database.
	 * Called onResume
	 */
	public abstract void loadFromDatabase();

}
