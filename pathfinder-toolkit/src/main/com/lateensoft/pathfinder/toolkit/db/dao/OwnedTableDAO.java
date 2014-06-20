package com.lateensoft.pathfinder.toolkit.db.dao;

import com.lateensoft.pathfinder.toolkit.dao.OwnedGenericDAO;

import java.util.List;

public abstract class OwnedTableDAO<OwnerId, RowId, T>
        extends GenericTableDAO<RowId, T, OwnedObject<OwnerId, T>> implements OwnedGenericDAO<OwnerId, RowId, T> {

    @Override
    public List<T> findAllForOwner(OwnerId ownerId) {
        return findFiltered(andSelectors(getOwnerIdSelector(ownerId), getBaseSelector()),
                getDefaultOrderBy());
    }

    @Override
    protected T getEntityFromRowData(OwnedObject<OwnerId, T> rowData) {
        return rowData.getObject();
    }

    protected abstract String getOwnerIdSelector(OwnerId ownerId);
}
