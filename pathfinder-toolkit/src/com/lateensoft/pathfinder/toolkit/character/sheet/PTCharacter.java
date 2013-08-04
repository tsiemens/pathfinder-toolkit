package com.lateensoft.pathfinder.toolkit.character.sheet;

import android.content.Context;

import com.lateensoft.pathfinder.toolkit.character.PTFeatList;
import com.lateensoft.pathfinder.toolkit.character.PTFluffInfo;
import com.lateensoft.pathfinder.toolkit.character.PTInventory;
import com.lateensoft.pathfinder.toolkit.character.PTSpellBook;
import com.lateensoft.pathfinder.toolkit.repository.PTStorable;
import com.lateensoft.pathfinder.toolkit.stats.PTAbilitySet;
import com.lateensoft.pathfinder.toolkit.stats.PTCombatStatSet;
import com.lateensoft.pathfinder.toolkit.stats.PTSaveSet;
import com.lateensoft.pathfinder.toolkit.stats.PTSkillSet;

public class PTCharacter implements PTStorable {
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
	
	public void setID(int id) {
		mID = (long) id;
	}
	
	public int getIDAsInt() {
		long longID = (long) mID;
		return (int) longID;
	}

	@Override
	public Long getCharacterID() {
		return mID;
	}
	
	public String getTag() {
		return mTag;
	}
}
