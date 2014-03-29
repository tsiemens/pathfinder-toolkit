package com.lateensoft.pathfinder.toolkit;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class BaseApplication extends Application{
	private static final String TAG = BaseApplication.class.getSimpleName();
		
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
	
	/**
	 * @return: The the human readable app version, or null in the event of an error.
	 */
	public static String getAppVersionName() {
		Context context = BaseApplication.getAppContext();
		PackageInfo pInfo;
		try{
			pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return pInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * @return: The the incremental app version code, or -1 in the event of an error.
	 */
	public static int getAppVersionCode() {
		Context context = BaseApplication.getAppContext();
		PackageInfo pInfo;
		try{
			pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return pInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return -1;
		}
	}
}
