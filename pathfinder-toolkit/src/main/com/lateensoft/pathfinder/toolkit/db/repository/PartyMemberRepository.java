package com.lateensoft.pathfinder.toolkit.db.repository;

import java.util.Hashtable;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.db.repository.TableAttribute.SQLDataType;
import com.lateensoft.pathfinder.toolkit.model.party.PartyMember;

public class PartyMemberRepository extends BaseRepository<PartyMember> {
    private static final String TABLE = "PartyMember";
    private static final String ID = "party_member_id";
    private static final String PARTY_ID = "party_id";
    private static final String NAME = "Name";
    private static final String INIT = "Initiative";
    private static final String AC = "AC";
    private static final String TOUCH = "Touch";
    private static final String FLAT_FOOTED = "FlatFooted";
    private static final String SPELL_RESIST = "SpellResist";
    private static final String DMG_REDUCT = "DamageReduction";
    private static final String CMD = "CMD";
    private static final String FORT = "FortSave";
    private static final String REFLEX = "ReflexSave";
    private static final String WILL = "WillSave";
    private static final String BLUFF = "BluffSkillBonus";
    private static final String DISGUISE = "DisguiseSkillBonus";
    private static final String PERCEPT = "PerceptionSkillBonus";
    private static final String SENSE = "SenseMotiveSkillBonus";
    private static final String STEALTH = "StealthSkillBonus";
    private static final String ROLLED = "RolledValue";
    
    public PartyMemberRepository() {
        super();
        TableAttribute id = new TableAttribute(ID, SQLDataType.INTEGER, true);
        TableAttribute partyId = new TableAttribute(PARTY_ID, SQLDataType.INTEGER);
        TableAttribute name = new TableAttribute(NAME, SQLDataType.TEXT);
        TableAttribute init = new TableAttribute(INIT, SQLDataType.INTEGER);
        TableAttribute ac = new TableAttribute(AC, SQLDataType.INTEGER);
        TableAttribute touch = new TableAttribute(TOUCH, SQLDataType.INTEGER);
        TableAttribute flatFooted = new TableAttribute(FLAT_FOOTED, SQLDataType.INTEGER);
        TableAttribute spellResist = new TableAttribute(SPELL_RESIST, SQLDataType.INTEGER);
        TableAttribute dmgRed = new TableAttribute(DMG_REDUCT, SQLDataType.INTEGER);
        TableAttribute cmd = new TableAttribute(CMD, SQLDataType.INTEGER);
        TableAttribute fort = new TableAttribute(FORT, SQLDataType.INTEGER);
        TableAttribute reflex = new TableAttribute(REFLEX, SQLDataType.INTEGER);
        TableAttribute will = new TableAttribute(WILL, SQLDataType.INTEGER);
        TableAttribute bluff = new TableAttribute(BLUFF, SQLDataType.INTEGER);
        TableAttribute disguise = new TableAttribute(DISGUISE, SQLDataType.INTEGER);
        TableAttribute percept = new TableAttribute(PERCEPT, SQLDataType.INTEGER);
        TableAttribute sense = new TableAttribute(SENSE, SQLDataType.INTEGER);
        TableAttribute stealth = new TableAttribute(STEALTH, SQLDataType.INTEGER);
        TableAttribute rolled = new TableAttribute(ROLLED, SQLDataType.INTEGER);
        TableAttribute[] columns = {id, partyId, name, init, ac, touch, flatFooted, spellResist, dmgRed,
                cmd, fort, reflex, will, bluff, disguise, percept, sense, stealth, rolled};
        m_tableInfo = new TableInfo(TABLE, columns);
    }
    
    @Override
    protected PartyMember buildFromHashTable(
            Hashtable<String, Object> hashTable) {
        PartyMember member = new PartyMember((String) hashTable.get(NAME));
        member.setId((Long) hashTable.get(ID));
        member.setPartyID(((Long) hashTable.get(PARTY_ID)).intValue());
        member.setInitiative(((Long) hashTable.get(INIT)).intValue());
        member.setAC(((Long) hashTable.get(AC)).intValue());
        member.setTouch(((Long) hashTable.get(TOUCH)).intValue());
        member.setFlatFooted(((Long) hashTable.get(FLAT_FOOTED)).intValue());
        member.setSpellResist(((Long) hashTable.get(SPELL_RESIST)).intValue());
        member.setDamageReduction(((Long) hashTable.get(DMG_REDUCT)).intValue());
        member.setCMD(((Long) hashTable.get(CMD)).intValue());
        member.setFortSave(((Long) hashTable.get(FORT)).intValue());
        member.setReflexSave(((Long) hashTable.get(REFLEX)).intValue());
        member.setWillSave(((Long) hashTable.get(WILL)).intValue());
        member.setBluffSkillBonus(((Long) hashTable.get(BLUFF)).intValue());
        member.setDisguiseSkillBonus(((Long) hashTable.get(DISGUISE)).intValue());
        member.setPerceptionSkillBonus(((Long) hashTable.get(PERCEPT)).intValue());
        member.setSenseMotiveSkillBonus(((Long) hashTable.get(SENSE)).intValue());
        member.setStealthSkillBonus(((Long) hashTable.get(STEALTH)).intValue());
        member.setLastRolledValue(((Long) hashTable.get(ROLLED)).intValue());
        return member;
    }

    @Override
    protected ContentValues getContentValues(PartyMember object) {
        ContentValues values = new ContentValues();
        if (isIDSet(object)) {
            values.put(ID, object.getId());
        }
        values.put(PARTY_ID, object.getPartyID());
        values.put(NAME, object.getName());
        values.put(INIT, object.getInitiative());
        values.put(AC, object.getAC());
        values.put(TOUCH, object.getTouch());
        values.put(FLAT_FOOTED, object.getFlatFooted());
        values.put(SPELL_RESIST, object.getSpellResist());
        values.put(DMG_REDUCT, object.getDamageReduction());
        values.put(CMD, object.getCMD());
        values.put(FORT, object.getFortSave());
        values.put(REFLEX, object.getReflexSave());
        values.put(WILL, object.getWillSave());
        values.put(BLUFF, object.getBluffSkillBonus());
        values.put(DISGUISE, object.getDisguiseSkillBonus());
        values.put(PERCEPT, object.getPerceptionSkillBonus());
        values.put(SENSE, object.getSenseMotiveSkillBonus());
        values.put(STEALTH, object.getStealthSkillBonus());
        values.put(ROLLED, object.getLastRolledValue());
        return values;
    }
    
    /**
     * Returns all members for the party with partyId
     * @param partyId the unique id of the party
     * @return Array of PartyMember, ordered alphabetically by name
     */
    public List<PartyMember> querySet(long partyId) {
        String selector = PARTY_ID + "=" + partyId;
        String orderBy = NAME + " ASC";
        String table = m_tableInfo.getTable();
        String[] columns = m_tableInfo.getColumns();
        Cursor cursor = getDatabase().query(true, table, columns, selector, 
                null, null, null, orderBy, null);
        
        List<PartyMember> members = Lists.newArrayListWithCapacity(cursor.getCount());
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Hashtable<String, Object> hashTable =  getTableOfValues(cursor);
            members.add(buildFromHashTable(hashTable));
            cursor.moveToNext();
        }
        return members;
    }

}
