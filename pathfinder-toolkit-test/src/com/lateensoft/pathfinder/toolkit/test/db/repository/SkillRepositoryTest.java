package com.lateensoft.pathfinder.toolkit.test.db.repository;

import java.util.List;

import com.lateensoft.pathfinder.toolkit.db.repository.SkillRepository;
import com.lateensoft.pathfinder.toolkit.model.character.stats.Skill;
import com.lateensoft.pathfinder.toolkit.model.character.stats.SkillSet;

public class SkillRepositoryTest extends BaseRepositoryTest {
	private Skill m_skill;
	private SkillRepository m_repo;
	
	public void setUp() {
		super.setUp();
		m_repo = new SkillRepository();
		
		m_skill = m_repo.queryAllForCharacter(m_characterId).get(0);
	}
	
	public void testQuery() {
		Skill queried = m_repo.query(m_skill.getId(), m_skill.getCharacterID());
		assertEquals(m_skill, queried);
	}
	
	public void testUpdate() {
		Skill toUpdate = new Skill(m_skill.getId(), m_skill.getCharacterID(), m_skill.getSkillKey(),
				"derp", true, 3, 4, m_skill.getAbilityKey());
	
		m_repo.update(toUpdate);
		Skill queried = m_repo.query(m_skill.getId(), m_skill.getCharacterID());
		assertEquals(toUpdate, queried);
	}

	public void testQuerySet() {
		List<Skill> queriedSkills = m_repo.queryAllForCharacter(m_characterId);
        for (int i = 0; i < SkillSet.SKILL_KEYS.size(); i++){
			assertEquals(SkillSet.SKILL_KEYS.get(i).intValue(), queriedSkills.get(i).getSkillKey());
		}
	}
	
	private static void assertEquals(Skill expected, Skill actual) {
		assertEquals(expected.getId(), actual.getId());
		assertEquals(expected.getSkillKey(), actual.getSkillKey());
		assertEquals(expected.getCharacterID(), actual.getCharacterID());
		assertEquals(expected.getSubType(), actual.getSubType());
		assertEquals(expected.isClassSkill(), actual.isClassSkill());
		assertEquals(expected.getAbilityKey(), actual.getAbilityKey());
		assertEquals(expected.getMiscMod(), actual.getMiscMod());
	}
}
