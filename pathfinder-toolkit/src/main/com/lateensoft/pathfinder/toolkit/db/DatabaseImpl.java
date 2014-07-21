package com.lateensoft.pathfinder.toolkit.db;

import android.content.Context;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseImpl extends SQLiteOpenHelper implements Database {
    
    @SuppressWarnings("unused")
    private final String TAG = DatabaseImpl.class.getSimpleName();
    
    public static final String DB_NAME = "pathfinder_toolkit.db";
    
    public static final int DB_VERSION = 1;
    private SQLiteDatabase database;
    
    public DatabaseImpl(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        open();
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
        if (database == null || !database.isOpen()) {
            database = getWritableDatabase();
            if (!database.isReadOnly()) {
                // Enable foreign key constraints, as they are disabled by default in sqlite 3.7
                database.execSQL("PRAGMA foreign_keys=ON;");
            }
        }
    }
    
    public void close() {
        database.close();
    }

    @Override
    public void beginTransaction() {
        database.beginTransaction();
    }

    @Override
    public void setTransactionSuccessful() {
        database.setTransactionSuccessful();
    }

    @Override
    public void endTransaction() {
        database.endTransaction();
    }

    @Override
    public Cursor query(Boolean distinct, String table, String[] columns, String selection, 
            String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        return database.query(distinct, table, columns, selection, selectionArgs,
                groupBy, having, orderBy, limit);
    }

    @Override
    public Cursor query(String table, String[] columns, String selection) {
        open();
        Cursor result = database.query(true, table, columns, selection, null,
                null, null, null, null);
        return result;
    }

    @Override
    public Cursor rawQuery(String query, String[] selectionArgs) {
        open();
        return database.rawQuery(query, selectionArgs);
    }

    @Override
    public int update(String table, ContentValues values, String whereClause) {
        open();
        return database.update(table, values, whereClause, null);
    }

    @Override
    public long insert(String table, ContentValues values) {
        open();
        return database.insert(table, null, values);
    }

    @Override
    public int delete(String table, String selector) {
        open();
        return database.delete(table, selector, null);
    }
}
