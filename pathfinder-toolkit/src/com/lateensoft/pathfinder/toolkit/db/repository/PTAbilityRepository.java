package com.lateensoft.pathfinder.toolkit.db.repository;

import java.util.Hashtable;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.db.repository.PTTableAttribute.SQLDataType;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTAbility;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTAbilitySet;

public class PTAbilityRepository extends PTBaseRepository<PTAbility> {
	private static final String TABLE = "Ability";
	private static final String ABILITY_KEY = "ability_key";
	private static final String SCORE = "Score";
	private static final String TEMP = "Temp";
	
	public PTAbilityRepository() {
		super();
		PTTableAttribute id = new PTTableAttribute(ABILITY_KEY, SQLDataType.INTEGER, true);
		PTTableAttribute character_id = new  PTTableAttribute(CHARACTER_ID, SQLDataType.INTEGER);
		PTTableAttribute score = new PTTableAttribute(SCORE, SQLDataType.INTEGER);
		PTTableAttribute Temp = new PTTableAttribute(TEMP, SQLDataType.INTEGER);
		PTTableAttribute[] columns = {id, character_id, score, Temp};
		m_tableInfo = new PTTableInfo(TABLE, columns);
	}
	
	@Override
	protected PTAbility buildFromHashTable(
			Hashtable<String, Object> hashTable) {
		int abilityKey = ((Long) hashTable.get(ABILITY_KEY)).intValue();
		long characterId  = (Long) hashTable.get(CHARACTER_ID);
		int score = ((Long) hashTable.get(SCORE)).intValue();
		int temp = ((Long) hashTable.get(TEMP)).intValue();
		return new PTAbility(abilityKey, characterId, score, temp);
	}

	@Override
	protected ContentValues getContentValues(PTAbility object) {
		ContentValues values = new ContentValues();
		values.put(ABILITY_KEY, object.getAbilityKey());
		values.put(CHARACTER_ID, object.getCharacterID());
		values.put(SCORE, object.getScore());
		values.put(TEMP, object.getTempBonus());
		return values;
	}
	
	/**
	 * @param ids must be { ability id, character id }
	 * @return ability identified by ability id, character id
	 */
	@Override
	public PTAbility query(long ... ids) {
		return super.query(ids);
	}
	
	/**
	 * Returns all abilities for the character with characterId
	 * @return List of PTAbility, ordered by id
	 */
	public List<PTAbility> queryAllForCharacter(long characterId) {
		String selector = CHARACTER_ID + "=" + characterId;
		String orderBy = ABILITY_KEY + " ASC";
		String table = m_tableInfo.getTable();
		String[] columns = m_tableInfo.getColumns();
		Cursor cursor = getDatabase().query(true, table, columns, selector, 
				null, null, null, orderBy, null);
		
		List<PTAbility> abilities = Lists.newArrayListWithCapacity(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Hashtable<String, Object> hashTable =  getTableOfValues(cursor);
			abilities.add(buildFromHashTable(hashTable));
			cursor.moveToNext();
		}
		return abilities;
	}

    public PTAbilitySet querySet(long characterId) {
        return PTAbilitySet.newValidatedAbilitySet(queryAllForCharacter(characterId),
                new PTAbilitySet.CorrectionListener() {
                    @Override
                    public void onInvalidAbilityRemoved(PTAbility removedAbility) {
                        PTAbilityRepository.this.delete(removedAbility);
                    }

                    @Override
                    public void onMissingAbilityAdded(PTAbility addedAbility) {
                        PTAbilityRepository.this.insert(addedAbility);
                    }
                });
    }

	@Override
	protected String getSelector(PTAbility object) {
		return getSelector(object.getID(), object.getCharacterID());
	}

	/**
	 * Return selector for ability. 
	 * @param ids must be { ability key, character id }
	 */
	@Override
	protected String getSelector(long ... ids) {
		if (ids.length >= 2) {
			return ABILITY_KEY + "=" + ids[0] + " AND " + 
					CHARACTER_ID + "=" + ids[1];
		} else {
			throw new IllegalArgumentException("abilities require ability key and character id to be identified");
		}
	}
	
	
}
