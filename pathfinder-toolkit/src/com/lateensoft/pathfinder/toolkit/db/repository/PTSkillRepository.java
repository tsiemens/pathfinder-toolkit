package com.lateensoft.pathfinder.toolkit.db.repository;

import java.util.Hashtable;

import com.lateensoft.pathfinder.toolkit.db.repository.PTTableAttribute.SQLDataType;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTSkill;

import android.content.ContentValues;
import android.database.Cursor;

public class PTSkillRepository extends PTBaseRepository<PTSkill> {
	static final String TABLE = "Skill";
	static final String SKILL_ID = "skill_id";
	static final String CLASS_SKILL = "ClassSkill";
	static final String RANK = "Rank";
	static final String MISC_MOD = "MiscMod";
	static final String ARMOR_CHECK_PENALTY = "ArmorCheckPenalty";
	static final String ABILITY_ID = "AbilityId";
	
	public PTSkillRepository() {
		super();
		PTTableAttribute id = new PTTableAttribute(SKILL_ID, SQLDataType.INTEGER, true);
		PTTableAttribute characterId = new PTTableAttribute(CHARACTER_ID, SQLDataType.INTEGER);
		PTTableAttribute classSkill = new PTTableAttribute(CLASS_SKILL, SQLDataType.INTEGER);
		PTTableAttribute rank = new PTTableAttribute(RANK, SQLDataType.INTEGER);
		PTTableAttribute miscMod = new PTTableAttribute(MISC_MOD, SQLDataType.INTEGER);
		PTTableAttribute keyAbilityKey = new PTTableAttribute(ABILITY_ID, SQLDataType.INTEGER);
		PTTableAttribute armorChk = new PTTableAttribute(ARMOR_CHECK_PENALTY, SQLDataType.INTEGER);
		PTTableAttribute[] attributes = {id, characterId,  classSkill, rank, miscMod,
				armorChk, keyAbilityKey};
		m_tableInfo = new PTTableInfo(TABLE, attributes);
	}
	
	@Override
	protected PTSkill buildFromHashTable(Hashtable<String, Object> hashTable) {
		int id = ((Long) hashTable.get(SKILL_ID)).intValue();
		int characterId = ((Long) hashTable.get(CHARACTER_ID)).intValue();
		boolean classSkill = ((Long) hashTable.get(CLASS_SKILL)).intValue() == 1;
		int rank = ((Long) hashTable.get(RANK)).intValue();
		int miscMod = ((Long) hashTable.get(MISC_MOD)).intValue();
		int armorCheckPenalty = ((Long) hashTable.get(ARMOR_CHECK_PENALTY)).intValue();
		long abilityId = ((Long) hashTable.get(ABILITY_ID));
		PTSkill skill = new PTSkill(id, characterId, classSkill, rank, 
				miscMod, armorCheckPenalty, abilityId);
		return skill;
	}
	
	@Override
	protected ContentValues getContentValues(PTSkill object) {
		ContentValues values = new ContentValues();
		if (isIDSet(object)) {
			values.put(SKILL_ID, object.getID());
		}
		values.put(CHARACTER_ID, object.getCharacterID());
		values.put(CLASS_SKILL, object.isClassSkill());
		values.put(RANK, object.getRank());
		values.put(MISC_MOD, object.getMiscMod());
		values.put(ARMOR_CHECK_PENALTY, object.getArmorCheckPenalty());
		values.put(ABILITY_ID, object.getAbilityId());
		return values;
	}
	
	/**
	 * Returns all skills for the character with characterId
	 * @param characterId
	 * @return Array of PTSkill, ordered alphabetically by name
	 */
	public PTSkill[] querySet(long characterId) {
		String selector = CHARACTER_ID + "=" + characterId; 
		String orderBy = SKILL_ID + " ASC";
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
	
	@Override
	protected String getSelector(PTSkill object) {
		return getSelector(object.getID(), object.getCharacterID());
	}

	/**
	 * Return selector for ability. 
	 * @param ids must be { skill id, character id }
	 */
	@Override
	protected String getSelector(long ... ids) {
		if (ids.length >= 2) {
			return SKILL_ID + "=" + ids[0] + " AND " + 
					CHARACTER_ID + "=" + ids[1];
		} else {
			throw new IllegalArgumentException("skills require skill and character id to be identified");
		}
	}
}
