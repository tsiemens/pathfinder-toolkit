package com.lateensoft.pathfinder.toolkit.settings;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.lateensoft.pathfinder.toolkit.R;

public class PTAboutActivity extends SherlockActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_about_screen);
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		TextView versionText = (TextView)findViewById(R.id.tv_version);
		
		PackageInfo pInfo;
		try{
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			versionText.setText(getString(R.string.about_app_version) + " " + pInfo.versionName);
		}catch (NameNotFoundException e) {
			e.printStackTrace();
		}

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
