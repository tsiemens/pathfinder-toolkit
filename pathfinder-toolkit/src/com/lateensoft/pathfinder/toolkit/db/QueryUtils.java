package com.lateensoft.pathfinder.toolkit.db;

import java.util.List;

/**
 * @author tsiemens
 */
public class QueryUtils {
    public static String selectorForAll(String col, List<Long> colVals) {
        StringBuilder stringBuilder = new StringBuilder(col + " IN (");
        for (int i = 0; i < colVals.size(); i++) {
            Long val = colVals.get(i);
            stringBuilder.append(Long.toString(val));
            if (i < colVals.size() - 1) {
                stringBuilder.append(",");
            }
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }
}
