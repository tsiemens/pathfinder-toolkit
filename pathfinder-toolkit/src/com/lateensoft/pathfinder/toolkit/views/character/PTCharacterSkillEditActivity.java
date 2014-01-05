package com.lateensoft.pathfinder.toolkit.views.character;

import android.os.Parcelable;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.stats.PTSkill;
import com.lateensoft.pathfinder.toolkit.views.PTParcelableEditorActivity;

public class PTCharacterSkillEditActivity extends PTParcelableEditorActivity {
	@SuppressWarnings("unused")
	private static final String TAG = PTCharacterSkillEditActivity.class.getSimpleName();
	
	private static final int MOD_SPINNER_OFFSET = 10;
	
	private TextView m_skillNameText;
	private Spinner m_abilityModSpinner;
	private Spinner m_rankSpinner;
	private Spinner m_miscModSpinner;
	private Spinner m_armorCheckSpinner;
	private CheckBox m_classSkillCheckBox;
	
	private PTSkill m_skill;
	
	@Override
	protected void setupContentView() {
		setContentView(R.layout.character_skill_editor);

		m_skillNameText = (TextView) findViewById(R.id.tvSkillName);
		m_abilityModSpinner = (Spinner) findViewById(R.id.spinnerSkillAbility);
		m_rankSpinner = (Spinner) findViewById(R.id.spinnerSkillRank);
		m_miscModSpinner = (Spinner) findViewById(R.id.spinnerSkillMisc);
		m_armorCheckSpinner = (Spinner) findViewById(R.id.spinnerSkillACP);
		m_classSkillCheckBox = (CheckBox) findViewById(R.id.checkboxClassSkill);

		m_skillNameText.setText(m_skill.getName() + " ("
				+ m_skill.getKeyAbility() + ")");

		setupSpinner(m_abilityModSpinner, R.array.skills_selectable_values_string,
				m_skill.getAbilityMod() + MOD_SPINNER_OFFSET, null);
		setupSpinner(m_rankSpinner, R.array.skills_selectable_values_string,
				m_skill.getRank() + MOD_SPINNER_OFFSET, null);
		setupSpinner(m_miscModSpinner, R.array.skills_selectable_values_string,
				m_skill.getMiscMod() + MOD_SPINNER_OFFSET, null);
		setupSpinner(m_armorCheckSpinner, R.array.skills_selectable_values_string,
				m_skill.getArmorCheckPenalty() + MOD_SPINNER_OFFSET, null);

		m_classSkillCheckBox.setChecked(m_skill.isClassSkill());
	}

	@Override
	protected void updateEditedParcelableValues() throws InvalidValueException {
		m_skill.setAbilityMod(m_abilityModSpinner
				.getSelectedItemPosition() - MOD_SPINNER_OFFSET);
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
