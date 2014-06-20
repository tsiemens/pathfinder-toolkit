package com.lateensoft.pathfinder.toolkit.db.dao;

import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.db.BaseDatabaseTest;
import org.junit.Assert;

public class BaseDAOTest extends BaseDatabaseTest {

    protected void handleDataAccessException(DataAccessException e) {
        e.printStackTrace();
        Assert.fail(e.getMessage());
    }
}
