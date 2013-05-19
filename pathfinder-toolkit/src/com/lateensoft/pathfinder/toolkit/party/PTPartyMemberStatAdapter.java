package com.lateensoft.pathfinder.toolkit.party;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lateensoft.pathfinder.toolkit.R;

public class PTPartyMemberStatAdapter extends ArrayAdapter<String>{
	Context mContext;
	int mLayoutResourceId;
	int[] mStatValues = null;
	String[] mStatNames = null;
	String TAG = "PTPartyMemberStatAdapter";
	
	public PTPartyMemberStatAdapter(Context context, int layoutResourceId, String[] statNames, int[] statValues) {
		super(context, layoutResourceId, statNames);
		mLayoutResourceId = layoutResourceId;
		mContext = context;
		mStatValues = statValues;	
		mStatNames = statNames;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		PartyMemberStatHolder holder;
		
		if(row == null) {		
			LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
			row = inflater.inflate(mLayoutResourceId, parent, false);	
			holder = new PartyMemberStatHolder();
			
			holder.label = (TextView)row.findViewById(R.id.partyMemberStatLabel);
			holder.value = (TextView)row.findViewById(R.id.partyMemberStatValue);
			
			row.setTag(holder);
		}
		else {
			holder = (PartyMemberStatHolder)row.getTag();
		}
		
		Resources r = mContext.getResources();
		holder.label.setText(r.getStringArray(R.array.party_member_stats)[position]);
		holder.value.setText(Integer.toString(mStatValues[position]));
		
		return row;
	}
	
	static class PartyMemberStatHolder {
		TextView label;
		TextView value;
	}
	
	public void updateValues(int[] newStatValues){
		mStatValues = newStatValues;
		notifyDataSetChanged();
	}

}
