package com.lateensoft.pathfinder.toolkit.model.character.stats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.lateensoft.pathfinder.toolkit.PTBaseApplication;
import com.lateensoft.pathfinder.toolkit.R;

public class PTSkillSet implements Parcelable {
	private static final String PARCEL_BUNDLE_KEY_SKILLS = "skills";
	
	public static final long ACRO = 1;
    public static final long APPRAISE = 2;
    public static final long BLUFF = 3;
    public static final long CLIMB = 4;
    public static final long CRAFT = 5;
    public static final long DIPLOM = 6;
    public static final long DISABLE_DEV = 7;
    public static final long DISGUISE = 8;
    public static final long ESCAPE = 9;
    public static final long FLY = 10;
    public static final long HANDLE_ANIMAL = 11;
    public static final long HEAL = 12;
    public static final long INTIMIDATE = 13;
    public static final long KNOW_ARCANA = 14;
    public static final long KNOW_DUNGEON = 15;
    public static final long KNOW_ENG = 16;
    public static final long KNOW_GEO = 17;
    public static final long KNOW_HIST = 18;
    public static final long KNOW_LOCAL = 19;
    public static final long KNOW_NATURE = 20;
    public static final long KNOW_NOBILITY = 21;
    public static final long KNOW_PLANES = 22;
    public static final long KNOW_RELIGION = 23;
    public static final long LING = 24;
    public static final long PERCEPT = 25;
    public static final long PERFORM = 26;
    public static final long PROF = 27;
    public static final long RIDE = 28;
    public static final long SENSE_MOTIVE = 29;
    public static final long SLEIGHT_OF_HAND = 30;
    public static final long SPELLCRAFT = 31;
    public static final long STEALTH = 32;
    public static final long SURVIVAL = 33;
    public static final long SWIM = 34;
    public static final long USE_MAGIC_DEVICE = 35;
    
    public static final long[] SKILL_IDS = { ACRO, APPRAISE, BLUFF, CLIMB,CRAFT, DIPLOM,
    	DISABLE_DEV,DISGUISE,ESCAPE,FLY,HANDLE_ANIMAL, HEAL,INTIMIDATE, KNOW_ARCANA, KNOW_DUNGEON,
    	KNOW_ENG, KNOW_GEO, KNOW_HIST, KNOW_LOCAL, KNOW_NATURE, KNOW_NOBILITY, KNOW_PLANES, KNOW_RELIGION,
    	LING,  PERCEPT, PERFORM, PROF, RIDE, SENSE_MOTIVE, SLEIGHT_OF_HAND,SPELLCRAFT, STEALTH,
    	SURVIVAL, SWIM, USE_MAGIC_DEVICE };
	
	PTSkill[] m_skills;
	
	public PTSkillSet() {
		int[] defaultSkillAbilityIds = getDefaultAbilityIds();
		
		m_skills = new PTSkill[SKILL_IDS.length];
		
		for(int i = 0; i < SKILL_IDS.length; i++) {
			m_skills[i] = new PTSkill(SKILL_IDS[i], (long) defaultSkillAbilityIds[i]);
		}
	}
	
	/**
	 * Safely populates the skill set with skills, in the correct order.
	 * Sets skill to default if not found.
	 * @param skills
	 */
	public PTSkillSet(PTSkill[] skills) {
		int[] defaultSkillAbilityIds = getDefaultAbilityIds();
		m_skills = new PTSkill[SKILL_IDS.length];
		List<PTSkill> skillsList = new ArrayList<PTSkill>(Arrays.asList(skills));
		
		for(int i = 0; i < SKILL_IDS.length; i++) {
			for (PTSkill skill : skillsList) {
				if(skill.getID() == SKILL_IDS[i]) {
					skillsList.remove(skill);
					m_skills[i] = skill;
					break;
				}
			}
			if (m_skills[i] == null) {
				m_skills[i] = new PTSkill(SKILL_IDS[i], (long) defaultSkillAbilityIds[i]);
			}
		}
	}
	
	public PTSkillSet(Parcel in) {
		Bundle objectBundle = in.readBundle();
		m_skills = (PTSkill[]) objectBundle.getParcelableArray(PARCEL_BUNDLE_KEY_SKILLS);
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		Bundle objectBundle = new Bundle();
		objectBundle.putParcelableArray(PARCEL_BUNDLE_KEY_SKILLS, m_skills);
		out.writeBundle(objectBundle);
	}
	
	public PTSkill getSkillByIndex(int index) {
		if( index >= 0 && index < m_skills.length )
			return m_skills[index];
		else
			return null;
	}
	
	public void setCharacterID(long id) {
		for (PTSkill skill : m_skills) {
			skill.setCharacterID(id);
		}
	}
	
	/**
	 * @return reference to skill at index. Index corresponds to array created by getTrainedSkills
	 */
	public PTSkill getTrainedSkill(int index){
		int trainedSkillIndex = 0;
		for (int i = 0; i < m_skills.length; i++) {
			if (m_skills[i].getRank() > 0) {
				if (trainedSkillIndex == index){
					return m_skills[i];
				} else {
					trainedSkillIndex++;
				}
			}
		}
		return null;
	}
	
	public PTSkill[] getSkills(){
		return m_skills;
	}
	
	public PTSkill[] getTrainedSkills(){
		ArrayList<PTSkill> trainedSkills = new ArrayList<PTSkill>();
		for (int i = 0; i < m_skills.length; i++) {
			if (m_skills[i].getRank() > 0) {
				trainedSkills.add(m_skills[i]);
			}
		}
		PTSkill[] returnArray = new PTSkill[trainedSkills.size()];
		return trainedSkills.toArray(returnArray);
	}
	
	public static int[] getDefaultAbilityIds() {
		Resources r = PTBaseApplication.getAppContext().getResources();
		return r.getIntArray(R.array.default_skill_ability_ids);
	}
	
	public static Map<Long, Long> getDefaultAbilityIdMap() {
		Map<Long, Long> map = new HashMap<Long, Long>(SKILL_IDS.length);
		int[] abilityIds = getDefaultAbilityIds();
		for(int i = 0; i < SKILL_IDS.length; i++) {
			map.put(SKILL_IDS[i], (long) abilityIds[i]);
		}
		return map;
	}
	
	/**
	 * @return the skill names, in the order as defined by SKILL_IDS
	 */
	public static String[] getSkillNames() {
		Resources res = PTBaseApplication.getAppContext().getResources();
		return res.getStringArray(R.array.skills);
	}
	
	/**
	 * @return a map of the skill ids to their name
	 */
	public static Map<Long, String> getSkillNameMap() {
		Map<Long, String> map = new HashMap<Long, String>(SKILL_IDS.length);
		String[] names = getSkillNames();
		for (int i = 0; i < names.length; i++) {
			map.put(SKILL_IDS[i], names[i]);
		}
		return map;
	}	
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	public static final Parcelable.Creator<PTSkillSet> CREATOR = new Parcelable.Creator<PTSkillSet>() {
		public PTSkillSet createFromParcel(Parcel in) {
			return new PTSkillSet(in);
		}
		
		public PTSkillSet[] newArray(int size) {
			return new PTSkillSet[size];
		}
	};
}


