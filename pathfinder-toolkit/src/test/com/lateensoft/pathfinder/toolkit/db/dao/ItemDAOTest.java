package com.lateensoft.pathfinder.toolkit.db.dao;

import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.db.dao.table.ItemDAO;
import com.lateensoft.pathfinder.toolkit.model.character.items.Item;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class ItemDAOTest extends CharacterComponentDAOTest {
    private Item item1;
    private Item item2;

    private ItemDAO dao;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        initAndTestAdd();
    }

    public void initAndTestAdd() throws DataAccessException {
        dao = new ItemDAO(Robolectric.application);
        item1 = new Item(getTestCharacterId(), "Cauldron", 5.0, 1, false);
        item2 = new Item(getTestCharacterId(), "Wands", 1.0, 2, true);
        dao.add(getTestCharacterId(), item1);
        dao.add(getTestCharacterId(), item2);
    }

    @Test
    public void testFind() {
        Item queried = dao.find(item1.getId());
        assertEquals(queried, item1);
    }

    @Test
    public void testUpdate() throws DataAccessException {
        Item toUpdate = new Item(item2.getId(), getTestCharacterId(), "Big Wand", 2.0, 1, false);

        dao.update(getTestCharacterId(), toUpdate);
        Item updated = dao.find(toUpdate.getId());
        assertEquals(toUpdate, updated);
    }

    @Test
    public void testRemove() throws DataAccessException {
        dao.remove(item1);
        Assert.assertNull(dao.find(item1.getId()));
    }

    @Test
    public void testQuerySet() {
        List<Item> queriedItems = dao.findAllForOwner(getTestCharacterId());
        assertEquals(queriedItems.get(0), item1);
        assertEquals(queriedItems.get(1), item2);
    }
}
