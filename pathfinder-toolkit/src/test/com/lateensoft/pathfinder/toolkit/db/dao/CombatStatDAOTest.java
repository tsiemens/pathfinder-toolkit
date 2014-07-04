package com.lateensoft.pathfinder.toolkit.db.dao;

import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.db.BaseDatabaseTest;
import com.lateensoft.pathfinder.toolkit.db.dao.table.CombatStatDAO;
import com.lateensoft.pathfinder.toolkit.model.character.stats.AbilityType;
import com.lateensoft.pathfinder.toolkit.model.character.stats.CombatStatSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;

@Config(manifest=Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class CombatStatDAOTest extends CharacterComponentDAOTest {

    private CombatStatDAO m_repo;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        m_repo = new CombatStatDAO(Robolectric.application);
    }

    @Test
    public void findAndUpdate() throws DataAccessException {
        CombatStatSet combatStatSet = m_repo.find(getTestCharacterId());
        combatStatSet.setTotalHP(50);
        combatStatSet.setWounds(3);
        combatStatSet.setNonLethalDamage(5);
        combatStatSet.setDamageReduction(8);
        combatStatSet.setBaseSpeed(30);
        combatStatSet.setInitAbility(AbilityType.STR);
        combatStatSet.setInitiativeMiscMod(2);
        combatStatSet.setACArmourBonus(10);
        combatStatSet.setACShieldBonus(11);
        combatStatSet.setACAbility(AbilityType.CON);
        combatStatSet.setSizeModifier(13);
        combatStatSet.setNaturalArmour(14);
        combatStatSet.setDeflectionMod(15);
        combatStatSet.setACMiscMod(16);
        combatStatSet.setBABPrimary(17);
        combatStatSet.setBABSecondary("2/6/7");
        combatStatSet.setCMBAbility(AbilityType.DEX);
        combatStatSet.setCMDAbility(AbilityType.WIS);
        combatStatSet.setCMDMiscMod(56);
        combatStatSet.setSpellResistance(67);

        m_repo.update(getTestCharacterId(), combatStatSet);

        CombatStatSet foundCombatStats = m_repo.find(getTestCharacterId());
        assertEquals(combatStatSet, foundCombatStats);
    }
}
