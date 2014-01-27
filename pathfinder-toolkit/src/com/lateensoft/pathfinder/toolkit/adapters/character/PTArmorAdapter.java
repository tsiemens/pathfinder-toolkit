package com.lateensoft.pathfinder.toolkit.adapters.character;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.model.character.items.PTArmor;

public class PTArmorAdapter extends ArrayAdapter<PTArmor> {
	@SuppressWarnings("unused")
	private static final String TAG = PTArmorAdapter.class.getSimpleName();
	
	private Activity m_activity;
	private int m_layoutResourceId;
	private PTArmor[] m_armorArray = null;
	
	public PTArmorAdapter(Activity activity, int textViewResourceId,
			PTArmor[] objects) {
		super(activity, textViewResourceId, objects);
		m_layoutResourceId = textViewResourceId;
		m_activity = activity;
		m_armorArray = objects;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ArmorHolder holder;
		
		if(row == null) {
			LayoutInflater inflater = m_activity.getLayoutInflater();
			
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
		
		holder.name.setText(m_armorArray[position].getName());
		holder.worn.setVisibility(m_armorArray[position].isWorn() ? View.VISIBLE : View.GONE);
		holder.weight.setText(Double.toString(m_armorArray[position].getWeight()));
		holder.ACBonus.setText(Integer.toString(m_armorArray[position].getACBonus()));
		holder.checkPen.setText(Integer.toString(m_armorArray[position].getCheckPen()));
		holder.maxDex.setText(Integer.toString(m_armorArray[position].getMaxDex()));
		holder.specialProperties.setText(m_armorArray[position].getSpecialProperties());
		holder.speed.setText(m_armorArray[position].getSpeedString());
		holder.size.setText((m_armorArray[position].getSize()));
		holder.spellFail.setText(Integer.toString(m_armorArray[position].getSpellFail()) + "%");
		
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
