package com.lateensoft.pathfinder.toolkit;

import android.view.View;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class PTBasePageFragment extends SherlockFragment{

	protected View mParentView;

	public boolean onCreateOptionsMenu(Menu menu) {		
		return true;
	}
	

	public boolean onOptionsItemSelected(MenuItem item) {
		return false;
	}
	
	protected void setActionBarTitle(String title, String subtitle) {
		ActionBar ab = ((SherlockFragmentActivity)getActivity()).getSupportActionBar();
		ab.setTitle(title);
		ab.setSubtitle(subtitle);
	}
	
	protected void setActionBarTitle(int titleResId, String subtitle) {
		ActionBar ab = ((SherlockFragmentActivity)getActivity()).getSupportActionBar();
		ab.setTitle(titleResId);
		ab.setSubtitle(subtitle);
	}
}
