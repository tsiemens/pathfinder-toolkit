package com.lateensoft.pathfinder.toolkit.repository;

import java.util.Hashtable;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Build;

import com.lateensoft.pathfinder.toolkit.datahelpers.PTDatabase;
import com.lateensoft.pathfinder.toolkit.repository.PTTableAttribute.SQLDataType;

public abstract class PTBaseRepository<T> {
	private PTDatabase mDatabase;
	protected PTTableAttributeSet mTableAttributeSet;
	
	// TODO Restrict T to IStorable?
	public PTBaseRepository() {
		mDatabase = PTDatabase.getSharedInstance();
	}
	
	public long insert(T object) {
		ContentValues values = getContentValues(object);
		String table = mTableAttributeSet.getTable();
		return mDatabase.insert(table, values);
	}

	public T query(int id) {
		String selector = getSelector(id);
		String table = mTableAttributeSet.getTable();
		String[] columns = mTableAttributeSet.getColumns();
		Cursor cursor = mDatabase.query(table, columns, selector);
		cursor.moveToFirst();
		return buildFromCursor(cursor);
	}
	
	/**
	 * This method must create the object that you want, but also must call the 
	 * repository of a direct superclass or any members that have their own tables
	 * @param cursor
	 * @return
	 */
	protected abstract T buildFromCursor(Cursor cursor);

	public int update(T object) {
		String selector = getSelector(((PTStorable)object).id());
		ContentValues values = getContentValues(object);
		String table = mTableAttributeSet.getTable();
		return mDatabase.update(table, values, selector);
	}
	
	protected String getSelector(int id) {
		String idColumn = mTableAttributeSet.getPrimaryKeyColumn();
		return idColumn + "=" + id;
	}
	
	public int delete(int id) {
		T data = query(id);
		String selector = getSelector(id);
		String table = mTableAttributeSet.getTable();
		return mDatabase.delete(table, selector);
	}
	
	public int delete(T object) {
		String selector = getSelector(((PTStorable)object).id());
		String table = mTableAttributeSet.getTable();
		return mDatabase.delete(table, selector);
	}
	
	protected Hashtable<String, PTTableDatum> getTableOfValues(Cursor cursor) {
		Hashtable<String, PTTableDatum> table = new Hashtable<String, PTTableDatum>();
		String[] columns = mTableAttributeSet.getColumns();
		for(int i = 0; i < columns.length; i++) {
			PTTableDatum datum = getDatum(cursor, columns[i]);
			table.put(columns[i], datum);
		}
		return table;
	}
	
	protected PTTableDatum getDatum(Cursor cursor, String column) {
		int index = cursor.getColumnIndex(column);
		SQLDataType type = mTableAttributeSet.getDataType(column);
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
		return new PTTableDatum(data, column, type);
	}
	
	/**
	 * Needs to package up the object so it can be stored in the database
	 * Don't think you need to worry about superclass stuff...
	 */
	protected abstract ContentValues getContentValues(T object);
}
