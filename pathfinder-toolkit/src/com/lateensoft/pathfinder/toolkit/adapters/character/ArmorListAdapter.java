package com.lateensoft.pathfinder.toolkit.adapters.character;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.model.character.items.Armor;

import java.util.List;

public class ArmorListAdapter extends ArrayAdapter<Armor> {
	@SuppressWarnings("unused")
	private static final String TAG = ArmorListAdapter.class.getSimpleName();
	
	private Context m_context;
	private int m_layoutResourceId;

	public ArmorListAdapter(Context context, int textViewResourceId,
                            List<Armor> objects) {
		super(context, textViewResourceId, objects);
		m_layoutResourceId = textViewResourceId;
		m_context = context;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ArmorHolder holder;
		
		if(row == null) {
			LayoutInflater inflater = LayoutInflater.from(m_context);
			
			row = inflater.inflate(m_layoutResourceId, parent, false);
			holder = new ArmorHolder();
			
			holder.name = (TextView)row.findViewById(R.id.armorName);
			holder.worn = (TextView)row.findViewById(R.id.tvIsWorn);
			holder.weight = (TextView)row.findViewById(R.id.armorWeight);
			holder.ACBonus = (TextView)row.findViewById(R.id.armorClass);
			holder.checkPen = (TextView)row.findViewById(R.id.armorCheckPenalty);
			holder.maxDex = (TextView)row.findViewById(R.id.armorMaxDex);
			holder.specialProperties = (TextView)row.findViewById(R.id.armorSpecial);
			holder.speed = (TextView)row.findViewById(R.id.armorSpeed);
			holder.size = (TextView)row.findViewById(R.id.armorSize);
			holder.spellFail = (TextView)row.findViewById(R.id.armorSpellFail);
			
			row.setTag(holder);
		} else {
			holder = (ArmorHolder)row.getTag();
		}
		
		holder.name.setText(getItem(position).getName());
		holder.worn.setVisibility(getItem(position).isWorn() ? View.VISIBLE : View.GONE);
		holder.weight.setText(Double.toString(getItem(position).getWeight()));
		holder.ACBonus.setText(Integer.toString(getItem(position).getACBonus()));
		holder.checkPen.setText(Integer.toString(getItem(position).getCheckPen()));
		holder.maxDex.setText(Integer.toString(getItem(position).getMaxDex()));
		holder.specialProperties.setText(getItem(position).getSpecialProperties());
		holder.speed.setText(getItem(position).getSpeedString());
		holder.size.setText((getItem(position).getSize()));
		holder.spellFail.setText(Integer.toString(getItem(position).getSpellFail()) + "%");
		
		return row;
	}
	
	static class ArmorHolder {
		TextView name;
		TextView worn;
		TextView spellFail;
		TextView weight;
		TextView ACBonus;
		TextView checkPen;
		TextView maxDex;
		TextView specialProperties;
		TextView speed;
		TextView size;
	}
}
