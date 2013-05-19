package com.lateensoft.pathfinder.toolkit.stats;


public class PTAbilityScore {
	private String mAbility;
	private int mScore;
	private static final String TAG = "PTAbilityScore";
	
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
		if (mScore <= -7) {
			return -3;
		} else if (mScore == 8) {
			return -2;
		} else if (mScore == 9) {
			return -1;
		} else if (mScore <= 13) {
			return mScore - 10;
		} else if (mScore <= 15) {
			return (2 * (mScore - 14)) + 5;
		} else if (mScore <= 17) {
			return (3 * (mScore - 16)) + 10;
		} else {
			return 17;
		}
		
	}
}
