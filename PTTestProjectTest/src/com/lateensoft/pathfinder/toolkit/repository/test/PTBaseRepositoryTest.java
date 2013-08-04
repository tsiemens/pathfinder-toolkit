package com.lateensoft.pathfinder.toolkit.repository.test;

import com.lateensoft.pathfinder.toolkit.PTBaseApplication;
import com.lateensoft.pathfinder.toolkit.character.sheet.PTCharacter;
import com.lateensoft.pathfinder.toolkit.datahelpers.PTDatabase;
import com.lateensoft.pathfinder.toolkit.repository.PTCharacterRepository;

import android.test.AndroidTestCase;

public class PTBaseRepositoryTest extends AndroidTestCase {
	protected PTDatabase mDatabase;
	protected final String CHARACTER_TAG = "Joe";
	protected final String CHARACTER_ID;
	
	protected void setUp() {
		mDatabase = PTDatabase.getSharedInstance();
		PTCharacter joe = new PTCharacter("Joe", PTBaseApplication.getAppContext());
		PTCharacterRepository repo = new PTCharacterRepository();
		repo.insert(joe);
		// TODO need to get id on insert
	}
}
