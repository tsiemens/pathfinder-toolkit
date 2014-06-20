package com.lateensoft.pathfinder.toolkit.db.dao;

import com.lateensoft.pathfinder.toolkit.db.repository.CharacterRepository;
import com.lateensoft.pathfinder.toolkit.model.character.PathfinderCharacter;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;

public abstract class CharacterComponentDAOTest extends BaseDAOTest {
	protected final String CHARACTER_NAME = "Test character";
	protected final double CHARACTER_GOLD = 40.0;

	private static long testCharacterId;
    private static CharacterRepository charRepo;

    @Deprecated
    /** should be DAO*/
    protected CharacterRepository getCharRepo() {
        return charRepo;
    }

    protected long getTestCharacterId() {
        return testCharacterId;
    }

	@Override
	public void setUp() throws Exception {
        super.setUp();
        if (charRepo == null) {
            charRepo = new CharacterRepository();
            PathfinderCharacter testCharacter = PathfinderCharacter.newDefaultCharacter(CHARACTER_NAME);
            testCharacter.setGold(CHARACTER_GOLD);
            testCharacterId = getCharRepo().insert(testCharacter);
            Assert.assertTrue(testCharacterId != -1);
        }
	}

    @After
    public void tearDown() {
        // Deletes all content from the used table
        getDatabase().delete(getDAO().getTable().getName(), null);
    }

	@AfterClass
	public static void tearDownClass() {
		charRepo.delete(testCharacterId);
        charRepo = null;
	}

    protected abstract GenericTableDAO getDAO();
}
