package com.lateensoft.pathfinder.toolkit.db.dao.table;

import android.content.ContentValues;
import android.content.Context;
import com.lateensoft.pathfinder.toolkit.db.dao.OwnedObject;
import com.lateensoft.pathfinder.toolkit.db.dao.OwnedWeakTableDAO;
import com.lateensoft.pathfinder.toolkit.db.dao.Table;
import com.lateensoft.pathfinder.toolkit.model.character.stats.Ability;
import com.lateensoft.pathfinder.toolkit.model.character.stats.AbilityType;

import java.util.Hashtable;

public class AbilityDAO extends OwnedWeakTableDAO<Long, AbilityType, Ability> {
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
    protected String getIdSelector(OwnedObject<Long, AbilityType> rowId) {
        return andSelectors(getOwnerIdSelector(rowId.getOwnerId()),
                ABILITY_KEY + "=" + Integer.toString(rowId.getObject().getKey()));
    }

    @Override
    protected OwnedObject<Long, AbilityType> getIdFromRowData(OwnedObject<Long, Ability> rowData) {
        return new OwnedObject<Long, AbilityType>(rowData.getOwnerId(), rowData.getObject().getType());
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
        int score = ((Long) hashTable.get(SCORE)).intValue();
        int temp = ((Long) hashTable.get(TEMP)).intValue();
        return new Ability(ability, score, temp);
    }

    @Override
    protected String getDefaultOrderBy() {
        return ABILITY_KEY + " ASC";
    }
}
