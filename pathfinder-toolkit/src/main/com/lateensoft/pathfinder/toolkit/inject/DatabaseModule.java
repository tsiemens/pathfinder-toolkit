package com.lateensoft.pathfinder.toolkit.inject;

import com.google.inject.AbstractModule;
import com.lateensoft.pathfinder.toolkit.db.Database;

public class DatabaseModule extends AbstractModule {
    private Database database;

    public DatabaseModule(Database database) {
        this.database = database;
    }

    @Override
    protected void configure() {
        bind(Database.class).toInstance(database);
    }
}
