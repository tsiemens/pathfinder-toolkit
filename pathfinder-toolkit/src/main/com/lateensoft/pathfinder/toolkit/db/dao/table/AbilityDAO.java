package com.lateensoft.pathfinder.toolkit.db.dao.table;

import android.content.ContentValues;
import android.content.Context;
import com.lateensoft.pathfinder.toolkit.db.dao.OwnedObject;
import com.lateensoft.pathfinder.toolkit.db.dao.OwnedWeakTableDAO;
import com.lateensoft.pathfinder.toolkit.db.dao.Table;
import com.lateensoft.pathfinder.toolkit.model.character.stats.Ability;
import com.lateensoft.pathfinder.toolkit.model.character.stats.AbilityType;

import java.util.Hashtable;

public class AbilityDAO extends OwnedWeakTableDAO<Long, Integer, Ability> {
    private static final String TABLE = "Ability";

    private static final String ABILITY_KEY = "ability_key";
    protected static final String CHARACTER_ID = "character_id";
    private static final String SCORE = "Score";
    private static final String TEMP = "Temp";

    public AbilityDAO(Context context) {
        super(context);
    }

    @Override
    protected Table initTable() {
        return new Table(TABLE, ABILITY_KEY, CHARACTER_ID, SCORE, TEMP);
    }

    @Override
    protected String getOwnerIdSelector(Long characterId) {
        return CHARACTER_ID + "=" + characterId;
    }

    @Override
    protected String getIdSelector(OwnedObject<Long, Integer> rowId) {
        return andSelectors(getOwnerIdSelector(rowId.getOwnerId()),
                ABILITY_KEY + "=" + Integer.toString(rowId.getObject()));
    }

    @Override
    protected OwnedObject<Long, Integer> getIdFromRowData(OwnedObject<Long, Ability> rowData) {
        return new OwnedObject<Long, Integer>(rowData.getOwnerId(), rowData.getObject().getType().getKey());
    }

    @Override
    protected ContentValues getContentValues(OwnedObject<Long, Ability> rowData) {
        Ability object = rowData.getObject();
        ContentValues values = new ContentValues();
        values.put(ABILITY_KEY, object.getType().getKey());
        values.put(CHARACTER_ID, rowData.getOwnerId());
        values.put(SCORE, object.getScore());
        values.put(TEMP, object.getTempBonus());
        return values;
    }



    @Override
    protected Ability buildFromHashTable(Hashtable<String, Object> hashTable) {
        AbilityType ability = AbilityType.forKey(((Long) hashTable.get(ABILITY_KEY)).intValue());
        long characterId  = (Long) hashTable.get(CHARACTER_ID);
        int score = ((Long) hashTable.get(SCORE)).intValue();
        int temp = ((Long) hashTable.get(TEMP)).intValue();
        return new Ability(ability, characterId, score, temp);
    }
}
