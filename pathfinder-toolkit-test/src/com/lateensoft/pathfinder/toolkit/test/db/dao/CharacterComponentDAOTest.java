package com.lateensoft.pathfinder.toolkit.test.db.dao;

import com.lateensoft.pathfinder.toolkit.db.repository.CharacterRepository;
import com.lateensoft.pathfinder.toolkit.model.character.PathfinderCharacter;

public class CharacterComponentDAOTest extends BaseDAOTest {
	protected final String CHARACTER_NAME = "Test character";
	protected final double CHARACTER_GOLD = 40.0;
	private long testCharacterId;

    private CharacterRepository charRepo;

    @Deprecated
    /** should be DAO*/
    protected CharacterRepository getCharRepo() {
        return charRepo;
    }

    protected long getTestCharacterId() {
        return testCharacterId;
    }

	@Override
	protected void setUp() throws Exception {
        super.setUp();
        charRepo = new CharacterRepository();
		PathfinderCharacter testCharacter = PathfinderCharacter.newDefaultCharacter(CHARACTER_NAME);
		testCharacter.setGold(CHARACTER_GOLD);
		testCharacterId = getCharRepo().insert(testCharacter);
		assertTrue(testCharacterId != INSERT_FAIL);
	}
	
	@Override
	protected void tearDown() throws Exception {
		getCharRepo().delete(testCharacterId);
		super.tearDown();
	}
}
