package com.lateensoft.pathfinder.toolkit.views.party;

import com.lateensoft.pathfinder.toolkit.AppPreferences;
import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.adapters.party.PartyRollAdapter;
import com.lateensoft.pathfinder.toolkit.db.repository.PartyMemberRepository;
import com.lateensoft.pathfinder.toolkit.db.repository.PartyRepository;
import com.lateensoft.pathfinder.toolkit.db.dao.Identifiable;
import com.lateensoft.pathfinder.toolkit.model.party.CampaignParty;
import com.lateensoft.pathfinder.toolkit.model.party.PartyMember;
import com.lateensoft.pathfinder.toolkit.util.DiceSet;
import com.lateensoft.pathfinder.toolkit.views.BasePageFragment;
import com.lateensoft.pathfinder.toolkit.views.ParcelableEditorActivity;
import com.lateensoft.pathfinder.toolkit.views.character.SpellEditActivity;

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

import java.util.Collections;

public class InitiativeTrackerFragment extends BasePageFragment {
    private static final String TAG = InitiativeTrackerFragment.class.getSimpleName();
    public static final int INIT_DIE_TYPE = 20;

    public CampaignParty m_party;

	private int m_dialogMode;
	private boolean m_hasRolled;

	private Button m_rollInitiativeButton;
	private ListView m_partyMemberList;
	
	private int m_partyMemberSelectedForEdit;
	
	private PartyRepository m_partyRepo;
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

		setRootView(inflater.inflate(R.layout.fragment_initiative_tracker, container, false));

		m_rollInitiativeButton = (Button) getRootView().findViewById(R.id.buttonRollInitiative);
		m_rollInitiativeButton.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View view) {
                DiceSet diceSet = new DiceSet();
                for (PartyMember member : m_party) {
                    member.setLastRolledValue(diceSet.singleRoll(INIT_DIE_TYPE) + member.getInitiative());
                }
                updateDatabase();
                m_hasRolled = true;
                refreshPartyView();
            }
        });

		m_partyMemberList = (ListView) getRootView().findViewById(R.id.listViewEncounterMembers);
		m_partyMemberList.setOnItemClickListener(new OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                m_party.get(position);
                m_partyMemberSelectedForEdit = position;
                showPartyMemberEditor(m_party.get(position));
            }
        });

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
        PartyDialogListener listener = new PartyDialogListener();
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

		switch (m_dialogMode) {
		case R.id.mi_reset:
			builder.setTitle(getString(R.string.reset_encounter_dialog_title));
			builder.setMessage(getString(R.string.reset_encounter_dialog_text))
					.setPositiveButton(R.string.ok_button_text, listener)
					.setNegativeButton(R.string.cancel_button_text, listener)
					.setNeutralButton(
							getString(R.string.use_defualt_party_button), listener);
			break;

		case R.id.mi_new_member:
			builder.setMessage(
					getString(R.string.new_encounter_member_dialog_text))
					.setPositiveButton(R.string.ok_button_text, listener)
					.setNegativeButton(R.string.cancel_button_text, listener);
			break;

		}
		AlertDialog alert = builder.create();
		alert.show();
	}

    private class PartyDialogListener implements DialogInterface.OnClickListener {
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
    }

	/**
	 * Load the current encounter party in shared prefs If there is no party set
	 * in user prefs, it pulls the default currently set in party manager If
	 * there is not current party, an empty party is set
	 */
	private void loadEncounterParty() {
		CampaignParty currentEncounterParty = m_partyRepo.queryEncounterParty();
		// If there is no saved encounter party, get from party manager
		// Also, if the encounter party was saved, but previously was empty, get
		// from party manager.
		if (currentEncounterParty == null || currentEncounterParty.size() == 0) {
			loadDefaultParty();
		} else {
			m_party = currentEncounterParty;
			m_hasRolled = false;
            for (PartyMember member : m_party)
                if (member.getLastRolledValue() != 0) {
                    m_hasRolled = true;
                }

			refreshPartyView();
		}
	}

	private void loadDefaultParty() {
		long currentPartyID = AppPreferences.getInstance().getLong(
				AppPreferences.KEY_LONG_SELECTED_PARTY_ID, -1);
		CampaignParty defaultParty = null;
		if (currentPartyID > 0) {
//			defaultParty = m_partyRepo.query(currentPartyID); TODO
		}

        // Save a copy as the encounter party, but only if there are members in it
		if(defaultParty != null && !defaultParty.isEmpty()) {
			CampaignParty currentEncParty = m_partyRepo.queryEncounterParty();
			if (currentEncParty != null) {
				m_partyRepo.delete(currentEncParty.getId());
			}

			defaultParty.setId(Identifiable.UNSET_ID);
            for (PartyMember member : defaultParty) {
                member.setId(Identifiable.UNSET_ID);
            }
//			m_partyRepo.insert(defaultParty); tODO add to encounters table
		} else {
			defaultParty = new CampaignParty("New Party");
		}
	
		m_party = defaultParty;
		m_hasRolled = false;
		refreshPartyView();
	}

	private void resetPartyRolls() {
        for (PartyMember member : m_party) {
            member.setLastRolledValue(0);
        }
		updateDatabase();
		m_hasRolled = false;
		refreshPartyView();
	}

	private void refreshPartyView() {
        Collections.sort(m_party, new PartyMember.RollComparator());
		PartyRollAdapter adapter = new PartyRollAdapter(getContext(),
				R.layout.party_roll_row, m_party, null);
		m_partyMemberList.setAdapter(adapter);
		m_rollInitiativeButton.setEnabled(!m_hasRolled);
        updateTitle();
	}

	private void showPartyMemberEditor(PartyMember member) {
		Intent intent = new Intent(getContext(),
				PartyMemberEditorActivity.class);
		intent.putExtra(
				SpellEditActivity.INTENT_EXTRAS_KEY_EDITABLE_PARCELABLE, member);
		startActivityForResult(intent, ParcelableEditorActivity.DEFAULT_REQUEST_CODE);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != ParcelableEditorActivity.DEFAULT_REQUEST_CODE) {
            return;
        }
		switch (resultCode) {
		case Activity.RESULT_OK:
			PartyMember member = ParcelableEditorActivity.getParcelableFromIntent(data);
            if (member != null) {
                if(m_partyMemberSelectedForEdit < 0) {
                    Log.v(TAG, "Adding a member");
                    if (m_party.getId() == Identifiable.UNSET_ID) {
                        // Party has not yet been added to database
                        m_party.add(member);
//                        m_partyRepo.insert(m_party); TODO
                    } else {
                        member.setPartyID(m_party.getId());
                        if (m_memberRepo.insert(member) != -1) {
                            m_party.add(member);
                        }
                    }
                    refreshPartyView();
                } else {
                    Log.v(TAG, "Editing a member");
                    if (m_memberRepo.update(member) != 0) {
                        m_party.set(m_partyMemberSelectedForEdit, member);
                        refreshPartyView();
                    }
                }
            }

            break;
		
		case PartyMemberEditorActivity.RESULT_DELETE:
			Log.v(TAG, "Deleting a member");
			if (m_memberRepo.delete(m_party.get(m_partyMemberSelectedForEdit)) != 0) {
				m_party.remove(m_partyMemberSelectedForEdit);
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
	
	private void updateDatabase() {
        for (PartyMember member : m_party) {
            m_memberRepo.update(member);
        }
	}
}
