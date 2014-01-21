package com.lateensoft.pathfinder.toolkit.adapters.character;

import java.util.Map;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTAbilitySet;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTSkill;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTSkillSet;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PTSkillsAdapter extends ArrayAdapter<PTSkill> {
	private Context m_context;
	private int m_layoutResourceId;
	private PTSkill[] m_skills = null;
	private Map<Long, String> m_skillNameMap;
	private Map<Long, String> m_abilityNameMap;
	
	private PTAbilitySet m_abilitySet;
	private int m_maxDex;

	public PTSkillsAdapter(Context context, int layoutResourceId,
			PTSkill[] skills, int maxDex, PTAbilitySet characterAbilities) {
		super(context, layoutResourceId, skills);
		m_layoutResourceId = layoutResourceId;
		m_context = context;
		m_skills = skills;
		m_skillNameMap = PTSkillSet.getSkillNameMap();
		m_abilityNameMap = PTAbilitySet.getAbilityShortNameMap();
		m_abilitySet = characterAbilities;
		m_maxDex = maxDex;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		SkillHolder holder;

		if (row == null) {
			LayoutInflater inflater = ((Activity) m_context).getLayoutInflater();

			row = inflater.inflate(m_layoutResourceId, parent, false);
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

		holder.name.setText(m_skillNameMap.get(m_skills[position].getID()));
		holder.total.setText(Integer.toString(m_skills[position].getSkillMod(m_abilitySet, m_maxDex)));
		holder.abilityName.setText(m_abilityNameMap.get(m_skills[position].getAbilityId()));
		holder.abilityMod.setText(Integer.toString(m_skills[position].getAbilityMod(m_abilitySet, m_maxDex)));
		holder.rank.setText(Integer.toString(m_skills[position].getRank()));
		holder.miscMod.setText(Integer.toString(m_skills[position].getMiscMod()));
		holder.armorCheckPenalty.setText(Integer.toString(m_skills[position].getArmorCheckPenalty()));
		
		if(m_skills[position].isClassSkill() && m_skills[position].getRank() > 0)
			holder.classSkill.setText(new String("3"));
		else
			holder.classSkill.setText(new String("0"));

		return row;
	}
	
	public void updateList(PTSkill[] updatedSkills){
		m_skills = updatedSkills;
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