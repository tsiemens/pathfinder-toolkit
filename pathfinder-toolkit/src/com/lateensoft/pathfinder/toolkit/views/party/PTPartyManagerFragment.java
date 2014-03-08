package com.lateensoft.pathfinder.toolkit.views.party;

import java.util.List;
import java.util.Map.Entry;

import com.lateensoft.pathfinder.toolkit.PTSharedPreferences;
import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.db.repository.PTPartyMemberRepository;
import com.lateensoft.pathfinder.toolkit.db.repository.PTPartyRepository;
import com.lateensoft.pathfinder.toolkit.model.party.PTParty;
import com.lateensoft.pathfinder.toolkit.model.party.PTPartyMember;
import com.lateensoft.pathfinder.toolkit.utils.EntryUtils;
import com.lateensoft.pathfinder.toolkit.views.PTBasePageFragment;
import com.lateensoft.pathfinder.toolkit.views.PTParcelableEditorActivity;
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
import android.view.MenuInflater;
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
	
	public PTParty m_party;

	private int m_dialogMode;
	private long m_partyIDSelectedInDialog;

	private EditText m_partyNameEditText;
	private ListView m_partyMemberList;
	
	private int m_partyMemberIndexSelectedForEdit;
	
	private PTPartyRepository m_partyRepo;
	private PTPartyMemberRepository m_memberRepo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		m_partyRepo = new PTPartyRepository();
		m_memberRepo = new PTPartyMemberRepository();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		setRootView(inflater.inflate(R.layout.fragment_party_manager,
				container, false));

		m_partyNameEditText = (EditText) getRootView()
				.findViewById(R.id.editTextPartyName);

		m_partyMemberList = (ListView) getRootView()
				.findViewById(R.id.listViewPartyMembers);
		m_partyMemberList.setOnItemClickListener(this);

		loadCurrentParty();
		return getRootView();
	}
	
	@Override
	public void onPause() {
		updateDatabase();
		super.onPause();
	}

    @Override
    public void updateTitle() {
        setTitle(R.string.title_activity_party_manager);
        setSubtitle(null);
    }

	/**
	 * Load the currently set party in shared prefs If there is no party set in
	 * user prefs, it automatically generates a new one.
	 */
	private void loadCurrentParty() {
		long currentPartyID = PTSharedPreferences.getInstance().getLong(
				PTSharedPreferences.KEY_LONG_SELECTED_PARTY_ID, -1);

		if (currentPartyID == -1) {
			// There was no current party set in shared prefs
			addNewParty();
		} else {
			m_party = m_partyRepo.query(currentPartyID);
			if (m_party == null) {
				// Recovery for some kind of catastrophic failure.
				List<Entry<Long, String>> ids = m_partyRepo.queryList();
				for (int i = 0; i < ids.size(); i++) {
					m_party = m_partyRepo.query(ids.get(i).getKey().longValue());
					if (m_party != null) {
						PTSharedPreferences.getInstance().putLong(
								PTSharedPreferences.KEY_LONG_SELECTED_PARTY_ID, m_party.getID());
						break;
					}
				}
				if (m_party == null) {
					addNewParty();
				}
			}
			refreshPartyView();

		}
	}

	/**
	 * Generates a new party and sets it to the current party.
	 */
	private void addNewParty() {
		m_party = new PTParty("New Party");
		m_partyRepo.insert(m_party);
		PTSharedPreferences.getInstance().putLong(
				PTSharedPreferences.KEY_LONG_SELECTED_PARTY_ID, m_party.getID());
		refreshPartyView();
	}

	/**
	 * Deletes the current party and loads the first in the list, or creates a
	 * new blank one, if there was only one.
	 */
	private void deleteCurrentParty() {
		int currentPartyIndex = 0;
		long currentPartyID = m_party.getID();
		List<Entry<Long, String>> partyIDs = m_partyRepo.queryList();

		for (int i = 0; i < partyIDs.size(); i++) {
			if (currentPartyID == partyIDs.get(i).getKey().longValue())
				currentPartyIndex = i;
		}

		if (partyIDs.size() == 1) {
			addNewParty();
		} else if (currentPartyIndex == 0) {
			PTSharedPreferences.getInstance().putLong(
					PTSharedPreferences.KEY_LONG_SELECTED_PARTY_ID, partyIDs.get(1).getKey().longValue());
			loadCurrentParty();
		} else {
			PTSharedPreferences.getInstance().putLong(
					PTSharedPreferences.KEY_LONG_SELECTED_PARTY_ID, partyIDs.get(0).getKey().longValue());
			loadCurrentParty();
		}

		m_partyRepo.delete(currentPartyID);
	}

	private void updateDatabase() {
		m_party.setName(m_partyNameEditText.getText().toString());
		m_partyRepo.update(m_party);
	}

	private void refreshPartyView() {
		m_partyNameEditText.setText(m_party.getName());
		String[] memberNames = m_party.getPartyMemberNames();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, memberNames);
		m_partyMemberList.setAdapter(adapter);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		updateDatabase();

		switch (item.getItemId()) {
		case R.id.mi_party_list:
			m_dialogMode = R.id.mi_party_list;
			showPartyDialog();
			break;
		case R.id.mi_new_member:
			m_dialogMode = R.id.mi_new_member;
			showPartyDialog();
			break;
		case R.id.mi_new_party:
			// Add new party
			m_dialogMode = R.id.mi_new_party;
			showPartyDialog();
			break;
		case R.id.mi_delete_party:
			// Delete party
			m_dialogMode = R.id.mi_delete_party;
			showPartyDialog();
			break;
		}

		super.onOptionsItemSelected(item);
		return true;

	}

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.party_manager_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void showPartyDialog() {
		m_partyIDSelectedInDialog = m_party.getID(); // actual current party

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		switch (m_dialogMode) {
		case R.id.mi_party_list:
			builder.setTitle("Select Party");

			List<Entry<Long, String>> partyIDs = m_partyRepo.queryList();
			String[] partyList = EntryUtils.valueArray(partyIDs);
			int currentPartyIndex = 0;

			for (int i = 0; i < partyIDs.size(); i++) {
				if (m_partyIDSelectedInDialog == partyIDs.get(i).getKey().longValue())
					currentPartyIndex = i;
			}

			builder.setSingleChoiceItems(partyList, currentPartyIndex, this)
					.setPositiveButton(R.string.ok_button_text, this)
					.setNegativeButton(R.string.cancel_button_text, this);
			break;

		case R.id.mi_new_party:
			builder.setMessage(R.string.new_party_dialog_text)
					.setPositiveButton(R.string.ok_button_text, this)
					.setNegativeButton(R.string.cancel_button_text, this);
			break;

		case R.id.mi_delete_party:
			builder.setTitle(getString(R.string.menu_item_delete_party));
			builder.setMessage(
					getString(R.string.delete_character_dialog_message_1)
							+ m_party.getName()
							+ getString(R.string.delete_character_dialog_message_2))
					.setPositiveButton(R.string.delete_button_text, this)
					.setNegativeButton(R.string.cancel_button_text, this);
			break;

		case R.id.mi_new_member:
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
			m_partyIDSelectedInDialog = m_partyRepo.queryList().get(selection).getKey().longValue();
			break;

		}
	}

	/**
	 * Called when dialog positive button is tapped
	 */
	private void performPositiveDialogAction() {
		switch (m_dialogMode) {
		case R.id.mi_party_list:
			// Check if "currently selected" party is the same as saved one
			if (m_partyIDSelectedInDialog != m_party.getID()) {
				updateDatabase(); // Ensures any data changed on the party
										// in the current fragment is saved
				PTSharedPreferences.getInstance().putLong(
						PTSharedPreferences.KEY_LONG_SELECTED_PARTY_ID, m_partyIDSelectedInDialog);
				loadCurrentParty();
			}
			break;

		case R.id.mi_new_party:
			updateDatabase();
			addNewParty();
			break;

		case R.id.mi_delete_party:
			deleteCurrentParty();
			break;

		case R.id.mi_new_member:
			m_partyMemberIndexSelectedForEdit = -1;
			showPartyMemberEditor(null);
			break;

		}

	}

	// Party member in list was clicked
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		m_partyMemberIndexSelectedForEdit = position;
		showPartyMemberEditor(m_party.getPartyMember(position));

	}

	private void showPartyMemberEditor(PTPartyMember member) {
		Intent intent = new Intent(getActivity(),
				PTPartyMemberEditorActivity.class);
		intent.putExtra(
				PTCharacterSpellEditActivity.INTENT_EXTRAS_KEY_EDITABLE_PARCELABLE, member);
		startActivityForResult(intent, PTParcelableEditorActivity.DEFAULT_REQUEST_CODE);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != PTParcelableEditorActivity.DEFAULT_REQUEST_CODE) {
            return;
        }
		switch (resultCode) {
		case Activity.RESULT_OK:
			PTPartyMember member = PTParcelableEditorActivity.getParcelableFromIntent(data);
            if (member != null) {
                if(m_partyMemberIndexSelectedForEdit < 0) {
                    Log.v(TAG, "Adding a member");
                    member.setPartyID(m_party.getID());
                    if (m_memberRepo.insert(member) != -1) {
                        m_party.addPartyMember(member);
                        refreshPartyView();
                    }
                } else {
                    Log.v(TAG, "Editing a member");
                    if (m_memberRepo.update(member) != 0) {
                        m_party.setPartyMember(m_partyMemberIndexSelectedForEdit, member);
                        refreshPartyView();
                    }
                }
            }

            break;
		
		case PTPartyMemberEditorActivity.RESULT_DELETE:
			Log.v(TAG, "Deleting a member");
			if (m_memberRepo.delete(m_party.getPartyMember(m_partyMemberIndexSelectedForEdit)) != 0) {
				m_party.deletePartyMember(m_partyMemberIndexSelectedForEdit);
				refreshPartyView();
			}
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
