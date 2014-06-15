package com.lateensoft.pathfinder.toolkit.db.dao;

public abstract class StrongIdentifiableTableDAO<T extends Identifiable>
        extends StrongGenericTableDAO<Long, T> {

    @Override
    protected boolean isIdSet(T entity) {
        return entity.getId() != Identifiable.UNSET_ID;
    }

    @Override
    protected Long setRowId(T entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }

    @Override
    protected Long getIdFromEntity(T entity) {
        return entity.getId();
    }
}
