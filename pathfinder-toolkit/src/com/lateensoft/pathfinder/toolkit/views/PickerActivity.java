package com.lateensoft.pathfinder.toolkit.views;

import android.app.*;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.model.IdStringPair;

import java.util.List;

/**
 * @author tsiemens
 */
public class PickerActivity extends FragmentActivity {
    private static final String TAG = PickerActivity.class.getSimpleName();

    private static final String PICKER_LISTS_KEY = "picker_lists";

    private ListTypeAdapter m_adapter;

    private ViewPager m_viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker);

        // TODO remove this
        List<PickerList> lists = Lists.newArrayList(new PickerList("Characters",
                Lists.newArrayList(new IdStringPair(1, "Char 1"), new IdStringPair(2, "Char 2")))
                ,new PickerList("Parties",
                        Lists.newArrayList(new IdStringPair(1, "Party 1"), new IdStringPair(2, "Party 2")))
        );

        m_adapter = new ListTypeAdapter(getFragmentManager(), lists);

        m_viewPager = (ViewPager)findViewById(R.id.view_pager);
        m_viewPager.setAdapter(m_adapter);

        ActionBar actionBar = getActionBar();

        if (lists.size() > 1) {
            // Specify that tabs should be displayed in the action bar.
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

            // Create a tab listener that is called when the user changes tabs.
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

    private static class ListTypeAdapter extends FragmentPagerAdapter {
        private List<PickerList> pickerLists;

        public ListTypeAdapter(FragmentManager fm, List<PickerList> pickerLists) {
            super(fm);
            this.pickerLists = pickerLists;
        }

        @Override
        public int getCount() {
            return pickerLists.size();
        }

        @Override
        public Fragment getItem(int position) {
            return ArrayListFragment.newInstance(pickerLists.get(position));
        }
    }

    private static final String PICKER_LIST_KEY = "list";

    private static class ArrayListFragment extends ListFragment {
        private PickerList pickerList;

        static ArrayListFragment newInstance(PickerList pickerList) {
            ArrayListFragment f = new ArrayListFragment();
            Bundle args = new Bundle();
            args.putParcelable(PICKER_LIST_KEY, pickerList);
            f.setArguments(args);

            return f;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            pickerList = getArguments() != null ?
                    (PickerList) getArguments().getParcelable(PICKER_LIST_KEY) : null;
            if (pickerList == null) {
                throw new IllegalArgumentException(PICKER_LIST_KEY + " arg was null");
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
            // TODO this adapter needs to be custom
            setListAdapter(new ArrayAdapter<IdStringPair>(getActivity(),
                    android.R.layout.simple_list_item_1, pickerList.getItems()));
        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            Log.i("FragmentList", "Item clicked: " + id);
        }
    }

    public static class PickerList implements Parcelable {

        private String m_name;
        private List<IdStringPair> m_items;

        public PickerList(String name, List<IdStringPair> items) {
            Preconditions.checkNotNull(items);
            m_name = name;
            m_items = items;
        }

        public PickerList(Parcel in) {
            m_name = in.readString();
            in.readTypedList(m_items, IdStringPair.CREATOR);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            out.writeString(m_name);
            out.writeTypedList(m_items);
        }

        public String getName() {
            return m_name;
        }

        public void setName(String name) {
            m_name = name;
        }

        public List<IdStringPair> getItems() {
            return m_items;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Parcelable.Creator<PickerList> CREATOR = new Parcelable.Creator<PickerList>() {
            public PickerList createFromParcel(Parcel in) {
                return new PickerList(in);
            }

            public PickerList[] newArray(int size) {
                return new PickerList[size];
            }
        };
    }
}
