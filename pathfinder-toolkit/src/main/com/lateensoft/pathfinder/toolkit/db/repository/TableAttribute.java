package com.lateensoft.pathfinder.toolkit.db.repository;

public class TableAttribute {
	private final String m_column;
	private final SQLDataType m_type;
	public final Boolean m_isPrimaryKey;
	
	public enum SQLDataType {
		NULL,
		INTEGER,
		REAL,
		TEXT,
		BLOB
	};
	
	public TableAttribute(String column, SQLDataType dataType, Boolean isPrimaryKey) {
		m_column = column;
		m_type = dataType;
		this.m_isPrimaryKey = isPrimaryKey;
	}
	
	public TableAttribute(String column, SQLDataType dataType) {
		m_column = column;
		m_type = dataType;
		m_isPrimaryKey = false;
	}
	
	public String GetColumn() {
		return m_column;
	}
	
	public SQLDataType GetType() {
		return m_type;
	}
}
