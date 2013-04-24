package com.lateensoft.pathfinder.toolkit.character;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.items.PTArmor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

public class PTArmorFragment extends PTCharacterSheetFragment implements
	OnClickListener, OnItemClickListener, android.content.DialogInterface.OnClickListener{
	
	static final String TAG = "PTArmorFragment";
	private static final int AC_SPINNER_OFFSET = 20;
	private static final int ASF_INCREMENT = 5;
	private static final int SPEED_INCREMENT = 5;
	private ListView mListView;
	int mArmorSelectedForEdit;
	private Button mAddButton;
	
	private Spinner mDialogACSpinner;
	private Spinner mDialogACPSpinner;
	private Spinner mDialogSizeSpinner;
	private Spinner mDialogSpeedSpinner;
	private Spinner mDialogMaxDexSpinner;
	private Spinner mDialogASFSpinner;
	private EditText mDialogWeightET;
	private EditText mDialogNameET;
	private EditText mDialogSpecialPropertiesET;
	
	private ViewGroup mContainer;

	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG, "Starting onCreateView");
		
		mContainer = container;
		
		View fragmentView = inflater.inflate(R.layout.character_armor_fragment,
				container, false);
		
		mAddButton = (Button) fragmentView.findViewById(R.id.buttonAddArmor);
		mAddButton.setOnClickListener(this);
		
		mListView = new ListView(container.getContext());
		mListView = (ListView) fragmentView.findViewById(R.id.lvArmor);
		refreshArmorListView();

		mListView.setOnItemClickListener(this);
		
		Log.v(TAG, "Finishing onCreateView");
		return fragmentView;
	}
	
	private void refreshArmorListView() {
		PTArmorAdapter adapter = new PTArmorAdapter(mContainer.getContext(),
				R.layout.character_armor_row,
				mCharacter.getInventory().getArmorArray());
		
		Log.v(TAG, "Called refreshArmorListView");
		
		mListView.setAdapter(adapter);
	}
	
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Log.v(TAG, "Item clicked: " + position);
		mArmorSelectedForEdit = position;
		showArmorDialog(mCharacter.getInventory().getArmor(position));
	}

	public void onClick(View v) {
		Log.v(TAG, "Add button clicked");
		mArmorSelectedForEdit = -1;
		showArmorDialog(null);
	}

	private void showArmorDialog(PTArmor armor) {
		if(armor == null) {
			armor = new PTArmor();
		}
	
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		LayoutInflater inflater = getActivity().getLayoutInflater();
		
		View dialogView = inflater.inflate(R.layout.character_armor_dialog, null);
		mDialogACSpinner = (Spinner) dialogView.findViewById(R.id.spArmorClass);
		mDialogACPSpinner = (Spinner) dialogView.findViewById(R.id.spArmorCheckPenalty);
		mDialogSizeSpinner = (Spinner) dialogView.findViewById(R.id.spArmorSize);
		mDialogSpeedSpinner = (Spinner) dialogView.findViewById(R.id.spArmorSpeed);
		mDialogASFSpinner = (Spinner) dialogView.findViewById(R.id.spArmorSpellFailure);
		mDialogWeightET = (EditText) dialogView.findViewById(R.id.etArmorWeight);
		mDialogSpecialPropertiesET = (EditText) dialogView.findViewById(
				R.id.etArmorSpecialProperties);
		mDialogNameET = (EditText) dialogView.findViewById(R.id.armorName);
		mDialogMaxDexSpinner = (Spinner) dialogView.findViewById(R.id.spArmorMaxDex);
		
		setupSpinner(mDialogACSpinner, R.array.ac_spinner_options);
		setupSpinner(mDialogACPSpinner, R.array.acp_spinner_options);
		setupSpinner(mDialogSizeSpinner, R.array.size_spinner_options);
		setupSpinner(mDialogSpeedSpinner, R.array.speed_spinner_options);
		setupSpinner(mDialogMaxDexSpinner, R.array.acp_spinner_options);
		setupSpinner(mDialogASFSpinner, R.array.armor_spell_fail_options);
		
		if(mArmorSelectedForEdit < 0) {
			builder.setTitle(R.string.new_armor_title);
			mDialogACSpinner.setSelection(AC_SPINNER_OFFSET);
		} else {
			builder.setTitle(armor.getName()).setNeutralButton(R.string.delete_button_text,
					this);
			mDialogNameET.setText(mCharacter.getInventory()
					.getArmor(mArmorSelectedForEdit).getName());
			mDialogACSpinner.setSelection((mCharacter.getInventory()
					.getArmor(mArmorSelectedForEdit).getACBonus()) + AC_SPINNER_OFFSET);
			mDialogACPSpinner.setSelection(mCharacter.getInventory()
					.getArmor(mArmorSelectedForEdit).getCheckPen());
			mDialogSizeSpinner.setSelection(mCharacter.getInventory()
					.getArmor(mArmorSelectedForEdit).getSizeInt());
			mDialogMaxDexSpinner.setSelection(armor.getMaxDex());
			mDialogSpeedSpinner.setSelection(armor.getSpeed()/5);
			mDialogASFSpinner.setSelection(armor.getSpellFail() / ASF_INCREMENT);
			mDialogWeightET.setText(Integer.toString(armor.getWeight()));
			mDialogSpecialPropertiesET.setText(mCharacter.getInventory().getArmor(mArmorSelectedForEdit).getSpecialProperties());
		}
		
		builder.setView(dialogView)
			.setPositiveButton(R.string.ok_button_text, this)
			.setNegativeButton(R.string.cancel_button_text, this);
		
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	public void onClick(DialogInterface dialogInterface, int selection) {
		PTArmor armor = getArmorFromDialog();
		
		switch (selection) {
		case DialogInterface.BUTTON_POSITIVE:
			Log.v(TAG, "Add.edit armor OK: " + mDialogNameET.getText());
			if(mArmorSelectedForEdit < 0) {
				Log.v(TAG, "Adding an armor");
				if(armor != null) {
					mCharacter.getInventory().addArmor(armor);
					refreshArmorListView(); 
				}
			} else {
				Log.v(TAG, "Editing an armor");
				
				mCharacter.getInventory().setArmor(armor, mArmorSelectedForEdit);
				refreshArmorListView();
			}
			
			break;
		
		case DialogInterface.BUTTON_NEUTRAL:
			Log.v(TAG, "Deleting an armor");
			mCharacter.getInventory().deleteArmor(mArmorSelectedForEdit);
			refreshArmorListView();
			break;
		
		case DialogInterface.BUTTON_NEGATIVE:
			break;
		
		default:
			break;

		}
	}

	private PTArmor getArmorFromDialog() {
		String name = new String(mDialogNameET.getText().toString());
		if(name == null || name.contentEquals("")) {
			return null;
		}
		
		int spellFail;
		int weight;
		
		String specialProperties = new String(mDialogSpecialPropertiesET.getText().toString());
		int speed = mDialogSpeedSpinner.getSelectedItemPosition() * SPEED_INCREMENT;
		
		spellFail = mDialogASFSpinner.getSelectedItemPosition() * ASF_INCREMENT;
		
		try {
			weight = Integer.parseInt(mDialogWeightET.getText().toString());
		} catch (NumberFormatException e) {
			weight = 1;
		}
		
		int size = mDialogSizeSpinner.getSelectedItemPosition();
		int ac = mDialogACSpinner.getSelectedItemPosition() - AC_SPINNER_OFFSET;
		int acp = mDialogACPSpinner.getSelectedItemPosition();
		int maxDex = mDialogMaxDexSpinner.getSelectedItemPosition();
		
		PTArmor armor = new PTArmor();
		armor.setName(name);
		armor.setSpeed(speed);
		armor.setSpecialProperties(specialProperties);
		armor.setSpellFail(spellFail);
		armor.setWeight(weight);
		armor.setSize(size);
		armor.setACBonus(ac);
		armor.setCheckPen(acp);
		armor.setMaxDex(maxDex);
		return armor;
	}

	private void setupSpinner(Spinner spinner, int optionResourceId) {
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
				optionResourceId, android.R.layout.simple_spinner_item);
		
		adapter.setDropDownViewResource(R.layout.spinner_plain);
		spinner.setAdapter(adapter);
	}
}
