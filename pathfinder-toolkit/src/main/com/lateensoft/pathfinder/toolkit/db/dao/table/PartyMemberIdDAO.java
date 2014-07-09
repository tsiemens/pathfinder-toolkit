package com.lateensoft.pathfinder.toolkit.db.dao.table;

import android.content.ContentValues;
import android.content.Context;
import com.lateensoft.pathfinder.toolkit.db.dao.OwnedObject;

import java.util.Hashtable;
import java.util.List;

public class PartyMemberIdDAO extends AbstractPartyMembershipDAO<Long> {

    public PartyMemberIdDAO(Context context) {
        super(context);
    }

    @Override
    protected OwnedObject<Long, Long> getIdFromRowData(OwnedObject<Long, Long> rowData) {
        return OwnedObject.of(rowData.getOwnerId(), rowData.getObject());
    }

    @Override
    protected ContentValues getContentValues(OwnedObject<Long, Long> rowData) {
        ContentValues values = new ContentValues();
        values.put(PARTY_ID, rowData.getOwnerId());
        values.put(CHARACTER_ID, rowData.getObject());
        return values;
    }

    @Override
    protected Long buildFromHashTable(Hashtable<String, Object> hashTable) {
        return ((Long) hashTable.get(CHARACTER_ID));
    }
}
