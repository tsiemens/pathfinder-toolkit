package com.lateensoft.pathfinder.toolkit.test;

import com.lateensoft.pathfinder.toolkit.stats.PTSave;

import junit.framework.TestCase;

public class PTSaveTest extends TestCase {
	private PTSave save;
	
	private static final String NAME = "Name";
	private static final int BASE = 1;
	private static final int ABILITY = 2;
	private static final int MAGIC = 3;
	private static final int MISC = 4;
	private static final int TEMP = 5;
	
	protected void setUp() throws Exception {
		super.setUp();
		save = new PTSave(NAME, BASE, ABILITY, MAGIC, MISC, TEMP);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetTotal() {
		int total = BASE + ABILITY + MAGIC + MISC + TEMP;
		assertEquals(save.getTotal(), total);
	}

}
