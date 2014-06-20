package com.lateensoft.pathfinder.toolkit.dao;

public interface OwnedWeakGenericDAO<OwnerId, ID, T>
        extends OwnedGenericDAO<OwnerId, ID, T> {
    public T find(OwnerId ownerId, ID id);
    public boolean exists(OwnerId ownerId, ID id);
    public void removeById(OwnerId ownerId, ID id) throws DataAccessException;
    public void remove(OwnerId ownerId, T entity) throws DataAccessException;
}
