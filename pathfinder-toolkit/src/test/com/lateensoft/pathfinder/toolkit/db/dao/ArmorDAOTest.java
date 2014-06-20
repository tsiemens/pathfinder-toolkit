package com.lateensoft.pathfinder.toolkit.db.dao;

import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.db.dao.table.ArmorDAO;
import com.lateensoft.pathfinder.toolkit.model.character.items.Armor;
import org.hamcrest.BaseMatcher;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(RobolectricTestRunner.class)
public class ArmorDAOTest extends CharacterComponentDAOTest {
	private Armor m_armor1;
	private Armor m_armor2;
	private ArmorDAO dao;

	@Override
	public void setUp() throws Exception {
		super.setUp();
        initAndTestAdd();
	}

    @Override
    protected GenericTableDAO getDAO() {
        return dao;
    }

    private void initAndTestAdd() {
        try {
            dao = new ArmorDAO(Robolectric.application);

            m_armor1 = new Armor();
            setValues(m_armor1, m_armor1.getId(), getTestCharacterId(), "Heavy armor",
                    7.5, true, 1, /*ACP*/-2, 3, 4, 5, "armor", "M");

            m_armor2 = new Armor();
            setValues(m_armor2, m_armor2.getId(), getTestCharacterId(), "Hat",
                    1.0, false, 10, /*ACP*/-4, 100, 11, 20, "Hat", "S");

            dao.add(getTestCharacterId(), m_armor1);
            dao.add(getTestCharacterId(), m_armor2);
        } catch (DataAccessException e) {
            handleDataAccessException(e);
        }
    }

    @Test
	public void testQuery() {
		Armor queried = dao.find(m_armor1.getId());

		assertEquals(queried, m_armor1);
	}

    @Test
	public void testUpdate() {
        try {
            Armor toUpdate = m_armor2;
            setValues(m_armor2, m_armor2.getId(), getTestCharacterId(), "Larger Hat",
                    2.0, true, 11, 25, 101, 12, 21, "Hat thing", "M");

            dao.update(getTestCharacterId(), toUpdate);
            Armor updated = dao.find(toUpdate.getId());
            assertEquals(toUpdate, updated);
        } catch (DataAccessException e) {
            handleDataAccessException(e);
        }
    }

    @Test
	public void testDelete() {
        try {
            dao.remove(m_armor1);
            assertNull(dao.find(m_armor1.getId()));
        } catch (DataAccessException e) {
            handleDataAccessException(e);
        }
    }

    @Test
	public void testFindAllForOwner() {
		List<Armor> queriedItems = dao.findAllForOwner(getTestCharacterId());
        assertThat(queriedItems, CoreMatchers.hasItems(m_armor1, m_armor2));
//		assertEquals(queriedItems.get(0), m_armor2);
//		assertEquals(queriedItems.get(1), m_armor1);
	}

    @Test
	public void testMaxDex() {
        try {
            Armor armor1 = new Armor();
            setValues(armor1, armor1.getId(), getTestCharacterId(), "Hat",
                    1.0, true, 10, 24, 2, 11, 20, "Hat", "S");
            dao.add(getTestCharacterId(), armor1);
            Armor armor2 = new Armor();
            setValues(armor2, armor2.getId(), getTestCharacterId(), "Hat",
                    1.0, true, 10, 24, 42, 11, 20, "Hat", "S");
            dao.add(getTestCharacterId(), armor2);

            assertEquals(2, dao.getMaxDexForCharacter(getTestCharacterId()));

            m_armor1.setWorn(false);
            dao.update(getTestCharacterId(), m_armor1);
            armor1.setWorn(false);
            dao.update(getTestCharacterId(), armor1);
            armor2.setWorn(false);
            dao.update(getTestCharacterId(), armor2);
            assertEquals(Integer.MAX_VALUE, dao.getMaxDexForCharacter(getTestCharacterId()));
        } catch (DataAccessException e) {
            handleDataAccessException(e);
        }
    }

    @Test
	public void testArmorCheck() {
        try {
            Armor armor1 = new Armor();
            setValues(armor1, armor1.getId(), getTestCharacterId(), "Heavy armor",
                    7.5, true, 1, /*ACP*/-5, 3, 4, 5, "armor", "M");
            dao.add(getTestCharacterId(), armor1);
            assertEquals(-7, dao.getArmorCheckPenaltyForCharacter(getTestCharacterId()));


            dao.remove(m_armor1);
            dao.remove(armor1);
            assertEquals(0, dao.getArmorCheckPenaltyForCharacter(getTestCharacterId()));
        } catch (DataAccessException e) {
            handleDataAccessException(e);
        }
    }

	private void setValues(Armor toUpdate, long id, long characterId, String name,
			double weight, boolean worn, int ACBonus, int checkPen, int maxDex, int spellFail,
			int speed, String specProp, String size) {
		toUpdate.setId(id);
		toUpdate.setCharacterID(characterId);
		toUpdate.setName(name);
		toUpdate.setWeight(weight);
		toUpdate.setWorn(worn);
		toUpdate.setACBonus(ACBonus);
		toUpdate.setCheckPen(checkPen);
		toUpdate.setMaxDex(maxDex);
		toUpdate.setSpellFail(spellFail);
		toUpdate.setSpeed(speed);
		toUpdate.setSpecialProperties(specProp);
		toUpdate.setSize(size);
	}

}
