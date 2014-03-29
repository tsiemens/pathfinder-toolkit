package com.lateensoft.pathfinder.toolkit.db.repository;

public interface Storable {
	public static final long UNSET_ID = 0;
	
	public void setID(long id);
	public long getID();
}
