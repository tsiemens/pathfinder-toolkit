package com.lateensoft.pathfinder.toolkit.test.db.repository;

import com.lateensoft.pathfinder.toolkit.db.repository.PTAbilityRepository;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTAbility;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTAbilitySet;

public class PTAbilityRepositoryTest extends PTBaseRepositoryTest {
	
	private PTAbility m_ability;
	private PTAbilityRepository m_repo;
	
	public void setUp() {
		super.setUp();
		m_repo = new PTAbilityRepository();

		m_ability = m_repo.querySet(m_characterId)[2];
	}
	
	public void testQuery() {
		PTAbility queried = m_repo.query(m_ability.getID(), m_characterId);
		assertEquals(m_ability, queried);
	}
	
	public void testUpdate() {
		PTAbility toUpdate = new PTAbility(m_ability.getID(), m_ability.getCharacterID(),
				8, -2);
		m_repo.update(toUpdate);
		PTAbility updated = m_repo.query(m_ability.getID(), m_characterId);
		assertEquals(updated, toUpdate);
	}
	
	public void testQuerySet() {
		PTAbility[] queriedAbilityScores = m_repo.querySet(m_characterId);

		for (int i = 0; i < PTAbilitySet.ABILITY_IDS.length; i++){
			assertEquals(PTAbilitySet.ABILITY_IDS[i], queriedAbilityScores[i].getID());
		}
	}
	
	private static void assertEquals(PTAbility score1, PTAbility score2) {
		assertEquals(score1.getTempBonus(), score2.getTempBonus());
		assertEquals(score1.getScore(), score2.getScore());
		assertEquals(score1.getCharacterID(), score2.getCharacterID());
		assertEquals(score1.getID(), score2.getID());
	}
}
