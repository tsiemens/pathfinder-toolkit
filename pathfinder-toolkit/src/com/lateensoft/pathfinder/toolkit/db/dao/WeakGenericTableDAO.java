package com.lateensoft.pathfinder.toolkit.db.dao;

import android.content.ContentValues;
import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.dao.WeakGenericDAO;

import java.util.List;

import static com.lateensoft.pathfinder.toolkit.dao.WeakGenericDAO.IdPair;

public abstract class WeakGenericTableDAO<StrongId, WeakId, T>
        extends GenericTableDAO<IdPair<StrongId, WeakId>, T>
        implements WeakGenericDAO<StrongId, WeakId, T> {

    public T find(StrongId strongId, WeakId weakId) {
        return super.find(idFor(strongId, weakId));
    }

    @Override
    public List<T> findAll(StrongId strongId) {
        return findFiltered(andSelectors(getStrongIdSelector(strongId), getBaseSelector()),
                getDefaultOrderBy());
    }

    public abstract String getStrongIdSelector(StrongId strongId);

    public boolean exists(StrongId strongId, WeakId weakId) {
        return super.exists(idFor(strongId, weakId));
    }

    @Override
    public void remove(StrongId strongId, T entity) throws DataAccessException {
        removeById(strongId, getWeakIdFromEntity(entity));
    }

    public void removeById(StrongId strongId, WeakId weakId) throws DataAccessException {
        super.removeById(idFor(strongId, weakId));
    }

    @Override
    public WeakId add(StrongId strongId, T entity) throws DataAccessException {
        ContentValues values = getContentValues(strongId, entity);
        String table = getTable().getName();
        long id = getDatabase().insert(table, values);
        if (id != -1 && !isIdSet(entity)) {
            return setRowId(entity, id);
        } else {
            throw new DataAccessException("Failed to insert " + entity);
        }
    }

    protected abstract boolean isIdSet(T entity);

    protected abstract WeakId setRowId(T entity, long rowId);

    @Override
    public void update(StrongId strongId, T object) throws DataAccessException {
        String selector = getIdSelector(idFor(strongId, getWeakIdFromEntity(object)));
        ContentValues values = getContentValues(strongId, object);
        String table = getTable().getName();
        int returnVal = getDatabase().update(table, values, selector);
        if (returnVal <= 0) {
            throw new DataAccessException("Failed to update (code " + returnVal + ") " + object);
        }
    }

    protected abstract WeakId getWeakIdFromEntity(T entity);

    protected abstract ContentValues getContentValues(StrongId strongId, T entity);

    private IdPair<StrongId, WeakId> idFor(StrongId strongId, WeakId weakId) {
        return new IdPair<StrongId, WeakId>(strongId, weakId);
    }
}
