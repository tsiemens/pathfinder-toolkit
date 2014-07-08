package com.lateensoft.pathfinder.toolkit.db.dao.table;

import android.content.ContentValues;
import android.content.Context;
import com.lateensoft.pathfinder.toolkit.db.dao.Table;
import com.lateensoft.pathfinder.toolkit.model.IdNamePair;

import java.util.Hashtable;

public class EncounterDAO extends NamedListDAO {
    private static final String TABLE = "Encounter";

    private static final String ENCOUNTER_ID = "encounter_id";
    private static final String NAME = "Name";

    public EncounterDAO(Context context) {
        super(context);
    }

    @Override
    protected Table initTable() {
        return new Table(TABLE, ENCOUNTER_ID, NAME);
    }

    @Override
    protected String getIdSelector(Long id) {
        return ENCOUNTER_ID + "=" + id;
    }

    @Override
    protected ContentValues getContentValues(IdNamePair object) {
        ContentValues values = new ContentValues();
        if (isIdSet(object)) {
            values.put(ENCOUNTER_ID, object.getId());
        }
        values.put(NAME, object.getName());
        return values;
    }

    @Override
    protected IdNamePair buildFromHashTable(Hashtable<String, Object> hashTable) {
        long id = (Long) hashTable.get(ENCOUNTER_ID);
        String name = (String) hashTable.get(NAME);

        return new IdNamePair(id, name);
    }

    @Override
    protected String getDefaultOrderBy() {
        return NAME + " ASC";
    }


}
