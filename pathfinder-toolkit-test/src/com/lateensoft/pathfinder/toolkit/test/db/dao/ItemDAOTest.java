package com.lateensoft.pathfinder.toolkit.test.db.dao;

import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.db.dao.table.ItemDAO;
import com.lateensoft.pathfinder.toolkit.model.character.items.Item;

import java.util.List;

public class ItemDAOTest extends CharacterComponentDAOTest {
	private Item item1;
	private Item item2;

	private ItemDAO dao;
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
        initAndTestAdd();
    }

    public void initAndTestAdd() {
        try {
            dao = new ItemDAO();
            item1 = new Item(getTestCharacterId(), "Cauldron", 5.0, 1, false);
            item2 = new Item(getTestCharacterId(), "Wands", 1.0, 2, true);

            dao.add(getTestCharacterId(), item1);
            dao.add(getTestCharacterId(), item2);
        } catch (DataAccessException e) {
            handleDataAccessException(e);
        }
    }

	public void testFind() {
		Item queried = dao.find(getTestCharacterId(), item1.getId());
		
		assertEquals(queried, item1);
	}
	
	public void testUpdate() {
		Item toUpdate = new Item(item2.getId(), getTestCharacterId(), "Big Wand", 2.0, 1, false);

        try {
            dao.update(getTestCharacterId(), toUpdate);
            Item updated = dao.find(getTestCharacterId(), toUpdate.getId());
            assertEquals(toUpdate, updated);
        } catch (DataAccessException e) {
            handleDataAccessException(e);
        }
    }
	
	public void testRemove() {
        try {
            dao.remove(getTestCharacterId(), item1);
            assertTrue(dao.find(getTestCharacterId(), item1.getId()) == null);
        } catch (DataAccessException e) {
            handleDataAccessException(e);
        }
    }
	
	public void testQuerySet() {
		List<Item> queriedItems = dao.findAll(getTestCharacterId());
		assertEquals(queriedItems.get(0), item1);
		assertEquals(queriedItems.get(1), item2);
	}
	
	public static void assertEquals(Item item1, Item item2) {
		assertEquals(item1.getId(), item2.getId());
		assertEquals(item1.getCharacterID(), item2.getCharacterID());
		assertEquals(item1.getName(), item2.getName());
		assertEquals(item1.getWeight(), item2.getWeight());
		assertEquals(item1.getQuantity(), item2.getQuantity());
		assertEquals(item1.isContained(), item2.isContained());
	}
}
