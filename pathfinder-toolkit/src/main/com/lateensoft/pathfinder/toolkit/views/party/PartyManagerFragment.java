package com.lateensoft.pathfinder.toolkit.views.party;

import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.text.Editable;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.adapters.SimpleSelectableListAdapter;
import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.db.dao.table.CharacterNameDAO;
import com.lateensoft.pathfinder.toolkit.db.dao.table.PartyDAO;
import com.lateensoft.pathfinder.toolkit.db.dao.table.PartyMemberNameDAO;
import com.lateensoft.pathfinder.toolkit.model.IdNamePair;
import com.lateensoft.pathfinder.toolkit.model.NamedList;
import com.lateensoft.pathfinder.toolkit.pref.GlobalPrefs;
import com.lateensoft.pathfinder.toolkit.pref.Preferences;
import com.lateensoft.pathfinder.toolkit.views.BasePageFragment;
import com.lateensoft.pathfinder.toolkit.views.MultiSelectActionModeController;
import com.lateensoft.pathfinder.toolkit.views.character.CharacterCombatStatsFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import com.lateensoft.pathfinder.toolkit.views.picker.PickerUtil;
import roboguice.RoboGuice;

public class PartyManagerFragment extends BasePageFragment {
    private static final String TAG = PartyManagerFragment.class.getSimpleName();
    private static final int GET_NEW_MEMBERS_CODE = 18880;
    private static final int GET_PARTY_CODE = 54716;

    public NamedList<IdNamePair> party;

    private EditText partyNameEditText;
    private ListView partyMemberList;
    
    private PartyDAO partyDao;
    private PartyMemberNameDAO memberDao;

    private Preferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        partyDao = new PartyDAO(getContext());
        memberDao = new PartyMemberNameDAO(getContext());
        preferences = RoboGuice.getInjector(getContext()).getInstance(Preferences.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        setRootView(inflater.inflate(R.layout.fragment_party_manager,
                container, false));

        partyNameEditText = (EditText) getRootView()
                .findViewById(R.id.editTextPartyName);

        partyMemberList = (ListView) getRootView()
                .findViewById(R.id.listViewPartyMembers);
        partyMemberList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (actionModeController.isActionModeStarted()) {
                    actionModeController.toggleListItemSelection(position);
                } else {
                    showMemberDetails(party.get(position).getId());
                }
            }
        });
        partyMemberList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (!actionModeController.isActionModeStarted()) {
                    actionModeController.startActionModeWithInitialSelection(position);
                    return true;
                }
                return false;
            }
        });

        loadCurrentParty();
        return getRootView();
    }

    private MultiSelectActionModeController actionModeController = new MultiSelectActionModeController() {
        @Override public Activity getActivity() {
            return PartyManagerFragment.this.getActivity();
        }

        @Override public int getActionMenuResourceId() {
            return R.menu.remove_action_mode_menu;
        }

        @Override public ListView getListView() {
            return partyMemberList;
        }

        @Override public boolean onActionItemClicked(MultiSelectActionModeController controller, MenuItem item) {
            if (item.getItemId() == R.id.mi_remove) {
                showRemoveCharactersFromPartyDialog(getSelectedItems(party));
                controller.finishActionMode();
                return true;
            }
            return false;
        }
    };
    
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

    private void showMemberDetails(long characterId) {
        preferences.put(GlobalPrefs.SELECTED_CHARACTER_ID, characterId);
        switchToPage(CharacterCombatStatsFragment.class);
    }

    /**
     * Load the currently set party in shared prefs If there is no party set in
     * user prefs, it automatically generates a new one.
     */
    private void loadCurrentParty() {
        long currentPartyID = getSelectedPartyId();

        if (currentPartyID == -1) {
            // There was no current party set in shared prefs
            addNewPartyAndSetSelected();
        } else {
            party = partyDao.find(currentPartyID, memberDao);
            if (party == null) {
                handleInvalidSelectedParty();
            }
            refreshPartyView();

        }
    }

    private void setSelectedParty(long partyId) {
        preferences.put(GlobalPrefs.SELECTED_PARTY_ID, partyId);
    }

    private long getSelectedPartyId() {
        return preferences.get(GlobalPrefs.SELECTED_PARTY_ID, -1L);
    }

    private void handleInvalidSelectedParty() {
        List<IdNamePair> ids = partyDao.findAll();
        for (IdNamePair id : ids) {
            party = partyDao.find(id.getId(), memberDao);
            if (party != null) {
                setSelectedParty(party.getId());
                break;
            }
        }
        if (party == null) {
            addNewPartyAndSetSelected();
        }
    }

    private void showRemoveCharactersFromPartyDialog(final List<IdNamePair> membersToRemove) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.membership_remove_dialog_title)
                .setMessage(String.format(getString(R.string.party_remove_members_dialog_msg),
                        party.size()));

        builder.setPositiveButton(R.string.ok_button_text, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    removeMembersFromParty(membersToRemove);
                }
            }
        })
                .setNegativeButton(R.string.cancel_button_text, null)
                .show();
    }

    private void removeMembersFromParty(List<IdNamePair> membersToRemove) {
        for (IdNamePair member : membersToRemove) {
            try {
                memberDao.remove(party.getId(), member);
                party.remove(member);
            } catch (DataAccessException e) {
                Log.e(TAG, "Failed to remove member" + member, e);
            }
        }
        refreshPartyView();
    }

    private void addNewPartyAndSetSelected() {
        try {
            NamedList<IdNamePair> newParty = new NamedList<IdNamePair>("New Party");
            long id = partyDao.add(newParty.idNamePair());
            newParty.setId(id);
            party = newParty;
            setSelectedParty(newParty.getId());
            refreshPartyView();
        } catch (DataAccessException e) {
            Log.e(TAG, "Failed to add party", e);
        }
    }

    private void deleteCurrentPartyAndSelectAnother() {
        try {
            partyDao.remove(party.idNamePair());
            List<IdNamePair> partyIDs = partyDao.findAll();

            if (partyIDs.isEmpty()) {
                addNewPartyAndSetSelected();
            } else {
                setSelectedParty(partyIDs.get(0).getId());
                loadCurrentParty();
            }
        } catch (DataAccessException e) {
            Log.e(TAG, "Failed to delete party " + party.idNamePair(), e);
        }
    }

    private void updateDatabase() {
        Editable text = partyNameEditText.getText();
        party.setName(text != null ? text.toString() : "");
        try {
            partyDao.update(party.idNamePair());
        } catch (DataAccessException e) {
            Log.e(TAG, "Failed to update party", e);
        }
    }

    private void refreshPartyView() {
        if (actionModeController.isActionModeStarted()) {
            actionModeController.finishActionMode();
        }

        Collections.sort(party);
        partyNameEditText.setText(party.getName());
        SimpleSelectableListAdapter<IdNamePair> adapter = new SimpleSelectableListAdapter<IdNamePair>(
                getContext(), party,
                new SimpleSelectableListAdapter.DisplayStringGetter<IdNamePair>() {
                    @Override public String getDisplayString(IdNamePair object) {
                        return object.getName();
                    }
                });
        adapter.setItemSelectionGetter(new SimpleSelectableListAdapter.ItemSelectionGetter() {
            @Override public boolean isItemSelected(int position) {
                return actionModeController.isListItemSelected(position);
            }
        });
        partyMemberList.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.mi_party_list:
            showPartyPicker();
            break;
        case R.id.mi_new_member:
            showMemberPicker();
            break;
        case R.id.mi_new_party:
            showCreateNewPartyDialog();
            break;
        case R.id.mi_delete_party:
            showDeletePartyDialog();
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

    private void showPartyPicker() {
        List<IdNamePair> parties = partyDao.findAll();
        parties.remove(new IdNamePair(party.getId(), party.getName()));
        PickerUtil.Builder builder = new PickerUtil.Builder(getActivity());
        builder.setTitle(R.string.single_party_picker_title)
                .setSingleChoice(true)
                .setPickableParties(parties);
        Intent pickerIntent = builder.build();
        startActivityForResult(pickerIntent, GET_PARTY_CODE);
    }

    private void showMemberPicker() {
        CharacterNameDAO charDao = new CharacterNameDAO(getContext());
        List<IdNamePair> characters = charDao.findAll();
        characters.removeAll(party);
        PickerUtil.Builder builder = new PickerUtil.Builder(getActivity());
        builder.setTitle(R.string.select_members_picker_title)
                .setSingleChoice(false)
                .setPickableCharacters(characters);
        Intent pickerIntent = builder.build();
        startActivityForResult(pickerIntent, GET_NEW_MEMBERS_CODE);
    }

    private void showCreateNewPartyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(getString(R.string.new_party_dialog_text))
                .setPositiveButton(R.string.ok_button_text, new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        updateDatabase();
                        addNewPartyAndSetSelected();
                    }
                })
                .setNegativeButton(R.string.cancel_button_text, null);
        builder.create().show();
    }

    private void showDeletePartyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(String.format(getString(R.string.confirm_delete_item_message),
                party.getName()))
                .setPositiveButton(R.string.ok_button_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteCurrentPartyAndSelectAnother();
                    }
                })
                .setNegativeButton(R.string.cancel_button_text, null);
        builder.create().show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GET_PARTY_CODE || requestCode == GET_NEW_MEMBERS_CODE) {
            PickerUtil.ResultData pickerData = new PickerUtil.ResultData(data);
            if (requestCode == GET_PARTY_CODE) {
                IdNamePair party = pickerData.getParty();
                if (party != null) {
                    setSelectedParty(party.getId());
                    loadCurrentParty();
                }
            } else {
                List<IdNamePair> membersToAdd = pickerData.getCharacters();
                if (membersToAdd != null) {
                    for (IdNamePair member : membersToAdd) {
                        try {
                            memberDao.add(party.getId(), member);
                        } catch (DataAccessException e) {
                            Log.e(TAG, "Failed to add member", e);
                        }
                    }
                    refreshPartyView();
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
