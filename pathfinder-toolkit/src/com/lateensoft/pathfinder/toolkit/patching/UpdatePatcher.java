package com.lateensoft.pathfinder.toolkit.patching;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.util.Log;

import com.lateensoft.pathfinder.toolkit.AppPreferences;
import com.lateensoft.pathfinder.toolkit.BaseApplication;
import com.lateensoft.pathfinder.toolkit.db.Database;
import com.lateensoft.pathfinder.toolkit.db.repository.CharacterRepository;
import com.lateensoft.pathfinder.toolkit.db.repository.PartyRepository;
import com.lateensoft.pathfinder.toolkit.deprecated.v1.PTUserPrefsManager;
import com.lateensoft.pathfinder.toolkit.model.character.PathfinderCharacter;
import com.lateensoft.pathfinder.toolkit.model.party.CampaignParty;

public class UpdatePatcher {
	private static final String TAG = UpdatePatcher.class.getSimpleName();

	public static boolean isPatchRequired() {
		return ( getPreviousVersion() < BaseApplication.getAppVersionCode() );
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
		Database.getInstance();
		
		if (prevVer != -1) {
            // Apply each patch in order, as required.
			if (prevVer < 6) {
                // These are non-forward compatible, and will need to be updated.
                if (prevVer < 5) {
                    applyPreV5Patch();
                }
				if (!applyV5ToCurrentPatch()) {
					completeSuccess = false;
				}
			} else {
                // These should be mostly forward compatible patches from now on.
                if (prevVer < 10) {
                    if (!applyV9ToV10Patch()) {
                        completeSuccess = false;
                    }
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
     * TODO - update party table to not have isencounterparty col
     * TODO - create encounter, partymembership, encounterparticipation tables
     * TODO - migrate partymembers to characters with party membership, and delete partymembers table
     * TODO - convert single encounter in party database to an encounter, then remove
     * All this should be done while avoiding the use of our datamodels to provide forward compatibility.
     * This may be unavoidable though with the transfer from partymember to character
     */
    private static boolean applyV9ToV10Patch() {

        return false;
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
	private static boolean applyV5ToCurrentPatch() {
		Log.i(TAG, "Applying v5 patches...");
		Context appContext = BaseApplication.getAppContext();
		CharacterRepository characterRepo = new CharacterRepository();
		PartyRepository partyRepo = new PartyRepository();
		AppPreferences newSharedPrefs = AppPreferences.getInstance();
		PTUserPrefsManager oldPrefsManager = new PTUserPrefsManager(appContext);
		com.lateensoft.pathfinder.toolkit.deprecated.v1.db.PTDatabaseManager oldDBManager = new com.lateensoft.pathfinder.toolkit.deprecated.v1.db.PTDatabaseManager(appContext);
		boolean completeSuccess = true;
		
		int oldSelectedCharacterID;
        try {
            oldSelectedCharacterID = oldPrefsManager.getSelectedCharacter();
        } catch (ClassCastException e) {
            // Cases in which this has become a long in preferences for unknown reason.
            oldSelectedCharacterID =
                    Long.valueOf(newSharedPrefs.getLong(PTUserPrefsManager.KEY_SHARED_PREFS_SELECTED_CHARACTER, -1)).intValue();
        }

        int oldSelectedPartyID;
        try {
            oldSelectedPartyID = oldPrefsManager.getSelectedParty();
        } catch (ClassCastException e) {
            oldSelectedPartyID =
                    Long.valueOf(newSharedPrefs.getLong(PTUserPrefsManager.KEY_SHARED_PREFS_SELECTED_PARTY, -1)).intValue();
        }
		
		// Delete, because need to convert to long later
		oldPrefsManager.remove(PTUserPrefsManager.KEY_SHARED_PREFS_SELECTED_CHARACTER);
		oldPrefsManager.remove(PTUserPrefsManager.KEY_SHARED_PREFS_SELECTED_PARTY);
		
		// Give user a week before they are asked to rate again.
		oldPrefsManager.remove(AppPreferences.KEY_LONG_LAST_RATE_PROMPT_TIME);
		
		int[] oldCharIDs = oldDBManager.getCharacterIDs();
		com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.PTCharacter oldChar;
		PathfinderCharacter newChar;
		for (int id : oldCharIDs) {
			oldChar = oldDBManager.getCharacter(id);
			newChar = CharacterConverter.convertCharacter(oldChar);
			
			if (characterRepo.insert(newChar) == -1){
				completeSuccess = false;
				Log.e(TAG, "Error migrating character "+oldChar.getName());
			} else if (id == oldSelectedCharacterID) {
				newSharedPrefs.putLong(AppPreferences.KEY_LONG_SELECTED_CHARACTER_ID, newChar.getID());
			}
		}
		
		int[] oldPartyIDs = oldDBManager.getPartyIDs();
		com.lateensoft.pathfinder.toolkit.deprecated.v1.model.party.PTParty oldParty;
		CampaignParty newParty;
		for (int id : oldPartyIDs) {
			oldParty = oldDBManager.getParty(id);
			newParty = PartyConverter.convertParty(oldParty);
			if (partyRepo.insert(newParty) == -1) {
				completeSuccess = false;
				Log.e(TAG, "Error migrating party "+oldParty.getName());
			} if (id == oldSelectedPartyID) {
				newSharedPrefs.putLong(AppPreferences.KEY_LONG_SELECTED_PARTY_ID, newParty.getID());
			}
		}
		
		oldParty = null;
		oldParty = oldPrefsManager.getEncounterParty();
		if (oldParty != null) {
			CampaignParty newEncParty = PartyConverter.convertParty(oldParty);
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
		Context appContext = BaseApplication.getAppContext();
		com.lateensoft.pathfinder.toolkit.deprecated.v1.db.PTDatabaseManager oldDBManager = new com.lateensoft.pathfinder.toolkit.deprecated.v1.db.PTDatabaseManager(appContext);
		oldDBManager.performUpdates(appContext);
		Log.i(TAG, "Pre v5 patch complete");
	}
	
	/**
	 * Sets the last used version in shared preferences to the current version
	 * @return true if the save was successful, false otherwise
	 */
	public static boolean updateLastUsedVersion(){
		Context context = BaseApplication.getAppContext();
		PackageInfo pInfo;
		try{
			pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return AppPreferences.getInstance().putInt(AppPreferences.KEY_INT_LAST_USED_VERSION, pInfo.versionCode);
		}catch (NameNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * @return The last used version code, before patch application, or -1 if none exists.
	 */
	public static int getPreviousVersion(){
		return AppPreferences.getInstance().getInt(AppPreferences.KEY_INT_LAST_USED_VERSION, -1);
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
