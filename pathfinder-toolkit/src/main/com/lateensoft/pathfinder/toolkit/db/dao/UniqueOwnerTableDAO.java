package com.lateensoft.pathfinder.toolkit.db.dao;

import android.content.Context;
import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.dao.OwnedWeakGenericDAO;
import com.lateensoft.pathfinder.toolkit.dao.UniqueOwnerGenericDAO;

public abstract class UniqueOwnerTableDAO<OwnerId, Entity>
        extends OwnedTableDAO<OwnerId, Void, OwnerId, Entity>
        implements UniqueOwnerGenericDAO<OwnerId, Entity> {

    public UniqueOwnerTableDAO(Context context) {
        super(context);
    }

    @Override
    public Void add(OwnerId ownerId, Entity entity) throws DataAccessException {
        add(OwnedObject.of(ownerId, entity));
        return null;
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
        // Do nothing, since uniquely owned entities do not use auto incremented Ids
    }
}
