package com.lateensoft.pathfinder.toolkit.db.dao.table;

import android.content.ContentValues;
import android.content.Context;
import com.lateensoft.pathfinder.toolkit.db.dao.OwnedIdentifiableTableDAO;
import com.lateensoft.pathfinder.toolkit.db.dao.OwnedObject;
import com.lateensoft.pathfinder.toolkit.db.dao.Table;
import com.lateensoft.pathfinder.toolkit.model.character.Spell;

import java.util.Hashtable;

public class SpellDAO extends OwnedIdentifiableTableDAO<Long, Spell> {
    private static final String TABLE = "Spell";

    private static final String ID = "spell_id";
    private static final String CHARACTER_ID = "character_id";
    private static final String NAME = "Name";
    private static final String PREPARED = "Prepared";
    private static final String LEVEL = "Level";
    private static final String DESC = "Description";

    public SpellDAO(Context context) {
        super(context);
    }

    @Override
    protected Table initTable() {
        return new Table(TABLE, ID, CHARACTER_ID, NAME, PREPARED, LEVEL, DESC);
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
    protected ContentValues getContentValues(OwnedObject<Long, Spell> rowData) {
        Spell object = rowData.getObject();
        ContentValues values = new ContentValues();
        if(isIdSet(rowData)) {
            values.put(ID, object.getId());
        }
        values.put(CHARACTER_ID, object.getCharacterID());
        values.put(NAME, object.getName());
        values.put(PREPARED, object.getPrepared());
        values.put(LEVEL, object.getLevel());
        values.put(DESC, object.getDescription());
        return values;
    }

    @Override
    protected Spell buildFromHashTable(Hashtable<String, Object> hashTable) {
        int id = ((Long) hashTable.get(ID)).intValue();
        int characterId = ((Long) hashTable.get(CHARACTER_ID)).intValue();
        String name = (String) hashTable.get(NAME);
        int prepared = ((Long) hashTable.get(PREPARED)).intValue();
        int level = ((Long) hashTable.get(LEVEL)).intValue();
        String desc = (String) hashTable.get(DESC);

        return new Spell(id, characterId, name, level, prepared, desc);
    }
}
