package com.lateensoft.pathfinder.toolkit;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.lateensoft.pathfinder.toolkit.datahelpers.PTDatabaseManager;
import com.lateensoft.pathfinder.toolkit.datahelpers.PTUserPrefsManager;
import com.lateensoft.pathfinder.toolkit.functional.PTDiceSet;
import com.lateensoft.pathfinder.toolkit.party.PTEncounterMemberEditorActivity;
import com.lateensoft.pathfinder.toolkit.party.PTParty;
import com.lateensoft.pathfinder.toolkit.party.PTPartyMember;
import com.lateensoft.pathfinder.toolkit.party.PTPartyRollAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class PTInitiativeTrackerActivity extends SherlockActivity implements OnClickListener, OnItemClickListener, android.content.DialogInterface.OnClickListener {

	private final int MENU_ITEM_RESET = 0;
	private final int MENU_ITEM_ADD_MEMBER = 1;
	private final String TAG = "PTInitiativeTrackerActivity";
	
	public PTParty mParty;
	
	private PTDatabaseManager mSQLManager;
	private PTUserPrefsManager mUserPrefsManager;
	
	private int mDialogMode;
	private boolean mHasRolled;
	
	private Button mRollInitiativeButton;
	private ListView mPartyMemberList;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ptinitiative_tracker);
        
        ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		mSQLManager = new PTDatabaseManager(getApplicationContext());
		mUserPrefsManager = new PTUserPrefsManager(getApplicationContext());
		
		mRollInitiativeButton = (Button) findViewById(R.id.buttonRollInitiative);
		mRollInitiativeButton.setOnClickListener(this);
	
		mPartyMemberList = (ListView) findViewById(R.id.listViewEncounterMembers);	
		mPartyMemberList.setOnItemClickListener(this);
		
		mHasRolled = false;
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
			case MENU_ITEM_RESET: // Tapped party list button
				mDialogMode = MENU_ITEM_RESET;
				showPartyDialog();
				break;
			case MENU_ITEM_ADD_MEMBER:
				mDialogMode = MENU_ITEM_ADD_MEMBER;
				showPartyDialog();
				break;
			}
		}

		return true;

	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuItem partyListItem = menu.add(Menu.NONE,
				MENU_ITEM_RESET, Menu.NONE,
				R.string.menu_item_reset_encounter);
		partyListItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		
		MenuItem addMemberListItem = menu.add(Menu.NONE,
				MENU_ITEM_ADD_MEMBER, Menu.NONE,
				R.string.menu_item_add_encounter_member);
		addMemberListItem.setIcon(android.R.drawable.ic_menu_add);
		addMemberListItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		
		// Initialize the global menu items
		PTSharedMenu.onCreateOptionsMenu(menu, getApplicationContext());
        return true;
    }
    
	private void showPartyDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		switch(mDialogMode){
		case MENU_ITEM_RESET:
			builder.setTitle(getString(R.string.reset_encounter_dialog_title));
			builder.setMessage(getString(R.string.reset_encounter_dialog_text))
				.setPositiveButton(R.string.ok_button_text, this)
				.setNegativeButton(R.string.cancel_button_text, this)
				.setNeutralButton(getString(R.string.use_defualt_party_button), this);
			break;
	
			
		case MENU_ITEM_ADD_MEMBER:
			builder.setTitle(getString(R.string.new_encounter_member_dialog_title));
			builder.setMessage(getString(R.string.new_encounter_member_dialog_text))
				.setPositiveButton(R.string.ok_button_text, this)
				.setNegativeButton(R.string.cancel_button_text, this);
			break;
			
		}
			AlertDialog alert = builder.create();
			alert.show();
	}
	
	// Click method for the party selection dialog
	public void onClick(DialogInterface dialogInterface, int selection) {
		switch (selection) {
		case DialogInterface.BUTTON_POSITIVE:	
				performPositiveDialogAction();
			break;
		case DialogInterface.BUTTON_NEGATIVE:
			break;
		case DialogInterface.BUTTON_NEUTRAL:
			loadDefaultParty();
		}
	}
		
	/**
	 * Called when dialog positive button is tapped
	 */
	public void performPositiveDialogAction(){
		switch (mDialogMode){
		case MENU_ITEM_RESET:
			resetPartyRolls();
			break;
			
		case MENU_ITEM_ADD_MEMBER:
			launchPartyMemberEditor(mParty.addPartyMember(new PTPartyMember("NPC")));
			refreshPartyView();
			break;
			
		}
		
	}
	
    
    /**
	 * Load the current encounter party in shared prefs
	 * If there is no party set in user prefs, it pulls the default currently set in party manager
	 * If there is not current party, an empty party is set
	 */
	public void loadEncounterParty(){
		
		PTParty currentEncounterParty = mUserPrefsManager.getEncounterParty();
		//If there is no saved encounter party, get from party manager
		//Also, if the encounter party was saved, but previously was empty, get from party manager.
		if(currentEncounterParty == null || currentEncounterParty.size() == 0){ 
			loadDefaultParty();
		}
		else{
			mParty = currentEncounterParty;
			mHasRolled = false;
			for(int i = 0; i < mParty.size(); i++)
				if(mParty.getPartyMember(i).getRolledValue() != 0)
					mHasRolled = true;
					
			refreshPartyView();	
		}
	}
	
	public void loadDefaultParty(){
		int currentPartyID = mUserPrefsManager.getSelectedParty();
		if(currentPartyID >= 0)
			mParty = mSQLManager.getParty(currentPartyID);
		else
			mParty = new PTParty("Empty Party");
		mUserPrefsManager.setEncounterParty(mParty);
		mHasRolled = false;
		refreshPartyView();
	}
	
	public void resetPartyRolls(){
		for(int i = 0; i < mParty.size(); i++){
			mParty.getPartyMember(i).setRolledValue(0);
		}
		mUserPrefsManager.setEncounterParty(mParty);
		mHasRolled = false;
		refreshPartyView();
	}
	
	private void refreshPartyView(){
		String[] memberNames = mParty.getNamesByRollValue();
		int[] memberRollValues = mParty.getRollValuesByRollValue();
		PTPartyRollAdapter adapter = new PTPartyRollAdapter(this, R.layout.party_roll_row, memberNames, memberRollValues, null);
		mPartyMemberList.setAdapter(adapter);	
		mRollInitiativeButton.setEnabled(!mHasRolled);
	}

	/**
	 * launches the party member editor
	 * @param index (the actual index, not the roll value index)
	 */
	public void launchPartyMemberEditor(int index){
		Intent intent = new Intent(this, PTEncounterMemberEditorActivity.class);
		//putting values to intent
		intent.putExtra(getString(R.string.party_member_index_key), index);
		startActivity(intent);
	}
	
	//When roll initiative button is clicked
	public void onClick(View view) {
		PTDiceSet diceSet = new PTDiceSet();
		int initiativeMod;
		
		for(int i = 0; i < mParty.size(); i++){
			initiativeMod = mParty.getPartyMember(i).getInitiative();
			mParty.getPartyMember(i).setRolledValue(diceSet.singleRoll(20)+initiativeMod);
		}
		mUserPrefsManager.setEncounterParty(mParty);
		mHasRolled = true;
		loadEncounterParty();
	}

	//Party member in list was clicked
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		launchPartyMemberEditor(mParty.getPartyMemberIndexByRollValueIndex(position));
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		loadEncounterParty();
	}
	
	@Override
	protected void onPause() {
		mUserPrefsManager.setEncounterParty(mParty);
		super.onPause();
	}
}
