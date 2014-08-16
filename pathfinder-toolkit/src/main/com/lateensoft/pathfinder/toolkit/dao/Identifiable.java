package com.lateensoft.pathfinder.toolkit.dao;

public interface Identifiable {
    public static final long UNSET_ID = -1;
    
    public void setId(long id);
    public long getId();
}
