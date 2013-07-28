package com.lateensoft.pathfinder.toolkit.datahelpers;

import com.lateensoft.pathfinder.toolkit.PTBaseApplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PTDatabase extends SQLiteOpenHelper {
	
	private final String TAG = PTDatabase.class.getSimpleName();
	static final String DB_NAME = "pathfinderToolkitDB";
	static int dbVersion = 1;
	private SQLiteDatabase mDatabase;
	
	private static PTDatabase s_sharedInstance;
	
	public static PTDatabase getSharedInstance() {
		if (s_sharedInstance == null) {
			s_sharedInstance = new PTDatabase();
		}
		return s_sharedInstance;
	}
	
	protected PTDatabase() {
		super(PTBaseApplication.getAppContext(), DB_NAME, null, dbVersion);
		//mDatabase = getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		PTTableCreator tableCreator = new PTTableCreator();
		tableCreator.createTables(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}
	
	public void open() throws SQLException {
		if (!mDatabase.isOpen()) {
			mDatabase = getWritableDatabase();
		}
	}
	
	public void close() {
		mDatabase.close();
	}
	
	public Cursor query(Boolean distinct, String table, String[] columns, String selection, 
			String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
		return mDatabase.query(distinct, table, columns, selection, selectionArgs, 
				groupBy, having, orderBy, limit);
	}
	
	public Cursor query(String table, String[] columns, String selection) {
		init();
		Cursor result = mDatabase.query(true, table, columns, selection, null, 
				null, null, null, null);
		finish();
		return result;
	}

	private void finish() {
		mDatabase.endTransaction();
		close();
	}

	private void init() {
		open();
		mDatabase.beginTransaction();
	}
	
	public int update(String table, ContentValues values, String whereClause) {
		init();
		int result = mDatabase.update(table, values, whereClause, null);
		finish();
		return result;
	}
	
	public long insert(String table, ContentValues values) {
		init();
		long result = mDatabase.insert(table, null, values);
		finish();
		return result;
	}
}
