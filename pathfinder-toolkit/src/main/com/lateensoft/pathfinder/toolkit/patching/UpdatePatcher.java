package com.lateensoft.pathfinder.toolkit.patching;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.google.inject.Injector;
import com.lateensoft.pathfinder.toolkit.patching.v10.PostV9Patch;
import com.lateensoft.pathfinder.toolkit.patching.v2.PreV5Patch;
import com.lateensoft.pathfinder.toolkit.patching.v6.PostV5Patch;
import com.lateensoft.pathfinder.toolkit.pref.GlobalPrefs;
import com.lateensoft.pathfinder.toolkit.pref.Preferences;
import roboguice.RoboGuice;

public class UpdatePatcher {
    private static final String TAG = UpdatePatcher.class.getSimpleName();

    private Context context;
    private Preferences preferences;
    private PackageInfo packageInfo;

    public UpdatePatcher(Context context) {
        this.context = context;
        Injector injector = RoboGuice.getInjector(context);
        preferences = injector.getInstance(Preferences.class);
        packageInfo = injector.getInstance(PackageInfo.class);
    }

    public boolean isPatchRequired() {
        int prevVersion = getPreviousVersion();
        return prevVersion != -1 && prevVersion < packageInfo.versionCode;
    }

    public int getPreviousVersion(){
        return preferences.get(GlobalPrefs.LAST_USED_VERSION, -1);
    }

    public boolean updateLastUsedVersion(){
        return preferences.put(GlobalPrefs.LAST_USED_VERSION, packageInfo.versionCode);
    }

    /**
     * @return true if was 100% successful
     */
    public boolean applyUpdatePatches() {
        // Give user a week before they are asked to rate again.
        preferences.remove(GlobalPrefs.LAST_RATE_PROMPT_TIME);

        Log.i(TAG, "Applying update patches...");
        boolean completeSuccess = true;

        Patch currentPatch = getFirstPatchForVersion(getPreviousVersion());
        while (currentPatch != null) {
            completeSuccess = currentPatch.apply() && completeSuccess;
            currentPatch = currentPatch.getNext();
        }

        updateLastUsedVersion();
        Log.i(TAG, "Patching complete!");
        if (!completeSuccess) {
            Log.e(TAG, "Failures occurred during patch!");
        }
        return completeSuccess;
    }

    private Patch getFirstPatchForVersion(int previousVersion) {
        if (previousVersion < 1) {
            return null;
        }
        else if (previousVersion < 5) {
            return new PreV5Patch(context);
        }
        else if (previousVersion < 6) {
            return new PostV5Patch(context);
        }
        else if (previousVersion < 10) {
            return new PostV9Patch(context);
        }
        else {
            return null;
        }
    }

    public static class PatcherTask extends AsyncTask<PatcherListener, Void, Boolean> {

        private PatcherListener m_listener;
        private UpdatePatcher patcher;

        public PatcherTask(UpdatePatcher patcher) {
            this.patcher = patcher;
        }
        
        @Override
        protected Boolean doInBackground(PatcherListener... params) {
            if (params.length > 0) {
                m_listener = params[0];
            }
            return patcher.applyUpdatePatches();
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
