package com.lateensoft.pathfinder.toolkit.views.party;

import com.lateensoft.pathfinder.toolkit.PTSharedPreferences;
import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.R.drawable;
import com.lateensoft.pathfinder.toolkit.R.id;
import com.lateensoft.pathfinder.toolkit.R.layout;
import com.lateensoft.pathfinder.toolkit.R.string;
import com.lateensoft.pathfinder.toolkit.adapters.party.PTPartyRollAdapter;
import com.lateensoft.pathfinder.toolkit.db.PTDatabaseManager;
import com.lateensoft.pathfinder.toolkit.model.party.PTParty;
import com.lateensoft.pathfinder.toolkit.model.party.PTPartyMember;
import com.lateensoft.pathfinder.toolkit.utils.PTDiceSet;
import com.lateensoft.pathfinder.toolkit.views.PTBasePageFragment;
import com.lateensoft.pathfinder.toolkit.views.character.PTCharacterSpellEditActivity;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class PTInitiativeTrackerFragment extends PTBasePageFragment implements
		OnClickListener, OnItemClickListener,
		android.content.DialogInterface.OnClickListener {
	private static final String TAG = PTInitiativeTrackerFragment.class.getSimpleName();

	private static final int MENU_ITEM_RESET = 0;
	private static final int MENU_ITEM_ADD_MEMBER = 1;

	public PTParty mParty;

	private PTDatabaseManager mSQLManager;

	private int mDialogMode;
	private boolean mHasRolled;

	private Button mRollInitiativeButton;
	private ListView mPartyMemberList;
	
	private int m_partyMemberSelectedForEdit;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		setRootView(inflater.inflate(R.layout.fragment_initiative_tracker, container, false));
		setTitle(R.string.title_activity_initiative_tracker);

		mSQLManager = new PTDatabaseManager(getActivity());

		mRollInitiativeButton = (Button) getRootView().findViewById(R.id.buttonRollInitiative);
		mRollInitiativeButton.setOnClickListener(this);

		mPartyMemberList = (ListView) getRootView().findViewById(R.id.listViewEncounterMembers);
		mPartyMemberList.setOnItemClickListener(this);

		mHasRolled = false;
		
		loadEncounterParty();
		return getRootView();
	}
	
	@Override
	public void onPause() {
		updateDatabase();
		super.onPause();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case MENU_ITEM_RESET: // Tapped party list button
			mDialogMode = MENU_ITEM_RESET;
			showPartyDialog();
			break;
		case MENU_ITEM_ADD_MEMBER:
			mDialogMode = MENU_ITEM_ADD_MEMBER;
			showPartyDialog();
			break;
		}

		super.onOptionsItemSelected(item);
		return true;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem partyListItem = menu.add(Menu.NONE, MENU_ITEM_RESET,
				Menu.NONE, R.string.menu_item_reset_encounter);
		partyListItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

		MenuItem addMemberListItem = menu.add(Menu.NONE, MENU_ITEM_ADD_MEMBER,
				Menu.NONE, R.string.menu_item_add_encounter_member);
		addMemberListItem.setIcon(R.drawable.ic_action_new);
		addMemberListItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

		super.onCreateOptionsMenu(menu);
		return true;
	}

	private void showPartyDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		switch (mDialogMode) {
		case MENU_ITEM_RESET:
			builder.setTitle(getString(R.string.reset_encounter_dialog_title));
			builder.setMessage(getString(R.string.reset_encounter_dialog_text))
					.setPositiveButton(R.string.ok_button_text, this)
					.setNegativeButton(R.string.cancel_button_text, this)
					.setNeutralButton(
							getString(R.string.use_defualt_party_button), this);
			break;

		case MENU_ITEM_ADD_MEMBER:
			builder.setTitle(getString(R.string.new_encounter_member_dialog_title));
			builder.setMessage(
					getString(R.string.new_encounter_member_dialog_text))
					.setPositiveButton(R.string.ok_button_text, this)
					.setNegativeButton(R.string.cancel_button_text, this);
			break;

		}
		AlertDialog alert = builder.create();
		alert.show();
	}

	// Click method for the party selection dialog
	@Override
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
	private void performPositiveDialogAction() {
		switch (mDialogMode) {
		case MENU_ITEM_RESET:
			resetPartyRolls();
			break;

		case MENU_ITEM_ADD_MEMBER:
			m_partyMemberSelectedForEdit = -1;
			showPartyMemberEditor(null);
			break;

		}

	}

	/**
	 * Load the current encounter party in shared prefs If there is no party set
	 * in user prefs, it pulls the default currently set in party manager If
	 * there is not current party, an empty party is set
	 */
	private void loadEncounterParty() {
		PTParty currentEncounterParty = PTSharedPreferences.getInstance().getEncounterParty();
		// If there is no saved encounter party, get from party manager
		// Also, if the encounter party was saved, but previously was empty, get
		// from party manager.
		if (currentEncounterParty == null || currentEncounterParty.size() == 0) {
			loadDefaultParty();
		} else {
			mParty = currentEncounterParty;
			mHasRolled = false;
			for (int i = 0; i < mParty.size(); i++)
				if (mParty.getPartyMember(i).getLastRolledValue() != 0)
					mHasRolled = true;

			refreshPartyView();
		}
	}

	private void loadDefaultParty() {
		long currentPartyID = PTSharedPreferences.getInstance().getLong(
				PTSharedPreferences.KEY_LONG_SELECTED_PARTY_ID, -1);
		if (currentPartyID >= 0)
			mParty = mSQLManager.getParty(Long.valueOf(currentPartyID).intValue());
		else
			mParty = new PTParty("Empty Party");
		updateDatabase();
		mHasRolled = false;
		refreshPartyView();
	}

	private void resetPartyRolls() {
		for (int i = 0; i < mParty.size(); i++) {
			mParty.getPartyMember(i).setLastRolledValue(0);
		}
		updateDatabase();
		mHasRolled = false;
		refreshPartyView();
	}

	private void refreshPartyView() {
		String[] memberNames = mParty.getNamesByRollValue();
		int[] memberRollValues = mParty.getRollValuesByRollValue();
		PTPartyRollAdapter adapter = new PTPartyRollAdapter(getActivity(),
				R.layout.party_roll_row, memberNames, memberRollValues, null);
		mPartyMemberList.setAdapter(adapter);
		mRollInitiativeButton.setEnabled(!mHasRolled);
		setTitle(R.string.title_activity_initiative_tracker);
		setSubtitle(mParty.getName());
	}

	/**
	 * launches the party member editor
	 * 
	 * @param index
	 *            (the actual index, not the roll value index)
	 */
	private void showPartyMemberEditor(PTPartyMember member) {
		Intent intent = new Intent(getActivity(),
				PTPartyMemberEditorActivity.class);
		intent.putExtra(
				PTCharacterSpellEditActivity.INTENT_EXTRAS_KEY_EDITABLE_PARCELABLE, member);
		startActivityForResult(intent, 0);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case Activity.RESULT_OK:
			PTPartyMember member = data.getExtras().getParcelable(
					PTPartyMemberEditorActivity.INTENT_EXTRAS_KEY_EDITABLE_PARCELABLE);
			Log.v(TAG, "Add/edit member OK: " + member.getName());
			if(m_partyMemberSelectedForEdit < 0) {
				Log.v(TAG, "Adding a member");
				if(member != null) {
					mParty.addPartyMember(member);
					refreshPartyView(); 
				}
			} else {
				Log.v(TAG, "Editing a member");
				mParty.setPartyMember(m_partyMemberSelectedForEdit, member);
				refreshPartyView();
			}
			
			break;
		
		case PTPartyMemberEditorActivity.RESULT_DELETE:
			Log.v(TAG, "Deleting a member");
			mParty.deletePartyMember(m_partyMemberSelectedForEdit);
			refreshPartyView();
			break;
		
		case Activity.RESULT_CANCELED:
			break;
		
		default:
			break;
		}
		updateDatabase();
		super.onActivityResult(requestCode, resultCode, data);
	}

	// When roll initiative button is clicked
	@Override
	public void onClick(View view) {
		PTDiceSet diceSet = new PTDiceSet();
		int initiativeMod;

		for (int i = 0; i < mParty.size(); i++) {
			initiativeMod = mParty.getPartyMember(i).getInitiative();
			mParty.getPartyMember(i).setLastRolledValue(
					diceSet.singleRoll(20) + initiativeMod);
		}
		updateDatabase();
		mHasRolled = true;
		loadEncounterParty();
	}

	// Party member in list was clicked
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		m_partyMemberSelectedForEdit = mParty
				.getPartyMemberIndexByRollValueIndex(position);
		showPartyMemberEditor(mParty.getPartyMember(m_partyMemberSelectedForEdit));

	}
	
	private void updateDatabase() {
		PTSharedPreferences.getInstance().setEncounterParty(mParty);
	}
}
