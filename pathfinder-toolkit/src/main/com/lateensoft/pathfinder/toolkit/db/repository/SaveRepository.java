package com.lateensoft.pathfinder.toolkit.db.repository;

import java.util.Hashtable;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.db.repository.TableAttribute.SQLDataType;
import com.lateensoft.pathfinder.toolkit.model.character.stats.*;

public class SaveRepository extends BaseRepository<Save> {
	static final String TABLE = "Save";
	static final String SAVE_KEY = "save_key";
	static final String BASE_VALUE = "BaseValue";
	static final String ABILITY_KEY = "ability_key";
	static final String MAGIC_MOD = "MagicMod";
	static final String MISC_MOD = "MiscMod";
	static final String TEMP_MOD = "TempMod";
	
	public SaveRepository() {
		super();
		TableAttribute saveKey = new TableAttribute(SAVE_KEY, SQLDataType.INTEGER, true);
		TableAttribute characterId = new TableAttribute(CHARACTER_ID, SQLDataType.INTEGER);
		TableAttribute baseValue = new TableAttribute(BASE_VALUE, SQLDataType.INTEGER);
		TableAttribute abilityKey = new TableAttribute(ABILITY_KEY, SQLDataType.INTEGER);
		TableAttribute magicMod = new TableAttribute(MAGIC_MOD, SQLDataType.INTEGER);
		TableAttribute miscMod = new TableAttribute(MISC_MOD, SQLDataType.INTEGER);
		TableAttribute tempMod = new TableAttribute(TEMP_MOD, SQLDataType.INTEGER);
		TableAttribute[] attributes = {saveKey, characterId, baseValue, abilityKey,
				magicMod, miscMod, tempMod};
		m_tableInfo = new TableInfo(TABLE, attributes);
	}

	@Override
	protected Save buildFromHashTable(Hashtable<String, Object> hashTable) {
		SaveType saveKey = SaveType.forKey(((Long) hashTable.get(SAVE_KEY)).intValue());
		int characterId = ((Long) hashTable.get(CHARACTER_ID)).intValue();
		int baseValue = ((Long) hashTable.get(BASE_VALUE)).intValue();
		AbilityType abilityKey = AbilityType.forKey(((Long) hashTable.get(ABILITY_KEY)).intValue());
		int magicMod = ((Long) hashTable.get(MAGIC_MOD)).intValue();
		int miscMod = ((Long) hashTable.get(MISC_MOD)).intValue();
		int tempMod = ((Long) hashTable.get(TEMP_MOD)).intValue();
		
		return new Save(saveKey, characterId, baseValue, abilityKey, magicMod,
				miscMod, tempMod);
	}

	@Override
	protected ContentValues getContentValues(Save object) {
		ContentValues values = new ContentValues();
		values.put(SAVE_KEY, object.getType().getKey());
		values.put(CHARACTER_ID, object.getCharacterID());
		values.put(BASE_VALUE, object.getBaseSave());
		values.put(ABILITY_KEY, object.getAbilityType().getKey());
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
	public Save query(long ... ids) {
		return super.query(ids);
	}
	
	/**
	 * Returns all saves for the character with characterId
	 * @param characterId ID of the character
	 * @return Array of Save, ordered by saveKey
	 */
	public List<Save> queryAllForCharacter(long characterId) {
		String selector = CHARACTER_ID + "=" + characterId; 
		String orderBy = SAVE_KEY + " ASC";
		String table = m_tableInfo.getTable();
		String[] columns = m_tableInfo.getColumns();
		Cursor cursor = getDatabase().query(true, table, columns, selector, 
				null, null, null, orderBy, null);
		
		List<Save> saves = Lists.newArrayListWithCapacity(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Hashtable<String, Object> hashTable =  getTableOfValues(cursor);
			saves.add(buildFromHashTable(hashTable));
			cursor.moveToNext();
		}
		return saves;
	}

    public SaveSet querySet(long characterId) {
        return new SaveSet(queryAllForCharacter(characterId),
                new ValidatedTypedSet.CorrectionListener<Save>() {
                    @Override
                    public void onInvalidItemRemoved(Save removedSave) {
                        SaveRepository.this.delete(removedSave);
                    }

                    @Override
                    public void onMissingItemAdded(Save addedSave) {
                        SaveRepository.this.insert(addedSave);
                    }
                }
        );
    }
	
	@Override
	protected String getSelector(Save object) {
		return getSelector(object.getId(), object.getCharacterID());
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
