package com.lateensoft.pathfinder.toolkit.db;

import com.google.inject.util.Modules;
import com.lateensoft.pathfinder.toolkit.inject.DatabaseModule;
import org.junit.AfterClass;
import org.junit.Before;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;
import roboguice.RoboGuice;

@Config(manifest=Config.NONE)
public class BaseDatabaseTest {
    private static Database database;

    @Before
    public void setUp() throws Exception {
        if (database == null) {
            database = new DatabaseImpl(Robolectric.application);
        }

        RoboGuice.setBaseApplicationInjector(Robolectric.application, RoboGuice.DEFAULT_STAGE,
                Modules.override(RoboGuice.newDefaultRoboModule(Robolectric.application))
                        .with(new DatabaseModule(database)));
    }

    @AfterClass
    public static void tearDownClass() {
        database = null;
    }

    protected Database getDatabase() {
        return database;
    }
}
