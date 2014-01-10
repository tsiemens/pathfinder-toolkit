package com.lateensoft.pathfinder.toolkit.test.db.repository;

import android.content.res.Resources;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.db.repository.PTSkillRepository;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTSave;
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
	
	// TODO remove
//	public void testInsert() {
//		long id = m_repo.insert(m_insertSkill);
//		assertTrue(id != INSERT_FAIL);
//	}
//	
	public void testQuery() {
		PTSkill queried = m_repo.query(m_skill.getID());
		assertEquals(m_skill.getID(), queried.getID());
		assertEquals(m_skill.getCharacterID(), queried.getCharacterID());
		assertEquals(m_skill.isClassSkill(), queried.isClassSkill());
		assertEquals(m_skill.getKeyAbility(), queried.getKeyAbility());
		assertEquals(m_skill.getKeyAbilityKey(), queried.getKeyAbilityKey());
		assertEquals(m_skill.getMiscMod(), queried.getMiscMod());
		assertEquals(m_skill.getArmorCheckPenalty(), queried.getArmorCheckPenalty());
		assertEquals(m_skill.getAbilityMod(), queried.getAbilityMod());
	}
	
	public void testUpdate() {
		PTSkill toUpdate = new PTSkill(m_skill.getID(), m_skill.getCharacterID(), m_skill.getName(),
				true, 3, 4, 5, 6, m_skill.getKeyAbilityKey(), m_skill.getKeyAbility());
	
		m_repo.update(toUpdate);
		PTSkill queried = m_repo.query(m_skill.getID());
		assertEquals(toUpdate.getID(), queried.getID());
		assertEquals(toUpdate.getCharacterID(), queried.getCharacterID());
		assertEquals(toUpdate.isClassSkill(), queried.isClassSkill());
		assertEquals(toUpdate.getKeyAbility(), queried.getKeyAbility());
		assertEquals(toUpdate.getKeyAbilityKey(), queried.getKeyAbilityKey());
		assertEquals(toUpdate.getMiscMod(), queried.getMiscMod());
		assertEquals(toUpdate.getArmorCheckPenalty(), queried.getArmorCheckPenalty());
		assertEquals(toUpdate.getAbilityMod(), queried.getAbilityMod());
	}
	
	// TODO remove, because we never manually delete skills
//	public void testDelete() {
//		assertTrue(false);
//		long id = m_repo.insert(m_insertSkill);
//		m_repo.delete(id);
//		boolean exception = false;
//		try {
//			@SuppressWarnings("unused")
//			PTSkill queried = m_repo.query(id);
//		}
//		catch(Exception e) {
//			exception = true;
//		}
//		finally {
//			assertTrue(exception);
//		}
//	}

	public void testQuerySet() {
		PTSkill[] queriedSkills = m_repo.querySet(m_characterId);
		Resources r = getContext().getResources();
		String[] names = r.getStringArray(R.array.skills);
		for (int i = 0; i < names.length; i++){
			assertTrue(queriedSkills[i].getName()+" should = "+names[i], 
					queriedSkills[i].getName().contentEquals(names[i]));
		}
	}
}
