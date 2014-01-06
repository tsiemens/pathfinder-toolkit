package com.lateensoft.pathfinder.toolkit.character.sheet;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.lateensoft.pathfinder.toolkit.character.PTFeatList;
import com.lateensoft.pathfinder.toolkit.character.PTFluffInfo;
import com.lateensoft.pathfinder.toolkit.character.PTInventory;
import com.lateensoft.pathfinder.toolkit.character.PTSpellBook;
import com.lateensoft.pathfinder.toolkit.repository.PTStorable;
import com.lateensoft.pathfinder.toolkit.stats.PTAbilitySet;
import com.lateensoft.pathfinder.toolkit.stats.PTCombatStatSet;
import com.lateensoft.pathfinder.toolkit.stats.PTSaveSet;
import com.lateensoft.pathfinder.toolkit.stats.PTSkillSet;

public class PTCharacter implements Parcelable, PTStorable {
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
	PTFluffInfo mFluffInfo;
	PTInventory mInventory;
	public double mGold;
	PTFeatList mFeats;
	PTSpellBook mSpellBook;
	
	private long mID;
	private String mTag;
	
	public PTCharacter(long mCharacterId, String name, Context context) {
		this(name, context);
		mID = mCharacterId;
	}
	
	public PTCharacter(String name, Context context) {
		mAbilitySet = new PTAbilitySet();
		mTempAbilitySet = new PTAbilitySet();
		mCombatStatSet = new PTCombatStatSet();
		mSkillSet = new PTSkillSet(context);
		mSaveSet = new PTSaveSet(context);
		mFluffInfo = new PTFluffInfo();
		mInventory = new PTInventory();
		mFeats = new PTFeatList();
		mSpellBook = new PTSpellBook();
		mGold = 0;
		setName(name);
		mTag = name;
		mID = 0;
	}
	
	public PTCharacter(Parcel in) {
		Bundle objectBundle = in.readBundle();
		mAbilitySet = (PTAbilitySet) objectBundle.getParcelable(PARCEL_BUNDLE_KEY_ABILITIES);
		mTempAbilitySet = (PTAbilitySet) objectBundle.getParcelable(PARCEL_BUNDLE_KEY_TEMP_ABILITIES);
		mCombatStatSet = (PTCombatStatSet) objectBundle.getParcelable(PARCEL_BUNDLE_KEY_COMBAT_STATS);
		mSkillSet = (PTSkillSet) objectBundle.getParcelable(PARCEL_BUNDLE_KEY_SKILLS);
		mSaveSet = (PTSaveSet) objectBundle.getParcelable(PARCEL_BUNDLE_KEY_SAVES);
		mFluffInfo = (PTFluffInfo) objectBundle.getParcelable(PARCEL_BUNDLE_KEY_FLUFF);
		mInventory = (PTInventory) objectBundle.getParcelable(PARCEL_BUNDLE_KEY_INVENTORY);
		mFeats = (PTFeatList) objectBundle.getParcelable(PARCEL_BUNDLE_KEY_FEATS);
		mSpellBook = (PTSpellBook) objectBundle.getParcelable(PARCEL_BUNDLE_KEY_SPELLS);
		mGold = in.readDouble();
		mID = in.readLong();
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
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
		out.writeLong(mID);
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
	
	public PTInventory getInventory(){
		return mInventory;
	}
	
	public void setInventory(PTInventory newInventory){
		mInventory = newInventory;
	}
	
	public PTFeatList getFeatList(){
		return mFeats;
	}
	
	public void setFeatList(PTFeatList newFeats){
		mFeats = newFeats;
	}

	public PTFluffInfo getFluff() {
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
	public Long getID() {
		return mID;
	}
	
	public void setID(long id) {
		mID = id;
	}

	@Override
	public Long getCharacterID() {
		return mID;
	}
	
	public String getTag() {
		return mTag;
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
