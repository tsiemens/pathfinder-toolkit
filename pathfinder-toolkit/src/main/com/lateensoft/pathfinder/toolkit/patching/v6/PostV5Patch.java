package com.lateensoft.pathfinder.toolkit.patching.v6;

import android.content.Context;
import android.util.Log;
import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.db.dao.table.*;
import com.lateensoft.pathfinder.toolkit.deprecated.v1.PTUserPrefsManager;
import com.lateensoft.pathfinder.toolkit.deprecated.v1.db.PTDatabaseManager;
import com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.PTCharacter;
import com.lateensoft.pathfinder.toolkit.deprecated.v1.model.party.PTParty;
import com.lateensoft.pathfinder.toolkit.deprecated.v1.model.party.PTPartyMember;
import com.lateensoft.pathfinder.toolkit.model.NamedList;
import com.lateensoft.pathfinder.toolkit.model.character.PathfinderCharacter;
import com.lateensoft.pathfinder.toolkit.model.party.Encounter;
import com.lateensoft.pathfinder.toolkit.model.party.EncounterParticipant;
import com.lateensoft.pathfinder.toolkit.patching.Patch;
import com.lateensoft.pathfinder.toolkit.patching.v10.PreV10PartyMember;
import com.lateensoft.pathfinder.toolkit.patching.v10.PreV10PartyMemberConverter;
import com.lateensoft.pathfinder.toolkit.pref.GlobalPrefs;
import com.lateensoft.pathfinder.toolkit.pref.Preferences;
import org.jetbrains.annotations.Nullable;
import roboguice.RoboGuice;

/**
 * In version 6, the database was overhauled, so old values must be extracted from that database, and migrated
 * to the new one, using the old deprecated data models. The old database must be deleted once complete.
 *
 * ID's in shared preferences were changed from int to long, so those must be deleted and recreated.
 *
 * Encounter party in shared preference is now in database
 *
 * Delete last tab in shared prefs
 */
public class PostV5Patch extends Patch {
    private static final String TAG = PostV5Patch.class.getSimpleName();

    private Context appContext;
    private Preferences preferences;

    private CharacterModelDAO characterDao;

    private PartyDAO<Long> partyDao;
    private EncounterDAO<EncounterParticipant> encounterDao;

    private PTUserPrefsManager v5Preferences;
    private PTDatabaseManager v5DBManager;

    private boolean errorsEncountered = false;

    public PostV5Patch(Context context) {
        super(context);
        appContext = context.getApplicationContext();
        preferences = RoboGuice.getInjector(context).getInstance(Preferences.class);
        characterDao = new CharacterModelDAO(context);

        PartyMemberIdDAO memberIdDAO = new PartyMemberIdDAO(context);
        partyDao = new PartyDAO<Long>(context, memberIdDAO);

        EncounterParticipantDAO participantDao = new EncounterParticipantDAO(context);
        encounterDao = new EncounterDAO<EncounterParticipant>(context, participantDao);

        v5Preferences = new PTUserPrefsManager(appContext);
        v5DBManager = new PTDatabaseManager(appContext);
    }

    @Override
    public boolean apply() {
        Log.i(TAG, "Applying v5 patches...");

        convertCharacters();
        convertParties();
        convertEncounter();

        v5Preferences.remove(PTUserPrefsManager.KEY_SHARED_PREFS_LAST_TAB);

        appContext.deleteDatabase(PTDatabaseManager.dbName);

        Log.i(TAG, "v5 patch complete");
        return !errorsEncountered;
    }

    private void convertCharacters() {
        int oldSelectedCharacterID = getOldSelectedCharacterId();
        // Delete, because need to convert to long later
        v5Preferences.remove(PTUserPrefsManager.KEY_SHARED_PREFS_SELECTED_CHARACTER);

        int[] oldCharIDs = v5DBManager.getCharacterIDs();
        for (int id : oldCharIDs) {
            PTCharacter oldChar = v5DBManager.getCharacter(id);
            PathfinderCharacter newChar = PreV6CharacterConverter.convertCharacter(oldChar);

            try {
                characterDao.add(newChar);
                if (id == oldSelectedCharacterID) {
                    preferences.put(GlobalPrefs.SELECTED_CHARACTER_ID, newChar.getId());
                }
            } catch (DataAccessException e) {
                errorsEncountered = true;
                Log.e(TAG, "Error migrating character " + oldChar.getName(), e);
            }
        }
    }

    private int getOldSelectedCharacterId() {
        try {
            return v5Preferences.getSelectedCharacter();
        } catch (ClassCastException e) {
            // Cases in which this has become a long in preferences for unknown reason.
            return Long.valueOf(preferences.getLong(PTUserPrefsManager.KEY_SHARED_PREFS_SELECTED_CHARACTER, -1)).intValue();
        }
    }

    private void convertParties() {
        int oldSelectedPartyID = getOldSelectedPartyId();
        // Delete, because need to convert to long later
        v5Preferences.remove(PTUserPrefsManager.KEY_SHARED_PREFS_SELECTED_PARTY);

        int[] oldPartyIDs = v5DBManager.getPartyIDs();
        for (int id : oldPartyIDs) {
            PTParty oldParty = v5DBManager.getParty(id);
            NamedList<PathfinderCharacter> newParty = convertParty(oldParty);

            try {
                for (PathfinderCharacter character : newParty) {
                    characterDao.add(character);
                }

                long newPartyId = partyDao.add(getPartyWithIdsOnly(newParty));

                if (id == oldSelectedPartyID) {
                    preferences.put(GlobalPrefs.SELECTED_PARTY_ID, newPartyId);
                }
            } catch (DataAccessException e) {
                errorsEncountered = true;
                Log.e(TAG, "Error migrating party " + oldParty.getName(), e);
            }
        }
    }

    private int getOldSelectedPartyId() {
        try {
            return v5Preferences.getSelectedParty();
        } catch (ClassCastException e) {
            return Long.valueOf(preferences.getLong(PTUserPrefsManager.KEY_SHARED_PREFS_SELECTED_PARTY, -1)).intValue();
        }
    }

    private NamedList<PathfinderCharacter> convertParty(PTParty preV6Party) {
        NamedList<PathfinderCharacter> newParty = new NamedList<PathfinderCharacter>(preV6Party.getName());
        for (int i = 0; i < preV6Party.size(); i++) {
            PTPartyMember preV6Member = preV6Party.getPartyMember(i);
            PathfinderCharacter newMember = PreV10PartyMemberConverter.convertPartyMember(convertV6MemberToV10(preV6Member));
            newParty.add(newMember);
        }
        return newParty;
    }

    private PreV10PartyMember convertV6MemberToV10(PTPartyMember deprecatedMember) {
        PreV10PartyMember preV10PartyMember = new PreV10PartyMember();
        preV10PartyMember.name = deprecatedMember.getName();
        preV10PartyMember.initiative = deprecatedMember.getInitiative();
        preV10PartyMember.AC = deprecatedMember.getAC();
        preV10PartyMember.touch = deprecatedMember.getTouch();
        preV10PartyMember.flatFooted = deprecatedMember.getFlatFooted();
        preV10PartyMember.spellResist = deprecatedMember.getSpellResist();
        preV10PartyMember.damageReduction = deprecatedMember.getDamageReduction();
        preV10PartyMember.CMD = deprecatedMember.getCMD();
        preV10PartyMember.fortSave = deprecatedMember.getFortSave();
        preV10PartyMember.reflexSave = deprecatedMember.getReflexSave();
        preV10PartyMember.willSave = deprecatedMember.getWillSave();
        preV10PartyMember.bluffSkillBonus = deprecatedMember.getBluffSkillBonus();
        preV10PartyMember.disguiseSkillBonus = deprecatedMember.getDisguiseSkillBonus();
        preV10PartyMember.perceptionSkillBonus = deprecatedMember.getPerceptionSkillBonus();
        preV10PartyMember.senseMotiveSkillBonus = deprecatedMember.getSenseMotiveSkillBonus();
        preV10PartyMember.stealthSkillBonus = deprecatedMember.getStealthSkillBonus();
        preV10PartyMember.lastRolledValue = deprecatedMember.getRolledValue();
        return preV10PartyMember;
    }

    private NamedList<Long> getPartyWithIdsOnly(NamedList<PathfinderCharacter> modelParty) {
        NamedList<Long> partyWithMemberIds = new NamedList<Long>(modelParty.getName());
        for (PathfinderCharacter character : modelParty) {
            partyWithMemberIds.add(character.getId());
        }
        return partyWithMemberIds;
    }

    private void convertEncounter() {
        PTParty oldEncounter = v5Preferences.getEncounterParty();
        if (oldEncounter != null) {
            try {
                Encounter<EncounterParticipant> newEncounter = convertEncounter(oldEncounter);
                encounterDao.add(newEncounter);

                preferences.put(GlobalPrefs.SELECTED_ENCOUNTER_ID, newEncounter.getId());
            } catch (DataAccessException e) {
                errorsEncountered = true;
                Log.e(TAG, "Error migrating encounter party " + oldEncounter.getName(), e);
            } finally {
                v5Preferences.remove(PTUserPrefsManager.KEY_SHARED_PREFS_ENCOUNTER_PARTY);
            }
        }
    }

    private Encounter<EncounterParticipant> convertEncounter(PTParty preV6Party) {
        Encounter<EncounterParticipant> newEncounter = new Encounter<EncounterParticipant>(preV6Party.getName());
        for (int i = 0; i < preV6Party.size(); i++) {
            PTPartyMember preV6Member = preV6Party.getPartyMember(i);
            EncounterParticipant newMember = PreV10PartyMemberConverter.convertEncounterParticipant(convertV6MemberToV10(preV6Member));
            newEncounter.add(newMember);
            newMember.setTurnOrder(i);
        }
        return newEncounter;
    }

    @Nullable
    @Override
    public Patch getNext() {
        return null;
    }
}
