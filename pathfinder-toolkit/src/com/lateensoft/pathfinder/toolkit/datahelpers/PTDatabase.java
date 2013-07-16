package com.lateensoft.pathfinder.toolkit.datahelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PTDatabase extends SQLiteOpenHelper {
	
	private final String TAG = "PTDatabaseManager";
	static final String DB_NAME = "pathfinderToolkitDB";
	static int dbVersion = 1;
	private SQLiteDatabase mDatabase;
	
	protected PTDatabase(Context context) {
		super(context, DB_NAME, null, dbVersion);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		PTTableCreator tableCreator = new PTTableCreator();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
}
