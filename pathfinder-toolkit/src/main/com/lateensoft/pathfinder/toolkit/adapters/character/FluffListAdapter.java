package com.lateensoft.pathfinder.toolkit.adapters.character;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lateensoft.pathfinder.toolkit.R;

import java.util.List;

public class FluffListAdapter extends ArrayAdapter<String>{
	int layoutResourceId;

    String[] fluffNames;
	List<String> fluffValues;

	public FluffListAdapter(Context context, int layoutResourceId, String[] fluffNames, List<String> fluffValues) {
		super(context, layoutResourceId, fluffValues);
		this.layoutResourceId = layoutResourceId;
        this.fluffNames = fluffNames;
		this.fluffValues = fluffValues;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		FluffHolder holder;
		
		if(row == null) {		
			LayoutInflater inflater = LayoutInflater.from(getContext());
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new FluffHolder();
			
			holder.label = (TextView)row.findViewById(R.id.fluffLabel);
			holder.value = (TextView)row.findViewById(R.id.fluffValue);
			
			row.setTag(holder);
		}
		else {
			holder = (FluffHolder)row.getTag();
		}
		
		holder.label.setText(fluffNames[position]);
        String value = fluffValues.get(position);
		if(value == null) {
			value = "";
		}
		holder.value.setText(value);
		
		return row;
	}
	
	static class FluffHolder {
		TextView label;
		TextView value;
	}
}
