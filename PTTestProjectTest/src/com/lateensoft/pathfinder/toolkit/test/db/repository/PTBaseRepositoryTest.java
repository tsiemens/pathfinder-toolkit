package com.lateensoft.pathfinder.toolkit.test.db.repository;

import com.lateensoft.pathfinder.toolkit.PTBaseApplication;
import com.lateensoft.pathfinder.toolkit.db.PTDatabase;
import com.lateensoft.pathfinder.toolkit.db.repository.PTCharacterRepository;
import com.lateensoft.pathfinder.toolkit.model.character.PTCharacter;

import android.test.AndroidTestCase;

public class PTBaseRepositoryTest extends AndroidTestCase {
	protected PTDatabase m_db;
	protected final String CHARACTER_NAME = "Joe";
	protected final double CHARACTER_GOLD = 40.0;
	protected long m_characterId;
	protected final int INSERT_FAIL = -1;
	
	protected void setUp() {
		m_db = PTDatabase.getInstance();
		PTCharacter joe = new PTCharacter(CHARACTER_NAME, PTBaseApplication.getAppContext());
		joe.setGold(CHARACTER_GOLD);
		PTCharacterRepository repo = new PTCharacterRepository();
		m_characterId = repo.insert(joe);
		assertTrue(m_characterId != INSERT_FAIL);
	}
	
	protected void tearDown() throws Exception {
		PTCharacterRepository repo = new PTCharacterRepository();
		repo.delete(m_characterId);
		super.tearDown();
	}
}
