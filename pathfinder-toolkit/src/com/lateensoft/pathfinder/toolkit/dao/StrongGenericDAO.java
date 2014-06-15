package com.lateensoft.pathfinder.toolkit.dao;

public interface StrongGenericDAO<ID, T> extends GenericDAO<ID, T> {
    public ID add(T entity) throws DataAccessException;
    public void update(T entity) throws DataAccessException;
    public void remove(T entity) throws DataAccessException;
}
