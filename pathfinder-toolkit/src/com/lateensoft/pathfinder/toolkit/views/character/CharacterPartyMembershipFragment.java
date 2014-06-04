package com.lateensoft.pathfinder.toolkit.views.character;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.adapters.SimpleSelectableListAdapter;
import com.lateensoft.pathfinder.toolkit.db.repository.PartyMembershipRepository;
import com.lateensoft.pathfinder.toolkit.db.repository.PartyRepository;
import com.lateensoft.pathfinder.toolkit.model.IdStringPair;
import com.lateensoft.pathfinder.toolkit.views.picker.PickerUtils;

import java.util.Collections;
import java.util.List;

import static com.lateensoft.pathfinder.toolkit.db.repository.PartyMembershipRepository.Membership;

public class CharacterPartyMembershipFragment extends AbstractCharacterSheetFragment {

	private static final String TAG = CharacterPartyMembershipFragment.class.getSimpleName();
    public static final int GET_NEW_PARTIES_CODE = 104222030;

	private ListView m_partyListView;
	private Button m_addButton;

    private PartyRepository m_partyRepo;
    private List<IdStringPair> m_parties;

    private ActionMode m_actionMode;
    // Integrated checking/selection in ListView is not behaving as expected, so this is needed for now
    private SparseBooleanArray m_actionModeSelections;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        m_partyRepo = new PartyRepository();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		setRootView(inflater.inflate(R.layout.character_membership_fragment,
				container, false));

		m_addButton = (Button) getRootView().findViewById(R.id.button_add);
		m_addButton.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                showPartyPicker();
            }
        });

		m_partyListView = (ListView) getRootView()
				.findViewById(R.id.lv_parties);
        m_partyListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        m_partyListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (m_actionMode != null) {
                    return false;
                }

                m_actionMode = getActivity().startActionMode(new ActionModeCallback());
                m_actionModeSelections = new SparseBooleanArray();
                m_actionModeSelections.put(position, true);
                m_partyListView.invalidateViews();
                return true;
            }
        });
        m_partyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (m_actionMode != null) {
                    m_actionModeSelections.put(position, !m_actionModeSelections.get(position));
                    m_partyListView.invalidateViews();
                }
            }
        });

		return getRootView();
	}

	private void refreshPartiesListView() {
        if (m_actionMode != null) {
            m_actionMode.finish();
            m_actionMode = null;
            m_actionModeSelections = null;
        }

        Collections.sort(m_parties);

        SimpleSelectableListAdapter adapter = new SimpleSelectableListAdapter<IdStringPair>(getActivity(),
                 m_parties, new SimpleSelectableListAdapter.DisplayStringGetter<IdStringPair>() {
                    @Override public String getDisplayString(IdStringPair object) {
                        return object.getValue();
                    }
                });
        adapter.setItemSelectionGetter(new SimpleSelectableListAdapter.ItemSelectionGetter() {
            @Override public boolean isItemSelected(int position) {
                if (m_actionMode != null && m_actionModeSelections != null) {
                    return m_actionModeSelections.get(position);
                } else {
                    return false;
                }
            }
        });

		m_partyListView.setAdapter(adapter);
	}

    private class ActionModeCallback implements ActionMode.Callback {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.membership_action_mode_menu, menu);
            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (item.getItemId() == R.id.mi_remove) {
                List<IdStringPair> selectedParties = Lists.newArrayListWithCapacity(m_actionModeSelections.size());
                for (int i = 0; i < m_actionModeSelections.size(); i++) {
                    int key = m_actionModeSelections.keyAt(i);
                    if (m_actionModeSelections.get(key)) {
                        selectedParties.add(m_parties.get(key));
                    }
                }
                showRemoveCharacterFromPartyDialog(selectedParties);
                mode.finish();
                return true;
            }
            return false;
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            m_actionMode = null;
            m_actionModeSelections = null;
            m_partyListView.invalidateViews();
        }
    }

    public void showRemoveCharacterFromPartyDialog(final List<IdStringPair> parties) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.membership_remove_dialog_title)
                .setMessage(String.format(getString(R.string.membership_remove_dialog_msg), parties.size()));

        builder.setPositiveButton(R.string.ok_button_text, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    removeCharacterFromParties(parties);
                }
            }
        })
                .setNegativeButton(R.string.cancel_button_text, null)
                .show();
    }

    private void removeCharacterFromParties(List<IdStringPair> parties) {
        PartyMembershipRepository memberRepo = m_partyRepo.getMembersRepo();
        for (IdStringPair party : parties) {
            int dels = memberRepo.delete(new Membership(party.getId(), m_currentCharacterID));
            if (dels > 0) {
                m_parties.remove(party);
            }
            refreshPartiesListView();
        }
    }

    public void showPartyPicker() {
        List<IdStringPair> parties = m_partyRepo.queryIdNameList();
        parties.removeAll(m_parties);
        PickerUtils.Builder builder = new PickerUtils.Builder(getActivity());
        builder.setTitle(R.string.membership_party_picker_title)
                .setSingleChoice(false)
                .setPickableParties(parties);
        Intent pickerIntent = builder.build();
        startActivityForResult(pickerIntent, GET_NEW_PARTIES_CODE);
    }

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_NEW_PARTIES_CODE && resultCode == Activity.RESULT_OK &&
                data != null) {
            PickerUtils.ResultData resultData = new PickerUtils.ResultData(data);
            List<IdStringPair> partiesToAdd = resultData.getParties();

            PartyMembershipRepository membersRepo = m_partyRepo.getMembersRepo();
            for (IdStringPair party : partiesToAdd) {
                long id = membersRepo.insert(new Membership(party.getId(), m_currentCharacterID));
                if (id >= 0) {
                    m_parties.add(party);
                } else {
                    Log.e(TAG, "Database returned " + id + " while adding character "
                            + m_currentCharacterID + " to " + party);
                }
            }
            refreshPartiesListView();
        }
    }

	@Override
	public void updateFragmentUI() {
		refreshPartiesListView();
	}
	
	@Override
	public String getFragmentTitle() {
		return getString(R.string.tab_character_membership);
	}

	@Override
	public void updateDatabase() {
		// Done dynamically
	}

	@Override
	public void loadFromDatabase() {
        m_parties = m_partyRepo.queryPartyNamesForCharacter(getCurrentCharacterID());
	}

}
