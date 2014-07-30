package com.lateensoft.pathfinder.toolkit.db;

import android.database.Cursor;

import java.util.Hashtable;

public class CursorUtil {
    public static Hashtable<String, Object> getTableOfValues(Cursor cursor) {
        Hashtable<String, Object> table = new Hashtable<String, Object>();
        String[] columns = cursor.getColumnNames();
        for (String column : columns) {
            Object datum = getDatum(cursor, column);
            if (datum != null) {
                table.put(column, datum);
            }
        }
        return table;
    }

    public static Object getDatum(Cursor cursor, String column) {
        int index = cursor.getColumnIndex(column);
        return getDatum(cursor, index);
    }

    public static Object getDatum(Cursor cursor, int index) {
        int type = cursor.getType(index);
        Object data;
        switch (type) {
            case Cursor.FIELD_TYPE_STRING:
                data = cursor.getString(index);
                break;
            case Cursor.FIELD_TYPE_INTEGER:
                // Because INTEGER can store Long and Integer, we need to use Long, and cast later
                data = cursor.getLong(index);
                break;
            case Cursor.FIELD_TYPE_FLOAT:
                data = cursor.getDouble(index);
                break;
            default:
                data = null;
                break;
        }
        return data;
    }
}
