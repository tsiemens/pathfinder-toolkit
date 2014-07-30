package com.lateensoft.pathfinder.toolkit.db.dao.table;

import android.content.Context;
import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.dao.Identifiable;
import com.lateensoft.pathfinder.toolkit.dao.OwnedGenericDAO;
import com.lateensoft.pathfinder.toolkit.db.dao.RootIdentifiableTableDAO;

import java.util.List;

public abstract class ListBasedDAO<L extends List<T> & Identifiable, T, ListFields extends Identifiable>
        extends RootIdentifiableTableDAO<ListFields> {

    public ListBasedDAO(Context context) {
        super(context);
    }

    public Long add(L list) throws DataAccessException {
        long id = -1;
        try {
            beginTransaction();
            id = this.add(getListFields(list));
            list.setId(id);

            OwnedGenericDAO<Long, ?, T> itemDao = getItemDao();
            for (T member : list) {
                itemDao.add(id, member);
            }
            setTransactionSuccessful();
        } finally {
            endTransaction();
        }
        return id;
    }

    protected abstract ListFields getListFields(L list);

    protected abstract OwnedGenericDAO<Long, ?, T> getItemDao();

    public L findList(long id) {
        ListFields listFields = find(id);
        List<T> items = getItemDao().findAllForOwner(id);
        if (listFields == null || items == null) {
            return null;
        }
        return buildList(listFields, items);
    }

    protected abstract L buildList(ListFields fields, List<T> items);

    public void remove(L list) throws DataAccessException {
        removeById(list.getId());
    }

    public void updateFields(L list) throws DataAccessException {
        update(getListFields(list));
    }
}
