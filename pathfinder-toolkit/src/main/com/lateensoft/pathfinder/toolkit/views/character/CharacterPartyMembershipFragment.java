package com.lateensoft.pathfinder.toolkit.views.character;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.adapters.SimpleSelectableListAdapter;
import com.lateensoft.pathfinder.toolkit.db.repository.LitePartyRepository;
import com.lateensoft.pathfinder.toolkit.db.repository.PartyMembershipRepository;
import com.lateensoft.pathfinder.toolkit.model.IdStringPair;
import com.lateensoft.pathfinder.toolkit.views.MultiSelectActionModeCallback;
import com.lateensoft.pathfinder.toolkit.views.picker.PickerUtils;

import java.util.Collections;
import java.util.List;

import static com.lateensoft.pathfinder.toolkit.db.repository.PartyMembershipRepository.Membership;

public class CharacterPartyMembershipFragment extends AbstractCharacterSheetFragment {

    private static final String TAG = CharacterPartyMembershipFragment.class.getSimpleName();
    public static final int GET_NEW_PARTIES_CODE = 33091;

    private ListView m_partyListView;
    private Button m_addButton;

    private LitePartyRepository m_partyRepo;
    private List<IdStringPair> m_parties;

    private ActionMode m_actionMode;
    private ActionModeCallback m_actionModeCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_partyRepo = new LitePartyRepository();
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

                m_actionModeCallback = new ActionModeCallback();
                m_actionMode = getActivity().startActionMode(m_actionModeCallback);
                m_actionModeCallback.selectListItem(position, true);
                return true;
            }
        });
        m_partyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (m_actionModeCallback != null) {
                    m_actionModeCallback.toggleListItemSelection(position);
                }
            }
        });

        return getRootView();
    }

    private void refreshPartiesListView() {
        if (m_actionMode != null) {
            m_actionMode.finish();
            m_actionModeCallback = null;
            m_actionMode = null;
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
                if (m_actionModeCallback != null) {
                    return m_actionModeCallback.isListItemSelected(position);
                } else {
                    return false;
                }
            }
        });

        m_partyListView.setAdapter(adapter);
    }

    private class ActionModeCallback extends MultiSelectActionModeCallback {

        public ActionModeCallback() {
            super(R.menu.membership_action_mode_menu);
        }

        @Override
        public ListView getListView() {
            return m_partyListView;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (item.getItemId() == R.id.mi_remove) {
                showRemoveCharacterFromPartyDialog(getSelectedItems(m_parties));
                mode.finish();
                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            super.onDestroyActionMode(mode);
            m_actionMode = null;
            m_actionModeCallback = null;
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
            if (partiesToAdd != null) {
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
