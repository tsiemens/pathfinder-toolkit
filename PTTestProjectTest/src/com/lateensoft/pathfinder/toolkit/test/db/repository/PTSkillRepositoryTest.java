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
		assertEquals(m_skill.getID(), queried.getID());
		assertEquals(m_skill.getCharacterID(), queried.getCharacterID());
		assertEquals(m_skill.isClassSkill(), queried.isClassSkill());
		assertEquals(m_skill.getAbilityId(), queried.getAbilityId());
		assertEquals(m_skill.getMiscMod(), queried.getMiscMod());
		assertEquals(m_skill.getArmorCheckPenalty(), queried.getArmorCheckPenalty());
	}
	
	public void testUpdate() {
		PTSkill toUpdate = new PTSkill(m_skill.getID(), m_skill.getCharacterID(),
				true, 3, 4, 5, m_skill.getAbilityId());
	
		m_repo.update(toUpdate);
		PTSkill queried = m_repo.query(m_skill.getID(), m_skill.getCharacterID());
		assertEquals(toUpdate.getID(), queried.getID());
		assertEquals(toUpdate.getCharacterID(), queried.getCharacterID());
		assertEquals(toUpdate.isClassSkill(), queried.isClassSkill());
		assertEquals(toUpdate.getAbilityId(), queried.getAbilityId());
		assertEquals(toUpdate.getMiscMod(), queried.getMiscMod());
		assertEquals(toUpdate.getArmorCheckPenalty(), queried.getArmorCheckPenalty());
	}

	public void testQuerySet() {
		PTSkill[] queriedSkills = m_repo.querySet(m_characterId);
		for (int i = 0; i < PTSkillSet.SKILL_IDS.length; i++){
			assertEquals(PTSkillSet.SKILL_IDS[i], queriedSkills[i].getID()); 
		}
	}
}
