package com.lateensoft.pathfinder.toolkit.db.repository;

import java.util.Hashtable;

import android.content.ContentValues;
import android.database.Cursor;

import com.lateensoft.pathfinder.toolkit.db.repository.PTTableAttribute.SQLDataType;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTAbilityScore;

public class PTAbilityScoreRepository extends PTBaseRepository<PTAbilityScore> {
	private static final String TABLE = "AbilityScore";
	private static final String ID = "ability_score_id";
	private static final String CHARACTER_ID = "character_id";
	private static final String ABILITY = "Ability";
	private static final String SCORE = "Score";
	private static final String IS_TEMP = "IsTemp";
	
	public PTAbilityScoreRepository() {
		super();
		PTTableAttribute id = new PTTableAttribute(ID, SQLDataType.INTEGER, true);
		PTTableAttribute character_id = new  PTTableAttribute(CHARACTER_ID, SQLDataType.INTEGER);
		PTTableAttribute ability = new PTTableAttribute(ABILITY, SQLDataType.TEXT);
		PTTableAttribute score = new PTTableAttribute(SCORE, SQLDataType.INTEGER);
		PTTableAttribute isTemp = new PTTableAttribute(IS_TEMP, SQLDataType.INTEGER);
		PTTableAttribute[] columns = {id, character_id, ability, score, isTemp};
		m_tableInfo = new PTTableInfo(TABLE, columns);
	}
	
	@Override
	protected PTAbilityScore buildFromHashTable(
			Hashtable<String, Object> hashTable) {
		long id = (Long) hashTable.get(ID);
		long characterId  = (Long) hashTable.get(CHARACTER_ID);
		String ability = (String) hashTable.get(ABILITY);
		int score = ((Long) hashTable.get(SCORE)).intValue();
		PTAbilityScore abilityScore = new PTAbilityScore(id, characterId, ability, score);
		return abilityScore;
	}
	
	/**
	 * Inserts the object into the database
	 * Sets the ID of the object if it is successfully added.
	 * @param object
	 * @return the ID of the object, or -1 if error occured
	 */
	public long insert(PTAbilityScore object, boolean isTemp) {
		ContentValues values = getContentValues(object);
		values.put(IS_TEMP, isTemp);
		String table = m_tableInfo.getTable();
		long id = getDatabase().insert(table, values);
		if (id != -1 && !isIDSet(object)) {
			object.setID(id);
		}
		return id;
	}
	
	/**
	 * Same as insert(object, false)
	 */
	@Override
	public long insert(PTAbilityScore object) {
		return insert(object, false);
	}

	@Override
	protected ContentValues getContentValues(PTAbilityScore object) {
		ContentValues values = new ContentValues();
		if (isIDSet(object)) {
			values.put(ID, object.getID());
		}
		values.put(CHARACTER_ID, object.getCharacterID());
		values.put(ABILITY, object.getAbility());
		values.put(SCORE, object.getScore());
		return values;
	}
	
	/**
	 * Returns all spells for the character with characterId
	 * @param characterId
	 * @return Array of PTSpell, ordered by level, then alphabetically by name
	 */
	public PTAbilityScore[] querySet(long characterId, boolean isTemp) {
		String selector = CHARACTER_ID + "=" + characterId
				+" AND " + IS_TEMP+(isTemp?"<>":"=")+"0"; 
		String orderBy = ID + " ASC";
		String table = m_tableInfo.getTable();
		String[] columns = m_tableInfo.getColumns();
		Cursor cursor = getDatabase().query(true, table, columns, selector, 
				null, null, null, orderBy, null);
		
		PTAbilityScore[] scores = new PTAbilityScore[cursor.getCount()];
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
}
