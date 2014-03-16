package com.lateensoft.pathfinder.toolkit.adapters.character;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTAbilitySet;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTSkill;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTSkillSet;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class PTSkillsAdapter extends ArrayAdapter<PTSkill> {
	private Context m_context;
	private int m_layoutResourceId;
	private SparseArray<String> m_skillNameMap;
	private SparseArray<String> m_abilityNameMap;
	
	private PTAbilitySet m_abilitySet;
	private int m_maxDex;
	private int m_armorCheckPenalty;

	public PTSkillsAdapter(Context context, int layoutResourceId,
			List<PTSkill> skills, int maxDex, PTAbilitySet characterAbilities, int armorCheckPenalty) {
		super(context, layoutResourceId, skills);
		m_layoutResourceId = layoutResourceId;
		m_context = context;
		m_skillNameMap = PTSkillSet.getSkillNameMap();
		m_abilityNameMap = PTAbilitySet.getAbilityShortNameMap();
		m_abilitySet = characterAbilities;
		m_maxDex = maxDex;
		m_armorCheckPenalty = armorCheckPenalty;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		SkillHolder holder;

		if (row == null) {
			LayoutInflater inflater = LayoutInflater.from(m_context);

			row = inflater.inflate(m_layoutResourceId, parent, false);
			holder = new SkillHolder();

			holder.name = (TextView) row.findViewById(R.id.tvSkillName);
			holder.total = (TextView) row.findViewById(R.id.tvSkillTotalMod);
			holder.abilityName = (TextView) row.findViewById(R.id.tvSkillAbility);
			holder.abilityMod = (TextView) row.findViewById(R.id.tvSkillAbilityMod);
			holder.rank = (TextView) row.findViewById(R.id.tvSkillRank);
			holder.miscMod = (TextView) row.findViewById(R.id.tvSkillMisc);
			holder.classSkill = (TextView) row.findViewById(R.id.tvClassSkill);
			
			row.setTag(holder);
		} else {
			holder = (SkillHolder) row.getTag();
		}

		String name = m_skillNameMap.get(getItem(position).getSkillKey());
		if (PTSkillSet.isSubtypedSkill(getItem(position).getSkillKey()) && 
				getItem(position).getSubType() != null && 
				!getItem(position).getSubType().isEmpty()) {
			name = name + " ("+ getItem(position).getSubType() + ")";
		}
		holder.name.setText(name);
		holder.total.setText(Integer.toString(getItem(position).getSkillMod(m_abilitySet, m_maxDex, m_armorCheckPenalty)));
		holder.abilityName.setText(m_abilityNameMap.get(getItem(position).getAbilityKey()));
		holder.abilityMod.setText(Integer.toString(m_abilitySet.getTotalAbilityMod(getItem(position).getAbilityKey(), m_maxDex)));
		holder.rank.setText(Integer.toString(getItem(position).getRank()));
		holder.miscMod.setText(Integer.toString(getItem(position).getMiscMod()));
		
		if(getItem(position).isClassSkill() && getItem(position).getRank() > 0)
			holder.classSkill.setText(new String("3"));
		else
			holder.classSkill.setText(new String("0"));

		return row;
	}
	
	/**
	 * Applied to the total skill mod.
	 * List is refreshed when called.
	 * @param acp
	 */
	public void setArmorCheckPenalty(int acp) {
		m_armorCheckPenalty = acp;
		notifyDataSetChanged();
	}


	static class SkillHolder {
		TextView name;
		TextView total;
		TextView abilityName;
		TextView abilityMod;
		TextView rank;
		TextView miscMod;
		TextView classSkill;
	}

	
}