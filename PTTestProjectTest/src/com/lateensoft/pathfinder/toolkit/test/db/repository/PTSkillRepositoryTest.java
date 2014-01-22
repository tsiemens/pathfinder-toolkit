package com.lateensoft.pathfinder.toolkit.test.db.repository;

import com.lateensoft.pathfinder.toolkit.db.repository.PTSkillRepository;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTSkill;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTSkillSet;

public class PTSkillRepositoryTest extends PTBaseRepositoryTest {	
	private PTSkill m_skill;
	private PTSkillRepository m_repo;
	
	public void setUp() {
		super.setUp();
		m_repo = new PTSkillRepository();
		
		m_skill = m_repo.querySet(m_characterId)[0];
	}
	
	public void testQuery() {
		PTSkill queried = m_repo.query(m_skill.getID(), m_skill.getCharacterID());
		assertEquals(m_skill, queried);
	}
	
	public void testUpdate() {
		PTSkill toUpdate = new PTSkill(m_skill.getID(), m_skill.getCharacterID(), m_skill.getSkillKey(),
				"derp", true, 3, 4, 5, m_skill.getAbilityKey());
	
		m_repo.update(toUpdate);
		PTSkill queried = m_repo.query(m_skill.getID(), m_skill.getCharacterID());
		assertEquals(toUpdate, queried);
	}

	public void testQuerySet() {
		PTSkill[] queriedSkills = m_repo.querySet(m_characterId);
		for (int i = 0; i < PTSkillSet.SKILL_KEYS.length; i++){
			assertEquals(PTSkillSet.SKILL_KEYS[i], queriedSkills[i].getSkillKey()); 
		}
	}
	
	private static void assertEquals(PTSkill expected, PTSkill actual) {
		assertEquals(expected.getID(), actual.getID());
		assertEquals(expected.getSkillKey(), actual.getSkillKey());
		assertEquals(expected.getCharacterID(), actual.getCharacterID());
		assertEquals(expected.getSubType(), actual.getSubType());
		assertEquals(expected.isClassSkill(), actual.isClassSkill());
		assertEquals(expected.getAbilityKey(), actual.getAbilityKey());
		assertEquals(expected.getMiscMod(), actual.getMiscMod());
		assertEquals(expected.getArmorCheckPenalty(), actual.getArmorCheckPenalty());
	}
}
