package com.lateensoft.pathfinder.toolkit;

import android.view.View;

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
	
	protected void setTitle(String title) {
		((SherlockFragmentActivity)getActivity()).getSupportActionBar().setTitle(title);
	}
	
	protected void setTitle(int resId) {
		((SherlockFragmentActivity)getActivity()).getSupportActionBar().setTitle(resId);
	}
}
