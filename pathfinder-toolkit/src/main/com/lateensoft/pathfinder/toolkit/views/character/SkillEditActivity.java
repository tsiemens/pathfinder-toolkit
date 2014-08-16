package com.lateensoft.pathfinder.toolkit.views.character;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.model.character.stats.AbilityType;
import com.lateensoft.pathfinder.toolkit.model.character.stats.Skill;
import com.lateensoft.pathfinder.toolkit.views.ParcelableEditorActivity;
import com.lateensoft.pathfinder.toolkit.views.character.AbilitySelectionDialog.OnAbilitySelectedListener;

public class SkillEditActivity extends ParcelableEditorActivity {
    @SuppressWarnings("unused")
    private static final String TAG = SkillEditActivity.class.getSimpleName();
    
    public static final String INTENT_EXTRAS_KEY_SKILL_DELETABLE_BOOLEAN = "skill_is_deletable";
    private static final int MOD_SPINNER_OFFSET = 10;
    
    private TextView m_skillNameText;
    private EditText m_subtypeET;
    private TextView m_abilityTV;
    private Spinner m_rankSpinner;
    private Spinner m_miscModSpinner;
    private CheckBox m_classSkillCheckBox;
    
    private Skill skill;
    private boolean m_deletable;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_deletable = getIntent().getExtras().getBoolean(INTENT_EXTRAS_KEY_SKILL_DELETABLE_BOOLEAN);
    }

    @Override
    protected void setupContentView() {
        setContentView(R.layout.skill_editor);

        m_skillNameText = (TextView) findViewById(R.id.tvSkillName);
        m_subtypeET = (EditText) findViewById(R.id.etSkillSubType);
        m_abilityTV = (TextView) findViewById(R.id.tvSkillAbility);
        m_rankSpinner = (Spinner) findViewById(R.id.spinnerSkillRank);
        m_miscModSpinner = (Spinner) findViewById(R.id.spinnerSkillMisc);
        m_classSkillCheckBox = (CheckBox) findViewById(R.id.checkboxClassSkill);

        m_subtypeET.setVisibility(
                skill.canBeSubTyped() ? View.VISIBLE : View.GONE);
        
        m_skillNameText.setText(skill.getType().getNameResId());
        
        m_subtypeET.setText(skill.getSubType());
        updateAbilityTextView();
        m_abilityTV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AbilitySelectionDialog dialog = new AbilitySelectionDialog(
                        SkillEditActivity.this, skill.getAbility(), skill.getType().getDefaultAbility());
                dialog.setOnAbilitySelectedListener(new AbilityDialogListener());
                dialog.show();
            }
        });
        
        setupSpinner(m_rankSpinner, R.array.skills_selectable_values_string,
                skill.getRank() + MOD_SPINNER_OFFSET, null);
        setupSpinner(m_miscModSpinner, R.array.skills_selectable_values_string,
                skill.getMiscMod() + MOD_SPINNER_OFFSET, null);

        m_classSkillCheckBox.setChecked(skill.isClassSkill());
    }
    
    private void updateAbilityTextView() {
        m_abilityTV.setText(skill.getAbility().getNameResId());
    }
    
    private class AbilityDialogListener implements OnAbilitySelectedListener {

        @Override public void onAbilitySelected(AbilityType ability) {
            skill.setAbility(ability);
            updateAbilityTextView();
        }
        
    }
    

    @Override
    protected void updateEditedParcelableValues() throws InvalidValueException {
        if (skill.canBeSubTyped()) {
            skill.setSubType(m_subtypeET.getText().toString());
        }
        skill.setRank(m_rankSpinner
                .getSelectedItemPosition() - MOD_SPINNER_OFFSET);
        skill.setMiscMod(m_miscModSpinner
                .getSelectedItemPosition() - MOD_SPINNER_OFFSET);
        skill.setClassSkill(m_classSkillCheckBox
                .isChecked());
    }

    @Override
    protected Parcelable getEditedParcelable() {
        return skill;
    }

    @Override
    protected void setParcelableToEdit(Parcelable p) {
        if (p == null) {
            // Should not happen usually, but could possibly due to lifecycle, in which case just leave
            finish();
        } else {
            skill = (Skill) p;
        }
        
    }

    @Override
    protected boolean isParcelableDeletable() {
        return m_deletable;
    }
}
