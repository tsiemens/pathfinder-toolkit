package com.lateensoft.pathfinder.toolkit.adapters.party;

import android.content.Context;
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
	
	Context m_context;
	int m_layoutResourceId;
	int[] m_rollValues = null;
	int[] m_critValues = null;
	String[] m_characterNames = null;

	/**
	 * 
	 * @param context
	 * @param layoutResourceId
	 * @param characterNames
	 * @param rollValues 
	 * @param critValues CRUIT_FUMBLE, NO_CRIT, CRIT. if null, all considered NO_CRIT
	 */
	public PTPartyRollAdapter(Context context, int layoutResourceId, String[] characterNames, 
			int[] rollValues, int[] critValues) {
		super(context, layoutResourceId, characterNames);
		m_layoutResourceId = layoutResourceId;
		m_context = context;
		m_rollValues = rollValues;
		m_characterNames = characterNames;
		m_critValues = critValues;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		CharacterRollHolder holder;
		
		if(row == null) {		
			LayoutInflater inflater = LayoutInflater.from(m_context);
			row = inflater.inflate(m_layoutResourceId, parent, false);
			holder = new CharacterRollHolder();
			
			holder.name = (TextView)row.findViewById(R.id.textViewRollName);
			holder.rollValue = (TextView)row.findViewById(R.id.textViewRollValue);
			
			row.setTag(holder);
		}
		else {
			holder = (CharacterRollHolder)row.getTag();
		}
		
		holder.name.setText(m_characterNames[position]);
		holder.rollValue.setText(Integer.toString(m_rollValues[position]));
		
		if(m_critValues != null){
			if(m_critValues[position] == CRIT_FUMBLE){
				holder.rollValue.setTextColor(Color.RED);
			}
			else if(m_critValues[position] == CRIT){
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
		m_rollValues = newRollValues;
		notifyDataSetChanged();
	}
}
