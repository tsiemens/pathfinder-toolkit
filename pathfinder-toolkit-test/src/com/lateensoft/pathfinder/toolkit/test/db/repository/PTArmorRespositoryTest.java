package com.lateensoft.pathfinder.toolkit.test.db.repository;

import com.lateensoft.pathfinder.toolkit.db.repository.PTArmorRepository;
import com.lateensoft.pathfinder.toolkit.model.character.items.PTArmor;

import java.util.List;

public class PTArmorRespositoryTest extends PTBaseRepositoryTest {	
	private PTArmor m_armor1;
	private PTArmor m_armor2;
	private PTArmorRepository m_repo;
	
	@Override
	public void setUp() {
		super.setUp();
		m_repo = new PTArmorRepository();
		
		m_armor1 = new PTArmor();
		setValues(m_armor1, m_armor1.getID(), m_characterId, "Heavy armor",
				7.5, true, 1, /*ACP*/-2, 3, 4, 5, "armor", "M");
		
		m_armor2 = new PTArmor();
		setValues(m_armor2, m_armor2.getID(), m_characterId, "Hat",
				1.0, false, 10, /*ACP*/-4, 100, 11, 20, "Hat", "S");
		
		m_repo.insert(m_armor1);
		m_repo.insert(m_armor2);
	}
	
	public void testInsert() {
		PTArmor toInsert = new PTArmor();
		setValues(toInsert, toInsert.getID(), m_characterId, "Shoes",
				1.0, true, 10, 24, 100, 11, 20, "footwear", "L");
		long id = m_repo.insert(toInsert);
		assertTrue(id != INSERT_FAIL);
	}
	
	
	public void testQuery() {
		PTArmor queried = m_repo.query(m_armor1.getID());
		
		assertEquals(queried, m_armor1);
	}
	
	public void testUpdate() {
		PTArmor toUpdate = m_armor2;
		setValues(m_armor2, m_armor2.getID(), m_characterId, "Larger Hat",
				2.0, true, 11, 25, 101, 12, 21, "Hat thing", "M");
		
		m_repo.update(toUpdate);
		PTArmor updated = m_repo.query(toUpdate.getID());
		assertEquals(toUpdate, updated);
	}
	
	public void testDelete() {
		m_repo.delete(m_armor1.getID());
		assertTrue(m_repo.query(m_armor1.getID()) == null);
	}
	
	public void testQuerySet() {
		List<PTArmor> queriedItems = m_repo.querySet(m_characterId);
		assertEquals(queriedItems.get(0), m_armor2);
		assertEquals(queriedItems.get(1), m_armor1);
	}
	
	public void testMaxDex() {
		PTArmor armor1 = new PTArmor();
		setValues(armor1, armor1.getID(), m_characterId, "Hat",
				1.0, true, 10, 24, 2, 11, 20, "Hat", "S");	
		m_repo.insert(armor1);
		PTArmor armor2 = new PTArmor();
		setValues(armor2, armor2.getID(), m_characterId, "Hat",
				1.0, true, 10, 24, 42, 11, 20, "Hat", "S");	
		m_repo.insert(armor2);
		
		assertEquals(2, m_repo.getMaxDex(m_characterId));
		
		m_armor1.setWorn(false);
		m_repo.update(m_armor1);
		armor1.setWorn(false);
		m_repo.update(armor1);
		armor2.setWorn(false);
		m_repo.update(armor2);
		assertEquals(Integer.MAX_VALUE, m_repo.getMaxDex(m_characterId));
	}
	
	public void testArmorCheck() {
		PTArmor armor1 = new PTArmor();
		setValues(armor1, armor1.getID(), m_characterId, "Heavy armor",
				7.5, true, 1, /*ACP*/-5, 3, 4, 5, "armor", "M");
		m_repo.insert(armor1);
		assertEquals(-7, m_repo.getArmorCheckPenalty(m_characterId));
		
		
		m_repo.delete(m_armor1);
		m_repo.delete(armor1);
		assertEquals(0, m_repo.getArmorCheckPenalty(m_characterId));
	}

	public static void setValues(PTArmor toUpdate, long id, long characterId, String name,
			double weight, boolean worn, int ACBonus, int checkPen, int maxDex, int spellFail,
			int speed, String specProp, String size) {
		toUpdate.setID(id);
		toUpdate.setCharacterID(characterId);
		toUpdate.setName(name);
		toUpdate.setWeight(5.0);
		toUpdate.setWorn(worn);
		toUpdate.setACBonus(ACBonus);
		toUpdate.setCheckPen(checkPen);
		toUpdate.setMaxDex(maxDex);
		toUpdate.setSpellFail(spellFail);
		toUpdate.setSpeed(speed);
		toUpdate.setSpecialProperties(specProp);
		toUpdate.setSize(size);
	}
	
	public static void assertEquals(PTArmor item1, PTArmor item2) {
		PTItemRepositoryTest.assertEquals(item1, item2);
		assertEquals(item1.isWorn(), item2.isWorn());
		assertEquals(item1.getACBonus(), item2.getACBonus());
		assertEquals(item1.getCheckPen(), item2.getCheckPen());
		assertEquals(item1.getMaxDex(), item2.getMaxDex());
		assertEquals(item1.getSpellFail(), item2.getSpellFail());
		assertEquals(item1.getSpeed(), item2.getSpeed());
		assertEquals(item1.getSpecialProperties(), item2.getSpecialProperties());
		assertEquals(item1.getSize(), item2.getSize());
	}


}
