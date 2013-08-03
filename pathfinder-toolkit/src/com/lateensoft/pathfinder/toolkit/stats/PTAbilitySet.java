package com.lateensoft.pathfinder.toolkit.stats;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;



public class PTAbilitySet implements Parcelable{
	private static final String TAG = PTAbilitySet.class.getSimpleName();
	private static final String PARCEL_BUNDLE_KEY_ABILITIES = "abilities";
	
	public static final String[] ABILITY_NAMES = {"Strength", "Dexterity", "Constitution",
		"Intelligence", "Wisdom", "Charisma"};
	static final int BASE_ABILITY_SCORE = 10;
	
	private PTAbilityScore[] mAbilities;
	
	public PTAbilitySet() {
		//Resources res = android.content.res.Resources.getSystem();
		//String[] names = res.getStringArray(R.array.abilities);
		//int base_ability = res.getInteger(R.integer.base_ability_score);
		mAbilities = new PTAbilityScore[ABILITY_NAMES.length];
		
		for(int i = 0; i < ABILITY_NAMES.length; i++) {
			mAbilities[i] = new PTAbilityScore(ABILITY_NAMES[i], BASE_ABILITY_SCORE);
		}
	}
	
	public PTAbilitySet(Parcel in) {
		Bundle objectBundle = in.readBundle();
		mAbilities = (PTAbilityScore[]) objectBundle.getParcelableArray(PARCEL_BUNDLE_KEY_ABILITIES);
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		Bundle objectBundle = new Bundle();
		objectBundle.putParcelableArray(PARCEL_BUNDLE_KEY_ABILITIES, mAbilities);
		out.writeBundle(objectBundle);
	}
	
	// Given a string ability name and a score, sets the 
	// ability score with the matching ability string
	// to have the given score
	public void setScore(String ability, int score) {
		for(int i = 0; i < mAbilities.length; i++) {
			if(ability.equals(mAbilities[i].getAbility())) {
				mAbilities[i].setScore(score);
				return;
			}
		}
	}
	
	public void setScore(int index, int score) {
		mAbilities[index].setScore(score);
	}
	
	public void setScores(int[] scores) {
		if(scores.length != mAbilities.length)
			return;
		
		for(int i = 0; i < mAbilities.length; i++) {
			mAbilities[i].setScore(scores[i]);
		}
	}
	
	// Returns the score for the corresponding ability
	// or zero if such an ability does not exist in the set
	public int getScore(String ability) {
		for(int i = 0; i < mAbilities.length; i++) {
			if(ability.equals(mAbilities[i].getAbility())) {
				return mAbilities[i].getScore();
			}
		}
		return 0;
	}
	
	// Returns an array of strings corresponding to the abilities
	// in the set
	public String[] getAbilities() {
		String[] abilities = new String[mAbilities.length];
		for(int i = 0; i < mAbilities.length; i++) {
			abilities[i] = mAbilities[i].getAbility();
		}
		return abilities;
	}
	
	// Returns an array of scores corresponding to the abilities
	// in the set
	public int[] getScores() {
		int[] scores = new int[mAbilities.length];
		for(int i = 0; i < mAbilities.length; i++) {
			scores[i] = mAbilities[i].getScore();
		}
		return scores;
	}
	
	public PTAbilityScore getAbilityScore(int index) {
		if( index >=0 && index <= mAbilities.length)
			return mAbilities[index];
		return mAbilities[0];
	}
	
	public int getLength(){
		return mAbilities.length;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	public static final Parcelable.Creator<PTAbilitySet> CREATOR = new Parcelable.Creator<PTAbilitySet>() {
		public PTAbilitySet createFromParcel(Parcel in) {
			return new PTAbilitySet(in);
		}
		
		public PTAbilitySet[] newArray(int size) {
			return new PTAbilitySet[size];
		}
	};
}
