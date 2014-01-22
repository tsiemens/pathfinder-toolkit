package com.lateensoft.pathfinder.toolkit.db.repository;

import java.util.Hashtable;

import com.lateensoft.pathfinder.toolkit.db.repository.PTTableAttribute.SQLDataType;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTSkill;

import android.content.ContentValues;
import android.database.Cursor;

public class PTSkillRepository extends PTBaseRepository<PTSkill> {
	private static final String TABLE = "Skill";
	private static final String SKILL_ID = "skill_id";
	private static final String SKILL_KEY = "skill_key";
	private static final String SUB_TYPE = "SubType";
	private static final String CLASS_SKILL = "ClassSkill";
	private static final String RANK = "Rank";
	private static final String MISC_MOD = "MiscMod";
	private static final String ARMOR_CHECK_PENALTY = "ArmorCheckPenalty";
	private static final String ABILITY_KEY = "ability_key";
	
	public PTSkillRepository() {
		super();
		PTTableAttribute id = new PTTableAttribute(SKILL_ID, SQLDataType.INTEGER, true);
		PTTableAttribute skillKey = new PTTableAttribute(SKILL_KEY, SQLDataType.INTEGER);
		PTTableAttribute characterId = new PTTableAttribute(CHARACTER_ID, SQLDataType.INTEGER);
		PTTableAttribute subType = new PTTableAttribute(SUB_TYPE, SQLDataType.TEXT);
		PTTableAttribute classSkill = new PTTableAttribute(CLASS_SKILL, SQLDataType.INTEGER);
		PTTableAttribute rank = new PTTableAttribute(RANK, SQLDataType.INTEGER);
		PTTableAttribute miscMod = new PTTableAttribute(MISC_MOD, SQLDataType.INTEGER);
		PTTableAttribute abilityKey = new PTTableAttribute(ABILITY_KEY, SQLDataType.INTEGER);
		PTTableAttribute armorChk = new PTTableAttribute(ARMOR_CHECK_PENALTY, SQLDataType.INTEGER);
		PTTableAttribute[] attributes = {id, skillKey, characterId, subType, classSkill, rank, miscMod,
				armorChk, abilityKey};
		m_tableInfo = new PTTableInfo(TABLE, attributes);
	}
	
	@Override
	protected PTSkill buildFromHashTable(Hashtable<String, Object> hashTable) {
		long id = ((Long) hashTable.get(SKILL_ID));
		int skillKey = ((Long) hashTable.get(SKILL_KEY)).intValue();
		int characterId = ((Long) hashTable.get(CHARACTER_ID)).intValue();
		String subType = (String) hashTable.get(SUB_TYPE);
		boolean classSkill = ((Long) hashTable.get(CLASS_SKILL)).intValue() == 1;
		int rank = ((Long) hashTable.get(RANK)).intValue();
		int miscMod = ((Long) hashTable.get(MISC_MOD)).intValue();
		int armorCheckPenalty = ((Long) hashTable.get(ARMOR_CHECK_PENALTY)).intValue();
		int abilityKey = ((Long) hashTable.get(ABILITY_KEY)).intValue();
		PTSkill skill = new PTSkill(id, characterId, skillKey, subType, classSkill, rank, 
				miscMod, armorCheckPenalty, abilityKey);
		return skill;
	}
	
	@Override
	protected ContentValues getContentValues(PTSkill object) {
		ContentValues values = new ContentValues();
		if (isIDSet(object)) {
			values.put(SKILL_ID, object.getID());
		}
		values.put(SKILL_KEY, object.getSkillKey());
		values.put(CHARACTER_ID, object.getCharacterID());
		if (object.getSubType() != null) {
			values.put(SUB_TYPE, object.getSubType());
		}
		values.put(CLASS_SKILL, object.isClassSkill());
		values.put(RANK, object.getRank());
		values.put(MISC_MOD, object.getMiscMod());
		values.put(ARMOR_CHECK_PENALTY, object.getArmorCheckPenalty());
		values.put(ABILITY_KEY, object.getAbilityKey());
		return values;
	}
	
	/**
	 * Returns all skills for the character with characterId
	 * @param characterId
	 * @return Array of PTSkill, ordered alphabetically by name
	 */
	public PTSkill[] querySet(long characterId) {
		String selector = CHARACTER_ID + "=" + characterId; 
		String orderBy = SKILL_KEY+" ASC, "+SUB_TYPE+" ASC";
		String table = m_tableInfo.getTable();
		String[] columns = m_tableInfo.getColumns();
		Cursor cursor = getDatabase().query(true, table, columns, selector, 
				null, null, null, orderBy, null);
		
		PTSkill[] skills = new PTSkill[cursor.getCount()];
		cursor.moveToFirst();
		int i = 0;
		while (!cursor.isAfterLast()) {
			Hashtable<String, Object> hashTable =  getTableOfValues(cursor);
			skills[i] = buildFromHashTable(hashTable);
			cursor.moveToNext();
			i++;
		}
		return skills;
	}
}
