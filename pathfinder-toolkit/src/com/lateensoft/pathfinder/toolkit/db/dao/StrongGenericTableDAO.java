package com.lateensoft.pathfinder.toolkit.db.dao;

import android.content.ContentValues;
import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.dao.StrongGenericDAO;

public abstract class StrongGenericTableDAO<ID, T> extends GenericTableDAO<ID, T>
                                                              implements StrongGenericDAO<ID, T> {

    @Override
    public ID add(T entity) throws DataAccessException {
        ContentValues values = getContentValues(entity);
        String table = getTable().getName();
        long id = getDatabase().insert(table, values);
        if (id != -1 && !isIdSet(entity)) {
            return setRowId(entity, id);
        } else {
            throw new DataAccessException("Failed to insert " + entity);
        }
    }

    protected abstract boolean isIdSet(T entity);

    protected abstract ID setRowId(T entity, long id);

    @Override
    public void update(T entity) throws DataAccessException {
        String selector = getIdSelector(getIdFromEntity(entity));
        ContentValues values = getContentValues(entity);
        String table = getTable().getName();
        int returnVal = getDatabase().update(table, values, selector);
        if (returnVal <= 0) {
            throw new DataAccessException("Failed to update (code " + returnVal + ") " + entity);
        }
    }

    @Override
    public void remove(T entity) throws DataAccessException {
        removeById(getIdFromEntity(entity));
    }

    protected abstract ID getIdFromEntity(T entity);

    protected abstract ContentValues getContentValues(T entity);
}
