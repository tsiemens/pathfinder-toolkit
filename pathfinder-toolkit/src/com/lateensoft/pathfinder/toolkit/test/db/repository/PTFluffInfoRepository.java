package com.lateensoft.pathfinder.toolkit.db.repository;

import java.util.Hashtable;

import android.content.ContentValues;

import com.lateensoft.pathfinder.toolkit.db.repository.PTTableAttribute.SQLDataType;
import com.lateensoft.pathfinder.toolkit.model.character.PTFluffInfo;

public class PTFluffInfoRepository extends PTBaseRepository<PTFluffInfo>{
	
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
	
	public PTFluffInfoRepository() {
		super();
		PTTableAttribute characterId = new PTTableAttribute(CHARACTER_ID, SQLDataType.INTEGER, true);
		PTTableAttribute name = new PTTableAttribute(NAME, SQLDataType.TEXT);
		PTTableAttribute alignment = new PTTableAttribute(ALIGNMENT, SQLDataType.TEXT);
		PTTableAttribute xp = new PTTableAttribute(XP, SQLDataType.TEXT);
		PTTableAttribute nextLevelXp = new PTTableAttribute(NEXT_LEVEL_XP, SQLDataType.TEXT);
		PTTableAttribute playerClass = new PTTableAttribute(PLAYER_CLASS, SQLDataType.TEXT);
		PTTableAttribute race = new PTTableAttribute(RACE, SQLDataType.TEXT);
		PTTableAttribute deity = new PTTableAttribute(DEITY, SQLDataType.TEXT);
		PTTableAttribute level = new PTTableAttribute(LEVEL, SQLDataType.TEXT);
		PTTableAttribute size = new PTTableAttribute(SIZE, SQLDataType.TEXT);
		PTTableAttribute gender = new PTTableAttribute(GENDER, SQLDataType.TEXT);
		PTTableAttribute height = new PTTableAttribute(HEIGHT, SQLDataType.TEXT);
		PTTableAttribute weight = new PTTableAttribute(WEIGHT, SQLDataType.TEXT);
		PTTableAttribute eyes = new PTTableAttribute(EYES, SQLDataType.TEXT);
		PTTableAttribute hair = new PTTableAttribute(HAIR, SQLDataType.TEXT);
		PTTableAttribute languages = new PTTableAttribute(LANGUAGES, SQLDataType.TEXT);
		PTTableAttribute description = new PTTableAttribute(DESCRIPTION, SQLDataType.TEXT);
		
		PTTableAttribute[] columns = {characterId, name, alignment, xp, nextLevelXp, playerClass,
				race, deity, level, size, gender, height, weight, eyes, hair, languages, description};
		m_tableInfo = new PTTableInfo(TABLE, columns);
	}
	
	@Override
	protected PTFluffInfo buildFromHashTable(Hashtable<String, Object> hashTable) {
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
		
		PTFluffInfo fluff = new PTFluffInfo();
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
	protected ContentValues getContentValues(PTFluffInfo object) {
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
