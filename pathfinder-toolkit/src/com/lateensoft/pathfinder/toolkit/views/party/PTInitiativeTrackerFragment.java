package com.lateensoft.pathfinder.toolkit.views.party;

import com.lateensoft.pathfinder.toolkit.PTSharedPreferences;
import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.adapters.party.PTPartyRollAdapter;
import com.lateensoft.pathfinder.toolkit.db.repository.PTPartyMemberRepository;
import com.lateensoft.pathfinder.toolkit.db.repository.PTPartyRepository;
import com.lateensoft.pathfinder.toolkit.db.repository.PTStorable;
import com.lateensoft.pathfinder.toolkit.model.party.PTParty;
import com.lateensoft.pathfinder.toolkit.model.party.PTPartyMember;
import com.lateensoft.pathfinder.toolkit.utils.PTDiceSet;
import com.lateensoft.pathfinder.toolkit.views.PTBasePageFragment;
import com.lateensoft.pathfinder.toolkit.views.PTParcelableEditorActivity;
import com.lateensoft.pathfinder.toolkit.views.character.PTCharacterSpellEditActivity;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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

	public PTParty m_party;

	private int m_dialogMode;
	private boolean m_hasRolled;

	private Button m_rollInitiativeButton;
	private ListView m_partyMemberList;
	
	private int m_partyMemberSelectedForEdit;
	
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

		setRootView(inflater.inflate(R.layout.fragment_initiative_tracker, container, false));

		m_rollInitiativeButton = (Button) getRootView().findViewById(R.id.buttonRollInitiative);
		m_rollInitiativeButton.setOnClickListener(this);

		m_partyMemberList = (ListView) getRootView().findViewById(R.id.listViewEncounterMembers);
		m_partyMemberList.setOnItemClickListener(this);

		m_hasRolled = false;
		
		loadEncounterParty();
		return getRootView();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.mi_reset:
			m_dialogMode = R.id.mi_reset;
			showPartyDialog();
			break;
		case R.id.mi_new_member:
			m_dialogMode = R.id.mi_new_member;
			showPartyDialog();
			break;
		}

		super.onOptionsItemSelected(item);
		return true;

	}

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.initiative_tracker_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void updateTitle() {
        setTitle(R.string.title_activity_initiative_tracker);
        if (m_party != null) {
            setSubtitle(m_party.getName());
        } else {
            setSubtitle(null);
        }
    }

    private void showPartyDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		switch (m_dialogMode) {
		case R.id.mi_reset:
			builder.setTitle(getString(R.string.reset_encounter_dialog_title));
			builder.setMessage(getString(R.string.reset_encounter_dialog_text))
					.setPositiveButton(R.string.ok_button_text, this)
					.setNegativeButton(R.string.cancel_button_text, this)
					.setNeutralButton(
							getString(R.string.use_defualt_party_button), this);
			break;

		case R.id.mi_new_member:
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
		switch (m_dialogMode) {
		case R.id.mi_reset:
			resetPartyRolls();
			break;

		case R.id.mi_new_member:
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
		PTParty currentEncounterParty = m_partyRepo.queryEncounterParty();
		// If there is no saved encounter party, get from party manager
		// Also, if the encounter party was saved, but previously was empty, get
		// from party manager.
		if (currentEncounterParty == null || currentEncounterParty.size() == 0) {
			loadDefaultParty();
		} else {
			m_party = currentEncounterParty;
			m_hasRolled = false;
			for (int i = 0; i < m_party.size(); i++)
				if (m_party.getPartyMember(i).getLastRolledValue() != 0) {
					m_hasRolled = true;
				}

			refreshPartyView();
		}
	}

	private void loadDefaultParty() {
		long currentPartyID = PTSharedPreferences.getInstance().getLong(
				PTSharedPreferences.KEY_LONG_SELECTED_PARTY_ID, -1);
		PTParty defaultParty = null;
		if (currentPartyID > 0) {
			defaultParty = m_partyRepo.query(currentPartyID);
		} 
		
		if(defaultParty != null && defaultParty.size() > 0) {
			PTParty currentEncParty = m_partyRepo.queryEncounterParty();
			if (currentEncParty != null) {
				m_partyRepo.delete(currentEncParty.getID());
			}
			// Make a copy, but only if there are members in it
			defaultParty.setID(PTStorable.UNSET_ID);
			for (int i = 0; i < defaultParty.size(); i++) {
				defaultParty.getPartyMember(i).setID(PTStorable.UNSET_ID);
			}
			m_partyRepo.insert(defaultParty, true);
		} else {
			defaultParty = new PTParty("New Party");
		}
	
		m_party = defaultParty;
		m_hasRolled = false;
		refreshPartyView();
	}

	private void resetPartyRolls() {
		for (int i = 0; i < m_party.size(); i++) {
			m_party.getPartyMember(i).setLastRolledValue(0);
		}
		updateDatabase();
		m_hasRolled = false;
		refreshPartyView();
	}

	private void refreshPartyView() {
		String[] memberNames = m_party.getNamesByRollValue();
		int[] memberRollValues = m_party.getRollValuesByRollValue();
		PTPartyRollAdapter adapter = new PTPartyRollAdapter(getActivity(),
				R.layout.party_roll_row, memberNames, memberRollValues, null);
		m_partyMemberList.setAdapter(adapter);
		m_rollInitiativeButton.setEnabled(!m_hasRolled);
        updateTitle();
	}

	/**
	 * launches the party member editor
	 * @param member the member to send to the editor
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
			PTPartyMember member = PTParcelableEditorActivity.getParcelableFromIntent(data);
            if (member != null) {
                if(m_partyMemberSelectedForEdit < 0) {
                    Log.v(TAG, "Adding a member");
                    if (m_party.getID() == PTStorable.UNSET_ID) {
                        // Party has not yet been added to database
                        m_party.addPartyMember(member);
                        m_partyRepo.insert(m_party);
                    } else {
                        member.setPartyID(m_party.getID());
                        if (m_memberRepo.insert(member) != -1) {
                            m_party.addPartyMember(member);
                        }
                    }
                    refreshPartyView();
                } else {
                    Log.v(TAG, "Editing a member");
                    if (m_memberRepo.update(member) != 0) {
                        m_party.setPartyMember(m_partyMemberSelectedForEdit, member);
                        refreshPartyView();
                    }
                }
            }

            break;
		
		case PTPartyMemberEditorActivity.RESULT_DELETE:
			Log.v(TAG, "Deleting a member");
			if (m_memberRepo.delete(m_party.getPartyMember(m_partyMemberSelectedForEdit)) != 0) {
				m_party.deletePartyMember(m_partyMemberSelectedForEdit);
				refreshPartyView();
			}
			break;
		
		case Activity.RESULT_CANCELED:
			break;
		
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	// When roll initiative button is clicked
	@Override
	public void onClick(View view) {
		PTDiceSet diceSet = new PTDiceSet();
		int initiativeMod;

		for (int i = 0; i < m_party.size(); i++) {
			initiativeMod = m_party.getPartyMember(i).getInitiative();
			m_party.getPartyMember(i).setLastRolledValue(
					diceSet.singleRoll(20) + initiativeMod);
		}
		updateDatabase();
		m_hasRolled = true;
		loadEncounterParty();
	}

	// Party member in list was clicked
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		m_partyMemberSelectedForEdit = m_party
				.getPartyMemberIndexByRollValueIndex(position);
		showPartyMemberEditor(m_party.getPartyMember(m_partyMemberSelectedForEdit));

	}
	
	private void updateDatabase() {
		for (int i = 0; i < m_party.size(); i++) {
			m_memberRepo.update(m_party.getPartyMember(i));
		}
	}
}
