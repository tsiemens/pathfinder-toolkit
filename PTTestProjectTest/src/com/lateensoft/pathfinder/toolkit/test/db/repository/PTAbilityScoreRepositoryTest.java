package com.lateensoft.pathfinder.toolkit.test.db.repository;

import com.lateensoft.pathfinder.toolkit.db.repository.PTAbilityScoreRepository;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTAbility;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTAbilitySet;

public class PTAbilityScoreRepositoryTest extends PTBaseRepositoryTest {
	
	private PTAbility m_abilityScore;
	private PTAbility m_tempAbilityScore;
	private PTAbilityScoreRepository m_repo;
	
	public void setUp() {
		super.setUp();
		m_repo = new PTAbilityScoreRepository();

		m_abilityScore = m_repo.querySet(m_characterId, false)[2];
		m_tempAbilityScore = m_repo.querySet(m_characterId, true)[0];
	}
	
	public void testQuery() {
		PTAbility queried = m_repo.query(m_abilityScore.getID());
		assertEquals(m_abilityScore, queried);
		queried = m_repo.query(m_tempAbilityScore.getID());
		assertEquals(m_tempAbilityScore, queried);
	}
	
	public void testUpdate() {
		PTAbility toUpdate = new PTAbility(m_abilityScore.getID(), m_abilityScore.getCharacterID(),
				"Invalid name", 7);
		m_repo.update(toUpdate);
		PTAbility updated = m_repo.query(m_abilityScore.getID());
		assertEquals(updated, toUpdate);
		
		toUpdate = new PTAbility(m_tempAbilityScore.getID(), m_tempAbilityScore.getCharacterID(),
				"temp Invalid name", 8);
		m_repo.update(toUpdate);
		updated = m_repo.query(m_tempAbilityScore.getID());
		assertEquals(updated, toUpdate);
	}
	
	public void testQuerySet() {
		PTAbility[] queriedAbilityScores = m_repo.querySet(m_characterId, false);

		for (int i = 0; i < PTAbilitySet.ABILITY_NAMES.length; i++){
			assertTrue(queriedAbilityScores[i].getAbility()+" should = "+PTAbilitySet.ABILITY_NAMES[i], 
					queriedAbilityScores[i].getAbility().contentEquals(PTAbilitySet.ABILITY_NAMES[i]));
		}
	}
	
	private static void assertEquals(PTAbility score1, PTAbility score2) {
		assertEquals(score1.getAbility(), score2.getAbility());
		assertEquals(score1.getScore(), score2.getScore());
		assertEquals(score1.getCharacterID(), score2.getCharacterID());
		assertEquals(score1.getID(), score2.getID());
	}
}
