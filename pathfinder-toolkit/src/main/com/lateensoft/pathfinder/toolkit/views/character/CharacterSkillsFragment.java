package com.lateensoft.pathfinder.toolkit.views.character;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.adapters.character.SkillListAdapter;
import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.db.dao.set.AbilitySetDAO;
import com.lateensoft.pathfinder.toolkit.db.dao.set.SkillSetDAO;
import com.lateensoft.pathfinder.toolkit.db.dao.table.ArmorDAO;
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

    private ListView skillsListView;

    private CheckBox applyACPCheckBox;
    private CheckBox trainedFilterCheckBox;

    private Skill skillSelectedForEdit;
    
    private SkillSetDAO skillSetDao;

    private SkillSet skillSet;
    
    private ArmorDAO armorDao;
    private AbilitySetDAO abilitySetDao;
    
    private AbilitySet abilitySet;
    private int maxDex = Integer.MAX_VALUE;
    private int armorCheckPenalty = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        skillSetDao = new SkillSetDAO(getContext());
        armorDao = new ArmorDAO(getContext());
        abilitySetDao = new AbilitySetDAO(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        setRootView(inflater.inflate(
                R.layout.character_skills_fragment, container, false));

        applyACPCheckBox = (CheckBox) getRootView().findViewById(R.id.checkboxApplyACP);
        applyACPCheckBox.setChecked(false);
        applyACPCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView == applyACPCheckBox) {
                    ((SkillListAdapter) skillsListView.getAdapter())
                            .setArmorCheckPenalty(isChecked ? armorCheckPenalty : 0);
                }
            }
        });
        
        trainedFilterCheckBox = (CheckBox) getRootView().findViewById(R.id.checkboxFilterTrained);
        trainedFilterCheckBox.setChecked(true);
        trainedFilterCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateFragmentUI();
            }
        });

        skillsListView = (ListView) getRootView()
                .findViewById(R.id.listViewCharacterSkills);
        skillsListView.setOnItemClickListener(this);

        return getRootView();
    }

    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        if (trainedFilterCheckBox.isChecked()) {
            skillSelectedForEdit = skillSet.getTrainedSkill(position);
        } else {
            skillSelectedForEdit = skillSet.getSkillByIndex(position);
        }
        showSkillEditor(skillSelectedForEdit);
    }
    
    private void showSkillEditor(Skill skill) {
        Intent skillEditIntent = new Intent(getContext(),
                SkillEditActivity.class);
        skillEditIntent.putExtra(
                SkillEditActivity.INTENT_EXTRAS_KEY_EDITABLE_PARCELABLE,skill);
        skillEditIntent.putExtra(SkillEditActivity.INTENT_EXTRAS_KEY_SKILL_DELETABLE_BOOLEAN,
                (skill.canBeSubTyped() && skillSet.hasMultipleOfSkill(skill.getType())));

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
            if (skillSelectedForEdit != null && skill != null) {
                skillSelectedForEdit.setSubType(skill.getSubType());
                skillSelectedForEdit.setAbility(skill.getAbility());
                skillSelectedForEdit.setRank(skill.getRank());
                skillSelectedForEdit.setMiscMod(skill.getMiscMod());
                skillSelectedForEdit.setClassSkill(skill.isClassSkill());

                try {
                    skillSetDao.getComponentDAO().update(getCurrentCharacterID(), skillSelectedForEdit);
                    addNewSubSkills();
                    updateSkillsList();
                } catch (DataAccessException e) {
                    Log.e(TAG, "Failed to update skill " + skill.getId(), e);
                }

                skillSelectedForEdit = null;
            }        
            break;
        case SpellEditActivity.RESULT_DELETE:
            Log.v(TAG, "Deleting a skill subtype");
            if (skillSelectedForEdit != null && skillSelectedForEdit.canBeSubTyped()
                && skillSet.hasMultipleOfSkill(skillSelectedForEdit.getType())) {
                try {
                    skillSetDao.getComponentDAO().remove(skillSelectedForEdit);
                    skillSet.deleteSkill(skillSelectedForEdit);
                } catch (DataAccessException e) {
                    Log.e(TAG, "Failed to update skill " + skillSelectedForEdit.getId(), e);
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
        if (skillSet == null || abilitySet == null) return;
        List<Skill> skills = trainedFilterCheckBox.isChecked() ?
                skillSet.getTrainedSkills() : skillSet.getSkills();
        SkillListAdapter adapter = new SkillListAdapter(getContext(),
                R.layout.character_skill_row, skills,
                maxDex, abilitySet, getAppliedArmorCheckPenalty());
        skillsListView.setAdapter(adapter);
    }
    
    private int getAppliedArmorCheckPenalty() {
        return applyACPCheckBox.isChecked() ? armorCheckPenalty : 0;
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
        long characterId = getCurrentCharacterID();
        skillSet = skillSetDao.findSet(characterId);

        addNewSubSkills();
        maxDex = armorDao.getMaxDexForCharacter(characterId);
        armorCheckPenalty = armorDao.getArmorCheckPenaltyForCharacter(characterId);
        abilitySet = abilitySetDao.findSet(characterId);
    }
    
    private void addNewSubSkills() {
        Skill newSkill;
        for (SkillType type : SkillType.values()) {
            if (type.canBeSubTyped() && skillSet.allSubSkillsUsed(type)) {
                try {
                    newSkill = skillSet.addNewSubSkill(type);
                    skillSetDao.getComponentDAO().add(getCurrentCharacterID(), newSkill);
                } catch (DataAccessException e) {
                    Log.e(TAG, "Failed to add skill ", e);
                }
            }
        }
    }
}
