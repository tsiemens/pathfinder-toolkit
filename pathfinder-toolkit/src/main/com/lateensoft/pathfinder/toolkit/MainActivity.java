package com.lateensoft.pathfinder.toolkit;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.*;
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
import com.lateensoft.pathfinder.toolkit.views.encounter.EncounterFragment;
import com.lateensoft.pathfinder.toolkit.views.party.PartyManagerFragment;
import com.lateensoft.pathfinder.toolkit.views.settings.SettingsActivity;
import org.jetbrains.annotations.NotNull;
import roboguice.activity.RoboFragmentActivity;

public class MainActivity extends RoboFragmentActivity implements OnChildClickListener, OnGroupClickListener,
        OnGroupExpandListener, OnGroupCollapseListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String KEY_CURRENT_FRAGMENT = "fragment_class_name";

    private DrawerLayout m_drawerLayout;
    private RelativeLayout m_leftDrawer;
	private ExpandableListView m_drawerList;
	
	private BasePageFragment m_currentFragment;
    private NavDrawerItem m_toOpen;

    private Deque<Class<? extends BasePageFragment>> m_fragmentBackStack;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_fragmentBackStack = new ArrayDeque<Class<? extends BasePageFragment>>();

        setContentView(R.layout.activity_drawer_main);
        
        setupNavDrawer();

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        
        // Patching
        UpdatePatcher patcher = new UpdatePatcher(this);
        if (patcher.isPatchRequired()) {

            final ProgressDialog progDialog = new ProgressDialog(this);
            progDialog.setCancelable(false);
            progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDialog.setTitle(getString(R.string.updating_dialog_message));
            progDialog.show();

            PatcherTask task = new PatcherTask(patcher);
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
        outState.putString(KEY_CURRENT_FRAGMENT, m_currentFragment.getSupportClass().getCanonicalName());
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
                    showFragmentForNavItem(m_toOpen);
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
        RateDialogHelper rateHelper = new RateDialogHelper(this);
        if (rateHelper.shouldPromptToRate()) {
            rateHelper.buildAndShowDialog();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = isDrawerOpen();
        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).setVisible(!drawerOpen);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    public boolean isDrawerOpen() {
        return m_drawerLayout.isDrawerOpen(m_leftDrawer);
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
        if (m_fragmentBackStack.size() > 0) {
            showFragment(m_fragmentBackStack.pop(), false);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.confirm_exit_dialog_title)
                    .setMessage(R.string.confirm_exit_dialog_message)
                    .setPositiveButton(R.string.ok_button_text, new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton(R.string.cancel_button_text, null)
                    .show();
        }
    }

    private void showFragmentForNavItem(NavDrawerItem item) {
        showFragmentForNavItem(item, true);
    }

    private void showFragmentForNavItem(NavDrawerItem item, boolean pushCurrentToStack) {
        NavDrawerAdapter adapter = (NavDrawerAdapter) m_drawerList.getExpandableListAdapter();
        if (adapter != null && item != null) {
            Class<? extends BasePageFragment> fragmentClass = item.getFragmentClass();
            if (fragmentClass == null) return;
            try {
                adapter.setSelectedItem(item);
                showFragment(fragmentClass.newInstance(), pushCurrentToStack);
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
        showFragment(fragmentClass, true);
    }

    public void showFragment(@NotNull Class<? extends BasePageFragment> fragmentClass, boolean pushCurrentToStack) {
        NavDrawerAdapter adapter = ((NavDrawerAdapter) m_drawerList.getExpandableListAdapter());
        if (adapter == null) return;
        NavDrawerItem item = adapter.getItemForFragment(fragmentClass);
        if (item != null) {
            showFragmentForNavItem(item, pushCurrentToStack);
        } else {
            Log.e(TAG, "Cannot show " + fragmentClass);
        }
    }

    private void showFragment(BasePageFragment fragment, boolean pushCurrentToStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.commit();
        if (m_currentFragment != null && pushCurrentToStack) {
            m_fragmentBackStack.push(m_currentFragment.getSupportClass());
        }
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
                m_toOpen = group;
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
            m_toOpen = (NavDrawerChildItem) adapter.getChild(groupPosition, childPosition);
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
                new NavDrawerGroupItem(getString(R.string.main_menu_encounters), R.drawable.initiative_icon, EncounterFragment.class),
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
}
