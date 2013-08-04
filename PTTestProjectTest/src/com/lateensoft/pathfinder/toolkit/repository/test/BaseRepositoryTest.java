package com.lateensoft.pathfinder.toolkit.repository.test;

import com.lateensoft.pathfinder.toolkit.PTBaseApplication;
import com.lateensoft.pathfinder.toolkit.character.sheet.PTCharacter;
import com.lateensoft.pathfinder.toolkit.datahelpers.PTDatabase;

import android.test.AndroidTestCase;

public class BaseRepositoryTest extends AndroidTestCase {
	protected PTDatabase mDatabase;
	
	protected void setUp() {
		mDatabase = PTDatabase.getSharedInstance();
		//mDatabase.forceCreate();
		PTCharacter joe = new PTCharacter("Joe", PTBaseApplication.getAppContext());
		// need character repo first
	}
}
