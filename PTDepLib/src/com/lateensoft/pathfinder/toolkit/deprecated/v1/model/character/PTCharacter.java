package com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character;

import com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.stats.PTAbilitySet;
import com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.stats.PTCombatStatSet;
import com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.stats.PTSaveSet;
import com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.stats.PTSkillSet;

import android.content.Context;

public class PTCharacter {
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
}
