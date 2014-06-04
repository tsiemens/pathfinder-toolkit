package com.lateensoft.pathfinder.toolkit.views.party;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.text.Editable;
import com.lateensoft.pathfinder.toolkit.AppPreferences;
import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.adapters.SimpleSelectableListAdapter;
import com.lateensoft.pathfinder.toolkit.db.repository.CharacterRepository;
import com.lateensoft.pathfinder.toolkit.db.repository.PartyMemberRepository;
import com.lateensoft.pathfinder.toolkit.db.repository.PartyRepository;
import com.lateensoft.pathfinder.toolkit.model.IdStringPair;
import com.lateensoft.pathfinder.toolkit.model.NamedList;
import com.lateensoft.pathfinder.toolkit.model.character.PathfinderCharacter;
import com.lateensoft.pathfinder.toolkit.model.party.PartyMember;
import com.lateensoft.pathfinder.toolkit.views.BasePageFragment;
import com.lateensoft.pathfinder.toolkit.views.ParcelableEditorActivity;
import com.lateensoft.pathfinder.toolkit.views.character.SpellEditActivity;

import android.app.AlertDialog;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

public class PartyManagerFragment extends BasePageFragment implements
		OnClickListener, OnItemClickListener {
	private static final String TAG = PartyManagerFragment.class.getSimpleName();
	
	public NamedList<PathfinderCharacter> m_party;

	private int m_dialogMode;
	private long m_partyIDSelectedInDialog;

	private EditText m_partyNameEditText;
	private ListView m_partyMemberList;
	
	private int m_partyMemberIndexSelectedForEdit;
	
	private PartyRepository m_partyRepo;
	private CharacterRepository m_characterRepo;
	private PartyMemberRepository m_memberRepo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		m_partyRepo = new PartyRepository();
		m_memberRepo = new PartyMemberRepository();
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
		long currentPartyID = AppPreferences.getInstance().getLong(
				AppPreferences.KEY_LONG_SELECTED_PARTY_ID, -1);

		if (currentPartyID == -1) {
			// There was no current party set in shared prefs
			addNewParty();
		} else {
			m_party = m_partyRepo.query(currentPartyID);
			if (m_party == null) {
				// Recovery for some kind of catastrophic failure.
				List<IdStringPair> ids = m_partyRepo.queryIdNameList();
                for (IdStringPair id : ids) {
                    m_party = m_partyRepo.query(id.getId());
                    if (m_party != null) {
                        AppPreferences.getInstance().putLong(
                                AppPreferences.KEY_LONG_SELECTED_PARTY_ID, m_party.getID());
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
		m_party = new NamedList<PathfinderCharacter>("New Party");
		m_partyRepo.insert(m_party);
		AppPreferences.getInstance().putLong(
				AppPreferences.KEY_LONG_SELECTED_PARTY_ID, m_party.getID());
		refreshPartyView();
	}

	/**
	 * Deletes the current party and loads the first in the list, or creates a
	 * new blank one, if there was only one.
	 */
	private void deleteCurrentParty() {
		int currentPartyIndex = 0;
		long currentPartyID = m_party.getID();
		List<IdStringPair> partyIDs = m_partyRepo.queryIdNameList();

		for (int i = 0; i < partyIDs.size(); i++) {
			if (currentPartyID == partyIDs.get(i).getId()) {
                currentPartyIndex = i;
            }
		}

		if (partyIDs.size() == 1) {
			addNewParty();
		} else if (currentPartyIndex == 0) {
			AppPreferences.getInstance().putLong(
					AppPreferences.KEY_LONG_SELECTED_PARTY_ID, partyIDs.get(1).getId());
			loadCurrentParty();
		} else {
			AppPreferences.getInstance().putLong(
					AppPreferences.KEY_LONG_SELECTED_PARTY_ID, partyIDs.get(0).getId());
			loadCurrentParty();
		}

		m_partyRepo.delete(currentPartyID);
	}

	private void updateDatabase() {
        Editable text = m_partyNameEditText.getText();
		m_party.setName(text != null ? text.toString() : "");
		m_partyRepo.update(m_party);
	}

	private void refreshPartyView() {
        Collections.sort(m_party, new CharacterComparator());
		m_partyNameEditText.setText(m_party.getName());
        SimpleSelectableListAdapter<PathfinderCharacter> adapter = new SimpleSelectableListAdapter<PathfinderCharacter>(
                getContext(), m_party,
                new SimpleSelectableListAdapter.DisplayStringGetter<PathfinderCharacter>() {
                    @Override public String getDisplayString(PathfinderCharacter object) {
                        return object.getName();
                    }
                });
		m_partyMemberList.setAdapter(adapter);
	}

    private static class CharacterComparator implements Comparator<PathfinderCharacter> {
        @Override public int compare(PathfinderCharacter lhs, PathfinderCharacter rhs) {
            return lhs.getName().compareTo(rhs.getName());
        }
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
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

		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

		switch (m_dialogMode) {
		case R.id.mi_party_list:
			builder.setTitle("Select Party");

			List<IdStringPair> partyIDs = m_partyRepo.queryIdNameList();
			String[] partyList = IdStringPair.valueArray(partyIDs);
			int currentPartyIndex = 0;

			for (int i = 0; i < partyIDs.size(); i++) {
				if (m_partyIDSelectedInDialog == partyIDs.get(i).getId()) {
                    currentPartyIndex = i;
                }
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
			m_partyIDSelectedInDialog = m_partyRepo.queryIdNameList().get(selection).getId();
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
				AppPreferences.getInstance().putLong(
						AppPreferences.KEY_LONG_SELECTED_PARTY_ID, m_partyIDSelectedInDialog);
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
		//showPartyMemberEditor(m_party.get(position));
        // TODO open character sheet
	}

	private void showPartyMemberEditor(PartyMember member) {
		Intent intent = new Intent(getContext(),
				PartyMemberEditorActivity.class);
		intent.putExtra(
				SpellEditActivity.INTENT_EXTRAS_KEY_EDITABLE_PARCELABLE, member);
		startActivityForResult(intent, ParcelableEditorActivity.DEFAULT_REQUEST_CODE);
	}

//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode != ParcelableEditorActivity.DEFAULT_REQUEST_CODE) {
//            return;
//        }
//		switch (resultCode) {
//		case Activity.RESULT_OK:
//			PartyMember member = ParcelableEditorActivity.getParcelableFromIntent(data);
//            if (member != null) {
//                if(m_partyMemberIndexSelectedForEdit < 0) {
//                    Log.v(TAG, "Adding a member");
//                    member.setPartyID(m_party.getID());
//                    if (m_memberRepo.insert(member) != -1) {
//                        m_party.add(member);
//                        refreshPartyView();
//                    }
//                } else {
//                    Log.v(TAG, "Editing a member");
//                    if (m_memberRepo.update(member) != 0) {
//                        m_party.set(m_partyMemberIndexSelectedForEdit, member);
//                        refreshPartyView();
//                    }
//                }
//            }
//
//            break;
//
//		case PartyMemberEditorActivity.RESULT_DELETE:
//			Log.v(TAG, "Deleting a member");
//			if (m_memberRepo.delete(m_party.get(m_partyMemberIndexSelectedForEdit)) != 0) {
//				m_party.remove(m_partyMemberIndexSelectedForEdit);
//				refreshPartyView();
//			}
//			break;
//
//		case Activity.RESULT_CANCELED:
//			break;
//
//		default:
//			break;
//		}
//		updateDatabase();
//		super.onActivityResult(requestCode, resultCode, data);
//	}
}
