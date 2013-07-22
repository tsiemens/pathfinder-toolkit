package com.lateensoft.pathfinder.toolkit.datahelpers;

import java.util.Calendar;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.lateensoft.pathfinder.toolkit.party.PTParty;

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
	public static String KEY_INT_SELECTED_CHARACTER_ID = "selectedCharacter";
	public static String KEY_INT_LAST_TAB = "lastTab";
	public static String KEY_INT_SELECTED_PARTY_ID = "selectedParty";
	public static String KEY_STRING_ENCOUNTER_PARTY_JSON = "encounterParty";
	
	private static PTSharedPreferences s_PTSharedPreferences;

	private SharedPreferences mSharedPreferences;
	Editor mEditor;
		
	/**
	 * Manages the shared preferences for the Pathfinder Toolkit application 
	 * @param context
	 */
	public static PTSharedPreferences getSharedInstance() {
		return s_PTSharedPreferences;
	}
	
	/**
	 * Initializes the static instance. Must be called prior to calling getInstance()
	 */
	public static void setup(Context context) {
		s_PTSharedPreferences = new PTSharedPreferences(context);
	}
	
	protected PTSharedPreferences(Context context){
		Context appContext = context.getApplicationContext();
		mSharedPreferences = context.getApplicationContext().getSharedPreferences(
				KEY_APP_SHARED_PREFS_NAME, appContext.MODE_PRIVATE);
		mEditor = mSharedPreferences.edit();
	}
	
	/**
	 * Put an int into the shared preferences
	 */
	public boolean putInt(String key, int value) {
		mEditor.putInt(key, value);
		return mEditor.commit();
	}
	
	/**
	 * Put a long into the shared preferences
	 */
	public boolean putLong(String key, long value) {
		mEditor.putLong(key, value);
		return mEditor.commit();
	}
	
	/**
	 * Put a boolean into the shared preferences
	 */
	public boolean putBoolean(String key, boolean value) {
		mEditor.putBoolean(key, value);
		return mEditor.commit();
	}
	
	/**
	 * Put a string into the shared preferences
	 */
	public boolean putString(String key, String value) {
		mEditor.putString(key, value);
		return mEditor.commit();
	}
	
	/**
	 * Get an int from the shared preferences	
	 */
	public int getInt(String key, int defValue) {
		return mSharedPreferences.getInt(key, defValue);
	}
	
	/**
	 * Get a long from the shared preferences	
	 */
	public long getLong(String key, long defValue) {
		return mSharedPreferences.getLong(key, defValue);
	}
	
	/**
	 * Get a boolean from the shared preferences	
	 */
	public boolean getBoolean(String key, boolean defValue) {
		return mSharedPreferences.getBoolean(key, defValue);
	}
	
	/**
	 * Get a string from the shared preferences	
	 */
	public String getString(String key, String defValue) {
		return mSharedPreferences.getString(key, defValue);
	}
	
	
	/**
	 * Saves the ID of the currently selected character to SharedPreferences
	 * @param characterID
	 * @return true if the save was successful, false otherwise
	 */
	@Deprecated
	public boolean setSelectedCharacter(int characterID){
		mEditor.putInt(KEY_INT_SELECTED_CHARACTER_ID, characterID);
		Log.v(TAG, "Selected character set to "+characterID);
		return mEditor.commit();
	}
	
	/**
	 * @return The ID of the currently selected character. Returns -1 if no character is selected
	 */
	@Deprecated
	public int getSelectedCharacter(){
		return mSharedPreferences.getInt(KEY_INT_SELECTED_CHARACTER_ID, -1);
	}
	
	/**
	 * Saves the last tab visited in character sheet to SharedPreferences
	 * @param tab
	 * @return true if the save was successful, false otherwise
	 */
	@Deprecated
	public boolean setLastCharacterTab(int tab){
		mEditor.putInt(KEY_INT_LAST_TAB, tab);
		Log.v(TAG, "Save last tab to "+tab);
		return mEditor.commit();
	}
	
	/**
	 * @return The last tab visited in character sheet. Returns 0 if no value is set.
	 */
	@Deprecated
	public int getLastCharacterTab(){
		return mSharedPreferences.getInt(KEY_INT_LAST_TAB, 0);
	}
	
	/**
	 * Saves the ID of the currently selected party to SharedPreferences
	 * @param characterID
	 * @return true if the save was successful, false otherwise
	 */
	@Deprecated
	public boolean setSelectedParty(int partyID){
		mEditor.putInt(KEY_INT_SELECTED_PARTY_ID, partyID);
		Log.v(TAG, "Selected party set to "+partyID);
		return mEditor.commit();
	}
	
	/**
	 * @return The ID of the currently selected party. Returns -1 if no character is selected
	 */
	@Deprecated
	public int getSelectedParty(){
		return mSharedPreferences.getInt(KEY_INT_SELECTED_PARTY_ID, -1);
	}
	
	/**
	 * Saves the current party in the encounter to shared prefs
	 * @param encounterParty
	 * @return true if the save was successful, false otherwise
	 */
	@Deprecated
	public boolean setEncounterParty(PTParty encounterParty){
		Gson gson = new Gson();
		String partyJson = gson.toJson(encounterParty);
		mEditor.putString(KEY_STRING_ENCOUNTER_PARTY_JSON, partyJson);
		Log.v(TAG, "Saved current encounter party");
		return mEditor.commit();
	}
	
	/**
	 * Should be getting from database in future
	 * @return the current encounter party. returns null if none is saved.
	 */
	@Deprecated
	public PTParty getEncounterParty(){
		Gson gson = new Gson();
		String partyJson = mSharedPreferences.getString(KEY_STRING_ENCOUNTER_PARTY_JSON, "");
		PTParty encounterParty;
		try{
			encounterParty = gson.fromJson(partyJson, PTParty.class);
		}catch(JsonSyntaxException e){
			encounterParty = null;
		}
		return encounterParty;
	}
	
	/**
	 * Saves the unix time of the last time the user was asked to rate the app to SharedPreferences
	 * @return true if the save was successful, false otherwise
	 * @deprecated use putLong(KEY_LONG_LAST_RATE_PROMPT_TIME, defValue)
	 */
	@Deprecated
	public boolean setRatePromptTime(){
		long unixTime = System.currentTimeMillis();
		mEditor.putLong(KEY_LONG_LAST_RATE_PROMPT_TIME, unixTime);
		return mEditor.commit();
	}
	
	/**
	 * @return true if the app was prompted for rating more than one week ago. ie since KEY_LONG_LAST_RATE_TIME was set.
	 */
	public boolean isLastRateTimeLongEnough(){
		long lastRateTime = this.getLong(KEY_LONG_LAST_RATE_PROMPT_TIME, 0L);
		long currentUnixTime = Calendar.getInstance().getTimeInMillis();
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
	public boolean updateLastRatedVersion(Context context){
		PackageInfo pInfo;
		try{
			pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			mEditor.putInt(KEY_INT_LAST_RATED_VERSION, pInfo.versionCode);
			return mEditor.commit();
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
	public boolean hasRatedCurrentVersion(Context context){
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
	public boolean updateLastUsedVersion(Context context){
		PackageInfo pInfo;
		try{
			pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			mEditor.putInt(KEY_INT_LAST_USED_VERSION, pInfo.versionCode);
			return mEditor.commit();
		}catch (NameNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * @return true if a new version of the app is running, since KEY_INT_LAST_USED_VERSION
	 * was last updated
	 */
	public boolean isNewVersion(Context context){
		int appCode = this.getInt(KEY_INT_LAST_USED_VERSION, 0);
		PackageInfo pInfo;
		try{
			pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return appCode != pInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}
	
}
