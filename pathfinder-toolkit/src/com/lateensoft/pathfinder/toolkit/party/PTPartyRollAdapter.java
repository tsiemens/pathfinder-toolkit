package com.lateensoft.pathfinder.toolkit.party;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lateensoft.pathfinder.toolkit.R;

public class PTPartyRollAdapter extends ArrayAdapter<String>{
	public static final int CRIT_FUMBLE = -1;
	public static final int NO_CRIT = 0;
	public static final int CRIT = 1;
	
	Context mContext;
	int mLayoutResourceId;
	int[] mRollValues = null;
	int[] mCritValues = null;
	String[] mCharacterNames = null;
	String TAG = "PTPartyMemberStatAdapter";
	
	/**
	 * 
	 * @param context
	 * @param layoutResourceId
	 * @param characterNames
	 * @param rollValues 
	 * @param critValues CRUIT_FUMBLE, NO_CRIT, CRIT. if null, all considered NO_CRIT
	 */
	public PTPartyRollAdapter(Context context, int layoutResourceId, String[] characterNames, 
			int[] rollValues, int[]critValues) {
		super(context, layoutResourceId, characterNames);
		mLayoutResourceId = layoutResourceId;
		mContext = context;
		mRollValues = rollValues;	
		mCharacterNames = characterNames;
		mCritValues = critValues;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		CharacterRollHolder holder;
		
		if(row == null) {		
			LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
			row = inflater.inflate(mLayoutResourceId, parent, false);	
			holder = new CharacterRollHolder();
			
			holder.name = (TextView)row.findViewById(R.id.textViewRollName);
			holder.rollValue = (TextView)row.findViewById(R.id.textViewRollValue);
			
			row.setTag(holder);
		}
		else {
			holder = (CharacterRollHolder)row.getTag();
		}
		
		holder.name.setText(mCharacterNames[position]);
		holder.rollValue.setText(Integer.toString(mRollValues[position]));
		
		if(mCritValues != null){
			if(mCritValues[position] == CRIT_FUMBLE){
				holder.rollValue.setTextColor(Color.RED);
			}
			else if(mCritValues[position] == CRIT){
				holder.rollValue.setTextColor(Color.GREEN);
			}
		}
		
		return row;
	}
	
	static class CharacterRollHolder {
		TextView name;
		TextView rollValue;
	}
	
	public void updateRollValues(int[] newRollValues){
		mRollValues = newRollValues;
		notifyDataSetChanged();
	}
}
