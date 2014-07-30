package com.lateensoft.pathfinder.toolkit.patching.v10;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.db.Database;
import com.lateensoft.pathfinder.toolkit.db.SQLiteDatabaseHelper;
import com.lateensoft.pathfinder.toolkit.db.TableCreator;
import com.lateensoft.pathfinder.toolkit.db.dao.table.*;
import com.lateensoft.pathfinder.toolkit.model.character.PathfinderCharacter;
import com.lateensoft.pathfinder.toolkit.model.party.Encounter;
import com.lateensoft.pathfinder.toolkit.model.party.EncounterParticipant;
import com.lateensoft.pathfinder.toolkit.patching.Patch;
import com.lateensoft.pathfinder.toolkit.pref.GlobalPrefs;
import com.lateensoft.pathfinder.toolkit.pref.Preferences;
import org.jetbrains.annotations.Nullable;
import roboguice.RoboGuice;

import java.sql.SQLException;
import java.util.List;

/**
 * In version 10, the following changes require patching, if coming from v9 or earlier:
 *
 * - The Name column for characters was moved from the fluff table to the character table
 *
 * - Parties now use characters instead of special member objects, so all old members should now be characters
 *   As a result:
 *
 *      - New tables for encounters, party membership, and encounter participation were added.
 *      - Party members need to be migrated to characters, and the partymembers table deleted
 *      - The party table now does not use the IsEncounterParty column
 */
public class PostV9Patch extends Patch {
    private static final String TAG = PostV9Patch.class.getSimpleName();

    private Preferences preferences;

    private Database database;
    private EncounterDAO<EncounterParticipant> encounterDAO;
    private CharacterModelDAO characterDao;
    private PartyMemberIdDAO partyMemberDao;

    private boolean errorsEncountered = false;

    public PostV9Patch(Context context) {
        super(context);
        preferences = RoboGuice.getInjector(context).getInstance(Preferences.class);

        database = RoboGuice.getInjector(context).getInstance(Database.class);
        EncounterParticipantDAO participantDAO = new EncounterParticipantDAO(context);
        encounterDAO = new EncounterDAO<EncounterParticipant>(context, participantDAO);
        characterDao = new CharacterModelDAO(context);
        partyMemberDao = new PartyMemberIdDAO(context);
    }

    @Override
    public boolean apply() {
        Log.i(TAG, "Applying v9 patches...");

        moveCharacterNameColumn();

        createNewTables();

        convertEncounter();
        /* Unfortunately we can't remove the IsEncounterParty Column, because Party table now has
        * character as a reference. Since DROP COLUMN is unsupported by SQLite, this would require
        * us to rebuild nearly the entire database.
        */

        convertPartyMembers();
        database.execSQL("DROP TABLE PartyMember;");

        Log.i(TAG, "v9 patch complete");
        return !errorsEncountered;
    }

    private void moveCharacterNameColumn() {
        database.execSQL("ALTER TABLE Character " +
                "ADD Name TEXT;");

        Cursor cursor = database.query("FluffInfo", new String[]{"character_id", "Name"}, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("Name", cursor.getString(1));
            database.update("Character", contentValues, String.format("character_id=%d", cursor.getLong(0)));
            cursor.moveToNext();
        }

        cursor.close();
        try {
            new SQLiteDatabaseHelper(database).dropColumn(new TableCreator().createFluffInfo(), "FluffInfo", "Name");
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private void createNewTables() {
        TableCreator tableCreator = new TableCreator();
        database.execSQL(tableCreator.createEncounter());
        database.execSQL(tableCreator.createEncounterParticipant());
        database.execSQL(tableCreator.createPartyMembership());
    }

    private void convertEncounter() {
        try {
            List<Long> encounterIds = findPartyIdsForSelector("InEncounter<>0");
            for (Long encounterId : encounterIds) {
                String partySelector = String.format("party_id=%d", encounterId);
                Cursor partyNameCursor = database.query("Party", new String[] {"Name"}, partySelector);
                partyNameCursor.moveToFirst();
                String encounterName = partyNameCursor.getString(0);
                partyNameCursor.close();

                Encounter<EncounterParticipant> encounter = new Encounter<EncounterParticipant>(encounterName);

                List<PreV10PartyMember> preV10Members = getV9PartyMembersFromDatabase(String.format("party_id=%d", encounterId));
                List<EncounterParticipant> v10Members = convertParticipants(preV10Members);
                encounter.addAll(v10Members);

                encounterDAO.add(encounter);
                preferences.put(GlobalPrefs.SELECTED_ENCOUNTER_ID, encounter.getId());

                database.delete("Party", partySelector);
            }
        } catch (Exception e) {
            errorsEncountered = true;
            Log.e(TAG, "Failed to convert encounter", e);
        }
    }

    private List<Long> findPartyIdsForSelector(String selector) {
        List<Long> ids = Lists.newArrayList();
        Cursor c = database.query("Party", new String[] {"party_id"}, selector);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            ids.add(c.getLong(0));
            c.moveToNext();
        }
        c.close();
        return ids;
    }

    private List<PreV10PartyMember> getV9PartyMembersFromDatabase(String selector) {
        String[] columns = new String[] { "party_member_id",
                "Name",
                "Initiative",
                "AC",
                "Touch",
                "FlatFooted",
                "SpellResist",
                "DamageReduction",
                "CMD",
                "FortSave",
                "ReflexSave",
                "WillSave",
                "BluffSkillBonus",
                "DisguiseSkillBonus",
                "PerceptionSkillBonus",
                "SenseMotiveSkillBonus",
                "StealthSkillBonus",
                "RolledValue"
        };
        Cursor c = database.query("PartyMember", columns, selector);

        List<PreV10PartyMember> partyMembers = Lists.newArrayList();
        c.moveToFirst();
        while (!c.isAfterLast()) {
            partyMembers.add(buildFromCursor(c));
            c.moveToNext();
        }
        c.close();
        return partyMembers;
    }

    protected PreV10PartyMember buildFromCursor(Cursor c) {
        PreV10PartyMember member = new PreV10PartyMember();
        member.name = c.getString(c.getColumnIndex("Name"));
        member.initiative = c.getInt(c.getColumnIndex("Initiative"));
        member.AC = c.getInt(c.getColumnIndex("AC"));
        member.touch = c.getInt(c.getColumnIndex("Touch"));
        member.flatFooted = c.getInt(c.getColumnIndex("FlatFooted"));
        member.spellResist = c.getInt(c.getColumnIndex("SpellResist"));
        member.damageReduction = c.getInt(c.getColumnIndex("DamageReduction"));
        member.CMD = c.getInt(c.getColumnIndex("CMD"));
        member.fortSave = c.getInt(c.getColumnIndex("FortSave"));
        member.reflexSave = c.getInt(c.getColumnIndex("ReflexSave"));
        member.willSave = c.getInt(c.getColumnIndex("WillSave"));
        member.bluffSkillBonus = c.getInt(c.getColumnIndex("BluffSkillBonus"));
        member.disguiseSkillBonus = c.getInt(c.getColumnIndex("DisguiseSkillBonus"));
        member.perceptionSkillBonus = c.getInt(c.getColumnIndex("PerceptionSkillBonus"));
        member.senseMotiveSkillBonus = c.getInt(c.getColumnIndex("SenseMotiveSkillBonus"));
        member.stealthSkillBonus = c.getInt(c.getColumnIndex("StealthSkillBonus"));
        member.lastRolledValue = c.getInt(c.getColumnIndex("RolledValue"));
        return member;
    }

    private List<EncounterParticipant> convertParticipants(List<PreV10PartyMember> preV10Members) {
        List<EncounterParticipant> participants = Lists.newArrayListWithCapacity(preV10Members.size());
        for (PreV10PartyMember member : preV10Members) {
            participants.add(PreV10PartyMemberConverter.convertEncounterParticipant(member));
        }
        return participants;
    }

    private void convertPartyMembers() {
        try {
            List<Long> partyIds = findPartyIdsForSelector(null);
            for (Long partyId : partyIds) {
                try {
                    List<PreV10PartyMember> preV10Members = getV9PartyMembersFromDatabase(String.format("party_id=%d", partyId));
                    List<PathfinderCharacter> members = convertMembers(preV10Members);
                    for (PathfinderCharacter character : members) {
                        characterDao.add(character);
                        partyMemberDao.add(partyId, character.getId());
                    }
                } catch (DataAccessException e) {
                    errorsEncountered = true;
                    Log.e(TAG, "Error migrating party " + partyId, e);
                }
            }
        } catch (Exception e) {
            errorsEncountered = true;
            Log.e(TAG, "Failed to convert parties", e);
        }
    }

    private List<PathfinderCharacter> convertMembers(List<PreV10PartyMember> preV10Members) {
        List<PathfinderCharacter> participants = Lists.newArrayListWithCapacity(preV10Members.size());
        for (PreV10PartyMember member : preV10Members) {
            participants.add(PreV10PartyMemberConverter.convertPartyMember(member));
        }
        return participants;
    }

    @Nullable
    @Override
    public Patch getNext() {
        return null;
    }
}
