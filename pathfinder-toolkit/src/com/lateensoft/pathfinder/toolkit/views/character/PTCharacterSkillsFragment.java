package com.lateensoft.pathfinder.toolkit.views.character;

import java.util.List;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.adapters.character.PTSkillsAdapter;
import com.lateensoft.pathfinder.toolkit.db.repository.PTAbilityRepository;
import com.lateensoft.pathfinder.toolkit.db.repository.PTArmorRepository;
import com.lateensoft.pathfinder.toolkit.db.repository.PTSkillRepository;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTAbilitySet;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTSkill;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTSkillSet;

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
import com.lateensoft.pathfinder.toolkit.views.PTParcelableEditorActivity;

public class PTCharacterSkillsFragment extends PTCharacterSheetFragment
		implements OnItemClickListener {
	private static final String TAG = PTCharacterSkillsFragment.class.getSimpleName();

	private ListView m_skillsListView;

	private CheckBox m_applyACPCheckBox;
	private CheckBox m_trainedFilterCheckBox;

	private PTSkill m_skillSelectedForEdit;
	
	private PTSkillRepository m_skillRepo;
	private PTSkillSet m_skillSet;
	
	private PTArmorRepository m_armorRepo;
	private PTAbilityRepository m_abilityRepo;
	
	private PTAbilitySet m_abilitySet;
	private int m_maxDex = Integer.MAX_VALUE;
	private int m_armorCheckPenalty = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		m_skillRepo = new PTSkillRepository();
		m_armorRepo = new PTArmorRepository();
		m_abilityRepo = new PTAbilityRepository();
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
					((PTSkillsAdapter) m_skillsListView.getAdapter())
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
	
	private void showSkillEditor(PTSkill skill) {
		Intent skillEditIntent = new Intent(getActivity(),
				PTCharacterSkillEditActivity.class);
		skillEditIntent.putExtra(
				PTCharacterSkillEditActivity.INTENT_EXTRAS_KEY_EDITABLE_PARCELABLE,skill);
		skillEditIntent.putExtra(PTCharacterSkillEditActivity.INTENT_EXTRAS_KEY_SKILL_DELETABLE_BOOLEAN,
				(PTSkillSet.isSubtypedSkill(skill.getSkillKey()) 
				&& m_skillSet.hasMultipleOfSkill(skill.getSkillKey())) );
		startActivityForResult(skillEditIntent, PTParcelableEditorActivity.DEFAULT_REQUEST_CODE);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != PTParcelableEditorActivity.DEFAULT_REQUEST_CODE) {
            return;
        }
		switch (resultCode) {
		case Activity.RESULT_OK:
			PTSkill skill = PTParcelableEditorActivity.getParcelableFromIntent(data);
			if (m_skillSelectedForEdit != null && skill != null) {
				m_skillSelectedForEdit.setSubType(skill.getSubType());
                if (PTAbilitySet.isValidAbility(skill.getAbilityKey())) {
				    m_skillSelectedForEdit.setAbilityKey(skill.getAbilityKey());
                } else {
                    m_skillSelectedForEdit.setAbilityKey(
                            PTSkillSet.getDefaultAbilityKeyMap().get(m_skillSelectedForEdit.getSkillKey()));
                }
				m_skillSelectedForEdit.setRank(skill.getRank());
				m_skillSelectedForEdit.setMiscMod(skill.getMiscMod());
				m_skillSelectedForEdit.setClassSkill(skill.isClassSkill());
				
				m_skillRepo.update(m_skillSelectedForEdit);
				addNewSubSkills();
				updateSkillsList();
				
				m_skillSelectedForEdit = null;
			}		
			break;
		case PTCharacterSpellEditActivity.RESULT_DELETE:
			Log.v(TAG, "Deleting a skill subtype");
			if (m_skillSelectedForEdit != null && PTSkillSet.isSubtypedSkill(m_skillSelectedForEdit.getSkillKey()) 
				&& m_skillSet.hasMultipleOfSkill(m_skillSelectedForEdit.getSkillKey())) {
				if (m_skillRepo.delete(m_skillSelectedForEdit.getID())!= 0) {
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
        PTSkillsAdapter adapter;
        if (m_trainedFilterCheckBox.isChecked()) {
            adapter = new PTSkillsAdapter(getActivity(),
                    R.layout.character_skill_row, m_skillSet.getTrainedSkills(),
                    m_maxDex, m_abilitySet, getAppliedArmorCheckPenalty());
        } else {
            adapter = new PTSkillsAdapter(getActivity(),
                    R.layout.character_skill_row, m_skillSet.getSkills(),
                    m_maxDex, m_abilitySet, getAppliedArmorCheckPenalty());
        }
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
		m_skillSet = new PTSkillSet(validateSkills(m_skillRepo.querySet(getCurrentCharacterID())));
		addNewSubSkills();
		m_maxDex = m_armorRepo.getMaxDex(getCurrentCharacterID());
		m_armorCheckPenalty = m_armorRepo.getArmorCheckPenalty(getCurrentCharacterID());
		m_abilitySet = new PTAbilitySet(m_abilityRepo.querySet(getCurrentCharacterID()));
	}

    private List<PTSkill> validateSkills(List<PTSkill> skills) {
        for (PTSkill skill : skills) {
            if (!PTAbilitySet.isValidAbility(skill.getAbilityKey())) {
                skill.setAbilityKey(
                        PTSkillSet.getDefaultAbilityKeyMap().get(skill.getSkillKey()));
                m_skillRepo.update(skill);
            }
        }
        return skills;
    }
	
	private void addNewSubSkills() {
		PTSkill newSkill;
		List<Integer> constSubSkills = PTSkillSet.SUBTYPED_SKILLS();
		for (int key : constSubSkills) {
			if (m_skillSet.allSubSkillsUsed(key)) {
				newSkill = m_skillSet.addNewSubSkill(key);
				m_skillRepo.insert(newSkill);
			}
		}
	}
}
