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
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class PTCharacterSkillsFragment extends PTCharacterSheetFragment implements OnClickListener, OnItemClickListener{
	final String TAG = "PTCharacterSkillsFragment";
	
	private final int MENU_ITEM_AUTOFILL = 3;
	
	private ListView mSkillsListView;
	
	private Spinner mDialogSkillAbilityModSpinner;
	private Spinner mDialogSkillRankSpinner;
	private Spinner mDialogSkillMiscModSpinner;
	private Spinner mDialogSkillACPSpinner;
	private CheckBox mDialogClassSkillCheckBox;
	
	private int mSkillSeletedForEdit;
	
	private ViewGroup mContainer;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		mContainer = container;
		
		View fragmentView = inflater.inflate(R.layout.character_skills_fragment, container, false);
		
		mSkillsListView = (ListView) fragmentView.findViewById(R.id.listViewCharacterSkills);
		PTSkillsAdapter adapter = new PTSkillsAdapter(mContainer.getContext(), R.layout.character_skill_row, mCharacter.getSkillSet());
		mSkillsListView.setAdapter(adapter);
		mSkillsListView.setOnItemClickListener(this);
	
		return fragmentView;
	}

	/**
	 * Shows a dialog to edit a skill.
	 * @param item
	 */
	private void showSkillDialog() {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		//Set up dialog layout
		builder.setTitle("Edit Skill");
		
		LayoutInflater inflater = getActivity().getLayoutInflater();

		View dialogView = inflater.inflate(R.layout.character_skills_dialog, null);
		TextView skillInfoTextView = (TextView) dialogView.findViewById(R.id.tvDialogSkillName);
		mDialogSkillAbilityModSpinner = (Spinner) dialogView.findViewById(R.id.spinnerSkillDialogAbility);
		mDialogSkillRankSpinner = (Spinner) dialogView.findViewById(R.id.spinnerSkillDialogRank);
		mDialogSkillMiscModSpinner = (Spinner) dialogView.findViewById(R.id.spinnerSkillDialogMisc);
		mDialogSkillACPSpinner = (Spinner) dialogView.findViewById(R.id.spinnerSkillDialogACP);
		mDialogClassSkillCheckBox = (CheckBox) dialogView.findViewById(R.id.checkboxDialogClassSkill);
		
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				getActivity(), R.array.skills_selectable_values_string,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(R.layout.spinner_plain);
		
		PTSkill editableSkill = mCharacter.getSkillSet().getSkill(mSkillSeletedForEdit);
		
		skillInfoTextView.setText(editableSkill.getName()+" ("+editableSkill.getKeyAbility()+")");
		
		setupDialogSpinner(mDialogSkillAbilityModSpinner, adapter, editableSkill.getAbilityMod());
		setupDialogSpinner(mDialogSkillRankSpinner, adapter, editableSkill.getRank());
		setupDialogSpinner(mDialogSkillMiscModSpinner, adapter, editableSkill.getMiscMod());
		setupDialogSpinner(mDialogSkillACPSpinner, adapter, editableSkill.getArmorCheckPenalty());
		
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
		//OK button tapped
		case DialogInterface.BUTTON_POSITIVE:
			mCharacter.getSkillSet().getSkill(mSkillSeletedForEdit).setAbilityMod(mDialogSkillAbilityModSpinner.getSelectedItemPosition()-10);
			mCharacter.getSkillSet().getSkill(mSkillSeletedForEdit).setRank(mDialogSkillRankSpinner.getSelectedItemPosition()-10);
			mCharacter.getSkillSet().getSkill(mSkillSeletedForEdit).setMiscMod(mDialogSkillMiscModSpinner.getSelectedItemPosition()-10);
			mCharacter.getSkillSet().getSkill(mSkillSeletedForEdit).setArmorCheckPenalty(mDialogSkillACPSpinner.getSelectedItemPosition()-10);
			mCharacter.getSkillSet().getSkill(mSkillSeletedForEdit).setClassSkill(mDialogClassSkillCheckBox.isChecked());
			break;
		case DialogInterface.BUTTON_NEGATIVE:
			//Do nothing
			break;
		}
		
		((PTSkillsAdapter)mSkillsListView.getAdapter()).updateList(mCharacter.getSkillSet());
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		mSkillSeletedForEdit = position;
		showSkillDialog();
		
	}

	@Override
	public void onResume() {
		super.onResume();
		((PTSkillsAdapter)mSkillsListView.getAdapter()).updateList(mCharacter.getSkillSet());
	}
	
}
