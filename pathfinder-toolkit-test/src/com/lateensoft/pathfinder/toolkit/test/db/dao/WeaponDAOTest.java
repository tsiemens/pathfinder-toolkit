package com.lateensoft.pathfinder.toolkit.test.db.dao;

import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.db.dao.table.WeaponDAO;
import com.lateensoft.pathfinder.toolkit.model.character.items.Weapon;
import com.lateensoft.pathfinder.toolkit.test.db.repository.ItemRepositoryTest;

import java.util.List;

public class WeaponDAOTest extends CharacterComponentDAOTest {
	private Weapon weapon1;
	private Weapon weapon2;
	private WeaponDAO dao;
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
        initAndTestAdd();
	}

    private void initAndTestAdd() {
        try {
            dao = new WeaponDAO();

            weapon1 = new Weapon();
            setValues(weapon1, weapon1.getId(), getTestCharacterId(), "Great Sword",
                    7.5, 5, "4/2", "x2", 5, "It's on fire!", 0, "Sword", "L");

            weapon2 = new Weapon();
            setValues(weapon2, weapon2.getId(), getTestCharacterId(), "Long Bow",
                    4.0, 3, "5", "x3", 100, "None", 20, "Bow", "M");

            dao.add(getTestCharacterId(), weapon1);
            dao.add(getTestCharacterId(), weapon2);
        } catch (DataAccessException e) {
            handleDataAccessException(e);
        }
    }

	public void testFind() {
		Weapon queried = dao.find(weapon1.getId());
		
		assertEquals(queried, weapon1);
	}
	
	public void testUpdate() {
        try {
            Weapon toUpdate = weapon2;
            setValues(toUpdate, weapon2.getId(), weapon2.getCharacterID(), "Longer Bow",
                    5.0, 3, "6", "x3", 120, "N/A", 40, "Bow", "L");

            dao.update(getTestCharacterId(), toUpdate);
            Weapon updated = dao.find(toUpdate.getId());
            assertEquals(toUpdate, updated);
        } catch (DataAccessException e) {
            handleDataAccessException(e);
        }
    }
	
	public void testDelete() {
        try {
            dao.remove(weapon1);
            assertTrue(dao.find(weapon1.getId()) == null);
        } catch (DataAccessException e) {
            handleDataAccessException(e);
        }
    }
	
	public void testQuerySet() {
		List<Weapon> queriedItems = dao.findAllForOwner(getTestCharacterId());
		assertEquals(queriedItems.get(0), weapon1);
		assertEquals(queriedItems.get(1), weapon2);
	}

	public static void setValues(Weapon toUpdate, long id, long characterId, String name,
			double weight, int totalAttackB, String dmg, String crit, int range, String specProp,
			int ammo, String type, String size) {
		toUpdate.setId(id);
		toUpdate.setCharacterID(characterId);
		toUpdate.setName(name);
		toUpdate.setWeight(weight);
		toUpdate.setTotalAttackBonus(totalAttackB);
		toUpdate.setDamage(dmg);
		toUpdate.setCritical(crit);
		toUpdate.setRange(range);
		toUpdate.setSpecialProperties(specProp);
		toUpdate.setAmmunition(ammo);
		toUpdate.setType(type);
		toUpdate.setSize(size);
	}
	
	public static void assertEquals(Weapon item1, Weapon item2) {
		ItemRepositoryTest.assertEquals(item1, item2);
		assertEquals(item1.getTotalAttackBonus(), item2.getTotalAttackBonus());
		assertEquals(item1.getDamage(), item2.getDamage());
		assertEquals(item1.getCritical(), item2.getCritical());
		assertEquals(item1.getRange(), item2.getRange());
		assertEquals(item1.getSpecialProperties(), item2.getSpecialProperties());
		assertEquals(item1.getAmmunition(), item2.getAmmunition());
		assertEquals(item1.getType(), item2.getType());
		assertEquals(item1.getSize(), item2.getSize());
	}

}
