package com.lateensoft.pathfinder.toolkit.views.picker;

import android.app.*;
import android.content.Intent;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.model.IdStringPair;
import com.lateensoft.pathfinder.toolkit.util.InputMethodUtils;
import roboguice.activity.RoboFragmentActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tsiemens
 */
public class PickerActivity extends RoboFragmentActivity {
    private static final String TAG = PickerActivity.class.getSimpleName();

    public static final String PICKER_LISTS_KEY = "picker_lists";
    public static final String TITLE_KEY = "title";
    public static final String IS_SINGLE_CHOICE_KEY = "is_single_choice";

    private List<PickerList> m_pickerLists;

    private ListTypeAdapter m_fragmentAdapter;
    private ViewPager m_viewPager;

    private EditText m_searchEditText;

    private boolean m_isSingleChoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker);

        ActionBar actionBar = getActionBar();
        Intent intent = getIntent();
        String title = intent.getStringExtra(TITLE_KEY);
        if (title != null) {
            setTitle(title);
        }

        m_isSingleChoice = intent.getBooleanExtra(IS_SINGLE_CHOICE_KEY, false);

        List<PickerList> lists = intent.getParcelableArrayListExtra(PICKER_LISTS_KEY);
        if (lists == null || actionBar == null) {
            finish();
            return;
        }
        m_pickerLists = lists;
        actionBar.setDisplayHomeAsUpEnabled(true);

        m_fragmentAdapter = new ListTypeAdapter(getFragmentManager());

        m_viewPager = (ViewPager)findViewById(R.id.view_pager);
        m_viewPager.setAdapter(m_fragmentAdapter);

        if (lists.size() > 1) {
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            ActionBar.TabListener tabListener = new ActionBar.TabListener() {
                @Override public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                    m_viewPager.setCurrentItem(tab.getPosition(), true);
                }

                @Override public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {}

                @Override public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {}
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
                    refreshFragment(position);
                }
            });
        }
    }

    private void refreshFragment(int position) {
        ArrayListFragment fragment = m_fragmentAdapter.getFragment(position);
        if (fragment != null) {
            ListAdapter adapter = fragment.getListAdapter();
            if (m_searchEditText != null && adapter != null) {
                Editable editable = m_searchEditText.getText();
                if (editable != null) {
                    String searchText = editable.toString();
                    ((PickerListAdapter) adapter).getFilter().filter(searchText == null ? "" : searchText);
                }
            }
        } else {
            Log.e(TAG, "Error: no fragment found onPageSelected: " + position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.picker_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.mi_search);
        ViewGroup actionView = (ViewGroup) menuItem.getActionView();
        if (actionView!= null) {
            m_searchEditText = (EditText) actionView.findViewById(R.id.et_search);
            m_searchEditText.addTextChangedListener(new TextWatcher() {
                @Override  public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    refreshFragment(m_viewPager.getCurrentItem());
                }

                @Override public void afterTextChanged(Editable s) {}
            });

            menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    m_searchEditText.requestFocusFromTouch();
                    InputMethodUtils.showSoftKeyboard(PickerActivity.this);
                    return true;
                }

                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    m_searchEditText.setText(null);
                    InputMethodUtils.hideSoftKeyboard(PickerActivity.this);
                    return true;
                }
            });
        } else {
            Log.e(TAG, "Error: No action view found for mi_search");
        }
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
        SparseArray<ArrayListFragment> m_fragments =new SparseArray<ArrayListFragment>(m_pickerLists.size());

        public ListTypeAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return m_pickerLists.size();
        }

        @Override
        public Fragment getItem(int position) {
            ArrayListFragment newFragment = newArrayListFragmentInstance(position);
            m_fragments.put(position, newFragment);
            return newFragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            m_fragments.put(position, null);
        }

        public ArrayListFragment getFragment(int position) {
            return m_fragments.get(position);
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
            Log.d(TAG, "Fragment created");
            getListView().setTextFilterEnabled(true);
            setListAdapter(new PickerListAdapter(getActivity(), pickerList, new PickerListAdapter.OnPairSelectionChangedListener() {
                @Override
                public void onSelectionChanged(ArrayAdapter adapter, SelectablePair pair, boolean isSelected) {
                    if (m_isSingleChoice && isSelected) {
                        for (PickerList list : m_pickerLists) {
                            for (SelectablePair p : list) {
                                if (p != pair) {
                                    p.setSelected(false);
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            }));
        }
    }

}
