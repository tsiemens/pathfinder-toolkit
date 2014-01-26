package com.lateensoft.pathfinder.toolkit.db;

/**
 * Used for returning lists of readable items, but no content.
 */
public class IDNamePair {
	private final long m_id;
	private final String m_name;
	
	public IDNamePair(long id, String name) {
		m_id = id;
		m_name = name;
	}
	
	public long getID() {
		return m_id;
	}
	
	public String getName() {
		return m_name;
	}
	
	/**
	 * @param pairs
	 * @return an array of names, in the same order.
	 */
	public static String[] toNameArray(IDNamePair[] pairs) {
		String[] names = new String[pairs.length];
		for(int i = 0; i < pairs.length; i++) {
			names[i] = pairs[i].getName();
		}
		return names;
	}
}
