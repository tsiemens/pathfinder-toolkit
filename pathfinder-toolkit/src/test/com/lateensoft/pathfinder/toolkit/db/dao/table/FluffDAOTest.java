package com.lateensoft.pathfinder.toolkit.db.dao.table;

import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.model.character.FluffInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

@Config(manifest=Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class FluffDAOTest extends CharacterComponentDAOTest {
    private FluffInfo fluff;
    private FluffDAO dao;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        initAndTestValidFindAndUpdate();
    }

    private void initAndTestValidFindAndUpdate() throws DataAccessException {
        dao = new FluffDAO(Robolectric.application);

        fluff = dao.find(getTestCharacterId());
        fluff.setAlignment("Chaotic Evil");
        fluff.setXP("1000");
        fluff.setNextLevelXP("45");
        fluff.setPlayerClass("Rogue");
        fluff.setRace("Human");
        fluff.setDeity("Glob");
        fluff.setLevel("20");
        fluff.setSize("med");
        fluff.setGender("M");
        fluff.setHeight("5'11\"");
        fluff.setWeight("150 lbs");
        fluff.setEyes("Red");
        fluff.setHair("None");
        fluff.setLanguages("Demonic");
        fluff.setDescription("Pretty much worse than Sauron");

        dao.update(getTestCharacterId(), fluff);
        assertEquals(fluff, dao.find(getTestCharacterId()));
    }

    @Test
    public void findInvalid() {
        assertNull(dao.find(-1L));
    }

    @Test(expected = DataAccessException.class)
    public void addInvalid() throws DataAccessException {
        dao.add(getTestCharacterId(), fluff);
    }

    @Test(expected = DataAccessException.class)
    public void updateInvalid() throws DataAccessException {
        dao.update(-1L, fluff);
    }
}
