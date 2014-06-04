package com.lateensoft.pathfinder.toolkit.test.db.repository;

import java.util.List;
import java.util.Map.Entry;

import com.lateensoft.pathfinder.toolkit.db.repository.CharacterRepository;
import com.lateensoft.pathfinder.toolkit.model.IdStringPair;
import com.lateensoft.pathfinder.toolkit.model.character.PathfinderCharacter;

public class CharacterRepositoryTest extends BaseRepositoryTest {
	
	private CharacterRepository m_repo;
	
	private PathfinderCharacter m_char1;
	
	@Override
	public void setUp() {
		super.setUp();
		m_repo = new CharacterRepository();
		
		m_char1 = PathfinderCharacter.newDefaultCharacter("A");
		m_repo.insert(m_char1);
	}
	
	@Override
	protected void tearDown() throws Exception {
		m_repo.delete(m_char1.getID());
		assertTrue (m_repo.query(m_char1.getID()) == null);
		super.tearDown();
	}

	public void testQuery() {
		PathfinderCharacter joe = m_repo.query(m_characterId);
		assertEquals(CHARACTER_GOLD, joe.getGold());
		assertEquals(CHARACTER_NAME, joe.getFluff().getName());
	}
	
	public void testQueryList() {
		List<IdStringPair> names = m_repo.queryIdNameList();
		assertEquals(m_char1.getName(), names.get(0).getValue());
		assertEquals(CHARACTER_NAME, names.get(1).getValue());
	}
}
