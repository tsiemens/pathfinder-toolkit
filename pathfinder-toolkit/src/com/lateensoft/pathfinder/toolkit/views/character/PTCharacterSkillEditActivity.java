package com.lateensoft.pathfinder.toolkit.views.character;

import java.util.Map;

import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTAbilitySet;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTSkill;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTSkillSet;
import com.lateensoft.pathfinder.toolkit.views.PTParcelableEditorActivity;
import com.lateensoft.pathfinder.toolkit.views.character.PTAbilitySelectionDialog.OnAbilitySelectedListener;

public class PTCharacterSkillEditActivity extends PTParcelableEditorActivity {
	@SuppressWarnings("unused")
	private static final String TAG = PTCharacterSkillEditActivity.class.getSimpleName();
	
	private static final int MOD_SPINNER_OFFSET = 10;
	
	private TextView m_skillNameText;
	private TextView m_abilityTV;
	private Spinner m_rankSpinner;
	private Spinner m_miscModSpinner;
	private Spinner m_armorCheckSpinner;
	private CheckBox m_classSkillCheckBox;
	
	private PTSkill m_skill;
	
	@Override
	protected void setupContentView() {
		setContentView(R.layout.character_skill_editor);

		m_skillNameText = (TextView) findViewById(R.id.tvSkillName);
		m_abilityTV = (TextView) findViewById(R.id.tvSkillAbility);
		m_rankSpinner = (Spinner) findViewById(R.id.spinnerSkillRank);
		m_miscModSpinner = (Spinner) findViewById(R.id.spinnerSkillMisc);
		m_armorCheckSpinner = (Spinner) findViewById(R.id.spinnerSkillACP);
		m_classSkillCheckBox = (CheckBox) findViewById(R.id.checkboxClassSkill);

		Map<Long, String> nameMap = PTSkillSet.getSkillNameMap();
		m_skillNameText.setText(nameMap.get(m_skill.getID()));
		
		updateAbilityTextView();
		m_abilityTV.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View v) {
				Map<Long, Long> map = PTSkillSet.getDefaultAbilityIdMap();
				PTAbilitySelectionDialog dialog = new PTAbilitySelectionDialog(
						PTCharacterSkillEditActivity.this, m_skill.getAbilityId(), map.get(m_skill.getID()).longValue());
				dialog.setOnAbilitySelectedListener(new AbilityDialogListener());
				dialog.show();
			}
		});
		
		setupSpinner(m_rankSpinner, R.array.skills_selectable_values_string,
				m_skill.getRank() + MOD_SPINNER_OFFSET, null);
		setupSpinner(m_miscModSpinner, R.array.skills_selectable_values_string,
				m_skill.getMiscMod() + MOD_SPINNER_OFFSET, null);
		setupSpinner(m_armorCheckSpinner, R.array.skills_selectable_values_string,
				m_skill.getArmorCheckPenalty() + MOD_SPINNER_OFFSET, null);

		m_classSkillCheckBox.setChecked(m_skill.isClassSkill());
	}
	
	private void updateAbilityTextView() {
		m_abilityTV.setText(PTAbilitySet.getAbilityShortNameMap()
				.get(m_skill.getAbilityId()));
	}
	
	private class AbilityDialogListener implements OnAbilitySelectedListener {

		@Override public void onAbilitySelected(long abilityId) {
			m_skill.setAbilityId(abilityId);
			updateAbilityTextView();
		}
		
	}
	

	@Override
	protected void updateEditedParcelableValues() throws InvalidValueException {
		m_skill.setRank(m_rankSpinner
				.getSelectedItemPosition() - MOD_SPINNER_OFFSET);
		m_skill.setMiscMod(m_miscModSpinner
				.getSelectedItemPosition() - MOD_SPINNER_OFFSET);
		m_skill.setArmorCheckPenalty(m_armorCheckSpinner
				.getSelectedItemPosition() - MOD_SPINNER_OFFSET);
		m_skill.setClassSkill(m_classSkillCheckBox
				.isChecked());
	}

	@Override
	protected Parcelable getEditedParcelable() {
		return m_skill;
	}

	@Override
	protected void setParcelableToEdit(Parcelable p) {
		if (p == null) {
			// Should not happen usually, but could possibly due to lifecycle, in which case just leave
			finish();
		} else {
			m_skill = (PTSkill) p;
		}
		
	}

	@Override
	protected boolean isParcelableDeletable() {
		return false;
	}
}
