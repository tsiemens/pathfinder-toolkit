package com.lateensoft.pathfinder.toolkit.db.repository;

import java.util.Hashtable;

import android.content.ContentValues;
import android.database.Cursor;

import com.lateensoft.pathfinder.toolkit.db.repository.PTTableAttribute.SQLDataType;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTSave;

public class PTSaveRepository extends PTBaseRepository<PTSave> {
	static final String TABLE = "Save";
	static final String SAVE_KEY = "save_key";
	static final String BASE_VALUE = "BaseValue";
	static final String ABILITY_KEY = "ability_key";
	static final String MAGIC_MOD = "MagicMod";
	static final String MISC_MOD = "MiscMod";
	static final String TEMP_MOD = "TempMod";
	
	public PTSaveRepository() {
		super();
		PTTableAttribute saveKey = new PTTableAttribute(SAVE_KEY, SQLDataType.INTEGER, true);
		PTTableAttribute characterId = new PTTableAttribute(CHARACTER_ID, SQLDataType.INTEGER);
		PTTableAttribute baseValue = new PTTableAttribute(BASE_VALUE, SQLDataType.INTEGER);
		PTTableAttribute abilityKey = new PTTableAttribute(ABILITY_KEY, SQLDataType.INTEGER);
		PTTableAttribute magicMod = new PTTableAttribute(MAGIC_MOD, SQLDataType.INTEGER);
		PTTableAttribute miscMod = new PTTableAttribute(MISC_MOD, SQLDataType.INTEGER);
		PTTableAttribute tempMod = new PTTableAttribute(TEMP_MOD, SQLDataType.INTEGER);
		PTTableAttribute[] attributes = {saveKey, characterId, baseValue, abilityKey,
				magicMod, miscMod, tempMod};
		m_tableInfo = new PTTableInfo(TABLE, attributes);
	}

	@Override
	protected PTSave buildFromHashTable(Hashtable<String, Object> hashTable) {
		int saveKey = ((Long) hashTable.get(SAVE_KEY)).intValue();
		int characterId = ((Long) hashTable.get(CHARACTER_ID)).intValue();
		int baseValue = ((Long) hashTable.get(BASE_VALUE)).intValue();
		int abilityKey = ((Long) hashTable.get(ABILITY_KEY)).intValue();
		int magicMod = ((Long) hashTable.get(MAGIC_MOD)).intValue();
		int miscMod = ((Long) hashTable.get(MISC_MOD)).intValue();
		int tempMod = ((Long) hashTable.get(TEMP_MOD)).intValue();
		
		PTSave save = new PTSave(saveKey, characterId, baseValue, abilityKey, magicMod, 
				miscMod, tempMod);
		return save;
	}

	@Override
	protected ContentValues getContentValues(PTSave object) {
		ContentValues values = new ContentValues();
		values.put(SAVE_KEY, object.getSaveKey());
		values.put(CHARACTER_ID, object.getCharacterID());
		values.put(BASE_VALUE, object.getBaseSave());
		values.put(ABILITY_KEY, object.getAbilityKey());
		values.put(MAGIC_MOD, object.getMagicMod());
		values.put(MISC_MOD, object.getMiscMod());
		values.put(TEMP_MOD, object.getTempMod());
		return values;
	}
	
	/**
	 * @param ids must be { save id, character id }
	 * @return ability identified by save id, character id
	 */
	@Override
	public PTSave query(long ... ids) {
		return super.query(ids);
	}
	
	/**
	 * Returns all saves for the character with characterId
	 * @param characterId
	 * @return Array of PTSave, ordered by saveKey
	 */
	public PTSave[] querySet(long characterId) {
		String selector = CHARACTER_ID + "=" + characterId; 
		String orderBy = SAVE_KEY + " ASC";
		String table = m_tableInfo.getTable();
		String[] columns = m_tableInfo.getColumns();
		Cursor cursor = getDatabase().query(true, table, columns, selector, 
				null, null, null, orderBy, null);
		
		PTSave[] saves = new PTSave[cursor.getCount()];
		cursor.moveToFirst();
		int i = 0;
		while (!cursor.isAfterLast()) {
			Hashtable<String, Object> hashTable =  getTableOfValues(cursor);
			saves[i] = buildFromHashTable(hashTable);
			cursor.moveToNext();
			i++;
		}
		return saves;
	}
	
	@Override
	protected String getSelector(PTSave object) {
		return getSelector(object.getID(), object.getCharacterID());
	}

	/**
	 * Return selector for save. 
	 * @param ids must be { save key, character id }
	 */
	@Override
	protected String getSelector(long ... ids) {
		if (ids.length >= 2) {
			return SAVE_KEY + "=" + ids[0] + " AND " + 
					CHARACTER_ID + "=" + ids[1];
		} else {
			throw new IllegalArgumentException("saves require save key and character id to be identified");
		}
	}
}
