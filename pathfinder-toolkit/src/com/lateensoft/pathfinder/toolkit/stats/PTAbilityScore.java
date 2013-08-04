package com.lateensoft.pathfinder.toolkit.stats;

import com.lateensoft.pathfinder.toolkit.repository.PTStorable;


public class PTAbilityScore implements PTStorable {
	private String mAbility;
	private int mScore;
	private static final String TAG = "PTAbilityScore";
	private static final int[] SCORES = {7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18};
	private static final int[] COSTS = {-4, -2, -1, 0, 1, 2, 3, 5, 7, 10, 13, 17};
	private int mId;
	private int mCharacterId;
	
	public PTAbilityScore(int id, int characterId, String ability, int score) {
		this(ability, score);
		mId = id;
		mCharacterId = characterId;
	}
	
	public PTAbilityScore(String ability, int score) {
		mAbility = ability;
		mScore = score;
	}
	
	public int getScore() {
		return mScore;
	}
	
	public String getAbility() {
		return mAbility;
	}
	
	public void setScore(int score) {
		mScore = score;
	}
	
	public void setAbility(String ability) {
		mAbility = ability;
	}
	
	public int getModifier() {
		float temp = (float) ((mScore - 10)/2.0);
		if (temp < 0) {
			return (int) (temp - 0.5);
		} else {
			return (int) temp;
		}
	}
	
	public void incScore() {
		if ((mScore + 1) > 18) {
			return;
		} else {
			mScore++;
			return;
		}
	}
	
	public void decScore() {
		if ((mScore - 1) < 7) {
			return;
		} else {
			mScore--;
			return;
		}
	}
	
	// Takes a raw ability score
	// Returns the point cost
	public int getAbilityPointCost() {
		for(int i = 0; i < SCORES.length; i++) {
			if(mScore == SCORES[i]) {
				return COSTS[i];
			}
		}
		return 0;
	}

	@Override
	public int getID() {
		return mId;
	}

	@Override
	public int getCharacterID() {
		return mCharacterId;
	}
}
