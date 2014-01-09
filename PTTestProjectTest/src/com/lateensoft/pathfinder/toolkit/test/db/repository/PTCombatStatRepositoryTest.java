package com.lateensoft.pathfinder.toolkit.test.db.repository;

import com.lateensoft.pathfinder.toolkit.db.repository.PTCombatStatRepository;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTCombatStatSet;

public class PTCombatStatRepositoryTest extends PTBaseRepositoryTest {	
	private PTCombatStatSet m_insertCombatStatSet;
	private PTCombatStatRepository m_repo;
	
	public void setUp() {
		super.setUp();
		m_insertCombatStatSet = new PTCombatStatSet(m_characterId);
		m_insertCombatStatSet.setTotalHP(50);
		m_insertCombatStatSet.setWounds(3);
		m_insertCombatStatSet.setNonLethalDamage(5);
		m_insertCombatStatSet.setDamageReduction(8);
		m_insertCombatStatSet.setBaseSpeed(30);
		m_insertCombatStatSet.setInitDexMod(1);
		m_insertCombatStatSet.setInitiativeMiscMod(2);
		m_insertCombatStatSet.setACArmourBonus(10);
		m_insertCombatStatSet.setACShieldBonus(11);
		m_insertCombatStatSet.setACDexMod(12);
		m_insertCombatStatSet.setSizeModifier(13);
		m_insertCombatStatSet.setNaturalArmour(14);
		m_insertCombatStatSet.setDeflectionMod(15);
		m_insertCombatStatSet.setACMiscMod(16);
		m_insertCombatStatSet.setBABPrimary(17);
		m_insertCombatStatSet.setBABSecondary("2/6/7");
		m_insertCombatStatSet.setStrengthMod(18);
		m_insertCombatStatSet.setCMDDexMod(46);
		m_insertCombatStatSet.setCMDMiscMod(56);
		m_insertCombatStatSet.setSpellResistance(67);
		
		m_repo = new PTCombatStatRepository();
	}
	
	public void testInsert() {
		long id = m_repo.insert(m_insertCombatStatSet);
		assertTrue(id != INSERT_FAIL);
	}
	
	public void testQuery() {
		long id = m_repo.insert(m_insertCombatStatSet);
		PTCombatStatSet queried = m_repo.query(id);
		assertEquals("id", m_insertCombatStatSet.getID(), queried.getID());
		assertEquals("char id", m_insertCombatStatSet.getCharacterID(), queried.getCharacterID());
		assertEquals("total hp", m_insertCombatStatSet.getTotalHP(), queried.getTotalHP());
		assertEquals("wounds", m_insertCombatStatSet.getWounds(), queried.getWounds());
		assertEquals("non leth dmg", m_insertCombatStatSet.getNonLethalDamage(), queried.getNonLethalDamage());
		assertEquals("dmg red", m_insertCombatStatSet.getDamageReduction(), queried.getDamageReduction());
		assertEquals("base speed", m_insertCombatStatSet.getBaseSpeed(), queried.getBaseSpeed());
		assertEquals("init dex", m_insertCombatStatSet.getInitDexMod(), queried.getInitDexMod());
		assertEquals("init misc", m_insertCombatStatSet.getInitiativeMiscMod(), queried.getInitiativeMiscMod());
		assertEquals("ac armor", m_insertCombatStatSet.getACArmourBonus(), queried.getACArmourBonus());
		assertEquals("ac shield", m_insertCombatStatSet.getACShieldBonus(), queried.getACShieldBonus());
		assertEquals("ac dex", m_insertCombatStatSet.getACDexMod(), queried.getACDexMod());
		assertEquals("size", m_insertCombatStatSet.getSizeModifier(), queried.getSizeModifier());
		assertEquals("natural armor", m_insertCombatStatSet.getNaturalArmour(), queried.getNaturalArmour());
		assertEquals("deflect mod", m_insertCombatStatSet.getDeflectionMod(), queried.getDeflectionMod());
		assertEquals("ac misc", m_insertCombatStatSet.getACMiscMod(), queried.getACMiscMod());
		assertEquals("bab 1", m_insertCombatStatSet.getBABPrimary(), queried.getBABPrimary());
		assertEquals("bab 2", m_insertCombatStatSet.getBABSecondary(), queried.getBABSecondary());
		assertEquals("strength", m_insertCombatStatSet.getStrengthMod(), queried.getStrengthMod());
		assertEquals("cmd dex", m_insertCombatStatSet.getCMDDexMod(), queried.getCMDDexMod());
		assertEquals("cmd misc", m_insertCombatStatSet.getCMDMiscMod(), queried.getCMDMiscMod());
		assertEquals("spell resist", m_insertCombatStatSet.getSpellResist(), queried.getSpellResist());

	}
	
	public void testUpdate() {
		long id = m_repo.insert(m_insertCombatStatSet);
		PTCombatStatSet toUpdate = new PTCombatStatSet(id, m_characterId);
		toUpdate.setTotalHP(51);
		toUpdate.setWounds(4);
		toUpdate.setNonLethalDamage(6);
		toUpdate.setDamageReduction(9);
		toUpdate.setBaseSpeed(31);
		toUpdate.setInitDexMod(2);
		toUpdate.setInitiativeMiscMod(3);
		toUpdate.setACArmourBonus(11);
		toUpdate.setACShieldBonus(12);
		toUpdate.setACDexMod(13);
		toUpdate.setSizeModifier(14);
		toUpdate.setNaturalArmour(15);
		toUpdate.setDeflectionMod(16);
		toUpdate.setACMiscMod(17);
		toUpdate.setBABPrimary(18);
		toUpdate.setBABSecondary("2/6/8");
		toUpdate.setStrengthMod(19);
		toUpdate.setCMDDexMod(47);
		toUpdate.setCMDMiscMod(57);
		toUpdate.setSpellResistance(68);
		
		m_repo.update(toUpdate);
		PTCombatStatSet updated = m_repo.query(id);
		assertEquals("id", toUpdate.getID(), updated.getID());
		assertEquals("char id", toUpdate.getCharacterID(), updated.getCharacterID());
		assertEquals("total hp", toUpdate.getTotalHP(), updated.getTotalHP());
		assertEquals("wounds", toUpdate.getWounds(), updated.getWounds());
		assertEquals("non leth dmg", toUpdate.getNonLethalDamage(), updated.getNonLethalDamage());
		assertEquals("dmg red", toUpdate.getDamageReduction(), updated.getDamageReduction());
		assertEquals("base speed", toUpdate.getBaseSpeed(), updated.getBaseSpeed());
		assertEquals("init dex", toUpdate.getInitDexMod(), updated.getInitDexMod());
		assertEquals("init misc", toUpdate.getInitiativeMiscMod(), updated.getInitiativeMiscMod());
		assertEquals("ac armor", toUpdate.getACArmourBonus(), updated.getACArmourBonus());
		assertEquals("ac shield", toUpdate.getACShieldBonus(), updated.getACShieldBonus());
		assertEquals("ac dex", toUpdate.getACDexMod(), updated.getACDexMod());
		assertEquals("size", toUpdate.getSizeModifier(), updated.getSizeModifier());
		assertEquals("natural armor", toUpdate.getNaturalArmour(), updated.getNaturalArmour());
		assertEquals("deflect mod", toUpdate.getDeflectionMod(), updated.getDeflectionMod());
		assertEquals("ac misc", toUpdate.getACMiscMod(), updated.getACMiscMod());
		assertEquals("bab 1", toUpdate.getBABPrimary(), updated.getBABPrimary());
		assertEquals("bab 2", toUpdate.getBABSecondary(), updated.getBABSecondary());
		assertEquals("strength", toUpdate.getStrengthMod(), updated.getStrengthMod());
		assertEquals("cmd dex", toUpdate.getCMDDexMod(), updated.getCMDDexMod());
		assertEquals("cmd misc", toUpdate.getCMDMiscMod(), updated.getCMDMiscMod());
		assertEquals("spell resist", toUpdate.getSpellResist(), updated.getSpellResist());
	}
	
	public void testDelete() {
		long id = m_repo.insert(m_insertCombatStatSet);
		m_repo.delete(id);
		boolean exception = false;
		try {
			@SuppressWarnings("unused")
			PTCombatStatSet queried = m_repo.query(id);
		}
		catch(Exception e) {
			exception = true;
		}
		finally {
			assertTrue(exception);
		}
	}

}
