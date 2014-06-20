package com.lateensoft.pathfinder.toolkit.db.dao;

import android.content.Context;
import com.lateensoft.pathfinder.toolkit.dao.OwnedGenericDAO;

import java.util.List;

public abstract class OwnedTableDAO<OwnerId, RowId, T>
        extends GenericTableDAO<RowId, T, OwnedObject<OwnerId, T>> implements OwnedGenericDAO<OwnerId, RowId, T> {

    public OwnedTableDAO(Context context) {
        super(context);
    }

    @Override
    public List<T> findAllForOwner(OwnerId ownerId) {
        return findFiltered(andSelectors(getOwnerIdSelector(ownerId), getBaseSelector()),
                getDefaultOrderBy());
    }

    protected abstract String getOwnerIdSelector(OwnerId ownerId);
}
