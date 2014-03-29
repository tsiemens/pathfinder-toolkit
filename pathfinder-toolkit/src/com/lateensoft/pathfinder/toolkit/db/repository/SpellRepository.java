package com.lateensoft.pathfinder.toolkit.db.repository;

import java.util.Hashtable;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.db.repository.TableAttribute.SQLDataType;
import com.lateensoft.pathfinder.toolkit.model.character.Spell;

public class SpellRepository extends BaseRepository<Spell> {
	public static final String TABLE = "Spell";
	public static final String ID = "spell_id";
	private final String NAME = "Name";
	private final String PREPARED = "Prepared";
	private final String LEVEL = "Level";
	private final String DESC = "Description";
	
	public SpellRepository() {
		super();
		TableAttribute id = new TableAttribute(ID, SQLDataType.INTEGER, true);
		TableAttribute characterId = new TableAttribute(CHARACTER_ID, SQLDataType.INTEGER);
		TableAttribute name = new TableAttribute(NAME, SQLDataType.TEXT);
		TableAttribute prepared = new TableAttribute(PREPARED, SQLDataType.INTEGER);
		TableAttribute level = new TableAttribute(LEVEL, SQLDataType.INTEGER);
		TableAttribute desc = new TableAttribute(DESC, SQLDataType.TEXT);
		TableAttribute[] attributes = {id, characterId, name, prepared, level, desc};
		m_tableInfo = new TableInfo(TABLE, attributes);
	}

	@Override
	protected Spell buildFromHashTable(Hashtable<String, Object> hashTable) {
		int id = ((Long) hashTable.get(ID)).intValue();
		int characterId = ((Long) hashTable.get(CHARACTER_ID)).intValue();
		String name = (String) hashTable.get(NAME);
		int prepared = ((Long) hashTable.get(PREPARED)).intValue();
		int level = ((Long) hashTable.get(LEVEL)).intValue();
		String desc = (String) hashTable.get(DESC);
		
		return new Spell(id, characterId, name, level, prepared, desc);
	}

	@Override
	protected ContentValues getContentValues(Spell object) {
		ContentValues values = new ContentValues();
		if(isIDSet(object)) { 
			values.put(ID, object.getID());
		}
		values.put(CHARACTER_ID, object.getCharacterID());
		values.put(NAME, object.getName());
		values.put(PREPARED, object.getPrepared());
		values.put(LEVEL, object.getLevel());
		values.put(DESC, object.getDescription());
		return values;
	}
	
	/**
	 * Returns all spells for the character with characterId
	 * @param characterId
	 * @return Array of Spell, ordered by level, then alphabetically by name
	 */
	public List<Spell> querySet(long characterId) {
		String selector = CHARACTER_ID + "=" + characterId; 
		String orderBy = LEVEL+" ASC, "+NAME +" ASC";
		String table = m_tableInfo.getTable();
		String[] columns = m_tableInfo.getColumns();
		Cursor cursor = getDatabase().query(true, table, columns, selector, 
				null, null, null, orderBy, null);
		
		List<Spell> spells = Lists.newArrayListWithCapacity(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Hashtable<String, Object> hashTable =  getTableOfValues(cursor);
			spells.add(buildFromHashTable(hashTable));
			cursor.moveToNext();
		}
		return spells;
	}

}
