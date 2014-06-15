package com.lateensoft.pathfinder.toolkit.db.repository;

import java.util.Hashtable;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.db.repository.TableAttribute.SQLDataType;
import com.lateensoft.pathfinder.toolkit.model.character.Feat;

public class FeatRepository extends BaseRepository<Feat> {
	public static final String TABLE = "Feat";
	public static final String ID = "feat_id";
	private final String NAME = "Name";
	private final String DESC = "Description";
	
	public FeatRepository() {
		super();
		TableAttribute id = new TableAttribute(ID, SQLDataType.INTEGER, true);
		TableAttribute characterId = new TableAttribute(CHARACTER_ID, SQLDataType.INTEGER);
		TableAttribute name = new TableAttribute(NAME, SQLDataType.TEXT);
		TableAttribute desc = new TableAttribute(DESC, SQLDataType.TEXT);
		TableAttribute[] attributes = {id, characterId, name, desc};
		m_tableInfo = new TableInfo(TABLE, attributes);
	}

	@Override
	protected Feat buildFromHashTable(Hashtable<String, Object> hashTable) {
		int id = ((Long) hashTable.get(ID)).intValue();
		int characterId = ((Long) hashTable.get(CHARACTER_ID)).intValue();
		String name = (String) hashTable.get(NAME);
		String desc = (String) hashTable.get(DESC);
		
		Feat feat = new Feat(id, characterId, name, desc);
		return feat;
	}

	@Override
	protected ContentValues getContentValues(Feat object) {
		ContentValues values = new ContentValues();
		if(isIDSet(object)) { 
			values.put(ID, object.getId());
		}
		values.put(CHARACTER_ID, object.getCharacterID());
		values.put(NAME, object.getName());
		values.put(DESC, object.getDescription());
		return values;
	}
	
	/**
	 * Returns all feats for the character with characterId
	 * @param characterId
	 * @return Array of Feat, ordered alphabetically by name
	 */
	public List<Feat> querySet(long characterId) {
		String selector = CHARACTER_ID + "=" + characterId; 
		String orderBy = NAME + " ASC";
		String table = m_tableInfo.getTable();
		String[] columns = m_tableInfo.getColumns();
		Cursor cursor = getDatabase().query(true, table, columns, selector, 
				null, null, null, orderBy, null);
		
		List<Feat> feats = Lists.newArrayListWithCapacity(cursor.getCount());
        cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Hashtable<String, Object> hashTable =  getTableOfValues(cursor);
			feats.add(buildFromHashTable(hashTable));
			cursor.moveToNext();
		}
		return feats;
	}

}
