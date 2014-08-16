package com.lateensoft.pathfinder.toolkit.db.dao.table;

import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.model.character.Feat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@Config(manifest=Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class FeatDAOTest extends  CharacterComponentDAOTest {
    private Feat feat1;
    private Feat feat2;
    private FeatDAO dao;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        initAndTestAdd();
    }

    private void initAndTestAdd() throws DataAccessException {
        dao = new FeatDAO(Robolectric.application);

        feat1 = new Feat("A Feat", "description 1");
        feat2 = new Feat("B Feat", "description 2");

        assertTrue(-1 != dao.add(getTestCharacterId(), feat1));
        assertTrue(-1 != dao.add(getTestCharacterId(), feat2));
    }

    @Test
    public void findValid() {
        Feat queried = dao.find(feat1.getId());
        assertEquals(feat1, queried);
    }

    @Test
    public void findInvalid() {
        Feat queried = dao.find(-1L);
        assertNull(queried);
    }

    @Test
    public void updateValid() throws DataAccessException {
        Feat toUpdate = new Feat(feat2.getId(), "sidkjf", "sdfsdf");
        dao.update(getTestCharacterId(), toUpdate);
        Feat updated = dao.find(feat2.getId());
        assertEquals(updated, toUpdate);
    }

    @Test(expected = DataAccessException.class)
    public void updateInvalid() throws DataAccessException {
        Feat toUpdate = new Feat(-1, "sidkjf", "sdfsdf");
        dao.update(getTestCharacterId(), toUpdate);
    }

    @Test
    public void removeValid() throws DataAccessException {
        dao.remove(feat1);
        assertNull(dao.find(feat1.getId()));
    }

    @Test(expected = DataAccessException.class)
    public void removeInvalid() throws DataAccessException {
        dao.remove(feat1);
        dao.remove(feat1);
    }

    @Test
    public void findAllForOwner() {
        List<Feat> queriedFeats = dao.findAllForOwner(getTestCharacterId());
        assertEquals(queriedFeats.get(0), feat1);
        assertEquals(queriedFeats.get(1), feat2);
    }
}
