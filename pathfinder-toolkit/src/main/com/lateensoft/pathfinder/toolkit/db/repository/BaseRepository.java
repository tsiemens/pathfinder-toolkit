package com.lateensoft.pathfinder.toolkit.db.repository;

import java.util.Hashtable;

import android.content.ContentValues;
import android.database.Cursor;

import com.lateensoft.pathfinder.toolkit.db.DatabaseImpl;
import com.lateensoft.pathfinder.toolkit.dao.Identifiable;

public abstract class BaseRepository<T extends Identifiable> {
    private DatabaseImpl m_database;
    protected TableInfo m_tableInfo;
    
    static final String CHARACTER_ID = "character_id";
    
    /**
     * Must setup mTableAttributeSet here
     */
    public BaseRepository() {
        m_database = DatabaseImpl.getInstance();
    }
    
    /**
     * Inserts the object into the database
     * Sets the ID of the object if it is successfully added.
     * @param object
     * @return the ID of the object, or -1 if error occured
     */
    public long insert(T object) {
        ContentValues values = getContentValues(object);
        String table = m_tableInfo.getTable();
        long id = m_database.insert(table, values);
        if (id != -1 && !isIDSet(object)) {
            object.setId(id);
        }
        return id;
    }

    public T query(long ... ids) {
        String selector = getSelector(ids);
        String table = m_tableInfo.getTable();
        String[] columns = m_tableInfo.getColumns();
        Cursor cursor = m_database.query(table, columns, selector);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            Hashtable<String, Object> hashTable =  getTableOfValues(cursor);
            return buildFromHashTable(hashTable);
        } else {
            return null;
        }
    }
    
    protected abstract T buildFromHashTable(Hashtable<String, Object> hashTable);

    public int update(T object) {
        String selector = getSelector(object);
        ContentValues values = getContentValues(object);
        String table = m_tableInfo.getTable();
        return m_database.update(table, values, selector);
    }
    
    /**
     * Gets selector for Identifiable
     * Unoverriden methods will only use getId(), as the single primary key
     * defined by the table attributes
     * @param object
     * @return an SQL where clause
     */
    protected String getSelector(T object) {
        String idColumn = m_tableInfo.getPrimaryKeyColumn();
        return idColumn + "=" + object.getId();
    }
    
    /**
     * Gets selector for Identifiable with ids for primary keys.
     * Unoverriden methods will only use the first argument, as the single primary key 
     * defined by the table attributes
     * @param ids
     * @return an SQL where clause
     */
    protected String getSelector(long ... ids) {
        String idColumn = m_tableInfo.getPrimaryKeyColumn();
        return idColumn + "=" + ids[0];
    }
    
    public int delete(long ... ids) {
        String selector = getSelector(ids);
        String table = m_tableInfo.getTable();
        return m_database.delete(table, selector);
    }
    
    public int delete(T object) {
        String selector = getSelector(object);
        String table = m_tableInfo.getTable();
        return m_database.delete(table, selector);
    }
    
    protected static Hashtable<String, Object> getTableOfValues(Cursor cursor) {
        Hashtable<String, Object> table = new Hashtable<String, Object>();
        String[] columns = cursor.getColumnNames();
        for(int i = 0; i < columns.length; i++) {
            Object datum = getDatum(cursor, columns[i]);
            if (datum != null) {
                table.put(columns[i], datum);
            }
        }
        return table;
    }
    
    protected static Object getDatum(Cursor cursor, String column) {
        int index = cursor.getColumnIndex(column);
        int type = cursor.getType(index);
        Object data;
        switch (type) {
        case Cursor.FIELD_TYPE_STRING:
            data = cursor.getString(index);
            break;
        case Cursor.FIELD_TYPE_INTEGER:
            // Because INTEGER can store Long and Integer, we need to use Long, and cast later
            data = cursor.getLong(index);
            break;
        case Cursor.FIELD_TYPE_FLOAT:
            data = cursor.getDouble(index);
            break;
        default:
            data = null;
            break;
        }
        return data;
    }
    
    /**
     * @param object
     * @return true if the object has a set ID (one which is not 0), false otherwise
     */
    protected boolean isIDSet(Identifiable object) {
        return object.getId() != Identifiable.UNSET_ID;
    }
    
    /**
     * Needs to package up the object so it can be stored in the database
     */
    protected abstract ContentValues getContentValues(T object);
    
    protected DatabaseImpl getDatabase() {
        return m_database;
    }
}
