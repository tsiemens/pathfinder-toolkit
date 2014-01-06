package com.lateensoft.pathfinder.toolkit.stats;

import com.lateensoft.pathfinder.toolkit.repository.PTStorable;

import android.os.Parcel;
import android.os.Parcelable;


public class PTAbilityScore implements Parcelable, PTStorable {
	private String mAbility;
	private int mScore;

	private static final int[] SCORES = {7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18};
	private static final int[] COSTS = {-4, -2, -1, 0, 1, 2, 3, 5, 7, 10, 13, 17};
	private long mId;
	private long mCharacterId;
	
	public PTAbilityScore(int id, int characterId, String ability, int score) {
		this(ability, score);
		mId = id;
		mCharacterId = characterId;
	}
	
	public PTAbilityScore(String ability, int score) {
		mAbility = ability;
		mScore = score;
	}
	
	public PTAbilityScore(Parcel in) {
		mAbility = in.readString();
		mScore = in.readInt();
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(mAbility);
		out.writeInt(mScore);
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
	public Long getID() {
		return mId;
	}

	@Override
	public Long getCharacterID() {
		return mCharacterId;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	public static final Parcelable.Creator<PTAbilityScore> CREATOR = new Parcelable.Creator<PTAbilityScore>() {
		public PTAbilityScore createFromParcel(Parcel in) {
			return new PTAbilityScore(in);
		}
		
		public PTAbilityScore[] newArray(int size) {
			return new PTAbilityScore[size];
		}
	};
}
