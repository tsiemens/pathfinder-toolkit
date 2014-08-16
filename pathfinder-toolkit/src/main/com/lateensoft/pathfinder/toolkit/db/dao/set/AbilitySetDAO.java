package com.lateensoft.pathfinder.toolkit.db.dao.set;

import android.content.Context;
import com.lateensoft.pathfinder.toolkit.dao.set.WeakValidatedTypedSetDAO;
import com.lateensoft.pathfinder.toolkit.db.dao.table.AbilityDAO;
import com.lateensoft.pathfinder.toolkit.model.character.stats.Ability;
import com.lateensoft.pathfinder.toolkit.model.character.stats.AbilitySet;

import java.util.List;

import static com.lateensoft.pathfinder.toolkit.model.character.stats.ValidatedTypedStatSet.CorrectionListener;

public class AbilitySetDAO extends WeakValidatedTypedSetDAO<Long, AbilitySet, Ability> {

    private final AbilityDAO abilityDAO;

    public AbilitySetDAO(Context context) {
        this.abilityDAO = new AbilityDAO(context);
    }

    @Override
    protected AbilitySet newSetFromComponents(List<Ability> components, CorrectionListener<Ability> correctionListener) {
        return new AbilitySet(components, correctionListener);
    }

    @Override
    public AbilityDAO getComponentDAO() {
        return abilityDAO;
    }
}
