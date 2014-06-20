package com.lateensoft.pathfinder.toolkit.db;

import com.lateensoft.pathfinder.toolkit.BaseApplication;
import com.lateensoft.pathfinder.toolkit.db.repository.TableAttribute;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {
	
	@SuppressWarnings("unused")
	private final String TAG = Database.class.getSimpleName();
	
	public static final String DB_NAME = "pathfinder_toolkit.db";
	
	public static final int DB_VERSION = 1;
	private SQLiteDatabase m_database;
	
	private static Database s_instance;
	
	public static Database getInstance() {
		if (s_instance == null) {
			s_instance = new Database();
		}
		return s_instance;
	}
	
	protected Database() {
		super(BaseApplication.getAppContext(), DB_NAME, null, DB_VERSION);
		open();
	}
	
	public void forceCreate() {
		TableCreator tableCreator = new TableCreator();
		tableCreator.createTables(m_database);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		TableCreator tableCreator = new TableCreator();
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

    public Cursor rawQuery(String query, String[] selectionArgs) {
        open();
        return m_database.rawQuery(query, selectionArgs);
    }

	/**
	 * Updates table
	 * @param table
	 * @param values
	 * @param whereClause
	 * @return number of rows affected, 0 otherwise
	 */
	public int update(String table, ContentValues values, String whereClause) {
		open();
		return m_database.update(table, values, whereClause, null);
	}
	
	public long insert(String table, ContentValues values) {
		open();
		return m_database.insert(table, null, values);
	}
	
	/**
	 * Performs delete on table
	 * @param table
	 * @param selector
	 * @return the number of rows deleted, 0 otherwise
	 */
	public int delete(String table, String selector) {
		open();
		return m_database.delete(table, selector, null);
	}
}
