package com.lateensoft.pathfinder.toolkit.model.character.stats;

import com.lateensoft.pathfinder.toolkit.db.repository.PTStorable;

import android.os.Parcel;
import android.os.Parcelable;


public class PTAbilityScore implements Parcelable, PTStorable {
	private String m_ability;
	private int m_score;

	private static final int[] SCORES = {7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18};
	private static final int[] COSTS = {-4, -2, -1, 0, 1, 2, 3, 5, 7, 10, 13, 17};
	private long m_id;
	private long m_characterId;
	
	public PTAbilityScore(long id, long characterId, String ability, int score) {
		m_id = id;
		m_characterId = characterId;
		m_ability = ability;
		m_score = score;
	}
	
	public PTAbilityScore(String ability, int score) {
		this(UNSET_ID, UNSET_ID, ability, score);
	}
	
	public PTAbilityScore(Parcel in) {
		m_ability = in.readString();
		m_score = in.readInt();
		m_id = in.readLong();
		m_characterId = in.readLong();
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(m_ability);
		out.writeInt(m_score);
		out.writeLong(m_id);
		out.writeLong(m_characterId);
	}
	
	public int getScore() {
		return m_score;
	}
	
	public String getAbility() {
		return m_ability;
	}
	
	public void setScore(int score) {
		m_score = score;
	}
	
	public void setAbility(String ability) {
		m_ability = ability;
	}
	
	public int getModifier() {
		float temp = (float) ((m_score - 10)/2.0);
		if (temp < 0) {
			return (int) (temp - 0.5);
		} else {
			return (int) temp;
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
		m_id = id;
	}

	@Override
	public long getID() {
		return m_id;
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
	
	public static final Parcelable.Creator<PTAbilityScore> CREATOR = new Parcelable.Creator<PTAbilityScore>() {
		public PTAbilityScore createFromParcel(Parcel in) {
			return new PTAbilityScore(in);
		}
		
		public PTAbilityScore[] newArray(int size) {
			return new PTAbilityScore[size];
		}
	};
}
