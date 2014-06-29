package com.lateensoft.pathfinder.toolkit.db.dao;

import android.content.Context;
import com.lateensoft.pathfinder.toolkit.dao.Identifiable;
import com.lateensoft.pathfinder.toolkit.dao.IdentifiableGenericDAO;

public abstract class RootIdentifiableTableDAO<T extends Identifiable> extends GenericTableDAO<Long, T, T>
        implements IdentifiableGenericDAO<T> {

    public RootIdentifiableTableDAO(Context context) {
        super(context);
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
    protected void setId(T entity, long id) {
        entity.setId(id);
    }
}
