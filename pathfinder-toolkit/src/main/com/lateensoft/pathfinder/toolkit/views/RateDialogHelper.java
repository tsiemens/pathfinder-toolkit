package com.lateensoft.pathfinder.toolkit.views;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.util.Log;
import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.pref.GlobalPrefs;
import com.lateensoft.pathfinder.toolkit.pref.Preferences;
import roboguice.RoboGuice;

public class RateDialogHelper {
    private static final String TAG = RateDialogHelper.class.getSimpleName();

    private static final long MILLISECONDS_BETWEEN_RATE_PROMPT = 604800000L; //One week

    private AlertDialog.Builder builder;
    private Context context;
    private Preferences preferences;

	public RateDialogHelper(Context context) {
        this.context = context;
        preferences = RoboGuice.getInjector(context).getInstance(Preferences.class);
	}

    public void buildAndShowDialog() {
        build();
        show();
    }

    private void build() {
        builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.rate_dialog_title)
                .setMessage(R.string.rate_dialog_message)
                .setPositiveButton(R.string.rate_dialog_positive_button, new RateDialogClickListener())
                .setNegativeButton(R.string.rate_dialog_negative_button, null);
    }

    private void show() {
        preferences.put(GlobalPrefs.LAST_RATE_PROMPT_TIME, System.currentTimeMillis());
        builder.show();
    }

    public boolean shouldPromptToRate() {
        return hasRatedCurrentVersion() && hasBeenLongEnoughtSinceLastPrompt();
    }

    private boolean hasBeenLongEnoughtSinceLastPrompt(){
        long lastRateTime = preferences.get(GlobalPrefs.LAST_RATE_PROMPT_TIME, 0L);
        long currentUnixTime = System.currentTimeMillis();
        if( lastRateTime == 0L ){
            return false;
        } else {
            Log.i(TAG, Long.toString((currentUnixTime - lastRateTime)) + " ms since last rate prompt");
            return (currentUnixTime - lastRateTime) > MILLISECONDS_BETWEEN_RATE_PROMPT;
        }
    }

    private void updateLastRatedVersion(){
        int versionCode = getVersionCode();
        preferences.put(GlobalPrefs.LAST_RATED_VERSION, versionCode);
    }

    private boolean hasRatedCurrentVersion(){
        int versionCode = getVersionCode();
        int lastRatedVerion = preferences.get(GlobalPrefs.LAST_RATED_VERSION, 0);
        return versionCode == lastRatedVerion;
    }

    private int getVersionCode() {
        PackageInfo pInfo = RoboGuice.getInjector(context).getInstance(PackageInfo.class);
        return pInfo.versionCode;
    }

    private class RateDialogClickListener implements OnClickListener {
        @Override
        public void onClick(DialogInterface dialogInterface, int selection) {

            if (selection == DialogInterface.BUTTON_POSITIVE) {
                updateLastRatedVersion();
                attemptToOpenMarketPage();
            }
        }

        public void attemptToOpenMarketPage() {
            Log.i(TAG, "Attempting to open market page");
            String appPackageName = context.getPackageName();
            try {
                context.startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + appPackageName)));
            } catch (ActivityNotFoundException e) {
                Log.i(TAG, "Failed to open market. Attempting in browser.");
                context.startActivity(new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id="
                                + appPackageName)
                ));
            }
        }
    }
}
