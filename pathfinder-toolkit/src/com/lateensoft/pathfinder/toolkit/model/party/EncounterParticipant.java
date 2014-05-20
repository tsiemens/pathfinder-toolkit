package com.lateensoft.pathfinder.toolkit.model.party;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.common.primitives.Ints;
import com.lateensoft.pathfinder.toolkit.model.character.PathfinderCharacter;

/**
 * @author tsiemens
 */
public class EncounterParticipant extends PathfinderCharacter implements Comparable<EncounterParticipant> {

    private int m_initiativeScore;
    private int m_turnOrder;

    protected EncounterParticipant(Builder builder) {
        super(builder);
        m_initiativeScore = builder.initiativeScore;
        m_turnOrder = builder.turnOrder;
    }

    public EncounterParticipant(Parcel in) {
        super(in);
        m_initiativeScore = in.readInt();
        m_turnOrder = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeInt(m_initiativeScore);
        out.writeInt(m_turnOrder);
    }

    public int getInitiativeScore() {
        return m_initiativeScore;
    }

    public void setInitiativeScore(int initiativeScore) {
        m_initiativeScore = initiativeScore;
    }

    public int getTurnOrder() {
        return m_turnOrder;
    }

    public void setTurnOrder(int turnOrder) {
        m_turnOrder = turnOrder;
    }

    @Override
    public int compareTo(EncounterParticipant another) {
        int turnCompared = Ints.compare(this.m_turnOrder, another.m_turnOrder);
        return turnCompared != 0 ? turnCompared :
                -1 * Ints.compare(this.m_initiativeScore, another.m_initiativeScore);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<EncounterParticipant> CREATOR = new Parcelable.Creator<EncounterParticipant>() {
        public EncounterParticipant createFromParcel(Parcel in) {
            return new EncounterParticipant(in);
        }

        public EncounterParticipant[] newArray(int size) {
            return new EncounterParticipant[size];
        }
    };

    @SuppressWarnings("unchecked")
    public static class Builder<T extends Builder> extends PathfinderCharacter.Builder<T> {
        private int initiativeScore = 0;
        private int turnOrder = 0;

        public Builder() {
            super();
        }

        public EncounterParticipant build() {
            return new EncounterParticipant(this);
        }

        public T setInitiativeScore(int initiativeScore) {
            this.initiativeScore = initiativeScore;
            return (T) this;
        }

        public T setTurnOrder(int turnOrder) {
            this.turnOrder = turnOrder;
            return (T) this;
        }
    }
}
