package com.lateensoft.pathfinder.toolkit.repository;

import android.content.ContentValues;
import android.database.Cursor;

import com.lateensoft.pathfinder.toolkit.stats.PTStat;

public class PTStatRepository extends PTBaseRepository<PTStat>{	
	static final String TABLE = "Stat";
	static final String ID = "stat_id";
	static final String NAME = "Name";
	static final String BASE_VALUE = "BaseValue";
	static final String[] COLUMNS = {ID, NAME, BASE_VALUE};
	
	public PTStatRepository() {
		super();
	}

	@Override
	protected PTStat buildTable(Cursor cursor) {
		int idIndex = cursor.getColumnIndex(ID);
		int nameIndex = cursor.getColumnIndex(NAME);
		int baseValueIndex = cursor.getColumnIndex(BASE_VALUE);
		int id = cursor.getInt(idIndex);
		String name = cursor.getString(nameIndex);
		int baseValue = cursor.getInt(baseValueIndex);
		return new PTStat(id, name, baseValue);
	}

	protected ContentValues getContentValues(PTStat stat) {
		ContentValues values = new ContentValues();
		values.put(NAME, stat.getName());
		values.put(BASE_VALUE, stat.getBaseValue());
		return values;
	}
	
	@Override
	protected void baseUpdate(PTStat object) {
		// Do nothing
	}
	
	@Override
	protected void baseDelete(PTStat object) {
		// Do nothing
	}
	
	protected String TABLE() {
		return TABLE;
	}
	
	protected String[] COLUMNS() {
		return COLUMNS;
	}
	
	protected String ID() {
		return ID;
	}	
}
