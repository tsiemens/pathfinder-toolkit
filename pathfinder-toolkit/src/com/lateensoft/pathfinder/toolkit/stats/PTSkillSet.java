package com.lateensoft.pathfinder.toolkit.stats;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.lateensoft.pathfinder.toolkit.R;

public class PTSkillSet implements Parcelable{
	private static final String PARCEL_BUNDLE_KEY_SKILLS = "skills";
	
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
	
	public PTSkillSet(Parcel in) {
		Bundle objectBundle = in.readBundle();
		mSkills = (PTSkill[]) objectBundle.getParcelableArray(PARCEL_BUNDLE_KEY_SKILLS);
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		Bundle objectBundle = new Bundle();
		objectBundle.putParcelableArray(PARCEL_BUNDLE_KEY_SKILLS, mSkills);
		out.writeBundle(objectBundle);
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


