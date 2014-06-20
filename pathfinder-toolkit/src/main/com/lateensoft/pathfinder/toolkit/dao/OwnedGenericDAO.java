package com.lateensoft.pathfinder.toolkit.dao;

import java.util.List;

public interface OwnedGenericDAO<OwnerId, ID, T> extends GenericDAO<T> {
    public ID add(OwnerId ownerId, T entity) throws DataAccessException;
    public void update(OwnerId ownerId, T entity) throws DataAccessException;
    public List<T> findAllForOwner(OwnerId ownerId);
}
