package com.lateensoft.pathfinder.toolkit.db.dao;

import android.content.Context;
import com.lateensoft.pathfinder.toolkit.dao.OwnedGenericDAO;

import java.util.List;

public abstract class OwnedTableDAO<OwnerId, EntityId, RowId, Entity>
        extends GenericTableDAO<RowId, Entity, OwnedObject<OwnerId, Entity>> implements OwnedGenericDAO<OwnerId, EntityId, Entity> {

    public OwnedTableDAO(Context context) {
        super(context);
    }

    @Override
    public List<Entity> findAllForOwner(OwnerId ownerId) {
        return findFiltered(andSelectors(getOwnerIdSelector(ownerId), getBaseSelector()),
                getDefaultOrderBy());
    }

    protected abstract String getOwnerIdSelector(OwnerId ownerId);
}
