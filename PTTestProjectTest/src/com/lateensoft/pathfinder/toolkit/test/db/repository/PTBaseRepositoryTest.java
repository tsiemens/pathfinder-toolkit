package com.lateensoft.pathfinder.toolkit.test.db.repository;

import com.lateensoft.pathfinder.toolkit.PTBaseApplication;
import com.lateensoft.pathfinder.toolkit.db.PTDatabase;
import com.lateensoft.pathfinder.toolkit.db.repository.PTCharacterRepository;
import com.lateensoft.pathfinder.toolkit.model.character.PTCharacter;

import android.test.AndroidTestCase;

public class PTBaseRepositoryTest extends AndroidTestCase {
	protected PTDatabase mDatabase;
	protected final String CHARACTER_TAG = "Joe";
	protected long mCharacterId;
	protected final int INSERT_FAIL = -1;
	
	protected void setUp() {
		mDatabase = PTDatabase.getSharedInstance();
		PTCharacter joe = new PTCharacter(CHARACTER_TAG, PTBaseApplication.getAppContext());
		PTCharacterRepository repo = new PTCharacterRepository();
		mCharacterId = repo.insert(joe);
		assertTrue(mCharacterId != INSERT_FAIL);
	}
}
