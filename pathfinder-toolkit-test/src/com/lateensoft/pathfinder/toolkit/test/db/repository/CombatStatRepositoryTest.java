package com.lateensoft.pathfinder.toolkit.test.db.repository;

import com.lateensoft.pathfinder.toolkit.db.repository.CombatStatRepository;
import com.lateensoft.pathfinder.toolkit.model.character.stats.CombatStatSet;

public class CombatStatRepositoryTest extends BaseRepositoryTest {
	private CombatStatSet m_combatStatSet;
	private CombatStatRepository m_repo;
	
	public void setUp() {
		super.setUp();
		m_repo = new CombatStatRepository();
		
		m_combatStatSet = m_repo.query(m_characterId);
		m_combatStatSet.setTotalHP(50);
		m_combatStatSet.setWounds(3);
		m_combatStatSet.setNonLethalDamage(5);
		m_combatStatSet.setDamageReduction(8);
		m_combatStatSet.setBaseSpeed(30);
		m_combatStatSet.setInitAbility(1);
		m_combatStatSet.setInitiativeMiscMod(2);
		m_combatStatSet.setACArmourBonus(10);
		m_combatStatSet.setACShieldBonus(11);
		m_combatStatSet.setACAbility(12);
		m_combatStatSet.setSizeModifier(13);
		m_combatStatSet.setNaturalArmour(14);
		m_combatStatSet.setDeflectionMod(15);
		m_combatStatSet.setACMiscMod(16);
		m_combatStatSet.setBABPrimary(17);
		m_combatStatSet.setBABSecondary("2/6/7");
		m_combatStatSet.setCMBAbilityKey(18);
		m_combatStatSet.setCMDAbility(46);
		m_combatStatSet.setCMDMiscMod(56);
		m_combatStatSet.setSpellResistance(67);
		
		m_repo.update(m_combatStatSet);
	}
	
	public void testQuery() {
		CombatStatSet queried = m_repo.query(m_characterId);
		
		assertEquals("id", m_combatStatSet.getId(), queried.getId());
		assertEquals("total hp", m_combatStatSet.getTotalHP(), queried.getTotalHP());
		assertEquals("wounds", m_combatStatSet.getWounds(), queried.getWounds());
		assertEquals("non leth dmg", m_combatStatSet.getNonLethalDamage(), queried.getNonLethalDamage());
		assertEquals("dmg red", m_combatStatSet.getDamageReduction(), queried.getDamageReduction());
		assertEquals("base speed", m_combatStatSet.getBaseSpeed(), queried.getBaseSpeed());
		assertEquals("init dex", m_combatStatSet.getInitAbility(), queried.getInitAbility());
		assertEquals("init misc", m_combatStatSet.getInitiativeMiscMod(), queried.getInitiativeMiscMod());
		assertEquals("ac armor", m_combatStatSet.getACArmourBonus(), queried.getACArmourBonus());
		assertEquals("ac shield", m_combatStatSet.getACShieldBonus(), queried.getACShieldBonus());
		assertEquals("ac dex", m_combatStatSet.getACAbility(), queried.getACAbility());
		assertEquals("size", m_combatStatSet.getSizeModifier(), queried.getSizeModifier());
		assertEquals("natural armor", m_combatStatSet.getNaturalArmour(), queried.getNaturalArmour());
		assertEquals("deflect mod", m_combatStatSet.getDeflectionMod(), queried.getDeflectionMod());
		assertEquals("ac misc", m_combatStatSet.getACMiscMod(), queried.getACMiscMod());
		assertEquals("bab 1", m_combatStatSet.getBABPrimary(), queried.getBABPrimary());
		assertEquals("bab 2", m_combatStatSet.getBABSecondary(), queried.getBABSecondary());
		assertEquals("strength", m_combatStatSet.getCMBAbility(), queried.getCMBAbility());
		assertEquals("cmd dex", m_combatStatSet.getCMDAbility(), queried.getCMDAbility());
		assertEquals("cmd misc", m_combatStatSet.getCMDMiscMod(), queried.getCMDMiscMod());
		assertEquals("spell resist", m_combatStatSet.getSpellResist(), queried.getSpellResist());
	}
	
	public void testUpdate() {
		CombatStatSet toUpdate = new CombatStatSet(m_characterId);
		
		toUpdate.setTotalHP(51);
		toUpdate.setWounds(4);
		toUpdate.setNonLethalDamage(6);
		toUpdate.setDamageReduction(9);
		toUpdate.setBaseSpeed(31);
		toUpdate.setInitAbility(2);
		toUpdate.setInitiativeMiscMod(3);
		toUpdate.setACArmourBonus(11);
		toUpdate.setACShieldBonus(12);
		toUpdate.setACAbility(13);
		toUpdate.setSizeModifier(14);
		toUpdate.setNaturalArmour(15);
		toUpdate.setDeflectionMod(16);
		toUpdate.setACMiscMod(17);
		toUpdate.setBABPrimary(18);
		toUpdate.setBABSecondary("2/6/8");
		toUpdate.setCMBAbilityKey(19);
		toUpdate.setCMDAbility(47);
		toUpdate.setCMDMiscMod(57);
		toUpdate.setSpellResistance(68);
		
		m_repo.update(toUpdate);
		CombatStatSet updated = m_repo.query(m_characterId);
		assertEquals("id", toUpdate.getId(), updated.getId());
		assertEquals("total hp", toUpdate.getTotalHP(), updated.getTotalHP());
		assertEquals("wounds", toUpdate.getWounds(), updated.getWounds());
		assertEquals("non leth dmg", toUpdate.getNonLethalDamage(), updated.getNonLethalDamage());
		assertEquals("dmg red", toUpdate.getDamageReduction(), updated.getDamageReduction());
		assertEquals("base speed", toUpdate.getBaseSpeed(), updated.getBaseSpeed());
		assertEquals("init dex", toUpdate.getInitAbility(), updated.getInitAbility());
		assertEquals("init misc", toUpdate.getInitiativeMiscMod(), updated.getInitiativeMiscMod());
		assertEquals("ac armor", toUpdate.getACArmourBonus(), updated.getACArmourBonus());
		assertEquals("ac shield", toUpdate.getACShieldBonus(), updated.getACShieldBonus());
		assertEquals("ac dex", toUpdate.getACAbility(), updated.getACAbility());
		assertEquals("size", toUpdate.getSizeModifier(), updated.getSizeModifier());
		assertEquals("natural armor", toUpdate.getNaturalArmour(), updated.getNaturalArmour());
		assertEquals("deflect mod", toUpdate.getDeflectionMod(), updated.getDeflectionMod());
		assertEquals("ac misc", toUpdate.getACMiscMod(), updated.getACMiscMod());
		assertEquals("bab 1", toUpdate.getBABPrimary(), updated.getBABPrimary());
		assertEquals("bab 2", toUpdate.getBABSecondary(), updated.getBABSecondary());
		assertEquals("strength", toUpdate.getCMBAbility(), updated.getCMBAbility());
		assertEquals("cmd dex", toUpdate.getCMDAbility(), updated.getCMDAbility());
		assertEquals("cmd misc", toUpdate.getCMDMiscMod(), updated.getCMDMiscMod());
		assertEquals("spell resist", toUpdate.getSpellResist(), updated.getSpellResist());
	}

}
