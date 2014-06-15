package com.lateensoft.pathfinder.toolkit.test.tmp;

import android.test.AndroidTestCase;
import com.lateensoft.pathfinder.toolkit.db.dao.GenericTableDAO;

public class TestTests extends AndroidTestCase {

    public void testJoinerAllNull() {
        assertEquals(null, GenericTableDAO.andSelectors(null, null, null));
    }

    public void testJoinerSingle() {
        assertEquals("boop", GenericTableDAO.andSelectors(null, "boop", null));
    }

    public void testJoinerMultiple() {
        assertEquals("foo AND bar", GenericTableDAO.andSelectors("foo", null, "bar"));
    }
}
