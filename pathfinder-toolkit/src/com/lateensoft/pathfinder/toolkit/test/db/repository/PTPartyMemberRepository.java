package com.lateensoft.pathfinder.toolkit.db.repository;

import java.util.Hashtable;

import android.content.ContentValues;
import android.database.Cursor;

import com.lateensoft.pathfinder.toolkit.db.repository.PTTableAttribute.SQLDataType;
import com.lateensoft.pathfinder.toolkit.model.party.PTPartyMember;

public class PTPartyMemberRepository extends PTBaseRepository<PTPartyMember> {
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
	
	public PTPartyMemberRepository() {
		super();
		PTTableAttribute id = new PTTableAttribute(ID, SQLDataType.INTEGER, true);
		PTTableAttribute partyId = new PTTableAttribute(PARTY_ID, SQLDataType.INTEGER);
		PTTableAttribute name = new PTTableAttribute(NAME, SQLDataType.TEXT);
		PTTableAttribute init = new PTTableAttribute(INIT, SQLDataType.INTEGER);
		PTTableAttribute ac = new PTTableAttribute(AC, SQLDataType.INTEGER);
		PTTableAttribute touch = new PTTableAttribute(TOUCH, SQLDataType.INTEGER);
		PTTableAttribute flatFooted = new PTTableAttribute(FLAT_FOOTED, SQLDataType.INTEGER);
		PTTableAttribute spellResist = new PTTableAttribute(SPELL_RESIST, SQLDataType.INTEGER);
		PTTableAttribute dmgRed = new PTTableAttribute(DMG_REDUCT, SQLDataType.INTEGER);
		PTTableAttribute cmd = new PTTableAttribute(CMD, SQLDataType.INTEGER);
		PTTableAttribute fort = new PTTableAttribute(FORT, SQLDataType.INTEGER);
		PTTableAttribute reflex = new PTTableAttribute(REFLEX, SQLDataType.INTEGER);
		PTTableAttribute will = new PTTableAttribute(WILL, SQLDataType.INTEGER);
		PTTableAttribute bluff = new PTTableAttribute(BLUFF, SQLDataType.INTEGER);
		PTTableAttribute disguise = new PTTableAttribute(DISGUISE, SQLDataType.INTEGER);
		PTTableAttribute percept = new PTTableAttribute(PERCEPT, SQLDataType.INTEGER);
		PTTableAttribute sense = new PTTableAttribute(SENSE, SQLDataType.INTEGER);
		PTTableAttribute stealth = new PTTableAttribute(STEALTH, SQLDataType.INTEGER);
		PTTableAttribute rolled = new PTTableAttribute(ROLLED, SQLDataType.INTEGER);
		PTTableAttribute[] columns = {id, partyId, name, init, ac, touch, flatFooted, spellResist, dmgRed,
				cmd, fort, reflex, will, bluff, disguise, percept, sense, stealth, rolled};
		m_tableInfo = new PTTableInfo(TABLE, columns);
	}
	
	@Override
	protected PTPartyMember buildFromHashTable(
			Hashtable<String, Object> hashTable) {
		PTPartyMember member = new PTPartyMember((String) hashTable.get(NAME));
		member.setID((Long) hashTable.get(ID));
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
	protected ContentValues getContentValues(PTPartyMember object) {
		ContentValues values = new ContentValues();
		if (isIDSet(object)) {
			values.put(ID, object.getID());
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
	 * @param partyId
	 * @return Array of PTPartyMember, ordered alphabetically by name
	 */
	public PTPartyMember[] querySet(long partyId) {
		String selector = PARTY_ID + "=" + partyId;
		String orderBy = NAME + " ASC";
		String table = m_tableInfo.getTable();
		String[] columns = m_tableInfo.getColumns();
		Cursor cursor = getDatabase().query(true, table, columns, selector, 
				null, null, null, orderBy, null);
		
		PTPartyMember[] members = new PTPartyMember[cursor.getCount()];
		cursor.moveToFirst();
		int i = 0;
		while (!cursor.isAfterLast()) {
			Hashtable<String, Object> hashTable =  getTableOfValues(cursor);
			members[i] = buildFromHashTable(hashTable);
			cursor.moveToNext();
			i++;
		}
		return members;
	}

}
