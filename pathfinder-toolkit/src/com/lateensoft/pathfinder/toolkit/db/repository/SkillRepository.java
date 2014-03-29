package com.lateensoft.pathfinder.toolkit.db.repository;

import java.util.Hashtable;
import java.util.List;

import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.db.repository.TableAttribute.SQLDataType;
import com.lateensoft.pathfinder.toolkit.model.character.stats.Skill;

import android.content.ContentValues;
import android.database.Cursor;
import com.lateensoft.pathfinder.toolkit.model.character.stats.SkillSet;

public class SkillRepository extends BaseRepository<Skill> {
	private static final String TABLE = "Skill";
	private static final String SKILL_ID = "skill_id";
	private static final String SKILL_KEY = "skill_key";
	private static final String SUB_TYPE = "SubType";
	private static final String CLASS_SKILL = "ClassSkill";
	private static final String RANK = "Rank";
	private static final String MISC_MOD = "MiscMod";
	private static final String ABILITY_KEY = "ability_key";
	
	public SkillRepository() {
		super();
		TableAttribute id = new TableAttribute(SKILL_ID, SQLDataType.INTEGER, true);
		TableAttribute skillKey = new TableAttribute(SKILL_KEY, SQLDataType.INTEGER);
		TableAttribute characterId = new TableAttribute(CHARACTER_ID, SQLDataType.INTEGER);
		TableAttribute subType = new TableAttribute(SUB_TYPE, SQLDataType.TEXT);
		TableAttribute classSkill = new TableAttribute(CLASS_SKILL, SQLDataType.INTEGER);
		TableAttribute rank = new TableAttribute(RANK, SQLDataType.INTEGER);
		TableAttribute miscMod = new TableAttribute(MISC_MOD, SQLDataType.INTEGER);
		TableAttribute abilityKey = new TableAttribute(ABILITY_KEY, SQLDataType.INTEGER);
		TableAttribute[] attributes = {id, skillKey, characterId, subType, classSkill, rank, miscMod, abilityKey};
		m_tableInfo = new TableInfo(TABLE, attributes);
	}
	
	@Override
	protected Skill buildFromHashTable(Hashtable<String, Object> hashTable) {
		long id = ((Long) hashTable.get(SKILL_ID));
		int skillKey = ((Long) hashTable.get(SKILL_KEY)).intValue();
		int characterId = ((Long) hashTable.get(CHARACTER_ID)).intValue();
		String subType = (String) hashTable.get(SUB_TYPE);
		boolean classSkill = ((Long) hashTable.get(CLASS_SKILL)).intValue() == 1;
		int rank = ((Long) hashTable.get(RANK)).intValue();
		int miscMod = ((Long) hashTable.get(MISC_MOD)).intValue();
		int abilityKey = ((Long) hashTable.get(ABILITY_KEY)).intValue();
		Skill skill = new Skill(id, characterId, skillKey, subType, classSkill, rank,
				miscMod, abilityKey);
		return skill;
	}
	
	@Override
	protected ContentValues getContentValues(Skill object) {
		ContentValues values = new ContentValues();
		if (isIDSet(object)) {
			values.put(SKILL_ID, object.getID());
		}
		values.put(SKILL_KEY, object.getSkillKey());
		values.put(CHARACTER_ID, object.getCharacterID());
		if (object.getSubType() != null) {
			values.put(SUB_TYPE, object.getSubType());
		}
		values.put(CLASS_SKILL, object.isClassSkill());
		values.put(RANK, object.getRank());
		values.put(MISC_MOD, object.getMiscMod());
		values.put(ABILITY_KEY, object.getAbilityKey());
		return values;
	}
	
	/**
	 * Returns all skills for the character with characterId
	 *
     * @param characterId
     * @return Array of Skill, ordered alphabetically by name
	 */
	public List<Skill> queryAllForCharacter(long characterId) {
		String selector = CHARACTER_ID + "=" + characterId; 
		String orderBy = SKILL_KEY+" ASC, "+SUB_TYPE+" ASC";
		String table = m_tableInfo.getTable();
		String[] columns = m_tableInfo.getColumns();
		Cursor cursor = getDatabase().query(true, table, columns, selector, 
				null, null, null, orderBy, null);
		
		List<Skill> skills = Lists.newArrayListWithCapacity(cursor.getCount());
        cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Hashtable<String, Object> hashTable =  getTableOfValues(cursor);
			skills.add(buildFromHashTable(hashTable));
			cursor.moveToNext();
		}
		return skills;
	}

    public SkillSet querySet(long characterId) {
        return SkillSet.newValidatedSkillSet(queryAllForCharacter(characterId),
                new SkillSet.CorrectionListener() {
                    @Override
                    public void onInvalidSkillRemoved(Skill removedSkill) {
                        SkillRepository.this.delete(removedSkill);
                    }

                    @Override
                    public void onMissingSkillAdded(Skill addedSkill) {
                        SkillRepository.this.insert(addedSkill);
                    }

                    @Override
                    public void onSkillModified(Skill modifiedSkill) {
                        SkillRepository.this.update(modifiedSkill);
                    }
                }
        );
    }
}
