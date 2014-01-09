package com.lateensoft.pathfinder.toolkit.model.character.stats;


import com.lateensoft.pathfinder.toolkit.db.repository.PTStorable;

public abstract class PTStat implements PTStorable {

	private String m_name;
	private int m_baseValue;
	private long m_id;

	public PTStat() {
		m_name = "";
		m_baseValue = 0;
		m_id = 0;
	}
	
	public PTStat(String name) {
		m_name = name;
		m_baseValue = 0;
		m_id = 0;
	}
	
	public PTStat(String name, int baseValue) {
		m_name = name;
		m_baseValue = baseValue;
		m_id = 0;
	}
	
	public PTStat(long id, String name, int baseValue) {
		m_name = name;
		m_baseValue = baseValue;
		m_id = id;
	}
	
	public PTStat(PTStat other) {
		m_name = other.getName();
		m_baseValue = other.getBaseValue();
	}
	
	public String getName() {
		return m_name;
	}
	
	public int getBaseValue() {
		return m_baseValue;
	}
	
	public void setName(String name) {
		m_name = name;
	}
	
	public void setBaseValue(int baseValue) {
		m_baseValue = baseValue;
	}
	
	@Override
	public void setID(long id) {
		m_id = id;
	}
	
	@Override
	public long getID() {
		return m_id;
	}
}
