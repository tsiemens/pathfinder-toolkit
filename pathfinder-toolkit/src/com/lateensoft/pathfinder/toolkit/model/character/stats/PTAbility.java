package com.lateensoft.pathfinder.toolkit.model.character.stats;

import com.lateensoft.pathfinder.toolkit.db.repository.PTStorable;

import android.os.Parcel;
import android.os.Parcelable;


public class PTAbility implements Parcelable, PTStorable {
	
	private static final int[] SCORES = {7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18};
	private static final int[] COSTS = {-4, -2, -1, 0, 1, 2, 3, 5, 7, 10, 13, 17};
	
	public static final int BASE_ABILITY_SCORE = 10;
	
	private long m_abilityId;
	private int m_score;
	private int m_tempScore;

	private long m_characterId;
	
	public PTAbility(long id, long characterId, int score, int temp) {
		m_abilityId = id;
		m_characterId = characterId;
		m_score = score;
		m_tempScore = temp;
	}
	
	public PTAbility(int id, int score, int temp) {
		this(UNSET_ID, UNSET_ID, score, temp);
	}
	
	public PTAbility(Parcel in) {
		m_score = in.readInt();
		m_tempScore = in.readInt();
		m_abilityId = in.readLong();
		m_characterId = in.readLong();
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(m_score);
		out.writeInt(m_tempScore);
		out.writeLong(m_abilityId);
		out.writeLong(m_characterId);
	}
	
	public int getScore() {
		return m_score;
	}
	
	public void setScore(int score) {
		m_score = score;
	}
	
	public int getTempScore() {
		return m_tempScore;
	}

	public void setTempScore(int tempScore) {
		m_tempScore = tempScore;
	}

	public int getAbilityModifier() {
		return calculateMod(m_score);
	}
	
	public int getTempModifier() {
		return calculateMod(m_tempScore);
	}
	
	private int calculateMod(int score) {
		float mod = (float) ((score - 10)/2.0);
		if (mod < 0) {
			return (int) (mod - 0.5);
		} else {
			return (int) mod;
		}
	}
	
	public void incScore() {
		if ((m_score + 1) > 18) {
			return;
		} else {
			m_score++;
			return;
		}
	}
	
	public void decScore() {
		if ((m_score - 1) < 7) {
			return;
		} else {
			m_score--;
			return;
		}
	}
	
	// Takes a raw ability score
	// Returns the point cost
	public int getAbilityPointCost() {
		for(int i = 0; i < SCORES.length; i++) {
			if(m_score == SCORES[i]) {
				return COSTS[i];
			}
		}
		return 0;
	}
	
	@Override
	public void setID(long id) {
		m_abilityId = id;
	}

	@Override
	public long getID() {
		return m_abilityId;
	}
	
	public void setCharacterID(long id) {
		m_characterId = id;
	}

	public long getCharacterID() {
		return m_characterId;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	public static final Parcelable.Creator<PTAbility> CREATOR = new Parcelable.Creator<PTAbility>() {
		public PTAbility createFromParcel(Parcel in) {
			return new PTAbility(in);
		}
		
		public PTAbility[] newArray(int size) {
			return new PTAbility[size];
		}
	};
}
