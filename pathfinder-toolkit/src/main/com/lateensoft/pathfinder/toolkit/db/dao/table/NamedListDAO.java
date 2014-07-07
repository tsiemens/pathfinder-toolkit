package com.lateensoft.pathfinder.toolkit.db.dao.table;

import android.content.Context;
import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.dao.OwnedWeakGenericDAO;
import com.lateensoft.pathfinder.toolkit.db.dao.RootIdentifiableTableDAO;
import com.lateensoft.pathfinder.toolkit.model.NamedList;

public abstract class NamedListDAO<T> extends RootIdentifiableTableDAO<NamedList<T>> {

    public NamedListDAO(Context context) {
        super(context);
    }

    @Override
    public Long add(NamedList<T> party) throws DataAccessException {
        long id = -1;
        try {
            beginTransaction();
            id = super.add(party);

            for (T member : party) {
                getElementDAO().add(id, member);
            }
            setTransactionSuccessful();
        } finally {
            endTransaction();
        }
        return id;
    }

    protected abstract OwnedWeakGenericDAO<Long, ?, T> getElementDAO();
}
