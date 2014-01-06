package com.lateensoft.pathfinder.toolkit.adapters.character;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTSkill;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTSkillSet;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PTSkillsAdapter extends ArrayAdapter<PTSkill> {
	final String TAG = "PTSkillsAdapter";
	
	Context mContext;
	int mLayoutResourceId;
	PTSkill[] mSkills = null;

	public PTSkillsAdapter(Context context, int layoutResourceId,
			PTSkill[] skills) {
		super(context, layoutResourceId, skills);
		mLayoutResourceId = layoutResourceId;
		mContext = context;
		mSkills = skills;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		SkillHolder holder;

		if (row == null) {
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();

			row = inflater.inflate(mLayoutResourceId, parent, false);
			holder = new SkillHolder();

			holder.name = (TextView) row.findViewById(R.id.tvSkillName);
			holder.total = (TextView) row.findViewById(R.id.tvSkillTotalMod);
			holder.abilityName = (TextView) row.findViewById(R.id.tvSkillAbility);
			holder.abilityMod = (TextView) row.findViewById(R.id.tvSkillAbilityMod);
			holder.rank = (TextView) row.findViewById(R.id.tvSkillRank);
			holder.miscMod = (TextView) row.findViewById(R.id.tvSkillMisc);
			holder.armorCheckPenalty = (TextView) row.findViewById(R.id.tvSkillACP);
			holder.classSkill = (TextView) row.findViewById(R.id.tvClassSkill);
			
			row.setTag(holder);
		} else {
			holder = (SkillHolder) row.getTag();
		}

		holder.name.setText(mSkills[position].getName());
		holder.total.setText(Integer.toString(mSkills[position].getSkillMod()));
		holder.abilityName.setText(mSkills[position].getKeyAbility());
		holder.abilityMod.setText(Integer.toString(mSkills[position].getAbilityMod()));
		holder.rank.setText(Integer.toString(mSkills[position].getRank()));
		holder.miscMod.setText(Integer.toString(mSkills[position].getMiscMod()));
		holder.armorCheckPenalty.setText(Integer.toString(mSkills[position].getArmorCheckPenalty()));
		
		if(mSkills[position].isClassSkill() && mSkills[position].getRank() > 0)
			holder.classSkill.setText(new String("3"));
		else
			holder.classSkill.setText(new String("0"));

		return row;
	}
	
	public void updateList(PTSkill[] updatedSkills){
		mSkills = updatedSkills;
		notifyDataSetChanged();
	}


	static class SkillHolder {
		TextView name;
		TextView total;
		TextView abilityName;
		TextView abilityMod;
		TextView rank;
		TextView miscMod;
		TextView armorCheckPenalty;
		TextView classSkill;
	}

	
}