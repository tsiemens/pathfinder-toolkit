package com.lateensoft.pathfinder.toolkit.db.dao;

public abstract class WeakIdentifiableTableDAO<StrongId, T extends Identifiable>
        extends WeakGenericTableDAO<StrongId, Long, T> {

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
    protected Long getWeakIdFromEntity(T entity) {
        return entity.getId();
    }
}
