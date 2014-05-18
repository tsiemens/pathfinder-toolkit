package com.lateensoft.pathfinder.toolkit.views.picker;

import android.app.*;
import android.content.Intent;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.*;
import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.model.IdStringPair;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tsiemens
 */
public class PickerActivity extends FragmentActivity {
    private static final String TAG = PickerActivity.class.getSimpleName();

    public static final String PICKER_LISTS_KEY = "picker_lists";
    public static final String TITLE_KEY = "title";
    public static final String IS_SINGLE_CHOICE_KEY = "is_single_choice";

    private List<PickerList> m_pickerLists;

    private ViewPager m_viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker);

        ActionBar actionBar = getActionBar();
        List<PickerList> lists = getIntent().getParcelableArrayListExtra(PICKER_LISTS_KEY);

        if (lists == null || actionBar == null) {
            finish();
            return;
        }
        actionBar.setDisplayHomeAsUpEnabled(true);
        m_pickerLists = lists;
        ListTypeAdapter adapter = new ListTypeAdapter(getFragmentManager());

        m_viewPager = (ViewPager)findViewById(R.id.view_pager);
        m_viewPager.setAdapter(adapter);

        if (lists.size() > 1) {
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            ActionBar.TabListener tabListener = new ActionBar.TabListener() {
                public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                    m_viewPager.setCurrentItem(tab.getPosition(), true);
                }

                public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
                    // hide the given tab
                }

                public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
                    // probably ignore this event
                }
            };

            for (PickerList list : lists) {
                actionBar.addTab(
                        actionBar.newTab()
                                .setText(list.getName())
                                .setTabListener(tabListener)
                );
            }

            m_viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    getActionBar().setSelectedNavigationItem(position);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.base_editor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mi_done) {
            Intent resultData = new Intent();
            for (PickerList pickerList : m_pickerLists) {
                resultData.putParcelableArrayListExtra(pickerList.getKey(), getCopyOfSelectedItems(pickerList));
            }
            setResult(RESULT_OK, resultData);
            finish();
        } else if (item.getItemId() == R.id.mi_cancel ||
                item.getItemId() == android.R.id.home) {
            setResult(RESULT_CANCELED);
            finish();
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private ArrayList<IdStringPair> getCopyOfSelectedItems(PickerList pickerList) {
        ArrayList<IdStringPair> selectedItems = Lists.newArrayList();
        for (SelectablePair pair : pickerList) {
            if (pair.isSelected()) {
                selectedItems.add(new IdStringPair(pair));
            }
        }
        return selectedItems;
    }

    private class ListTypeAdapter extends FragmentPagerAdapter {

        public ListTypeAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return m_pickerLists.size();
        }

        @Override
        public Fragment getItem(int position) {
            return newArrayListFragmentInstance(position);
        }
    }

    private static final String PICKER_LIST_INDEX_KEY = "list_index";

    private ArrayListFragment newArrayListFragmentInstance(int pickerListIndex) {
        ArrayListFragment f = new ArrayListFragment();
        Bundle args = new Bundle();
        args.putInt(PICKER_LIST_INDEX_KEY, pickerListIndex);
        f.setArguments(args);
        return f;
    }

    private class ArrayListFragment extends ListFragment {
        private PickerList pickerList;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                pickerList = m_pickerLists.get(getArguments().getInt(PICKER_LIST_INDEX_KEY));
            } else {
                throw new IllegalArgumentException(PICKER_LIST_INDEX_KEY + " arg was invalid");
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_list, container, false);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            setListAdapter(new PickerListAdapter(getActivity(), pickerList));
        }
    }

}
