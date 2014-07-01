package com.lateensoft.pathfinder.toolkit.db;

import android.content.ContentValues;
import android.database.Cursor;

public interface Database {

    public void beginTransaction();
    public void setTransactionSuccessful();
    public void endTransaction();

    public Cursor query(Boolean distinct, String table, String[] columns, String selection,
            String[] selectionArgs, String groupBy, String having, String orderBy, String limit);
    
    public Cursor query(String table, String[] columns, String selection);

    public Cursor rawQuery(String query, String[] selectionArgs);

    /** Updates table and returns number of rows affected, 0 otherwise */
    public int update(String table, ContentValues values, String whereClause);
    
    public long insert(String table, ContentValues values);

    /**Performs delete on table and returns the number of rows deleted, 0 otherwise */
    public int delete(String table, String selector);
}
