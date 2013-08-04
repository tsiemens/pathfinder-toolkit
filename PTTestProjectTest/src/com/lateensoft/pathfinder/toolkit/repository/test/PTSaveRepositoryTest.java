package com.lateensoft.pathfinder.toolkit.repository.test;

import com.lateensoft.pathfinder.toolkit.repository.PTSaveRepository;
import com.lateensoft.pathfinder.toolkit.stats.PTSave;

public class PTSaveRepositoryTest extends PTBaseRepositoryTest {
	static final int ID = 4;
	static final String NAME = "Fort";
	static final int BASE_VALUE = 8;
	static final int TOTAL = 14;
	static final int ABILITY_MOD = 3;
	static final int MAGIC_MOD = 5;
	static final int MISC_MOD = -2;
	static final int TEMP_MOD = 0;
	
	static final String NEW_NAME = "Will";
	static final int NEW_BASE_VALUE = 3;
	static final int NEW_ABILITY_MOD = 1;
	static final int NEW_MAGIC_MOD = -1;
	static final int NEW_MISC_MOD = 17;
	static final int NEW_TEMP_MOD = 2;
	
	private PTSave mInsert;
	private PTSaveRepository mRepo;
	
	public void setUp() {
		super.setUp();
		mInsert = new PTSave(mCharacterId, NAME, BASE_VALUE, ABILITY_MOD, 
				MAGIC_MOD, MISC_MOD, TEMP_MOD);
		mRepo = new PTSaveRepository();
	}
	
	public void testInsert() {
		long id = mRepo.insert(mInsert);
		assertTrue(id != INSERT_FAIL);
	}
	
	public void testQuery() {
		long id = mRepo.insert(mInsert);
		PTSave queried = mRepo.query(id);
		assertEquals(mInsert.getCharacterID(), queried.getCharacterID());
		assertEquals(mInsert.getName(), queried.getName());
		assertEquals(mInsert.getBaseValue(), queried.getBaseValue());
		assertEquals(mInsert.getTotal(), queried.getTotal());
		assertEquals(mInsert.getAbilityMod(), queried.getAbilityMod());
		assertEquals(mInsert.getMagicMod(), queried.getMagicMod());
		assertEquals(mInsert.getMiscMod(), queried.getMiscMod());
		assertEquals(mInsert.getTempMod(), queried.getTempMod());
	}
	
	public void testUpdate() {
		long id = mRepo.insert(mInsert);
		PTSave toUpdate = new PTSave((int) id, mCharacterId, NEW_NAME, NEW_BASE_VALUE, 
				NEW_ABILITY_MOD, NEW_MAGIC_MOD, NEW_MISC_MOD, NEW_TEMP_MOD);
		mRepo.update(toUpdate);
		PTSave updated = mRepo.query(id);
		assertEquals(updated.getCharacterID(), toUpdate.getCharacterID());
		assertEquals(updated.getName(), toUpdate.getName());
		assertEquals(updated.getBaseValue(), toUpdate.getBaseValue());
		assertEquals(updated.getAbilityMod(), toUpdate.getAbilityMod());
		assertEquals(updated.getMagicMod(), toUpdate.getMagicMod());
		assertEquals(updated.getMiscMod(), toUpdate.getMiscMod());
		assertEquals(updated.getTempMod(), toUpdate.getTempMod());
	}
	
	public void testDelete() {
		long id = mRepo.insert(mInsert);
		mRepo.delete(id);
		boolean exception = false;
		try {
			@SuppressWarnings("unused")
			PTSave queried = mRepo.query(id);
		}
		catch(Exception e) {
			exception = true;
		}
		finally {
			assertTrue(exception);
		}
	}
}
