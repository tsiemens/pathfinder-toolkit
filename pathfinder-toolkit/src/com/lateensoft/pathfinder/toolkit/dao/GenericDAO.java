package com.lateensoft.pathfinder.toolkit.dao;

import java.util.List;

/** A multi purpose DAO (Data Access Object) */
public interface GenericDAO<ID, T> {
    public T find(ID id);
    public List<T> findAll();
    public boolean exists(ID id);
    public void removeById(ID id) throws DataAccessException;
}
