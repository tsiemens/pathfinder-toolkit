package com.lateensoft.pathfinder.toolkit.db.repository;

import java.util.Hashtable;

import com.lateensoft.pathfinder.toolkit.db.repository.PTTableAttribute.SQLDataType;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTSkill;

import android.content.ContentValues;
import android.database.Cursor;

public class PTSkillRepository extends PTBaseRepository<PTSkill> {
	static final String TABLE = "Skill";
	static final String ID = "skill_id";
	static final String NAME = "Name";
	static final String CLASS_SKILL = "ClassSkill";
	static final String KEY_ABILITY = "KeyAbility";
	static final String ABILITY_MOD = "AbilityMod";
	static final String RANK = "Rank";
	static final String MISC_MOD = "MiscMod";
	static final String ARMOR_CHECK_PENALTY = "ArmorCheckPenalty";
	static final String KEY_ABILITY_KEY = "KeyAbilityKey";
	
	public PTSkillRepository() {
		super();
		PTTableAttribute id = new PTTableAttribute(ID, SQLDataType.INTEGER, true);
		PTTableAttribute characterId = new PTTableAttribute(CHARACTER_ID, SQLDataType.INTEGER);
		PTTableAttribute name = new PTTableAttribute(NAME, SQLDataType.TEXT);
		PTTableAttribute classSkill = new PTTableAttribute(CLASS_SKILL, SQLDataType.INTEGER);
		PTTableAttribute keyAbility = new PTTableAttribute(KEY_ABILITY, SQLDataType.TEXT);
		PTTableAttribute abilityMod = new PTTableAttribute(ABILITY_MOD, SQLDataType.INTEGER);
		PTTableAttribute rank = new PTTableAttribute(RANK, SQLDataType.INTEGER);
		PTTableAttribute miscMod = new PTTableAttribute(MISC_MOD, SQLDataType.INTEGER);
		PTTableAttribute keyAbilityKey = new PTTableAttribute(KEY_ABILITY_KEY, SQLDataType.INTEGER);
		PTTableAttribute armorChk = new PTTableAttribute(ARMOR_CHECK_PENALTY, SQLDataType.INTEGER);
		PTTableAttribute[] attributes = {id, characterId, name, classSkill, keyAbility, abilityMod,
				rank, miscMod, armorChk, keyAbilityKey};
		m_tableInfo = new PTTableInfo(TABLE, attributes);
	}
	
	@Override
	protected PTSkill buildFromHashTable(Hashtable<String, Object> hashTable) {
		int id = ((Long) hashTable.get(ID)).intValue();
		int characterId = ((Long) hashTable.get(CHARACTER_ID)).intValue();
		String name = (String) hashTable.get(NAME);
		boolean classSkill = ((Long) hashTable.get(CLASS_SKILL)).intValue() == 1;
		String keyAbility = (String) hashTable.get(KEY_ABILITY);
		int abilityMod = ((Long) hashTable.get(ABILITY_MOD)).intValue();
		int rank = ((Long) hashTable.get(RANK)).intValue();
		int miscMod = ((Long) hashTable.get(MISC_MOD)).intValue();
		int armorCheckPenalty = ((Long) hashTable.get(ARMOR_CHECK_PENALTY)).intValue();
		int keyAbilityKey = ((Long) hashTable.get(KEY_ABILITY_KEY)).intValue();
		PTSkill skill = new PTSkill(id, characterId, name, classSkill, abilityMod, rank, 
				miscMod, armorCheckPenalty, keyAbilityKey, keyAbility);
		return skill;
	}
	
	@Override
	protected ContentValues getContentValues(PTSkill object) {
		ContentValues values = new ContentValues();
		if (isIDSet(object)) {
			values.put(ID, object.getID());
		}
		values.put(CHARACTER_ID, object.getCharacterID());
		values.put(NAME, object.getName());
		values.put(CLASS_SKILL, object.isClassSkill());
		values.put(KEY_ABILITY, object.getKeyAbility());
		values.put(ABILITY_MOD, object.getAbilityMod());
		values.put(RANK, object.getRank());
		values.put(MISC_MOD, object.getMiscMod());
		values.put(ARMOR_CHECK_PENALTY, object.getArmorCheckPenalty());
		values.put(KEY_ABILITY_KEY, object.getKeyAbilityKey());
		return values;
	}
	
	/**
	 * Returns all skills for the character with characterId
	 * @param characterId
	 * @return Array of PTSkill, ordered alphabetically by name
	 */
	public PTSkill[] querySet(long characterId) {
		String selector = CHARACTER_ID + "=" + characterId; 
		String orderBy = NAME + " ASC";
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
