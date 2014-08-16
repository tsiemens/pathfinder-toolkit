package com.lateensoft.pathfinder.toolkit.db;

import android.database.Cursor;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;

public class SQLiteDatabaseHelper {

    private Database database;

    public SQLiteDatabaseHelper(Database database) {
        this.database = database;
    }

    /**
     * NOTE: If another table has any foreign keys to this table with ON DELETE conditions,
     * data could be lost by calling dropColumns
     */
    public void dropColumn(String createTableCmd,
                            String tableName,
                            String... colsToRemove) throws java.sql.SQLException {
        database.beginTransaction();
        try {

            List<String> updatedTableColumns = getTableColumns(tableName);
            updatedTableColumns.removeAll(Arrays.asList(colsToRemove));

            String columnsSeparated = Joiner.on(",").join(updatedTableColumns);

            database.execSQL("ALTER TABLE " + tableName + " RENAME TO " + tableName + "_old;");

            // Creating the table on its new format (no redundant columns)
            database.execSQL(createTableCmd);

            // Populating the table with the data
            database.execSQL("INSERT INTO " + tableName + "(" + columnsSeparated + ") SELECT "
                    + columnsSeparated + " FROM " + tableName + "_old;");
            database.execSQL("DROP TABLE " + tableName + "_old;");
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public List<String> getTableColumns(String tableName) {
        List<String> columns = Lists.newArrayList();
        String cmd = "pragma table_info(" + tableName + ");";
        Cursor cur = database.rawQuery(cmd, null);

        cur.moveToFirst();
        while (!cur.isAfterLast()) {
            columns.add(cur.getString(cur.getColumnIndex("name")));
            cur.moveToNext();
        }
        cur.close();

        return columns;
    }
}
