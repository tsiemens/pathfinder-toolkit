package com.lateensoft.pathfinder.toolkit.test.db.repository;

import java.util.List;

import com.lateensoft.pathfinder.toolkit.db.repository.AbilityRepository;
import com.lateensoft.pathfinder.toolkit.model.character.stats.Ability;
import com.lateensoft.pathfinder.toolkit.model.character.stats.AbilitySet;

public class AbilityRepositoryTest extends BaseRepositoryTest {
	
	private Ability m_ability;
	private AbilityRepository m_repo;
	
	public void setUp() {
		super.setUp();
		m_repo = new AbilityRepository();

		m_ability = m_repo.queryAllForCharacter(m_characterId).get(2);
	}
	
	public void testQuery() {
		Ability queried = m_repo.query(m_ability.getId(), m_characterId);
		assertEquals(m_ability, queried);
	}
	
	public void testUpdate() {
		Ability toUpdate = new Ability(m_ability.getAbilityKey(), m_ability.getCharacterID(),
				8, -2);
		m_repo.update(toUpdate);
		Ability updated = m_repo.query(m_ability.getId(), m_characterId);
		assertEquals(updated, toUpdate);
	}
	
	public void testQuerySet() {
		List<Ability> queriedAbilityScores = m_repo.queryAllForCharacter(m_characterId);

        for (int i = 0; i < AbilitySet.ABILITY_KEYS.size(); i++){
			assertEquals(AbilitySet.ABILITY_KEYS.get(i).intValue(), queriedAbilityScores.get(i).getId());
		}
	}
	
	private static void assertEquals(Ability score1, Ability score2) {
		assertEquals(score1.getTempBonus(), score2.getTempBonus());
		assertEquals(score1.getScore(), score2.getScore());
		assertEquals(score1.getCharacterID(), score2.getCharacterID());
		assertEquals(score1.getId(), score2.getId());
	}
}
