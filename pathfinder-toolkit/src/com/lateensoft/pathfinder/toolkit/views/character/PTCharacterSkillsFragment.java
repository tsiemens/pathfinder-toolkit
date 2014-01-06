package com.lateensoft.pathfinder.toolkit.views.character;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.adapters.character.PTSkillsAdapter;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTSkill;

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

	private ListView mSkillsListView;

	private Button mFilterButton;
	private boolean mIsFiltered = true;

	private PTSkill mSkillSelectedForEdit;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		setRootView(inflater.inflate(
				R.layout.character_skills_fragment, container, false));

		mFilterButton = (Button) getRootView().findViewById(R.id.buttonFilter);
		mFilterButton.setOnClickListener(this);

		mSkillsListView = (ListView) getRootView()
				.findViewById(R.id.listViewCharacterSkills);
		mSkillsListView.setOnItemClickListener(this);
		updateFragmentUI();

		return getRootView();
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (mIsFiltered) {
			mSkillSelectedForEdit = mCharacter.getSkillSet().getTrainedSkill(
					position);
		} else {
			mSkillSelectedForEdit = mCharacter.getSkillSet().getSkill(position);
		}
		showSkillEditor(mSkillSelectedForEdit);
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
			Log.v(TAG, "Edit skill OK: " + skill.getName());
			if (mSkillSelectedForEdit != null) {
				mSkillSelectedForEdit.setAbilityMod(skill.getAbilityMod());
				mSkillSelectedForEdit.setRank(skill.getRank());
				mSkillSelectedForEdit.setMiscMod(skill.getMiscMod());
				mSkillSelectedForEdit.setArmorCheckPenalty(skill.getArmorCheckPenalty());
				mSkillSelectedForEdit.setClassSkill(skill.isClassSkill());
				
				if (mSkillSelectedForEdit.getRank() <= 0) {
					// Prevent out of bounds exception from removing views
					// Not terribly costly since very few times will a player save a
					// skill with no rank
					setSkillsAdapter();
				} else {
					updateSkillsList();
				}
				
				mSkillSelectedForEdit = null;
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
		if (mIsFiltered) {
			((PTSkillsAdapter) mSkillsListView.getAdapter())
					.updateList(mCharacter.getSkillSet().getTrainedSkills());
		} else {
			((PTSkillsAdapter) mSkillsListView.getAdapter())
					.updateList(mCharacter.getSkillSet().getSkills());
		}
	}

	private void setSkillsAdapter() {
		PTSkillsAdapter adapter = null;
		if (mIsFiltered) {
			adapter = new PTSkillsAdapter(getActivity(),
					R.layout.character_skill_row, mCharacter.getSkillSet()
							.getTrainedSkills());
		} else {
			adapter = new PTSkillsAdapter(getActivity(),
					R.layout.character_skill_row, mCharacter.getSkillSet()
							.getSkills());
		}
		mSkillsListView.setAdapter(adapter);
	}

	private void setFilterButtonText() {
		if (mIsFiltered) {
			mFilterButton.setText(R.string.skills_trained_filter);
		} else {
			mFilterButton.setText(R.string.skills_all_filter);
		}
	}

	@Override
	public void onClick(View arg0) {
		mIsFiltered = !mIsFiltered;
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
}
