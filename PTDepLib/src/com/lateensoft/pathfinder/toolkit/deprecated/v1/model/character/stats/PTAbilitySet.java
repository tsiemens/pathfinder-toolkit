package com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.stats;

public class PTAbilitySet {
	private PTAbilityScore[] mAbilities;
	static final String[] ABILITY_NAMES = {"Strength", "Dexterity", "Constitution",
		"Intelligence", "Wisdom", "Charisma"};
	static final int BASE_ABILITY_SCORE = 10;
	
	public PTAbilitySet() {
		//Resources res = android.content.res.Resources.getSystem();
		//String[] names = res.getStringArray(R.array.abilities);
		//int base_ability = res.getInteger(R.integer.base_ability_score);
		mAbilities = new PTAbilityScore[ABILITY_NAMES.length];
		
		for(int i = 0; i < ABILITY_NAMES.length; i++) {
			mAbilities[i] = new PTAbilityScore(ABILITY_NAMES[i], BASE_ABILITY_SCORE);
		}
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
}
