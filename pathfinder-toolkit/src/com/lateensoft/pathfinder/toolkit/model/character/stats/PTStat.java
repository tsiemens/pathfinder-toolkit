package com.lateensoft.pathfinder.toolkit.model.character.stats;


import com.lateensoft.pathfinder.toolkit.db.repository.PTStorable;

public abstract class PTStat implements PTStorable {

	private String mName;
	private int mBaseValue;
	private long mId;

	public PTStat() {
		mName = "";
		mBaseValue = 0;
		mId = 0;
	}
	
	public PTStat(String name) {
		mName = name;
		mBaseValue = 0;
		mId = 0;
	}
	
	public PTStat(String name, int baseValue) {
		mName = name;
		mBaseValue = baseValue;
		mId = 0;
	}
	
	public PTStat(long id, String name, int baseValue) {
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
	
	public Long getID() {
		return mId;
	}
}
