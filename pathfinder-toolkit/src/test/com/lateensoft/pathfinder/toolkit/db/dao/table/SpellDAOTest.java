package com.lateensoft.pathfinder.toolkit.db.dao.table;

import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.model.character.Spell;
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
public class SpellDAOTest extends  CharacterComponentDAOTest {
    private Spell spell1;
    private Spell spell2;
    private SpellDAO dao;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        initAndTestAdd();
    }

    private void initAndTestAdd() throws DataAccessException {
        dao = new SpellDAO(Robolectric.application);

        spell1 = new Spell("A Spell", 1, 3, "description 1");
        spell2 = new Spell("B Spell", 5, -1, "description 2");

        assertTrue(-1 != dao.add(getTestCharacterId(), spell1));
        assertTrue(-1 != dao.add(getTestCharacterId(), spell2));
    }

    @Test
    public void findValid() {
        Spell queried = dao.find(spell1.getId());
        assertEquals(spell1, queried);
    }

    @Test
    public void findInvalid() {
        Spell queried = dao.find(-1L);
        assertNull(queried);
    }

    @Test
    public void updateValid() throws DataAccessException {
        Spell toUpdate = new Spell(spell2.getId(), "sidkjf", 5, 7000, "sdfsdf");
        dao.update(getTestCharacterId(), toUpdate);
        Spell updated = dao.find(spell2.getId());
        assertEquals(updated, toUpdate);
    }

    @Test(expected = DataAccessException.class)
    public void updateInvalid() throws DataAccessException {
        Spell toUpdate = new Spell(-1, "sidkjf", 6, 9, "sdfsdf");
        dao.update(getTestCharacterId(), toUpdate);
    }

    @Test
    public void removeValid() throws DataAccessException {
        dao.remove(spell1);
        assertNull(dao.find(spell1.getId()));
    }

    @Test(expected = DataAccessException.class)
    public void removeInvalid() throws DataAccessException {
        dao.remove(spell1);
        dao.remove(spell1);
    }

    @Test
    public void findAllForOwner() {
        List<Spell> queriedSpells = dao.findAllForOwner(getTestCharacterId());
        assertEquals(queriedSpells.get(0), spell1);
        assertEquals(queriedSpells.get(1), spell2);
    }
}
