package com.lateensoft.pathfinder.toolkit.datahelpers;

import com.lateensoft.pathfinder.toolkit.PTBaseApplication;

import android.content.Context;
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
}
