package com.lateensoft.pathfinder.toolkit.stats;

import com.lateensoft.pathfinder.toolkit.R;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;


public class PTAbilityModSet extends PTAbilitySet{
	private static final String TAG = "PTAbilitySet";
	
	public PTAbilityModSet() {
		super();
	}
	
	public void setMod(String ability, int score) {
		super.setScore(ability, score);
	}
	
	public int getMod(String ability) {
		return super.getScore(ability);
	}
	
	public int[] getMods() {
		return super.getScores();
	}
	
	public void setMods(int[] modifiers) {
		if(modifiers.length != super.getLength())
			return;
		
		super.setScores(modifiers);
	}
	
	// Returns boolean of isHuman
	public boolean setRacialMods(int index, Context context) {
		
		Resources r = context.getResources();
		
		switch (index) {
		case 0:
			super.setScores(r.getIntArray(R.array.HumanMods));
			return true;

		case 1:
			super.setScores(r.getIntArray(R.array.DwarfMods));
			return false;

		case 2:
			super.setScores(r.getIntArray(R.array.ElfMods));
			return false;

		case 3:
			super.setScores(r.getIntArray(R.array.HalflingMods));
			return  false;

		case 4:
			super.setScores(r.getIntArray(R.array.GnomeMods));
			return false;

		case 5:
			super.setScores(r.getIntArray(R.array.HalfElfMods));
			return true;

		default:
			super.setScores(r.getIntArray(R.array.HumanMods));
			return true;

		}
	}
	
	public void setHumanRacialMods(int index, Context context) {
		Resources r = context.getResources();
		super.setScores(r.getIntArray(R.array.HumanMods));
		setScore(index, 2);
	}
}
