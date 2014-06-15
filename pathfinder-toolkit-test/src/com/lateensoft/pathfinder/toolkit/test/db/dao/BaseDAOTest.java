package com.lateensoft.pathfinder.toolkit.test.db.dao;

import android.test.AndroidTestCase;
import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.db.Database;

public class BaseDAOTest extends AndroidTestCase {
	private Database database;
	protected final int INSERT_FAIL = -1;
	
	@Override
	protected void setUp() throws Exception {
        super.setUp();
		database = Database.getInstance();
	}

    protected Database getDatabase() {
        return database;
    }

    protected void handleDataAccessException(DataAccessException e) {
        fail(e.getMessage());
    }
}
