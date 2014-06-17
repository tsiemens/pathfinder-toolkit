package com.lateensoft.pathfinder.toolkit.db.dao;

import com.lateensoft.pathfinder.toolkit.dao.Identifiable;
import com.lateensoft.pathfinder.toolkit.dao.IdentifiableGenericDAO;

public abstract class RootIdentifiableTableDAO<T extends Identifiable> extends GenericTableDAO<Long, T, T>
        implements IdentifiableGenericDAO<T> {

    @Override
    protected T getEntityFromRowData(T rowData) {
        return rowData;
    }

    @Override
     protected Long getIdFromRowData(T rowData) {
        return rowData.getId();
    }

    @Override
    protected boolean isIdSet(T entity) {
        return entity.getId() != Identifiable.UNSET_ID;
    }

    @Override
    protected Long setId(T entity, long id) {
        entity.setId(id);
        return id;
    }
}
