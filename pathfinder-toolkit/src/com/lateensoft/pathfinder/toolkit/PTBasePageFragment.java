package com.lateensoft.pathfinder.toolkit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
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
}
