package com.lateensoft.pathfinder.toolkit.db.repository;

import com.lateensoft.pathfinder.toolkit.db.repository.TableAttribute.SQLDataType;

public class TableInfo {
    private TableAttribute[] m_attributes;
    private String m_table;
    
    public TableInfo(String table, TableAttribute[] attributes) {
        m_table = table;
        m_attributes = attributes;
    }
    
    public String[] getColumns() {
        String[] columns = new String[m_attributes.length];
        for(int i = 0; i < m_attributes.length; i++) {
            columns[i] = m_attributes[i].GetColumn();
        }
        return columns;
    }
    
    public String getPrimaryKeyColumn() {
        for(int i = 0; i < m_attributes.length; i++) {
            if (m_attributes[i].m_isPrimaryKey) {
                return m_attributes[i].GetColumn();
            }
        }
        return null;
    }
    
    public SQLDataType getDataType(String name) {
        for(int i = 0; i < m_attributes.length; i++) {
            if (m_attributes[i].GetColumn().equals(name)) {
                return m_attributes[i].GetType();
            }
        }
        return SQLDataType.NULL;
    }
    
    public String getTable() {
        return m_table;
    }
}
