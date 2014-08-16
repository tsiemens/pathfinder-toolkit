package com.lateensoft.pathfinder.toolkit.db.dao.set;

import android.content.Context;
import com.lateensoft.pathfinder.toolkit.dao.set.IdentifiableValidatedTypedSetDAO;
import com.lateensoft.pathfinder.toolkit.db.dao.table.SkillDAO;
import com.lateensoft.pathfinder.toolkit.model.character.stats.Skill;
import com.lateensoft.pathfinder.toolkit.model.character.stats.SkillSet;

import java.util.List;

import static com.lateensoft.pathfinder.toolkit.model.character.stats.ValidatedTypedStatSet.CorrectionListener;

public class SkillSetDAO extends IdentifiableValidatedTypedSetDAO<Long, SkillSet, Skill, SkillDAO> {

    private final SkillDAO skillDAO;

    public SkillSetDAO(Context context) {
        this.skillDAO = new SkillDAO(context);
    }

    @Override
    protected SkillSet newSetFromComponents(List<Skill> components, CorrectionListener<Skill> correctionListener) {
        return new SkillSet(components, correctionListener);
    }

    @Override
    public SkillDAO getComponentDAO() {
        return skillDAO;
    }
}
