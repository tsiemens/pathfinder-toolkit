package com.lateensoft.pathfinder.toolkit;

import java.util.GregorianCalendar;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.lateensoft.pathfinder.toolkit.model.party.PTParty;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class PTSharedPreferences {
	private final String TAG = PTSharedPreferences.class.getSimpleName();
	
	private final long MILISECONDS_BETWEEN_RATE_PROMPT = 604800000L; //One week
	
	private static final String KEY_APP_SHARED_PREFS_NAME = "ptUserPrefs";
		
	public static String KEY_LONG_LAST_RATE_PROMPT_TIME = "lastRateTime";
	public static String KEY_INT_LAST_RATED_VERSION = "lastRateVersion";
	public static String KEY_INT_LAST_USED_VERSION = "lastUsedVersion";
	public static String KEY_LONG_SELECTED_CHARACTER_ID = "selectedCharacter";
	public static String KEY_INT_SELECTED_PARTY_ID = "selectedParty";
	public static String KEY_STRING_ENCOUNTER_PARTY_JSON = "encounterParty";
	
	private static PTSharedPreferences s_instance;

	private SharedPreferences m_sharedPreferences;
	Editor m_editor;
		
	/**
	 * Manages the shared preferences for the Pathfinder Toolkit application 
	 * @return The singleton instance of the manager. 
	 */
	public static PTSharedPreferences getInstance() {
		if (s_instance == null) {
			s_instance = new PTSharedPreferences();
		}
		return s_instance;
	}
	
	protected PTSharedPreferences(){
		Context appContext = PTBaseApplication.getAppContext();
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
	 * Saves the ID of the currently selected character to SharedPreferences
	 * @param characterID
	 * @return true if the save was successful, false otherwise
	 */
	public boolean setSelectedCharacter(long characterID){
		Log.i(TAG, "Selected character set to "+characterID);
		return this.putLong(KEY_LONG_SELECTED_CHARACTER_ID, characterID);
	}
	
	/**
	 * @return The ID of the currently selected character. Returns -1 if no character is selected
	 */
	public long getSelectedCharacter(){
		return this.getLong(KEY_LONG_SELECTED_CHARACTER_ID, -1);
	}
	
	/**
	 * Saves the ID of the currently selected party to SharedPreferences
	 * @param characterID
	 * @return true if the save was successful, false otherwise
	 */
	public boolean setSelectedParty(int partyID){
		Log.i(TAG, "Selected party set to "+partyID);
		return this.putInt(KEY_INT_SELECTED_PARTY_ID, partyID);
	}
	
	/**
	 * @return The ID of the currently selected party. Returns -1 if no character is selected
	 */
	public int getSelectedParty(){
		return m_sharedPreferences.getInt(KEY_INT_SELECTED_PARTY_ID, -1);
	}
	
	/**
	 * Saves the current party in the encounter to shared prefs
	 * @param encounterParty
	 * @return true if the save was successful, false otherwise
	 * 
	 */
	public boolean setEncounterParty(PTParty encounterParty){
		Gson gson = new Gson();
		String partyJson = gson.toJson(encounterParty);
		Log.i(TAG, "Saved current encounter party");
		return this.putString(KEY_STRING_ENCOUNTER_PARTY_JSON, partyJson);
	}
	
	/**
	 * Should be getting from database in future
	 * @return the current encounter party. returns null if none is saved.
	 */
	public PTParty getEncounterParty(){
		Gson gson = new Gson();
		String partyJson = m_sharedPreferences.getString(KEY_STRING_ENCOUNTER_PARTY_JSON, "");
		PTParty encounterParty;
		try{
			encounterParty = gson.fromJson(partyJson, PTParty.class);
		}catch(JsonSyntaxException e){
			encounterParty = null;
		}
		return encounterParty;
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
		}else
			return (currentUnixTime - lastRateTime) > MILISECONDS_BETWEEN_RATE_PROMPT;	
	}
	
	/**
	 * Sets the last rated version (KEY_INT_LAST_RATED_VERSION) to the current version
	 * @return true if the save was successful, false otherwise
	 */
	public boolean updateLastRatedVersion(){
		Context context = PTBaseApplication.getAppContext();
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
		Context context = PTBaseApplication.getAppContext();
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
	 * Sets the last used version (KEY_INT_LAST_RATED_VERSION) to the current version
	 * @return true if the save was successful, false otherwise
	 */
	public boolean updateLastUsedVersion(){
		Context context = PTBaseApplication.getAppContext();
		PackageInfo pInfo;
		try{
			pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return this.putInt(KEY_INT_LAST_USED_VERSION, pInfo.versionCode);
		}catch (NameNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * @return true if a new version of the app is running, since KEY_INT_LAST_USED_VERSION
	 * was last updated
	 */
	public boolean isNewVersion(){
		int appCode = this.getInt(KEY_INT_LAST_USED_VERSION, 0);
		return appCode != getAppVersionCode();
	}
	
	/**
	 * @return: The the incremental app version code, or -1 in the event of an error.
	 */
	public int getAppVersionCode() {
		Context context = PTBaseApplication.getAppContext();
		PackageInfo pInfo;
		try{
			pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return pInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	/**
	 * @return: The the human readable app version, or null in the event of an error.
	 */
	public String getAppVersionName() {
		Context context = PTBaseApplication.getAppContext();
		PackageInfo pInfo;
		try{
			pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return pInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
