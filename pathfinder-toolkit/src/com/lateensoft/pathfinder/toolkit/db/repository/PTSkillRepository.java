package com.lateensoft.pathfinder.toolkit.db.repository;

import java.util.Hashtable;

import com.lateensoft.pathfinder.toolkit.db.repository.PTTableAttribute.SQLDataType;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTSkill;

import android.content.ContentValues;

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
		PTTableAttribute miscMod = new PTTableAttribute(MISC_MOD, SQLDataType.INTEGER);
		PTTableAttribute keyAbilityKey = new PTTableAttribute(KEY_ABILITY_KEY, SQLDataType.INTEGER);
		PTTableAttribute[] attributes = {id, characterId, name, classSkill, keyAbility, abilityMod,
				miscMod, keyAbilityKey};
		mTableInfo = new PTTableInfo(TABLE, attributes);
	}
	
	@Override
	protected PTSkill buildFromHashTable(Hashtable<String, Object> hashTable) {
		int id = (Integer) hashTable.get(ID);
		int characterId = (Integer) hashTable.get(CHARACTER_ID);
		String name = (String) hashTable.get(NAME);
		boolean classSkill = ((Integer) hashTable.get(CLASS_SKILL)) == 1;
		String keyAbility = (String) hashTable.get(KEY_ABILITY);
		int abilityMod = (Integer) hashTable.get(ABILITY_MOD);
		int rank = (Integer) hashTable.get(RANK);
		int miscMod = (Integer) hashTable.get(MISC_MOD);
		int armorCheckPenalty = (Integer) hashTable.get(ARMOR_CHECK_PENALTY);
		int keyAbilityKey = (Integer) hashTable.get(KEY_ABILITY_KEY);
		PTSkill skill = new PTSkill(id, characterId, name, classSkill, abilityMod, rank, 
				miscMod, armorCheckPenalty, keyAbilityKey, keyAbility);
		return skill;
	}
	
	@Override
	protected ContentValues getContentValues(PTSkill object) {
		ContentValues values = new ContentValues();
		if (!isIDSet(object)) {
			values.put(ID, object.getID());
		}
		values.put(CHARACTER_ID, object.getCharacterID());
		values.put(NAME, object.getName());
		values.put(CLASS_SKILL, object.isClassSkill());
		values.put(KEY_ABILITY, object.getKeyAbility());
		values.put(ABILITY_MOD, object.getAbilityMod());
		values.put(MISC_MOD, object.getMiscMod());
		values.put(ARMOR_CHECK_PENALTY, object.getArmorCheckPenalty());
		values.put(KEY_ABILITY_KEY, object.getKeyAbilityKey());
		return values;
	}
}
