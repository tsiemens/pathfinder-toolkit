package com.lateensoft.pathfinder.toolkit.test.model.character.stats;

import com.lateensoft.pathfinder.toolkit.model.character.stats.Ability;
import com.lateensoft.pathfinder.toolkit.model.character.stats.AbilitySet;

import junit.framework.TestCase;

public class AbilityTest extends TestCase {
	private Ability abilityScore;
	
	private static final int DEFAULT_SCORE = 10;
	private static final int[] SCORES = {7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18};
	private static final int[] COSTS = {-4, -2, -1, 0, 1, 2, 3, 5, 7, 10, 13, 17};
	
	protected void setUp() throws Exception {
		super.setUp();
		abilityScore = new Ability(AbilitySet.KEY_STR, DEFAULT_SCORE, 0);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetAbilityPointCost() {
		for(int i = 0; i < SCORES.length; i++) {
			abilityScore.setScore(SCORES[i]);
			assertEquals(abilityScore.getAbilityPointCost(), COSTS[i]);
		}
	}

}
