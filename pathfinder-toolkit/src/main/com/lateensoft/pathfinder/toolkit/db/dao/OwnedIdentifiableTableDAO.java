package com.lateensoft.pathfinder.toolkit.db.dao;

import android.content.Context;
import com.lateensoft.pathfinder.toolkit.dao.*;

public abstract class OwnedIdentifiableTableDAO<OwnerId, T extends Identifiable>
        extends OwnedTableDAO<OwnerId, Long, T> implements IdentifiableGenericDAO<T>{

    public OwnedIdentifiableTableDAO(Context context) {
        super(context);
    }

    @Override
    public void remove(T entity) throws DataAccessException {
        removeById(entity.getId());
    }

    @Override
    public final Long add(OwnerId ownerId, T entity) throws DataAccessException {
        return add(OwnedObject.of(ownerId, entity));
    }

    @Override
    public final void update(OwnerId ownerId, T entity) throws DataAccessException {
        update(OwnedObject.of(ownerId, entity));
    }

    @Override
    protected boolean isIdSet(OwnedObject<OwnerId, T> entity) {
        return entity.getObject().getId() != Identifiable.UNSET_ID;
    }

    @Override
    protected Long setId(OwnedObject<OwnerId, T> entity, long id) {
        entity.getObject().setId(id);
        return id;
    }

    @Override
    protected Long getIdFromRowData(OwnedObject<OwnerId, T> rowData) {
        return rowData.getObject().getId();
    }
}
