package com.lateensoft.pathfinder.toolkit.db.repository;

import java.util.Hashtable;

import android.content.ContentValues;
import android.database.Cursor;

import com.lateensoft.pathfinder.toolkit.db.repository.PTTableAttribute.SQLDataType;
import com.lateensoft.pathfinder.toolkit.model.character.PTSpell;

public class PTSpellRepository extends PTBaseRepository<PTSpell> {
	public static final String TABLE = "Spell";
	public static final String ID = "spell_id";
	private final String NAME = "Name";
	private final String PREPARED = "Prepared";
	private final String LEVEL = "Level";
	private final String DESC = "Description";
	
	public PTSpellRepository() {
		super();
		PTTableAttribute id = new PTTableAttribute(ID, SQLDataType.INTEGER, true);
		PTTableAttribute characterId = new PTTableAttribute(CHARACTER_ID, SQLDataType.INTEGER);
		PTTableAttribute name = new PTTableAttribute(NAME, SQLDataType.TEXT);
		PTTableAttribute prepared = new PTTableAttribute(PREPARED, SQLDataType.INTEGER);
		PTTableAttribute level = new PTTableAttribute(LEVEL, SQLDataType.INTEGER);
		PTTableAttribute desc = new PTTableAttribute(DESC, SQLDataType.TEXT);
		PTTableAttribute[] attributes = {id, characterId, name, prepared, level, desc};
		m_tableInfo = new PTTableInfo(TABLE, attributes);
	}

	@Override
	protected PTSpell buildFromHashTable(Hashtable<String, Object> hashTable) {
		int id = ((Long) hashTable.get(ID)).intValue();
		int characterId = ((Long) hashTable.get(CHARACTER_ID)).intValue();
		String name = (String) hashTable.get(NAME);
		int prepared = ((Long) hashTable.get(PREPARED)).intValue();
		int level = ((Long) hashTable.get(LEVEL)).intValue();
		String desc = (String) hashTable.get(DESC);
		
		PTSpell spell = new PTSpell(id, characterId, name, level, prepared, desc);
		return spell;
	}

	@Override
	protected ContentValues getContentValues(PTSpell object) {
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
	 * @return Array of PTSpell, ordered by level, then alphabetically by name
	 */
	public PTSpell[] querySet(long characterId) {
		String selector = CHARACTER_ID + "=" + characterId; 
		String orderBy = LEVEL+" ASC, "+NAME +" ASC";
		String table = m_tableInfo.getTable();
		String[] columns = m_tableInfo.getColumns();
		Cursor cursor = getDatabase().query(true, table, columns, selector, 
				null, null, null, orderBy, null);
		
		PTSpell[] spells = new PTSpell[cursor.getCount()];
		cursor.moveToFirst();
		int i = 0;
		while (!cursor.isAfterLast()) {
			Hashtable<String, Object> hashTable =  getTableOfValues(cursor);
			spells[i] = buildFromHashTable(hashTable);
			cursor.moveToNext();
			i++;
		}
		return spells;
	}

}
