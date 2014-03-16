package com.lateensoft.pathfinder.toolkit.views;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import com.lateensoft.pathfinder.toolkit.PTBaseApplication;
import com.lateensoft.pathfinder.toolkit.PTMainActivity;

import android.app.Activity;
import android.app.Fragment;
import android.view.View;

public abstract class PTBasePageFragment extends Fragment{

	private View m_rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
	public void onResume() {
		super.onResume();
        updateTitle();
		hideKeyboardDelayed(100);
	}

	public void setTitle(String title) {
        Activity a = getActivity();
        if (a != null) {
            ActionBar ab = a.getActionBar();
            if (ab != null) {
                ab.setTitle(title);
            }
        }
	}
	
	public void setTitle(int resId) {
        Activity a = getActivity();
        if (a != null) {
            ActionBar ab = a.getActionBar();
            if (ab != null) {
                ab.setTitle(resId);
            }
        }
	}
	
	public void setSubtitle(String subtitle) {
        Activity a = getActivity();
        if (a != null) {
            ActionBar ab = a.getActionBar();
            if (ab != null) {
                ab.setSubtitle(subtitle);
            }
        }
	}

    /**
     * Sets the default title and subtitle for the fragment
     */
    public abstract void updateTitle();
	
	public View getRootView() {
		return m_rootView;
	}
	
	protected void setRootView(View rootView) {
		m_rootView = rootView;
	}

    public Context getContext() {
        Activity a = getActivity();
        if (a != null) {
            return a;
        } else {
            return PTBaseApplication.getAppContext();
        }
    }

    public void switchToPage(final long pageId) {
        new MainActivityAction() {
            @Override public void performOnMainActivity(PTMainActivity activity) {
                activity.showView(pageId);
            }
        }.performAction();
    }

    public void hideKeyboardDelayed(final long delay) {
        new MainActivityAction() {
            @Override public void performOnMainActivity(PTMainActivity activity) {
                activity.hideKeyboardDelayed(delay);
            }
        }.performAction();
    }

    private abstract class MainActivityAction {
        public abstract void performOnMainActivity(PTMainActivity activity);

        public void performAction() {
            Activity a = PTBasePageFragment.this.getActivity();
            if (a instanceof PTMainActivity) {
                performOnMainActivity((PTMainActivity) a);
            }
        }
    }


}
