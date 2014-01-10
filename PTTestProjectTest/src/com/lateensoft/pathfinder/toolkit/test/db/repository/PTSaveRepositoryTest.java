package com.lateensoft.pathfinder.toolkit.test.db.repository;

import android.content.res.Resources;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.db.repository.PTSaveRepository;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTSave;

public class PTSaveRepositoryTest extends PTBaseRepositoryTest {
	private static final int BASE_VALUE = 8;
	private static final int ABILITY_MOD = 3;
	private static final int MAGIC_MOD = 5;
	private static final int MISC_MOD = -2;
	private static final int TEMP_MOD = 0;
	
	private static final int NEW_BASE_VALUE = 3;
	private static final int NEW_ABILITY_MOD = 1;
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
		m_save.setBaseValue(BASE_VALUE);
		m_save.setAbilityMod(ABILITY_MOD);
		m_save.setMagicMod(MAGIC_MOD);
		m_save.setMiscMod(MISC_MOD);
		m_save.setTempMod(TEMP_MOD);
		m_repo.update(m_save);
	}
	
	public void testQuery() {
		PTSave queried = m_repo.query(m_save.getID());
		assertEquals(m_save.getCharacterID(), queried.getCharacterID());
		assertEquals(m_save.getName(), queried.getName());
		assertEquals(m_save.getBaseValue(), queried.getBaseValue());
		assertEquals(m_save.getTotal(), queried.getTotal());
		assertEquals(m_save.getAbilityMod(), queried.getAbilityMod());
		assertEquals(m_save.getMagicMod(), queried.getMagicMod());
		assertEquals(m_save.getMiscMod(), queried.getMiscMod());
		assertEquals(m_save.getTempMod(), queried.getTempMod());
	}
	
	public void testUpdate() {
		PTSave toUpdate = new PTSave(m_save.getID(), m_characterId, m_save.getName(), NEW_BASE_VALUE, 
				NEW_ABILITY_MOD, NEW_MAGIC_MOD, NEW_MISC_MOD, NEW_TEMP_MOD);
		m_repo.update(toUpdate);
		PTSave updated = m_repo.query(m_save.getID());
		assertEquals(updated.getCharacterID(), toUpdate.getCharacterID());
		assertEquals(updated.getName(), toUpdate.getName());
		assertEquals(updated.getBaseValue(), toUpdate.getBaseValue());
		assertEquals(updated.getAbilityMod(), toUpdate.getAbilityMod());
		assertEquals(updated.getMagicMod(), toUpdate.getMagicMod());
		assertEquals(updated.getMiscMod(), toUpdate.getMiscMod());
		assertEquals(updated.getTempMod(), toUpdate.getTempMod());
	}
	
	public void testQuerySet() {
		PTSave[] queriedSaves = m_repo.querySet(m_characterId);
		Resources r = getContext().getResources();
		String[] names = r.getStringArray(R.array.save_names);
		for (int i = 0; i < names.length; i++){
			assertTrue(queriedSaves[i].getName()+" should = "+names[i], 
					queriedSaves[i].getName().contentEquals(names[i]));
		}
	}
}
