package com.lateensoft.pathfinder.toolkit.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class PTTableCreator {
	private static final String TAG = PTTableCreator.class.getSimpleName();
	
	public PTTableCreator() {
	}
	
	public void createTables(SQLiteDatabase db) {
		db.execSQL(createCharacter());
		db.execSQL(createSkill());
		db.execSQL(createSave());
		db.execSQL(createCombatStatSet());
		db.execSQL(createAbilityScore());
		db.execSQL(createItem());
		db.execSQL(createArmor());
		db.execSQL(createWeapon());
		db.execSQL(createPartyMember());
		db.execSQL(createParty());
		db.execSQL(createSpell());
		db.execSQL(createSpellBook());
		db.execSQL(createFeat());
		db.execSQL(createFluffInfo());
	}
	
	public String createSkill() {
		Log.d(TAG, "Creating skill table");
		return "CREATE TABLE Skill (" +
				"skill_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"character_id INTEGER, " +
				"Name TEXT, " +
				"ClassSkill INTEGER, " +
				"KeyAbility TEXT, " +
				"AbilityMod INTEGER, " +
				"Rank INTEGER, " +
				"MiscMod INTEGER, " +
				"ArmorCheckPenalty INTEGER, " +
				"KeyAbilityKey INTEGER, " +
				"FOREIGN KEY (character_id) REFERENCES Character(character_id)" +
				");";
	}
	
	public String createSave() {
		Log.d(TAG, "Creating save table");
		return "CREATE TABLE Save (" +
				"save_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"character_id INTEGER, " +
				"Name TEXT, " +
				"BaseValue INTEGER, " +
				"Total INTEGER, " +
				"AbilityMod INTEGER, " +
				"MagicMod INTEGER, " +
				"MiscMod INTEGER, " +
				"TempMod INTEGER, " +
				"FOREIGN KEY(character_id) REFERENCES Character(character_id)" +
				");";
	}
	
	public String createCombatStatSet() {
		Log.d(TAG, "Creating combat skill set table");
		return "CREATE TABLE CombatStatSet (" +
				"combat_stat_set_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"character_id INTEGER, " +
				"TotalHP INTEGER, " +
				"Wounds INTEGER, " +
				"NonLethalDamange INTEGER, " +
				"DmageReduction INTEGER, " +
				"BaseSpeedFt INTEGER, " +
				"InitDexMod INTEGER, " +
				"InitMiscMod INTEGER, " +
				"ACArmor INTEGER, " +
				"ACShield INTEGER, " +
				"ACDexMod INTEGER," +
				"SizeMod INTEGER, " +
				"ACNaturalArmor, INTEGER, " +
				"DeflectionMod INTEGER, " +
				"ACMiscMod INTEGER, " +
				"BABPrimary INTEGER, " +
				"BABSecondary INTEGER, " +
				"StrengthMod INTEGER, " +
				"CMDDexMod INTEGER, " +
				"CMDMiscMod INTEGER, " +
				"SpellResist INTEGER, " +
				"FOREIGN KEY (character_id) REFERENCES Character(character_id)" +
				");";
	}
	
	// TODO: add something for set calc? Do we store that?
	public String createAbilityScore() {
		Log.d(TAG, "Creating ability score table");
		return "CREATE TABLE AbilityScore (" +
				"ability_score_id INTEGER PRIMARY KEY AUTOINCREMENT," +
				"character_id INTEGER,  " +
				"Ability TEXT, " +
				"Score INTEGER, " +
				"FOREIGN KEY (character_id) REFERENCES Character(character_id)" +
				");";
	}
	
	public String createItem() {
		Log.d(TAG, "Creating item table");
		return "CREATE TABLE Item (" +
				"item_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"character_id INTEGER, " +
				"Name TEXT, " +
				"Weight REAL, " +
				"Quantity INTEGER, " +
				"IsContained INTEGER, " +
				"FOREIGN KEY (character_id) REFERENCES Character(character_id)" +
				");";
	}
	
	public String createArmor() {
		Log.d(TAG, "Creating armor table");
		return "CREATE TABLE Armor (" +
				"armor_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"character_id INTEGER, " +
				"Name TEXT, " +
				"Weight REAL, " +
				"Quantity INTEGER, " +
				"IsContained INTEGER, " +
				"Worn INTEGER," +
				"ACBonus INTEGER, " +
				"CheckPen INTEGER, " +
				"MaxDex INTEGER, " +
				"SpellFail INTEGER," +
				"Speed INTEGER, " +
				"SpecialProperties TEXT," +
				"Size TEXT, " +
				"FOREIGN KEY (character_id) REFERENCES Character(character_id)" +
				");";
	}
	
	public String createWeapon() {
		Log.d(TAG, "Creating weapon table");
		return "CREATE TABLE Weapon (" +
				"weapon_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"character_id INTEGER, " +
				"Name TEXT, " +
				"Weight REAL, " +
				"Quantity INTEGER, " +
				"IsContained INTEGER, " +
				"TotalAttackBonus INTEGER, " +
				"Attack TEXT, " +
				"Damage TEXT, " +
				"Critical TEXT, " +
				"Range INTEGER, " +
				"SpecialProperties TEXT, " +
				"Ammunition INTEGER, " +
				"Type TEXT, " +
				"Size Text, " +
				"FOREIGN KEY (character_id) REFERENCES Character(character_id)" +
				");";
	}
	
	public String createPartyMember() {
		Log.d(TAG, "Creating party member table");
		return "CREATE TABLE PartyMember (" +
				"party_member_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"party_id INTEGER, " +
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
				"RolledValue INTEGER, " +
				"FOREIGN KEY (party_id) REFERENCES Party(party_id)" +
				");";
	}
	
	public String createParty() {
		Log.d(TAG, "Creating party table");
		return "CREATE TABLE Party (" +
				"party_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"Name TEXT" +
				");";
	}
	
	public String createSpell() {
		Log.d(TAG, "Creating spell table");
		return "CREATE TABLE Spell (" +
				"spell_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"character_id INTEGER, " +
				"Name TEXT," +
				"Prepared INTEGER," +
				"Level INTEGER," +
				"Description TEXT, " +
				"FOREIGN KEY (character_id) REFERENCES Character(character_id)" +
				");";
	}
	
	public String createSpellBook() {
		Log.d(TAG, "Creating spellbook table");
		return "CREATE TABLE SpellBook (" +
				"spell_book_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"character_id INTEGER, " +
				"SpellCount TEXT, " +
				"FOREIGN KEY (character_id) REFERENCES Character(character_id)" +
				");";
	}
	
	public String createFeat() {
		Log.d(TAG, "Creating feat table");
		return "CREATE TABLE Feat (" +
				"feat_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"character_id INTEGER, " +
				"Name TEXT, " +
				"Description TEXT, " +
				"FOREIGN KEY (character_id) REFERENCES Character(character_id)" +
				");";
	}
	
	public String createFluffInfo() {
		Log.d(TAG, "Creating fluff info table");
		return "CREATE TABLE FluffInfo (" +
				"fluff_info_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"character_id INTEGER, " +
				"Name TEXT, " +
				"Alignment TEXT, " +
				"XP TEXT, " +
				"NextLevelXP TEXT, " +
				"XPChange TEXT, " +
				"PlayerClass TEXT, " +
				"Race TEXT, " +
				"Deity TEXT, " +
				"Level TEXT, " +
				"Size TEXT, " +
				"Gender TEXT, " +
				"Height TEXT, " +
				"Weight TEXT, " +
				"Eyes TEXT, " +
				"Hair TEXT, " +
				"Languages TEXT, " +
				"Description TEXT, " +
				"Other TEXT, " +
				"FOREIGN KEY (character_id) REFERENCES Character(character_id)" +
				");"; 
	}
	
	public String createCharacter() {
		Log.d(TAG, "Creating character table");
		return "CREATE TABLE Character (" +
				"character_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"Tag TEXT " +
				");";
	}
}
