package com.lateensoft.pathfinder.toolkit.stats;

import com.lateensoft.pathfinder.toolkit.repository.PTStorable;

public class PTSkill implements PTStorable {
	String mName;
	boolean mClassSkill;
	String mKeyAbility;
	int mAbilityMod;
	int mRank;
	int mMiscMod;
	int mArmorCheckPenalty;
	int mKeyAbilityKey;
	int mId;
	
	public PTSkill() {
		mName = "";
		mClassSkill = false;
		mKeyAbility = "";
		mAbilityMod = 0;
		mRank = 0;
		mMiscMod = 0;
		mArmorCheckPenalty = 0;
		mKeyAbilityKey = 0;
	}
	
	public PTSkill(int id, String name, int abilityKey, String abilityString) {
		mName = name;
		mClassSkill = false;
		mAbilityMod = 0;
		mRank = 0;
		mMiscMod = 0;
		mArmorCheckPenalty = 0;
		mKeyAbilityKey = abilityKey;
		mKeyAbility = abilityString;
		mId = id;
	}
	
	public PTSkill(String name, int abilityKey, String abilityString) {
		mName = name;
		mClassSkill = false;
		mAbilityMod = 0;
		mRank = 0;
		mMiscMod = 0;
		mArmorCheckPenalty = 0;
		mKeyAbilityKey = abilityKey;
		mKeyAbility = abilityString;
	}
	
	public PTSkill(int id, String name, Boolean classSkill, int abilityMod, int rank,
			int miscMod, int armorCheckPenalty, int abilityKey, String keyAbility) {
		mName = name;
		mClassSkill = false;
		mAbilityMod = 0;
		mRank = 0;
		mMiscMod = 0;
		mArmorCheckPenalty = 0;
		mKeyAbilityKey = abilityKey;
		mKeyAbility = keyAbility;
		mId = id;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return mName;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		mName = name;
	}
	/**
	 * @return the classSkill
	 */
	public boolean isClassSkill() {
		return mClassSkill;
	}
	/**
	 * @param classSkill the classSkill to set
	 */
	public void setClassSkill(boolean classSkill) {
		mClassSkill = classSkill;
	}
	/**
	 * @return the keyAbility
	 */
	public String getKeyAbility() {
		return mKeyAbility;
	}
	/**
	 * @param keyAbility the keyAbility to set
	 */
	public void setKeyAbility(String keyAbility) {
		mKeyAbility = keyAbility;
	}
	/**
	 * @return the total skill Mod
	 */
	public int getSkillMod() {
		int skillMod = 0;
		skillMod = skillMod + mAbilityMod + mRank + mMiscMod + mArmorCheckPenalty;
		
		if(mClassSkill && mRank > 0)
			skillMod += 3;
		
		return skillMod;
	}

	/**
	 * @return the abilityMod
	 */
	public int getAbilityMod() {
		return mAbilityMod;
	}
	/**
	 * @param abilityMod the abilityMod to set
	 */
	public void setAbilityMod(int abilityMod) {
		mAbilityMod = abilityMod;
	}
	/**
	 * @return the rank
	 */
	public int getRank() {
		return mRank;
	}
	/**
	 * @param rank the rank to set
	 */
	public void setRank(int rank) {
		mRank = rank;
	}
	/**
	 * @return the miscMod
	 */
	public int getMiscMod() {
		return mMiscMod;
	}
	/**
	 * @param miscMod the miscMod to set
	 */
	public void setMiscMod(int miscMod) {
		mMiscMod = miscMod;
	}
	/**
	 * @return the armorCheckPenalty
	 */
	public int getArmorCheckPenalty() {
		return mArmorCheckPenalty;
	}
	/**
	 * @param armorCheckPenalty the armorCheckPenalty to set
	 */
	public void setArmorCheckPenalty(int armorCheckPenalty) {
		mArmorCheckPenalty = armorCheckPenalty;
	}
	
	public int getKeyAbilityKey() {
		return mKeyAbilityKey;
	}
	
	public void setKeyAbilityKey(int keyAbilityKey) {
		mKeyAbilityKey = keyAbilityKey;
	}

	@Override
	public int id() {
		return mId;
	}
}
