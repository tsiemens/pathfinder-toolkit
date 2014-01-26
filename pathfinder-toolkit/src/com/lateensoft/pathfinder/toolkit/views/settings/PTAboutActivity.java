package com.lateensoft.pathfinder.toolkit.views.settings;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;

import com.lateensoft.pathfinder.toolkit.PTBaseApplication;
import com.lateensoft.pathfinder.toolkit.R;

public class PTAboutActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_about_screen);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		TextView versionText = (TextView)findViewById(R.id.tv_version);
		
		versionText.setText(getString(R.string.about_app_version) + " " + 
				PTBaseApplication.getAppVersionName());

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
