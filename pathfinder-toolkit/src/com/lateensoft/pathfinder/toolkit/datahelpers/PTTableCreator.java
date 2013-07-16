package com.lateensoft.pathfinder.toolkit.datahelpers;

public class PTTableCreator {
	
	public enum SQLDataType {
		NULL,
		INTEGER,
		REAL,
		TEXT,
		BLOB
	}
	
	public PTTableCreator() {
	}
	
	public String createStat() {
		return "CREATE TABLE Stat (stat_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"Name TEXT, BaseValue INTEGER)";
	}
	
	public String createSkill() {
		return "CREATE TABLE Skill (skill_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"Name TEXT, ClassSkill INTEGER, KeyAbility TEXT, AbilityMod INTEGER, " +
				"MiscMod INTEGER, ArmorCheckPenalty INTEGER, KeyAbilityKey INTEGER)";
	}
	
	public String createSave() {
		return "CREATE TABLE Save (" +
				"save_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"stat_id INTEGER, " +
				"Total INTEGER, " +
				"AbilityMod INTEGER, " +
				"MagicMod INTEGER, " +
				"MiscMod INTEGER, " +
				"TempMod INTEGER, " +
				"FOREIGN KEY(stat_id) REFRENCES Stat(stat_id)" +
				")";
	}
	
	public String createSaveSet() {
		return "CREATE TABLE SaveSet (" +
				"save_set_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"fort_save_id INTEGER, " +
				"refl_save_id INTEGER, " +
				"fort_save_id INTEGER, " +
				"FOREIGN KEY (fort_save_id) REFRENCES Save(save_id), " +
				"FOREIGN KEY (refl_save_id) REFRENCES Save(save_id), " +
				"FOREIGN KEY (fort_save_id) REFRENCES Save(save_id), " +
				")";
	}
	
	public String createAbilityScore() {
		return "CREATE TABLE AbilityScore (" +
				"blah";
	}
}
