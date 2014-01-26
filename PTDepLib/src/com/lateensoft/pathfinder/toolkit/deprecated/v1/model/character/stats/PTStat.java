package com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.stats;

public class PTStat {
	String mName;
	int mBaseValue;

	public PTStat() {
		mName = "";
		mBaseValue = 0;
	}
	
	public PTStat(String name) {
		mName = name;
		mBaseValue = 0;
	}
	
	public PTStat(String name, int baseValue) {
		mName = name;
		mBaseValue = baseValue;
	}
	
	public PTStat(PTStat other) {
		mName = other.getName();
		mBaseValue = other.getBaseValue();
	}
	
	public String getName() {
		return mName;
	}
	
	public int getBaseValue() {
		return mBaseValue;
	}
	
	public void setName(String name) {
		mName = name;
	}
	
	public void setBaseValue(int baseValue) {
		mBaseValue = baseValue;
	}
}
