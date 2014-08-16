package com.lateensoft.pathfinder.toolkit.views.character;

import android.content.Intent;
import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.adapters.character.SkillListAdapter;
import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.db.dao.set.AbilitySetDAO;
import com.lateensoft.pathfinder.toolkit.db.dao.set.SkillSetDAO;
import com.lateensoft.pathfinder.toolkit.db.dao.table.ArmorDAO;
import com.lateensoft.pathfinder.toolkit.db.dao.table.SkillDAO;
import com.lateensoft.pathfinder.toolkit.model.character.stats.AbilitySet;
import com.lateensoft.pathfinder.toolkit.model.character.stats.Skill;
import com.lateensoft.pathfinder.toolkit.model.character.stats.SkillSet;

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
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CharacterSkillsFragment extends ParcelableListFragment<Skill, SkillDAO> {
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
        applyACPCheckBox.setOnCheckedChangeListener(acpCheckboxListener);

        trainedFilterCheckBox = (CheckBox) getRootView().findViewById(R.id.checkboxFilterTrained);
        trainedFilterCheckBox.setChecked(true);
        trainedFilterCheckBox.setOnCheckedChangeListener(trainedFilterCheckboxListener);

        skillsListView = (ListView) getRootView()
                .findViewById(R.id.listViewCharacterSkills);
        skillsListView.setOnItemClickListener(listClickListener);

        return getRootView();
    }

    private OnCheckedChangeListener acpCheckboxListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            ((SkillListAdapter) skillsListView.getAdapter())
                    .setArmorCheckPenalty(isChecked ? armorCheckPenalty : 0);
        }
    };

    private OnCheckedChangeListener trainedFilterCheckboxListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            updateFragmentUI();
        }
    };

    private OnItemClickListener listClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (trainedFilterCheckBox.isChecked()) {
                skillSelectedForEdit = skillSet.getTrainedSkill(position);
            } else {
                skillSelectedForEdit = skillSet.getSkillByIndex(position);
            }
            showEditorActivity(skillSelectedForEdit);
        }
    };

    @Override
    protected Class<? extends ParcelableEditorActivity> getParcelableEditorActivity() {
        return SkillEditActivity.class;
    }

    @Override
    protected void addCustomExtrasToEditorActivityIntent(Intent intent, Skill toEdit) {
        intent.putExtra(SkillEditActivity.INTENT_EXTRAS_KEY_SKILL_DELETABLE_BOOLEAN,
                (toEdit.canBeSubTyped() && skillSet.hasMultipleOfSkill(toEdit.getType())));
    }

    @Override
    protected EditAction getActionForResult(@NotNull Skill result) {
        return skillSelectedForEdit != null ? EditAction.UPDATE : EditAction.NONE;
    }

    @Override
    protected SkillDAO getDAO() {
        return skillSetDao.getComponentDAO();
    }

    @Override
    protected void updateModel(EditAction action, Skill updatedParcelable) {
        skillSelectedForEdit.setSubType(updatedParcelable.getSubType());
        skillSelectedForEdit.setAbility(updatedParcelable.getAbility());
        skillSelectedForEdit.setRank(updatedParcelable.getRank());
        skillSelectedForEdit.setMiscMod(updatedParcelable.getMiscMod());
        skillSelectedForEdit.setClassSkill(updatedParcelable.isClassSkill());
        addNewSubSkills();
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

    @Override
    protected Skill getObjectMarkedForDeletion() {
        if (skillSelectedForEdit != null && skillSelectedForEdit.canBeSubTyped()
                && skillSet.hasMultipleOfSkill(skillSelectedForEdit.getType())) {
            return skillSelectedForEdit;
        } else {
            return null;
        }
    }

    @Override
    protected void removeFromModel(Skill toRemove) {
        skillSet.deleteSkill(toRemove);
        addNewSubSkills();
    }

    @Override
    public void updateFragmentUI() {
        updateSkillsList();
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
    public String getFragmentTitle() {
        return getString(R.string.tab_character_skills);
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
}
