package com.lateensoft.pathfinder.toolkit;

import java.util.Calendar;

import android.app.*;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
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

import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.lateensoft.pathfinder.toolkit.adapters.NavDrawerAdapter;
import com.lateensoft.pathfinder.toolkit.patching.UpdatePatcher;
import com.lateensoft.pathfinder.toolkit.patching.UpdatePatcher.PatcherListener;
import com.lateensoft.pathfinder.toolkit.patching.UpdatePatcher.PatcherTask;
import com.lateensoft.pathfinder.toolkit.views.BasePageFragment;
import com.lateensoft.pathfinder.toolkit.views.DiceRollerFragment;
import com.lateensoft.pathfinder.toolkit.views.PointbuyCalculatorFragment;
import com.lateensoft.pathfinder.toolkit.views.character.*;
import com.lateensoft.pathfinder.toolkit.views.character.CharacterSkillsFragment;
import com.lateensoft.pathfinder.toolkit.views.party.InitiativeTrackerFragment;
import com.lateensoft.pathfinder.toolkit.views.party.PartyManagerFragment;
import com.lateensoft.pathfinder.toolkit.views.party.PartySkillCheckerFragment;
import com.lateensoft.pathfinder.toolkit.views.settings.SettingsActivity;

public class MainActivity extends Activity implements
		OnClickListener, OnChildClickListener, OnGroupClickListener,
		OnGroupExpandListener, OnGroupCollapseListener {
	private static final String TAG = MainActivity.class.getSimpleName();

	private static final String KEY_CURRENT_FRAGMENT = "fragment_id";
	
	private DrawerLayout m_drawerLayout;
    private RelativeLayout m_leftDrawer;
	private ExpandableListView m_drawerList;
	
	private BasePageFragment m_currentFragment;
	private long m_currentFragmentId = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (savedInstanceState != null) {
			m_currentFragmentId = savedInstanceState.getLong(KEY_CURRENT_FRAGMENT);
		}

		setContentView(R.layout.activity_drawer_main);
		
		setupNavDrawer();

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		
		// Patching
		if (UpdatePatcher.isPatchRequired()) {

			final ProgressDialog progDialog = new ProgressDialog(this);
			progDialog.setCancelable(false);
			progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progDialog.setTitle(getString(R.string.updating_dialog_message));
			progDialog.show();

			PatcherTask task = new PatcherTask();
			PatcherListener patchListener = new PatcherListener() {
				@Override public void onPatchComplete(boolean completeSuccess) {
					progDialog.dismiss();
					MainActivity.this.showStartupFragment();
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
			showView(NavDrawerAdapter.FLUFF_ID);
		}
	}
	
	private void setupNavDrawer() {
		m_drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		m_drawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
        m_leftDrawer = (RelativeLayout) findViewById(R.id.left_drawer);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
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
		drawerToggle.syncState();

		// Set the drawer toggle as the DrawerListener
		m_drawerLayout.setDrawerListener(drawerToggle);

		m_drawerList = (ExpandableListView) findViewById(R.id.drawer_exp_lv);

		// Set the adapter for the list view
		m_drawerList.setAdapter(new NavDrawerAdapter(this));
		m_drawerList.setGroupIndicator(getResources().getDrawable(R.drawable.nav_item_expand_icon));
		// Set the list's click listener
		// mDrawerList.setOnItemClickListener(this);
		m_drawerList.setOnChildClickListener(this);
		m_drawerList.setOnGroupClickListener(this);
		m_drawerList.setOnGroupExpandListener(this);
		m_drawerList.setOnGroupCollapseListener(this);

        ImageView settingsButton = (ImageView) findViewById(R.id.iv_settings_button);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });
	}

	private void showRateDialogIfRequired() {
		Preferences sharedPrefs = Preferences.getInstance();
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
	public boolean onPrepareOptionsMenu(Menu menu) {
		boolean drawerOpen = isDrawerOpen();
		for (int i = 0; i < menu.size(); i++) {
			menu.getItem(i).setVisible(!drawerOpen);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
            setDrawerOpen(!isDrawerOpen());
		}

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
		Preferences sharedPrefs = Preferences.getInstance();
		
		switch (selection) {
		case DialogInterface.BUTTON_POSITIVE:
			sharedPrefs.updateLastRatedVersion();
			sharedPrefs.putLong(Preferences.KEY_LONG_LAST_RATE_PROMPT_TIME,
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
			sharedPrefs.putLong(Preferences.KEY_LONG_LAST_RATE_PROMPT_TIME,
					Calendar.getInstance().getTimeInMillis());
		}

	}
	
	public void showView(long id) {
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		BasePageFragment newFragment = null;
		
		if ( id == NavDrawerAdapter.FLUFF_ID ) {
			newFragment = new CharacterFluffFragment();
			
		} else if ( id == NavDrawerAdapter.COMBAT_STATS_ID ) {
			newFragment = new CharacterCombatStatsFragment();
			
		} else if ( id == NavDrawerAdapter.ABILITIES_ID ) {
			newFragment = new CharacterAbilitiesFragment();
			
		} else if ( id == NavDrawerAdapter.SKILLS_ID ) {
			newFragment = new CharacterSkillsFragment();
			
		} else if ( id == NavDrawerAdapter.INVENTORY_ID ) {
			newFragment = new CharacterInventoryFragment();
			
		} else if ( id == NavDrawerAdapter.ARMOR_ID ) {
			newFragment = new CharacterArmorFragment();
			
		} else if ( id == NavDrawerAdapter.WEAPONS_ID ) {
			newFragment = new CharacterWeaponsFragment();
			
		} else if ( id == NavDrawerAdapter.FEATS_ID ) {
			newFragment = new CharacterFeatsFragment();
			
		} else if ( id == NavDrawerAdapter.SPELLS_ID ) {
			newFragment = new CharacterSpellBookFragment();
			
		} else if ( id == NavDrawerAdapter.INITIATIVE_TRACKER_ID ) {
			newFragment = new InitiativeTrackerFragment();
			
		} else if ( id == NavDrawerAdapter.SKILL_CHECKER_ID ) {
			newFragment = new PartySkillCheckerFragment();
			
		} else if ( id == NavDrawerAdapter.PARTY_MANAGER_ID ) {
			newFragment = new PartyManagerFragment();
			
		} else if ( id == NavDrawerAdapter.POINTBUY_ID ) {
			newFragment = new PointbuyCalculatorFragment();
		
		} else if (id == NavDrawerAdapter.DICE_ROLLER_ID) {
			newFragment = new DiceRollerFragment();
		} 
		
		if (newFragment != null) {
			((NavDrawerAdapter)m_drawerList.getExpandableListAdapter()).setSelectedItem(id);
			m_drawerList.invalidateViews();
			if (newFragment instanceof AbstractCharacterSheetFragment) {
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
		if (id != NavDrawerAdapter.CHARACTER_SHEET_ID ) {
			if (id != ((NavDrawerAdapter)list.getExpandableListAdapter()).getSelectedItem()) {
				showView(id);		
			}
            setDrawerOpen(false);
		}
		return false;
	}

	@Override
	public boolean onChildClick(ExpandableListView list, View parent, int groupPosition,
			int childPosition, long id)
	{
		if (id != ((NavDrawerAdapter)list.getExpandableListAdapter()).getSelectedItem()) {
			((NavDrawerAdapter)list.getExpandableListAdapter()).setSelectedItem(id);
			list.invalidateViews();
			showView(id);
		}
        setDrawerOpen(false);
		return false;
	}

    private void setDrawerOpen(boolean open) {
        if (open) {
            m_drawerLayout.openDrawer(m_leftDrawer);
        } else {
            m_drawerLayout.closeDrawer(m_leftDrawer);
        }
    }

    private boolean isDrawerOpen() {
        return m_drawerLayout.isDrawerOpen(m_leftDrawer);
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
