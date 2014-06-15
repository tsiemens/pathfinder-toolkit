package com.lateensoft.pathfinder.toolkit.db.repository;

import android.content.ContentValues;
import android.database.Cursor;
import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.model.IdStringPair;
import com.lateensoft.pathfinder.toolkit.model.NamedList;

import java.util.Hashtable;
import java.util.List;

public abstract class AbstractNamedListRepository<T> extends BaseRepository<NamedList<T>> {

    protected abstract String getIdColumn();
    protected abstract String getNameColumn();
    protected abstract long insertListItem(NamedList<T> list, T item);

	/**
	 * Inserts the character, and all subcomponents into database
	 * 
	 * @return the id of the character inserted, or -1 if failure occurred.
	 */
    @Override
	public long insert(NamedList<T> list) {
        long id = super.insert(list);
		if (id != -1) {
            long subitemId ;
            for (T subItem : list) {
                subitemId = insertListItem(list, subItem);
                if (subitemId == -1) {
                    delete(id);
                    return -1;
                }
            }
		}
		return id;
	}

	@Override
	protected ContentValues getContentValues(NamedList<T> object) {
		ContentValues values = new ContentValues();
		if (isIDSet(object)) { 
			values.put(getIdColumn(), object.getId());
		}
		values.put(getNameColumn(), object.getName());
		return values;
	}
	
	/**
	 * @return all lists as IdNamePairs, ordered alphabetically by name
	 */
	public List<IdStringPair> queryIdStringList() {
		String orderBy = getNameColumn() + " ASC";
		String table = m_tableInfo.getTable();
		String[] columns = m_tableInfo.getColumns();
		Cursor cursor = getDatabase().query(true, table, columns, null,
				null, null, null, orderBy, null);
		
		List<IdStringPair> lists = Lists.newArrayListWithCapacity(cursor.getCount());
        cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Hashtable<String, Object> hashTable =  getTableOfValues(cursor);
			lists.add(new IdStringPair((Long) hashTable.get(getIdColumn()),
                    (String) hashTable.get(getNameColumn())));
			cursor.moveToNext();
		}
		return lists;
	}
}
