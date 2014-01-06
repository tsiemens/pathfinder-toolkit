package com.lateensoft.pathfinder.toolkit.adapters.character;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.model.character.items.PTWeapon;

public class PTWeaponAdapter extends ArrayAdapter<PTWeapon> {
	Context mContext;
	int mLayoutResourceId;
	PTWeapon[] mWeapons = null;
	static final String TAG = "PTWeaponAdapter";
	
	public PTWeaponAdapter(Context context, int layoutResourceId, PTWeapon[] weapons) {
		super(context, layoutResourceId, weapons);
		mLayoutResourceId = layoutResourceId;
		mContext = context;
		mWeapons = weapons;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		WeaponHolder holder;
		
		if(row == null) {
			LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
			
			row = inflater.inflate(mLayoutResourceId, parent, false);
			holder = new WeaponHolder();
			
			holder.name = (TextView)row.findViewById(R.id.weaponName);
			holder.weight = (TextView)row.findViewById(R.id.weaponWeight);
			holder.totalAttackBonus = (TextView)row.findViewById(R.id.weaponAttack);
			holder.damage = (TextView)row.findViewById(R.id.weaponDamage);
			holder.range = (TextView)row.findViewById(R.id.weaponRange);
			holder.specialProperties = (TextView)row.findViewById(R.id.weaponSpecial);
			//holder.ammunition = (TextView)row.findViewById
			holder.type = (TextView)row.findViewById(R.id.weaponType);
			holder.size = (TextView)row.findViewById(R.id.weaponSize);
			holder.critical = (TextView)row.findViewById(R.id.weaponCrit);
			
			row.setTag(holder);
		} else {
			holder = (WeaponHolder)row.getTag();
		}
		
		holder.name.setText(mWeapons[position].getName());
		holder.weight.setText(Double.toString(mWeapons[position].getWeight()));
		holder.totalAttackBonus.setText(Integer.toString(mWeapons[position].getTotalAttackBonus()));
		holder.damage.setText(mWeapons[position].getDamage());
		holder.range.setText(Integer.toString(mWeapons[position].getRange()));
		holder.specialProperties.setText(mWeapons[position].getSpecialProperties());
		holder.type.setText(mWeapons[position].getType());
		holder.size.setText(mWeapons[position].getSize());
		holder.critical.setText(mWeapons[position].getCritical());
		//holder.name.setText(mWeapons[position].getName());
		
		return row;
	}
	
	static class WeaponHolder {
		TextView name;
		TextView weight;
		TextView totalAttackBonus;
		TextView damage;
		TextView range;
		TextView specialProperties;
		TextView ammunition;
		TextView type;
		TextView size;
		TextView critical;
	}
}
