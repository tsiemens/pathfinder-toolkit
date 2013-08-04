package com.lateensoft.pathfinder.toolkit.repository;

import java.util.Hashtable;

import android.content.ContentValues;
import android.database.Cursor;

import com.lateensoft.pathfinder.toolkit.datahelpers.PTDatabase;
import com.lateensoft.pathfinder.toolkit.repository.PTTableAttribute.SQLDataType;

public abstract class PTBaseRepository<T> {
	private PTDatabase mDatabase;
	protected PTTableInfo mTableInfo;
	
	static final String CHARACTER_ID = "character_id";
	
	/**
	 * Must setup mTableAttributeSet here
	 */
	public PTBaseRepository() {
		mDatabase = PTDatabase.getSharedInstance();
	}
	
	public long insert(T object) {
		ContentValues values = getContentValues(object);
		String table = mTableInfo.getTable();
		return mDatabase.insert(table, values);
	}

	public T query(int id) {
		String selector = getSelector(id);
		String table = mTableInfo.getTable();
		String[] columns = mTableInfo.getColumns();
		Cursor cursor = mDatabase.query(table, columns, selector);
		cursor.moveToFirst();
		Hashtable<String, Object> hashTable =  getTableOfValues(cursor);
		return buildFromHashTable(hashTable);
	}
	
	protected abstract T buildFromHashTable(Hashtable<String, Object> hashTable);

	public int update(T object) {
		String selector = getSelector(((PTStorable)object).getID());
		ContentValues values = getContentValues(object);
		String table = mTableInfo.getTable();
		return mDatabase.update(table, values, selector);
	}
	
	protected String getSelector(int id) {
		String idColumn = mTableInfo.getPrimaryKeyColumn();
		return idColumn + "=" + id;
	}
	
	public int delete(int id) {
		T data = query(id);
		String selector = getSelector(id);
		String table = mTableInfo.getTable();
		return mDatabase.delete(table, selector);
	}
	
	public int delete(T object) {
		String selector = getSelector(((PTStorable)object).getID());
		String table = mTableInfo.getTable();
		return mDatabase.delete(table, selector);
	}
	
	protected Hashtable<String, Object> getTableOfValues(Cursor cursor) {
		Hashtable<String, Object> table = new Hashtable<String, Object>();
		String[] columns = mTableInfo.getColumns();
		for(int i = 0; i < columns.length; i++) {
			Object datum = getDatum(cursor, columns[i]);
			table.put(columns[i], datum);
		}
		return table;
	}
	
	protected Object getDatum(Cursor cursor, String column) {
		int index = cursor.getColumnIndex(column);
		SQLDataType type = mTableInfo.getDataType(column);
		Object data;
		switch (type) {
		case TEXT:
			data = cursor.getString(index);
			break;
		case INTEGER:
			data = cursor.getInt(index);
			break;
		case REAL:
			data = cursor.getFloat(index);
			break;
		default:
			data = null;
			break;
		}
		return data;
	}
	
	/**
	 * Needs to package up the object so it can be stored in the database
	 */
	protected abstract ContentValues getContentValues(T object);
}
