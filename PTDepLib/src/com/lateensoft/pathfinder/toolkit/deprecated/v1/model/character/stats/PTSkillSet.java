package com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.stats;

import android.content.Context;
import android.content.res.Resources;

import com.lateensoft.pathfinder.toolkit.deprecated.R;

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
	
	public PTSkill[] getSkills(){
		return mSkills;
	}
	
	//can set/get through reff to skill
}


