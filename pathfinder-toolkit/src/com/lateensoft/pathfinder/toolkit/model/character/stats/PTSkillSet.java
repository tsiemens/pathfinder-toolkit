package com.lateensoft.pathfinder.toolkit.model.character.stats;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.lateensoft.pathfinder.toolkit.R;

public class PTSkillSet implements Parcelable{
	private static final String PARCEL_BUNDLE_KEY_SKILLS = "skills";
	
	PTSkill[] m_skills;
	
	public PTSkillSet(Context context) {
		Resources r = context.getResources();
		String[] skills = r.getStringArray(R.array.skills);
		int[] skillAbilityKeys = r.getIntArray(R.array.skill_ability_keys);
		String[] skillAbilityShortStrings = r.getStringArray(R.array.abilities_short);
		
		m_skills = new PTSkill[skills.length];
		
		for(int i = 0; i < skills.length; i++) {
			m_skills[i] = new PTSkill(skills[i], skillAbilityKeys[i], skillAbilityShortStrings[skillAbilityKeys[i]]);
		}
	}
	
	/**
	 * Sets the skills in the set to skills
	 * Dangerous; should only be used by database
	 * @param skills
	 */
	public PTSkillSet(PTSkill[] skills) {
		m_skills = skills;
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
	
	public PTSkill getSkill(int index) {
		if( index >= 0 && index < m_skills.length )
			return m_skills[index];
		else
			return null;
	}
	
	public void setCharacterId(long id) {
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
	
	//can set/get through reff to skill
	
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


