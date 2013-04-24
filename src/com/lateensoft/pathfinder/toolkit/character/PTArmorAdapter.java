package com.lateensoft.pathfinder.toolkit.character;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.items.PTArmor;

public class PTArmorAdapter extends ArrayAdapter<PTArmor> {
	
	Context mContext;
	int mLayoutResourceId;
	PTArmor[] mArmor = null;
	static final String TAG = "PTArmorAdapter";
	
	public PTArmorAdapter(Context context, int textViewResourceId,
			PTArmor[] objects) {
		super(context, textViewResourceId, objects);
		mLayoutResourceId = textViewResourceId;
		mContext = context;
		mArmor = objects;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ArmorHolder holder;
		
		if(row == null) {
			LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
			
			row = inflater.inflate(mLayoutResourceId, parent, false);
			holder = new ArmorHolder();
			
			holder.name = (TextView)row.findViewById(R.id.armorName);
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
		
		holder.name.setText(mArmor[position].getName());
		holder.weight.setText(Integer.toString(mArmor[position].getWeight()));
		holder.ACBonus.setText(Integer.toString(mArmor[position].getACBonus()));
		holder.checkPen.setText(Integer.toString(mArmor[position].getCheckPen()));
		holder.maxDex.setText(Integer.toString(mArmor[position].getMaxDex()));
		holder.specialProperties.setText(mArmor[position].getSpecialProperties());
		holder.speed.setText(mArmor[position].getSpeedString());
		holder.size.setText((mArmor[position].getSize()));
		holder.spellFail.setText(Integer.toString(mArmor[position].getSpellFail()) + "%");
		
		return row;
	}
	
	static class ArmorHolder {
		TextView name;
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
