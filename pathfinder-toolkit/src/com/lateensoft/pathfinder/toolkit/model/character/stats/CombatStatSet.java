package com.lateensoft.pathfinder.toolkit.model.character.stats;

import com.lateensoft.pathfinder.toolkit.db.repository.Storable;

import android.os.Parcel;
import android.os.Parcelable;

public class CombatStatSet implements Parcelable, Storable {
	public static final int DEFUALT_INIT_ABILITY_KEY = AbilitySet.KEY_DEX;
	public static final int DEFUALT_AC_ABILITY_KEY = AbilitySet.KEY_DEX;
	public static final int DEFUALT_CMB_ABILITY_KEY = AbilitySet.KEY_STR;
	public static final int DEFUALT_CMD_ABILITY_KEY = AbilitySet.KEY_DEX;
	
	private int m_totalHP;
	private int m_wounds;
	private int m_nonLethalDamage;
	private int m_damageReduction;
	private int m_baseSpeedFt;
	//may need other for speed
	
	/** Defaulted to dex */
	private int m_initAbilityKey;
	private int m_initMiscMod;
	
	private int m_ACArmour;
	private int m_ACShield;
	/** Defaulted to dex */
	private int m_ACAbilityKey;
	private int m_sizeMod;
	private int m_ACNaturalArmour;
	private int m_deflectionMod;
	private int m_ACMiscMod;
	
	private int m_spellResist;
	
	private int m_BABPrimary;
	private String m_BABSecondary;
	/** Defaulted to strength */
	private int m_CMBAbilityKey;
	
	/** Defaulted to dex */
	private int m_CMDAbilityKey;
	private int m_CMDMiscMod;
	
	private long m_characterId;
	
	public CombatStatSet(long characterId) {
		this();
		m_characterId = characterId;
	}
	
	public CombatStatSet(){
		m_totalHP = 0;
		m_wounds = 0;
		m_nonLethalDamage = 0;
		m_damageReduction = 0;
		m_baseSpeedFt = 0;
		
		m_initAbilityKey = DEFUALT_INIT_ABILITY_KEY;
		m_initMiscMod = 0;
		
		m_ACArmour = 0;
		m_ACShield = 0;
		m_ACAbilityKey = DEFUALT_AC_ABILITY_KEY;
		m_sizeMod = 0;
		m_ACNaturalArmour = 0;
		m_deflectionMod = 0;
		m_ACMiscMod = 0;
		
		m_BABPrimary = 0;
		m_BABSecondary = "";
		m_CMBAbilityKey = DEFUALT_CMB_ABILITY_KEY;
		
		m_CMDAbilityKey = DEFUALT_CMD_ABILITY_KEY;
		m_CMDMiscMod = 0;
		
		m_spellResist = 0;
		
		m_characterId = UNSET_ID;
	}
	
	public CombatStatSet(Parcel in) {
		m_totalHP = in.readInt();
		m_wounds = in.readInt();
		m_nonLethalDamage = in.readInt();
		m_damageReduction = in.readInt();
		m_baseSpeedFt = in.readInt();
		
		m_initAbilityKey = in.readInt();
		m_initMiscMod = in.readInt();
		
		m_ACArmour = in.readInt();
		m_ACShield = in.readInt();
		m_ACAbilityKey = in.readInt();
		m_sizeMod = in.readInt();
		m_ACNaturalArmour = in.readInt();
		m_deflectionMod = in.readInt();
		m_ACMiscMod = in.readInt();
		
		m_BABPrimary = in.readInt();
		m_BABSecondary = in.readString();
		m_CMBAbilityKey = in.readInt();
		
		m_CMDAbilityKey = in.readInt();
		m_CMDMiscMod = in.readInt();
		
		m_spellResist = in.readInt();
		
		m_characterId = in.readLong();
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(m_totalHP);
		out.writeInt(m_wounds);
		out.writeInt(m_nonLethalDamage);
		out.writeInt(m_damageReduction);
		out.writeInt(m_baseSpeedFt);
		
		out.writeInt(m_initAbilityKey);
		out.writeInt(m_initMiscMod);
		
		out.writeInt(m_ACArmour);
		out.writeInt(m_ACShield);
		out.writeInt(m_ACAbilityKey);
		out.writeInt(m_sizeMod);
		out.writeInt(m_ACNaturalArmour);
		out.writeInt(m_deflectionMod);
		out.writeInt(m_ACMiscMod);
		
		out.writeInt(m_BABPrimary);
		out.writeString(m_BABSecondary);
		out.writeInt(m_CMBAbilityKey);
		
		out.writeInt(m_CMDAbilityKey);
		out.writeInt(m_CMDMiscMod);
		
		out.writeInt(m_spellResist);
		
		out.writeLong(m_characterId);
	}
	
	public void setTotalHP(int totalHP){
		m_totalHP = totalHP;
	}
	
	public int getTotalHP(){
		return m_totalHP;
	}
	
	public void setWounds(int wounds){
		m_wounds = wounds;
	}
	
	public int getWounds(){
		return m_wounds;
	}
	
	public void setNonLethalDamage(int nonlethalDamage){
		m_nonLethalDamage = nonlethalDamage;
	}
	
	public int getNonLethalDamage(){
		return m_nonLethalDamage;
	}
	
	public int getCurrentHP(){
		return m_totalHP - m_wounds - m_nonLethalDamage;
	}
	
	public void setDamageReduction(int damageReduction){
		m_damageReduction = damageReduction;
	}
	
	public int getDamageReduction(){
		return m_damageReduction;
	}
	
	public void setBaseSpeed(int baseSpeedInFeet){
		m_baseSpeedFt = baseSpeedInFeet;
	}
	
	public int getBaseSpeed(){
		return m_baseSpeedFt;
	}
	
	public void setInitAbilityKey(int abilityKey){
		m_initAbilityKey = abilityKey;
	}
	
	public int getInitAbilityKey(){
		return m_initAbilityKey;
	}
	
	public void setInitiativeMiscMod(int initMiscMod){
		m_initMiscMod = initMiscMod;
	}
	
	public int getInitiativeMiscMod(){
		return m_initMiscMod;
	}
	
	/**
	 * @param abilitySet
	 * @return the totalled initiative mod, using the values in abilitySet
	 */
	public int getInitiativeMod(AbilitySet abilitySet, int maxDex){
		return abilitySet.getTotalAbilityMod(m_initAbilityKey, maxDex) + m_initMiscMod;
	}
	
	public void setACArmourBonus(int armourBonus){
		m_ACArmour = armourBonus;
	}
	
	public int getACArmourBonus(){
		return m_ACArmour;
	}
	
	public void setACShieldBonus(int shieldBonus){
		m_ACShield = shieldBonus;
	}
	
	public int getACShieldBonus(){
		return m_ACShield;
	}
	
	public void setACAbilityKey(int abilityKey){
		m_ACAbilityKey = abilityKey;
	}
	
	public int getACAbilityKey(){
		return m_ACAbilityKey;
	}
	
	public void setSizeModifier(int sizeMod){
		m_sizeMod = sizeMod;
	}
	
	public int getSizeModifier(){
		return m_sizeMod;
	}
	
	public void setNaturalArmour(int naturalArmour){
		m_ACNaturalArmour = naturalArmour;
	}
	
	public int getNaturalArmour(){
		return m_ACNaturalArmour;
	}
	
	public void setDeflectionMod(int deflectionMod){
		m_deflectionMod = deflectionMod;
	}
	
	public int getDeflectionMod(){
		return m_deflectionMod;
	}
	
	public void setACMiscMod(int miscMod){
		m_ACMiscMod = miscMod;
	}
	
	public int getACMiscMod(){
		return m_ACMiscMod;
	}
	
	/**
	 * 
	 * @return the net AC of the character. This is 10 + armour + shield + dexmod + sizemod + natural + defect + misc
	 */
	public int getTotalAC(AbilitySet abilitySet, int maxDex){
		return 10 + m_ACArmour + m_ACShield + abilitySet.getTotalAbilityMod(m_ACAbilityKey, maxDex) 
				+ m_sizeMod + m_ACNaturalArmour + m_deflectionMod + m_ACMiscMod;
	}
	
	/**
	 * 
	 * @return the touch AC of the character. This is 10 + dexmod + sizemod + defect + misc
	 */
	public int getTouchAC(AbilitySet abilitySet, int maxDex){
		return 10 + abilitySet.getTotalAbilityMod(m_ACAbilityKey, maxDex) + m_sizeMod + m_deflectionMod + m_ACMiscMod;
	}
	
	/**
	 * 
	 * @return the touch AC of the character. This is normal AC without dex
	 */
	public int getFlatFootedAC(){
		return 10 + m_ACArmour + m_ACShield + m_sizeMod + m_ACNaturalArmour + m_deflectionMod + m_ACMiscMod;
	}
	
	
	/**
	 * Used to set the Base Attack Bonus for first attack. Sometimes referred to as "full" BAB
	 * @param BABPrimary
	 */
	public void setBABPrimary(int BABPrimary){
		m_BABPrimary = BABPrimary;
	}
	
	public int getBABPrimary(){
		return m_BABPrimary;
	}
	
	/**
	 * Sets the representation of the Base Attack Bonus for attacks after the first. 
	 * If a character has a BAB of x/y/z, this should be in the format of y/z
	 * @param BABSecondary
	 */
	public void setBABSecondary(String BABSecondary){
		if(BABSecondary != null)
			m_BABSecondary = BABSecondary;
	}
	
	public String getBABSecondary(){
		return m_BABSecondary;
	}
	
	public void setCMBAbilityKey(int abilityKey){
		m_CMBAbilityKey = abilityKey;
	}
	
	public int getCMBAbilityKey(){
		return m_CMBAbilityKey;
	}
	
	public void setCMDAbilityKey(int abilityKey){
		m_CMDAbilityKey = abilityKey;
	}
	
	public int getCMDAbilityKey(){
		return m_CMDAbilityKey;
	}
	
	public void setSpellResistance(int spellResist){
		m_spellResist = spellResist;
	}
	
	public int getSpellResist(){
		return m_spellResist;
	}
	
	public int getCMDMiscMod(){
		return m_CMDMiscMod;
	}
	
	public void setCMDMiscMod(int miscMod){
		m_CMDMiscMod = miscMod;
	}
	
	/**
	 * 
	 * @return CMB = BAB + Strength mod - size mod
	 */
	public int getCombatManeuverBonus(AbilitySet abilitySet, int maxDex){
		return m_BABPrimary + abilitySet.getTotalAbilityMod(m_CMBAbilityKey, maxDex) - m_sizeMod;
	}
	
	/**
	 * 
	 * @return CMD = BAB + Strength mod - size mod + dex mod + 10 + misc mod
	 */
	public int getCombatManeuverDefense(AbilitySet abilitySet, int maxDex){
		return getCombatManeuverBonus(abilitySet, maxDex) 
				+ abilitySet.getTotalAbilityMod(m_CMDAbilityKey, maxDex) + m_CMDMiscMod + 10;
	}

	/**
	 * Sets the character ID
	 */
	@Override
	public void setID(long id) {
		m_characterId = id;
	}
	
	/**
	 * @return the character ID
	 */
	@Override
	public long getID() {
		return m_characterId;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	public static final Parcelable.Creator<CombatStatSet> CREATOR = new Parcelable.Creator<CombatStatSet>() {
		public CombatStatSet createFromParcel(Parcel in) {
			return new CombatStatSet(in);
		}
		
		public CombatStatSet[] newArray(int size) {
			return new CombatStatSet[size];
		}
	};
}
