package com.lateensoft.pathfinder.toolkit.db.dao.table;

import android.content.ContentValues;
import android.content.Context;
import com.lateensoft.pathfinder.toolkit.db.dao.OwnedObject;
import com.lateensoft.pathfinder.toolkit.db.dao.OwnedWeakTableDAO;
import com.lateensoft.pathfinder.toolkit.db.dao.Table;
import com.lateensoft.pathfinder.toolkit.model.character.FluffInfo;

import java.util.Hashtable;

public class FluffDAO extends OwnedWeakTableDAO<Long, Void, FluffInfo> {
    public static final String TABLE = "FluffInfo";

    private static final String CHARACTER_ID = "character_id";
    private static final String ALIGNMENT = "Alignment";
    private static final String XP = "XP";
    private static final String NEXT_LEVEL_XP = "NextLevelXP";
    private static final String PLAYER_CLASS = "PlayerClass";
    private static final String RACE = "Race";
    private static final String DEITY = "Deity";
    private static final String LEVEL = "Level";
    private static final String SIZE = "Size";
    private static final String GENDER = "Gender";
    private static final String HEIGHT = "Height";
    private static final String WEIGHT = "Weight";
    private static final String EYES = "Eyes";
    private static final String HAIR = "Hair";
    private static final String LANGUAGES = "Languages";
    private static final String DESCRIPTION = "Description";

    public FluffDAO(Context context) {
        super(context);
    }

    @Override
    protected Table initTable() {
        return new Table(TABLE, CHARACTER_ID, ALIGNMENT, XP, NEXT_LEVEL_XP, PLAYER_CLASS,
                RACE, DEITY, LEVEL, SIZE, GENDER, HEIGHT, WEIGHT, EYES, HAIR, LANGUAGES, DESCRIPTION);
    }

    @Override
    protected String getOwnerIdSelector(Long characterId) {
        return CHARACTER_ID + "=" + characterId;
    }

    @Override
    protected String getIdSelector(OwnedObject<Long, Void> rowId) {
        return getOwnerIdSelector(rowId.getOwnerId());
    }

    @Override
    protected OwnedObject<Long, Void> getIdFromRowData(OwnedObject<Long, FluffInfo> rowData) {
        return new OwnedObject<Long, Void>(rowData.getOwnerId(), null);
    }

    @Override
    protected ContentValues getContentValues(OwnedObject<Long, FluffInfo> rowData) {
        FluffInfo object = rowData.getObject();
        ContentValues values = new ContentValues();
        values.put(CHARACTER_ID, object.getId());
        values.put(ALIGNMENT, object.getAlignment());
        values.put(XP, object.getXP());
        values.put(NEXT_LEVEL_XP, object.getNextLevelXP());
        values.put(PLAYER_CLASS, object.getPlayerClass());
        values.put(RACE, object.getRace());
        values.put(DEITY, object.getDeity());
        values.put(LEVEL, object.getLevel());
        values.put(SIZE, object.getSize());
        values.put(GENDER, object.getGender());
        values.put(HEIGHT, object.getHeight());
        values.put(WEIGHT, object.getWeight());
        values.put(EYES, object.getEyes());
        values.put(HAIR, object.getHair());
        values.put(LANGUAGES, object.getLanguages());
        values.put(DESCRIPTION, object.getDescription());
        return values;
    }

    @Override
    protected FluffInfo buildFromHashTable(Hashtable<String, Object> hashTable) {
        long characterId = (Long) hashTable.get(CHARACTER_ID);
        String alignment = (String) hashTable.get(ALIGNMENT);
        String xp = (String) hashTable.get(XP);
        String nextLevelXp = (String) hashTable.get(NEXT_LEVEL_XP);
        String playerClass = (String) hashTable.get(PLAYER_CLASS);
        String race = (String) hashTable.get(RACE);
        String deity = (String) hashTable.get(DEITY);
        String level = (String) hashTable.get(LEVEL);
        String size = (String) hashTable.get(SIZE);
        String gender = (String) hashTable.get(GENDER);
        String height = (String) hashTable.get(HEIGHT);
        String weight = (String) hashTable.get(WEIGHT);
        String eyes = (String) hashTable.get(EYES);
        String hair = (String) hashTable.get(HAIR);
        String languages = (String) hashTable.get(LANGUAGES);
        String description = (String) hashTable.get(DESCRIPTION);

        FluffInfo fluff = new FluffInfo();
        fluff.setId(characterId);
        fluff.setAlignment(alignment);
        fluff.setXP(xp);
        fluff.setNextLevelXP(nextLevelXp);
        fluff.setPlayerClass(playerClass);
        fluff.setRace(race);
        fluff.setDeity(deity);
        fluff.setLevel(level);
        fluff.setSize(size);
        fluff.setGender(gender);
        fluff.setHeight(height);
        fluff.setWeight(weight);
        fluff.setEyes(eyes);
        fluff.setHair(hair);
        fluff.setLanguages(languages);
        fluff.setDescription(description);
        return fluff;
    }
}
