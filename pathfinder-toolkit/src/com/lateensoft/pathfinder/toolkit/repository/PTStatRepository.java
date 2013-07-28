package com.lateensoft.pathfinder.toolkit.repository;

import android.content.ContentValues;
import android.database.Cursor;

import com.lateensoft.pathfinder.toolkit.datahelpers.PTDatabase;
import com.lateensoft.pathfinder.toolkit.stats.PTStat;

public class PTStatRepository {
	private PTDatabase mDatabase;
	
	static final String TABLE = "stat";
	static final String ID = "stat_id";
	static final String NAME = "Name";
	static final String BASE_VALUE = "BaseValue";
	static final String[] COLUMNS = {ID, NAME, BASE_VALUE};
	
	public PTStatRepository() {
		mDatabase = PTDatabase.getSharedInstance();
	}
	
	public long insertStat(PTStat stat) {
		String selector = getSelector(stat.id());
		ContentValues values = statAsContentValues(stat);
		return mDatabase.insert(TABLE, values);
	}
	
	public PTStat getStat(int id) {
		String selector = getSelector(id);
		Cursor cursor = mDatabase.query(TABLE, COLUMNS, selector);
		cursor.moveToFirst();
		int nameIndex = cursor.getColumnIndex(NAME);
		int baseValueIndex = cursor.getColumnIndex(BASE_VALUE);
		String name = cursor.getString(nameIndex);
		int baseValue = cursor.getInt(baseValueIndex);
		return new PTStat(id, name, baseValue);
	}
	
	public int updateStat(PTStat stat) {
		String selector = getSelector(stat.id());
		ContentValues values = statAsContentValues(stat);
		return mDatabase.update(TABLE, values, selector);
	}
	
	private ContentValues statAsContentValues(PTStat stat) {
		ContentValues values = new ContentValues();
		values.put(NAME, stat.getName());
		values.put(BASE_VALUE, stat.getBaseValue());
		return values;
	}
	
	private String getSelector(int id) {
		return ID + "=" + id;
	}
}
