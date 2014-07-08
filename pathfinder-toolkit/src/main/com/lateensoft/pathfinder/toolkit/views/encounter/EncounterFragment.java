package com.lateensoft.pathfinder.toolkit.views.encounter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.adapters.SelectableItemAdapter;
import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.db.dao.table.*;
import com.lateensoft.pathfinder.toolkit.model.IdNamePair;
import com.lateensoft.pathfinder.toolkit.model.NamedList;
import com.lateensoft.pathfinder.toolkit.model.character.PathfinderCharacter;
import com.lateensoft.pathfinder.toolkit.model.party.EncounterParticipant;
import com.lateensoft.pathfinder.toolkit.pref.GlobalPrefs;
import com.lateensoft.pathfinder.toolkit.pref.Preferences;
import com.lateensoft.pathfinder.toolkit.views.BasePageFragment;
import com.lateensoft.pathfinder.toolkit.views.MultiSelectActionModeController;
import com.lateensoft.pathfinder.toolkit.views.character.CharacterCombatStatsFragment;
import com.lateensoft.pathfinder.toolkit.views.picker.PickerUtil;
import com.lateensoft.pathfinder.toolkit.views.widget.DynamicArrayAdapter;
import com.lateensoft.pathfinder.toolkit.views.widget.DynamicListView;
import roboguice.RoboGuice;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class EncounterFragment extends BasePageFragment {
    private static final String TAG = EncounterFragment.class.getSimpleName();
    private static final int ADD_PARTICIPANTS_REQUEST_CODE = 2338;

    private EditText encounterNameEditor;
    private TextView lastSkillCheckName;
    private Button nextTurnButton;
    private DynamicListView participantList;

    private EncounterViewModel encounter;

    private Preferences preferences;
    private EncounterDAO encounterDao;
    private EncounterParticipantDAO participantDao;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        encounterDao = new EncounterDAO(getContext());
        participantDao = new EncounterParticipantDAO(getContext());
        preferences = RoboGuice.getInjector(getContext()).getInstance(Preferences.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRootView(inflater.inflate(R.layout.fragment_encounters, container, false));

        initializeComponents();

        loadCurrentEncounterFromDatabase();
        refreshParticipantListContent();
        return getRootView();
    }

    private void initializeComponents() {
        encounterNameEditor = (EditText) getRootView().findViewById(R.id.et_encounter_name);
        lastSkillCheckName = (TextView) getRootView().findViewById(R.id.tv_last_skill_check);
        nextTurnButton = (Button) getRootView().findViewById(R.id.button_next);
        participantList = (DynamicListView) getRootView().findViewById(R.id.listview);

        participantList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (actionModeController.isActionModeStarted()) {
                    actionModeController.toggleListItemSelection(position);
                } else {
                    showParticipantDetails(encounter.get(position));
                }
            }
        });
        participantList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (!actionModeController.isActionModeStarted()) {
                    actionModeController.startActionModeWithInitialSelection(position);
                    return true;
                }
                return false;
            }
        });
    }

    private MultiSelectActionModeController actionModeController = new MultiSelectActionModeController() {
        @Override public Activity getActivity() {
            return EncounterFragment.this.getActivity();
        }

        @Override public int getActionMenuResourceId() {
            return R.menu.membership_action_mode_menu;
        }

        @Override public ListView getListView() {
            return participantList;
        }

        @Override public boolean onActionItemClicked(MultiSelectActionModeController controller, MenuItem item) {
            if (item.getItemId() == R.id.mi_remove) {
                showRemoveCharactersFromPartyDialog(getSelectedItems(encounter));
                controller.finishActionMode();
                return true;
            }
            return false;
        }
    };

    private void showRemoveCharactersFromPartyDialog(final List<EncounterParticipantRowModel> participantsToRemove) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.membership_remove_dialog_title)
                .setMessage(String.format("Remove (%d) participants from encounter?", // TODO strings.xml
                        encounter.size()));

        builder.setPositiveButton(R.string.ok_button_text, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    removeParticipantsFromEncounter(participantsToRemove);
                }
            }
        })
                .setNegativeButton(R.string.cancel_button_text, null)
                .show();
    }

    private void removeParticipantsFromEncounter(List<EncounterParticipantRowModel> participantsToRemove) {
        for (EncounterParticipantRowModel row : participantsToRemove) {
            try {
                participantDao.remove(encounter.getId(), row.getParticipant());
                encounter.remove(row);
            } catch (DataAccessException e) {
                Log.e(TAG, "Could not remove participant " + row.getParticipant().getId()
                        + " from party " + encounter.getId(), e);
            }
        }
    }

    private void showParticipantDetails(EncounterParticipantRowModel row) {
        preferences.put(GlobalPrefs.SELECTED_CHARACTER_ID, row.getParticipant().getId());
        switchToPage(CharacterCombatStatsFragment.class);
    }

    private void refreshParticipantListContent() {
        if (actionModeController.isActionModeStarted()) {
            actionModeController.finishActionMode();
        }

        EncounterParticipantListAdapter adapter = new EncounterParticipantListAdapter(getActivity(), encounter);
        participantList.setDynamicAdapter(adapter);
        adapter.setDragIconTouchListener(dragIconTouchListener);
        adapter.setOnItemsSwappedListener(itemsSwappedListener);
        adapter.setItemSelectionGetter(new SelectableItemAdapter.ItemSelectionGetter() {
            @Override public boolean isItemSelected(int position) {
                return actionModeController.isListItemSelected(position);
            }
        });
    }

    private EncounterParticipantListAdapter.RowTouchListener dragIconTouchListener = new EncounterParticipantListAdapter.RowTouchListener() {
        @Override public void onTouch(View v, MotionEvent event, int position) {
            if (event.getAction() == MotionEvent.ACTION_DOWN && participantList.canHoverRows()
                    && !actionModeController.isActionModeStarted()) {
                participantList.hoverRow(position);
            }
        }
    };

    private DynamicArrayAdapter.OnItemsSwappedListener itemsSwappedListener = new DynamicArrayAdapter.OnItemsSwappedListener() {
        @Override public void onItemsSwapped(int pos1, int pos2) {
            encounter.updateTurnOrders();
            updateDatabase();
        }
    };

    @Override
    protected void onResumeWithoutResult() {
        super.onResumeWithoutResult();
        loadCurrentEncounterFromDatabase();
    }

    private void loadCurrentEncounterFromDatabase() {
        encounter = null;
        long selectedEncounterId = preferences.get(GlobalPrefs.SELECTED_ENCOUNTER_ID, -1L);
        try {
            loadEncounterFromDatabase(selectedEncounterId);
        } catch (Exception e) {
            Log.w(TAG, e);
            loadDefaultEncounter();
        }
    }

    private void loadEncounterFromDatabase(long id) throws IllegalArgumentException {
        NamedList<EncounterParticipant> encounter = encounterDao.find(id, participantDao);
        if (encounter != null) {
            this.encounter = new EncounterViewModel(encounter);
        } else {
            throw new IllegalArgumentException("No encounter with id=" + id);
        }
    }

    private void loadDefaultEncounter() {
        List<IdNamePair> allParties = encounterDao.findAll();
        if (allParties.isEmpty()) {
            addNewEncounter();
        } else {
            long defaultEncounterId = allParties.get(0).getId();
            encounter = new EncounterViewModel(encounterDao.find(defaultEncounterId, participantDao));
        }
        saveCurrentlySelectedEncounter(encounter.getId());
    }

    private void addNewEncounter() {
        try {
            encounter = new EncounterViewModel();
            encounterDao.add(encounter.buildEncounterForModel(), participantDao);
        } catch (DataAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    private void saveCurrentlySelectedEncounter(long encounterId) {
        preferences.put(GlobalPrefs.SELECTED_ENCOUNTER_ID, encounterId);
    }

    @Override
    public void onPause() {
        updateDatabase();
        super.onPause();
    }

    private void updateDatabase() {
        Editable text = encounterNameEditor.getText();
        encounter.setName(text != null ? text.toString() : "");
        try {
            NamedList<EncounterParticipant> encounter = this.encounter.buildEncounterForModel();
            encounterDao.update(encounter.idNamePair());
            for (EncounterParticipant participant : encounter) {
                participantDao.update(encounter.getId(), participant);
            }
        } catch (DataAccessException e) {
            Log.e(TAG, "Failed to update encounter", e);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.encounters_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mi_start:
                showMenuPopup(R.menu.encounters_start_menu, getActivity().findViewById(R.id.mi_start));
                break;
            case R.id.mi_reset:
                // TODO reset encounter
                break;
            case R.id.mi_check_skill:
                showMenuPopup(R.menu.encounters_check_menu, getActivity().findViewById(R.id.mi_check_skill));
                break;
            case R.id.mi_select_encounter:
                // TODO open selector for encounters (need to rewrite anything?)
                break;
            case R.id.mi_add_participant:
                showAddCharactersPicker();
                break;
            case R.id.mi_new_encounter:
                addNewEncounter();
                refreshParticipantListContent();
                break;
            case R.id.mi_delete_encounter:
                showConfirmDeleteDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showMenuPopup(int menuResource, View anchor) {
        if (anchor == null) {
            anchor = getActivity().findViewById(R.id.overflow_popup_anchor);
        }
        PopupMenu popup = new PopupMenu(getActivity(), anchor);
        popup.inflate(menuResource);

        popup.show();
    }

    private void showAddCharactersPicker() {
        Intent pickerIntent = new PickerUtil.Builder(getContext())
                .setTitle("Add Participants") // TODO strings.xml
                .setSingleChoice(false)
                .setPickableCharacters(getNonSelectedCharacters())
                .setPickableParties(getNonSelectedParties())
                .build();
        startActivityForResult(pickerIntent, ADD_PARTICIPANTS_REQUEST_CODE);
    }

    private List<IdNamePair> getNonSelectedCharacters() {
        CharacterNameDAO charDao = new CharacterNameDAO(getContext());
        List<IdNamePair> characters = charDao.findAll();
        characters.removeAll(getParticipantIdsInEncounter());
        return characters;
    }

    private List<IdNamePair> getParticipantIdsInEncounter() {
        List<IdNamePair> participantIds = Lists.newArrayList();
        for (EncounterParticipantRowModel row : encounter) {
            EncounterParticipant participant = row.getParticipant();
            participantIds.add(new IdNamePair(participant.getId(), participant.getName()));
        }
        return participantIds;
    }

    private List<IdNamePair> getNonSelectedParties() {
        PartyDAO partyDao = new PartyDAO(getContext());
        List<IdNamePair> allPartyIds = partyDao.findAll();

        PartyMemberNameDAO memberDao = new PartyMemberNameDAO(getContext());
        List<IdNamePair> nonSelectedParties = Lists.newArrayList();

        List<IdNamePair> participantsInEncounter = getParticipantIdsInEncounter();
        for (IdNamePair partyId : allPartyIds) {
            NamedList<IdNamePair> party = partyDao.find(partyId.getId(), memberDao);
            party.removeAll(participantsInEncounter);
            if (!party.isEmpty()) {
                nonSelectedParties.add(party.idNamePair());
            }
        }

        return nonSelectedParties;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == ADD_PARTICIPANTS_REQUEST_CODE) {
            PickerUtil.ResultData result = new PickerUtil.ResultData(data);
            Set<IdNamePair> charactersToAdd = getAllCharactersInResult(result);
            Set<EncounterParticipant> newParticipants = createEncounterParticipantsForCharacters(charactersToAdd);

            encounter.addAll(EncounterParticipantRowModel.from(newParticipants));
            encounter.updateTurnOrders();
            refreshParticipantListContent();
        }
    }

    private Set<IdNamePair> getAllCharactersInResult(PickerUtil.ResultData result) {
        Set<IdNamePair> characters = Sets.newHashSet();
        characters.addAll(result.getCharacters());
        List<IdNamePair> parties = result.getParties();
        PartyMemberNameDAO memberDao = new PartyMemberNameDAO(getContext());
        for (IdNamePair party : parties) {
            characters.addAll(memberDao.findAllForOwner(party.getId()));
        }
        return characters;
    }

    private Set<EncounterParticipant> createEncounterParticipantsForCharacters(Collection<IdNamePair> charIds) {
        Set<EncounterParticipant> participants = Sets.newHashSet();
        CharacterModelDAO charDao = new CharacterModelDAO(getContext());
        for (IdNamePair id : charIds) {
            PathfinderCharacter character = charDao.find(id.getId());
            try {
                if (character == null) throw new DataAccessException("No character found for " + id);

                EncounterParticipant participant = new EncounterParticipant(character);
                participantDao.add(encounter.getId(), participant);
                participants.add(participant);
            } catch (DataAccessException e) {
                Log.e(TAG, "Could not add participant " + id + " to encounter " + encounter.getId(), e);
            }
        }
        return participants;
    }

    private void showConfirmDeleteDialog() {
        // TODO
    }

    private void deleteCurrentEncounter() {
        try {
            encounterDao.remove(encounter);
        } catch (DataAccessException e) {
            Log.e(TAG, "Failed to delete encounter with id=" + encounter.getId(), e);
        }
    }

    @Override
    public void updateTitle() {
        setTitle("Encounters"); // TODO strings.xml
        setSubtitle("Encounter name here");
    }
}
