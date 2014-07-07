package com.lateensoft.pathfinder.toolkit.db.dao.table;

import android.content.ContentValues;
import android.content.Context;
import com.lateensoft.pathfinder.toolkit.db.dao.Table;
import com.lateensoft.pathfinder.toolkit.model.NamedList;

import java.util.Hashtable;
import java.util.List;

public abstract class AbstractPartyDAO<Member> extends NamedListDAO<Member> {
    private static final String TABLE = "Party";

    private static final String PARTY_ID = "party_id";
    private static final String NAME = "Name";

    public AbstractPartyDAO(Context context) {
        super(context);
    }

    @Override
    protected Table initTable() {
        return new Table(TABLE, PARTY_ID, NAME);
    }

    @Override
    protected String getIdSelector(Long id) {
        return PARTY_ID + "=" + id;
    }

    @Override
    protected ContentValues getContentValues(NamedList<Member> party) {
        ContentValues values = new ContentValues();
        if (isIdSet(party)) {
            values.put(PARTY_ID, party.getId());
        }
        values.put(NAME, party.getName());
        return values;
    }

    @Override
    protected NamedList<Member> buildFromHashTable(Hashtable<String, Object> hashTable) {
        long id = (Long) hashTable.get(PARTY_ID);
        String name = (String) hashTable.get(NAME);
        List<Member> members = getElementDAO().findAllForOwner(id);

        return new NamedList<Member>(id, name, members);
    }

    @Override
    protected String getDefaultOrderBy() {
        return NAME + " ASC";
    }
}
