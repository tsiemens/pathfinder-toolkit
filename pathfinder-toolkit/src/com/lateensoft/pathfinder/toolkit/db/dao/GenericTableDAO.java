package com.lateensoft.pathfinder.toolkit.db.dao;

import android.database.Cursor;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.dao.sql.GenericSQLiteDAO;
import com.lateensoft.pathfinder.toolkit.db.Database;
import org.jetbrains.annotations.Nullable;

import java.util.Hashtable;
import java.util.List;

public abstract class GenericTableDAO<ID, T> implements GenericSQLiteDAO<ID, T> {
    private Database database;
    private Table table;

    public GenericTableDAO() {
        database = Database.getInstance();
        table = initTable();
    }

    protected abstract Table initTable();

    @Override
    public T find(ID id) {
        String selector = andSelectors(getIdSelector(id), getBaseSelector());
        String tables = getFromQueryClause();
        String[] columns = getColumnsForQuery();
        Cursor cursor = database.query(tables, columns, selector);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            Hashtable<String, Object> hashTable =  getTableOfValues(cursor);
            return buildFromHashTable(hashTable);
        } else {
            return null;
        }
    }

    protected List<String> getTablesForQuery() {
        return Lists.newArrayList(getTable().getName());
    }

    private String getFromQueryClause() {
        return Joiner.on(", ").join(getTablesForQuery());
    }

    protected String[] getColumnsForQuery() {
        return getTable().getColumnNames();
    }

    @Override
    public boolean exists(ID id) {
        Cursor cursor= getDatabase().rawQuery("select count(*) count from " + getTable().getName() + " where " +
                getIdSelector(id), null);
        cursor.moveToFirst();
        int count = cursor.getInt(cursor.getColumnIndex("count"));
        cursor.close();
        return count != 0;
    }

    @Override
    public List<T> findAll() {
        return findFiltered(getBaseSelector(), getDefaultOrderBy());
    }

    @Override
    public List<T> findFiltered(String selector, String orderBy) {
        String table = getFromQueryClause();
        String[] columns = getColumnsForQuery();
        Cursor cursor = database.query(true, table, columns, selector,
                null, null, null, orderBy, null);

        List<T> entities = Lists.newArrayListWithCapacity(cursor.getCount());
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Hashtable<String, Object> hashTable = getTableOfValues(cursor);
            entities.add(buildFromHashTable(hashTable));
            cursor.moveToNext();
        }
        return entities;
    }

    @Override
    public void removeById(ID id) throws DataAccessException {
        String selector = getIdSelector(id);
        String table = getTable().getName();
        int returnVal = database.delete(table, selector);
        if (returnVal <= 0) {
            throw new DataAccessException("Database.delete for " + id + "returned " + returnVal);
        }
    }

    protected Database getDatabase() {
        return database;
    }

    protected Table getTable() {
        return table;
    }

    protected abstract String getIdSelector(ID id);

    protected @Nullable String getBaseSelector() {
        return null;
    }

    protected String getDefaultOrderBy() {
        return null;
    }

    protected abstract T buildFromHashTable(Hashtable<String, Object> hashTable);

    protected static Hashtable<String, Object> getTableOfValues(Cursor cursor) {
        Hashtable<String, Object> table = new Hashtable<String, Object>();
        String[] columns = cursor.getColumnNames();
        for (String column : columns) {
            Object datum = getDatum(cursor, column);
            if (datum != null) {
                table.put(column, datum);
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

    public static String andSelectors(String... selectors) {
        String selector = Joiner.on(" AND ").skipNulls().join(selectors);
        if (!selector.isEmpty()) {
            return selector;
        } else {
            return null;
        }
    }
}
