package com.lateensoft.pathfinder.toolkit.stats;

import android.os.Parcel;
import android.os.Parcelable;

public class PTCombatStatSet implements Parcelable{
	private int mTotalHP;
	private int mWounds;
	private int mNonLethalDamage;
	private int mDamageReduction;
	private int mBaseSpeedFt;
	//may need other for speed
	
	private int mInitDexMod;
	private int mInitMiscMod;
	
	private int mACArmour;
	private int mACShield;
	private int mACDexMod;
	private int mSizeMod;
	private int mACNaturalArmour;
	private int mDeflectionMod;
	private int mACMiscMod;
	
	private int mBABPrimary;
	private String mBABSecondary;
	private int mStrengthMod;
	private int mCMDDexMod;
	
	private int mCMDMiscMod;
	
	private int mSpellResist;
	
	public PTCombatStatSet(){
		mTotalHP = 0;
		mWounds = 0;
		mNonLethalDamage = 0;
		mDamageReduction = 0;
		mBaseSpeedFt = 0;
		
		mInitDexMod = 0;
		mInitMiscMod = 0;
		
		mACArmour = 0;
		mACShield = 0;
		mACDexMod = 0;
		mSizeMod = 0;
		mACNaturalArmour = 0;
		mDeflectionMod = 0;
		mACMiscMod = 0;
		
		mBABPrimary = 0;
		mBABSecondary = "";
		mStrengthMod = 0;
		mACDexMod = 0;
		
		mCMDMiscMod = 0;
		
		mSpellResist = 0;
	}
	
	public PTCombatStatSet(Parcel in) {
		mTotalHP = in.readInt();
		mWounds = in.readInt();
		mNonLethalDamage = in.readInt();
		mDamageReduction = in.readInt();
		mBaseSpeedFt = in.readInt();
		
		mInitDexMod = in.readInt();
		mInitMiscMod = in.readInt();
		
		mACArmour = in.readInt();
		mACShield = in.readInt();
		mACDexMod = in.readInt();
		mSizeMod = in.readInt();
		mACNaturalArmour = in.readInt();
		mDeflectionMod = in.readInt();
		mACMiscMod = in.readInt();
		
		mBABPrimary = in.readInt();
		mBABSecondary = in.readString();
		mStrengthMod = in.readInt();
		mACDexMod = in.readInt();
		
		mCMDMiscMod = in.readInt();
		
		mSpellResist = in.readInt();
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(mTotalHP);
		out.writeInt(mWounds);
		out.writeInt(mNonLethalDamage);
		out.writeInt(mDamageReduction);
		out.writeInt(mBaseSpeedFt);
		
		out.writeInt(mInitDexMod);
		out.writeInt(mInitMiscMod);
		
		out.writeInt(mACArmour);
		out.writeInt(mACShield);
		out.writeInt(mACDexMod);
		out.writeInt(mSizeMod);
		out.writeInt(mACNaturalArmour);
		out.writeInt(mDeflectionMod);
		out.writeInt(mACMiscMod);
		
		out.writeInt(mBABPrimary);
		out.writeString(mBABSecondary);
		out.writeInt(mStrengthMod);
		out.writeInt(mACDexMod);
		
		out.writeInt(mCMDMiscMod);
		
		out.writeInt(mSpellResist);
	}
	
	public void setTotalHP(int totalHP){
		mTotalHP = totalHP;
	}
	
	public int getTotalHP(){
		return mTotalHP;
	}
	
	public void setWounds(int wounds){
		mWounds = wounds;
	}
	
	public int getWounds(){
		return mWounds;
	}
	
	public void setNonLethalDamage(int nonlethalDamage){
		mNonLethalDamage = nonlethalDamage;
	}
	
	public int getNonLethalDamage(){
		return mNonLethalDamage;
	}
	
	public int getCurrentHP(){
		return mTotalHP - mWounds - mNonLethalDamage;
	}
	
	public void setDamageReduction(int damageReduction){
		mDamageReduction = damageReduction;
	}
	
	public int getDamageReduction(){
		return mDamageReduction;
	}
	
	public void setBaseSpeed(int baseSpeedInFeet){
		mBaseSpeedFt = baseSpeedInFeet;
	}
	
	public int getBaseSpeed(){
		return mBaseSpeedFt;
	}
	
	public void setInitDexMod(int dexMod){
		mInitDexMod = dexMod;
	}
	
	public int getInitDexMod(){
		return mInitDexMod;
	}
	
	public void setInitiativeMiscMod(int initMiscMod){
		mInitMiscMod = initMiscMod;
	}
	
	public int getInitiativeMiscMod(){
		return mInitMiscMod;
	}
	
	public int getInitiativeMod(){
		return mInitDexMod + mInitMiscMod;
	}
	
	public void setACArmourBonus(int armourBonus){
		mACArmour = armourBonus;
	}
	
	public int getACArmourBonus(){
		return mACArmour;
	}
	
	public void setACShieldBonus(int shieldBonus){
		mACShield = shieldBonus;
	}
	
	public int getACShieldBonus(){
		return mACShield;
	}
	
	public void setACDexMod(int dexMod){
		mACDexMod = dexMod;
	}
	
	public int getACDexMod(){
		return mACDexMod;
	}
	
	public void setSizeModifier(int sizeMod){
		mSizeMod = sizeMod;
	}
	
	public int getSizeModifier(){
		return mSizeMod;
	}
	
	public void setNaturalArmour(int naturalArmour){
		mACNaturalArmour = naturalArmour;
	}
	
	public int getNaturalArmour(){
		return mACNaturalArmour;
	}
	
	public void setDeflectionMod(int deflectionMod){
		mDeflectionMod = deflectionMod;
	}
	
	public int getDeflectionMod(){
		return mDeflectionMod;
	}
	
	public void setACMiscMod(int miscMod){
		mACMiscMod = miscMod;
	}
	
	public int getACMiscMod(){
		return mACMiscMod;
	}
	
	/**
	 * 
	 * @return the net AC of the character. This is 10 + armour + shield + dexmod + sizemod + natural + defect + misc
	 */
	public int getTotalAC(){
		return 10 + mACArmour + mACShield + mACDexMod + mSizeMod + mACNaturalArmour + mDeflectionMod + mACMiscMod;
	}
	
	/**
	 * 
	 * @return the touch AC of the character. This is 10 + dexmod + sizemod + defect + misc
	 */
	public int getTouchAC(){
		return 10 + mACDexMod + mSizeMod + mDeflectionMod + mACMiscMod;
	}
	
	/**
	 * 
	 * @return the touch AC of the character. This is normal AC - dexmod
	 */
	public int getFlatFootedAC(){
		return getTotalAC() - mACDexMod;
	}
	
	
	/**
	 * Used to set the Base Attack Bonus for first attack. Sometimes referred to as "full" BAB
	 * @param BABPrimary
	 */
	public void setBABPrimary(int BABPrimary){
		mBABPrimary = BABPrimary;
	}
	
	public int getBABPrimary(){
		return mBABPrimary;
	}
	
	/**
	 * Sets the representation of the Base Attack Bonus for attacks after the first. 
	 * If a character has a BAB of x/y/z, this should be in the format of y/z
	 * @param BABSecondary
	 */
	public void setBABSecondary(String BABSecondary){
		if(BABSecondary != null)
			mBABSecondary = BABSecondary;
	}
	
	public String getBABSecondary(){
		return mBABSecondary;
	}
	
	public void setStrengthMod(int strengthMod){
		mStrengthMod = strengthMod;
	}
	
	public int getStrengthMod(){
		return mStrengthMod;
	}
	
	public void setCMDDexMod(int dexMod){
		mCMDDexMod = dexMod;
	}
	
	public int getCMDDexMod(){
		return mCMDDexMod;
	}
	
	public void setSpellResistance(int spellResist){
		mSpellResist = spellResist;
	}
	
	public int getSpellResist(){
		return mSpellResist;
	}
	
	public int getCMDMiscMod(){
		return mCMDMiscMod;
	}
	
	public void setCMDMiscMod(int miscMod){
		mCMDMiscMod = miscMod;
	}
	
	/**
	 * 
	 * @return CMB = BAB + Strength mod - size mod
	 */
	public int getCombatManeuverBonus(){
		return mBABPrimary + mStrengthMod - mSizeMod;
	}
	
	/**
	 * 
	 * @return CMD = BAB + Strength mod - size mod + dex mod + 10 + misc mod
	 */
	public int getCombatManeuverDefense(){
		return getCombatManeuverBonus() + mCMDDexMod + mCMDMiscMod + 10;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	public static final Parcelable.Creator<PTCombatStatSet> CREATOR = new Parcelable.Creator<PTCombatStatSet>() {
		public PTCombatStatSet createFromParcel(Parcel in) {
			return new PTCombatStatSet(in);
		}
		
		public PTCombatStatSet[] newArray(int size) {
			return new PTCombatStatSet[size];
		}
	};
}
