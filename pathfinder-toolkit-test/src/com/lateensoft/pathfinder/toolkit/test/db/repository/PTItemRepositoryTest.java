package com.lateensoft.pathfinder.toolkit.test.db.repository;

import com.lateensoft.pathfinder.toolkit.db.repository.PTItemRepository;
import com.lateensoft.pathfinder.toolkit.model.character.items.PTItem;

import java.util.List;

public class PTItemRepositoryTest extends PTBaseRepositoryTest {	
	private PTItem m_item1;
	private PTItem m_item2;
	private PTItemRepository m_repo;
	
	@Override
	public void setUp() {
		super.setUp();
		m_repo = new PTItemRepository();
		
		m_item1 = new PTItem(m_characterId, "Cauldron", 5.0, 1, false);
		m_item2 = new PTItem(m_characterId, "Wands", 1.0, 2, true);
		
		m_repo.insert(m_item1);
		m_repo.insert(m_item2);
	}
	
	public void testInsert() {
		PTItem insertItem = new PTItem(m_characterId, "Different Wands", 1.0, 2, true);
		long id = m_repo.insert(insertItem);
		assertTrue(id != INSERT_FAIL);
	}
	
	public void testQuery() {
		PTItem queried = m_repo.query(m_item1.getID());
		
		assertEquals(queried, m_item1);
	}
	
	public void testUpdate() {
		PTItem toUpdate = new PTItem(m_item2.getID(), m_characterId, "Big Wand", 2.0, 1, false);
		
		m_repo.update(toUpdate);
		PTItem updated = m_repo.query(toUpdate.getID());
		assertEquals(toUpdate, updated);
	}
	
	public void testDelete() {
		m_repo.delete(m_item1.getID());
		assertTrue (m_repo.query(m_item1.getID()) == null);
	}
	
	public void testQuerySet() {
		List<PTItem> queriedItems = m_repo.querySet(m_characterId);
		assertEquals(queriedItems.get(0), m_item1);
		assertEquals(queriedItems.get(1), m_item2);
	}
	
	public static void assertEquals(PTItem item1, PTItem item2) {
		assertEquals(item1.getID(), item2.getID());
		assertEquals(item1.getCharacterID(), item2.getCharacterID());
		assertEquals(item1.getName(), item2.getName());
		assertEquals(item1.getWeight(), item2.getWeight());
		assertEquals(item1.getQuantity(), item2.getQuantity());
		assertEquals(item1.isContained(), item2.isContained());
	}
}
