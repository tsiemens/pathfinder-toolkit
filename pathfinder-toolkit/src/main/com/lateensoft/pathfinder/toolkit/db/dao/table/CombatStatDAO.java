package com.lateensoft.pathfinder.toolkit.db.dao.table;

import android.content.ContentValues;
import android.content.Context;
import com.lateensoft.pathfinder.toolkit.db.dao.OwnedObject;
import com.lateensoft.pathfinder.toolkit.db.dao.OwnedWeakTableDAO;
import com.lateensoft.pathfinder.toolkit.db.dao.Table;
import com.lateensoft.pathfinder.toolkit.model.character.stats.AbilityType;
import com.lateensoft.pathfinder.toolkit.model.character.stats.CombatStatSet;

import java.util.Hashtable;

public class CombatStatDAO extends OwnedWeakTableDAO<Long, Void, CombatStatSet> {
    private static final String TABLE = "CombatStatSet";

    private static final String CHARACTER_ID = "character_id";
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

    public CombatStatDAO(Context context) {
        super(context);
    }

    @Override
    protected Table initTable() {
        return new Table(TABLE,
                CHARACTER_ID, TOTAL_HP, WOUNDS, NON_LETHAL_DAMAGE, DAMAGE_REDUCTION, BASE_SPEED_FT,
                INIT_ABILITY_KEY, INIT_MISC_MOD, AC_ARMOR, AC_SHIELD, AC_ABILITY_KEY, SIZE_MOD,
                AC_NATURAL_ARMOR, DEFLECTION_MOD, AC_MISC_MOD, BAB_PRIMARY, BAB_SECONDARY,
                CMB_ABILITY_KEY, CMD_ABILITY_KEY, CMD_MISC_MOD, SPELL_RESIST
        );
    }

    @Override
    protected String getOwnerIdSelector(Long characterId) {
        return CHARACTER_ID + "=" + characterId;
    }

    @Override
    protected String getIdSelector(OwnedObject<Long, Void> rowId) {
        return getOwnerIdSelector(rowId.getOwnerId());
    }

    @Override
    protected OwnedObject<Long, Void> getIdFromRowData(OwnedObject<Long, CombatStatSet> rowData) {
        return new OwnedObject<Long, Void>(rowData.getOwnerId(), null);
    }

    @Override
    protected ContentValues getContentValues(OwnedObject<Long, CombatStatSet> rowData) {
        CombatStatSet object = rowData.getObject();
        ContentValues values = new ContentValues();
        values.put(CHARACTER_ID, rowData.getOwnerId());
        values.put(TOTAL_HP, object.getTotalHP());
        values.put(WOUNDS, object.getWounds());
        values.put(NON_LETHAL_DAMAGE, object.getNonLethalDamage());
        values.put(DAMAGE_REDUCTION, object.getDamageReduction());
        values.put(BASE_SPEED_FT, object.getBaseSpeed());
        values.put(INIT_ABILITY_KEY, object.getInitAbility().getKey());
        values.put(INIT_MISC_MOD, object.getInitiativeMiscMod());
        values.put(AC_ARMOR, object.getACArmourBonus());
        values.put(AC_SHIELD, object.getACShieldBonus());
        values.put(AC_ABILITY_KEY, object.getACAbility().getKey());
        values.put(SIZE_MOD, object.getSizeModifier());
        values.put(AC_NATURAL_ARMOR, object.getNaturalArmour());
        values.put(DEFLECTION_MOD, object.getDeflectionMod());
        values.put(AC_MISC_MOD, object.getACMiscMod());
        values.put(BAB_PRIMARY, object.getBABPrimary());
        values.put(BAB_SECONDARY, object.getBABSecondary());
        values.put(CMB_ABILITY_KEY, object.getCMBAbility().getKey());
        values.put(CMD_ABILITY_KEY, object.getCMDAbility().getKey());
        values.put(CMD_MISC_MOD, object.getCMDMiscMod());
        values.put(SPELL_RESIST, object.getSpellResist());
        return values;
    }

    @Override
    protected CombatStatSet buildFromHashTable(Hashtable<String, Object> hashTable) {
        long characterId = (Long) hashTable.get(CHARACTER_ID);
        int totalHP = ((Long) hashTable.get(TOTAL_HP)).intValue();
        int wounds = ((Long) hashTable.get(WOUNDS)).intValue();
        int nonLethalDamage = ((Long) hashTable.get(NON_LETHAL_DAMAGE)).intValue();
        int damageReduction = ((Long) hashTable.get(DAMAGE_REDUCTION)).intValue();
        int baseSpeedFt = ((Long) hashTable.get(BASE_SPEED_FT)).intValue();
        AbilityType initAbilityKey = AbilityType.forKey(((Long) hashTable.get(INIT_ABILITY_KEY)).intValue());
        int initMiscMod = ((Long) hashTable.get(INIT_MISC_MOD)).intValue();
        int acArmor = ((Long) hashTable.get(AC_ARMOR)).intValue();
        int acShield = ((Long) hashTable.get(AC_SHIELD)).intValue();
        AbilityType acAbilityKey = AbilityType.forKey(((Long) hashTable.get(AC_ABILITY_KEY)).intValue());
        int sizeMod = ((Long) hashTable.get(SIZE_MOD)).intValue();
        int acNaturalArmor = ((Long) hashTable.get(AC_NATURAL_ARMOR)).intValue();
        int deflectionMod = ((Long) hashTable.get(DEFLECTION_MOD)).intValue();
        int acMiscMod = ((Long) hashTable.get(AC_MISC_MOD)).intValue();
        int babPrimary = ((Long) hashTable.get(BAB_PRIMARY)).intValue();
        String babSecondary = (String) hashTable.get(BAB_SECONDARY);
        AbilityType cmbAbilityKey = AbilityType.forKey(((Long) hashTable.get(CMB_ABILITY_KEY)).intValue());
        AbilityType cmdAbilityKey = AbilityType.forKey(((Long) hashTable.get(CMD_ABILITY_KEY)).intValue());
        int cmdMiscMod = ((Long) hashTable.get(CMD_MISC_MOD)).intValue();
        int spellResist = ((Long) hashTable.get(SPELL_RESIST)).intValue();

        CombatStatSet statSet = new CombatStatSet(characterId);
        statSet.setTotalHP(totalHP);
        statSet.setWounds(wounds);
        statSet.setNonLethalDamage(nonLethalDamage);
        statSet.setDamageReduction(damageReduction);
        statSet.setBaseSpeed(baseSpeedFt);
        statSet.setInitAbility(initAbilityKey);
        statSet.setInitiativeMiscMod(initMiscMod);
        statSet.setACArmourBonus(acArmor);
        statSet.setACShieldBonus(acShield);
        statSet.setACAbility(acAbilityKey);
        statSet.setSizeModifier(sizeMod);
        statSet.setNaturalArmour(acNaturalArmor);
        statSet.setDeflectionMod(deflectionMod);
        statSet.setACMiscMod(acMiscMod);
        statSet.setBABPrimary(babPrimary);
        statSet.setBABSecondary(babSecondary);
        statSet.setCMBAbility(cmbAbilityKey);
        statSet.setCMDAbility(cmdAbilityKey);
        statSet.setCMDMiscMod(cmdMiscMod);
        statSet.setSpellResistance(spellResist);
        return statSet;
    }
}
