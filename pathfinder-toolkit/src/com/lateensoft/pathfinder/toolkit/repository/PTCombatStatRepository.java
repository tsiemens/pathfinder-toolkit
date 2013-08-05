package com.lateensoft.pathfinder.toolkit.repository;

import java.util.Hashtable;

import android.content.ContentValues;

import com.lateensoft.pathfinder.toolkit.repository.PTTableAttribute.SQLDataType;
import com.lateensoft.pathfinder.toolkit.stats.PTCombatStatSet;

public class PTCombatStatRepository extends PTBaseRepository<PTCombatStatSet> {
	private static final String TABLE = "CombatStatSet";
	private static final String ID = "combat_stat_set_id"; 
	private static final String TOTAL_HP = "TotalHP";
	private static final String WOUNDS = "Wounds";
	private static final String NON_LETHAL_DAMAGE = "NonLethalDamage";
	private static final String DAMAGE_REDUCTION = "DamageReduction";
	private static final String BASE_SPEED_FT = "BaseSpeeedFt";
	private static final String INIT_DEX_MOD = "InitDexMod";
	private static final String INIT_MISC_MOD = "InitMiscMod";
	private static final String AC_ARMOR = "ACArmor";
	private static final String AC_SHIELD = "ACShield";
	private static final String AC_DEX_MOD = "ACDexMod";
	private static final String SIZE_MOD = "SizeMod";
	private static final String AC_NATURAL_ARMOR = "ACNaturalArmor";
	private static final String DEFLECTION_MOD = "DeflectionMod";
	private static final String AC_MISC_MOD = "ACMiscMod";
	private static final String BAB_PRIMARY = "BABPrimary";
	private static final String BAB_SECONDARY = "BABSecondary";
	private static final String STRENGTH_MOD = "StrengthMod";
	private static final String CMD_DEX_MOD = "CMDDexMod";
	private static final String CMD_MISC_MOD = "CMDMiscMod";
	private static final String SPELL_RESIST = "SpellResist";
	
	public PTCombatStatRepository() {
		super();
		PTTableAttribute id = new PTTableAttribute(ID, SQLDataType.INTEGER, true);
		PTTableAttribute characterId = new PTTableAttribute(CHARACTER_ID, SQLDataType.INTEGER);
		PTTableAttribute totalHP = new PTTableAttribute(TOTAL_HP, SQLDataType.INTEGER);
		PTTableAttribute wounds = new PTTableAttribute(WOUNDS, SQLDataType.INTEGER);
		PTTableAttribute nonLethalDamage = new PTTableAttribute(NON_LETHAL_DAMAGE, SQLDataType.INTEGER);
		PTTableAttribute damageReduction = new PTTableAttribute(DAMAGE_REDUCTION, SQLDataType.INTEGER);
		PTTableAttribute baseSpeedFt = new PTTableAttribute(BASE_SPEED_FT, SQLDataType.INTEGER);
		PTTableAttribute initDexMod = new PTTableAttribute(INIT_DEX_MOD, SQLDataType.INTEGER);
		PTTableAttribute initMiscMod = new PTTableAttribute(INIT_MISC_MOD, SQLDataType.INTEGER);
		PTTableAttribute acArmor = new PTTableAttribute(AC_ARMOR, SQLDataType.INTEGER);
		PTTableAttribute acShield = new PTTableAttribute(AC_SHIELD, SQLDataType.INTEGER);
		PTTableAttribute acDexMod = new PTTableAttribute(AC_DEX_MOD, SQLDataType.INTEGER);
		PTTableAttribute sizeMod = new PTTableAttribute(SIZE_MOD, SQLDataType.INTEGER);
		PTTableAttribute acNaturalArmor = new PTTableAttribute(AC_NATURAL_ARMOR, SQLDataType.INTEGER);
		PTTableAttribute deflectionMod = new PTTableAttribute(DEFLECTION_MOD, SQLDataType.INTEGER);
		PTTableAttribute acMiscMod = new PTTableAttribute(AC_MISC_MOD, SQLDataType.INTEGER);
		PTTableAttribute babPrimary = new PTTableAttribute(BAB_PRIMARY, SQLDataType.INTEGER);
		PTTableAttribute babSecondary = new PTTableAttribute(BAB_SECONDARY, SQLDataType.TEXT);
		PTTableAttribute strengthMod = new PTTableAttribute(STRENGTH_MOD, SQLDataType.INTEGER);
		PTTableAttribute cmdDexMod = new PTTableAttribute(CMD_DEX_MOD, SQLDataType.INTEGER);
		PTTableAttribute cmdMiscMod = new PTTableAttribute(CMD_MISC_MOD, SQLDataType.INTEGER);
		PTTableAttribute spellResist = new PTTableAttribute(SPELL_RESIST, SQLDataType.INTEGER);
		PTTableAttribute[] columns = {id, characterId, totalHP, wounds, nonLethalDamage, 
				damageReduction, baseSpeedFt, initDexMod, initMiscMod, acArmor, acShield, 
				acDexMod, sizeMod, acNaturalArmor, deflectionMod, acMiscMod, babPrimary,
				babSecondary, strengthMod, cmdDexMod, cmdMiscMod, spellResist};
		mTableInfo = new PTTableInfo(TABLE, columns);
	}
	
	@Override
	protected PTCombatStatSet buildFromHashTable(
			Hashtable<String, Object> hashTable) {
		long id = (Long) hashTable.get(ID);
		long characterId = (Long) hashTable.get(CHARACTER_ID);
		int totalHP = (Integer) hashTable.get(TOTAL_HP);
		int wounds = (Integer) hashTable.get(WOUNDS);
		int nonLethalDamage = (Integer) hashTable.get(NON_LETHAL_DAMAGE);
		int damageReduction = (Integer) hashTable.get(DAMAGE_REDUCTION);
		int baseSpeedFt = (Integer) hashTable.get(BASE_SPEED_FT);
		int initDexMod = (Integer) hashTable.get(INIT_DEX_MOD);
		int initMiscMod = (Integer) hashTable.get(INIT_MISC_MOD);
		int acArmor = (Integer) hashTable.get(AC_ARMOR);
		int acShield = (Integer) hashTable.get(AC_SHIELD);
		int acDexMod = (Integer) hashTable.get(AC_DEX_MOD);
		int sizeMod = (Integer) hashTable.get(SIZE_MOD);
		int acNaturalArmor = (Integer) hashTable.get(AC_NATURAL_ARMOR);
		int deflectionMod = (Integer) hashTable.get(DEFLECTION_MOD);
		int acMiscMod = (Integer) hashTable.get(AC_MISC_MOD);
		int babPrimary = (Integer) hashTable.get(BAB_PRIMARY);
		String babSecondary = (String) hashTable.get(BAB_SECONDARY);
		int strengthMod = (Integer) hashTable.get(STRENGTH_MOD);
		int cmdDexMod = (Integer) hashTable.get(CMD_DEX_MOD);
		int cmdMiscMod = (Integer) hashTable.get(CMD_MISC_MOD);
		int spellResist = (Integer) hashTable.get(SPELL_RESIST);
		
		PTCombatStatSet statSet = new PTCombatStatSet(id, characterId);
		statSet.setTotalHP(totalHP);
		statSet.setWounds(wounds);
		statSet.setNonLethalDamage(nonLethalDamage);
		statSet.setDamageReduction(damageReduction);
		statSet.setBaseSpeed(baseSpeedFt);
		statSet.setInitDexMod(initDexMod);
		statSet.setInitiativeMiscMod(initMiscMod);
		statSet.setACArmourBonus(acArmor);
		statSet.setACShieldBonus(acShield);
		statSet.setACDexMod(acDexMod);
		statSet.setSizeModifier(sizeMod);
		statSet.setNaturalArmour(acNaturalArmor);
		statSet.setDeflectionMod(deflectionMod);
		statSet.setACMiscMod(acMiscMod);
		statSet.setBABPrimary(babPrimary);
		statSet.setBABSecondary(babSecondary);
		statSet.setStrengthMod(strengthMod);
		statSet.setCMDDexMod(cmdDexMod);
		statSet.setCMDMiscMod(cmdMiscMod);
		statSet.setSpellResistance(spellResist);
		return statSet;
	}
	@Override
	protected ContentValues getContentValues(PTCombatStatSet object) {
		ContentValues values = new ContentValues();
		if (!isIDSet(object)) {
			values.put(ID, object.getID());
		}
		values.put(CHARACTER_ID, object.getCharacterID());
		values.put(TOTAL_HP, object.getTotalHP());
		values.put(WOUNDS, object.getWounds());
		values.put(NON_LETHAL_DAMAGE, object.getNonLethalDamage());
		values.put(DAMAGE_REDUCTION, object.getDamageReduction());
		values.put(BASE_SPEED_FT, object.getBaseSpeed());
		values.put(INIT_DEX_MOD, object.getInitDexMod());
		values.put(INIT_MISC_MOD, object.getInitiativeMiscMod());
		values.put(AC_ARMOR, object.getACArmourBonus());
		values.put(AC_SHIELD, object.getACShieldBonus());
		values.put(AC_DEX_MOD, object.getACDexMod());
		values.put(SIZE_MOD, object.getSizeModifier());
		values.put(AC_NATURAL_ARMOR, object.getNaturalArmour());
		values.put(DEFLECTION_MOD, object.getDeflectionMod());
		values.put(AC_MISC_MOD, object.getACMiscMod());
		values.put(BAB_PRIMARY, object.getBABPrimary());
		values.put(BAB_SECONDARY, object.getBABSecondary());
		values.put(STRENGTH_MOD, object.getStrengthMod());
		values.put(CMD_DEX_MOD, object.getCMDDexMod());
		values.put(CMD_MISC_MOD, object.getCMDMiscMod());
		values.put(SPELL_RESIST, object.getSpellResist());
		return values;
	}
}
