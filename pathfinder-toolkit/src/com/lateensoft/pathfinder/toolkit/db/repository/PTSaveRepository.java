package com.lateensoft.pathfinder.toolkit.db.repository;

import java.util.Hashtable;

import android.content.ContentValues;

import com.lateensoft.pathfinder.toolkit.db.repository.PTTableAttribute.SQLDataType;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTSave;

public class PTSaveRepository extends PTBaseRepository<PTSave> {
	static final String TABLE = "Save";
	static final String ID = "save_id";
	static final String CHARACTER_ID = "character_id";
	static final String NAME = "Name";
	static final String BASE_VALUE = "BaseValue";
	static final String TOTAL = "Total";
	static final String ABILITY_MOD = "AbilityMod";
	static final String MAGIC_MOD = "MagicMod";
	static final String MISC_MOD = "MiscMod";
	static final String TEMP_MOD = "TempMod";
	
	public PTSaveRepository() {
		super();
		PTTableAttribute id = new PTTableAttribute(ID, SQLDataType.INTEGER, true);
		PTTableAttribute characterId = new PTTableAttribute(CHARACTER_ID, SQLDataType.INTEGER);
		PTTableAttribute name = new PTTableAttribute(NAME, SQLDataType.TEXT);
		PTTableAttribute baseValue = new PTTableAttribute(BASE_VALUE, SQLDataType.INTEGER);
		PTTableAttribute total = new PTTableAttribute(TOTAL, SQLDataType.INTEGER);
		PTTableAttribute abilityMod = new PTTableAttribute(ABILITY_MOD, SQLDataType.INTEGER);
		PTTableAttribute magicMod = new PTTableAttribute(MAGIC_MOD, SQLDataType.INTEGER);
		PTTableAttribute miscMod = new PTTableAttribute(MISC_MOD, SQLDataType.INTEGER);
		PTTableAttribute tempMod = new PTTableAttribute(TEMP_MOD, SQLDataType.INTEGER);
		PTTableAttribute[] attributes = {id, characterId, name, baseValue, total, abilityMod,
				magicMod, miscMod, tempMod};
		m_tableInfo = new PTTableInfo(TABLE, attributes);
	}

	@Override
	protected PTSave buildFromHashTable(Hashtable<String, Object> hashTable) {
		int id = ((Long) hashTable.get(ID)).intValue();
		int characterId = ((Long) hashTable.get(CHARACTER_ID)).intValue();
		String name = (String) hashTable.get(NAME);
		int baseValue = ((Long) hashTable.get(BASE_VALUE)).intValue();
		int abilityMod = ((Long) hashTable.get(ABILITY_MOD)).intValue();
		int magicMod = ((Long) hashTable.get(MAGIC_MOD)).intValue();
		int miscMod = ((Long) hashTable.get(MISC_MOD)).intValue();
		int tempMod = ((Long) hashTable.get(TEMP_MOD)).intValue();
		
		PTSave save = new PTSave(id, characterId, name, baseValue, abilityMod, magicMod, 
				miscMod, tempMod);
		return save;
	}

	@Override
	protected ContentValues getContentValues(PTSave object) {
		ContentValues values = new ContentValues();
		if(isIDSet(object)) { 
			values.put(ID, object.getID());
		}
		values.put(CHARACTER_ID, object.getCharacterID());
		values.put(NAME, object.getName());
		values.put(BASE_VALUE, object.getBaseValue());
		values.put(TOTAL, object.getTotal());
		values.put(ABILITY_MOD, object.getAbilityMod());
		values.put(MAGIC_MOD, object.getMagicMod());
		values.put(MISC_MOD, object.getMiscMod());
		values.put(TEMP_MOD, object.getTempMod());
		return values;
	}
}
