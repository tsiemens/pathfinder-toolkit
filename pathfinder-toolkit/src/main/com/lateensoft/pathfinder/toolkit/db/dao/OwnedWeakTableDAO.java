package com.lateensoft.pathfinder.toolkit.db.dao;

import android.content.Context;
import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.dao.OwnedWeakGenericDAO;

public abstract class OwnedWeakTableDAO<OwnerId, EntityId, Entity>
        extends OwnedTableDAO<OwnerId, EntityId, OwnedObject<OwnerId, EntityId>, Entity>
        implements OwnedWeakGenericDAO<OwnerId, EntityId, Entity> {

    public OwnedWeakTableDAO(Context context) {
        super(context);
    }

    @Override
    public final Entity find(OwnerId ownerId, EntityId entityId) {

        return find(OwnedObject.of(ownerId, entityId));
    }

    @Override
    public final boolean exists(OwnerId ownerId, EntityId entityId) {
        return exists(OwnedObject.of(ownerId, entityId));
    }

    @Override
    public final void removeById(OwnerId ownerId, EntityId entityId) throws DataAccessException {
        removeById(OwnedObject.of(ownerId, entityId));
    }

    @Override
    public final void remove(OwnerId ownerId, Entity entity) throws DataAccessException {
        remove(OwnedObject.of(ownerId, entity));
    }

    @Override
    public final EntityId add(OwnerId ownerId, Entity entity) throws DataAccessException {
        return add(OwnedObject.of(ownerId, entity)).getObject();
    }

    @Override
    public final void update(OwnerId ownerId, Entity entity) throws DataAccessException {
        update(OwnedObject.of(ownerId, entity));
    }

    @Override
    protected boolean isIdSet(OwnedObject<OwnerId, Entity> entity) {
        return true;
    }

    @Override
    protected void setId(OwnedObject<OwnerId, Entity> entity, long id) {
        // Do nothing, since weak entities do not use auto incremented Ids
    }
}
