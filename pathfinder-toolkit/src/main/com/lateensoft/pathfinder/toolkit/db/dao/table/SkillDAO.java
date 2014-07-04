package com.lateensoft.pathfinder.toolkit.db.dao.table;

import android.content.ContentValues;
import android.content.Context;
import com.lateensoft.pathfinder.toolkit.db.dao.OwnedIdentifiableTableDAO;
import com.lateensoft.pathfinder.toolkit.db.dao.OwnedObject;
import com.lateensoft.pathfinder.toolkit.db.dao.Table;
import com.lateensoft.pathfinder.toolkit.model.character.stats.*;

import java.util.Hashtable;

public class SkillDAO extends OwnedIdentifiableTableDAO<Long, Skill> {

    private static final String TABLE = "Skill";

    private static final String CHARACTER_ID = "character_id";
    private static final String SKILL_ID = "skill_id";
    private static final String SKILL_KEY = "skill_key";
    private static final String SUB_TYPE = "SubType";
    private static final String CLASS_SKILL = "ClassSkill";
    private static final String RANK = "Rank";
    private static final String MISC_MOD = "MiscMod";
    private static final String ABILITY_KEY = "ability_key";

    public SkillDAO(Context context) {
        super(context);
    }

    @Override
    protected Table initTable() {
        return new Table(TABLE, CHARACTER_ID, SKILL_ID, SKILL_KEY, SUB_TYPE,
                CLASS_SKILL, RANK, MISC_MOD, ABILITY_KEY);
    }

    @Override
    protected String getOwnerIdSelector(Long characterId) {
        return CHARACTER_ID + "=" + characterId;
    }

    @Override
    protected String getIdSelector(Long id) {
        return SKILL_ID + "=" + id;
    }

    @Override
    protected ContentValues getContentValues(OwnedObject<Long, Skill> rowData) {
        Skill object = rowData.getObject();
        ContentValues values = new ContentValues();
        if (isIdSet(rowData)) {
            values.put(SKILL_ID, object.getId());
        }
        values.put(SKILL_KEY, object.getType().getKey());
        values.put(CHARACTER_ID, object.getCharacterID());
        if (object.getSubType() != null) {
            values.put(SUB_TYPE, object.getSubType());
        }
        values.put(CLASS_SKILL, object.isClassSkill());
        values.put(RANK, object.getRank());
        values.put(MISC_MOD, object.getMiscMod());
        values.put(ABILITY_KEY, object.getAbility().getKey());
        return values;
    }

    @Override
    protected Skill buildFromHashTable(Hashtable<String, Object> hashTable) {
        long id = ((Long) hashTable.get(SKILL_ID));
        SkillType skillType = SkillType.forKey(((Long) hashTable.get(SKILL_KEY)).intValue());
        int characterId = ((Long) hashTable.get(CHARACTER_ID)).intValue();
        String subType = (String) hashTable.get(SUB_TYPE);
        boolean classSkill = ((Long) hashTable.get(CLASS_SKILL)).intValue() == 1;
        int rank = ((Long) hashTable.get(RANK)).intValue();
        int miscMod = ((Long) hashTable.get(MISC_MOD)).intValue();
        AbilityType abilityKey = AbilityType.forKey(((Long) hashTable.get(ABILITY_KEY)).intValue());

        return new Skill(id, characterId, skillType, subType, classSkill, rank,
                miscMod, abilityKey);
    }
}
