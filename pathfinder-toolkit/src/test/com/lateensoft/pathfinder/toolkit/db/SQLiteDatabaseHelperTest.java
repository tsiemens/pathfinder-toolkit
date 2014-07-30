package com.lateensoft.pathfinder.toolkit.db;

import android.content.ContentValues;
import android.database.Cursor;
import com.google.common.collect.Lists;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

@Config(manifest=Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class SQLiteDatabaseHelperTest extends BaseDatabaseTest {

    SQLiteDatabaseHelper helper;

    private String testTable;
    private String createTestTable;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getDatabase().execSQL("CREATE TABLE TestTable ( Col1 INTEGER PRIMARY KEY AUTOINCREMENT, Col2 TEXT, Col3 TEXT);");
        helper = new SQLiteDatabaseHelper(getDatabase());
        testTable = "TestTable";
        createTestTable = "CREATE TABLE TestTable ( Col1 INTEGER PRIMARY KEY AUTOINCREMENT, Col3 TEXT);";

        getDatabase().insert(testTable, createTestContentValues());
    }

    @After
    public void tearDown() {
        getDatabase().execSQL("DROP TABLE TestTable;");
    }

    @Test
    public void testGetTableColumns() {
        List<String> cols = helper.getTableColumns(testTable);

        assertThat(cols, containsIgnoringCase("Col1", "Col2", "Col3"));
    }

    @Test
    public void testDropColumn() throws SQLException {
        helper.dropColumn(createTestTable, testTable, "Col2");

        List<String> cols = helper.getTableColumns("TestTable");
        assertThat(cols, containsIgnoringCase("Col1", "Col3"));

        Cursor c = getDatabase().query("TestTable", null, null);
        assertContainsData(c, Lists.newArrayList(Long.valueOf(1), "thing1"));
    }

    private ContentValues createTestContentValues() {
        ContentValues cv = new ContentValues();
        cv.put("Col1", 1);
        cv.put("Col3", "thing1");
        return cv;
    }

    private static Matcher<Iterable<String>> containsIgnoringCase(final String... expected) {
        return new BaseMatcher<Iterable<String>>() {
            @Override
            public boolean matches(Object o) {
                if (!(o instanceof Iterable)) return false;
                return Lists.newArrayList((Iterable) o).equals(Lists.newArrayList(expected));
            }

            @Override
            public void describeTo(Description description) {
                description.appendValueList("<[", ",", "]>", expected);
            }
        };
    }

    private static void assertContainsData(Cursor cursor, List... rows) {
        cursor.moveToFirst();
        int row = 0;
        while (!cursor.isAfterLast()) {
            for (int col = 0; col < cursor.getColumnCount(); col++) {
                Assert.assertEquals(CursorUtil.getDatum(cursor, col), rows[row].get(col));
            }
            cursor.moveToNext();
            row++;
        }
    }
}
