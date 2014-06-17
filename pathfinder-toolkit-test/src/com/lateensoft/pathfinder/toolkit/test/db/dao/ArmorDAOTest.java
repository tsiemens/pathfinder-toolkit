package com.lateensoft.pathfinder.toolkit.test.db.dao;

import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.db.dao.table.ArmorDAO;
import com.lateensoft.pathfinder.toolkit.model.character.items.Armor;
import com.lateensoft.pathfinder.toolkit.test.db.repository.ItemRepositoryTest;

import java.util.List;

public class ArmorDAOTest extends CharacterComponentDAOTest {
	private Armor m_armor1;
	private Armor m_armor2;
	private ArmorDAO m_repo;
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
        initAndTestAdd();
	}

    private void initAndTestAdd() {
        try {
            m_repo = new ArmorDAO();

            m_armor1 = new Armor();
            setValues(m_armor1, m_armor1.getId(), getTestCharacterId(), "Heavy armor",
                    7.5, true, 1, /*ACP*/-2, 3, 4, 5, "armor", "M");

            m_armor2 = new Armor();
            setValues(m_armor2, m_armor2.getId(), getTestCharacterId(), "Hat",
                    1.0, false, 10, /*ACP*/-4, 100, 11, 20, "Hat", "S");

            m_repo.add(getTestCharacterId(), m_armor1);
            m_repo.add(getTestCharacterId(), m_armor2);
        } catch (DataAccessException e) {
            handleDataAccessException(e);
        }
    }

	
	public void testQuery() {
		Armor queried = m_repo.find(m_armor1.getId());
		
		assertEquals(queried, m_armor1);
	}
	
	public void testUpdate() {
        try {
            Armor toUpdate = m_armor2;
            setValues(m_armor2, m_armor2.getId(), getTestCharacterId(), "Larger Hat",
                    2.0, true, 11, 25, 101, 12, 21, "Hat thing", "M");

            m_repo.update(getTestCharacterId(), toUpdate);
            Armor updated = m_repo.find(toUpdate.getId());
            assertEquals(toUpdate, updated);
        } catch (DataAccessException e) {
            handleDataAccessException(e);
        }
    }
	
	public void testDelete() {
        try {
            m_repo.remove(m_armor1);
            assertTrue(m_repo.find(m_armor1.getId()) == null);
        } catch (DataAccessException e) {
            handleDataAccessException(e);
        }
    }
	
	public void testQuerySet() {
		List<Armor> queriedItems = m_repo.findAllForOwner(getTestCharacterId());
		assertEquals(queriedItems.get(0), m_armor2);
		assertEquals(queriedItems.get(1), m_armor1);
	}
	
	public void testMaxDex() {
        try {
            Armor armor1 = new Armor();
            setValues(armor1, armor1.getId(), getTestCharacterId(), "Hat",
                    1.0, true, 10, 24, 2, 11, 20, "Hat", "S");
            m_repo.add(getTestCharacterId(), armor1);
            Armor armor2 = new Armor();
            setValues(armor2, armor2.getId(), getTestCharacterId(), "Hat",
                    1.0, true, 10, 24, 42, 11, 20, "Hat", "S");
            m_repo.add(getTestCharacterId(), armor2);

            assertEquals(2, m_repo.getMaxDexForCharacter(getTestCharacterId()));

            m_armor1.setWorn(false);
            m_repo.update(getTestCharacterId(), m_armor1);
            armor1.setWorn(false);
            m_repo.update(getTestCharacterId(), armor1);
            armor2.setWorn(false);
            m_repo.update(getTestCharacterId(), armor2);
            assertEquals(Integer.MAX_VALUE, m_repo.getMaxDexForCharacter(getTestCharacterId()));
        } catch (DataAccessException e) {
            handleDataAccessException(e);
        }
    }
	
	public void testArmorCheck() {
        try {
            Armor armor1 = new Armor();
            setValues(armor1, armor1.getId(), getTestCharacterId(), "Heavy armor",
                    7.5, true, 1, /*ACP*/-5, 3, 4, 5, "armor", "M");
            m_repo.add(getTestCharacterId(), armor1);
            assertEquals(-7, m_repo.getArmorCheckPenaltyForCharacter(getTestCharacterId()));


            m_repo.remove(m_armor1);
            m_repo.remove(armor1);
            assertEquals(0, m_repo.getArmorCheckPenaltyForCharacter(getTestCharacterId()));
        } catch (DataAccessException e) {
            handleDataAccessException(e);
        }
    }

	public static void setValues(Armor toUpdate, long id, long characterId, String name,
			double weight, boolean worn, int ACBonus, int checkPen, int maxDex, int spellFail,
			int speed, String specProp, String size) {
		toUpdate.setId(id);
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
	
	public static void assertEquals(Armor item1, Armor item2) {
		ItemRepositoryTest.assertEquals(item1, item2);
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
