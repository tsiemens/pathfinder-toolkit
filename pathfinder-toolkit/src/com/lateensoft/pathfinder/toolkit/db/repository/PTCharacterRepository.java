package com.lateensoft.pathfinder.toolkit.db.repository;

import java.util.Hashtable;

import android.content.ContentValues;

import com.lateensoft.pathfinder.toolkit.PTBaseApplication;
import com.lateensoft.pathfinder.toolkit.db.repository.PTTableAttribute.SQLDataType;
import com.lateensoft.pathfinder.toolkit.model.character.PTCharacter;

public class PTCharacterRepository extends PTBaseRepository<PTCharacter> {
	private static final String TABLE = "Character";
	private static final String ID = "character_id";
	private static final String TAG = "Tag";
	
	public PTCharacterRepository() {
		super();
		PTTableAttribute id = new PTTableAttribute(ID, SQLDataType.INTEGER, true);
		PTTableAttribute tag = new PTTableAttribute(TAG, SQLDataType.TEXT);
		PTTableAttribute[] columns = {id, tag};
		m_tableInfo = new PTTableInfo(TABLE, columns);
	}
	
	
	/**
	 * Inserts the character, and all subcomponents into database
	 * 
	 * @return the id of the character inserted, or -1 if failure occurred.
	 */
	@Override
	public long insert(PTCharacter object) {
		// TODO Auto-generated method stub
		return super.insert(object);
	}

	@Override
	protected PTCharacter buildFromHashTable(Hashtable<String, Object> hashTable) {
		long id = (Long) hashTable.get(ID);
		String tag = (String) hashTable.get(TAG);
		PTCharacter character = new PTCharacter(id, tag, PTBaseApplication.getAppContext());
		return character;
	}

	@Override
	protected ContentValues getContentValues(PTCharacter object) {
		ContentValues values = new ContentValues();
		if (isIDSet(object)) { 
			values.put(ID, object.getID());
		}
		values.put(TAG, object.getTag());
		return values;
	}

}
