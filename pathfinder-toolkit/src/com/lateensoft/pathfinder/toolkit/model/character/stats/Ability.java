package com.lateensoft.pathfinder.toolkit.model.character.stats;

import com.lateensoft.pathfinder.toolkit.db.repository.Storable;

import android.os.Parcel;
import android.os.Parcelable;


public class Ability implements Parcelable, Storable, Comparable<Ability> {
	
	private static final int[] SCORES = {7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18};
	private static final int[] COSTS = {-4, -2, -1, 0, 1, 2, 3, 5, 7, 10, 13, 17};
	
	public static final int BASE_ABILITY_SCORE = 10;
	
	private int m_abilityKey;
	private int m_score;
	private int m_tempBonus;

	private long m_characterId;
	
	public Ability(int abilityKey, long characterId, int score, int temp) {
		m_abilityKey = abilityKey;
		m_characterId = characterId;
		m_score = score;
		m_tempBonus = temp;
	}
	
	public Ability(int abilityKey, int score, int temp) {
		this(abilityKey, UNSET_ID, score, temp);
	}
	
	public Ability(int abilityKey) {
		this(abilityKey, BASE_ABILITY_SCORE, 0);
	}
	
	public Ability(Parcel in) {
		m_score = in.readInt();
		m_tempBonus = in.readInt();
		m_abilityKey = in.readInt();
		m_characterId = in.readLong();
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(m_score);
		out.writeInt(m_tempBonus);
		out.writeInt(m_abilityKey);
		out.writeLong(m_characterId);
	}
	
	/**
	 * @return The base score for the ability
	 */
	public int getScore() {
		return m_score;
	}
	
	public void setScore(int score) {
		m_score = score;
	}
	
	/**
	 * @return The temporary change to the base score
	 */
	public int getTempBonus() {
		return m_tempBonus;
	}

	public void setTempBonus(int tempBonus) {
		m_tempBonus = tempBonus;
	}

	public int getAbilityModifier() {
		return calculateMod(m_score);
	}
	
	/**
	 * The modifier for the base + temp bonus
	 * @return
	 */
	public int getTempModifier() {
		return calculateMod(m_score + m_tempBonus);
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
	
	/**
	 * @return the cost for the base ability score
	 */
	public int getAbilityPointCost() {
		for(int i = 0; i < SCORES.length; i++) {
			if(m_score == SCORES[i]) {
				return COSTS[i];
			}
		}
		return 0;
	}
	
	public void setAbilityKey(int abilityKey) {
		m_abilityKey = abilityKey;
	}

	public int getAbilityKey() {
		return m_abilityKey;
	}
	
	@Override
	public void setID(long id) {
		m_abilityKey = (int)id;
	}

	/**
	 * same as getAbilityKey()
	 */
	@Override
	public long getID() {
		return m_abilityKey;
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
	
	public static final Parcelable.Creator<Ability> CREATOR = new Parcelable.Creator<Ability>() {
		public Ability createFromParcel(Parcel in) {
			return new Ability(in);
		}
		
		public Ability[] newArray(int size) {
			return new Ability[size];
		}
	};

    @Override
    public int compareTo(Ability another) {
        return this.getAbilityKey() - another.getAbilityKey();
    }
}
