package com.lateensoft.pathfinder.toolkit.views.settings;

import android.app.ActionBar;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.MenuItem;
import roboguice.activity.RoboPreferenceActivity;

public class SettingsActivity extends RoboPreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        // Because we only have general settings now, we have no headers,
        // and just pass off to the fragment handling prefs.
        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new GeneralPrefsFragment()).commit();
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    
}
