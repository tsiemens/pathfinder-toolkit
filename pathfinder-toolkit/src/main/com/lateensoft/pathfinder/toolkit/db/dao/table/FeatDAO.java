package com.lateensoft.pathfinder.toolkit.db.dao.table;

import android.content.ContentValues;
import android.content.Context;
import com.lateensoft.pathfinder.toolkit.db.dao.OwnedIdentifiableTableDAO;
import com.lateensoft.pathfinder.toolkit.db.dao.OwnedObject;
import com.lateensoft.pathfinder.toolkit.db.dao.Table;
import com.lateensoft.pathfinder.toolkit.model.character.Feat;

import java.util.Hashtable;

public class FeatDAO extends OwnedIdentifiableTableDAO<Long, Feat> {
    public static final String TABLE = "Feat";

    protected static final String CHARACTER_ID = "character_id";
    public static final String ID = "feat_id";
    private static final String NAME = "Name";
    private static final String DESC = "Description";

    public FeatDAO(Context context) {
        super(context);
    }

    @Override
    protected Table initTable() {
        return new Table(TABLE, CHARACTER_ID, ID, NAME, DESC);
    }

    @Override
    protected String getOwnerIdSelector(Long characterId) {
        return CHARACTER_ID + "=" + characterId;
    }

    @Override
    protected String getIdSelector(Long id) {
        return ID + "=" + id;
    }

    @Override
    protected ContentValues getContentValues(OwnedObject<Long, Feat> rowData) {
        Feat object = rowData.getObject();
        ContentValues values = new ContentValues();
        if(isIdSet(rowData)) {
            values.put(ID, object.getId());
        }
        values.put(CHARACTER_ID, rowData.getOwnerId());
        values.put(NAME, object.getName());
        values.put(DESC, object.getDescription());
        return values;
    }

    @Override
    protected Feat buildFromHashTable(Hashtable<String, Object> hashTable) {
        int id = ((Long) hashTable.get(ID)).intValue();
        int characterId = ((Long) hashTable.get(CHARACTER_ID)).intValue();
        String name = (String) hashTable.get(NAME);
        String desc = (String) hashTable.get(DESC);

        return new Feat(id, characterId, name, desc);
    }
}
