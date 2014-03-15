package com.lateensoft.pathfinder.toolkit.model.character.stats;

import java.util.*;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;
import android.util.SparseIntArray;

import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.PTBaseApplication;
import com.lateensoft.pathfinder.toolkit.R;

public class PTSkillSet implements Parcelable {
	public static final int ACRO = 1;
    public static final int APPRAISE = 2;
    public static final int BLUFF = 3;
    public static final int CLIMB = 4;
    public static final int CRAFT = 5;
    public static final int DIPLOM = 6;
    public static final int DISABLE_DEV = 7;
    public static final int DISGUISE = 8;
    public static final int ESCAPE = 9;
    public static final int FLY = 10;
    public static final int HANDLE_ANIMAL = 11;
    public static final int HEAL = 12;
    public static final int INTIMIDATE = 13;
    public static final int KNOW_ARCANA = 14;
    public static final int KNOW_DUNGEON = 15;
    public static final int KNOW_ENG = 16;
    public static final int KNOW_GEO = 17;
    public static final int KNOW_HIST = 18;
    public static final int KNOW_LOCAL = 19;
    public static final int KNOW_NATURE = 20;
    public static final int KNOW_NOBILITY = 21;
    public static final int KNOW_PLANES = 22;
    public static final int KNOW_RELIGION = 23;
    public static final int LING = 24;
    public static final int PERCEPT = 25;
    public static final int PERFORM = 26;
    public static final int PROF = 27;
    public static final int RIDE = 28;
    public static final int SENSE_MOTIVE = 29;
    public static final int SLEIGHT_OF_HAND = 30;
    public static final int SPELLCRAFT = 31;
    public static final int STEALTH = 32;
    public static final int SURVIVAL = 33;
    public static final int SWIM = 34;
    public static final int USE_MAGIC_DEVICE = 35;
    
	List<PTSkill> m_skills;
	
	/**
	 * @return an unmodifiable list of the skill keys, in order.
	 */
	public static List<Integer> SKILL_KEYS() {
		Integer[] keys = { ACRO, APPRAISE, BLUFF, CLIMB, CRAFT, DIPLOM,
		    	DISABLE_DEV,DISGUISE,ESCAPE,FLY,HANDLE_ANIMAL, HEAL,INTIMIDATE, KNOW_ARCANA, KNOW_DUNGEON,
		    	KNOW_ENG, KNOW_GEO, KNOW_HIST, KNOW_LOCAL, KNOW_NATURE, KNOW_NOBILITY, KNOW_PLANES, KNOW_RELIGION,
		    	LING,  PERCEPT, PERFORM, PROF, RIDE, SENSE_MOTIVE, SLEIGHT_OF_HAND,SPELLCRAFT, STEALTH,
		    	SURVIVAL, SWIM, USE_MAGIC_DEVICE };
		return Collections.unmodifiableList(Arrays.asList(keys));
	}
	
	/**
	 * @return an unmodifiable list skill keys which can be subtyped.
	 */
	public static List<Integer> SUBTYPED_SKILLS() {
		Integer[] keys = {CRAFT, PERFORM, PROF};
		return Collections.unmodifiableList(Arrays.asList(keys));
	}
	
	public PTSkillSet() {
		int[] defaultSkillAbilityIds = getDefaultAbilityIds();
		List<Integer> constSkillKeys = SKILL_KEYS();
		m_skills = Lists.newArrayListWithCapacity(constSkillKeys.size());
		
		for(int i = 0; i < constSkillKeys.size(); i++) {
			m_skills.add(new PTSkill(constSkillKeys.get(i), defaultSkillAbilityIds[i]));
		}
	}
	
	/**
	 * Safely populates the skill set with skills, in the correct order.
	 * Sets skill to default if not found.
	 * @param skills
	 */
	public PTSkillSet(Collection<? extends PTSkill> skills) {
        m_skills = Lists.newArrayList(skills);
		verifySortSkills(m_skills);
	}
	
	public PTSkillSet(Parcel in) {
        in.readTypedList(m_skills, PTSkill.CREATOR);
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
        out.writeTypedList(m_skills);
	}
	
	/**
	 * Ensures that all skills occur at least once in the list.
	 * If a skill does not appear, add a default version.
     * Also verifies that all skills have valid skill keys
	 */
	private void verifySortSkills(List<PTSkill> skills) {
        for (PTSkill skill : skills) {
            if (!isValidSkill(skill.getSkillKey())) {
                skills.remove(skill);
            }
        }

		int[] defaultSkillAbilityIds = getDefaultAbilityIds();
		boolean found;
		List<Integer> constSkillKeys = SKILL_KEYS();
		for(int i = 0; i < constSkillKeys.size(); i++) {
			found = false;
			for (PTSkill skill : skills) {
				if(skill.getSkillKey() == constSkillKeys.get(i)) {
					found = true;
					break;
				}
			}
			if (!found) {
				skills.add(new PTSkill(constSkillKeys.get(i), defaultSkillAbilityIds[i]));
			}
		}
		
		Collections.sort(skills);
	}
	
	public PTSkill getSkillByIndex(int index) {
		if( index >= 0 && index < m_skills.size() )
			return m_skills.get(index);
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
		for (int i = 0; i < m_skills.size(); i++) {
			if (m_skills.get(i).getRank() > 0) {
				if (trainedSkillIndex == index){
					return m_skills.get(i);
				} else {
					trainedSkillIndex++;
				}
			}
		}
		return null;
	}
	
	public List<PTSkill> getSkills(){
		return m_skills;
	}
	
	public int size() {
		return m_skills.size();
	}
	
	public ArrayList<PTSkill> getTrainedSkills(){
		ArrayList<PTSkill> trainedSkills = new ArrayList<PTSkill>();
		for (int i = 0; i < m_skills.size(); i++) {
			if (m_skills.get(i).getRank() > 0) {
				trainedSkills.add(m_skills.get(i));
			}
		}

        return trainedSkills;
	}
	
	public PTSkill addNewSubSkill(int skillKey) {
		int abilityKey = getDefaultAbilityKeyMap().get(skillKey);
		PTSkill newSkill = new PTSkill(skillKey, abilityKey);
		newSkill.setCharacterID(m_skills.get(0).getCharacterID());
		m_skills.add(newSkill);
		Collections.sort(m_skills);
		return newSkill;
	}
	
	public void deleteSkill(PTSkill skill) {
		m_skills.remove(skill);
	}
	
	/**
	 * 
	 * @param skillKey
	 * @return
	 */
	public boolean hasMultipleOfSkill(int skillKey) {
		int numOfSkill = 0;
		for (PTSkill skill : m_skills) {
			if (skill.getSkillKey() == skillKey) {
				numOfSkill++;
			}
			if (numOfSkill > 1) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @param skillKey
	 * @return true if all subtypes of this skill are either trained or named
	 */
	public boolean allSubSkillsUsed(int skillKey) {
		for (int i = 0; i < m_skills.size(); i++) {
			if (m_skills.get(i).getSkillKey() == skillKey && m_skills.get(i).getRank() == 0
					&& (m_skills.get(i).getSubType() == null || m_skills.get(i).getSubType().isEmpty()) ) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * @return The default abilitykey for the skills, ordered as SKILL_KEYS
	 */
	private static int[] getDefaultAbilityIds() {
		Resources r = PTBaseApplication.getAppContext().getResources();
		return r.getIntArray(R.array.default_skill_ability_keys);
	}
	
	/**
	 * @return a map of the skill key to the ability key
	 */
	public static SparseIntArray getDefaultAbilityKeyMap() {
		List<Integer> constSkillKeys = SKILL_KEYS();
		SparseIntArray map = new SparseIntArray(constSkillKeys.size());
		int[] abilityIds = getDefaultAbilityIds();
		for(int i = 0; i < constSkillKeys.size(); i++) {
			map.append(constSkillKeys.get(i), abilityIds[i]);
		}
		return map;
	}
	
	/**
	 * @return the skill names, in the order as defined by SKILL_KEYS
	 */
	public static String[] getSkillNames() {
		Resources res = PTBaseApplication.getAppContext().getResources();
		return res.getStringArray(R.array.skills);
	}
	
	public static boolean isSubtypedSkill(int skillKey) {
		List<Integer> subtypedSkills = SUBTYPED_SKILLS();
		for (int key : subtypedSkills) {
			if (key == skillKey) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @return a map of the skill keys to their name
	 */
	public static SparseArray<String> getSkillNameMap() {
		List<Integer> constSkillKeys = SKILL_KEYS();
		SparseArray<String> map = new SparseArray<String>(constSkillKeys.size());
		String[] names = getSkillNames();
		for (int i = 0; i < names.length; i++) {
			map.append(constSkillKeys.get(i), names[i]);
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

    public static boolean isValidSkill(int skillKey) {
        return (skillKey > 0 && skillKey <= SKILL_KEYS().size());
    }
}


