package com.lateensoft.pathfinder.toolkit.views.settings;

import android.app.ActionBar;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;

import com.lateensoft.pathfinder.toolkit.BaseApplication;
import com.lateensoft.pathfinder.toolkit.R;
import roboguice.RoboGuice;
import roboguice.activity.RoboActivity;

public class AboutActivity extends RoboActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_about_screen);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		TextView versionText = (TextView)findViewById(R.id.tv_version);

        PackageInfo pInfo = RoboGuice.getInjector(this).getInstance(PackageInfo.class);
		versionText.setText(getString(R.string.about_app_version) + " " + pInfo.versionName);

		// Makes the html <a> tags clickable 
		TextView oglText = (TextView)findViewById(R.id.tv_ogl_link);
		oglText.setMovementMethod(LinkMovementMethod.getInstance());
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
