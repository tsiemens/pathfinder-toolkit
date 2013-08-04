package com.lateensoft.pathfinder.toolkit.datahelpers.test;

import com.lateensoft.pathfinder.toolkit.datahelpers.PTDatabase;

import android.test.AndroidTestCase;

public class TableCreatorTest extends AndroidTestCase {
	public void testCreation() {
		PTDatabase db = PTDatabase.getSharedInstance();
		db.open();
	}
}
