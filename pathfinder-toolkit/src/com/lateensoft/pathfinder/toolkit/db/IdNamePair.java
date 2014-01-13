package com.lateensoft.pathfinder.toolkit.db;

/**
 * Used for returning lists of readable items, but no content.
 */
public class IdNamePair {
	private long m_id;
	private String m_name;
	
	public IdNamePair(long id, String name) {
		m_id = id;
		m_name = name;
	}
	
	public long getID() {
		return m_id;
	}
	
	public void setID(long id) {
		m_id = id;
	}
	
	public String getName() {
		return m_name;
	}
	
	public void setName(String name) {
		m_name = name;
	}
}
