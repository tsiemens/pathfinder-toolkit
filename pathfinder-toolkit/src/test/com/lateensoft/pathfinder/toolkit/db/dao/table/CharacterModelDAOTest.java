package com.lateensoft.pathfinder.toolkit.db.dao.table;

import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.model.character.PathfinderCharacter;
import com.lateensoft.pathfinder.toolkit.util.CharacterUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@Config(manifest=Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class CharacterModelDAOTest extends  CharacterComponentDAOTest {
    private PathfinderCharacter character;
    private CharacterModelDAO dao;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        initAndTestAdd();
    }

    private void initAndTestAdd() throws DataAccessException {
        dao = new CharacterModelDAO(Robolectric.application);

        character = CharacterUtils.buildTestCharacter();

        assertTrue(-1 != dao.add(character));
    }

    @Test
    public void findValid() {
        PathfinderCharacter queried = dao.find(character.getId());
        assertEquals(character, queried);
    }

    @Test
    public void findInvalid() {
        PathfinderCharacter queried = dao.find(-1L);
        assertNull(queried);
    }

    @Test
    public void updateValid() throws DataAccessException {
        character.setGold(13440);
        character.setName("new name");
        dao.update(character);
        PathfinderCharacter updated = dao.find(character.getId());
        assertEquals(character, updated);
    }

    @Test(expected = DataAccessException.class)
    public void updateInvalid() throws DataAccessException {
        character.setId(-1L);
        dao.update(character);
    }

    @Test
    public void removeValid() throws DataAccessException {
        dao.remove(character);
        assertNull(dao.find(character.getId()));
    }

    @Test(expected = DataAccessException.class)
    public void removeInvalid() throws DataAccessException {
        dao.remove(character);
        dao.remove(character);
    }
}
