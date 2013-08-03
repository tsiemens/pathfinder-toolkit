package com.lateensoft.pathfinder.toolkit.settings;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.datahelpers.PTSharedPreferences;

public class PTAboutActivity extends SherlockActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_about_screen);
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		TextView versionText = (TextView)findViewById(R.id.tv_version);
		
		versionText.setText(getString(R.string.about_app_version) + " " + 
				PTSharedPreferences.getSharedInstance().getAppVersionName());

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
