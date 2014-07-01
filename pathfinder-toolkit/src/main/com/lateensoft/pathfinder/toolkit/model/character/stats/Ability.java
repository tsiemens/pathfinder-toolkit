package com.lateensoft.pathfinder.toolkit.model.character.stats;

import com.google.common.base.Preconditions;
import com.lateensoft.pathfinder.toolkit.dao.Identifiable;

import android.os.Parcel;
import android.os.Parcelable;
import org.jetbrains.annotations.NotNull;


public class Ability implements TypedStat<AbilityType>, Parcelable, Identifiable, Comparable<Ability> {
    
    private static final int[] SCORES = {7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18};
    private static final int[] COSTS = {-4, -2, -1, 0, 1, 2, 3, 5, 7, 10, 13, 17};
    private static final int MAX_SCORE = 18;
    private static final int MIN_SCORE = 7;
    
    public static final int BASE_ABILITY_SCORE = 10;

    private final AbilityType type;
    private int score;
    private int tempBonus;

    @Deprecated // once the DAOs are implemented, no owned objects will reference the character.
    private long m_characterId;

    @Deprecated
    public Ability(@NotNull AbilityType type, long characterId, int score, int temp) {
        Preconditions.checkNotNull(type);
        this.type = type;
        m_characterId = characterId;
        this.score = score;
        this.tempBonus = temp;
    }
    
    public Ability(@NotNull AbilityType type, int score, int temp) {
        this(type, UNSET_ID, score, temp);
    }
    
    public Ability(@NotNull AbilityType type) {
        this(type, BASE_ABILITY_SCORE, 0);
    }
    
    public Ability(Parcel in) {
        score = in.readInt();
        tempBonus = in.readInt();
        type = AbilityType.forKey(in.readInt());
        m_characterId = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(score);
        out.writeInt(tempBonus);
        out.writeInt(type.getKey());
        out.writeLong(m_characterId);
    }

    @Override
    public AbilityType getType() {
        return type;
    }

    /**
     * @return The base score for the ability
     */
    public int getScore() {
        return score;
    }
    
    public void setScore(int score) {
        this.score = score;
    }
    
    /**
     * @return The temporary change to the base score
     */
    public int getTempBonus() {
        return tempBonus;
    }

    public void setTempBonus(int tempBonus) {
        this.tempBonus = tempBonus;
    }

    public int getAbilityModifier() {
        return calculateModifier(score);
    }
    
    /**
     * The modifier for the base + temp bonus
     */
    public int getTempModifier() {
        return calculateModifier(score + tempBonus);
    }
    
    private int calculateModifier(int score) {
        float mod = (float) ((score - 10)/2.0);
        if (mod < 0) {
            return (int) (mod - 0.5);
        } else {
            return (int) mod;
        }
    }
    
    public void incScore() {
        if (score < MAX_SCORE) {
            score++;
        }
    }
    
    public void decScore() {
        if (score > MIN_SCORE) {
            score--;
        }
    }
    
    public int getAbilityPointCost() {
        for(int i = 0; i < SCORES.length; i++) {
            if(score == SCORES[i]) {
                return COSTS[i];
            }
        }
        return 0;
    }
    
    @Override
    @Deprecated
    public void setId(long id) {
//        type = AbilityType.forKey((int) id);
    }

    @Override
    @Deprecated // The Identifiable interface will not be needed by ability once DAOs are used.
    // It will use the OwnedWeakTableDAO, which does not expect a unique ID
    public long getId() {
        return type.getKey();
    }

    @Deprecated
    public void setCharacterID(long id) {
        m_characterId = id;
    }

    @Deprecated
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
        return this.type.compareTo(another.type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ability)) return false;

        Ability ability = (Ability) o;

        if (type != ability.type) return false;
        if (score != ability.score) return false;
        if (tempBonus != ability.tempBonus) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + score;
        result = 31 * result + tempBonus;
        return result;
    }

    @Override
    public String toString() {
        return type.toString() + "; score:" + score + "; temp:" + tempBonus;
    }
}
