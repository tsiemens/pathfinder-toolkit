package com.lateensoft.pathfinder.toolkit;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class PTBaseApplication extends Application{
	private static final String TAG = PTBaseApplication.class.getSimpleName();
		
	// Used for statically obtaining app context,
	// treating the application as a singleton, which it is.
	private static Context s_context;
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "Starting application");
		s_context = getApplicationContext();
		
		// Application setup
		
		// Perform database migrations if necessary here
	}

	@Override
	public void onTerminate() {
		Log.d(TAG, "Terminating application");
		super.onTerminate();
	}

	/**
	 * Gets the application context.
	 */
	static public Context getAppContext() {
		return s_context;
	}
}
