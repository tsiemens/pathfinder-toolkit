package com.lateensoft.pathfinder.toolkit.repository;

import java.util.Hashtable;

import android.content.ContentValues;

import com.lateensoft.pathfinder.toolkit.repository.PTTableAttribute.SQLDataType;
import com.lateensoft.pathfinder.toolkit.stats.PTAbilityScore;

public class PTAbilityScoreRepository extends PTBaseRepository<PTAbilityScore> {
	private static final String TABLE = "AbilityScore";
	private static final String ID = "ability_score_id";
	private static final String CHARACTER_ID = "character_id";
	private static final String ABILITY = "Ability";
	private static final String SCORE = "Score";
	
	public PTAbilityScoreRepository() {
		super();
		PTTableAttribute id = new PTTableAttribute(ID, SQLDataType.INTEGER, true);
		PTTableAttribute character_id = new  PTTableAttribute(CHARACTER_ID, SQLDataType.INTEGER);
		PTTableAttribute ability = new PTTableAttribute(ABILITY, SQLDataType.TEXT);
		PTTableAttribute score = new PTTableAttribute(SCORE, SQLDataType.INTEGER);
		PTTableAttribute[] columns = {id, character_id, ability, score};
		mTableInfo = new PTTableInfo(TABLE, columns);
	}
	
	@Override
	protected PTAbilityScore buildFromHashTable(
			Hashtable<String, Object> hashTable) {
		int id = (Integer) hashTable.get(ID);
		int characterId  = (Integer) hashTable.get(CHARACTER_ID);
		String ability = (String) hashTable.get(ABILITY);
		int score = (Integer) hashTable.get(SCORE);
		PTAbilityScore abilityScore = new PTAbilityScore(id, characterId, ability, score);
		return abilityScore;
	}

	@Override
	protected ContentValues getContentValues(PTAbilityScore object) {
		ContentValues values = new ContentValues();
		if (!isIDSet(object)) {
			values.put(ID, object.getID());
		}
		values.put(CHARACTER_ID, object.getCharacterID());
		values.put(ABILITY, object.getAbility());
		values.put(SCORE, object.getScore());
		return values;
	}
}
