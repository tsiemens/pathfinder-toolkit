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

    @Deprecated // This should not be stored here
    private long m_encounterId;

    protected EncounterParticipant(Builder builder) {
        super(builder);
        m_initiativeScore = builder.initiativeScore;
        m_turnOrder = builder.turnOrder;
        m_encounterId = builder.encounterId;
    }

    public EncounterParticipant(Parcel in) {
        super(in);
        m_encounterId = in.readLong();
        m_initiativeScore = in.readInt();
        m_turnOrder = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeLong(m_encounterId);
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

    @Deprecated
    public long getEncounterId() {
        return m_encounterId;
    }

    @Deprecated
    public void setEncounterId(long encounterId) {
        m_encounterId = encounterId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EncounterParticipant)) return false;
        if (!super.equals(o)) return false;

        EncounterParticipant that = (EncounterParticipant) o;

        if (m_initiativeScore != that.m_initiativeScore) return false;
        if (m_turnOrder != that.m_turnOrder) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + m_initiativeScore;
        result = 31 * result + m_turnOrder;
        return result;
    }

    public static Builder<Builder> builder() {
        return new Builder<Builder>();
    }

    @SuppressWarnings("unchecked")
    public static class Builder<T extends Builder> extends PathfinderCharacter.Builder<T> {
        private int initiativeScore = 0;
        private int turnOrder = 0;
        @Deprecated
        private long encounterId = UNSET_ID;

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

        @Deprecated
        public T setEncounterId(long encounterId) {
            this.encounterId = encounterId;
            return (T) this;
        }
    }
}
