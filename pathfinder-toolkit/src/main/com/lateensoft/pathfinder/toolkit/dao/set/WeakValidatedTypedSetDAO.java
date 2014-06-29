package com.lateensoft.pathfinder.toolkit.dao.set;

import com.lateensoft.pathfinder.toolkit.dao.*;
import com.lateensoft.pathfinder.toolkit.model.character.stats.TypedStat;
import com.lateensoft.pathfinder.toolkit.model.character.stats.ValidatedTypedStatSet;

public abstract class WeakValidatedTypedSetDAO<SetID, Set extends ValidatedTypedStatSet<Comp, ?>,
        Comp extends TypedStat<? extends Enum<?>> & Comparable<Comp> & Identifiable>

        extends ValidatedTypedSetDAO<SetID, Set, Comp, OwnedWeakGenericDAO<SetID, ?, Comp>> {

    @Override
    protected DAOCorrectionListener getCorrectionListener(SetID setId) {
        return new DAOCorrectionListener(setId) {
            @Override
            protected void remove(Comp removedComponent) throws DataAccessException {
                getComponentDAO().remove(getSetId(), removedComponent);
            }
        };
    }
}
