package com.lateensoft.pathfinder.toolkit.db.dao;

import com.lateensoft.pathfinder.toolkit.dao.*;

public abstract class OwnedIdentifiableTableDAO<OwnerId, T extends Identifiable>
        extends OwnedTableDAO<OwnerId, Long, T> implements IdentifiableGenericDAO<T>{

    @Override
    public void remove(T entity) throws DataAccessException {
        removeById(entity.getId());
    }

    @Override
    public Long add(OwnerId ownerId, T entity) throws DataAccessException {
        return add(OwnedObject.of(ownerId, entity));
    }

    @Override
    public void update(OwnerId ownerId, T entity) throws DataAccessException {
        update(OwnedObject.of(ownerId, entity));
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

    @Override
    protected Long getIdFromRowData(OwnedObject<OwnerId, T> rowData) {
        return rowData.getObject().getId();
    }
}
