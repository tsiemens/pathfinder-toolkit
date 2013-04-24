package com.lateensoft.pathfinder.toolkit;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PTMainMenuAdapter extends ArrayAdapter<String>{
	Context mContext;
	int mLayoutResourceId;
	final int[] mImageIds = {R.drawable.dice_roller_icon, R.drawable.character_sheet_icon, R.drawable.initiative_icon,
			R.drawable.skill_checker_icon, R.drawable.party_icon, R.drawable.stat_calc_icon};
	String[] mMenuItemNames = null;
	String TAG = "PTMainMenuAdapter";
	
	public PTMainMenuAdapter(Context context, int layoutResourceId, String[] menuItems) {
		super(context, layoutResourceId, menuItems);
		mLayoutResourceId = layoutResourceId;
		mContext = context;	
		mMenuItemNames = menuItems;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		MenuItemHolder holder;
		
		if(row == null) {		
			LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
			row = inflater.inflate(mLayoutResourceId, parent, false);	
			holder = new MenuItemHolder();
			
			holder.name = (TextView)row.findViewById(R.id.textViewMainLabel);
			holder.icon = (ImageView)row.findViewById(R.id.imageMainRow);
			
			row.setTag(holder);
		}
		else {
			holder = (MenuItemHolder)row.getTag();
		}
		
		holder.name.setText(mMenuItemNames[position]);
		holder.icon.setImageResource(mImageIds[position]);
		
		
		return row;
	}
	
	static class MenuItemHolder {
		TextView name;
		ImageView icon;
	}
	
	

}
