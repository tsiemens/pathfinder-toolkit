package com.lateensoft.pathfinder.toolkit.adapters.nav;

import com.lateensoft.pathfinder.toolkit.R;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.lateensoft.pathfinder.toolkit.views.BasePageFragment;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NavDrawerAdapter implements ExpandableListAdapter {

    private Context m_context;
    List<NavDrawerGroupItem> m_items;
    
    private NavDrawerItem m_selectedItem = null;
    
    public NavDrawerAdapter(Context context, List<NavDrawerGroupItem> navDrawerItems) {
        m_context = context;
        m_items = navDrawerItems;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        View row = convertView;
        ItemHolder holder;

        if(row == null) {
            LayoutInflater inflater = LayoutInflater.from(m_context);
            row = inflater.inflate(R.layout.nav_drawer_item, parent, false);
            holder = new ItemHolder();

            holder.name = (TextView)row.findViewById(R.id.tv_item_label);
            holder.icon = (ImageView)row.findViewById(R.id.iv_item_icon);
            holder.groupState = (ImageView)row.findViewById(R.id.iv_group_state);

            row.setTag(holder);
        } else {
            holder = (ItemHolder)row.getTag();
        }

        NavDrawerItem child = (NavDrawerItem) getChild(groupPosition, childPosition);

        if (child != null) {
            holder.name.setText(child.getText());
            holder.icon.setImageResource(child.getIconResId());
            holder.icon.setVisibility(child.getIconVisibility());
        }
        holder.groupState.setImageResource(R.drawable.expander_open_holo_dark);
        holder.groupState.setVisibility(View.INVISIBLE);

        if (child != null && child == m_selectedItem) {
            row.setBackgroundColor(m_context.getResources().getColor(android.R.color.holo_blue_light));
        } else {
            row.setBackgroundColor(m_context.getResources().getColor(R.color.holo_dialog_lighter_grey));
        }

        return row;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        View row = convertView;

        ItemHolder holder;

        if(row == null) {
            LayoutInflater inflater = LayoutInflater.from(m_context);
            row = inflater.inflate(R.layout.nav_drawer_item, parent, false);
            holder = new ItemHolder();

            holder.name = (TextView)row.findViewById(R.id.tv_item_label);
            holder.icon = (ImageView)row.findViewById(R.id.iv_item_icon);
            holder.groupState = (ImageView)row.findViewById(R.id.iv_group_state);

            row.setTag(holder);
        }
        else {
            holder = (ItemHolder)row.getTag();
        }

        NavDrawerGroupItem group = (NavDrawerGroupItem) getGroup(groupPosition);

        if (group != null) {
            holder.name.setText(group.getText());
            holder.icon.setImageResource(group.getIconResId());
        }
        if (isExpanded) {
            holder.groupState.setImageResource(R.drawable.expander_open_holo_dark);
        } else {
            holder.groupState.setImageResource(R.drawable.expander_close_holo_dark);
        }

        boolean hasChildren = getChildrenCount(groupPosition) != 0;
        if (hasChildren) {
            holder.groupState.setVisibility(View.VISIBLE);
        } else {
            holder.groupState.setVisibility(View.INVISIBLE);
        }

        // Show the selection indicator on the group, or on the character group is collapsed while a child is selected
        if (group != null && (group == m_selectedItem ||
                (m_selectedItem instanceof NavDrawerChildItem &&
                        ((NavDrawerChildItem) m_selectedItem).getGroup() == group && !isExpanded))) {
            row.setBackgroundColor(m_context.getResources().getColor(android.R.color.holo_blue_light));
        } else {
            row.setBackgroundColor(m_context.getResources().getColor(R.color.holo_dialog_grey));
        }

        return row;
    }
    
    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<NavDrawerChildItem> children = m_items.get(groupPosition).getChildren();
        return children != null ? children.get(childPosition) : null;
    }

    @Override
     public Object getGroup(int groupPosition) {
        return m_items.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return ((Integer) groupPosition).longValue();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return ((Integer) childPosition).longValue();
    }

    @Override
    public int getGroupCount() {
        return m_items.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        List<NavDrawerChildItem> children = m_items.get(groupPosition).getChildren();
        return children != null ? children.size() : 0;
    }

    private static long COMBINED_ID_GROUP_MOD = 10000L;

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return groupId * COMBINED_ID_GROUP_MOD + (childId + 1);
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return groupId * COMBINED_ID_GROUP_MOD;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return m_items.isEmpty();
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        
    }
    
    public void setSelectedItem(NavDrawerItem item) {
        m_selectedItem = item;
    }
    
    public NavDrawerItem getSelectedItem() {
        return m_selectedItem;
    }

    public int getGroupIndexForItem(NavDrawerItem item) {
        boolean isGroup = item instanceof NavDrawerGroupItem;
        for (int i = 0; i < m_items.size(); i++) {
            NavDrawerGroupItem group = m_items.get(i);
            if ((isGroup && item == group) ||
                    (!isGroup && group.getChildren() != null && group.getChildren().contains(item))) {
                return i;
            }
        }
        return -1;
    }

    public int getChildIndexForItem(NavDrawerItem item) {
        if (item instanceof NavDrawerGroupItem) return -1;
        for (NavDrawerGroupItem group : m_items) {
            List<NavDrawerChildItem> children = group.getChildren();
            if (children != null) {
                for (int i = 0; i < children.size(); i++) {
                    if (item == children.get(i)) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    public NavDrawerItem getItemForFragment(@NotNull Class<? extends BasePageFragment> fragmentClass) {
        for (NavDrawerGroupItem group : m_items) {
            if (fragmentClass.equals(group.getFragmentClass())) return group;
            List<NavDrawerChildItem> children = group.getChildren();
            if (children != null) {
                for (NavDrawerChildItem child : children) {
                    if (fragmentClass.equals(child.getFragmentClass())) {
                        return child;
                    }
                }
            }
        }
        return null;
    }

    static class ItemHolder {
        TextView name;
        ImageView icon;
        ImageView groupState;
    }

}
