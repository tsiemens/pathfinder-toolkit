package com.lateensoft.pathfinder.toolkit.test.db.dao;

import com.lateensoft.pathfinder.toolkit.db.repository.WeaponRepository;
import com.lateensoft.pathfinder.toolkit.model.character.items.Weapon;
import com.lateensoft.pathfinder.toolkit.test.db.repository.BaseRepositoryTest;
import com.lateensoft.pathfinder.toolkit.test.db.repository.ItemRepositoryTest;

import java.util.List;

public class WeaponDAOTest extends BaseRepositoryTest {
	private Weapon m_weapon1;
	private Weapon m_weapon2;
	private WeaponRepository m_repo;
	
	@Override
	public void setUp() {
		super.setUp();
		m_repo = new WeaponRepository();
		
		m_weapon1 = new Weapon();
		setValues(m_weapon1, m_weapon1.getId(), m_characterId, "Great Sword",
				7.5, 5, "4/2", "x2", 5, "It's on fire!", 0, "Sword", "L");
		
		m_weapon2 = new Weapon();
		setValues(m_weapon2, m_weapon2.getId(), m_characterId, "Long Bow",
				4.0, 3, "5", "x3", 100, "None", 20, "Bow", "M");
		
		m_repo.insert(m_weapon1);
		m_repo.insert(m_weapon2);
	}
	
	public void testInsert() {
		Weapon toInsert = new Weapon();
		setValues(toInsert, toInsert.getId(), m_characterId, "Longer Bow",
				5.0, 3, "6", "x3", 120, "N/A", 40, "Bow", "L");
		long id = m_repo.insert(toInsert);
		assertTrue(id != INSERT_FAIL);
	}
	
	
	public void testQuery() {
		Weapon queried = m_repo.query(m_weapon1.getId());
		
		assertEquals(queried, m_weapon1);
	}
	
	public void testUpdate() {
		Weapon toUpdate = m_weapon2;
		setValues(toUpdate, m_weapon2.getId(), m_weapon2.getCharacterID(), "Longer Bow",
				5.0, 3, "6", "x3", 120, "N/A", 40, "Bow", "L");
		
		m_repo.update(toUpdate);
		Weapon updated = m_repo.query(toUpdate.getId());
		assertEquals(toUpdate, updated);
	}
	
	public void testDelete() {
		m_repo.delete(m_weapon1.getId());
		assertTrue(m_repo.query(m_weapon1.getId()) == null);
	}
	
	public void testQuerySet() {
		List<Weapon> queriedItems = m_repo.querySet(m_characterId);
		assertEquals(queriedItems.get(0), m_weapon1);
		assertEquals(queriedItems.get(1), m_weapon2);
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
