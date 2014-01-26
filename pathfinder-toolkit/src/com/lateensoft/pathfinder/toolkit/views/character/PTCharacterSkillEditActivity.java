package com.lateensoft.pathfinder.toolkit.views.character;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
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
	
	public static final String INTENT_EXTRAS_KEY_SKILL_DELETABLE_BOOLEAN = "skill_is_deletable";
	private static final int MOD_SPINNER_OFFSET = 10;
	
	private TextView m_skillNameText;
	private EditText m_subtypeET;
	private TextView m_abilityTV;
	private Spinner m_rankSpinner;
	private Spinner m_miscModSpinner;
	private CheckBox m_classSkillCheckBox;
	
	private PTSkill m_skill;
	private boolean m_deletable;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		m_deletable = getIntent().getExtras().getBoolean(INTENT_EXTRAS_KEY_SKILL_DELETABLE_BOOLEAN);
	}

	@Override
	protected void setupContentView() {
		setContentView(R.layout.character_skill_editor);

		m_skillNameText = (TextView) findViewById(R.id.tvSkillName);
		m_subtypeET = (EditText) findViewById(R.id.etSkillSubType);
		m_abilityTV = (TextView) findViewById(R.id.tvSkillAbility);
		m_rankSpinner = (Spinner) findViewById(R.id.spinnerSkillRank);
		m_miscModSpinner = (Spinner) findViewById(R.id.spinnerSkillMisc);
		m_classSkillCheckBox = (CheckBox) findViewById(R.id.checkboxClassSkill);

		m_subtypeET.setVisibility(PTSkillSet.isSubtypedSkill(
				m_skill.getSkillKey()) ? View.VISIBLE : View.GONE);
		
		SparseArray<String> nameMap = PTSkillSet.getSkillNameMap();
		m_skillNameText.setText(nameMap.get(m_skill.getSkillKey()));
		
		m_subtypeET.setText(m_skill.getSubType());
		updateAbilityTextView();
		m_abilityTV.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View v) {
				SparseIntArray map = PTSkillSet.getDefaultAbilityKeyMap();
				PTAbilitySelectionDialog dialog = new PTAbilitySelectionDialog(
						PTCharacterSkillEditActivity.this, m_skill.getAbilityKey(), map.get(m_skill.getSkillKey()));
				dialog.setOnAbilitySelectedListener(new AbilityDialogListener());
				dialog.show();
			}
		});
		
		setupSpinner(m_rankSpinner, R.array.skills_selectable_values_string,
				m_skill.getRank() + MOD_SPINNER_OFFSET, null);
		setupSpinner(m_miscModSpinner, R.array.skills_selectable_values_string,
				m_skill.getMiscMod() + MOD_SPINNER_OFFSET, null);

		m_classSkillCheckBox.setChecked(m_skill.isClassSkill());
	}
	
	private void updateAbilityTextView() {
		m_abilityTV.setText(PTAbilitySet.getAbilityShortNameMap()
				.get(m_skill.getAbilityKey()));
	}
	
	private class AbilityDialogListener implements OnAbilitySelectedListener {

		@Override public void onAbilitySelected(int abilityId) {
			m_skill.setAbilityKey(abilityId);
			updateAbilityTextView();
		}
		
	}
	

	@Override
	protected void updateEditedParcelableValues() throws InvalidValueException {
		if (PTSkillSet.isSubtypedSkill(m_skill.getSkillKey())) {
			m_skill.setSubType(m_subtypeET.getText().toString());
		}
		m_skill.setRank(m_rankSpinner
				.getSelectedItemPosition() - MOD_SPINNER_OFFSET);
		m_skill.setMiscMod(m_miscModSpinner
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
		return m_deletable;
	}
}
