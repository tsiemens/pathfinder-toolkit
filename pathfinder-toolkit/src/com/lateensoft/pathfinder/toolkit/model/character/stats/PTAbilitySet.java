package com.lateensoft.pathfinder.toolkit.model.character.stats;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;



public class PTAbilitySet implements Parcelable{
	@SuppressWarnings("unused")
	private static final String TAG = PTAbilitySet.class.getSimpleName();
	private static final String PARCEL_BUNDLE_KEY_ABILITIES = "abilities";
	
	public static final String[] ABILITY_NAMES = {"Strength", "Dexterity", "Constitution",
		"Intelligence", "Wisdom", "Charisma"};
	static final int BASE_ABILITY_SCORE = 10;
	
	private PTAbilityScore[] m_abilities;
	
	public PTAbilitySet() {
		//Resources res = android.content.res.Resources.getSystem();
		//String[] names = res.getStringArray(R.array.abilities);
		//int base_ability = res.getInteger(R.integer.base_ability_score);
		m_abilities = new PTAbilityScore[ABILITY_NAMES.length];
		
		for(int i = 0; i < ABILITY_NAMES.length; i++) {
			m_abilities[i] = new PTAbilityScore(ABILITY_NAMES[i], BASE_ABILITY_SCORE);
		}
	}
	
	public PTAbilitySet(Parcel in) {
		Bundle objectBundle = in.readBundle();
		m_abilities = (PTAbilityScore[]) objectBundle.getParcelableArray(PARCEL_BUNDLE_KEY_ABILITIES);
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		Bundle objectBundle = new Bundle();
		objectBundle.putParcelableArray(PARCEL_BUNDLE_KEY_ABILITIES, m_abilities);
		out.writeBundle(objectBundle);
	}
	
	// Given a string ability name and a score, sets the 
	// ability score with the matching ability string
	// to have the given score
	public void setScore(String ability, int score) {
		for(int i = 0; i < m_abilities.length; i++) {
			if(ability.equals(m_abilities[i].getAbility())) {
				m_abilities[i].setScore(score);
				return;
			}
		}
	}
	
	public void setScore(int index, int score) {
		m_abilities[index].setScore(score);
	}
	
	public void setScores(int[] scores) {
		if(scores.length != m_abilities.length)
			return;
		
		for(int i = 0; i < m_abilities.length; i++) {
			m_abilities[i].setScore(scores[i]);
		}
	}
	
	// Returns the score for the corresponding ability
	// or zero if such an ability does not exist in the set
	public int getScore(String ability) {
		for(int i = 0; i < m_abilities.length; i++) {
			if(ability.equals(m_abilities[i].getAbility())) {
				return m_abilities[i].getScore();
			}
		}
		return 0;
	}
	
	// Returns an array of strings corresponding to the abilities
	// in the set
	public String[] getAbilities() {
		String[] abilities = new String[m_abilities.length];
		for(int i = 0; i < m_abilities.length; i++) {
			abilities[i] = m_abilities[i].getAbility();
		}
		return abilities;
	}
	
	// Returns an array of scores corresponding to the abilities
	// in the set
	public int[] getScores() {
		int[] scores = new int[m_abilities.length];
		for(int i = 0; i < m_abilities.length; i++) {
			scores[i] = m_abilities[i].getScore();
		}
		return scores;
	}
	
	public PTAbilityScore getAbilityScore(int index) {
		if( index >=0 && index <= m_abilities.length)
			return m_abilities[index];
		return m_abilities[0];
	}
	
	public int getLength(){
		return m_abilities.length;
	}
	
	public void setCharacterID(long id) {
		for (PTAbilityScore ability : m_abilities) {
			ability.setCharacterID(id);
		}
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
