package com.lateensoft.pathfinder.toolkit.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TableCreator {
    private static final String TAG = TableCreator.class.getSimpleName();
    
    public TableCreator() {
    }
    
    public void createTables(SQLiteDatabase db) {
        db.execSQL(createCharacter());
        db.execSQL(createSkill());
        db.execSQL(createSave());
        db.execSQL(createCombatStatSet());
        db.execSQL(createAbility());
        db.execSQL(createItem());
        db.execSQL(createArmor());
        db.execSQL(createWeapon());
        db.execSQL(createSpell());
        db.execSQL(createFeat());
        db.execSQL(createFluffInfo());

        db.execSQL(createPartyMember());
        db.execSQL(createParty());
        db.execSQL(createEncounter());
        db.execSQL(createEncounterParticipant());
        db.execSQL(createPartyMembership());
    }
    
    public String createSkill() {
        Log.d(TAG, "Creating skill table");
        return "CREATE TABLE Skill (" +
                "skill_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "skill_key INTEGER, " +
                "character_id INTEGER, " +
                "SubType TEXT, " +
                "ClassSkill INTEGER, " +
                "Rank INTEGER, " +
                "MiscMod INTEGER, " +
                "ability_key INTEGER, " +
                "FOREIGN KEY (character_id) REFERENCES Character(character_id) " +
                    "ON DELETE CASCADE " +
                ");";
    }
    
    public String createSave() {
        Log.d(TAG, "Creating save table");
        return "CREATE TABLE Save (" +
                "save_key INTEGER, " +
                "character_id INTEGER, " +
                "BaseValue INTEGER, " +
                "ability_key INTEGER, " +
                "MagicMod INTEGER, " +
                "MiscMod INTEGER, " +
                "TempMod INTEGER, " +
                "PRIMARY KEY (save_key, character_id) " +
                "FOREIGN KEY(character_id) REFERENCES Character(character_id) " +
                    "ON DELETE CASCADE " +
                ");";
    }
    
    public String createCombatStatSet() {
        Log.d(TAG, "Creating combat skill set table");
        return "CREATE TABLE CombatStatSet (" +
                "character_id INTEGER PRIMARY KEY, " +
                "TotalHP INTEGER, " +
                "Wounds INTEGER, " +
                "NonLethalDamage INTEGER, " +
                "DamageReduction INTEGER, " +
                "BaseSpeedFt INTEGER, " +
                "InitAbilityKey INTEGER, " +
                "InitMiscMod INTEGER, " +
                "ACArmor INTEGER, " +
                "ACShield INTEGER, " +
                "ACAbilityKey INTEGER," +
                "SizeMod INTEGER, " +
                "ACNaturalArmor, INTEGER, " +
                "DeflectionMod INTEGER, " +
                "ACMiscMod INTEGER, " +
                "BABPrimary INTEGER, " +
                "BABSecondary TEXT, " +
                "CMBAbilityKey INTEGER, " +
                "CMDAbilityKey INTEGER, " +
                "CMDMiscMod INTEGER, " +
                "SpellResist INTEGER, " +
                "FOREIGN KEY (character_id) REFERENCES Character(character_id) " +
                    "ON DELETE CASCADE " +
                ");";
    }
    
    public String createAbility() {
        Log.d(TAG, "Creating ability table");
        return "CREATE TABLE Ability (" +
                "ability_key INTEGER," +
                "character_id INTEGER,  " +
                "Score INTEGER, " +
                "Temp INTEGER, " +
                "PRIMARY KEY (ability_key, character_id) " +
                "FOREIGN KEY (character_id) REFERENCES Character(character_id) " +
                    "ON DELETE CASCADE " +
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
                "FOREIGN KEY (character_id) REFERENCES Character(character_id) " +
                    "ON DELETE CASCADE " +
                ");";
    }
    
    public String createArmor() {
        Log.d(TAG, "Creating armor table");
        return "CREATE TABLE Armor (" +
                "item_id INTEGER PRIMARY KEY, " +
                "Worn INTEGER," +
                "ACBonus INTEGER, " +
                "CheckPen INTEGER, " +
                "MaxDex INTEGER, " +
                "SpellFail INTEGER," +
                "Speed INTEGER, " +
                "SpecialProperties TEXT," +
                "Size TEXT, " +
                "FOREIGN KEY (item_id) REFERENCES Item(item_id) " +
                    "ON DELETE CASCADE " +
                ");";
    }
    
    public String createWeapon() {
        Log.d(TAG, "Creating weapon table");
        return "CREATE TABLE Weapon (" +
                "item_id INTEGER PRIMARY KEY, " +
                "TotalAttackBonus INTEGER, " +
                "Damage TEXT, " +
                "Critical TEXT, " +
                "Range INTEGER, " +
                "SpecialProperties TEXT, " +
                "Ammunition INTEGER, " +
                "Type TEXT, " +
                "Size TEXT, " +
                "FOREIGN KEY (item_id) REFERENCES Item(item_id) " +
                    "ON DELETE CASCADE " +
                ");";
    }

    // TODO remove this
    @Deprecated
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
                "DisguiseSkillBonus INTEGER, " +
                "PerceptionSkillBonus INTEGER, " +
                "SenseMotiveSkillBonus INTEGER, " +
                "StealthSkillBonus INTEGER, " +
                "RolledValue INTEGER, " +
                "FOREIGN KEY (party_id) REFERENCES Party(party_id) " +
                    "ON DELETE CASCADE " +
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
                "FOREIGN KEY (character_id) REFERENCES Character(character_id) " +
                    "ON DELETE CASCADE " +
                ");";
    }
    
    public String createFeat() {
        Log.d(TAG, "Creating feat table");
        return "CREATE TABLE Feat (" +
                "feat_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "character_id INTEGER, " +
                "Name TEXT, " +
                "Description TEXT, " +
                "FOREIGN KEY (character_id) REFERENCES Character(character_id) " +
                    "ON DELETE CASCADE " +
                ");";
    }
    
    public String createFluffInfo() {
        Log.d(TAG, "Creating fluff info table");
        return "CREATE TABLE FluffInfo (" +
                "character_id INTEGER PRIMARY KEY, " +
                "Alignment TEXT, " +
                "XP TEXT, " +
                "NextLevelXP TEXT, " +
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
                "FOREIGN KEY (character_id) REFERENCES Character(character_id) " +
                    "ON DELETE CASCADE " +
                ");"; 
    }
    
    public String createCharacter() {
        Log.d(TAG, "Creating character table");
        return "CREATE TABLE Character (" +
                "character_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Name TEXT, " +
                "Gold REAL " +
                ");";
    }

    public String createParty() {
        Log.d(TAG, "Creating party table");
        return "CREATE TABLE Party (" +
                "party_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Name TEXT " +
                ");";
    }

    public String createPartyMembership() {
        Log.d(TAG, "Creating party membership table");
        return "CREATE TABLE PartyMembership (" +
                "party_id INTEGER, " +
                "character_id INTEGER, "  +
                "PRIMARY KEY (party_id, character_id) " +
                "FOREIGN KEY (character_id) REFERENCES Character(character_id) " +
                "ON DELETE CASCADE, " +
                "FOREIGN KEY (party_id) REFERENCES Party(party_id) " +
                "ON DELETE CASCADE " +
                ");";
    }

    public String createEncounter() {
        Log.d(TAG, "Creating encounter table");
        return "CREATE TABLE Encounter (" +
                "encounter_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Name TEXT " +
                ");";
    }

    public String createEncounterParticipant() {
        Log.d(TAG, "Creating encounter participant table");
        return "CREATE TABLE EncounterParticipant (" +
                "encounter_id INTEGER, " +
                "character_id INTEGER, "  +
                "InitiativeScore INTEGER, " +
                "TurnOrder INTEGER, " +
                "PRIMARY KEY (encounter_id, character_id) " +
                "FOREIGN KEY (character_id) REFERENCES Character(character_id) " +
                "ON DELETE CASCADE, " +
                "FOREIGN KEY (encounter_id) REFERENCES Encounter(encounter_id) " +
                "ON DELETE CASCADE " +
                ");";
    }
}
