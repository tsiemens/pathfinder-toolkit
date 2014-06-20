package com.lateensoft.pathfinder.toolkit.test.db.repository;

import java.util.List;

import com.lateensoft.pathfinder.toolkit.db.repository.SaveRepository;
import com.lateensoft.pathfinder.toolkit.model.character.stats.AbilitySet;
import com.lateensoft.pathfinder.toolkit.model.character.stats.Save;
import com.lateensoft.pathfinder.toolkit.model.character.stats.SaveSet;

public class SaveRepositoryTest extends BaseRepositoryTest {
	private static final int BASE_VALUE = 8;
	private static final int ABILITY_KEY = AbilitySet.KEY_CHA;
	private static final int MAGIC_MOD = 5;
	private static final int MISC_MOD = -2;
	private static final int TEMP_MOD = 0;
	
	private static final int NEW_BASE_VALUE = 3;
	private static final int NEW_ABILITY_KEY = 1;
	private static final int NEW_MAGIC_MOD = -1;
	private static final int NEW_MISC_MOD = 17;
	private static final int NEW_TEMP_MOD = 2;
	
	private static final int SAVE_INDEX = 2;
	private Save m_save;
	private SaveRepository m_repo;
	
	public void setUp() {
		super.setUp();
		m_repo = new SaveRepository();
		m_save = m_repo.queryAllForCharacter(m_characterId).get(SAVE_INDEX);
		m_save.setBaseSave(BASE_VALUE);
		m_save.setAbilityType(ABILITY_KEY);
		m_save.setMagicMod(MAGIC_MOD);
		m_save.setMiscMod(MISC_MOD);
		m_save.setTempMod(TEMP_MOD);
		m_repo.update(m_save);
	}
	
	public void testQuery() {
		Save queried = m_repo.query(m_save.getId(), m_characterId);
		assertEquals(m_save.getCharacterID(), queried.getCharacterID());
		assertEquals(m_save.getBaseSave(), queried.getBaseSave());
		assertEquals(m_save.getAbilityType(), queried.getAbilityType());
		assertEquals(m_save.getMagicMod(), queried.getMagicMod());
		assertEquals(m_save.getMiscMod(), queried.getMiscMod());
		assertEquals(m_save.getTempMod(), queried.getTempMod());
	}
	
	public void testUpdate() {
		Save toUpdate = new Save(m_save.getType(), m_characterId, NEW_BASE_VALUE,
				NEW_ABILITY_KEY, NEW_MAGIC_MOD, NEW_MISC_MOD, NEW_TEMP_MOD);
		m_repo.update(toUpdate);
		Save updated = m_repo.query(m_save.getId(), m_characterId);
		assertEquals(updated.getType(), toUpdate.getType());
		assertEquals(updated.getCharacterID(), toUpdate.getCharacterID());
		assertEquals(updated.getBaseSave(), toUpdate.getBaseSave());
		assertEquals(updated.getAbilityType(), toUpdate.getAbilityType());
		assertEquals(updated.getMagicMod(), toUpdate.getMagicMod());
		assertEquals(updated.getMiscMod(), toUpdate.getMiscMod());
		assertEquals(updated.getTempMod(), toUpdate.getTempMod());
	}
	
	public void testQuerySet() {
		List<Save> queriedSaves = m_repo.queryAllForCharacter(m_characterId);

        for (int i = 0; i < SaveSet.SAVE_KEYS.size(); i++){
			assertEquals(queriedSaves.get(i).getType(), SaveSet.SAVE_KEYS.get(i).intValue());
		}
	}
}
