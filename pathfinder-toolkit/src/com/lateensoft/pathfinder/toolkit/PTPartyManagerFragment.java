package com.lateensoft.pathfinder.toolkit;

import com.lateensoft.pathfinder.toolkit.datahelpers.PTDatabaseManager;
import com.lateensoft.pathfinder.toolkit.datahelpers.PTSharedPreferences;
import com.lateensoft.pathfinder.toolkit.party.PTParty;
import com.lateensoft.pathfinder.toolkit.party.PTPartyMember;
import com.lateensoft.pathfinder.toolkit.party.PTPartyMemberEditorActivity;
import com.lateensoft.pathfinder.toolkit.views.character.PTCharacterSpellEditActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class PTPartyManagerFragment extends PTBasePageFragment implements
		OnClickListener, OnItemClickListener {
	private static final String TAG = PTPartyManagerFragment.class.getSimpleName();
	
	private final int MENU_ITEM_PARTY_LIST = 0;
	private final int MENU_ITEM_ADD_MEMBER = 1;
	private final int MENU_ITEM_NEW_PARTY = 2;
	private final int MENU_ITEM_DELETE_PARTY = 3;
	private final int DIALOG_MODE_ADD_MEMBER = 4;

	public PTParty mParty;

	private PTDatabaseManager mSQLManager;

	private int mDialogMode;
	private int mPartySelectedInDialog;

	private EditText mPartyNameEditText;
	private ListView mPartyMemberList;
	
	private int m_partyMemberSelectedForEdit;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		setRootView(inflater.inflate(R.layout.fragment_party_manager,
				container, false));
		setTitle(R.string.title_activity_party_manager);
		setSubtitle(null);

		mSQLManager = new PTDatabaseManager(getActivity());

		mPartyNameEditText = (EditText) getRootView()
				.findViewById(R.id.editTextPartyName);

		mPartyMemberList = (ListView) getRootView()
				.findViewById(R.id.listViewPartyMembers);
		mPartyMemberList.setOnItemClickListener(this);

		loadCurrentParty();
		return getRootView();
	}
	
	@Override
	public void onPause() {
		updateDatabase();
		super.onPause();
	}

	/**
	 * Load the currently set party in shared prefs If there is no party set in
	 * user prefs, it automatically generates a new one.
	 */
	private void loadCurrentParty() {
		int currentPartyID = PTSharedPreferences.getSharedInstance().getSelectedParty();

		if (currentPartyID == -1) { // There was no current party set in shared
									// prefs
			addNewParty();
		} else {
			mParty = mSQLManager.getParty(currentPartyID);
			refreshPartyView();

		}
	}

	/**
	 * Generates a new party and sets it to the current party.
	 */
	private void addNewParty() {
		mParty = mSQLManager.addNewParty("New Party");
		PTSharedPreferences.getSharedInstance().setSelectedParty(mParty.mID);
		refreshPartyView();
	}

	/**
	 * Deletes the current party and loads the first in the list, or creates a
	 * new blank one, if there was only one.
	 */
	private void deleteCurrentParty() {
		int currentPartyIndex = 0;
		int currentPartyID = mParty.mID;
		int partyIDs[] = mSQLManager.getPartyIDs();

		for (int i = 0; i < partyIDs.length; i++) {
			if (currentPartyID == partyIDs[i])
				currentPartyIndex = i;
		}

		if (partyIDs.length == 1) {
			addNewParty();
		} else if (currentPartyIndex == 0) {
			PTSharedPreferences.getSharedInstance().setSelectedParty(partyIDs[1]);
			loadCurrentParty();
		} else {
			PTSharedPreferences.getSharedInstance().setSelectedParty(partyIDs[0]);
			loadCurrentParty();
		}

		mSQLManager.deleteParty(currentPartyID);
	}

	private void updateDatabase() {
		mParty.setName(mPartyNameEditText.getText().toString());
		mSQLManager.updateParty(mParty);
	}

	private void refreshPartyView() {
		mPartyNameEditText.setText(mParty.getName());
		String[] memberNames = mParty.getPartyMemberNames();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, memberNames);
		mPartyMemberList.setAdapter(adapter);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		updateDatabase();

		switch (item.getItemId()) {
		case MENU_ITEM_PARTY_LIST: // Tapped party list button
			mDialogMode = MENU_ITEM_PARTY_LIST;
			showPartyDialog();
			break;
		case MENU_ITEM_ADD_MEMBER:
			mDialogMode = DIALOG_MODE_ADD_MEMBER;
			showPartyDialog();
			break;
		case MENU_ITEM_NEW_PARTY:
			// Add new party
			mDialogMode = MENU_ITEM_NEW_PARTY;
			showPartyDialog();
			break;
		case MENU_ITEM_DELETE_PARTY:
			// Delete party
			mDialogMode = MENU_ITEM_DELETE_PARTY;
			showPartyDialog();
			break;
		}

		super.onOptionsItemSelected(item);
		return true;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuItem partyListItem = menu.add(Menu.NONE, MENU_ITEM_PARTY_LIST,
				Menu.NONE, R.string.menu_item_party_list);
		partyListItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

		MenuItem addMemberListItem = menu.add(Menu.NONE, MENU_ITEM_ADD_MEMBER,
				Menu.NONE, R.string.menu_item_party_list);
		addMemberListItem.setIcon(R.drawable.ic_action_new);
		addMemberListItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

		MenuItem newPartyItem = menu.add(Menu.NONE, MENU_ITEM_NEW_PARTY,
				Menu.NONE, R.string.menu_item_new_party);
		newPartyItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

		MenuItem deletePartyItem = menu.add(Menu.NONE, MENU_ITEM_DELETE_PARTY,
				Menu.NONE, R.string.menu_item_delete_party);
		deletePartyItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

		super.onCreateOptionsMenu(menu);
		return true;
	}

	private void showPartyDialog() {
		mPartySelectedInDialog = mParty.mID; // actual current party

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		switch (mDialogMode) {
		case MENU_ITEM_PARTY_LIST:
			builder.setTitle("Select Party");

			String[] partyList = mSQLManager.getPartyNames();
			int partyIDs[] = mSQLManager.getPartyIDs();
			int currentPartyIndex = 0;

			for (int i = 0; i < partyIDs.length; i++) {
				if (mPartySelectedInDialog == partyIDs[i])
					currentPartyIndex = i;
			}

			builder.setSingleChoiceItems(partyList, currentPartyIndex, this)
					.setPositiveButton(R.string.ok_button_text, this)
					.setNegativeButton(R.string.cancel_button_text, this);
			break;

		case MENU_ITEM_NEW_PARTY:
			builder.setTitle(getString(R.string.menu_item_new_party));
			builder.setMessage("Create new party?")
					.setPositiveButton(R.string.ok_button_text, this)
					.setNegativeButton(R.string.cancel_button_text, this);
			break;

		case MENU_ITEM_DELETE_PARTY:
			builder.setTitle(getString(R.string.menu_item_delete_party));
			builder.setMessage(
					getString(R.string.delete_character_dialog_message_1)
							+ mParty.getName()
							+ getString(R.string.delete_character_dialog_message_2))
					.setPositiveButton(R.string.delete_button_text, this)
					.setNegativeButton(R.string.cancel_button_text, this);
			break;

		case DIALOG_MODE_ADD_MEMBER:
			builder.setTitle(getString(R.string.new_party_member_dialog_title));
			builder.setMessage(getString(R.string.new_party_member_dialog_text))
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
		default:
			// Set the currently selected party in the dialog
			mPartySelectedInDialog = mSQLManager.getPartyIDs()[selection];
			break;

		}
	}

	/**
	 * Called when dialog positive button is tapped
	 */
	private void performPositiveDialogAction() {
		switch (mDialogMode) {
		case MENU_ITEM_PARTY_LIST:
			// Check if "currently selected" party is the same as saved one
			if (mPartySelectedInDialog != mParty.mID) {
				updateDatabase(); // Ensures any data changed on the party
										// in the current fragment is saved
				PTSharedPreferences.getSharedInstance().setSelectedParty(mPartySelectedInDialog);
				loadCurrentParty();
			}
			break;

		case MENU_ITEM_NEW_PARTY:
			updateDatabase();
			addNewParty();
			break;

		case MENU_ITEM_DELETE_PARTY:
			deleteCurrentParty();
			break;

		case DIALOG_MODE_ADD_MEMBER:
			m_partyMemberSelectedForEdit = -1;
			showPartyMemberEditor(null);
			break;

		}

	}

	// Party member in list was clicked
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		m_partyMemberSelectedForEdit = position;
		showPartyMemberEditor(mParty.getPartyMember(position));

	}

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
}
