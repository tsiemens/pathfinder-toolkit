package com.lateensoft.pathfinder.toolkit.db.dao;

import com.lateensoft.pathfinder.toolkit.db.BaseDatabaseTest;
import com.lateensoft.pathfinder.toolkit.db.repository.CharacterRepository;
import com.lateensoft.pathfinder.toolkit.model.character.PathfinderCharacter;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;

public abstract class CharacterComponentDAOTest extends BaseDatabaseTest {
	protected final String CHARACTER_NAME = "Test character";
	protected final double CHARACTER_GOLD = 40.0;

    private static PathfinderCharacter testCharacter;
    private static CharacterRepository charRepo;

    @Deprecated
    /** TODO should be DAO*/
    protected CharacterRepository getCharRepo() {
        return charRepo;
    }

    protected long getTestCharacterId() {
        return testCharacter.getId();
    }

    protected PathfinderCharacter getTestCharacter() {
        return testCharacter;
    }

	@Override
	public void setUp() throws Exception {
        super.setUp();
        charRepo = new CharacterRepository();
        testCharacter = PathfinderCharacter.newDefaultCharacter(CHARACTER_NAME);
        testCharacter.setGold(CHARACTER_GOLD);
        long testCharacterId = getCharRepo().insert(testCharacter);
        Assert.assertTrue(testCharacterId != -1);
	}

    @After
    public void tearDown() {
        charRepo.delete(testCharacter);
        charRepo = null;
    }
}
