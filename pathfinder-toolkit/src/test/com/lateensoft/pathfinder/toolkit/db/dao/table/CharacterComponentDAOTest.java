package com.lateensoft.pathfinder.toolkit.db.dao.table;

import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.db.BaseDatabaseTest;
import com.lateensoft.pathfinder.toolkit.model.character.PathfinderCharacter;
import org.junit.After;
import org.junit.Assert;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;

@Config(manifest=Config.NONE)
public abstract class CharacterComponentDAOTest extends BaseDatabaseTest {
    protected final String CHARACTER_NAME = "Test character";
    protected final double CHARACTER_GOLD = 40.0;

    private PathfinderCharacter testCharacter;
    private CharacterModelDAO charDao;

    protected long getTestCharacterId() {
        return testCharacter.getId();
    }

    protected PathfinderCharacter getTestCharacter() {
        return testCharacter;
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        charDao = new CharacterModelDAO(Robolectric.application);
        testCharacter = PathfinderCharacter.newDefaultCharacter(CHARACTER_NAME);
        testCharacter.setGold(CHARACTER_GOLD);
        long testCharacterId = charDao.add(testCharacter);
        Assert.assertTrue(testCharacterId != -1);
    }

    @After
    public void tearDown() throws DataAccessException {
        charDao.remove(testCharacter);
    }
}
