package com.lateensoft.pathfinder.toolkit.db.repository;

import java.util.Hashtable;

import android.content.ContentValues;

import com.lateensoft.pathfinder.toolkit.db.repository.PTTableAttribute.SQLDataType;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTCombatStatSet;

public class PTCombatStatRepository extends PTBaseRepository<PTCombatStatSet> {
	private static final String TABLE = "CombatStatSet";
	private static final String TOTAL_HP = "TotalHP";
	private static final String WOUNDS = "Wounds";
	private static final String NON_LETHAL_DAMAGE = "NonLethalDamage";
	private static final String DAMAGE_REDUCTION = "DamageReduction";
	private static final String BASE_SPEED_FT = "BaseSpeedFt";
	private static final String INIT_ABILITY_KEY = "InitAbilityKey";
	private static final String INIT_MISC_MOD = "InitMiscMod";
	private static final String AC_ARMOR = "ACArmor";
	private static final String AC_SHIELD = "ACShield";
	private static final String AC_ABILITY_KEY = "ACAbilityKey";
	private static final String SIZE_MOD = "SizeMod";
	private static final String AC_NATURAL_ARMOR = "ACNaturalArmor";
	private static final String DEFLECTION_MOD = "DeflectionMod";
	private static final String AC_MISC_MOD = "ACMiscMod";
	private static final String BAB_PRIMARY = "BABPrimary";
	private static final String BAB_SECONDARY = "BABSecondary";
	private static final String CMB_ABILITY_KEY = "CMBAbilityKey";
	private static final String CMD_ABILITY_KEY = "CMDAbilityKey";
	private static final String CMD_MISC_MOD = "CMDMiscMod";
	private static final String SPELL_RESIST = "SpellResist";
	
	public PTCombatStatRepository() {
		super();
		PTTableAttribute characterId = new PTTableAttribute(CHARACTER_ID, SQLDataType.INTEGER, true);
		PTTableAttribute totalHP = new PTTableAttribute(TOTAL_HP, SQLDataType.INTEGER);
		PTTableAttribute wounds = new PTTableAttribute(WOUNDS, SQLDataType.INTEGER);
		PTTableAttribute nonLethalDamage = new PTTableAttribute(NON_LETHAL_DAMAGE, SQLDataType.INTEGER);
		PTTableAttribute damageReduction = new PTTableAttribute(DAMAGE_REDUCTION, SQLDataType.INTEGER);
		PTTableAttribute baseSpeedFt = new PTTableAttribute(BASE_SPEED_FT, SQLDataType.INTEGER);
		PTTableAttribute initAbilityKey = new PTTableAttribute(INIT_ABILITY_KEY, SQLDataType.INTEGER);
		PTTableAttribute initMiscMod = new PTTableAttribute(INIT_MISC_MOD, SQLDataType.INTEGER);
		PTTableAttribute acArmor = new PTTableAttribute(AC_ARMOR, SQLDataType.INTEGER);
		PTTableAttribute acShield = new PTTableAttribute(AC_SHIELD, SQLDataType.INTEGER);
		PTTableAttribute acAbilityKey = new PTTableAttribute(AC_ABILITY_KEY, SQLDataType.INTEGER);
		PTTableAttribute sizeMod = new PTTableAttribute(SIZE_MOD, SQLDataType.INTEGER);
		PTTableAttribute acNaturalArmor = new PTTableAttribute(AC_NATURAL_ARMOR, SQLDataType.INTEGER);
		PTTableAttribute deflectionMod = new PTTableAttribute(DEFLECTION_MOD, SQLDataType.INTEGER);
		PTTableAttribute acMiscMod = new PTTableAttribute(AC_MISC_MOD, SQLDataType.INTEGER);
		PTTableAttribute babPrimary = new PTTableAttribute(BAB_PRIMARY, SQLDataType.INTEGER);
		PTTableAttribute babSecondary = new PTTableAttribute(BAB_SECONDARY, SQLDataType.TEXT);
		PTTableAttribute cmbAbilityKey = new PTTableAttribute(CMB_ABILITY_KEY, SQLDataType.INTEGER);
		PTTableAttribute cmdAbilityKey = new PTTableAttribute(CMD_ABILITY_KEY, SQLDataType.INTEGER);
		PTTableAttribute cmdMiscMod = new PTTableAttribute(CMD_MISC_MOD, SQLDataType.INTEGER);
		PTTableAttribute spellResist = new PTTableAttribute(SPELL_RESIST, SQLDataType.INTEGER);
		PTTableAttribute[] columns = {characterId, totalHP, wounds, nonLethalDamage, 
				damageReduction, baseSpeedFt, initAbilityKey, initMiscMod, acArmor, acShield, 
				acAbilityKey, sizeMod, acNaturalArmor, deflectionMod, acMiscMod, babPrimary,
				babSecondary, cmbAbilityKey, cmdAbilityKey, cmdMiscMod, spellResist};
		m_tableInfo = new PTTableInfo(TABLE, columns);
	}
	
	@Override
	protected PTCombatStatSet buildFromHashTable(
			Hashtable<String, Object> hashTable) {
		long characterId = (Long) hashTable.get(CHARACTER_ID);
		int totalHP = ((Long) hashTable.get(TOTAL_HP)).intValue();
		int wounds = ((Long) hashTable.get(WOUNDS)).intValue();
		int nonLethalDamage = ((Long) hashTable.get(NON_LETHAL_DAMAGE)).intValue();
		int damageReduction = ((Long) hashTable.get(DAMAGE_REDUCTION)).intValue();
		int baseSpeedFt = ((Long) hashTable.get(BASE_SPEED_FT)).intValue();
		int initAbilityKey = ((Long) hashTable.get(INIT_ABILITY_KEY)).intValue();
		int initMiscMod = ((Long) hashTable.get(INIT_MISC_MOD)).intValue();
		int acArmor = ((Long) hashTable.get(AC_ARMOR)).intValue();
		int acShield = ((Long) hashTable.get(AC_SHIELD)).intValue();
		int acAbilityKey = ((Long) hashTable.get(AC_ABILITY_KEY)).intValue();
		int sizeMod = ((Long) hashTable.get(SIZE_MOD)).intValue();
		int acNaturalArmor = ((Long) hashTable.get(AC_NATURAL_ARMOR)).intValue();
		int deflectionMod = ((Long) hashTable.get(DEFLECTION_MOD)).intValue();
		int acMiscMod = ((Long) hashTable.get(AC_MISC_MOD)).intValue();
		int babPrimary = ((Long) hashTable.get(BAB_PRIMARY)).intValue();
		String babSecondary = (String) hashTable.get(BAB_SECONDARY);
		int cmbAbilityKey = ((Long) hashTable.get(CMB_ABILITY_KEY)).intValue();
		int cmdAbilityKey = ((Long) hashTable.get(CMD_ABILITY_KEY)).intValue();
		int cmdMiscMod = ((Long) hashTable.get(CMD_MISC_MOD)).intValue();
		int spellResist = ((Long) hashTable.get(SPELL_RESIST)).intValue();
		
		PTCombatStatSet statSet = new PTCombatStatSet(characterId);
		statSet.setTotalHP(totalHP);
		statSet.setWounds(wounds);
		statSet.setNonLethalDamage(nonLethalDamage);
		statSet.setDamageReduction(damageReduction);
		statSet.setBaseSpeed(baseSpeedFt);
		statSet.setInitAbilityKey(initAbilityKey);
		statSet.setInitiativeMiscMod(initMiscMod);
		statSet.setACArmourBonus(acArmor);
		statSet.setACShieldBonus(acShield);
		statSet.setACAbilityKey(acAbilityKey);
		statSet.setSizeModifier(sizeMod);
		statSet.setNaturalArmour(acNaturalArmor);
		statSet.setDeflectionMod(deflectionMod);
		statSet.setACMiscMod(acMiscMod);
		statSet.setBABPrimary(babPrimary);
		statSet.setBABSecondary(babSecondary);
		statSet.setCMBAbilityKey(cmbAbilityKey);
		statSet.setCMDAbilityKey(cmdAbilityKey);
		statSet.setCMDMiscMod(cmdMiscMod);
		statSet.setSpellResistance(spellResist);
		return statSet;
	}
	@Override
	protected ContentValues getContentValues(PTCombatStatSet object) {
		ContentValues values = new ContentValues();
		values.put(CHARACTER_ID, object.getID());
		values.put(TOTAL_HP, object.getTotalHP());
		values.put(WOUNDS, object.getWounds());
		values.put(NON_LETHAL_DAMAGE, object.getNonLethalDamage());
		values.put(DAMAGE_REDUCTION, object.getDamageReduction());
		values.put(BASE_SPEED_FT, object.getBaseSpeed());
		values.put(INIT_ABILITY_KEY, object.getInitAbilityKey());
		values.put(INIT_MISC_MOD, object.getInitiativeMiscMod());
		values.put(AC_ARMOR, object.getACArmourBonus());
		values.put(AC_SHIELD, object.getACShieldBonus());
		values.put(AC_ABILITY_KEY, object.getACAbilityKey());
		values.put(SIZE_MOD, object.getSizeModifier());
		values.put(AC_NATURAL_ARMOR, object.getNaturalArmour());
		values.put(DEFLECTION_MOD, object.getDeflectionMod());
		values.put(AC_MISC_MOD, object.getACMiscMod());
		values.put(BAB_PRIMARY, object.getBABPrimary());
		values.put(BAB_SECONDARY, object.getBABSecondary());
		values.put(CMB_ABILITY_KEY, object.getCMBAbilityKey());
		values.put(CMD_ABILITY_KEY, object.getCMDAbilityKey());
		values.put(CMD_MISC_MOD, object.getCMDMiscMod());
		values.put(SPELL_RESIST, object.getSpellResist());
		return values;
	}
}
