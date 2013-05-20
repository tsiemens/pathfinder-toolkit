package com.lateensoft.pathfinder.toolkit;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.lateensoft.pathfinder.toolkit.character.PTCharacter;
import com.lateensoft.pathfinder.toolkit.character.PTCharacterAbilityFragment;
import com.lateensoft.pathfinder.toolkit.character.PTCharacterCombatStatsFragment;
import com.lateensoft.pathfinder.toolkit.character.PTCharacterFeatsFragment;
import com.lateensoft.pathfinder.toolkit.character.PTCharacterFluffFragment;
import com.lateensoft.pathfinder.toolkit.character.PTCharacterInventoryFragment;
import com.lateensoft.pathfinder.toolkit.character.PTCharacterSheetFragment;
import com.lateensoft.pathfinder.toolkit.character.PTCharacterSkillsFragment;
import com.lateensoft.pathfinder.toolkit.character.PTCharacterSpellBookFragment;
import com.lateensoft.pathfinder.toolkit.character.PTArmorFragment;
import com.lateensoft.pathfinder.toolkit.character.PTSkillsAdapter;
import com.lateensoft.pathfinder.toolkit.character.PTWeaponsFragment;
import com.lateensoft.pathfinder.toolkit.datahelpers.PTDatabaseManager;
import com.lateensoft.pathfinder.toolkit.datahelpers.PTUserPrefsManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

//Currently an alternative to PTCharacterSheet, using the actionbar with tabs
public class PTCharacterSheetActivity extends SherlockFragmentActivity implements
		DialogInterface.OnClickListener, ActionBar.TabListener {

	private final int MENU_ITEM_CHARACTER_LIST = 0;
	private final int MENU_ITEM_NEW_CHARACTER = 1;
	private final int MENU_ITEM_DELETE_CHARACTER = 2;
	private final int MENU_ITEM_AUTOFILL = 3;
	
	private static final int STR_KEY = 0;
	static final int DEX_KEY = 1;
	static final int CON_KEY = 2;
	static final int INT_KEY = 3;
	static final int WIS_KEY = 4;
	static final int CHA_KEY = 5;
	
	static final int FORT_KEY = 0;
	static final int REF_KEY = 1;
	static final int WILL_KEY = 2;
	
	static final int TAB_INDEX_FLUFF = 0;
	static final int TAB_INDEX_ABILITIES = 2;

	private final String TAG = "PTCharacterSheetActivity";

	private ActionBar mActionBar;
	private PTCharacterSheetFragment mCurrentFragment;
	public PTCharacter mCharacter;

	private PTDatabaseManager mSQLManager;
	private PTUserPrefsManager mUserPrefsManager;

	private int mDialogMode;
	private int mCharacterSelectedInDialog;

	private boolean isPerformingOnCreate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isPerformingOnCreate = true;
		// Notice that setContentView() is not used, because we use the root
		// android.R.id.content as the container for each fragment
		// setup action bar for tabs
		mActionBar = getSupportActionBar();
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		mActionBar.setDisplayHomeAsUpEnabled(true);

		mSQLManager = new PTDatabaseManager(getApplicationContext());
		mUserPrefsManager = new PTUserPrefsManager(this.getApplicationContext());

		loadCurrentCharacter();
		mCharacterSelectedInDialog = mCharacter.mID;

		isPerformingOnCreate = false;
	}

	private void setUpTabs() {
		// Fluff
		Tab tab = mActionBar
				.newTab()
				.setText(R.string.tab_character_fluff)
				.setTabListener(this);
		mActionBar.addTab(tab);

		// Combat Stats
		tab = mActionBar
				.newTab()
				.setText(R.string.tab_character_combat_stats)
				.setTabListener(this);
		mActionBar.addTab(tab);

		// Abilities
		tab = mActionBar
				.newTab()
				.setText(R.string.tab_character_abilities)
				.setTabListener(this);
		mActionBar.addTab(tab);

		// Skills
		tab = mActionBar
				.newTab()
				.setText(R.string.tab_character_skills)
				.setTabListener(this);
		mActionBar.addTab(tab);

		// Inventory
		tab = mActionBar
				.newTab()
				.setText(R.string.tab_character_inventory)
				.setTabListener(this);
		mActionBar.addTab(tab);

		// Armor
		tab = mActionBar
				.newTab()
				.setText(R.string.tab_character_armor)
				.setTabListener(this);
		mActionBar.addTab(tab);

		// Weapons
		tab = mActionBar
				.newTab()
				.setText(R.string.tab_character_weapons)
				.setTabListener(this);
		mActionBar.addTab(tab);

		// Feats
		tab = mActionBar
				.newTab()
				.setText(R.string.tab_character_feats)
				.setTabListener(this);
		mActionBar.addTab(tab);

		// Spells
		tab = mActionBar
				.newTab()
				.setText(R.string.tab_character_spells)
				.setTabListener(this);
		mActionBar.addTab(tab);
	}
	
	

	/**
	 * Load the currently set character in shared prefs If there is no character
	 * set in user prefs, it automatically generates a new one.
	 */
	public void loadCurrentCharacter() {
		int currentCharacterID = mUserPrefsManager.getSelectedCharacter();

		if (currentCharacterID == -1) { // There was no current character set in
										// shared prefs
			Log.v(TAG, "Default character add");
			addNewCharacter();
		} else {
			Log.v(TAG, "Loading character");
			mCharacter = mSQLManager.getCharacter(currentCharacterID);
			forceFragmentSaveUpdate();

		}
		Log.v(TAG, "Loaded character: " + mCharacter.mID);
	}

	/**
	 * Generates a new character and sets it to the current character. Reloads
	 * the fragments.
	 */
	public void addNewCharacter() {
		mCharacter = mSQLManager.addNewCharacter("New Adventurer",
				this.getApplicationContext());
		mUserPrefsManager.setSelectedCharacter(mCharacter.mID);
		forceFragmentSaveUpdate();
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
			mUserPrefsManager.setSelectedCharacter(characterIDs[1]);
			loadCurrentCharacter();
		} else {
			mUserPrefsManager.setSelectedCharacter(characterIDs[0]);
			loadCurrentCharacter();
		}

		mSQLManager.deleteCharacter(currentCharacterID);
		Log.v(TAG, "Deleted current character: " + currentCharacterID);
	}

	public void updateCharacterDatabase() {
		mSQLManager.updateCharacter(mCharacter);
	}

	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (PTSharedMenu.onOptionsItemSelected(item, this) == false) {
			// handle local menu items here

			switch (item.getItemId()) {
			case android.R.id.home: // Tapped the back button on the action bar,
									// to
									// return to main menu
				finish();
				break;
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
				int selectedTab = mActionBar.getSelectedNavigationIndex();
				//forceFragmentSaveUpdate();
				
				/*if(selectedTab == 0) {
					mActionBar.selectTab(mActionBar.getTabAt(TAB_INDEX_ABILITIES));
				} else {
					mActionBar.selectTab(mActionBar.getTabAt(TAB_INDEX_FLUFF));
				}*/
				mActionBar.selectTab(mActionBar.getTabAt(TAB_INDEX_FLUFF));
				autoFill();
				mActionBar.selectTab(mActionBar.getTabAt(selectedTab));
				break;
			}
		}

		return super.onOptionsItemSelected(item);
	}

	private void autoFill() {
		mCharacter.getCombatStatSet().setACDexMod(mCharacter.getAbilitySet()
				.getAbilityScore(DEX_KEY).getModifier());
		mCharacter.getCombatStatSet().setInitDexMod(mCharacter.getAbilitySet()
				.getAbilityScore(DEX_KEY).getModifier());
		mCharacter.getCombatStatSet().setCMDDexMod(mCharacter.getAbilitySet()
				.getAbilityScore(DEX_KEY).getModifier());
		mCharacter.getSaveSet().getSave(REF_KEY).setAbilityMod(mCharacter.getAbilitySet()
				.getAbilityScore(DEX_KEY).getModifier());
		
		mCharacter.getCombatStatSet().setStrengthMod(mCharacter.getAbilitySet()
				.getAbilityScore(STR_KEY).getModifier());
		
		mCharacter.getSaveSet().getSave(FORT_KEY).setAbilityMod(mCharacter.getAbilitySet()
				.getAbilityScore(CON_KEY).getModifier());
		
		mCharacter.getSaveSet().getSave(WILL_KEY).setAbilityMod(mCharacter.getAbilitySet()
				.getAbilityScore(WIS_KEY).getModifier());
		
		int key;
		
		for(int i = 0; i < mCharacter.getSkillSet().getSkills().length; i++) {
			key = mCharacter.getSkillSet().getSkill(i).getKeyAbilityKey();
			mCharacter.getSkillSet().getSkill(i).setAbilityMod(
					mCharacter.getAbilitySet().getAbilityScore(key).getModifier());
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Initialize the global menu items

		MenuItem characterListItem = menu.add(Menu.NONE,
				MENU_ITEM_CHARACTER_LIST, Menu.NONE,
				R.string.menu_item_character_list);
		characterListItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

		MenuItem newCharacterItem = menu.add(Menu.NONE,
				MENU_ITEM_NEW_CHARACTER, Menu.NONE,
				R.string.menu_item_new_character);
		newCharacterItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

		MenuItem deleteCharacterItem = menu.add(Menu.NONE,
				MENU_ITEM_DELETE_CHARACTER, Menu.NONE,
				R.string.menu_item_delete_character);
		deleteCharacterItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		
		
		MenuItem autoFillItem = menu.add(Menu.NONE,
				MENU_ITEM_AUTOFILL, Menu.NONE,
				R.string.auto_fill);
		autoFillItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
			
		

		PTSharedMenu.onCreateOptionsMenu(menu, getApplicationContext());

		return true;
	}

	private void showCharacterDialog() {
		mCharacterSelectedInDialog = mCharacter.mID; // actual current character

		AlertDialog.Builder builder = new AlertDialog.Builder(this);

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
					this).setPositiveButton(R.string.ok_button_text, this)
					.setNegativeButton(R.string.cancel_button_text, this);
			break;

		case MENU_ITEM_NEW_CHARACTER:
			builder.setTitle(getString(R.string.menu_item_new_character));
			builder.setMessage("Create new character?")
					.setPositiveButton(R.string.ok_button_text, this)
					.setNegativeButton(R.string.cancel_button_text, this);
			break;

		case MENU_ITEM_DELETE_CHARACTER:
			builder.setTitle(getString(R.string.menu_item_delete_character));
			builder.setMessage(
					getString(R.string.delete_character_dialog_message_1)
							+ mCharacter.getName()
							+ getString(R.string.delete_character_dialog_message_2))
					.setPositiveButton(R.string.delete_button_text, this)
					.setNegativeButton(R.string.cancel_button_text, this);
			break;
			

		}
		AlertDialog alert = builder.create();
		alert.show();
	}

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

	/**
	 * Called when dialog positive button is tapped
	 */
	public void performPositiveDialogAction() {
		switch (mDialogMode) {
		case MENU_ITEM_CHARACTER_LIST:
			// Check if "currently selected" character is the same as saved one
			if (mCharacterSelectedInDialog != mCharacter.mID) {
				forceFragmentSaveUpdate();
				updateCharacterDatabase(); // Ensures any data changed on the
											// character in the current fragment
											// is saved
				mUserPrefsManager
						.setSelectedCharacter(mCharacterSelectedInDialog);
				loadCurrentCharacter();
			}
			break;

		case MENU_ITEM_NEW_CHARACTER:
			forceFragmentSaveUpdate();
			updateCharacterDatabase();
			addNewCharacter();
			break;

		case MENU_ITEM_DELETE_CHARACTER:
			forceFragmentSaveUpdate();
			deleteCurrentCharacter();
			break;

		}

	}

	/**
	 * Depending on the use, this forces the current tab to save its values to
	 * mCharacter, and updates them. Ends with current tab set to Fluff.
	 */
	public void forceFragmentSaveUpdate() {
		if (!isPerformingOnCreate) {
			// Forces resume of first tab, even if on it already. Should be the least resource consuming tab.
			mActionBar.selectTab(mActionBar.getTabAt(7));
			mActionBar.selectTab(mActionBar.getTabAt(0));
		}
	}

	public PTCharacter getCurrentCharacter() {
		return mCharacter;
	}

	public void setCurrentCharacter(PTCharacter character) {
		if (character != null)
			mCharacter = character;
	}

	@Override
	protected void onResume() {
		super.onResume();
		setUpTabs();
		PTUserPrefsManager userPrefs = new PTUserPrefsManager(this);
		mActionBar.selectTab(mActionBar.getTabAt(userPrefs.getLastCharacterTab()));
		isPerformingOnCreate = false;
	}

	@Override
	protected void onPause() {
		PTUserPrefsManager userPrefs = new PTUserPrefsManager(this);
		userPrefs.setLastCharacterTab(mActionBar.getSelectedNavigationIndex());
		mActionBar.removeAllTabs(); // Used to prevent fragments from detaching
									// when app gets killed
		super.onPause();
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		PTCharacterSheetFragment newFrag = null;
		switch (tab.getPosition()) {
		case 0:
			newFrag = new PTCharacterFluffFragment();
			break;
		case 1:
			newFrag = new PTCharacterCombatStatsFragment();
			break;
		case 2:
			newFrag = new PTCharacterAbilityFragment();
			break;
		case 3:
			newFrag = new PTCharacterSkillsFragment();
			break;
		case 4:
			newFrag = new PTCharacterInventoryFragment();
			break;
		case 5:
			newFrag = new PTArmorFragment();
			break;
		case 6:
			newFrag = new PTWeaponsFragment();
			break;
		case 7:
			newFrag = new PTCharacterFeatsFragment();
			break;
		case 8:
			newFrag = new PTCharacterSpellBookFragment();
			break;
		default:
			break;
		}
		
		if (newFrag != null) {
			ft.replace(android.R.id.content, newFrag);
			mCurrentFragment = newFrag;
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// do any saving with Current fragment
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// Do nothing
		
	}

}
