package com.lateensoft.pathfinder.toolkit.repository;

import com.lateensoft.pathfinder.toolkit.repository.PTTableAttribute.SQLDataType;

public class PTTableDatum {
	private final Object mDatum;
	private final String mColumn;
	private final SQLDataType mType;
	
	public PTTableDatum(Object datum, String column, SQLDataType type) {
		mDatum = datum;
		mColumn = column;
		mType = type;
	}
	
	public Object getDatum() {
		return mDatum;
	}
	
	public String getColumn() {
		return mColumn;
	}
	
	public SQLDataType getType() {
		return mType;
	}
}
