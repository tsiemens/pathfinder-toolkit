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
		return "CREATE TABLE Stat (" +
				"stat_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"Name TEXT, " +
				"BaseValue INTEGER)";
	}
	
	public String createSkill() {
		return "CREATE TABLE Skill (" +
				"skill_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"Name TEXT, " +
				"ClassSkill INTEGER, " +
				"KeyAbility TEXT, " +
				"AbilityMod INTEGER, " +
				"MiscMod INTEGER, " +
				"ArmorCheckPenalty INTEGER, " +
				"KeyAbilityKey INTEGER)";
	}
	
	public String createSkillSet() {
		return "CREATE TABLE SkillSet (skill_set_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"skill_ids TEXT)";
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
				"FOREIGN KEY (fort_save_id) REFRENCES Save(save_id))";
	}
	
	public String createCombatStatSet() {
		return "CREATE TABLE CombatStatSet (" +
				"combat_stat_set_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"TotalHP INTEGER, " +
				"Wounds INTEGER, " +
				"NonLethalDamange INTEGER, " +
				"DmaageReduction INTEGER, " +
				"BaseSpeedFt INTEGER, " +
				"InitDexMod INTEGER, " +
				"InitMiscMod INTEGER, " +
				"ACArmour INTEGER, " +
				"ACShield INTEGER, " +
				"ACDexMod INTEGER," +
				"SizeMod INTEGER, " +
				"ACNaturalArmour, INTEGER, " +
				"DeflectionMod INTEGER, " +
				"ACMiscMod INTEGER, " +
				"BABPrimary INTEGER, " +
				"BABSecondary INTEGER, " +
				"StrengthMod INTEGER, " +
				"CMDDexMod INTEGER, " +
				"CMDMiscMod INTEGER, " +
				"SpellResist INTEGER)";
	}
	
	public String createAbilityScore() {
		return "CREATE TABLE AbilityScore (" +
				"ability_score_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"Ability TEXT, " +
				"Score INTEGER" +
				")";
	}
	
	public String createAbilitySet() {
		return "CREATE TABLE AbilitySet (" +
				"ability_set_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"str_key INTEGER, " +
				"dex_key INTEGER, " +
				"con_key INTEGER, " +
				"int_key INTEGER, " +
				"wis_key INTEGER, " +
				"cha_key ITNEGER, " +
				"FOREIGN KEY (str_key_integer) REFRENCES AbilityScore(ability_score_id), " +
				"FOREIGN KEY (dex_key_integer) REFRENCES AbilityScore(ability_score_id), " +
				"FOREIGN KEY (con_key_integer) REFRENCES AbilityScore(ability_score_id), " +
				"FOREIGN KEY (int_key_integer) REFRENCES AbilityScore(ability_score_id), " +
				"FOREIGN KEY (wis_key_integer) REFRENCES AbilityScore(ability_score_id), " +
				"FOREIGN KEY (cha_key_integer) REFRENCES AbilityScore(ability_score_id) " +
				")";
	}
	
	public String createAbilitySetCalc() {
		return "CREATE TABLE AbilitySetCalc (" +
				"ability_set_calc_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"base_ability_set_id INTEGER," +
				"str_post_key INTEGER, " +
				"dex_post_key INTEGER, " +
				"con_post_key INTEGER, " +
				"int_post_key INTEGER, " +
				"wis_post_key INTEGER, " +
				"cha_post_key ITNEGER, " +
				"FOREIGN KEY (base_ability_set_id) REFRENCES AbilitySet(ability_set_id), " +
				"FOREIGN KEY (str_post_key_integer) REFRENCES AbilityScore(ability_score_id), " +
				"FOREIGN KEY (dex_post_key_integer) REFRENCES AbilityScore(ability_score_id), " +
				"FOREIGN KEY (con_post_key_integer) REFRENCES AbilityScore(ability_score_id), " +
				"FOREIGN KEY (int_post_key_integer) REFRENCES AbilityScore(ability_score_id), " +
				"FOREIGN KEY (wis_post_key_integer) REFRENCES AbilityScore(ability_score_id), " +
				"FOREIGN KEY (cha_post_key_integer) REFRENCES AbilityScore(ability_score_id))";
	}
	
	public String createAbilityModSet() {
		return "CREATE TABLE AbilityModSet (" +
				"ability_mod_set_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"base_ability_set_id INTEGER, " +
				"FOREIGN KEY (base_ability_set_id) REFRENCES AbilitySet(ability_set_id)" +
				")";
	}
	
	public String createItem() {
		return "CREATE TABLE Item (" +
				"item_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"Name TEXT, " +
				"Weight REAL, " +
				"Quantity INTEGER, " +
				"IsContained INTEGER" +
				")";
	}
	
	public String createArmor() {
		return "CREATE TABLE Armor (" +
				"armor_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"base_item_id INTEGER," +
				"Worn INTEGER," +
				"ACBonus INTEGER, " +
				"CheckPen INTEGER, " +
				"MaxDex INTEGER, " +
				"SpellFail INTEGER," +
				"Speed INTEGER, " +
				"SpecialProperties TEXT," +
				"Size TEXT, " +
				"FOREIGN KEY (base_item_id) REFRENCES Item(item_id))";
	}
	
	public String createWeapon() {
		return "CREATE TABLE Weapon (" +
				"weapon_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"base_item_id INTEGER," +
				"TotalAttackBonus INTEGER, " +
				"Attack TEXT, " +
				"Damage TEXT, " +
				"Critical TEXT, " +
				"Range INTEGER, " +
				"SpecialProperties TEXT, " +
				"Ammunition INTEGER, " +
				"Type TEXT, " +
				"Size Text," +
				"FOREIGN KEY (base_item_id) REFRENCES Item(item_id))";
	}
	
	public String createPartyMember() {
		return "CREATE TABLE PartyMember (" +
				"party_member_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"Name TEXT, " +
				"Initiative INTEGER, " +
				"AC INTEGER, " +
				"Touch INTEGER, " +
				"FlatFooted INTEGER, " +
				"SpellResist INTEGER, " +
				"DamageReduction INTEGER, " +
				"CMD INTEGER, " +
				"FortSave INTEGER, " +
				"ReflexSave INTEGER, " +
				"WillSave INTEGER, " +
				"BluffSkillBonus INTEGER, " +
				"PerceptionSkillBonus INTEGER, " +
				"SenseMotiveSkillBonus INTEGER, " +
				"StealthSkillBonus INTEGER, " +
				"RolledValue INTEGER);";
	}
	
	public String createParty() {
		return "CREATE TABLE Party (" +
				"party_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"party_member_ids TEXT," +
				"Name TEXT)";
	}
}
