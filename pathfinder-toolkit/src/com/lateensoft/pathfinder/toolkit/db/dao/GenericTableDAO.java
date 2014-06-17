package com.lateensoft.pathfinder.toolkit.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.dao.GenericDAO;
import com.lateensoft.pathfinder.toolkit.db.Database;
import org.jetbrains.annotations.Nullable;

import java.util.Hashtable;
import java.util.List;

public abstract class GenericTableDAO<RowId, Entity, RowData> implements GenericDAO<Entity> {
    private Database database;
    private Table table;

    public GenericTableDAO() {
        database = Database.getInstance();
        table = initTable();
    }

    protected abstract Table initTable();

    public Entity find(RowId id) {
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

    protected String getFromQueryClause() {
        return Joiner.on(", ").join(getTablesForQuery());
    }

    protected String[] getColumnsForQuery() {
        return getTable().getColumnNames();
    }

    public boolean exists(RowId id) {
        Cursor cursor= getDatabase().rawQuery("select count(*) count from " + getTable().getName() + " where " +
                getIdSelector(id), null);
        cursor.moveToFirst();
        int count = cursor.getInt(cursor.getColumnIndex("count"));
        cursor.close();
        return count != 0;
    }

    @Override
    public List<Entity> findAll() {
        return findFiltered(getBaseSelector(), getDefaultOrderBy());
    }

    public List<Entity> findFiltered(String selector, String orderBy) {
        String table = getFromQueryClause();
        String[] columns = getColumnsForQuery();
        Cursor cursor = database.query(true, table, columns, selector,
                null, null, null, orderBy, null);

        List<Entity> entities = Lists.newArrayListWithCapacity(cursor.getCount());
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Hashtable<String, Object> hashTable = getTableOfValues(cursor);
            entities.add(buildFromHashTable(hashTable));
            cursor.moveToNext();
        }
        return entities;
    }

    public void remove(RowData rowData) throws DataAccessException {
        removeById(getIdFromRowData(rowData));
    }

    public void removeById(RowId id) throws DataAccessException {
        String selector = getIdSelector(id);
        String table = getTable().getName();
        int returnVal = database.delete(table, selector);
        if (returnVal <= 0) {
            throw new DataAccessException("Database.delete for " + id + "returned " + returnVal);
        }
    }

    public RowId add(RowData rowData) throws DataAccessException {
        ContentValues values = getContentValues(rowData);
        String table = getTable().getName();
        long id = getDatabase().insert(table, values);
        Entity entity1 = getEntityFromRowData(rowData);
        if (id != -1 && !isIdSet(entity1)) {
            return setId(entity1, id);
        } else {
            throw new DataAccessException("Failed to insert " + rowData);
        }
    }

    protected abstract Entity getEntityFromRowData(RowData rowData);

    protected abstract boolean isIdSet(Entity entity);

    protected abstract RowId setId(Entity entity, long id);

    public void update(RowData rowData) throws DataAccessException {
        String selector = getIdSelector(getIdFromRowData(rowData));
        ContentValues values = getContentValues(rowData);
        String table = getTable().getName();
        int returnVal = getDatabase().update(table, values, selector);
        if (returnVal <= 0) {
            throw new DataAccessException("Failed to update (code " + returnVal + ") " + rowData);
        }
    }

    protected abstract RowId getIdFromRowData(RowData rowData);

    protected abstract ContentValues getContentValues(RowData rowData);

    protected abstract String getIdSelector(RowId id);

    protected abstract Entity buildFromHashTable(Hashtable<String, Object> hashTable);

    protected @Nullable String getBaseSelector() {
        return null;
    }

    protected String getDefaultOrderBy() {
        return null;
    }

    protected Database getDatabase() {
        return database;
    }

    protected Table getTable() {
        return table;
    }

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
