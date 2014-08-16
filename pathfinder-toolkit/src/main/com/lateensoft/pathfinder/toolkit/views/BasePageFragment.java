package com.lateensoft.pathfinder.toolkit.views;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import com.google.common.base.Preconditions;
import com.lateensoft.pathfinder.toolkit.MainActivity;

import android.app.Activity;
import android.view.View;
import roboguice.fragment.RoboFragment;

public abstract class BasePageFragment extends RoboFragment{

    private View rootView;

    private boolean isWaitingForResult = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isWaitingForResult) {
            onResumeWithoutResult();
        }
        updateTitle();
        isWaitingForResult = false;
        hideKeyboardDelayed(100);
    }

    protected void onResumeWithoutResult() { }

    @Override
    public final void onPrepareOptionsMenu(final Menu menu) {
        super.onPrepareOptionsMenu(menu);
        new MainActivityAction() {
            @Override public void performOnMainActivity(MainActivity activity) {
                if (!activity.isDrawerOpen()) {
                    onPreparePageOptionsMenu(menu);
                }
            }
        }.performAction();
    }

    protected void onPreparePageOptionsMenu(Menu menu) { }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        isWaitingForResult = true;
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

    /** Sets the default title and subtitle for the fragment */
    public abstract void updateTitle();
    
    public View getRootView() {
        return rootView;
    }
    
    protected void setRootView(View rootView) {
        this.rootView = rootView;
    }

    public Context getContext() {
        Activity a = getActivity();
        return Preconditions.checkNotNull(a);
    }

    public void switchToPage(final Class<? extends BasePageFragment> fragment) {
        new MainActivityAction() {
            @Override public void performOnMainActivity(MainActivity activity) {
                activity.showFragment(fragment);
            }
        }.performAction();
    }

    public void hideKeyboardDelayed(final long delay) {
        new MainActivityAction() {
            @Override public void performOnMainActivity(MainActivity activity) {
                activity.hideKeyboardDelayed(delay);
            }
        }.performAction();
    }

    private abstract class MainActivityAction {
        public abstract void performOnMainActivity(MainActivity activity);

        public void performAction() {
            Activity a = BasePageFragment.this.getActivity();
            if (a instanceof MainActivity) {
                performOnMainActivity((MainActivity) a);
            }
        }
    }

    /** There appears to be a conflict between some of the android SDK and JDK libraries, which
     * causes some kind of ambiguity between Object.class() and Object.class(), especially for support fragments.
     * This is a quick and dirty fix */
    @Deprecated
    @SuppressWarnings("unchecked")
    public <T extends BasePageFragment> Class<T> getSupportClass() {
        return (Class<T>) ((Object) this).getClass();
    }
}
