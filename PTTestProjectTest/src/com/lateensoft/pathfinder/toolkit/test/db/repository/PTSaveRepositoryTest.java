package com.lateensoft.pathfinder.toolkit.test.db.repository;

import com.lateensoft.pathfinder.toolkit.db.repository.PTSaveRepository;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTAbilitySet;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTSave;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTSaveSet;

public class PTSaveRepositoryTest extends PTBaseRepositoryTest {
	private static final int BASE_VALUE = 8;
	private static final int ABILITY_KEY = PTAbilitySet.KEY_CHA;
	private static final int MAGIC_MOD = 5;
	private static final int MISC_MOD = -2;
	private static final int TEMP_MOD = 0;
	
	private static final int NEW_BASE_VALUE = 3;
	private static final int NEW_ABILITY_KEY = 1;
	private static final int NEW_MAGIC_MOD = -1;
	private static final int NEW_MISC_MOD = 17;
	private static final int NEW_TEMP_MOD = 2;
	
	private static final int SAVE_INDEX = 2;
	private PTSave m_save;
	private PTSaveRepository m_repo;
	
	public void setUp() {
		super.setUp();
		m_repo = new PTSaveRepository();
		m_save = m_repo.querySet(m_characterId)[SAVE_INDEX];
		m_save.setBaseSave(BASE_VALUE);
		m_save.setAbilityKey(ABILITY_KEY);
		m_save.setMagicMod(MAGIC_MOD);
		m_save.setMiscMod(MISC_MOD);
		m_save.setTempMod(TEMP_MOD);
		m_repo.update(m_save);
	}
	
	public void testQuery() {
		PTSave queried = m_repo.query(m_save.getID(), m_characterId);
		assertEquals(m_save.getCharacterID(), queried.getCharacterID());
		assertEquals(m_save.getBaseSave(), queried.getBaseSave());
		assertEquals(m_save.getAbilityKey(), queried.getAbilityKey());
		assertEquals(m_save.getMagicMod(), queried.getMagicMod());
		assertEquals(m_save.getMiscMod(), queried.getMiscMod());
		assertEquals(m_save.getTempMod(), queried.getTempMod());
	}
	
	public void testUpdate() {
		PTSave toUpdate = new PTSave(m_save.getSaveKey(), m_characterId, NEW_BASE_VALUE, 
				NEW_ABILITY_KEY, NEW_MAGIC_MOD, NEW_MISC_MOD, NEW_TEMP_MOD);
		m_repo.update(toUpdate);
		PTSave updated = m_repo.query(m_save.getID(), m_characterId);
		assertEquals(updated.getSaveKey(), toUpdate.getSaveKey());
		assertEquals(updated.getCharacterID(), toUpdate.getCharacterID());
		assertEquals(updated.getBaseSave(), toUpdate.getBaseSave());
		assertEquals(updated.getAbilityKey(), toUpdate.getAbilityKey());
		assertEquals(updated.getMagicMod(), toUpdate.getMagicMod());
		assertEquals(updated.getMiscMod(), toUpdate.getMiscMod());
		assertEquals(updated.getTempMod(), toUpdate.getTempMod());
	}
	
	public void testQuerySet() {
		PTSave[] queriedSaves = m_repo.querySet(m_characterId);
		for (int i = 0; i < PTSaveSet.SAVE_KEYS.length; i++){
			assertEquals(queriedSaves[i].getSaveKey(), PTSaveSet.SAVE_KEYS[i]);
		}
	}
}
