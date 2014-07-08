package com.lateensoft.pathfinder.toolkit.db.dao.table;

import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.model.character.items.Size;
import com.lateensoft.pathfinder.toolkit.model.character.items.Weapon;
import com.lateensoft.pathfinder.toolkit.model.character.items.WeaponType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@Config(manifest=Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class WeaponDAOTest extends CharacterComponentDAOTest {
    private Weapon weapon1;
    private Weapon weapon2;
    private WeaponDAO dao;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        initAndTestAdd();
    }

    private void initAndTestAdd() throws DataAccessException {
        dao = new WeaponDAO(Robolectric.application);

        weapon1 = new Weapon();
        setValues(weapon1, weapon1.getId(), getTestCharacterId(), "Great Sword",
                7.5, 5, "4/2", "x2", 5, "It's on fire!", 0, WeaponType.SLASHING_PIERCING, Size.LARGE);

        weapon2 = new Weapon();
        setValues(weapon2, weapon2.getId(), getTestCharacterId(), "Long Bow",
                4.0, 3, "5", "x3", 100, "None", 20, WeaponType.PIERCING, Size.MEDIUM);

        dao.add(getTestCharacterId(), weapon1);
        dao.add(getTestCharacterId(), weapon2);
    }

    @Test
    public void testFind() {
        Weapon queried = dao.find(weapon1.getId());

        assertEquals(queried, weapon1);
    }

    @Test
    public void testUpdate() throws DataAccessException {
        Weapon toUpdate = weapon2;
        setValues(toUpdate, weapon2.getId(), weapon2.getCharacterID(), "Longer Bow",
                5.0, 3, "6", "x3", 120, "N/A", 40, WeaponType.PIERCING, Size.LARGE);

        dao.update(getTestCharacterId(), toUpdate);
        Weapon updated = dao.find(toUpdate.getId());
        assertEquals(toUpdate, updated);
    }

    @Test
    public void testDelete() throws DataAccessException {
        dao.remove(weapon1);
        assertNull(dao.find(weapon1.getId()));
    }

    @Test
    public void testQuerySet() {
        List<Weapon> queriedItems = dao.findAllForOwner(getTestCharacterId());
        assertEquals(queriedItems.get(0), weapon1);
        assertEquals(queriedItems.get(1), weapon2);
    }

    private void setValues(Weapon toUpdate, long id, long characterId, String name,
                           double weight, int totalAttackB, String dmg, String crit, int range, String specProp,
                           int ammo, WeaponType type, Size size) {
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

}
