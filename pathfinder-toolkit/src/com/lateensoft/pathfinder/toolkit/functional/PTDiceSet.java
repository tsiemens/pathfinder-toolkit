package com.lateensoft.pathfinder.toolkit.functional;

import java.util.Random;

public class PTDiceSet {

	private Random mNumberGenerator;
	final private int mDiceInSet[] = { 4, 6, 8, 10, 12, 20, 100 };
	final private int TOTAL_DICE_IN_SET = 7;

	public PTDiceSet() {
		mNumberGenerator = new Random();
	}

	/**
	 * @param dieType
	 * @return an int between 1 and dieType (inclusive), if the dieType is a
	 *         valid polyhedral die (4, 6, 8, 10, 12, 20, 100). Returns 0 if
	 *         dieType is invalid.
	 */
	public int singleRoll(int dieType) {
		for (int i = 0; i < TOTAL_DICE_IN_SET; i++) {
			if (dieType == mDiceInSet[i])
				return mNumberGenerator.nextInt(dieType) + 1;
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
					rollTotal += (mNumberGenerator.nextInt(dieType) + 1);
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
	public int[] multiRollWithResults(int rolls, int dieType){
		int[] rollResults = new int[rolls];
		
		for (int i = 0; i < TOTAL_DICE_IN_SET; i++) {
			if (dieType == mDiceInSet[i]) {
				for (int j = 0; j < rolls; j++)
					rollResults[j] = (mNumberGenerator.nextInt(dieType) + 1);
				return rollResults;
			}
		}

		return null;
	}
}