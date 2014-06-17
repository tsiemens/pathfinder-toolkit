package com.lateensoft.pathfinder.toolkit.db.dao;

import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.dao.OwnedWeakGenericDAO;

import java.util.List;

public abstract class OwnedWeakTableDAO<OwnerId, ID, T>
        extends GenericTableDAO<OwnedObject<OwnerId, ID>, T, OwnedObject<OwnerId, T>> implements OwnedWeakGenericDAO<OwnerId, ID, T> {

    @Override
    public T find(OwnerId ownerId, ID id) {
        return find(OwnedObject.of(ownerId, id));
    }

    @Override
    public boolean exists(OwnerId ownerId, ID id) {
        return exists(OwnedObject.of(ownerId, id));
    }

    @Override
    public void removeById(OwnerId ownerId, ID id) throws DataAccessException {
        removeById(OwnedObject.of(ownerId, id));
    }

    @Override
    public void remove(OwnerId ownerId, T entity) throws DataAccessException {
        remove(OwnedObject.of(ownerId, entity));
    }

    @Override
    public ID add(OwnerId ownerId, T entity) throws DataAccessException {
        return add(OwnedObject.of(ownerId, entity)).getObject();
    }

    @Override
    public void update(OwnerId ownerId, T entity) throws DataAccessException {
        update(OwnedObject.of(ownerId, entity));
    }

    @Override
    public List<T> findAllForOwner(OwnerId ownerId) {
        return findFiltered(andSelectors(getOwnerIdSelector(ownerId), getBaseSelector()),
                getDefaultOrderBy());
    }

    protected abstract String getOwnerIdSelector(OwnerId ownerId);
}
