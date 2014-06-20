package com.lateensoft.pathfinder.toolkit.db.repository;

import java.util.Hashtable;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.db.repository.TableAttribute.SQLDataType;
import com.lateensoft.pathfinder.toolkit.model.character.stats.Ability;
import com.lateensoft.pathfinder.toolkit.model.character.stats.AbilitySet;
import com.lateensoft.pathfinder.toolkit.model.character.stats.AbilityType;
import com.lateensoft.pathfinder.toolkit.model.character.stats.ValidatedTypedSet;

public class AbilityRepository extends BaseRepository<Ability> {
	private static final String TABLE = "Ability";
	private static final String ABILITY_KEY = "ability_key";
	private static final String SCORE = "Score";
	private static final String TEMP = "Temp";
	
	public AbilityRepository() {
		super();
		TableAttribute id = new TableAttribute(ABILITY_KEY, SQLDataType.INTEGER, true);
		TableAttribute character_id = new TableAttribute(CHARACTER_ID, SQLDataType.INTEGER);
		TableAttribute score = new TableAttribute(SCORE, SQLDataType.INTEGER);
		TableAttribute Temp = new TableAttribute(TEMP, SQLDataType.INTEGER);
		TableAttribute[] columns = {id, character_id, score, Temp};
		m_tableInfo = new TableInfo(TABLE, columns);
	}
	
	@Override
	protected Ability buildFromHashTable(
			Hashtable<String, Object> hashTable) {
		AbilityType ability = AbilityType.forKey(((Long) hashTable.get(ABILITY_KEY)).intValue());
		long characterId  = (Long) hashTable.get(CHARACTER_ID);
		int score = ((Long) hashTable.get(SCORE)).intValue();
		int temp = ((Long) hashTable.get(TEMP)).intValue();
		return new Ability(ability, characterId, score, temp);
	}

	@Override
	protected ContentValues getContentValues(Ability object) {
		ContentValues values = new ContentValues();
		values.put(ABILITY_KEY, object.getType().getKey());
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
	public Ability query(long ... ids) {
		return super.query(ids);
	}
	
	/**
	 * Returns all abilities for the character with characterId
	 * @return List of Ability, ordered by id
	 */
	public List<Ability> queryAllForCharacter(long characterId) {
		String selector = CHARACTER_ID + "=" + characterId;
		String orderBy = ABILITY_KEY + " ASC";
		String table = m_tableInfo.getTable();
		String[] columns = m_tableInfo.getColumns();
		Cursor cursor = getDatabase().query(true, table, columns, selector, 
				null, null, null, orderBy, null);
		
		List<Ability> abilities = Lists.newArrayListWithCapacity(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Hashtable<String, Object> hashTable =  getTableOfValues(cursor);
			abilities.add(buildFromHashTable(hashTable));
			cursor.moveToNext();
		}
		return abilities;
	}

    public AbilitySet querySet(long characterId) {
        return new AbilitySet(queryAllForCharacter(characterId),
                new ValidatedTypedSet.CorrectionListener<Ability>() {
                    @Override
                    public void onInvalidItemRemoved(Ability removedAbility) {
                        AbilityRepository.this.delete(removedAbility);
                    }

                    @Override
                    public void onMissingItemAdded(Ability addedAbility) {
                        AbilityRepository.this.insert(addedAbility);
                    }
                }
        );
    }

	@Override
	protected String getSelector(Ability object) {
		return getSelector(object.getId(), object.getCharacterID());
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
