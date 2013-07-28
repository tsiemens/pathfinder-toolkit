package com.lateensoft.pathfinder.toolkit.stats;

import com.lateensoft.pathfinder.toolkit.repository.IStorable;

public class PTStat implements IStorable {
	private String mName;
	private int mBaseValue;
	private int mId;

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
	
	public PTStat(int id, String name, int baseValue) {
		mName = name;
		mBaseValue = baseValue;
		mId = id;
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
	
	public int id() {
		return mId;
	}
}
