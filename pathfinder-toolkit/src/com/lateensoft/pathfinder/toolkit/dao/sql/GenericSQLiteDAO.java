package com.lateensoft.pathfinder.toolkit.dao.sql;

import com.lateensoft.pathfinder.toolkit.dao.GenericDAO;

import java.util.List;

public interface GenericSQLiteDAO<ID, T> extends GenericDAO<ID, T> {
    public List<T> findFiltered(String selector, String orderBy);
}
