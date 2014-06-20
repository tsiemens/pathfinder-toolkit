package com.lateensoft.pathfinder.toolkit.dao;

import java.util.List;

/** A base DAO (Data Access Object) */
public interface GenericDAO<T> {
    public List<T> findAll();
}
