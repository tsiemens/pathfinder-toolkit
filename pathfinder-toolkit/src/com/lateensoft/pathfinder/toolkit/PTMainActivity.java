package com.lateensoft.pathfinder.toolkit;

import java.util.Calendar;

import android.app.*;
import android.app.FragmentManager.OnBackStackChangedListener;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

import com.lateensoft.pathfinder.toolkit.adapters.PTNavDrawerAdapter;
import com.lateensoft.pathfinder.toolkit.patching.PTUpdatePatcher;
import com.lateensoft.pathfinder.toolkit.patching.PTUpdatePatcher.PatcherListener;
import com.lateensoft.pathfinder.toolkit.patching.PTUpdatePatcher.PatcherTask;
import com.lateensoft.pathfinder.toolkit.views.PTBasePageFragment;
import com.lateensoft.pathfinder.toolkit.views.PTDiceRollerFragment;
import com.lateensoft.pathfinder.toolkit.views.PTPointbuyCalculatorFragment;
import com.lateensoft.pathfinder.toolkit.views.character.PTCharacterAbilityFragment;
import com.lateensoft.pathfinder.toolkit.views.character.PTCharacterArmorFragment;
import com.lateensoft.pathfinder.toolkit.views.character.PTCharacterCombatStatsFragment;
import com.lateensoft.pathfinder.toolkit.views.character.PTCharacterFeatsFragment;
import com.lateensoft.pathfinder.toolkit.views.character.PTCharacterFluffFragment;
import com.lateensoft.pathfinder.toolkit.views.character.PTCharacterInventoryFragment;
import com.lateensoft.pathfinder.toolkit.views.character.PTCharacterSheetFragment;
import com.lateensoft.pathfinder.toolkit.views.character.PTCharacterSkillsFragment;
import com.lateensoft.pathfinder.toolkit.views.character.PTCharacterSpellBookFragment;
import com.lateensoft.pathfinder.toolkit.views.character.PTCharacterWeaponsFragment;
import com.lateensoft.pathfinder.toolkit.views.party.PTInitiativeTrackerFragment;
import com.lateensoft.pathfinder.toolkit.views.party.PTPartyManagerFragment;
import com.lateensoft.pathfinder.toolkit.views.party.PTPartySkillCheckerFragment;

public class PTMainActivity extends Activity implements
		OnClickListener, OnChildClickListener, OnGroupClickListener,
		OnGroupExpandListener, OnGroupCollapseListener {
	private static final String TAG = PTMainActivity.class.getSimpleName();

	private static final String KEY_CURRENT_FRAGMENT = "fragment_id";
	
	private DrawerLayout m_drawerLayout;
	private ActionBarDrawerToggle m_drawerToggle;
	private ExpandableListView m_drawerList;
	
	private PTBasePageFragment m_currentFragment;
	private long m_currentFragmentId = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Enable auto-rotation of screen based on device orientation
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

		if (savedInstanceState != null) {
			m_currentFragmentId = savedInstanceState.getLong(KEY_CURRENT_FRAGMENT);
		}

		setContentView(R.layout.activity_drawer_main);
		
		setupNavDrawer();

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		
		// Patching
		if (PTUpdatePatcher.isPatchRequired()) {

			final ProgressDialog progDialog = new ProgressDialog(this);
			progDialog.setCancelable(false);
			progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progDialog.setTitle(getString(R.string.updating_dialog_message));
			progDialog.show();

			PatcherTask task = new PatcherTask();
			PatcherListener patchListener = new PatcherListener() {
				@Override public void onPatchComplete(boolean completeSuccess) {
					progDialog.dismiss();
					PTMainActivity.this.showStartupFragment();
				}
			};
			task.execute(patchListener);

		} else {
			showStartupFragment();
		}
		
		showRateDialogIfRequired();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putLong(KEY_CURRENT_FRAGMENT, m_currentFragmentId);
		super.onSaveInstanceState(outState);
	}
	
	private void showStartupFragment() {
		if (m_currentFragmentId != 0) {
			showView(m_currentFragmentId);
		} else {
			showView(PTNavDrawerAdapter.FLUFF_ID);
		}
	}
	
	private void setupNavDrawer() {
		m_drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		m_drawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		m_drawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
				m_drawerLayout, /* DrawerLayout object */
				R.drawable.ic_drawer, /* nav drawer icon to replace 'Up' caret */
				R.string.app_name, /* "open drawer" description */
				R.string.app_name /* "close drawer" description */
				) {

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
                if (m_currentFragment != null) {
                    m_currentFragment.updateTitle();
                }
				invalidateOptionsMenu();
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
                ActionBar ab = getActionBar();
                if (ab != null) {
                    ab.setTitle(R.string.app_name);
                    ab.setSubtitle(null);
                }
				invalidateOptionsMenu();
				hideKeyboard();
			}
		};
		m_drawerToggle.syncState();

		// Set the drawer toggle as the DrawerListener
		m_drawerLayout.setDrawerListener(m_drawerToggle);

		m_drawerList = (ExpandableListView) findViewById(R.id.left_drawer);

		// Set the adapter for the list view
		m_drawerList.setAdapter(new PTNavDrawerAdapter(this));
		m_drawerList.setGroupIndicator(getResources().getDrawable(R.drawable.nav_item_expand_icon));
		// Set the list's click listener
		// mDrawerList.setOnItemClickListener(this);
		m_drawerList.setOnChildClickListener(this);
		m_drawerList.setOnGroupClickListener(this);
		m_drawerList.setOnGroupExpandListener(this);
		m_drawerList.setOnGroupCollapseListener(this);
	}

	private void showRateDialogIfRequired() {
		PTSharedPreferences sharedPrefs = PTSharedPreferences.getInstance();
		if (sharedPrefs.isLastRateTimeLongEnough()
				&& !sharedPrefs.hasRatedCurrentVersion()) {
			showRateAppPromptDialog();
		}
	}

	public void showRateAppPromptDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.rate_dialog_title))
				.setMessage(getString(R.string.rate_dialog_message))
				.setPositiveButton(
						getString(R.string.rate_dialog_positive_button), this)
				.setNegativeButton(
						getString(R.string.rate_dialog_negative_button), this);

		AlertDialog alert = builder.create();
		alert.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Add menu items which are visible for all fragments
		PTSharedMenu.onCreateOptionsMenu(menu, getMenuInflater());
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		boolean drawerOpen = m_drawerLayout.isDrawerOpen(m_drawerList);
		for (int i = 0; i < menu.size(); i++) {
			menu.getItem(i).setVisible(!drawerOpen
                    || menu.getItem(i).getItemId() == R.id.mi_app_settings);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			if (m_drawerLayout.isDrawerOpen(m_drawerList)) {
				m_drawerLayout.closeDrawer(m_drawerList);
			} else {
				m_drawerLayout.openDrawer(m_drawerList);
			}
		}

		PTSharedMenu.onOptionsItemSelected(item, this);
		return super.onOptionsItemSelected(item);
	}

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.confirm_exit_dialog_title)
        .setMessage(R.string.confirm_exit_dialog_message)
        .setPositiveButton(R.string.ok_button_text, new OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        })
        .setNegativeButton(R.string.cancel_button_text, null)
        .show();
    }

    // For dialog
    @Override
	public void onClick(DialogInterface dialogInterface, int selection) {
		PTSharedPreferences sharedPrefs = PTSharedPreferences.getInstance();
		
		switch (selection) {
		case DialogInterface.BUTTON_POSITIVE:
			sharedPrefs.updateLastRatedVersion();
			sharedPrefs.putLong(PTSharedPreferences.KEY_LONG_LAST_RATE_PROMPT_TIME,
					Calendar.getInstance().getTimeInMillis());
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
			sharedPrefs.putLong(PTSharedPreferences.KEY_LONG_LAST_RATE_PROMPT_TIME,
					Calendar.getInstance().getTimeInMillis());
		}

	}
	
	public void showView(long id) {
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		PTBasePageFragment newFragment = null;
		
		if ( id == PTNavDrawerAdapter.FLUFF_ID ) {
			newFragment = new PTCharacterFluffFragment();
			
		} else if ( id == PTNavDrawerAdapter.COMBAT_STATS_ID ) {
			newFragment = new PTCharacterCombatStatsFragment();
			
		} else if ( id == PTNavDrawerAdapter.ABILITIES_ID ) {
			newFragment = new PTCharacterAbilityFragment();
			
		} else if ( id == PTNavDrawerAdapter.SKILLS_ID ) {
			newFragment = new PTCharacterSkillsFragment();
			
		} else if ( id == PTNavDrawerAdapter.INVENTORY_ID ) {
			newFragment = new PTCharacterInventoryFragment();
			
		} else if ( id == PTNavDrawerAdapter.ARMOR_ID ) {
			newFragment = new PTCharacterArmorFragment();
			
		} else if ( id == PTNavDrawerAdapter.WEAPONS_ID ) {
			newFragment = new PTCharacterWeaponsFragment();
			
		} else if ( id == PTNavDrawerAdapter.FEATS_ID ) {
			newFragment = new PTCharacterFeatsFragment();
			
		} else if ( id == PTNavDrawerAdapter.SPELLS_ID ) {
			newFragment = new PTCharacterSpellBookFragment();
			
		} else if ( id == PTNavDrawerAdapter.INITIATIVE_TRACKER_ID ) {
			newFragment = new PTInitiativeTrackerFragment();
			
		} else if ( id == PTNavDrawerAdapter.SKILL_CHECKER_ID ) {
			newFragment = new PTPartySkillCheckerFragment();
			
		} else if ( id == PTNavDrawerAdapter.PARTY_MANAGER_ID ) {
			newFragment = new PTPartyManagerFragment();
			
		} else if ( id == PTNavDrawerAdapter.POINTBUY_ID ) {
			newFragment = new PTPointbuyCalculatorFragment();
		
		} else if (id == PTNavDrawerAdapter.DICE_ROLLER_ID) {
			newFragment = new PTDiceRollerFragment();		
		} 
		
		if (newFragment != null) {
			((PTNavDrawerAdapter)m_drawerList.getExpandableListAdapter()).setSelectedItem(id);
			m_drawerList.invalidateViews();
			if (newFragment instanceof PTCharacterSheetFragment) {
				m_drawerList.expandGroup(0);
			}
			
			fragmentTransaction.replace(R.id.content_frame, newFragment);
			fragmentTransaction.commit();

			m_currentFragment = newFragment;
			m_currentFragmentId = id;
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
			m_drawerLayout.closeDrawer(m_drawerList);
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
		m_drawerLayout.closeDrawer(m_drawerList);	
		return false;
	}
	
	public void hideKeyboardDelayed(long delayMs) {
		new Handler().postDelayed(new Runnable() {
		    @Override
		    public void run() {
		    	hideKeyboard();
		    }
		 }, delayMs);
	}
	
	public void hideKeyboard() {
		InputMethodManager iMM = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		View focus = getCurrentFocus();
		if (focus != null) {
			iMM.hideSoftInputFromWindow(focus.getWindowToken(), 0);
		}
	}
}
