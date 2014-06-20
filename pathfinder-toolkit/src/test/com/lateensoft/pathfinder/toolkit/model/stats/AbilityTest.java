package com.lateensoft.pathfinder.toolkit.model.stats;

import com.lateensoft.pathfinder.toolkit.model.character.stats.Ability;
import com.lateensoft.pathfinder.toolkit.model.character.stats.AbilitySet;
import com.lateensoft.pathfinder.toolkit.model.character.stats.AbilityType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class AbilityTest {
	private Ability abilityScore;
	
	private static final int DEFAULT_SCORE = 10;
	private static final int[] SCORES = {7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18};
	private static final int[] COSTS = {-4, -2, -1, 0, 1, 2, 3, 5, 7, 10, 13, 17};

    @Before
	public void setUp() throws Exception {
		abilityScore = new Ability(AbilityType.STR, DEFAULT_SCORE, 0);
	}

    @Test
	public void testGetAbilityPointCost() {
		for(int i = 0; i < SCORES.length; i++) {
			abilityScore.setScore(SCORES[i]);
			assertEquals(COSTS[i], abilityScore.getAbilityPointCost());
		}
	}

    @Test
    public void calculateModifierZero() {
        abilityScore.setScore(10);
        abilityScore.setTempBonus(0);
        assertEquals(0, abilityScore.getAbilityModifier());
    }

    @Test
    public void calculateTempModifierZero() {
        abilityScore.setScore(10);
        abilityScore.setTempBonus(0);
        assertEquals(0, abilityScore.getTempModifier());
    }

    @Test
    public void calculateModifierPositive() {
        abilityScore.setScore(18);
        abilityScore.setTempBonus(3);
        assertEquals(4, abilityScore.getAbilityModifier());
    }

    @Test
    public void calculateTempModifierPositive() {
        abilityScore.setScore(18);
        abilityScore.setTempBonus(3);
        assertEquals(5, abilityScore.getTempModifier());
    }

    @Test
    public void calculateModifierNegative() {
        abilityScore.setScore(7);
        abilityScore.setTempBonus(-1);
        assertEquals(-2, abilityScore.getAbilityModifier());
    }

    @Test
    public void calculateTempModifierNegative() {
        abilityScore.setScore(7);
        abilityScore.setTempBonus(-1);
        assertEquals(-2, abilityScore.getTempModifier());
    }
}
