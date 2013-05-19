package com.lateensoft.pathfinder.toolkit.datahelpers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.lateensoft.pathfinder.toolkit.party.PTParty;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class PTUserPrefsManager {
	private final String TAG = "PTUserPrefsManager";
	
	private final long SECONDS_BETWEEN_RATE_PROMPT = 604800L; //One week
	
	private final String KEY_APP_SHARED_PREFS_NAME = "ptUserPrefs";
	private final String KEY_SHARED_PREFS_LAST_RATE_TIME = "lastRateTime";
	private final String KEY_SHARED_PREFS_LAST_RATE_VERSION = "lastRateVersion";
	private final String KEY_SHARED_PREFS_LAST_USED_VERSION = "lastUsedVersion";
    private final String KEY_SHARED_PREFS_SELECTED_CHARACTER = "selectedCharacter";
    private final String KEY_SHARED_PREFS_LAST_TAB = "lastTab";
    private final String KEY_SHARED_PREFS_SELECTED_PARTY = "selectedParty";
    private final String KEY_SHARED_PREFS_ENCOUNTER_PARTY = "encounterParty";

	SharedPreferences mSharedPreferences;
	Editor mEditor;
	
	/**
	 * Manages the shared preferences for the Pathfinder Toolkit application 
	 * @param context
	 */
	public PTUserPrefsManager(Context context){
		Context appContext = context.getApplicationContext();
		mSharedPreferences = context.getApplicationContext().getSharedPreferences(
				KEY_APP_SHARED_PREFS_NAME, appContext.MODE_PRIVATE);
		mEditor = mSharedPreferences.edit();
	}
		
	/**
	 * Saves the ID of the currently selected character to SharedPreferences
	 * @param characterID
	 * @return true if the save was successful, false otherwise
	 */
	public boolean setSelectedCharacter(int characterID){
		mEditor.putInt(KEY_SHARED_PREFS_SELECTED_CHARACTER, characterID);
		Log.v(TAG, "Selected character set to "+characterID);
		return mEditor.commit();
	}
	
	/**
	 * @return The ID of the currently selected character. Returns -1 if no character is selected
	 */
	public int getSelectedCharacter(){
		return mSharedPreferences.getInt(KEY_SHARED_PREFS_SELECTED_CHARACTER, -1);
	}
	
	/**
	 * Saves the last tab visited in character sheet to SharedPreferences
	 * @param tab
	 * @return true if the save was successful, false otherwise
	 */
	public boolean setLastCharacterTab(int tab){
		mEditor.putInt(KEY_SHARED_PREFS_LAST_TAB, tab);
		Log.v(TAG, "Save last tab to "+tab);
		return mEditor.commit();
	}
	
	/**
	 * @return The last tab visited in character sheet. Returns 0 if no value is set.
	 */
	public int getLastCharacterTab(){
		return mSharedPreferences.getInt(KEY_SHARED_PREFS_LAST_TAB, 0);
	}
	
	/**
	 * Saves the ID of the currently selected party to SharedPreferences
	 * @param characterID
	 * @return true if the save was successful, false otherwise
	 */
	public boolean setSelectedParty(int partyID){
		mEditor.putInt(KEY_SHARED_PREFS_SELECTED_PARTY, partyID);
		Log.v(TAG, "Selected party set to "+partyID);
		return mEditor.commit();
	}
	
	/**
	 * @return The ID of the currently selected party. Returns -1 if no character is selected
	 */
	public int getSelectedParty(){
		return mSharedPreferences.getInt(KEY_SHARED_PREFS_SELECTED_PARTY, -1);
	}
	
	/**
	 * Saves the current party in the encounter to shared prefs
	 * @param encounterParty
	 * @return true if the save was successful, false otherwise
	 */
	public boolean setEncounterParty(PTParty encounterParty){
		Gson gson = new Gson();
		String partyJson = gson.toJson(encounterParty);
		mEditor.putString(KEY_SHARED_PREFS_ENCOUNTER_PARTY, partyJson);
		Log.v(TAG, "Saved current encounter party");
		return mEditor.commit();
	}
	
	/**
	 * 
	 * @return the current encounter party. returns null if none is saved.
	 */
	public PTParty getEncounterParty(){
		Gson gson = new Gson();
		String partyJson = mSharedPreferences.getString(KEY_SHARED_PREFS_ENCOUNTER_PARTY, "");
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
	 */
	public boolean setRatePromptTime(){
		long unixTime = System.currentTimeMillis() / 1000L;
		mEditor.putLong(KEY_SHARED_PREFS_LAST_RATE_TIME, unixTime);
		return mEditor.commit();
	}
	
	/**
	 * @return true if the app was rated more than one week ago
	 */
	public boolean checkLastRateTime(){
		long lastRateTime = mSharedPreferences.getLong(KEY_SHARED_PREFS_LAST_RATE_TIME, 0L);
		long currentUnixTime = System.currentTimeMillis() / 1000L;
		if( lastRateTime == 0L ){
			setRatePromptTime();
			return false;
		}else
			return (currentUnixTime - lastRateTime) > SECONDS_BETWEEN_RATE_PROMPT;	
	}
	
	/**
	 * Sets the last rated version to the current version
	 * @return true if the save was successful, false otherwise
	 */
	public boolean setLastRatedVersion(Context context){
		PackageInfo pInfo;
		try{
			pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			mEditor.putInt(KEY_SHARED_PREFS_LAST_RATE_VERSION, pInfo.versionCode);
			return mEditor.commit();
		}catch (NameNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 
	 * @return true if the last rated version is not the current one (version code) 
	 */
	public boolean checkLastRatedVersion(Context context){
		int appCode = mSharedPreferences.getInt(KEY_SHARED_PREFS_LAST_RATE_VERSION, 0);
		PackageInfo pInfo;
		try{
			pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return appCode != pInfo.versionCode;
		}catch (NameNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Sets the last used version to the current version
	 * @return true if the save was successful, false otherwise
	 */
	public boolean setLastUsedVersion(Context context){
		PackageInfo pInfo;
		try{
			pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			mEditor.putInt(KEY_SHARED_PREFS_LAST_USED_VERSION, pInfo.versionCode);
			return mEditor.commit();
		}catch (NameNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 
	 * @return true if the last used version is not the current one (version code) 
	 */
	public boolean checkLastUsedVersion(Context context){
		int appCode = mSharedPreferences.getInt(KEY_SHARED_PREFS_LAST_USED_VERSION, 0);
		PackageInfo pInfo;
		try{
			pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return appCode != pInfo.versionCode;
		}catch (NameNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}
	
}
