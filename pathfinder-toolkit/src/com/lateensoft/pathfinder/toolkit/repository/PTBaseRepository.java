package com.lateensoft.pathfinder.toolkit.repository;

import android.content.ContentValues;
import android.database.Cursor;

import com.lateensoft.pathfinder.toolkit.datahelpers.PTDatabase;

public abstract class PTBaseRepository<T> {
	private PTDatabase mDatabase;
	
	// TODO Restrict T to IStorable?
	public PTBaseRepository() {
		mDatabase = PTDatabase.getSharedInstance();
	}
	
	public long insert(T object) {
		ContentValues values = getContentValues(object);
		return mDatabase.insert(TABLE(), values);
	}

	public T query(int id) {
		String selector = getSelector(id);
		Cursor cursor = mDatabase.query(TABLE(), COLUMNS(), selector);
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
		baseUpdate(object);
		String selector = getSelector(((IStorable)object).id());
		ContentValues values = getContentValues(object);
		return mDatabase.update(TABLE(), values, selector);
	}
	
	protected String getSelector(int id) {
		return ID() + "=" + id;
	}
	
	/**
	 * Needs to package up the object so it can be stored in the database
	 * Don't think you need to worry about superclass stuff...
	 */
	protected abstract ContentValues getContentValues(T object);
	
	/**
	 * This method is used to update the superclass of an object
	 * @param object
	 */
	protected abstract void baseUpdate(T object);
	
	protected abstract String TABLE();
	protected abstract String[] COLUMNS();
	protected abstract String ID();
}
