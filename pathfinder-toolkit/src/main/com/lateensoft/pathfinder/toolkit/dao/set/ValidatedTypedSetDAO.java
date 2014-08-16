package com.lateensoft.pathfinder.toolkit.dao.set;

import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.dao.OwnedGenericDAO;
import com.lateensoft.pathfinder.toolkit.model.character.stats.TypedStat;
import com.lateensoft.pathfinder.toolkit.model.character.stats.ValidatedTypedStatSet;

import java.util.List;

import static com.lateensoft.pathfinder.toolkit.model.character.stats.ValidatedTypedStatSet.CorrectionListener;

public abstract class ValidatedTypedSetDAO<SetID, Set extends ValidatedTypedStatSet<Comp, ?>,
        Comp extends TypedStat<? extends Enum<?>> & Comparable<Comp>,
        CompDAO extends OwnedGenericDAO<SetID, ?, Comp>> {

    public Set findSet(SetID setId) {
        return newSetFromComponents(getComponentDAO().findAllForOwner(setId),
                getCorrectionListener(setId));
    }

    public void add(SetID setId, Set set) throws DataAccessException {
        OwnedGenericDAO<SetID, ?, Comp> componentDAO = getComponentDAO();
        for (Comp component : set) {
            componentDAO.add(setId, component);
        }
    }

    protected abstract Set newSetFromComponents(List<Comp> components, CorrectionListener<Comp> correctionListener);

    public abstract CompDAO getComponentDAO();

    protected abstract DAOCorrectionListener getCorrectionListener(SetID setId);

    protected abstract class DAOCorrectionListener implements CorrectionListener<Comp> {
        private final SetID setId;

        public DAOCorrectionListener(SetID setId) {
            this.setId = setId;
        }

        public SetID getSetId() {
            return setId;
        }

        @Override
        public void onInvalidItemRemoved(Comp removedComponent) {
            try {
                remove(removedComponent);
            } catch (DataAccessException e) {
                handleException(e);
            }
        }

        protected abstract void remove(Comp removedComponent) throws DataAccessException;

        @Override
        public void onMissingItemAdded(Comp addedSkill) {
            try {
                getComponentDAO().add(setId, addedSkill);
            } catch (DataAccessException e) {
                handleException(e);
            }
        }

        private void handleException(Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
