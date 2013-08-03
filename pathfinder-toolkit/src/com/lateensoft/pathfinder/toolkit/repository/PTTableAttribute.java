package com.lateensoft.pathfinder.toolkit.repository;

public class PTTableAttribute {
	private final String mColumn;
	private final SQLDataType mType;
	public final Boolean isPrimaryKey;
	
	public enum SQLDataType {
		NULL,
		INTEGER,
		REAL,
		TEXT,
		BLOB
	};
	
	public PTTableAttribute(String column, SQLDataType dataType, Boolean isPrimaryKey) {
		mColumn = column;
		mType = dataType;
		this.isPrimaryKey = isPrimaryKey;
	}
	
	public PTTableAttribute(String column, SQLDataType dataType) {
		mColumn = column;
		mType = dataType;
		isPrimaryKey = false;
	}
	
	public String GetColumn() {
		return mColumn;
	}
	
	public SQLDataType GetType() {
		return mType;
	}
}
