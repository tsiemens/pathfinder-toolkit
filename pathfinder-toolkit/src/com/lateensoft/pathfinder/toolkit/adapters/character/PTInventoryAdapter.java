package com.lateensoft.pathfinder.toolkit.adapters.character;

import com.lateensoft.pathfinder.toolkit.model.character.items.PTItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lateensoft.pathfinder.toolkit.R;

import java.util.List;

public class PTInventoryAdapter extends ArrayAdapter<PTItem>{
	Context m_context;
	int m_layoutResourceId;

	public PTInventoryAdapter(Context context, int layoutResourceId, List<PTItem> items) {
		super(context, layoutResourceId, items);
		m_layoutResourceId = layoutResourceId;
		m_context = context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		SkillHolder holder;
		
		if(row == null) {		
			LayoutInflater inflater = LayoutInflater.from(m_context);
			
			row = inflater.inflate(m_layoutResourceId, parent, false);
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
		
		holder.quantity.setText(Integer.toString(getItem(position).getQuantity()));
	
		holder.name.setText(getItem(position).getName());

		holder.weight.setText(Double.toString(getItem(position).getWeight()));
		
		if(getItem(position).isContained())
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
}
