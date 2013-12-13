package com.lateensoft.pathfinder.toolkit.character;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.items.PTArmor;
import com.lateensoft.pathfinder.toolkit.items.PTWeapon;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

public class PTCharacterWeaponsFragment extends PTCharacterSheetFragment implements
OnClickListener, OnItemClickListener, android.content.DialogInterface.OnClickListener{
	
	private static final String TAG = PTCharacterWeaponsFragment.class.getSimpleName();
	private static final int ATK_BONUS_OFFSET = 10;
	private ListView mListView;
	int mWeaponSelectedForEdit;
	private Button mAddButton;
	
	private EditText mDialogNameET;
	private Spinner mDialogHighestAtkSpinner;
	private Spinner mDialogAmmunitionSpinner;
	private EditText mDialogDamageET;
	private EditText mDialogCriticalET;
	private Spinner mDialogTypeSpinner;
	private Spinner mDialogRangeSpinner;
	private Spinner mDialogSizeSpinner;
	private EditText mDialogWeightET;
	private EditText mDialogSpecialPropertiesET;
	private View mDialogView;
	private OnTouchListener mSpinnerOnTouchListener;	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG, "Starting onCreateView");
		
		setRootView(inflater.inflate(R.layout.character_weapon_fragment,
				container, false));
		
		mAddButton = (Button) getRootView().findViewById(R.id.buttonAddWeapon);
		mAddButton.setOnClickListener(this);
		
		mListView = new ListView(container.getContext());
		mListView = (ListView) getRootView().findViewById(R.id.listViewWeapons);
		refreshWeaponsListView();

		mListView.setOnItemClickListener(this);
		
		Log.v(TAG, "Finishing onCreateView");
		return getRootView();
	}

	private void refreshWeaponsListView() {
		PTWeaponAdapter adapter = new PTWeaponAdapter(getActivity(),
				R.layout.character_weapon_row,
				mCharacter.getInventory().getWeaponArray());
		
		Log.v(TAG, "Called refreshWeaponsListView");
		
		mListView.setAdapter(adapter);
	}
	
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Log.v(TAG, "Item clicked: " + position);
		mWeaponSelectedForEdit = position;
		showWeaponDialog(mCharacter.getInventory().getWeapon(position));
	}



	public void onClick(View v) {
		Log.v(TAG, "Add button clicked");
		mWeaponSelectedForEdit = -1;
		showWeaponDialog(null);
	}
	
	private void showWeaponDialog(PTWeapon weapon) {
		if(weapon == null)
			weapon = new PTWeapon();
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View dialogView = inflater.inflate(R.layout.character_weapon_dialog, null);
		
		mDialogNameET = (EditText) dialogView.findViewById(R.id.weaponDialogName);
		mDialogHighestAtkSpinner = (Spinner) dialogView.findViewById(R.id.weaponDialogHighestAtk);
		mDialogAmmunitionSpinner = (Spinner) dialogView.findViewById(R.id.weaponDialogAmmo);
		mDialogDamageET = (EditText) dialogView.findViewById(R.id.weaponDialogDamage);
		mDialogCriticalET = (EditText) dialogView.findViewById(R.id.weaponDialogCrit);
		mDialogTypeSpinner = (Spinner) dialogView.findViewById(R.id.weaponDialogType);
		mDialogRangeSpinner = (Spinner) dialogView.findViewById(R.id.weaponDialogRange);
		mDialogSizeSpinner = (Spinner) dialogView.findViewById(R.id.weaponDialogSize);
		mDialogWeightET = (EditText) dialogView.findViewById(R.id.weaponDialogWeight);
		mDialogSpecialPropertiesET = (EditText) dialogView
				.findViewById(R.id.weaponDialogSpecialProperties);
		
		setupSpinner(mDialogHighestAtkSpinner, R.array.weapon_attack_bonus_options);
		setupSpinner(mDialogAmmunitionSpinner, R.array.selectable_values_string);
		setupSpinner(mDialogTypeSpinner, R.array.weapon_type_options);
		setupSpinner(mDialogRangeSpinner, R.array.weapon_range_options);
		setupSpinner(mDialogSizeSpinner, R.array.size_spinner_options);

		if(mWeaponSelectedForEdit < 0) {
			builder.setTitle(R.string.new_weapon_title);
			mDialogHighestAtkSpinner.setSelection(ATK_BONUS_OFFSET);
		} else {
			builder.setTitle(weapon.getName()).setNeutralButton(R.string.delete_button_text,
					this);
			mDialogNameET.setText(weapon.getName());
			mDialogHighestAtkSpinner.setSelection(weapon.getTotalAttackBonus() + ATK_BONUS_OFFSET);
			mDialogAmmunitionSpinner.setSelection(weapon.getAmmunition());
			mDialogDamageET.setText(weapon.getDamage());
			mDialogCriticalET.setText(weapon.getCritical());
			mDialogTypeSpinner.setSelection(weapon.getTypeInt(getActivity()));
			mDialogRangeSpinner.setSelection(weapon.getRange()/5);
			mDialogSizeSpinner.setSelection(weapon.getSizeInt());
			mDialogWeightET.setText(Double.toString(weapon.getWeight()));
			mDialogSpecialPropertiesET.setText(weapon.getSpecialProperties());
			
		}
		
		builder.setView(dialogView)
		.setPositiveButton(R.string.ok_button_text, this)
		.setNegativeButton(R.string.cancel_button_text, this);
	
	AlertDialog alert = builder.create();
	mDialogView = dialogView;
	alert.show();
	}
	
	public void onClick(DialogInterface dialogInterface, int selection) {
		PTWeapon weapon = getWeaponFromDialog();
		
		switch (selection) {
		case DialogInterface.BUTTON_POSITIVE:
			Log.v(TAG, "Add.edit armor OK: " + mDialogNameET.getText());
			if(mWeaponSelectedForEdit < 0) {
				Log.v(TAG, "Adding a weapon");
				if(weapon != null) {
					mCharacter.getInventory().addWeapon(weapon);
					refreshWeaponsListView(); 
				}
			} else {
				Log.v(TAG, "Editing a weapon");
				
				mCharacter.getInventory().setWeapon(weapon, mWeaponSelectedForEdit);
				refreshWeaponsListView();
			}
			
			break;
		
		case DialogInterface.BUTTON_NEUTRAL:
			Log.v(TAG, "Deleting a weapon");
			mCharacter.getInventory().deleteWeapon(mWeaponSelectedForEdit);
			refreshWeaponsListView();
			break;
		
		case DialogInterface.BUTTON_NEGATIVE:
			break;
		
		default:
			break;

		}
	}

	private PTWeapon getWeaponFromDialog() {
		String name = new  String(mDialogNameET.getText().toString());
		if(name == null || name.contentEquals("")) {
			return null;
		}
		
		int attack = mDialogHighestAtkSpinner.getSelectedItemPosition() - ATK_BONUS_OFFSET;;
		int weight;
		try {
			weight = Integer.parseInt(mDialogWeightET.getText().toString());
		} catch (NumberFormatException e) {
			weight = 1;
		}
		
		int range = mDialogRangeSpinner.getSelectedItemPosition()*5;
		int ammo = mDialogAmmunitionSpinner.getSelectedItemPosition();
		String damage = new String(mDialogDamageET.getText().toString());
		String critical = new String(mDialogCriticalET.getText().toString());
		String specialProperties = new String(mDialogSpecialPropertiesET.getText().toString());
		String type = new String(getStringByResourceAndIndex(R.array.weapon_type_options,
				mDialogTypeSpinner.getSelectedItemPosition()));
		String size = new String(getStringByResourceAndIndex(R.array.size_spinner_options,
				mDialogSizeSpinner.getSelectedItemPosition()));
		
		
		PTWeapon weapon = new PTWeapon();
		weapon.setName(name);
		weapon.setTotalAttackBonus(attack);
		weapon.setWeight(weight);
		weapon.setRange(range);
		weapon.setAmmunition(ammo);
		weapon.setDamage(damage);
		weapon.setCritical(critical);
		weapon.setSpecialProperties(specialProperties);
		weapon.setType(type);
		weapon.setSize(size);
		
		return weapon;
	}
	
	private void setupSpinner(Spinner spinner, int optionResourceId) {
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
				optionResourceId, android.R.layout.simple_spinner_item);
		
		if(mSpinnerOnTouchListener == null) {
			mSpinnerOnTouchListener = new OnTouchListener() {
	            @Override
	            public boolean onTouch(View v, MotionEvent event) {
	                closeKeyboard();
	                return false;
	            }
			};
		}
		
		adapter.setDropDownViewResource(R.layout.spinner_plain);
		spinner.setAdapter(adapter);
		
		spinner.setOnTouchListener(mSpinnerOnTouchListener);
	}
	
	private void closeKeyboard() {
		InputMethodManager iMM = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		iMM.hideSoftInputFromWindow(mDialogView.getWindowToken(), 0);
	}
	
	private String getStringByResourceAndIndex(int resourceId, int position) {
		Resources r = getActivity().getResources();
		String[] resource = r.getStringArray(resourceId);
		return resource[position];
	}

	@Override
	public void updateFragmentUI() {
		refreshWeaponsListView();
	}
	
	@Override
	public String getFragmentTitle() {
		return getString(R.string.tab_character_weapons);
	}
}
