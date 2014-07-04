package com.lateensoft.pathfinder.toolkit.db.dao;

import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.db.dao.table.SkillDAO;
import com.lateensoft.pathfinder.toolkit.model.character.stats.AbilityType;
import com.lateensoft.pathfinder.toolkit.model.character.stats.Skill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@Config(manifest=Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class SkillDAOTest extends CharacterComponentDAOTest {

    private Skill skill;
    private SkillDAO dao;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        initAndTestValidFindAndUpdate();
    }

    private void initAndTestValidFindAndUpdate() throws DataAccessException {
        dao = new SkillDAO(Robolectric.application);
        skill = dao.findAllForOwner(getTestCharacterId()).get(3);
        skill.setMiscMod(8);
        skill.setAbility(AbilityType.CHA);
        skill.setRank(5);
        skill.setClassSkill(true);
        skill.setSubType("sub");
        dao.update(getTestCharacterId(), skill);
        assertEquals(skill, dao.find(skill.getId()));
    }

    @Test
    public void findInvalid() {
        assertNull(dao.find(-1L));
    }

    @Test(expected = DataAccessException.class)
    public void addInvalid() throws DataAccessException {
        dao.add(getTestCharacterId(), skill);
    }

    @Test(expected = DataAccessException.class)
    public void updateInvalid() throws DataAccessException {
        skill.setId(-1L);
        dao.update(-1L, skill);
    }

    @Test
    public void removeValid() throws DataAccessException {
        dao.remove(skill);
        assertNull(dao.find(skill.getId()));
    }

    @Test(expected = DataAccessException.class)
    public void removeInvalid() throws DataAccessException {
        dao.remove(skill);
        dao.remove(skill);
    }
}
