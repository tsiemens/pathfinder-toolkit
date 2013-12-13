package com.lateensoft.pathfinder.toolkit.character;

import com.lateensoft.pathfinder.toolkit.PTSharedMenu;
import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.stats.PTSkill;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class PTCharacterSkillsFragment extends PTCharacterSheetFragment
		implements OnClickListener, OnItemClickListener,
		android.view.View.OnClickListener {
	private static final String TAG = PTCharacterSkillsFragment.class.getSimpleName();

	private final int MENU_ITEM_AUTOFILL = 3;

	private ListView mSkillsListView;

	private Button mFilterButton;
	private boolean mIsFiltered = true;

	private Spinner mDialogSkillAbilityModSpinner;
	private Spinner mDialogSkillRankSpinner;
	private Spinner mDialogSkillMiscModSpinner;
	private Spinner mDialogSkillACPSpinner;
	private CheckBox mDialogClassSkillCheckBox;

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

	/**
	 * Shows a dialog to edit a skill.
	 * 
	 * @param item
	 */
	private void showSkillDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Set up dialog layout
		builder.setTitle("Edit Skill");

		LayoutInflater inflater = getActivity().getLayoutInflater();

		View dialogView = inflater.inflate(R.layout.character_skills_dialog,
				null);
		TextView skillInfoTextView = (TextView) dialogView
				.findViewById(R.id.tvDialogSkillName);
		mDialogSkillAbilityModSpinner = (Spinner) dialogView
				.findViewById(R.id.spinnerSkillDialogAbility);
		mDialogSkillRankSpinner = (Spinner) dialogView
				.findViewById(R.id.spinnerSkillDialogRank);
		mDialogSkillMiscModSpinner = (Spinner) dialogView
				.findViewById(R.id.spinnerSkillDialogMisc);
		mDialogSkillACPSpinner = (Spinner) dialogView
				.findViewById(R.id.spinnerSkillDialogACP);
		mDialogClassSkillCheckBox = (CheckBox) dialogView
				.findViewById(R.id.checkboxDialogClassSkill);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				getActivity(), R.array.skills_selectable_values_string,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(R.layout.spinner_plain);

		PTSkill editableSkill;
		if (mSkillSelectedForEdit != null) {
			editableSkill = mSkillSelectedForEdit;
		} else {
			return;
		}

		skillInfoTextView.setText(editableSkill.getName() + " ("
				+ editableSkill.getKeyAbility() + ")");

		setupDialogSpinner(mDialogSkillAbilityModSpinner, adapter,
				editableSkill.getAbilityMod());
		setupDialogSpinner(mDialogSkillRankSpinner, adapter,
				editableSkill.getRank());
		setupDialogSpinner(mDialogSkillMiscModSpinner, adapter,
				editableSkill.getMiscMod());
		setupDialogSpinner(mDialogSkillACPSpinner, adapter,
				editableSkill.getArmorCheckPenalty());

		mDialogClassSkillCheckBox.setChecked(editableSkill.isClassSkill());

		builder.setView(dialogView)
				.setPositiveButton(getString(R.string.ok_button_text), this)
				.setNegativeButton(getString(R.string.cancel_button_text), this);

		AlertDialog alert = builder.create();
		alert.show();
	}

	private void setupDialogSpinner(Spinner spinner,
			ArrayAdapter<CharSequence> adapter, int currentValue) {

		spinner.setAdapter(adapter);
		spinner.setSelection(currentValue + 10, true); // +10 is because at
														// position 0, is value
														// = -10
	}

	public void onClick(DialogInterface dialogInterface, int selection) {
		switch (selection) {
		// OK button tapped
		case DialogInterface.BUTTON_POSITIVE:
			mSkillSelectedForEdit.setAbilityMod(mDialogSkillAbilityModSpinner
					.getSelectedItemPosition() - 10);
			mSkillSelectedForEdit.setRank(mDialogSkillRankSpinner
					.getSelectedItemPosition() - 10);
			mSkillSelectedForEdit.setMiscMod(mDialogSkillMiscModSpinner
					.getSelectedItemPosition() - 10);
			mSkillSelectedForEdit.setArmorCheckPenalty(mDialogSkillACPSpinner
					.getSelectedItemPosition() - 10);
			mSkillSelectedForEdit.setClassSkill(mDialogClassSkillCheckBox
					.isChecked());
			break;
		case DialogInterface.BUTTON_NEGATIVE:
			// Do nothing
			break;
		}

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

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (mIsFiltered) {
			mSkillSelectedForEdit = mCharacter.getSkillSet().getTrainedSkill(
					position);
		} else {
			mSkillSelectedForEdit = mCharacter.getSkillSet().getSkill(position);
		}
		showSkillDialog();

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
