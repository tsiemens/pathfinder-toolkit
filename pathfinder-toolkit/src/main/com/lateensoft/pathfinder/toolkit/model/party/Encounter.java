package com.lateensoft.pathfinder.toolkit.model.party;

import com.google.common.base.Preconditions;
import com.lateensoft.pathfinder.toolkit.dao.Identifiable;
import com.lateensoft.pathfinder.toolkit.model.NamedList;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class Encounter<T> extends NamedList<T> {

    private boolean isInCombat = false;
    private @Nullable T currentTurn;

    public Encounter(String name) {
        super(name);
    }

    public Encounter(String name, Collection<T> participants) {
        super(name, participants);
    }

    public Encounter(long id, String name, Collection<T> participants) {
        super(id, name, participants);
    }

    public boolean isInCombat() {
        return isInCombat;
    }

    public void setInCombat(boolean isInCombat) {
        this.isInCombat = isInCombat;
    }

    @Nullable
    public T getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(@Nullable T currentTurn) {
        Preconditions.checkArgument(currentTurn == null || this.contains(currentTurn));
        this.currentTurn = currentTurn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Encounter)) return false;
        if (!super.equals(o)) return false;

        Encounter encounter = (Encounter) o;

        if (isInCombat != encounter.isInCombat) return false;
        if (currentTurn != null ? !currentTurn.equals(encounter.currentTurn) : encounter.currentTurn != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (isInCombat ? 1 : 0);
        result = 31 * result + (currentTurn != null ? currentTurn.hashCode() : 0);
        return result;
    }
}
