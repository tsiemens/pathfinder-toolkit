package com.lateensoft.pathfinder.toolkit.db.dao;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.List;

public class Table {
    private ImmutableList<String> columns;
    private String name;
    
    public Table(String table, String... columns) {
        name = table;
        this.columns = ImmutableList.copyOf(columns);
    }

    public String[] getColumnNames() {
        String[] colNames = new String[columns.size()];
        return columns.toArray(colNames);
    }

    public String getName() {
        return name;
    }

    /**
     * Union of columns will merge ourColumnToJoin and othercolumnToJoin.
     * It will be renamed to "[this.getName].ourColumnToJoin ourColumnToJoin"
     */
    public String[] union(Table otherTable,
                          String ourColumnToJoin, String otherColumnToJoin) {
        List<String> otherColsList = Lists.newArrayList(otherTable.columns);
        otherColsList.remove(otherColumnToJoin);

        List<String> combinedCols = Lists.newArrayList(this.columns);
        int joinColIndex = combinedCols.indexOf(ourColumnToJoin);

        combinedCols.set(joinColIndex, getUnambiguousRenamedColumn(this.getName(), ourColumnToJoin));
        combinedCols.addAll(otherColsList);

        String[] colsArray = new String[combinedCols.size()];
        combinedCols.toArray(colsArray);
        return colsArray;
    }

    private static String getUnambiguousRenamedColumn(String table, String column) {
        return table + "." + column + " " + column;
    }
}
