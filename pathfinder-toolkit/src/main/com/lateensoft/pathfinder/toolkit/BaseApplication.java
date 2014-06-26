package com.lateensoft.pathfinder.toolkit;

import android.app.Application;
import android.util.Log;
import com.google.inject.util.Modules;
import com.lateensoft.pathfinder.toolkit.db.Database;
import com.lateensoft.pathfinder.toolkit.db.DatabaseImpl;
import com.lateensoft.pathfinder.toolkit.inject.ApplicationModule;
import com.lateensoft.pathfinder.toolkit.inject.DatabaseModule;
import com.lateensoft.pathfinder.toolkit.pref.AppPreferences;
import com.lateensoft.pathfinder.toolkit.pref.Preferences;
import roboguice.RoboGuice;

public class BaseApplication extends Application{
	private static final String TAG = BaseApplication.class.getSimpleName();
		
    private Database database;
    private Preferences preferences;
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "Starting application");

        preferences = new AppPreferences(this);
        database = new DatabaseImpl(this);

        initializeApplicationInjector();
	}

    private void initializeApplicationInjector() {
        RoboGuice.setBaseApplicationInjector(this, RoboGuice.DEFAULT_STAGE,
                Modules.override(RoboGuice.newDefaultRoboModule(this))
                        .with(  new ApplicationModule(this, preferences),
                                new DatabaseModule(database)));
    }

    @Override
	public void onTerminate() {
		Log.d(TAG, "Terminating application");
		super.onTerminate();
	}
}
