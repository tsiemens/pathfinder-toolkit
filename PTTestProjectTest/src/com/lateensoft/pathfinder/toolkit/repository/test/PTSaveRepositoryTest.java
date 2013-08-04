package com.lateensoft.pathfinder.toolkit.repository.test;

import com.lateensoft.pathfinder.toolkit.repository.PTSaveRepository;
import com.lateensoft.pathfinder.toolkit.stats.PTSave;

public class PTSaveRepositoryTest extends PTBaseRepositoryTest {
	static final int ID = 4;
	static final int CHARACTER_ID = 24;
	static final String NAME = "Fort";
	static final int BASE_VALUE = 8;
	static final int TOTAL = 14;
	static final int ABILITY_MOD = 3;
	static final int MAGIC_MOD = 5;
	static final int MISC_MOD = -2;
	static final int TEMP_MOD = 0;
	
	public void testInsert() {
		PTSave inserted = new PTSave(ID, CHARACTER_ID, NAME, BASE_VALUE, ABILITY_MOD, 
				MAGIC_MOD, MISC_MOD, TEMP_MOD);
		PTSaveRepository repo = new PTSaveRepository();
		repo.insert(inserted);
		PTSave queried = repo.query(ID);
		assertEquals(inserted.getAbilityMod(), queried.getAbilityMod());
		assertEquals(inserted.getBase(), queried.getBase());
		assertEquals(inserted.getBaseValue(), queried.getBaseValue());
		assertEquals(inserted.getID(), queried.getID());
		assertEquals(inserted.getMagicMod(), queried.getMagicMod());
		assertEquals(inserted.getMiscMod(), inserted.getMiscMod());
		assertEquals(inserted.getName(), queried.getName());
		assertEquals(inserted.getTempMod(), queried.getTempMod());
		assertEquals(inserted.getCharacterID(), queried.getCharacterID());
	}
}
