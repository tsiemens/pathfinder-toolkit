package com.lateensoft.pathfinder.toolkit.patching;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.util.Log;

import com.lateensoft.pathfinder.toolkit.PTBaseApplication;
import com.lateensoft.pathfinder.toolkit.PTSharedPreferences;
import com.lateensoft.pathfinder.toolkit.db.PTDatabase;
import com.lateensoft.pathfinder.toolkit.db.repository.PTCharacterRepository;
import com.lateensoft.pathfinder.toolkit.db.repository.PTPartyRepository;
import com.lateensoft.pathfinder.toolkit.model.character.PTCharacter;
import com.lateensoft.pathfinder.toolkit.model.party.PTParty;

public class PTUpdatePatcher {
	private static final String TAG = PTUpdatePatcher.class.getSimpleName();

	public static boolean isPatchRequired() {
		return ( getPreviousVersion() < PTBaseApplication.getAppVersionCode() );
	}
	
	/**
	 * Applies all necessary patches to the app.
	 * @return false if was not 100% successful, and the user can expect some data loss.
	 */
	public static boolean applyUpdatePatches() {
		Log.i(TAG, "Applying update patches...");
		int prevVer = getPreviousVersion();
		boolean completeSuccess = true;
		
		// For the sake of functionality, creating database is part of the patch.
		PTDatabase.getInstance();
		
		if (prevVer != -1) {
			// Apply each patch in order, as required
			if (prevVer < 5) {
				applyPreV5Patch();
			}
			if (prevVer < 6) {
				if (!applyV5ToV6Patch()) {
					completeSuccess = false;
				}
			}
		}
		
		updateLastUsedVersion();
		Log.i(TAG, "Patching complete!");
		if (!completeSuccess) {
			Log.e(TAG, "Failures occurred during patch!");
		}
		return completeSuccess;
	}
	
	/**
	 * In version 6, the database was overhauled, so old values must be extracted from that database, and migrated
	 * to the new one, using the old deprecated data models. The old database must be deleted once complete.
	 * 
	 * ID's in shared preferences were changed from int to long, so those must be deleted and recreated.
	 * 
	 * Encounter party in shared preference is now in database
	 * 
	 * Delete last tab in shared prefs
	 * 
	 * @return false if was not 100% successful, and the user can expect some data loss.
	 */
	private static boolean applyV5ToV6Patch() {
		Log.i(TAG, "Applying v5 patches...");
		Context appContext = PTBaseApplication.getAppContext();
		PTCharacterRepository characterRepo = new PTCharacterRepository();
		PTPartyRepository partyRepo = new PTPartyRepository();
		PTSharedPreferences newSharedPrefs = PTSharedPreferences.getInstance();
		com.lateensoft.pathfinder.toolkit.deprecated.v1.PTUserPrefsManager oldPrefsManager = new com.lateensoft.pathfinder.toolkit.deprecated.v1.PTUserPrefsManager(appContext);
		com.lateensoft.pathfinder.toolkit.deprecated.v1.db.PTDatabaseManager oldDBManager = new com.lateensoft.pathfinder.toolkit.deprecated.v1.db.PTDatabaseManager(appContext);
		boolean completeSuccess = true;
		
		int oldSelectedCharacterID = oldPrefsManager.getSelectedCharacter();
		int oldSelectedPartyID = oldPrefsManager.getSelectedParty();
		
		// Delete, because need to convert to long later
		oldPrefsManager.remove(com.lateensoft.pathfinder.toolkit.deprecated.v1.PTUserPrefsManager.KEY_SHARED_PREFS_SELECTED_CHARACTER);
		oldPrefsManager.remove(com.lateensoft.pathfinder.toolkit.deprecated.v1.PTUserPrefsManager.KEY_SHARED_PREFS_SELECTED_PARTY);
		
		// Give user a week before they are asked to rate again.
		oldPrefsManager.remove(PTSharedPreferences.KEY_LONG_LAST_RATE_PROMPT_TIME);
		
		int[] oldCharIDs = oldDBManager.getCharacterIDs();
		com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.PTCharacter oldChar;
		PTCharacter newChar;
		for (int id : oldCharIDs) {
			oldChar = oldDBManager.getCharacter(id);
			newChar = PTCharacterConverter.convertCharacter(oldChar);
			
			if (characterRepo.insert(newChar) == -1){
				completeSuccess = false;
				Log.e(TAG, "Error migrating character "+oldChar.getName());
			} else if (id == oldSelectedCharacterID) {
				newSharedPrefs.putLong(PTSharedPreferences.KEY_LONG_SELECTED_CHARACTER_ID, newChar.getID());
			}
		}
		
		int[] oldPartyIDs = oldDBManager.getPartyIDs();
		com.lateensoft.pathfinder.toolkit.deprecated.v1.model.party.PTParty oldParty;
		PTParty newParty;
		for (int id : oldPartyIDs) {
			oldParty = oldDBManager.getParty(id);
			newParty = PTPartyConverter.convertParty(oldParty);
			if (partyRepo.insert(newParty) == -1) {
				completeSuccess = false;
				Log.e(TAG, "Error migrating party "+oldParty.getName());
			} if (id == oldSelectedPartyID) {
				newSharedPrefs.putLong(PTSharedPreferences.KEY_LONG_SELECTED_PARTY_ID, newParty.getID());
			}
		}
		
		oldParty = null;
		oldParty = oldPrefsManager.getEncounterParty();
		if (oldParty != null) {
			PTParty newEncParty = PTPartyConverter.convertParty(oldParty);
			if (partyRepo.insert(newEncParty, true) == -1) {
				completeSuccess = false;
				Log.e(TAG, "Error migrating encounter party "+oldParty.getName());
			}
		}
		
		// Final cleanup
		oldPrefsManager.remove(com.lateensoft.pathfinder.toolkit.deprecated.v1.PTUserPrefsManager.KEY_SHARED_PREFS_ENCOUNTER_PARTY);
		oldPrefsManager.remove(com.lateensoft.pathfinder.toolkit.deprecated.v1.PTUserPrefsManager.KEY_SHARED_PREFS_LAST_TAB);
		
		appContext.deleteDatabase(com.lateensoft.pathfinder.toolkit.deprecated.v1.db.PTDatabaseManager.dbName);
		
		Log.i(TAG, "v5 patch complete");
		return completeSuccess;
	}
	
	private static void applyPreV5Patch() {
		Log.i(TAG, "Applying pre v5 patch...");
		Context appContext = PTBaseApplication.getAppContext();
		com.lateensoft.pathfinder.toolkit.deprecated.v1.db.PTDatabaseManager oldDBManager = new com.lateensoft.pathfinder.toolkit.deprecated.v1.db.PTDatabaseManager(appContext);
		oldDBManager.performUpdates(appContext);
		Log.i(TAG, "Pre v5 patch complete");
	}
	
	/**
	 * Sets the last used version in shared preferences to the current version
	 * @return true if the save was successful, false otherwise
	 */
	public static boolean updateLastUsedVersion(){
		Context context = PTBaseApplication.getAppContext();
		PackageInfo pInfo;
		try{
			pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return PTSharedPreferences.getInstance().putInt(PTSharedPreferences.KEY_INT_LAST_USED_VERSION, pInfo.versionCode);
		}catch (NameNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * @return The last used version code, before patch application, or -1 if none exists.
	 */
	public static int getPreviousVersion(){
		return PTSharedPreferences.getInstance().getInt(PTSharedPreferences.KEY_INT_LAST_USED_VERSION, -1);
	}
	
	/**
	 * AsyncTask that performs patch
	 * Can have a single listener
	 * @author trevsiemens
	 *
	 */
	public static class PatcherTask extends AsyncTask<PatcherListener, Void, Boolean> {

		private PatcherListener m_listener;
		
		@Override
		protected Boolean doInBackground(PatcherListener... params) {
			if (params.length > 0) {
				m_listener = params[0];
			}
			return applyUpdatePatches();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (m_listener != null) {
				m_listener.onPatchComplete(result);
			}
			super.onPostExecute(result);
		}	
	}
	
	public static interface PatcherListener {
		public void onPatchComplete(boolean completeSuccess);
	}
}
