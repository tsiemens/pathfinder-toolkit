package com.lateensoft.pathfinder.toolkit;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PTNavDrawerAdapter implements ExpandableListAdapter{

	public static long CHARACTER_SHEET_ID = 1;
	public static long FLUFF_ID = 10; 
	public static long COMBAT_STATS_ID = 11; 
	public static long ABILITIES_ID = 12;
	public static long SKILLS_ID = 13; 
	public static long INVENTORY_ID = 14;
	public static long ARMOR_ID = 15; 
	public static long WEAPONS_ID = 16;
	public static long FEATS_ID = 17;
	public static long SPELLS_ID = 18;
	public static long INITIATIVE_TRACKER_ID = 2;
	public static long SKILL_CHECKER_ID = 3;
	public static long PARTY_MANAGER_ID = 4;
	public static long POINTBUY_ID = 5;
	public static long DICE_ROLLER_ID = 6;
	
	private Context mContext;
	
	private String[] mGroupNames;
	private int[] mGroupIconRes;
	private String[] mCharacterSheetPages;
	
	private long mSelectedItem;
	
	public PTNavDrawerAdapter(Context context) {
		mContext = context;
		
		mGroupNames = mContext.getResources().getStringArray(R.array.main_menu_array);
		mGroupIconRes = new int[]{
				R.drawable.character_sheet_icon,
				R.drawable.initiative_icon,
				R.drawable.skill_checker_icon,
				R.drawable.party_icon,
				R.drawable.stat_calc_icon,
				R.drawable.dice_roller_icon
		};
		mCharacterSheetPages = new String[]{
				mContext.getString(R.string.tab_character_fluff),
				mContext.getString(R.string.tab_character_combat_stats),
				mContext.getString(R.string.tab_character_abilities),
				mContext.getString(R.string.tab_character_skills),
				mContext.getString(R.string.tab_character_inventory),
				mContext.getString(R.string.tab_character_armor),
				mContext.getString(R.string.tab_character_weapons),
				mContext.getString(R.string.tab_character_feats),
				mContext.getString(R.string.tab_character_spells)
		};
	}
	
	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		if (getGroupId(groupPosition) == CHARACTER_SHEET_ID){
			return CHARACTER_SHEET_ID * 10 + childPosition;
		}
		return 0;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if (getGroupId(groupPosition) == CHARACTER_SHEET_ID){
			return mCharacterSheetPages.length;
		}
		return 0;
	}

	@Override
	public long getCombinedChildId(long groupId, long childId) {
		return childId;
	}

	@Override
	public long getCombinedGroupId(long groupId) {
		return groupId;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return null;
	}

	@Override
	public int getGroupCount() {
		return mGroupNames.length;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition + 1;
	}
	
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		View row = convertView;
		ItemHolder holder;
		
		if(row == null) {		
			LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
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
		
		holder.name.setText(mCharacterSheetPages[childPosition]);

		holder.icon.setImageResource(R.drawable.character_sheet_icon);
		holder.icon.setVisibility(View.INVISIBLE);
		holder.groupState.setImageResource(R.drawable.expander_open_holo_dark);
		holder.groupState.setVisibility(View.INVISIBLE);
		
		if (getCombinedChildId(getGroupId(groupPosition), getChildId(groupPosition, childPosition)) == mSelectedItem) {
			row.setBackgroundColor(mContext.getResources().getColor(android.R.color.holo_blue_light));
		} else {
			row.setBackgroundColor(mContext.getResources().getColor(R.color.holo_dialog_lighter_grey));
		}
		
		return row;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		View row = convertView;
		
		ItemHolder holder;
		
		if(row == null) {		
			LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
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
		
		holder.name.setText(mGroupNames[groupPosition]);
		holder.icon.setImageResource(mGroupIconRes[groupPosition]);
		if (isExpanded) {
			holder.groupState.setImageResource(R.drawable.expander_open_holo_dark);
		} else {
			holder.groupState.setImageResource(R.drawable.expander_close_holo_dark);
		}
		
		if (getGroupId(groupPosition) != CHARACTER_SHEET_ID) {
			holder.groupState.setVisibility(View.INVISIBLE);
		} else {
			holder.groupState.setVisibility(View.VISIBLE);
		}
		
		// Show the selection indicator on the group, or on the character group is collapsed while a child is selected
		if (getCombinedGroupId(getGroupId(groupPosition)) == mSelectedItem || 
				(getCombinedGroupId(getGroupId(groupPosition)) == CHARACTER_SHEET_ID &&
				mSelectedItem >= FLUFF_ID && !isExpanded)) {
			row.setBackgroundColor(mContext.getResources().getColor(android.R.color.holo_blue_light));
		} else {
			row.setBackgroundColor(mContext.getResources().getColor(R.color.holoDialogGrey));
		}
		
		return row;
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
		return false;
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
	
	public void setSelectedItem(long combinedItemId) {
		mSelectedItem = combinedItemId;
	}
	
	public long getSelectedItem() {
		return mSelectedItem;
	}
	
	static class ItemHolder {
		TextView name;
		ImageView icon;
		ImageView groupState;
	}

}
