package com.lateensoft.pathfinder.toolkit;

import com.lateensoft.pathfinder.toolkit.datahelpers.PTSharedPreferences;

import android.app.Application;
import android.util.Log;

public class PTBaseApplication extends Application{
	private static final String TAG = PTBaseApplication.class.getSimpleName();
		
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "Starting application");
		// Application setup
		PTSharedPreferences.setup(this);
		
		// Perform database migrations if necessary here
	}

	@Override
	public void onTerminate() {
		Log.d(TAG, "Terminating application");
		super.onTerminate();
	}

}
