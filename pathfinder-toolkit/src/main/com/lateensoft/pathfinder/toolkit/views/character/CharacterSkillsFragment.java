package com.lateensoft.pathfinder.toolkit.views.character;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.adapters.character.SkillListAdapter;
import com.lateensoft.pathfinder.toolkit.db.repository.AbilityRepository;
import com.lateensoft.pathfinder.toolkit.db.repository.ArmorRepository;
import com.lateensoft.pathfinder.toolkit.db.repository.SkillRepository;
import com.lateensoft.pathfinder.toolkit.model.character.stats.AbilitySet;
import com.lateensoft.pathfinder.toolkit.model.character.stats.Skill;
import com.lateensoft.pathfinder.toolkit.model.character.stats.SkillSet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import com.lateensoft.pathfinder.toolkit.model.character.stats.SkillType;
import com.lateensoft.pathfinder.toolkit.views.ParcelableEditorActivity;

import java.util.List;

public class CharacterSkillsFragment extends AbstractCharacterSheetFragment
        implements OnItemClickListener {
    private static final String TAG = CharacterSkillsFragment.class.getSimpleName();

    private ListView m_skillsListView;

    private CheckBox m_applyACPCheckBox;
    private CheckBox m_trainedFilterCheckBox;

    private Skill m_skillSelectedForEdit;
    
    private SkillRepository m_skillRepo;
    private SkillSet m_skillSet;
    
    private ArmorRepository m_armorRepo;
    private AbilityRepository m_abilityRepo;
    
    private AbilitySet m_abilitySet;
    private int m_maxDex = Integer.MAX_VALUE;
    private int m_armorCheckPenalty = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_skillRepo = new SkillRepository();
        m_armorRepo = new ArmorRepository();
        m_abilityRepo = new AbilityRepository();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        setRootView(inflater.inflate(
                R.layout.character_skills_fragment, container, false));

        m_applyACPCheckBox = (CheckBox) getRootView().findViewById(R.id.checkboxApplyACP);
        m_applyACPCheckBox.setChecked(false);
        m_applyACPCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView == m_applyACPCheckBox) {
                    ((SkillListAdapter) m_skillsListView.getAdapter())
                    .setArmorCheckPenalty(isChecked ? m_armorCheckPenalty : 0);
                }
            }
        });
        
        m_trainedFilterCheckBox = (CheckBox) getRootView().findViewById(R.id.checkboxFilterTrained);
        m_trainedFilterCheckBox.setChecked(true);
        m_trainedFilterCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateFragmentUI();
            }
        });

        m_skillsListView = (ListView) getRootView()
                .findViewById(R.id.listViewCharacterSkills);
        m_skillsListView.setOnItemClickListener(this);

        return getRootView();
    }

    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        if (m_trainedFilterCheckBox.isChecked()) {
            m_skillSelectedForEdit = m_skillSet.getTrainedSkill(position);
        } else {
            m_skillSelectedForEdit = m_skillSet.getSkillByIndex(position);
        }
        showSkillEditor(m_skillSelectedForEdit);
    }
    
    private void showSkillEditor(Skill skill) {
        Intent skillEditIntent = new Intent(getContext(),
                SkillEditActivity.class);
        skillEditIntent.putExtra(
                SkillEditActivity.INTENT_EXTRAS_KEY_EDITABLE_PARCELABLE,skill);
        skillEditIntent.putExtra(SkillEditActivity.INTENT_EXTRAS_KEY_SKILL_DELETABLE_BOOLEAN,
                (skill.canBeSubTyped() && m_skillSet.hasMultipleOfSkill(skill.getType())));

        startActivityForResult(skillEditIntent, ParcelableEditorActivity.DEFAULT_REQUEST_CODE);
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != ParcelableEditorActivity.DEFAULT_REQUEST_CODE) {
            return;
        }
        switch (resultCode) {
        case Activity.RESULT_OK:
            Skill skill = ParcelableEditorActivity.getParcelableFromIntent(data);
            if (m_skillSelectedForEdit != null && skill != null) {
                m_skillSelectedForEdit.setSubType(skill.getSubType());
                m_skillSelectedForEdit.setAbility(skill.getAbility());
                m_skillSelectedForEdit.setRank(skill.getRank());
                m_skillSelectedForEdit.setMiscMod(skill.getMiscMod());
                m_skillSelectedForEdit.setClassSkill(skill.isClassSkill());
                
                m_skillRepo.update(m_skillSelectedForEdit);
                addNewSubSkills();
                updateSkillsList();
                
                m_skillSelectedForEdit = null;
            }        
            break;
        case SpellEditActivity.RESULT_DELETE:
            Log.v(TAG, "Deleting a skill subtype");
            if (m_skillSelectedForEdit != null && m_skillSelectedForEdit.canBeSubTyped()
                && m_skillSet.hasMultipleOfSkill(m_skillSelectedForEdit.getType())) {
                if (m_skillRepo.delete(m_skillSelectedForEdit.getId())!= 0) {
                    m_skillSet.deleteSkill(m_skillSelectedForEdit);
                }
                // Adding is in case they delete the only unranked skill
                addNewSubSkills();
                updateSkillsList();
            }
            break;
        
        case Activity.RESULT_CANCELED:
            break;
        default:
            break;
        }
        
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateSkillsList() {
        if (m_skillSet == null || m_abilitySet == null) return;
        List<Skill> skills = m_trainedFilterCheckBox.isChecked() ?
                m_skillSet.getTrainedSkills() : m_skillSet.getSkills();
        SkillListAdapter adapter = new SkillListAdapter(getContext(),
                R.layout.character_skill_row, skills,
                m_maxDex, m_abilitySet, getAppliedArmorCheckPenalty());
        m_skillsListView.setAdapter(adapter);
    }
    
    private int getAppliedArmorCheckPenalty() {
        return m_applyACPCheckBox.isChecked() ? m_armorCheckPenalty : 0;
    }

    @Override
    public void updateFragmentUI() {
        updateSkillsList();
    }

    @Override
    public String getFragmentTitle() {
        return getString(R.string.tab_character_skills);
    }

    @Override
    public void updateDatabase() {
        // Done in onActivityResult
    }

    @Override
    public void loadFromDatabase() {
        m_skillSet = m_skillRepo.querySet(getCurrentCharacterID());

        addNewSubSkills();
        m_maxDex = m_armorRepo.getMaxDex(getCurrentCharacterID());
        m_armorCheckPenalty = m_armorRepo.getArmorCheckPenalty(getCurrentCharacterID());
        m_abilitySet = m_abilityRepo.querySet(getCurrentCharacterID());
    }
    
    private void addNewSubSkills() {
        Skill newSkill;
        for (SkillType type : SkillType.values()) {
            if (type.canBeSubTyped() && m_skillSet.allSubSkillsUsed(type)) {
                newSkill = m_skillSet.addNewSubSkill(type);
                m_skillRepo.insert(newSkill);
            }
        }
    }
}
