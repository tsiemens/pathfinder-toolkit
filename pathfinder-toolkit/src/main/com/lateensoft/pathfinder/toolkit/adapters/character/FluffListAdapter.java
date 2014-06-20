package com.lateensoft.pathfinder.toolkit.adapters.character;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lateensoft.pathfinder.toolkit.R;

public class FluffListAdapter extends ArrayAdapter<String>{
	Context m_context;
	int m_layoutResourceId;
	String[] m_fluff = null;

	public FluffListAdapter(Context context, int layoutResourceId, String[] fluff) {
		super(context, layoutResourceId, fluff);
		m_layoutResourceId = layoutResourceId;
		m_context = context;
		m_fluff = fluff;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		FluffHolder holder;
		
		if(row == null) {		
			LayoutInflater inflater = LayoutInflater.from(m_context);
			row = inflater.inflate(m_layoutResourceId, parent, false);
			holder = new FluffHolder();
			
			holder.label = (TextView)row.findViewById(R.id.fluffLabel);
			holder.value = (TextView)row.findViewById(R.id.fluffValue);
			
			row.setTag(holder);
		}
		else {
			holder = (FluffHolder)row.getTag();
		}
		
		Resources r = m_context.getResources();
		holder.label.setText(r.getStringArray(R.array.fluff_fields)[position]);
		if(m_fluff[position] == null) {
			m_fluff[position] = "";
		}
		holder.value.setText(m_fluff[position]);
		
		return row;
	}
	
	static class FluffHolder {
		TextView label;
		TextView value;
	}
}
