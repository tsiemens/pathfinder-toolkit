package com.lateensoft.pathfinder.toolkit.views;

import android.os.Bundle;
import com.lateensoft.pathfinder.toolkit.PTMainActivity;

import android.app.Activity;
import android.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class PTBasePageFragment extends Fragment{

	private View m_rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
	public void onResume() {
		super.onResume();
		Activity a = getActivity();
		if (a instanceof PTMainActivity) {
			((PTMainActivity) a).hideKeyboardDelayed(100);
		}
	}

	public void setTitle(String title) {
		getActivity().getActionBar().setTitle(title);
	}
	
	public void setTitle(int resId) {
		getActivity().getActionBar().setTitle(resId);
	}
	
	public void setSubtitle(String subtitle) {
		getActivity().getActionBar().setSubtitle(subtitle);
	}
	
	public View getRootView() {
		return m_rootView;
	}
	
	protected void setRootView(View rootView) {
		m_rootView = rootView;
	}
	
	public MenuInflater getMenuInflater() {
		Activity a = getActivity();
		if (a != null) {
			return a.getMenuInflater();
		} else {
			return null;
		}
	}
}
