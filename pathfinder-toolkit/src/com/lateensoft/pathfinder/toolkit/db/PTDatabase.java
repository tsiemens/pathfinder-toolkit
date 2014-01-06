package com.lateensoft.pathfinder.toolkit.db;

import com.lateensoft.pathfinder.toolkit.PTBaseApplication;
import com.lateensoft.pathfinder.toolkit.db.repository.PTTableAttribute;

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
		mDatabase = getWritableDatabase();
	}
	
	public void forceCreate() {
		PTTableCreator tableCreator = new PTTableCreator();
		tableCreator.createTables(mDatabase);
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
		open();
		Cursor result = mDatabase.query(true, table, columns, selection, null, 
				null, null, null, null);
		return result;
	}

	public int update(String table, ContentValues values, String whereClause) {
		open();
		int result = mDatabase.update(table, values, whereClause, null);
		return result;
	}
	
	public long insert(String table, ContentValues values) {
		open();
		long result = mDatabase.insert(table, null, values);
		return result;
	}
	
	public int delete(String table, String selector) {
		open();
		int result = mDatabase.delete(table, selector, null);
		return result;
	}
	
	public void create(String name, PTTableAttribute[] attributes) {
		StringBuilder sb = new StringBuilder();
		String create = String.format("CREATE TABLE %s (", name);
		sb.append(create);
		String columnTemplate = "%s %s";
		String primaryKeyAddition = "PRIMARY KEY AUTOINCREMENT";
		String comma = ", ";
		for(int i = 0; i < attributes.length; i++) {
			String columnName = attributes[i].GetColumn();
			String columnType = attributes[i].GetType().name();
			String columnDefine = String.format(columnTemplate, columnName, columnType);
			sb.append(columnDefine);
			if (attributes[i].isPrimaryKey) {
				sb.append(primaryKeyAddition);
			}
			if ((i + 1) < attributes.length) {
				sb.append(comma);
			}
			//TODO foreign keys
		}
	}
}
