package com.lateensoft.pathfinder.toolkit.character;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.lateensoft.pathfinder.toolkit.stats.PTAbilityScore;
import com.lateensoft.pathfinder.toolkit.stats.PTAbilitySet;
import com.lateensoft.pathfinder.toolkit.stats.PTCombatStatSet;
import com.lateensoft.pathfinder.toolkit.stats.PTSaveSet;
import com.lateensoft.pathfinder.toolkit.stats.PTSkillSet;

public class PTCharacter implements Parcelable{
	private static final String PARCEL_BUNDLE_KEY_ABILITIES = "abilities";
	private static final String PARCEL_BUNDLE_KEY_TEMP_ABILITIES = "abilities_temp";
	private static final String PARCEL_BUNDLE_KEY_COMBAT_STATS = "combat_stats";
	private static final String PARCEL_BUNDLE_KEY_SKILLS = "skills";
	private static final String PARCEL_BUNDLE_KEY_SAVES = "saves";
	private static final String PARCEL_BUNDLE_KEY_FLUFF = "fluff";
	private static final String PARCEL_BUNDLE_KEY_INVENTORY = "inventory";
	private static final String PARCEL_BUNDLE_KEY_FEATS = "feats";
	private static final String PARCEL_BUNDLE_KEY_SPELLS = "spells";
	
	PTAbilitySet mAbilitySet;
	PTAbilitySet mTempAbilitySet;
	PTCombatStatSet mCombatStatSet;
	PTSkillSet mSkillSet;
	PTSaveSet mSaveSet;
	PTCharacterFluffInfo mFluffInfo;
	PTCharacterInventory mInventory;
	public double mGold;
	PTCharacterFeatList mFeats;
	PTSpellBook mSpellBook;
	
	public int mID;
	
	public PTCharacter(String name, Context context) {
		mAbilitySet = new PTAbilitySet();
		mTempAbilitySet = new PTAbilitySet();
		mCombatStatSet = new PTCombatStatSet();
		mSkillSet = new PTSkillSet(context);
		mSaveSet = new PTSaveSet(context);
		mFluffInfo = new PTCharacterFluffInfo();
		mInventory = new PTCharacterInventory();
		mFeats = new PTCharacterFeatList();
		mSpellBook = new PTSpellBook();
		mGold = 0;
		setName(name);
		mID = 0;
	}
	
	public PTCharacter(Parcel in) {
		Bundle objectBundle = in.readBundle();
		mAbilitySet = (PTAbilitySet) objectBundle.getParcelable(PARCEL_BUNDLE_KEY_ABILITIES);
		mTempAbilitySet = (PTAbilitySet) objectBundle.getParcelable(PARCEL_BUNDLE_KEY_TEMP_ABILITIES);
		mCombatStatSet = (PTCombatStatSet) objectBundle.getParcelable(PARCEL_BUNDLE_KEY_COMBAT_STATS);
		mSkillSet = (PTSkillSet) objectBundle.getParcelable(PARCEL_BUNDLE_KEY_SKILLS);
		mSaveSet = (PTSaveSet) objectBundle.getParcelable(PARCEL_BUNDLE_KEY_SAVES);
		mFluffInfo = (PTCharacterFluffInfo) objectBundle.getParcelable(PARCEL_BUNDLE_KEY_FLUFF);
		mInventory = (PTCharacterInventory) objectBundle.getParcelable(PARCEL_BUNDLE_KEY_INVENTORY);
		mFeats = (PTCharacterFeatList) objectBundle.getParcelable(PARCEL_BUNDLE_KEY_FEATS);
		mSpellBook = (PTSpellBook) objectBundle.getParcelable(PARCEL_BUNDLE_KEY_SPELLS);
		mGold = in.readDouble();
		mID = in.readInt();
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		// TODO
		Bundle objectBundle = new Bundle();
		objectBundle.putParcelable(PARCEL_BUNDLE_KEY_ABILITIES, mAbilitySet);
		objectBundle.putParcelable(PARCEL_BUNDLE_KEY_TEMP_ABILITIES, mTempAbilitySet);
		objectBundle.putParcelable(PARCEL_BUNDLE_KEY_COMBAT_STATS, mCombatStatSet);
		objectBundle.putParcelable(PARCEL_BUNDLE_KEY_SKILLS, mSkillSet);
		objectBundle.putParcelable(PARCEL_BUNDLE_KEY_SAVES, mSaveSet);
		objectBundle.putParcelable(PARCEL_BUNDLE_KEY_FLUFF, mFluffInfo);
		objectBundle.putParcelable(PARCEL_BUNDLE_KEY_INVENTORY, mInventory);
		objectBundle.putParcelable(PARCEL_BUNDLE_KEY_FEATS, mFeats);
		objectBundle.putParcelable(PARCEL_BUNDLE_KEY_SPELLS, mSpellBook);
		out.writeBundle(objectBundle);
		out.writeDouble(mGold);
		out.writeInt(mID);
	}
	
	public void setAbilitySet(PTAbilitySet abilitySet) {
		mAbilitySet = abilitySet;
	}
	
	public PTAbilitySet getAbilitySet() {
		return mAbilitySet;
	}
	
	public PTAbilitySet getTempAbilitySet() {
		return mTempAbilitySet;
	}
	
	public PTCombatStatSet getCombatStatSet(){
		return mCombatStatSet;
	}
	
	public PTSkillSet getSkillSet() {
		return mSkillSet;
	}
	
	public PTCharacterInventory getInventory(){
		return mInventory;
	}
	
	public void setInventory(PTCharacterInventory newInventory){
		mInventory = newInventory;
	}
	
	public PTCharacterFeatList getFeatList(){
		return mFeats;
	}
	
	public void setFeatList(PTCharacterFeatList newFeats){
		mFeats = newFeats;
	}

	public PTCharacterFluffInfo getFluff() {
		return mFluffInfo;
	}
	
	public PTSaveSet getSaveSet(){
		return mSaveSet;
	}
	
	public String getName(){
		return mFluffInfo.getName();
	}
	
	public void setName(String name){
		if(name != null && name != "")
			mFluffInfo.setName(name);
	}

	public PTSpellBook getSpellBook() {
		return mSpellBook;
	}
	
	public void setSpellBook(PTSpellBook spellBook) {
		mSpellBook = spellBook;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	public static final Parcelable.Creator<PTCharacter> CREATOR = new Parcelable.Creator<PTCharacter>() {
		public PTCharacter createFromParcel(Parcel in) {
			return new PTCharacter(in);
		}
		
		public PTCharacter[] newArray(int size) {
			return new PTCharacter[size];
		}
	};
}
