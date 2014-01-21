package com.lateensoft.pathfinder.toolkit.views.character;

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
import android.widget.Button;
import android.widget.ListView;

public class PTCharacterSkillsFragment extends PTCharacterSheetFragment
		implements OnItemClickListener,
		android.view.View.OnClickListener {
	private static final String TAG = PTCharacterSkillsFragment.class.getSimpleName();

	private ListView m_skillsListView;

	private Button m_filterButton;
	private boolean m_isFiltered = true;

	private PTSkill m_skillSelectedForEdit;
	
	private PTSkillRepository m_skillRepo;
	private PTSkillSet m_skillSet;
	
	private PTArmorRepository m_armorRepo;
	private PTAbilityRepository m_abilityRepo;
	
	private PTAbilitySet m_abilitySet;
	private int m_maxDex = Integer.MAX_VALUE;

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

		m_filterButton = (Button) getRootView().findViewById(R.id.buttonFilter);
		m_filterButton.setOnClickListener(this);

		m_skillsListView = (ListView) getRootView()
				.findViewById(R.id.listViewCharacterSkills);
		m_skillsListView.setOnItemClickListener(this);

		return getRootView();
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (m_isFiltered) {
			m_skillSelectedForEdit = m_skillSet.getTrainedSkill(
					position);
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
		startActivityForResult(skillEditIntent, 0);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case Activity.RESULT_OK:
			PTSkill skill = data.getExtras().getParcelable(
					PTCharacterSkillEditActivity.INTENT_EXTRAS_KEY_EDITABLE_PARCELABLE);
			Log.v(TAG, "Edit skill OK: " + skill.getID());
			if (m_skillSelectedForEdit != null) {
				m_skillSelectedForEdit.setAbilityId(skill.getAbilityId());
				m_skillSelectedForEdit.setRank(skill.getRank());
				m_skillSelectedForEdit.setMiscMod(skill.getMiscMod());
				m_skillSelectedForEdit.setArmorCheckPenalty(skill.getArmorCheckPenalty());
				m_skillSelectedForEdit.setClassSkill(skill.isClassSkill());
				
				if (m_skillSelectedForEdit.getRank() <= 0) {
					// Prevent out of bounds exception from removing views
					// Not terribly costly since very few times will a player save a
					// skill with no rank
					setSkillsAdapter();
				} else {
					updateSkillsList();
				}
				
				m_skillRepo.update(m_skillSelectedForEdit);
				m_skillSelectedForEdit = null;
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
		if (m_isFiltered) {
			((PTSkillsAdapter) m_skillsListView.getAdapter())
					.updateList(m_skillSet.getTrainedSkills());
		} else {
			((PTSkillsAdapter) m_skillsListView.getAdapter())
					.updateList(m_skillSet.getSkills());
		}
	}

	private void setSkillsAdapter() {
		PTSkillsAdapter adapter = null;
		if (m_isFiltered) {
			adapter = new PTSkillsAdapter(getActivity(),
					R.layout.character_skill_row, m_skillSet.getTrainedSkills(),
					m_maxDex, m_abilitySet);
		} else {
			adapter = new PTSkillsAdapter(getActivity(),
					R.layout.character_skill_row, m_skillSet.getSkills(),
					m_maxDex, m_abilitySet);
		}
		m_skillsListView.setAdapter(adapter);
	}

	private void setFilterButtonText() {
		if (m_isFiltered) {
			m_filterButton.setText(R.string.skills_trained_filter);
		} else {
			m_filterButton.setText(R.string.skills_all_filter);
		}
	}

	@Override
	public void onClick(View arg0) {
		m_isFiltered = !m_isFiltered;
		updateFragmentUI();

	}

	@Override
	public void updateFragmentUI() {
		setFilterButtonText();
		setSkillsAdapter();
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
		m_skillSet = new PTSkillSet(m_skillRepo.querySet(getCurrentCharacterID()));
		
		m_maxDex = m_armorRepo.getMaxDex(getCurrentCharacterID());
		m_abilitySet = new PTAbilitySet(m_abilityRepo.querySet(getCurrentCharacterID()));
	}
}
