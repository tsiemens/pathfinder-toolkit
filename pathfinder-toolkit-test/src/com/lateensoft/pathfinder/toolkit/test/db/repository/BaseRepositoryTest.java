package com.lateensoft.pathfinder.toolkit.test.db.repository;

import com.lateensoft.pathfinder.toolkit.BaseApplication;
import com.lateensoft.pathfinder.toolkit.db.Database;
import com.lateensoft.pathfinder.toolkit.db.repository.CharacterRepository;
import com.lateensoft.pathfinder.toolkit.model.character.Character;

import android.test.AndroidTestCase;

public class BaseRepositoryTest extends AndroidTestCase {
	protected Database m_db;
	protected final String CHARACTER_NAME = "Joe";
	protected final double CHARACTER_GOLD = 40.0;
	protected long m_characterId;
	protected final int INSERT_FAIL = -1;
	
	@Override
	protected void setUp() {
		m_db = Database.getInstance();
		Character joe = new Character(CHARACTER_NAME, BaseApplication.getAppContext());
		joe.setGold(CHARACTER_GOLD);
		CharacterRepository repo = new CharacterRepository();
		m_characterId = repo.insert(joe);
		assertTrue(m_characterId != INSERT_FAIL);
	}
	
	@Override
	protected void tearDown() throws Exception {
		CharacterRepository repo = new CharacterRepository();
		repo.delete(m_characterId);
		super.tearDown();
	}
}
