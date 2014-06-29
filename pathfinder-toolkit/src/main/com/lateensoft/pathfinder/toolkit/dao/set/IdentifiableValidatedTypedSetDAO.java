package com.lateensoft.pathfinder.toolkit.dao.set;

import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.dao.Identifiable;
import com.lateensoft.pathfinder.toolkit.dao.IdentifiableGenericDAO;
import com.lateensoft.pathfinder.toolkit.dao.OwnedGenericDAO;
import com.lateensoft.pathfinder.toolkit.model.character.stats.TypedStat;
import com.lateensoft.pathfinder.toolkit.model.character.stats.ValidatedTypedStatSet;

public abstract class IdentifiableValidatedTypedSetDAO<SetID, Set extends ValidatedTypedStatSet<Comp, ?>,
        Comp extends TypedStat<? extends Enum<?>> & Comparable<Comp> & Identifiable,
        CompDAO extends OwnedGenericDAO<SetID, ?, Comp> & IdentifiableGenericDAO<Comp>>

        extends ValidatedTypedSetDAO<SetID, Set, Comp, CompDAO> {

    @Override
    protected DAOCorrectionListener getCorrectionListener(final SetID setId) {
        return new DAOCorrectionListener(setId) {
            @Override
            protected void remove(Comp removedComponent) throws DataAccessException {
                getComponentDAO().remove(removedComponent);
            }
        };
    }
}
