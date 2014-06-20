package com.lateensoft.pathfinder.toolkit.dao;

public interface IdentifiableGenericDAO<T extends Identifiable> extends GenericDAO<T> {
    public T find(Long id);
    public boolean exists(Long id);
    public void removeById(Long id) throws DataAccessException;
    public void remove(T entity) throws DataAccessException;
}
