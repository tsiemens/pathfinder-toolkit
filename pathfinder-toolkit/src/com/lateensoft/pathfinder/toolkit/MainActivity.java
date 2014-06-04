package com.lateensoft.pathfinder.toolkit;

import java.util.Calendar;
import java.util.List;

import android.app.*;
import android.content.ActivityNotFoundException;
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
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.adapters.nav.NavDrawerAdapter;
import com.lateensoft.pathfinder.toolkit.adapters.nav.NavDrawerChildItem;
import com.lateensoft.pathfinder.toolkit.adapters.nav.NavDrawerGroupItem;
import com.lateensoft.pathfinder.toolkit.adapters.nav.NavDrawerItem;
import com.lateensoft.pathfinder.toolkit.patching.UpdatePatcher;
import com.lateensoft.pathfinder.toolkit.patching.UpdatePatcher.PatcherListener;
import com.lateensoft.pathfinder.toolkit.patching.UpdatePatcher.PatcherTask;
import com.lateensoft.pathfinder.toolkit.util.InputMethodUtils;
import com.lateensoft.pathfinder.toolkit.views.*;
import com.lateensoft.pathfinder.toolkit.views.character.*;
import com.lateensoft.pathfinder.toolkit.views.character.CharacterSkillsFragment;
import com.lateensoft.pathfinder.toolkit.views.party.InitiativeTrackerFragment;
import com.lateensoft.pathfinder.toolkit.views.party.PartyManagerFragment;
import com.lateensoft.pathfinder.toolkit.views.party.PartySkillCheckerFragment;
import com.lateensoft.pathfinder.toolkit.views.settings.SettingsActivity;
import org.jetbrains.annotations.NotNull;

public class MainActivity extends Activity implements OnChildClickListener, OnGroupClickListener,
		OnGroupExpandListener, OnGroupCollapseListener {
	private static final String TAG = MainActivity.class.getSimpleName();

	private static final String KEY_CURRENT_FRAGMENT = "fragment_class_name";

	private DrawerLayout m_drawerLayout;
    private RelativeLayout m_leftDrawer;
	private ExpandableListView m_drawerList;
	
	private BasePageFragment m_currentFragment;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

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
					MainActivity.this.showStartupFragment(savedInstanceState);
				}
			};
			task.execute(patchListener);

		} else {
			showStartupFragment(savedInstanceState);
		}
		
		showRateDialogIfRequired();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString(KEY_CURRENT_FRAGMENT, m_currentFragment.getClass().getCanonicalName());
		super.onSaveInstanceState(outState);
	}
	
	private void showStartupFragment(Bundle savedInstanceState) {
        String fragmentName = savedInstanceState != null ?
                savedInstanceState.getString(KEY_CURRENT_FRAGMENT) : null;
        Class<? extends BasePageFragment> fragmentClass = null;
        if (fragmentName != null) {
            fragmentClass = toPageFragmentClass(fragmentName);
        }
        if (fragmentClass == null) {
            fragmentClass = CharacterFluffFragment.class;
        }
        showFragment(fragmentClass);
	}

    @SuppressWarnings("unchecked")
    private Class<? extends BasePageFragment> toPageFragmentClass(final String className){
        try{
            return (Class<? extends BasePageFragment>) Class.forName(className);
        } catch(Exception e){
            throw new IllegalStateException(e);
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
                InputMethodUtils.hideSoftKeyboard(MainActivity.this);
			}
		};
		drawerToggle.syncState();

		m_drawerLayout.setDrawerListener(drawerToggle);

		m_drawerList = (ExpandableListView) findViewById(R.id.drawer_exp_lv);

		// Set the adapter for the list view
		m_drawerList.setAdapter(new NavDrawerAdapter(this, buildNavDrawerList()));
		m_drawerList.setGroupIndicator(getResources().getDrawable(R.drawable.nav_item_expand_icon));
		// Set the list's click listener
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
		AppPreferences sharedPrefs = AppPreferences.getInstance();
		if (sharedPrefs.isLastRateTimeLongEnough()
				&& !sharedPrefs.hasRatedCurrentVersion()) {
			showRateAppPromptDialog();
		}
	}

	public void showRateAppPromptDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
        OnClickListener listener = new RateDialogClickListener();
		builder.setTitle(getString(R.string.rate_dialog_title))
				.setMessage(R.string.rate_dialog_message)
				.setPositiveButton(R.string.rate_dialog_positive_button, listener)
				.setNegativeButton(R.string.rate_dialog_negative_button, listener);

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

    // TODO
//	public void showView(long id) {
//		FragmentManager fragmentManager = getFragmentManager();
//		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//		BasePageFragment newFragment = null;
//
//		if ( id == NavDrawerAdapter.FLUFF_ID ) {
//			newFragment = new CharacterFluffFragment();
//
//		} else if ( id == NavDrawerAdapter.COMBAT_STATS_ID ) {
//			newFragment = new CharacterCombatStatsFragment();
//
//		} else if ( id == NavDrawerAdapter.ABILITIES_ID ) {
//			newFragment = new CharacterAbilitiesFragment();
//
//		} else if ( id == NavDrawerAdapter.SKILLS_ID ) {
//			newFragment = new CharacterSkillsFragment();
//
//		} else if ( id == NavDrawerAdapter.INVENTORY_ID ) {
//			newFragment = new CharacterInventoryFragment();
//
//		} else if ( id == NavDrawerAdapter.ARMOR_ID ) {
//			newFragment = new CharacterArmorFragment();
//
//		} else if ( id == NavDrawerAdapter.WEAPONS_ID ) {
//			newFragment = new CharacterWeaponsFragment();
//
//		} else if ( id == NavDrawerAdapter.FEATS_ID ) {
//			newFragment = new CharacterFeatsFragment();
//
//		} else if ( id == NavDrawerAdapter.SPELLS_ID ) {
//			newFragment = new CharacterSpellBookFragment();
//
//		} else if ( id == NavDrawerAdapter.INITIATIVE_TRACKER_ID ) {
//			newFragment = new InitiativeTrackerFragment();
//
//		} else if ( id == NavDrawerAdapter.SKILL_CHECKER_ID ) {
//			newFragment = new PartySkillCheckerFragment();
//
//		} else if ( id == NavDrawerAdapter.PARTY_MANAGER_ID ) {
//			newFragment = new PartyManagerFragment();
//
//		} else if ( id == NavDrawerAdapter.POINTBUY_ID ) {
//			newFragment = new PointbuyCalculatorFragment();
//
//		} else if (id == NavDrawerAdapter.DICE_ROLLER_ID) {
//			newFragment = new DiceRollerFragment();
//		}
//
//
//		if (newFragment != null) {
//			((NavDrawerAdapter)m_drawerList.getExpandableListAdapter()).setSelectedItemId(id);
//			m_drawerList.invalidateViews();
//			if (newFragment instanceof AbstractCharacterSheetFragment) {
//				m_drawerList.expandGroup(0);
//			}
//
//
//			fragmentTransaction.replace(R.id.content_frame, newFragment);
//			fragmentTransaction.commit();
//
//			m_currentFragment = newFragment;
//			m_currentFragmentId = id;
//		}
//	}

    private void showFragmentForNavItem(NavDrawerItem item) {
        NavDrawerAdapter adapter = (NavDrawerAdapter) m_drawerList.getExpandableListAdapter();
        if (adapter != null && item != null) {
            Class<? extends BasePageFragment> fragmentClass = item.getFragmentClass();
            if (fragmentClass == null) return;
            try {
                adapter.setSelectedItem(item);
                showFragment(fragmentClass.newInstance());
                m_drawerList.invalidateViews();
                if (item instanceof NavDrawerChildItem) {
                    int groupIndex = adapter.getGroupIndexForItem(item);
                    if (groupIndex >= 0) {
                        m_drawerList.expandGroup(groupIndex);
                    }
                }
            } catch (InstantiationException e) {
                Log.e(TAG, null, e);
            } catch (IllegalAccessException e) {
                Log.e(TAG, null, e);
            }
        }
    }

    public void showFragment(@NotNull Class<? extends BasePageFragment> fragmentClass) {
        NavDrawerAdapter adapter = ((NavDrawerAdapter) m_drawerList.getExpandableListAdapter());
        if (adapter == null) return;
        NavDrawerItem item = adapter.getItemForFragment(fragmentClass);
        if (item != null) {
            showFragmentForNavItem(item);
        } else {
            Log.e(TAG, "Cannot show " + fragmentClass);
        }
    }

    private void showFragment(BasePageFragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.commit();
        m_currentFragment = fragment;
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
	public boolean onGroupClick(ExpandableListView list, View parent, int groupPosition, long id){
        NavDrawerAdapter adapter = (NavDrawerAdapter) list.getExpandableListAdapter();
        if (adapter != null) {
            NavDrawerGroupItem group = (NavDrawerGroupItem) adapter.getGroup(groupPosition);
            if (group != null && group.getChildren() == null && group != adapter.getSelectedItem()) {
                // Group is selectable, and was not selected
                showFragmentForNavItem(group);
                setDrawerOpen(false);
            }
        }
        return false;
	}

	@Override
	public boolean onChildClick(ExpandableListView list, View parent, int groupPosition,
			int childPosition, long id) {
        NavDrawerAdapter adapter = (NavDrawerAdapter) list.getExpandableListAdapter();
        if (adapter != null) {
            NavDrawerChildItem child = (NavDrawerChildItem) adapter.getChild(groupPosition, childPosition);
            if (child != adapter.getSelectedItem()) {
                showFragmentForNavItem(child);
            }
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

    private List<NavDrawerGroupItem> buildNavDrawerList() {
        List<NavDrawerChildItem> characterPages = Lists.newArrayList(
                createCharacterSheetItem(R.string.tab_character_fluff, CharacterFluffFragment.class),
                createCharacterSheetItem(R.string.tab_character_combat_stats, CharacterCombatStatsFragment.class),
                createCharacterSheetItem(R.string.tab_character_abilities, CharacterAbilitiesFragment.class),
                createCharacterSheetItem(R.string.tab_character_skills, CharacterSkillsFragment.class),
                createCharacterSheetItem(R.string.tab_character_inventory, CharacterInventoryFragment.class),
                createCharacterSheetItem(R.string.tab_character_armor, CharacterArmorFragment.class),
                createCharacterSheetItem(R.string.tab_character_weapons, CharacterWeaponsFragment.class),
                createCharacterSheetItem(R.string.tab_character_feats, CharacterFeatsFragment.class),
                createCharacterSheetItem(R.string.tab_character_spells, CharacterSpellBookFragment.class),
                createCharacterSheetItem(R.string.tab_character_membership, CharacterPartyMembershipFragment.class)
        );

        return Lists.newArrayList(
                new NavDrawerGroupItem(getString(R.string.main_menu_character_group), R.drawable.character_sheet_icon, characterPages),
                new NavDrawerGroupItem(getString(R.string.main_menu_init_tracker), R.drawable.initiative_icon, InitiativeTrackerFragment.class),
                new NavDrawerGroupItem(getString(R.string.main_menu_party_skill_checker), R.drawable.skill_checker_icon, PartySkillCheckerFragment.class),
                new NavDrawerGroupItem(getString(R.string.main_menu_party_manager), R.drawable.party_icon, PartyManagerFragment.class),
                new NavDrawerGroupItem(getString(R.string.main_menu_pointbuy), R.drawable.stat_calc_icon, PointbuyCalculatorFragment.class),
                new NavDrawerGroupItem(getString(R.string.main_menu_dice_roller), R.drawable.dice_roller_icon, DiceRollerFragment.class)
        );
    }

    private NavDrawerChildItem createCharacterSheetItem(int textId, Class<? extends AbstractCharacterSheetFragment> fragment) {
        NavDrawerChildItem item = new NavDrawerChildItem(getString(textId), R.drawable.character_sheet_icon, fragment);
        item.setIconVisibility(View.INVISIBLE);
        return item;
    }
	
	public void hideKeyboardDelayed(long delayMs) {
		new Handler().postDelayed(new Runnable() {
		    @Override
		    public void run() {
		    	InputMethodUtils.hideSoftKeyboard(MainActivity.this);
		    }
		 }, delayMs);
	}

    private class RateDialogClickListener implements OnClickListener {
        @Override
        public void onClick(DialogInterface dialogInterface, int selection) {
            AppPreferences sharedPrefs = AppPreferences.getInstance();

            switch (selection) {
                case DialogInterface.BUTTON_POSITIVE:
                    sharedPrefs.updateLastRatedVersion();
                    sharedPrefs.putLong(AppPreferences.KEY_LONG_LAST_RATE_PROMPT_TIME,
                            Calendar.getInstance().getTimeInMillis());
                    Log.v(TAG, "Attempting to open market page");
                    String appPackageName = MainActivity.this.getPackageName();
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
                    sharedPrefs.putLong(AppPreferences.KEY_LONG_LAST_RATE_PROMPT_TIME,
                            Calendar.getInstance().getTimeInMillis());
            }

        }
    }
}
