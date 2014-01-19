package com.lateensoft.pathfinder.toolkit.db.repository;

import java.util.Hashtable;

import android.content.ContentValues;
import android.database.Cursor;

import com.lateensoft.pathfinder.toolkit.db.repository.PTTableAttribute.SQLDataType;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTAbility;

public class PTAbilityScoreRepository extends PTBaseRepository<PTAbility> {
	private static final String TABLE = "Ability";
	private static final String ABILITY_ID = "ability_id";
	private static final String SCORE = "Score";
	private static final String TEMP = "Temp";
	
	public PTAbilityScoreRepository() {
		super();
		PTTableAttribute id = new PTTableAttribute(ABILITY_ID, SQLDataType.INTEGER, true);
		PTTableAttribute character_id = new  PTTableAttribute(CHARACTER_ID, SQLDataType.INTEGER);
		PTTableAttribute score = new PTTableAttribute(SCORE, SQLDataType.INTEGER);
		PTTableAttribute Temp = new PTTableAttribute(TEMP, SQLDataType.INTEGER);
		PTTableAttribute[] columns = {id, character_id, score, Temp};
		m_tableInfo = new PTTableInfo(TABLE, columns);
	}
	
	@Override
	protected PTAbility buildFromHashTable(
			Hashtable<String, Object> hashTable) {
		long id = (Long) hashTable.get(ABILITY_ID);
		long characterId  = (Long) hashTable.get(CHARACTER_ID);
		int score = ((Long) hashTable.get(SCORE)).intValue();
		int temp = ((Long) hashTable.get(TEMP)).intValue();
		return new PTAbility(id, characterId, score, temp);
	}

	@Override
	protected ContentValues getContentValues(PTAbility object) {
		ContentValues values = new ContentValues();
		values.put(ABILITY_ID, object.getID());
		values.put(CHARACTER_ID, object.getCharacterID());
		values.put(SCORE, object.getScore());
		values.put(TEMP, object.getTempScore());
		return values;
	}
	
	/**
	 * Returns all abilities for the character with characterId
	 * @param characterId
	 * @return Array of PTAbility, ordered by id
	 */
	public PTAbility[] querySet(long characterId) {
		String selector = CHARACTER_ID + "=" + characterId;
		String orderBy = ABILITY_ID + " ASC";
		String table = m_tableInfo.getTable();
		String[] columns = m_tableInfo.getColumns();
		Cursor cursor = getDatabase().query(true, table, columns, selector, 
				null, null, null, orderBy, null);
		
		PTAbility[] scores = new PTAbility[cursor.getCount()];
		cursor.moveToFirst();
		int i = 0;
		while (!cursor.isAfterLast()) {
			Hashtable<String, Object> hashTable =  getTableOfValues(cursor);
			scores[i] = buildFromHashTable(hashTable);
			cursor.moveToNext();
			i++;
		}
		return scores;
	}

	@Override
	protected String getSelector(PTAbility object) {
		return getSelector(object.getID(), object.getCharacterID());
	}

	/**
	 * Return selector for ability. 
	 * @param ids must be { ability id, character id }
	 */
	@Override
	protected String getSelector(long ... ids) {
		if (ids.length >= 2) {
			return ABILITY_ID + "=" + ids[0] + ", " + 
					CHARACTER_ID + "=" + ids[1];
		} else {
			throw new IllegalArgumentException("abilities require character and ability id to be identified");
		}
	}
	
	
}
