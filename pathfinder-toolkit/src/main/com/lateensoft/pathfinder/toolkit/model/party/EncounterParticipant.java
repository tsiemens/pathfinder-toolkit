package com.lateensoft.pathfinder.toolkit.model.party;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.common.primitives.Ints;
import com.lateensoft.pathfinder.toolkit.model.character.PathfinderCharacter;
import org.jetbrains.annotations.NotNull;

/**
 * @author tsiemens
 */
public class EncounterParticipant extends PathfinderCharacter implements Comparable<EncounterParticipant> {

    private int initiativeScore;
    private int turnOrder;

    public EncounterParticipant(PathfinderCharacter character) {
        super(character);
        initiativeScore = 0;
        turnOrder = 0;
    }

    protected EncounterParticipant(Builder builder) {
        super(builder);
        initiativeScore = builder.initiativeScore;
        turnOrder = builder.turnOrder;
    }

    public EncounterParticipant(Parcel in) {
        super(in);
        initiativeScore = in.readInt();
        turnOrder = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeInt(initiativeScore);
        out.writeInt(turnOrder);
    }

    public int getInitiativeScore() {
        return initiativeScore;
    }

    public void setInitiativeScore(int initiativeScore) {
        this.initiativeScore = initiativeScore;
    }

    public int getTurnOrder() {
        return turnOrder;
    }

    public void setTurnOrder(int turnOrder) {
        this.turnOrder = turnOrder;
    }

    @Override
    public int compareTo(@NotNull EncounterParticipant another) {
        int turnCompared = Ints.compare(this.turnOrder, another.turnOrder);
        return turnCompared != 0 ? turnCompared :
                -1 * Ints.compare(this.initiativeScore, another.initiativeScore);
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

        if (initiativeScore != that.initiativeScore) return false;
        if (turnOrder != that.turnOrder) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + initiativeScore;
        result = 31 * result + turnOrder;
        return result;
    }

    public static Builder<Builder> builder() {
        return new Builder<Builder>();
    }

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
