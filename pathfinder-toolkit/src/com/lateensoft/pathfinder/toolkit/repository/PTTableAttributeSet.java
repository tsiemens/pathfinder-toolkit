package com.lateensoft.pathfinder.toolkit.repository;

import com.lateensoft.pathfinder.toolkit.repository.PTTableAttribute.SQLDataType;

public class PTTableAttributeSet {
	private PTTableAttribute[] mAttributes;
	// TODO hashmap? Probably unnecessary
	
	public PTTableAttributeSet(PTTableAttribute[] attributes) {
		mAttributes = attributes;
	}
	
	public String[] getColumns() {
		String[] columns = new String[mAttributes.length];
		for(int i = 0; i < mAttributes.length; i++) {
			columns[i] = mAttributes[i].GetColumn();
		}
		return columns;
	}
	
	public SQLDataType getDataType(String name) {
		for(int i = 0; i < mAttributes.length; i++) {
			if (mAttributes[i].GetColumn().equals(name)) {
				return mAttributes[i].GetType();
			}
		}
		return SQLDataType.NULL;
	}
}
