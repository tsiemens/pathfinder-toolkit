package com.lateensoft.pathfinder.toolkit.settings;

import com.lateensoft.pathfinder.toolkit.R;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class PTGeneralPrefsFragment extends PreferenceFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		addPreferencesFromResource(R.xml.prefs_general);
	}
}
