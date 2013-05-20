package com.lateensoft.pathfinder.toolkit.stats;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;

import com.lateensoft.pathfinder.toolkit.R;

public class PTSkillSet {
	PTSkill[] mSkills;
	
	public PTSkillSet(Context context) {
		Resources r = context.getResources();
		String[] skills = r.getStringArray(R.array.skills);
		int[] skillAbilityKeys = r.getIntArray(R.array.skill_ability_keys);
		String[] skillAbilityShortStrings = r.getStringArray(R.array.abilities_short);
		
		mSkills = new PTSkill[skills.length];
		
		for(int i = 0; i < skills.length; i++) {
			mSkills[i] = new PTSkill(skills[i], skillAbilityKeys[i], skillAbilityShortStrings[skillAbilityKeys[i]]);
		}
	}
	
	public PTSkill getSkill(int index) {
		if( index >= 0 && index < mSkills.length )
			return mSkills[index];
		else
			return null;
	}
	
	/**
	 * @return reference to skill at index. Index corresponds to array created by getTrainedSkills
	 */
	public PTSkill getTrainedSkill(int index){
		int trainedSkillIndex = 0;
		for (int i = 0; i < mSkills.length; i++) {
			if (mSkills[i].getRank() > 0) {
				if (trainedSkillIndex == index){
					return mSkills[i];
				} else {
					trainedSkillIndex++;
				}
			}
		}
		return null;
	}
	
	public PTSkill[] getSkills(){
		return mSkills;
	}
	
	public PTSkill[] getTrainedSkills(){
		ArrayList<PTSkill> trainedSkills = new ArrayList<PTSkill>();
		for (int i = 0; i < mSkills.length; i++) {
			if (mSkills[i].getRank() > 0) {
				trainedSkills.add(mSkills[i]);
			}
		}
		PTSkill[] returnArray = new PTSkill[trainedSkills.size()];
		return trainedSkills.toArray(returnArray);
	}
	
	//can set/get through reff to skill
}


