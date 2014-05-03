package com.lateensoft.pathfinder.toolkit.adapters.character;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.model.character.items.Weapon;

import java.util.List;

public class WeaponListAdapter extends ArrayAdapter<Weapon> {
	Context m_context;
	int m_layoutResourceId;
	
	public WeaponListAdapter(Context context, int layoutResourceId, List<Weapon> weapons) {
		super(context, layoutResourceId, weapons);
		m_layoutResourceId = layoutResourceId;
		m_context = context;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		WeaponHolder holder;
		
		if(row == null) {
			LayoutInflater inflater = LayoutInflater.from(m_context);
			
			row = inflater.inflate(m_layoutResourceId, parent, false);
			holder = new WeaponHolder();
			
			holder.name = (TextView)row.findViewById(R.id.weaponName);
			holder.weight = (TextView)row.findViewById(R.id.weaponWeight);
			holder.totalAttackBonus = (TextView)row.findViewById(R.id.weaponAttack);
			holder.damage = (TextView)row.findViewById(R.id.weaponDamage);
			holder.range = (TextView)row.findViewById(R.id.weaponRange);
			holder.specialProperties = (TextView)row.findViewById(R.id.weaponSpecial);
			holder.type = (TextView)row.findViewById(R.id.weaponType);
			holder.size = (TextView)row.findViewById(R.id.weaponSize);
			holder.critical = (TextView)row.findViewById(R.id.weaponCrit);
			holder.ammo = (TextView)row.findViewById(R.id.weaponAmmo);
			
			row.setTag(holder);
		} else {
			holder = (WeaponHolder)row.getTag();
		}
		
		holder.name.setText(getItem(position).getName());
		holder.weight.setText(getItem(position).getWeightQuantityContainedText(getContext()));
		holder.totalAttackBonus.setText(Integer.toString(getItem(position).getTotalAttackBonus()));
		holder.damage.setText(getItem(position).getDamage());
		holder.range.setText(Integer.toString(getItem(position).getRange()));
		holder.specialProperties.setText(getItem(position).getSpecialProperties());
		holder.type.setText(getItem(position).getType());
		holder.size.setText(getItem(position).getSize());
		holder.critical.setText(getItem(position).getCritical());
		holder.ammo.setText(Integer.toString(getItem(position).getAmmunition()));
		
		return row;
	}
	
	static class WeaponHolder {
		TextView name;
		TextView weight;
		TextView totalAttackBonus;
		TextView damage;
		TextView range;
		TextView specialProperties;
		TextView type;
		TextView size;
		TextView critical;
		TextView ammo;
	}
}
