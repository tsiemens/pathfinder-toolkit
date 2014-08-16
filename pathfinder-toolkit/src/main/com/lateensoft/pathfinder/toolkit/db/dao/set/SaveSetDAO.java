package com.lateensoft.pathfinder.toolkit.db.dao.set;

import android.content.Context;
import com.lateensoft.pathfinder.toolkit.dao.set.WeakValidatedTypedSetDAO;
import com.lateensoft.pathfinder.toolkit.db.dao.table.SaveDAO;
import com.lateensoft.pathfinder.toolkit.model.character.stats.Save;
import com.lateensoft.pathfinder.toolkit.model.character.stats.SaveSet;

import java.util.List;

import static com.lateensoft.pathfinder.toolkit.model.character.stats.ValidatedTypedStatSet.CorrectionListener;

public class SaveSetDAO extends WeakValidatedTypedSetDAO<Long, SaveSet, Save> {

    private final SaveDAO saveDAO;

    public SaveSetDAO(Context context) {
        this.saveDAO = new SaveDAO(context);
    }

    @Override
    protected SaveSet newSetFromComponents(List<Save> components, CorrectionListener<Save> correctionListener) {
        return new SaveSet(components, correctionListener);
    }

    @Override
    public SaveDAO getComponentDAO() {
        return saveDAO;
    }
}
