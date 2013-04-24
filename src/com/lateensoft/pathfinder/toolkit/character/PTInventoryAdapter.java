package com.lateensoft.pathfinder.toolkit.character;

import com.lateensoft.pathfinder.toolkit.items.PTItem;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lateensoft.pathfinder.toolkit.R;

public class PTInventoryAdapter extends ArrayAdapter<PTItem>{
	Context mContext;
	int mLayoutResourceId;
	PTItem[] mItems = null;
	String TAG = "PTInventoryAdapter";
	
	public PTInventoryAdapter(Context context, int layoutResourceId, PTItem[] items) {
		super(context, layoutResourceId, items);
		mLayoutResourceId = layoutResourceId;
		mContext = context;
		mItems = items;		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		SkillHolder holder;
		
		if(row == null) {		
			LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
			
			row = inflater.inflate(mLayoutResourceId, parent, false);	
			holder = new SkillHolder();
			
			holder.quantity = (TextView)row.findViewById(R.id.tvItemQuantity);
			holder.name = (TextView)row.findViewById(R.id.tvItemName);
			holder.weight = (TextView)row.findViewById(R.id.tvItemWeight);
			holder.contained = (TextView)row.findViewById(R.id.tvItemContained);
			
			row.setTag(holder);
		}
		else {
			holder = (SkillHolder)row.getTag();
		}
		
		holder.quantity.setText(Integer.toString(mItems[position].getQuantity()));
	
		holder.name.setText(mItems[position].getName());

		holder.weight.setText(Integer.toString(mItems[position].getWeight()));
		
		if(mItems[position].isContained())
			holder.contained.setText("\u2713"); //Check mark
		else holder.contained.setText("");
		
		return row;
	}
	
	static class SkillHolder {
		TextView quantity;
		TextView name;
		TextView weight;
		TextView contained;
	}
	
	public void refill(PTItem[] items){
		mItems = items;
		notifyDataSetChanged();
	}

}
