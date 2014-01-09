package com.lateensoft.pathfinder.toolkit.test.db.repository;

import com.lateensoft.pathfinder.toolkit.db.repository.PTSaveRepository;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTSave;

public class PTSaveRepositoryTest extends PTBaseRepositoryTest {
	private static final String NAME = "Fort";
	private static final int BASE_VALUE = 8;
	private static final int ABILITY_MOD = 3;
	private static final int MAGIC_MOD = 5;
	private static final int MISC_MOD = -2;
	private static final int TEMP_MOD = 0;
	
	private static final String NEW_NAME = "Will";
	private static final int NEW_BASE_VALUE = 3;
	private static final int NEW_ABILITY_MOD = 1;
	private static final int NEW_MAGIC_MOD = -1;
	private static final int NEW_MISC_MOD = 17;
	private static final int NEW_TEMP_MOD = 2;
	
	private PTSave m_insertSave;
	private PTSaveRepository m_repo;
	
	public void setUp() {
		super.setUp();
		m_insertSave = new PTSave(m_characterId, NAME, BASE_VALUE, ABILITY_MOD, 
				MAGIC_MOD, MISC_MOD, TEMP_MOD);
		m_repo = new PTSaveRepository();
	}
	
	public void testInsert() {
		long id = m_repo.insert(m_insertSave);
		assertTrue(id != INSERT_FAIL);
	}
	
	public void testQuery() {
		long id = m_repo.insert(m_insertSave);
		PTSave queried = m_repo.query(id);
		assertEquals(m_insertSave.getCharacterID(), queried.getCharacterID());
		assertEquals(m_insertSave.getName(), queried.getName());
		assertEquals(m_insertSave.getBaseValue(), queried.getBaseValue());
		assertEquals(m_insertSave.getTotal(), queried.getTotal());
		assertEquals(m_insertSave.getAbilityMod(), queried.getAbilityMod());
		assertEquals(m_insertSave.getMagicMod(), queried.getMagicMod());
		assertEquals(m_insertSave.getMiscMod(), queried.getMiscMod());
		assertEquals(m_insertSave.getTempMod(), queried.getTempMod());
	}
	
	public void testUpdate() {
		long id = m_repo.insert(m_insertSave);
		PTSave toUpdate = new PTSave((int) id, m_characterId, NEW_NAME, NEW_BASE_VALUE, 
				NEW_ABILITY_MOD, NEW_MAGIC_MOD, NEW_MISC_MOD, NEW_TEMP_MOD);
		m_repo.update(toUpdate);
		PTSave updated = m_repo.query(id);
		assertEquals(updated.getCharacterID(), toUpdate.getCharacterID());
		assertEquals(updated.getName(), toUpdate.getName());
		assertEquals(updated.getBaseValue(), toUpdate.getBaseValue());
		assertEquals(updated.getAbilityMod(), toUpdate.getAbilityMod());
		assertEquals(updated.getMagicMod(), toUpdate.getMagicMod());
		assertEquals(updated.getMiscMod(), toUpdate.getMiscMod());
		assertEquals(updated.getTempMod(), toUpdate.getTempMod());
	}
	
	public void testDelete() {
		long id = m_repo.insert(m_insertSave);
		m_repo.delete(id);
		boolean exception = false;
		try {
			@SuppressWarnings("unused")
			PTSave queried = m_repo.query(id);
		}
		catch(Exception e) {
			exception = true;
		}
		finally {
			assertTrue(exception);
		}
	}
}
