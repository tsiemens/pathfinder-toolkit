package com.lateensoft.pathfinder.toolkit.character.sheet;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lateensoft.pathfinder.toolkit.R;

public class PTFluffAdapter extends ArrayAdapter<String>{
	Context mContext;
	int mLayoutResourceId;
	String[] mFluff = null;
	String TAG = "PTFluffAdapter";
	
	public PTFluffAdapter(Context context, int layoutResourceId, String[] fluff) {
		super(context, layoutResourceId, fluff);
		mLayoutResourceId = layoutResourceId;
		mContext = context;
		mFluff = fluff;		
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		FluffHolder holder;
		
		if(row == null) {		
			LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
			row = inflater.inflate(mLayoutResourceId, parent, false);	
			holder = new FluffHolder();
			
			holder.label = (TextView)row.findViewById(R.id.fluffLabel);
			holder.value = (TextView)row.findViewById(R.id.fluffValue);
			
			row.setTag(holder);
		}
		else {
			holder = (FluffHolder)row.getTag();
		}
		
		Resources r = mContext.getResources();
		holder.label.setText(r.getStringArray(R.array.fluff_fields)[position]);
		if(mFluff[position] == null) {
			mFluff[position] = new String("");
		}
		holder.value.setText(mFluff[position]);
		
		return row;
	}
	
	static class FluffHolder {
		TextView label;
		TextView value;
	}
}
