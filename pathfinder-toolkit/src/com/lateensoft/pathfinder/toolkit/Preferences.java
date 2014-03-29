package com.lateensoft.pathfinder.toolkit;

import java.util.GregorianCalendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class Preferences {
	private final String TAG = Preferences.class.getSimpleName();
	
	private final long MILISECONDS_BETWEEN_RATE_PROMPT = 604800000L; //One week
	
	private static final String KEY_APP_SHARED_PREFS_NAME = "ptUserPrefs";
		
	public static String KEY_LONG_LAST_RATE_PROMPT_TIME = "lastRateTime";
	public static String KEY_INT_LAST_RATED_VERSION = "lastRateVersion";
	public static String KEY_INT_LAST_USED_VERSION = "lastUsedVersion";
	public static String KEY_LONG_SELECTED_CHARACTER_ID = "selectedCharacter";
	public static String KEY_LONG_SELECTED_PARTY_ID = "selectedParty";
	
	private static Preferences s_instance;

	private SharedPreferences m_sharedPreferences;
	Editor m_editor;
		
	/**
	 * Manages the shared preferences for the Pathfinder Toolkit application 
	 * @return The singleton instance of the manager. 
	 */
	public static Preferences getInstance() {
		if (s_instance == null) {
			s_instance = new Preferences();
		}
		return s_instance;
	}
	
	protected Preferences(){
		Context appContext = BaseApplication.getAppContext();
		m_sharedPreferences = appContext.getSharedPreferences(
				KEY_APP_SHARED_PREFS_NAME, Context.MODE_PRIVATE);
		m_editor = m_sharedPreferences.edit();
	}
	
	/**
	 * Put an int into the shared preferences
	 */
	public boolean putInt(String key, int value) {
		m_editor.putInt(key, value);
		return m_editor.commit();
	}
	
	/**
	 * Put a long into the shared preferences
	 */
	public boolean putLong(String key, long value) {
		m_editor.putLong(key, value);
		return m_editor.commit();
	}
	
	/**
	 * Put a boolean into the shared preferences
	 */
	public boolean putBoolean(String key, boolean value) {
		m_editor.putBoolean(key, value);
		return m_editor.commit();
	}
	
	/**
	 * Put a string into the shared preferences
	 */
	public boolean putString(String key, String value) {
		m_editor.putString(key, value);
		return m_editor.commit();
	}
	
	/**
	 * Get an int from the shared preferences	
	 */
	public int getInt(String key, int defValue) {
		return m_sharedPreferences.getInt(key, defValue);
	}
	
	/**
	 * Get a long from the shared preferences	
	 */
	public long getLong(String key, long defValue) {
		return m_sharedPreferences.getLong(key, defValue);
	}
	
	/**
	 * Get a boolean from the shared preferences	
	 */
	public boolean getBoolean(String key, boolean defValue) {
		return m_sharedPreferences.getBoolean(key, defValue);
	}
	
	/**
	 * Get a string from the shared preferences	
	 */
	public String getString(String key, String defValue) {
		return m_sharedPreferences.getString(key, defValue);
	}
	
	/**
	 * @return true if the app was prompted for rating more than one week ago. ie since KEY_LONG_LAST_RATE_TIME was set.
	 */
	public boolean isLastRateTimeLongEnough(){
		long lastRateTime = this.getLong(KEY_LONG_LAST_RATE_PROMPT_TIME, 0L);
		long currentUnixTime = new GregorianCalendar().getTimeInMillis();
		if( lastRateTime == 0L ){
			this.putLong(KEY_LONG_LAST_RATE_PROMPT_TIME, currentUnixTime);
			return false;
		} else {
			Log.v(TAG, ""+(currentUnixTime = lastRateTime)+" ms since last rate prompt");
			return (currentUnixTime - lastRateTime) > MILISECONDS_BETWEEN_RATE_PROMPT;
		}
	}
	
	/**
	 * Sets the last rated version (KEY_INT_LAST_RATED_VERSION) to the current version
	 * @return true if the save was successful, false otherwise
	 */
	public boolean updateLastRatedVersion(){
		Context context = BaseApplication.getAppContext();
		PackageInfo pInfo;
		try{
			pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return this.putInt(KEY_INT_LAST_RATED_VERSION, pInfo.versionCode);
		}catch (NameNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * @return true if the current version of the app has been rated. ie if KEY_INT_LAST_RATED_VERSION
	 * is the current version
	 * @return true if the last rated version is not the current one (version code) 
	 */
	public boolean hasRatedCurrentVersion(){
		Context context = BaseApplication.getAppContext();
		int appCode = this.getInt(KEY_INT_LAST_RATED_VERSION, 0);
		PackageInfo pInfo;
		try{
			pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return appCode == pInfo.versionCode;
		}catch (NameNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Removes a preference for key
	 */
	public void remove(String key) {
		m_editor.remove(key);
	}
	
}
