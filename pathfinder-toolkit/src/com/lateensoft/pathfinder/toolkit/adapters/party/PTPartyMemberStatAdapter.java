package com.lateensoft.pathfinder.toolkit.adapters.party;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lateensoft.pathfinder.toolkit.R;

public class PTPartyMemberStatAdapter extends ArrayAdapter<String>{
	Context m_context;
	int m_layoutResourceId;
	int[] m_statValues = null;
	String[] m_statNames = null;

	public PTPartyMemberStatAdapter(Context context, int layoutResourceId, String[] statNames, int[] statValues) {
		super(context, layoutResourceId, statNames);
		m_layoutResourceId = layoutResourceId;
		m_context = context;
		m_statValues = statValues;
		m_statNames = statNames;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		PartyMemberStatHolder holder;
		
		if(row == null) {		
			LayoutInflater inflater = LayoutInflater.from(m_context);
			row = inflater.inflate(m_layoutResourceId, parent, false);
			holder = new PartyMemberStatHolder();
			
			holder.label = (TextView)row.findViewById(R.id.partyMemberStatLabel);
			holder.value = (TextView)row.findViewById(R.id.partyMemberStatValue);
			
			row.setTag(holder);
		}
		else {
			holder = (PartyMemberStatHolder)row.getTag();
		}
		
		Resources r = m_context.getResources();
		holder.label.setText(r.getStringArray(R.array.party_member_stats)[position]);
		holder.value.setText(Integer.toString(m_statValues[position]));
		
		return row;
	}
	
	static class PartyMemberStatHolder {
		TextView label;
		TextView value;
	}
	
	public void updateValues(int[] newStatValues){
		m_statValues = newStatValues;
		notifyDataSetChanged();
	}

}
