package com.lateensoft.pathfinder.toolkit.test;

import com.lateensoft.pathfinder.toolkit.stats.PTStat;

import junit.framework.TestCase;

public class PTStatTest extends TestCase {
	private PTStat stat;
	private static final String STAT_NAME_INITIAL = "Name";
	private static final String STAT_NAME_FINAL = "Another Name";
	private static final int VALUE_INITIAL = 10;
	private static final int VALUE_FINAL = 42;
	
	protected void setUp() throws Exception {
		super.setUp();
		stat = new PTStat(STAT_NAME_INITIAL, VALUE_INITIAL);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testPTStatString() {
		stat = new PTStat(STAT_NAME_INITIAL);
		assertEquals(stat.getName(), STAT_NAME_INITIAL);
	}

	public void testPTStatStringInt() {
		assertEquals(stat.getName(), STAT_NAME_INITIAL);
		assertEquals(stat.getBaseValue(),VALUE_INITIAL);
	}

	public void testPTStatPTStat() {
		PTStat stat2 = new PTStat(stat);
		assertEquals(stat2.getName(), stat.getName());
		assertEquals(stat2.getBaseValue(), stat.getBaseValue());
	}

	public void testGetName() {
		assertEquals(STAT_NAME_INITIAL, stat.getName());
	}

	public void testGetBaseValue() {
		assertEquals(VALUE_INITIAL, stat.getBaseValue());
	}

	public void testSetName() {
		stat.setName(STAT_NAME_FINAL);
		assertEquals(STAT_NAME_FINAL, stat.getName());
	}

	public void testSetBaseValue() {
		stat.setBaseValue(VALUE_FINAL);
		assertEquals(VALUE_FINAL, stat.getBaseValue());
	}

}
