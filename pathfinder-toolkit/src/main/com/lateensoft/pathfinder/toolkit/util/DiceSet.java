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

    public int roll(Die die) {
        return randGenerator.nextInt(die.getSides()) + 1;
    }

    public List<Integer> rollMultiple(Die die, int numberOfRolls) {
        List<Integer> rolls = Lists.newArrayListWithCapacity(numberOfRolls);
        for (int i = 0; i < numberOfRolls; i++) {
            rolls.add(roll(die));
        }
        return rolls;
    }
}