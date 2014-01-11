package com.lateensoft.pathfinder.toolkit.db;

import com.lateensoft.pathfinder.toolkit.PTBaseApplication;
import com.lateensoft.pathfinder.toolkit.db.repository.PTTableAttribute;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PTDatabase extends SQLiteOpenHelper {
	
	@SuppressWarnings("unused")
	private final String TAG = PTDatabase.class.getSimpleName();
	
	public static final String DB_NAME = "pathfinder_toolkit.db";
	
	public static int dbVersion = 1;
	private SQLiteDatabase m_database;
	
	private static PTDatabase s_instance;
	
	public static PTDatabase getInstance() {
		if (s_instance == null) {
			s_instance = new PTDatabase();
		}
		return s_instance;
	}
	
	protected PTDatabase() {
		super(PTBaseApplication.getAppContext(), DB_NAME, null, dbVersion);
		open();
	}
	
	public void forceCreate() {
		PTTableCreator tableCreator = new PTTableCreator();
		tableCreator.createTables(m_database);
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
		if (m_database == null || !m_database.isOpen()) {
			m_database = getWritableDatabase();
			if (!m_database.isReadOnly()) {
		        // Enable foreign key constraints, as they are disabled by default in sqlite 3.7
		        m_database.execSQL("PRAGMA foreign_keys=ON;");
		    }
		}
	}
	
	public void close() {
		m_database.close();
	}
	
	public Cursor query(Boolean distinct, String table, String[] columns, String selection, 
			String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
		return m_database.query(distinct, table, columns, selection, selectionArgs, 
				groupBy, having, orderBy, limit);
	}
	
	public Cursor query(String table, String[] columns, String selection) {
		open();
		Cursor result = m_database.query(true, table, columns, selection, null, 
				null, null, null, null);
		return result;
	}

	public int update(String table, ContentValues values, String whereClause) {
		open();
		int result = m_database.update(table, values, whereClause, null);
		return result;
	}
	
	public long insert(String table, ContentValues values) {
		open();
		long result = m_database.insert(table, null, values);
		return result;
	}
	
	public int delete(String table, String selector) {
		open();
		int result = m_database.delete(table, selector, null);
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
