package com.lateensoft.pathfinder.toolkit.views.settings;

import com.lateensoft.pathfinder.toolkit.R;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class GeneralPrefsFragment extends PreferenceFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		addPreferencesFromResource(R.xml.prefs_general);
	}
}
