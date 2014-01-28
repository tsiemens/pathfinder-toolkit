package com.lateensoft.pathfinder.toolkit.test.db.repository;

import java.util.List;
import java.util.Map.Entry;

import com.lateensoft.pathfinder.toolkit.db.repository.PTCharacterRepository;
import com.lateensoft.pathfinder.toolkit.model.character.PTCharacter;

public class PTCharacterRepositoryTest extends PTBaseRepositoryTest {
	
	private PTCharacterRepository m_repo;
	
	private PTCharacter m_char1;
	
	@Override
	public void setUp() {
		super.setUp();
		m_repo = new PTCharacterRepository();
		
		m_char1 = new PTCharacter("A", getContext());
		m_repo.insert(m_char1);
	}
	
	@Override
	protected void tearDown() throws Exception {
		m_repo.delete(m_char1.getID());
		assertTrue (m_repo.query(m_char1.getID()) == null);
		super.tearDown();
	}

	public void testQuery() {
		PTCharacter joe = m_repo.query(m_characterId);
		assertEquals(CHARACTER_GOLD, joe.getGold());
		assertEquals(CHARACTER_NAME, joe.getFluff().getName());
	}
	
	public void testQueryList() {
		List<Entry<Long, String>> names = m_repo.queryList();
		assertEquals(m_char1.getName(), names.get(0).getValue());
		assertEquals(CHARACTER_NAME, names.get(1).getValue());
	}
}
