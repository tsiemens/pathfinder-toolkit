package com.lateensoft.pathfinder.toolkit.db.dao;

import android.content.Context;
import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.dao.OwnedWeakGenericDAO;

import java.util.List;

public abstract class OwnedWeakTableDAO<OwnerId, ID, T>
        extends GenericTableDAO<OwnedObject<OwnerId, ID>, T, OwnedObject<OwnerId, T>> implements OwnedWeakGenericDAO<OwnerId, ID, T> {

    public OwnedWeakTableDAO(Context context) {
        super(context);
    }

    @Override
    public final T find(OwnerId ownerId, ID id) {
        return find(OwnedObject.of(ownerId, id));
    }

    @Override
    public final boolean exists(OwnerId ownerId, ID id) {
        return exists(OwnedObject.of(ownerId, id));
    }

    @Override
    public final void removeById(OwnerId ownerId, ID id) throws DataAccessException {
        removeById(OwnedObject.of(ownerId, id));
    }

    @Override
    public final void remove(OwnerId ownerId, T entity) throws DataAccessException {
        remove(OwnedObject.of(ownerId, entity));
    }

    @Override
    public final ID add(OwnerId ownerId, T entity) throws DataAccessException {
        return add(OwnedObject.of(ownerId, entity)).getObject();
    }

    @Override
    public final void update(OwnerId ownerId, T entity) throws DataAccessException {
        update(OwnedObject.of(ownerId, entity));
    }

    @Override
    public List<T> findAllForOwner(OwnerId ownerId) {
        return findFiltered(andSelectors(getOwnerIdSelector(ownerId), getBaseSelector()),
                getDefaultOrderBy());
    }

    protected abstract String getOwnerIdSelector(OwnerId ownerId);
}
