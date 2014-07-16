package com.lateensoft.pathfinder.toolkit.util;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Random;

public class DiceSet {

    public enum Die {
        D4(4), D6(6), D8(8), D10(10), D12(12), D20(20), D100(100);

        private int sides;

        Die(int sides) {
            this.sides = sides;
        }

        public int getSides() {
            return sides;
        }
    }

    private Random randGenerator = new Random();

    @Deprecated
    final private int mDiceInSet[] = { 4, 6, 8, 10, 12, 20, 100 };
    @Deprecated
    final private int TOTAL_DICE_IN_SET = 7;

    public int roll(Die die) {
        return randGenerator.nextInt(die.getSides()) + 1;
    }

    public List<Integer> rollMany(Die die, int numberOfRolls) {
        List<Integer> rolls = Lists.newArrayListWithCapacity(numberOfRolls);
        for (int i = 0; i < numberOfRolls; i++) {
            rolls.add(roll(die));
        }
        return rolls;
    }

    /**
     * @param dieType
     * @return an int between 1 and dieType (inclusive), if the dieType is a
     *         valid polyhedral die (4, 6, 8, 10, 12, 20, 100). Returns 0 if
     *         dieType is invalid.
     */
    @Deprecated
    public int singleRoll(int dieType) {
        for (int i = 0; i < TOTAL_DICE_IN_SET; i++) {
            if (dieType == mDiceInSet[i])
                return randGenerator.nextInt(dieType) + 1;
        }

        return 0;
    }

    /**
     * Performs an XdY type roll
     * 
     * @param rolls
     * @param dieType
     * @return an int between 1 and rolls*dieType (inclusive), if the dieType is
     *         a valid polyhedral die (4, 6, 8, 10, 12, 20, 100). Returns 0 if
     *         dieType is invalid.
     */
    public int multiRoll(int rolls, int dieType) {
        int rollTotal = 0;

        for (int i = 0; i < TOTAL_DICE_IN_SET; i++) {
            if (dieType == mDiceInSet[i]) {
                for (int j = 0; j < rolls; j++)
                    rollTotal += (randGenerator.nextInt(dieType) + 1);
                return rollTotal;
            }
        }

        return 0;
    }
    
    
    /**
     * Performs an XdY type roll
     * 
     * @param rolls
     * @param dieType
     * @return an int[] of size X containing all the roll results, if the dieType is
     *         a valid polyhedral die (4, 6, 8, 10, 12, 20, 100). Returns null if
     *         dieType is invalid.
     */
    @Deprecated
    public int[] multiRollWithResults(int rolls, int dieType){
        int[] rollResults = new int[rolls];
        
        for (int i = 0; i < TOTAL_DICE_IN_SET; i++) {
            if (dieType == mDiceInSet[i]) {
                for (int j = 0; j < rolls; j++)
                    rollResults[j] = (randGenerator.nextInt(dieType) + 1);
                return rollResults;
            }
        }

        return null;
    }
}