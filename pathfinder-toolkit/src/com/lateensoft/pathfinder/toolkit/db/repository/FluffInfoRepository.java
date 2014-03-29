package com.lateensoft.pathfinder.toolkit.db.repository;

import java.util.Hashtable;

import android.content.ContentValues;

import com.lateensoft.pathfinder.toolkit.db.repository.TableAttribute.SQLDataType;
import com.lateensoft.pathfinder.toolkit.model.character.FluffInfo;

public class FluffInfoRepository extends BaseRepository<FluffInfo> {
	
	public static final String TABLE = "FluffInfo";
	public static final String NAME = "Name";
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
	
	public FluffInfoRepository() {
		super();
		TableAttribute characterId = new TableAttribute(CHARACTER_ID, SQLDataType.INTEGER, true);
		TableAttribute name = new TableAttribute(NAME, SQLDataType.TEXT);
		TableAttribute alignment = new TableAttribute(ALIGNMENT, SQLDataType.TEXT);
		TableAttribute xp = new TableAttribute(XP, SQLDataType.TEXT);
		TableAttribute nextLevelXp = new TableAttribute(NEXT_LEVEL_XP, SQLDataType.TEXT);
		TableAttribute playerClass = new TableAttribute(PLAYER_CLASS, SQLDataType.TEXT);
		TableAttribute race = new TableAttribute(RACE, SQLDataType.TEXT);
		TableAttribute deity = new TableAttribute(DEITY, SQLDataType.TEXT);
		TableAttribute level = new TableAttribute(LEVEL, SQLDataType.TEXT);
		TableAttribute size = new TableAttribute(SIZE, SQLDataType.TEXT);
		TableAttribute gender = new TableAttribute(GENDER, SQLDataType.TEXT);
		TableAttribute height = new TableAttribute(HEIGHT, SQLDataType.TEXT);
		TableAttribute weight = new TableAttribute(WEIGHT, SQLDataType.TEXT);
		TableAttribute eyes = new TableAttribute(EYES, SQLDataType.TEXT);
		TableAttribute hair = new TableAttribute(HAIR, SQLDataType.TEXT);
		TableAttribute languages = new TableAttribute(LANGUAGES, SQLDataType.TEXT);
		TableAttribute description = new TableAttribute(DESCRIPTION, SQLDataType.TEXT);
		
		TableAttribute[] columns = {characterId, name, alignment, xp, nextLevelXp, playerClass,
				race, deity, level, size, gender, height, weight, eyes, hair, languages, description};
		m_tableInfo = new TableInfo(TABLE, columns);
	}
	
	@Override
	protected FluffInfo buildFromHashTable(Hashtable<String, Object> hashTable) {
		long characterId = (Long) hashTable.get(CHARACTER_ID);
		String name = (String) hashTable.get(NAME);
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
		fluff.setID(characterId);
		fluff.setName(name);
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
	
	@Override
	protected ContentValues getContentValues(FluffInfo object) {
		ContentValues values = new ContentValues();
		values.put(CHARACTER_ID, object.getID());
		values.put(NAME, object.getName());
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
}
