package com.lateensoft.pathfinder.toolkit.db.dao;

import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.db.dao.table.CharacterDAO;
import com.lateensoft.pathfinder.toolkit.db.dao.table.CharacterModelDAO;
import com.lateensoft.pathfinder.toolkit.model.character.Character;
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
public class CharacterModelDAOTest extends  CharacterComponentDAOTest {
    private Character Character1;
    private CharacterModelDAO dao;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        initAndTestAdd();
    }

    private void initAndTestAdd() throws DataAccessException {
        dao = new CharacterModelDAO(Robolectric.application);

        Character1 = new Character("A Character", "description 1");
        Character2 = new Character("B Character", "description 2");

        assertTrue(-1 != dao.add(getTestCharacterId(), Character1));
        assertTrue(-1 != dao.add(getTestCharacterId(), Character2));
    }

    @Test
    public void findValid() {
        Character queried = dao.find(Character1.getId());
        assertEquals(Character1, queried);
    }

    @Test
    public void findInvalid() {
        Character queried = dao.find(-1L);
        assertNull(queried);
    }

    @Test
    public void updateValid() throws DataAccessException {
        Character toUpdate = new Character(Character2.getId(), -1, "sidkjf", "sdfsdf");
        dao.update(getTestCharacterId(), toUpdate);
        Character updated = dao.find(Character2.getId());
        assertEquals(updated, toUpdate);
    }

    @Test(expected = DataAccessException.class)
    public void updateInvalid() throws DataAccessException {
        Character toUpdate = new Character(-1, -1, "sidkjf", "sdfsdf");
        dao.update(getTestCharacterId(), toUpdate);
    }

    @Test
    public void removeValid() throws DataAccessException {
        dao.remove(Character1);
        assertNull(dao.find(Character1.getId()));
    }

    @Test(expected = DataAccessException.class)
    public void removeInvalid() throws DataAccessException {
        dao.remove(Character1);
        dao.remove(Character1);
    }

    @Test
    public void findAllForOwner() {
        List<Character> queriedCharacters = dao.findAllForOwner(getTestCharacterId());
        assertEquals(queriedCharacters.get(0), Character1);
        assertEquals(queriedCharacters.get(1), Character2);
    }
}
