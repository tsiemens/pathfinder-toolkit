package com.lateensoft.pathfinder.toolkit.db.dao.table;

import android.content.ContentValues;
import android.content.Context;
import com.lateensoft.pathfinder.toolkit.db.dao.OwnedObject;
import com.lateensoft.pathfinder.toolkit.db.dao.OwnedWeakTableDAO;
import com.lateensoft.pathfinder.toolkit.db.dao.Table;
import com.lateensoft.pathfinder.toolkit.model.character.stats.AbilityType;
import com.lateensoft.pathfinder.toolkit.model.character.stats.Save;
import com.lateensoft.pathfinder.toolkit.model.character.stats.SaveType;

import java.util.Hashtable;

public class SaveDAO extends OwnedWeakTableDAO<Long, Integer, Save> {
    static final String TABLE = "Save";

    private static final String CHARACTER_ID = "character_id";
    private static final String SAVE_KEY = "save_key";
    private static final String BASE_VALUE = "BaseValue";
    private static final String ABILITY_KEY = "ability_key";
    private static final String MAGIC_MOD = "MagicMod";
    private static final String MISC_MOD = "MiscMod";
    private static final String TEMP_MOD = "TempMod";

    public SaveDAO(Context context) {
        super(context);
    }

    @Override
    protected Table initTable() {
        return new Table(TABLE, CHARACTER_ID, SAVE_KEY, BASE_VALUE, ABILITY_KEY,
                MAGIC_MOD, MISC_MOD, TEMP_MOD);
    }

    @Override
    protected String getOwnerIdSelector(Long characterId) {
        return CHARACTER_ID + "=" + characterId;
    }

    @Override
    protected String getIdSelector(OwnedObject<Long, Integer> rowId) {
        return andSelectors(getOwnerIdSelector(rowId.getOwnerId()),
                SAVE_KEY + "=" + Integer.toString(rowId.getObject()));
    }

    @Override
    protected OwnedObject<Long, Integer> getIdFromRowData(OwnedObject<Long, Save> rowData) {
        return new OwnedObject<Long, Integer>(rowData.getOwnerId(), rowData.getObject().getType().getKey());
    }

    @Override
    protected ContentValues getContentValues(OwnedObject<Long, Save> rowData) {
        Save object = rowData.getObject();
        ContentValues values = new ContentValues();
        values.put(SAVE_KEY, object.getType().getKey());
        values.put(CHARACTER_ID, object.getCharacterID());
        values.put(BASE_VALUE, object.getBaseSave());
        values.put(ABILITY_KEY, object.getAbilityType().getKey());
        values.put(MAGIC_MOD, object.getMagicMod());
        values.put(MISC_MOD, object.getMiscMod());
        values.put(TEMP_MOD, object.getTempMod());
        return values;
    }

    @Override
    protected Save buildFromHashTable(Hashtable<String, Object> hashTable) {
        SaveType saveKey = SaveType.forKey(((Long) hashTable.get(SAVE_KEY)).intValue());
        int characterId = ((Long) hashTable.get(CHARACTER_ID)).intValue();
        int baseValue = ((Long) hashTable.get(BASE_VALUE)).intValue();
        AbilityType abilityKey = AbilityType.forKey(((Long) hashTable.get(ABILITY_KEY)).intValue());
        int magicMod = ((Long) hashTable.get(MAGIC_MOD)).intValue();
        int miscMod = ((Long) hashTable.get(MISC_MOD)).intValue();
        int tempMod = ((Long) hashTable.get(TEMP_MOD)).intValue();

        return new Save(saveKey, characterId, baseValue, abilityKey, magicMod,
                miscMod, tempMod);
    }
}
