package com.lateensoft.pathfinder.toolkit;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.lateensoft.pathfinder.toolkit.datahelpers.PTDatabaseManager;
import com.lateensoft.pathfinder.toolkit.datahelpers.PTUserPrefsManager;

public class PTMainActivity extends SherlockFragmentActivity implements
		OnClickListener, OnChildClickListener, OnGroupClickListener,
		OnGroupExpandListener, OnGroupCollapseListener {
	private final String TAG = PTMainActivity.class.getSimpleName();

	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private ExpandableListView mDrawerList;
	
	private PTBasePageFragment mCurrentFragment;

	String mListLabels[];
	private PTUserPrefsManager mUserPrefsManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mUserPrefsManager = new PTUserPrefsManager(this);
		PTDatabaseManager SQLManager = new PTDatabaseManager(
				this.getApplicationContext());

		// Needs to update the database after upgrading
		if (mUserPrefsManager.checkLastUsedVersion(this)) {
			SQLManager.performUpdates(this);
			mUserPrefsManager.setLastUsedVersion(this);
		}

		mListLabels = getResources().getStringArray(R.array.main_menu_array);

		setContentView(R.layout.activity_drawer_main);

		showRateDialogIfRequired();

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
				mDrawerLayout, /* DrawerLayout object */
				R.drawable.ic_drawer, /* nav drawer icon to replace 'Up' caret */
				R.string.app_name, /* "open drawer" description */
				R.string.app_name /* "close drawer" description */
				) {

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				supportInvalidateOptionsMenu();
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				supportInvalidateOptionsMenu();
			}
		};
		mDrawerToggle.syncState();

		// Set the drawer toggle as the DrawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		mDrawerList = (ExpandableListView) findViewById(R.id.left_drawer);

		// Set the adapter for the list view
		mDrawerList.setAdapter(new PTNavDrawerAdapter(this));
		mDrawerList.setGroupIndicator(getResources().getDrawable(R.drawable.nav_item_expand_icon));
		// Set the list's click listener
		// mDrawerList.setOnItemClickListener(this);
		mDrawerList.setOnChildClickListener(this);
		mDrawerList.setOnGroupClickListener(this);
		mDrawerList.setOnGroupExpandListener(this);
		mDrawerList.setOnGroupCollapseListener(this);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
	}

	private void showRateDialogIfRequired() {
		if (mUserPrefsManager.checkLastRateTime()
				&& mUserPrefsManager.checkLastRatedVersion(this)) {
			showRateAppPromptDialog();
		}
	}

	public void showRateAppPromptDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// Set up dialog layout

		ImageView dialogMemeImage = new ImageView(this);
		dialogMemeImage.setImageResource(R.drawable.rate_meme);
		dialogMemeImage.setScaleType(ScaleType.FIT_CENTER);

		builder.setTitle(getString(R.string.rate_dialog_header));

		builder.setView(dialogMemeImage)
				.setPositiveButton(
						getString(R.string.rate_dialog_positive_button), this)
				.setNegativeButton(
						getString(R.string.rate_dialog_negative_button), this);

		AlertDialog alert = builder.create();
		alert.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Initialize the global menu items
		if (mCurrentFragment != null) {
			mCurrentFragment.onCreateOptionsMenu(menu);
		}
		PTSharedMenu.onCreateOptionsMenu(menu, getApplicationContext());

		// Add aditional menu items
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		for (int i = 0; i < menu.size(); i++) {
			menu.getItem(i).setVisible(!drawerOpen);

		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
				mDrawerLayout.closeDrawer(mDrawerList);
			} else {
				mDrawerLayout.openDrawer(mDrawerList);
			}
		}

		if (PTSharedMenu.onOptionsItemSelected(item, this) == false) {
			if (mCurrentFragment != null) {
				mCurrentFragment.onOptionsItemSelected(item);
			}
		}

		return super.onOptionsItemSelected(item);
	}

	// For dialog
	public void onClick(DialogInterface dialogInterface, int selection) {
		switch (selection) {
		case DialogInterface.BUTTON_POSITIVE:
			mUserPrefsManager.setLastRatedVersion(this);
			mUserPrefsManager.setRatePromptTime();
			Log.v(TAG, "Attempting to open market page");
			String appPackageName = this.getPackageName();
			try {
				startActivity(new Intent(Intent.ACTION_VIEW,
						Uri.parse("market://details?id=" + appPackageName)));
			} catch (ActivityNotFoundException e) {
				Log.v(TAG, "Failed to open market. Attempting in browser.");
				startActivity(new Intent(
						Intent.ACTION_VIEW,
						Uri.parse("http://play.google.com/store/apps/details?id="
								+ appPackageName)));
			}
			break;
		default:
			Log.v(TAG, "Delaying rate dialog.");
			mUserPrefsManager.setRatePromptTime();
		}

	}
	
	public void showView(long id) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		PTBasePageFragment newFragment = null;
		
		
		
		if (id == PTNavDrawerAdapter.DICE_ROLLER_ID) {
			newFragment = new PTDiceRollerFragment();		
		} else if ( id == PTNavDrawerAdapter.FLUFF_ID ) {
			// TODO
			
		} else if ( id == PTNavDrawerAdapter.COMBAT_STATS_ID ) {
			// TODO
			
		} else if ( id == PTNavDrawerAdapter.ABILITIES_ID ) {
			// TODO
			
		} else if ( id == PTNavDrawerAdapter.SKILLS_ID ) {
			// TODO
			
		} else if ( id == PTNavDrawerAdapter.INVENTORY_ID ) {
			// TODO
			
		} else if ( id == PTNavDrawerAdapter.ARMOR_ID ) {
			// TODO
			
		} else if ( id == PTNavDrawerAdapter.WEAPONS_ID ) {
			// TODO
			
		} else if ( id == PTNavDrawerAdapter.FEATS_ID ) {
			// TODO
			
		} else if ( id == PTNavDrawerAdapter.SPELLS_ID ) {
			// TODO
			
		} else if ( id == PTNavDrawerAdapter.INITIATIVE_TRACKER_ID ) {
			newFragment = new PTInitiativeTrackerFragment();
			
		} else if ( id == PTNavDrawerAdapter.SKILL_CHECKER_ID ) {
			newFragment = new PTPartySkillCheckerFragment();
			
		} else if ( id == PTNavDrawerAdapter.PARTY_MANAGER_ID ) {
			newFragment = new PTPartyManagerFragment();
			
		} else if ( id == PTNavDrawerAdapter.POINTBUY_ID ) {
			newFragment = new PTPointbuyCalculatorFragment();
		}
		
		if (newFragment != null) {
			((PTNavDrawerAdapter)mDrawerList.getExpandableListAdapter()).setSelectedItem(id);
			mDrawerList.invalidateViews();
			fragmentTransaction.replace(R.id.content_frame, newFragment);
			fragmentTransaction.commit();
			mCurrentFragment = newFragment;
			supportInvalidateOptionsMenu();
		}
	}

	@Override
	public void onGroupCollapse(int arg0) {
		// Do nothing
	}

	@Override
	public void onGroupExpand(int arg0) {
		// Do nothing
	}

	@Override
	public boolean onGroupClick(ExpandableListView list, View parent, int position,
			long id)
	{
		if (id != PTNavDrawerAdapter.CHARACTER_SHEET_ID ) {
			if (id != ((PTNavDrawerAdapter)list.getExpandableListAdapter()).getSelectedItem()) {
				showView(id);		
			}
			mDrawerLayout.closeDrawer(mDrawerList);
		}
		return false;
	}

	@Override
	public boolean onChildClick(ExpandableListView list, View parent, int groupPosition,
			int childPosition, long id)
	{
		if (id != ((PTNavDrawerAdapter)list.getExpandableListAdapter()).getSelectedItem()) {
			((PTNavDrawerAdapter)list.getExpandableListAdapter()).setSelectedItem(id);
			list.invalidateViews();
			showView(id);
		}
		mDrawerLayout.closeDrawer(mDrawerList);	
		return false;
	}
}
