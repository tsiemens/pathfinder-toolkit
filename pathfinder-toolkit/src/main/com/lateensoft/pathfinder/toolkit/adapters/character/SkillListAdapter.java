package com.lateensoft.pathfinder.toolkit.adapters.character;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.model.character.stats.AbilitySet;
import com.lateensoft.pathfinder.toolkit.model.character.stats.Skill;
import com.lateensoft.pathfinder.toolkit.model.character.stats.SkillSet;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class SkillListAdapter extends ArrayAdapter<Skill> {
	private int m_layoutResourceId;

	private AbilitySet m_abilitySet;
	private int m_maxDex;
	private int m_armorCheckPenalty;

	public SkillListAdapter(Context context, int layoutResourceId,
                            List<Skill> skills, int maxDex, AbilitySet characterAbilities, int armorCheckPenalty) {
		super(context, layoutResourceId, skills);
		m_layoutResourceId = layoutResourceId;
		m_abilitySet = characterAbilities;
		m_maxDex = maxDex;
		m_armorCheckPenalty = armorCheckPenalty;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		SkillHolder holder;

		if (row == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());

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

        Skill skill = getItem(position);

		String name = getContext().getString(skill.getType().getNameResId());
		if (skill.canBeSubTyped() && skill.getSubType() != null &&
				!skill.getSubType().isEmpty()) {
			name = name + " ("+ getItem(position).getSubType() + ")";
		}
		holder.name.setText(name);
		holder.total.setText(Integer.toString(getItem(position).getSkillMod(m_abilitySet, m_maxDex, m_armorCheckPenalty)));
		holder.abilityName.setText(getItem(position).getAbility().getNameResId());
		holder.abilityMod.setText(Integer.toString(m_abilitySet.getTotalAbilityMod(getItem(position).getAbility(), m_maxDex)));
		holder.rank.setText(Integer.toString(getItem(position).getRank()));
		holder.miscMod.setText(Integer.toString(getItem(position).getMiscMod()));
		
		if(getItem(position).isClassSkill() && getItem(position).getRank() > 0)
			holder.classSkill.setText("3");
		else
			holder.classSkill.setText("0");

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