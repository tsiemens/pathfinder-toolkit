package com.lateensoft.pathfinder.toolkit.views.encounter;

import android.content.Context;
import android.text.Editable;
import android.util.Log;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.db.dao.table.*;
import com.lateensoft.pathfinder.toolkit.model.IdNamePair;
import com.lateensoft.pathfinder.toolkit.model.NamedList;
import com.lateensoft.pathfinder.toolkit.model.character.PathfinderCharacter;
import com.lateensoft.pathfinder.toolkit.model.party.EncounterParticipant;
import com.lateensoft.pathfinder.toolkit.pref.GlobalPrefs;
import com.lateensoft.pathfinder.toolkit.pref.Preferences;
import com.lateensoft.pathfinder.toolkit.views.character.CharacterCombatStatsFragment;
import roboguice.RoboGuice;

import java.util.*;

public class EncounterPresenter {
    private static final String TAG = EncounterPresenter.class.getSimpleName();

    private Context context;
    private Preferences preferences;
    private EncounterDAO encounterDao;
    private EncounterParticipantDAO participantDao;

    private EncounterFragment fragment;

    private EncounterViewModel encounter;
    private EncounterRoller roller;

    private EncounterRoller.SkillCheckType lastSkillCheck;

    public EncounterPresenter(Context context) {
        this.context = context;
        encounterDao = new EncounterDAO(context);
        participantDao = new EncounterParticipantDAO(context);
        preferences = RoboGuice.getInjector(context).getInstance(Preferences.class);
    }

    public void bind(EncounterFragment encounterFragment) {
        fragment = encounterFragment;
        setEncounter(loadCurrentEncounterFromDatabase());
    }

    private void setEncounter(EncounterViewModel encounter) {
        setCurrentlySelectedEncounter(encounter.getId());
        roller = new EncounterRoller(encounter);
        this.encounter = encounter;
        notifyModelChanged();
    }

    private void setCurrentlySelectedEncounter(long encounterId) {
        preferences.put(GlobalPrefs.SELECTED_ENCOUNTER_ID, encounterId);
    }

    private void notifyModelChanged() {
        getFragment().notifyModelChanged();
    }

    private EncounterViewModel loadCurrentEncounterFromDatabase() {
        long selectedEncounterId = preferences.get(GlobalPrefs.SELECTED_ENCOUNTER_ID, -1L);
        try {
            return loadEncounterFromDatabase(selectedEncounterId);
        } catch (Exception e) {
            Log.w(TAG, e);
            return loadDefaultEncounter();
        }
    }

    private EncounterViewModel loadEncounterFromDatabase(long id) throws IllegalArgumentException {
        NamedList<EncounterParticipant> encounter = encounterDao.find(id, participantDao);
        if (encounter != null) {
            return new EncounterViewModel(encounter);
        } else {
            throw new IllegalArgumentException("No encounter with id=" + id);
        }
    }

    private EncounterViewModel loadDefaultEncounter() {
        List<IdNamePair> allParties = encounterDao.findAll();
        if (allParties.isEmpty()) {
            return loadNewEncounter();
        } else {
            long defaultEncounterId = allParties.get(0).getId();
            return new EncounterViewModel(encounterDao.find(defaultEncounterId, participantDao));
        }
    }

    private EncounterViewModel loadNewEncounter() {
        try {
            EncounterViewModel encounter = new EncounterViewModel();
            encounterDao.add(encounter.buildEncounterForModel(), participantDao);
            return encounter;
        } catch (DataAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    public boolean isEncounterOngoing() {
        if (encounter == null) return false;
        for (EncounterParticipantRowModel row : encounter) {
            if (row.getParticipant().getInitiativeScore() > 0) {
                return true;
            }
        }
        return false;
    }

    public void onViewLostFocus() {
        updateDatabase();
    }

    private void updateDatabase() {
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

    private EncounterFragment getFragment() {
        Preconditions.checkState(fragment != null, "Fragment must be bound to presenter using bind()");
        return fragment;
    }

    public EncounterViewModel getModel() {
        return encounter;
    }

    public EncounterRoller.SkillCheckType getLastSkillCheck() {
        return lastSkillCheck;
    }

    public void onEncounterNameEdited(Editable editedName) {
        getModel().setName(editedName != null ? editedName.toString() : "");
    }

    public void onNextTurnSelected() {
        EncounterParticipantRowModel currentTurn = encounter.getCurrentTurn();
        if (currentTurn == null && !encounter.isEmpty()) {
            encounter.setCurrentTurn(encounter.get(0));
        } else if (currentTurn != null) {
            int currentTurnIndex = encounter.indexOf(currentTurn);
            int nextTurnIndex = (currentTurnIndex + 1) % encounter.size();
            encounter.setCurrentTurn(encounter.get(nextTurnIndex));
        }
        notifyModelAttributesChanged();
    }

    public void onParticipantSelected(EncounterParticipantRowModel participant) {
        showParticipantDetails(participant);
    }

    private void showParticipantDetails(EncounterParticipantRowModel row) {
        preferences.put(GlobalPrefs.SELECTED_CHARACTER_ID, row.getParticipant().getId());
        fragment.switchToPage(CharacterCombatStatsFragment.class);
    }

    public void onEncounterSelected(IdNamePair encounterId) {
        setEncounter(loadEncounterFromDatabase(encounterId.getId()));
    }

    public void onCharactersAndPartiesSelectedToAddToEncounter(List<IdNamePair> characters,
                                                               List<IdNamePair> parties) {
        Set<IdNamePair> charactersToAdd = getUniqueCharacters(characters, parties);
        Set<EncounterParticipant> newParticipants = createEncounterParticipantsForCharacters(charactersToAdd);
        getModel().addAll(EncounterParticipantRowModel.listFrom(newParticipants));
        getModel().updateTurnOrders();
        notifyModelChanged();
    }

    private Set<IdNamePair> getUniqueCharacters(List<IdNamePair> characters,
                                                List<IdNamePair> parties) {
        Set<IdNamePair> uniqueCharacters = Sets.newHashSet();
        uniqueCharacters.addAll(characters);
        PartyMemberNameDAO memberDao = new PartyMemberNameDAO(context);
        for (IdNamePair party : parties) {
            uniqueCharacters.addAll(memberDao.findAllForOwner(party.getId()));
        }
        return uniqueCharacters;
    }

    private Set<EncounterParticipant> createEncounterParticipantsForCharacters(Collection<IdNamePair> charIds) {
        Set<EncounterParticipant> participants = Sets.newHashSet();
        CharacterModelDAO charDao = new CharacterModelDAO(context);
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

    public void onParticipantsSelectedForDeletion(List<EncounterParticipantRowModel> participantsToRemove) {
        removeParticipantsFromEncounter(participantsToRemove);
        notifyModelChanged();
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

    public void onParticipantOrdersChanged() {
        getModel().updateTurnOrders();
    }

    public enum EncounterStartMode { AUTO_ROLL, MANUAL }

    public void onEncounterStarted(EncounterStartMode mode) {
        if (mode == EncounterStartMode.AUTO_ROLL) {
            roller.rollInitiatives();
            Collections.sort(encounter, new Comparator<EncounterParticipantRowModel>() {
                @Override public int compare(EncounterParticipantRowModel lhs, EncounterParticipantRowModel rhs) {
                    return rhs.getParticipant().getInitiativeScore() - lhs.getParticipant().getInitiativeScore();
                }
            });
        }
        notifyModelAttributesChanged();
    }

    private void notifyModelAttributesChanged() {
        getFragment().notifyModelAttributesChanged();
    }

    public void onSkillCheckSelected(EncounterRoller.SkillCheckType checkType) {
        lastSkillCheck = checkType;
        roller.rollSkillChecks(checkType);
        notifyModelAttributesChanged();
    }

    public void onEncounterResetSelected() {
        roller.resetInitiatives();
        roller.resetLastSkillChecks();
        lastSkillCheck = null;
        encounter.setCurrentTurn(null);
        notifyModelAttributesChanged();
    }

    public List<IdNamePair> getSelectableEncounters() {
        updateDatabase();
        List<IdNamePair> encounters = encounterDao.findAll();
        Collections.sort(encounters);
        return encounters;
    }

    public List<IdNamePair> getCharactersAvailableToAddToEncounter() {
        return getCharactersNotInEncounter();
    }

    private List<IdNamePair> getCharactersNotInEncounter() {
        CharacterNameDAO charDao = new CharacterNameDAO(context);
        List<IdNamePair> characters = charDao.findAll();
        characters.removeAll(getModel().getParticipantIds());
        return characters;
    }

    public List<IdNamePair> getPartiesAvailableToAddToEncounter() {
        return getPartiesNotEntirelyInEncounter();
    }

    private List<IdNamePair> getPartiesNotEntirelyInEncounter() {
        PartyDAO partyDao = new PartyDAO(context);
        List<IdNamePair> allPartyIds = partyDao.findAll();

        PartyMemberNameDAO memberDao = new PartyMemberNameDAO(context);
        List<IdNamePair> nonSelectedParties = Lists.newArrayList();

        List<IdNamePair> participantsInEncounter = getModel().getParticipantIds();
        for (IdNamePair partyId : allPartyIds) {
            NamedList<IdNamePair> party = partyDao.find(partyId.getId(), memberDao);
            party.removeAll(participantsInEncounter);
            if (!party.isEmpty()) {
                nonSelectedParties.add(party.idNamePair());
            }
        }

        return nonSelectedParties;
    }

    public void onDeleteCurrentEncounterSelected() {
        deleteCurrentEncounter();
        setEncounter(loadDefaultEncounter());
    }

    private void deleteCurrentEncounter() {
        try {
            encounterDao.remove(encounter);
        } catch (DataAccessException e) {
            Log.e(TAG, "Failed to delete encounter with id=" + encounter.getId(), e);
        }
    }

    public void onNewEncounterSelected() {
        setEncounter(loadNewEncounter());
    }
}
