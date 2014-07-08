package com.lateensoft.pathfinder.toolkit.db.dao.table;

import android.content.Context;
import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.dao.OwnedGenericDAO;
import com.lateensoft.pathfinder.toolkit.db.dao.RootIdentifiableTableDAO;
import com.lateensoft.pathfinder.toolkit.model.IdNamePair;
import com.lateensoft.pathfinder.toolkit.model.NamedList;

import java.util.List;

public abstract class NamedListDAO extends RootIdentifiableTableDAO<IdNamePair> {

    public NamedListDAO(Context context) {
        super(context);
    }

    public <T> Long add(NamedList<T> list, OwnedGenericDAO<Long, ?, T> itemDao) throws DataAccessException {
        long id = -1;
        try {
            beginTransaction();
            id = this.add(list.idNamePair());
            list.setId(id);

            for (T member : list) {
                itemDao.add(id, member);
            }
            setTransactionSuccessful();
        } finally {
            endTransaction();
        }
        return id;
    }

    public <T> NamedList<T> find(long id, OwnedGenericDAO<Long, ?, T> itemDao) {
        IdNamePair idNamePair = find(id);
        List<T> items = itemDao.findAllForOwner(id);
        if (idNamePair == null || items == null) {
            return null;
        }
        return new NamedList<T>(idNamePair, items);
    }

    public void remove(NamedList list) throws DataAccessException {
        removeById(list.getId());
    }
}
