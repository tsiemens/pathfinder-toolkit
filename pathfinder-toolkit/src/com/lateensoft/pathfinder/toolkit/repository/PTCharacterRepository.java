package com.lateensoft.pathfinder.toolkit.repository;

import java.util.Hashtable;

import android.content.ContentValues;

import com.lateensoft.pathfinder.toolkit.PTBaseApplication;
import com.lateensoft.pathfinder.toolkit.character.sheet.PTCharacter;
import com.lateensoft.pathfinder.toolkit.repository.PTTableAttribute.SQLDataType;

public class PTCharacterRepository extends PTBaseRepository<PTCharacter> {
	private static final String TABLE = "Character";
	private static final String ID = "character_id";
	private static final String TAG = "Tag";
	
	public PTCharacterRepository() {
		super();
		PTTableAttribute id = new PTTableAttribute(ID, SQLDataType.INTEGER, true);
		PTTableAttribute tag = new PTTableAttribute(TAG, SQLDataType.TEXT);
		PTTableAttribute[] columns = {id, tag};
		mTableInfo = new PTTableInfo(TABLE, columns);
	}
	
	@Override
	protected PTCharacter buildFromHashTable(Hashtable<String, Object> hashTable) {
		int id = (Integer) hashTable.get(ID);
		String tag = (String) hashTable.get(TAG);
		PTCharacter character = new PTCharacter(id, tag, PTBaseApplication.getAppContext());
		return character;
	}

	@Override
	protected ContentValues getContentValues(PTCharacter object) {
		ContentValues values = new ContentValues();
		values.put(ID, object.getID());
		values.put(TAG, object.getTag());
		return values;
	}

}
