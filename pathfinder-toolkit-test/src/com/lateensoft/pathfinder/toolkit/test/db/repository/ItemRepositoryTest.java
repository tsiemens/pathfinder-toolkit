package com.lateensoft.pathfinder.toolkit.test.db.repository;

import com.lateensoft.pathfinder.toolkit.db.repository.ItemRepository;
import com.lateensoft.pathfinder.toolkit.model.character.items.Item;

import java.util.List;

public class ItemRepositoryTest extends BaseRepositoryTest {
	private Item m_item1;
	private Item m_item2;
	private ItemRepository m_repo;
	
	@Override
	public void setUp() {
		super.setUp();
		m_repo = new ItemRepository();
		
		m_item1 = new Item(m_characterId, "Cauldron", 5.0, 1, false);
		m_item2 = new Item(m_characterId, "Wands", 1.0, 2, true);
		
		m_repo.insert(m_item1);
		m_repo.insert(m_item2);
	}
	
	public void testInsert() {
		Item insertItem = new Item(m_characterId, "Different Wands", 1.0, 2, true);
		long id = m_repo.insert(insertItem);
		assertTrue(id != INSERT_FAIL);
	}
	
	public void testQuery() {
		Item queried = m_repo.query(m_item1.getID());
		
		assertEquals(queried, m_item1);
	}
	
	public void testUpdate() {
		Item toUpdate = new Item(m_item2.getID(), m_characterId, "Big Wand", 2.0, 1, false);
		
		m_repo.update(toUpdate);
		Item updated = m_repo.query(toUpdate.getID());
		assertEquals(toUpdate, updated);
	}
	
	public void testDelete() {
		m_repo.delete(m_item1.getID());
		assertTrue (m_repo.query(m_item1.getID()) == null);
	}
	
	public void testQuerySet() {
		List<Item> queriedItems = m_repo.querySet(m_characterId);
		assertEquals(queriedItems.get(0), m_item1);
		assertEquals(queriedItems.get(1), m_item2);
	}
	
	public static void assertEquals(Item item1, Item item2) {
		assertEquals(item1.getID(), item2.getID());
		assertEquals(item1.getCharacterID(), item2.getCharacterID());
		assertEquals(item1.getName(), item2.getName());
		assertEquals(item1.getWeight(), item2.getWeight());
		assertEquals(item1.getQuantity(), item2.getQuantity());
		assertEquals(item1.isContained(), item2.isContained());
	}
}
