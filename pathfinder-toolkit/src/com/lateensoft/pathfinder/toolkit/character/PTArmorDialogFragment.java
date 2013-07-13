package com.lateensoft.pathfinder.toolkit.character;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.items.PTArmor;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class PTArmorDialogFragment extends DialogFragment 
	implements OnClickListener {
	
	private static final String TAG = PTArmorDialogFragment.class.getSimpleName();
	private static final int AC_SPINNER_OFFSET = 20;
	private static final int ASF_INCREMENT = 5;
	private static final int SPEED_INCREMENT = 5;
	
	private int mArmorSelectedForEdit;
	private PTArmor mArmor;
	private OnArmorDialogReturnListener mListener;
	
	private Spinner mDialogACSpinner;
	private Spinner mDialogACPSpinner;
	private Spinner mDialogSizeSpinner;
	private Spinner mDialogSpeedSpinner;
	private Spinner mDialogMaxDexSpinner;
	private Spinner mDialogASFSpinner;
	private EditText mDialogWeightET;
	private EditText mDialogNameET;
	private EditText mDialogSpecialPropertiesET;
	private OnTouchListener mSpinnerOnTouchListener;
	
	public enum ArmorAction {
		ADD_EDIT, DELETE, CANCEL
	}
	
	public class ArmorReturn {
		public PTArmor armor;
		public ArmorAction action;
	}
	
	public PTArmorDialogFragment() {
	}
	
	public static PTArmorDialogFragment newInstance(PTArmor armor) {
		PTArmorDialogFragment fragment = new PTArmorDialogFragment();
		
		Bundle args = new Bundle();
		args.putParcelable("armor", armor);
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnArmorDialogReturnListener) activity;
		}
		catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() 
					+ " must implement OnArmorDialogReturnListener");
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mArmor = getArguments().getParcelable("armor");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle SavedInstanceState) {
		View view = inflater.inflate(R.layout.character_armor_dialog, null);
		mDialogACSpinner = (Spinner) view.findViewById(R.id.spArmorClass);
		mDialogACPSpinner = (Spinner) view.findViewById(R.id.spArmorCheckPenalty);
		mDialogSizeSpinner = (Spinner) view.findViewById(R.id.spArmorSize);
		mDialogSpeedSpinner = (Spinner) view.findViewById(R.id.spArmorSpeed);
		mDialogASFSpinner = (Spinner) view.findViewById(R.id.spArmorSpellFailure);
		mDialogWeightET = (EditText) view.findViewById(R.id.etArmorWeight);
		mDialogSpecialPropertiesET = (EditText) view.findViewById(
				R.id.etArmorSpecialProperties);
		mDialogNameET = (EditText) view.findViewById(R.id.armorName);
		mDialogMaxDexSpinner = (Spinner) view.findViewById(R.id.spArmorMaxDex);
		
		setupSpinner(mDialogACSpinner, R.array.ac_spinner_options);
		setupSpinner(mDialogACPSpinner, R.array.acp_spinner_options);
		setupSpinner(mDialogSizeSpinner, R.array.size_spinner_options);
		setupSpinner(mDialogSpeedSpinner, R.array.speed_spinner_options);
		setupSpinner(mDialogMaxDexSpinner, R.array.acp_spinner_options);
		setupSpinner(mDialogASFSpinner, R.array.armor_spell_fail_options);
		
		if(mArmorSelectedForEdit < 0) {
			getDialog().setTitle(R.string.new_armor_title);
			mDialogACSpinner.setSelection(AC_SPINNER_OFFSET);
		} else {
			getDialog().setTitle(mArmor.getName());
			//getDialog().setNeutralButton(R.string.delete_button_text,
				//	this);
			mDialogNameET.setText(mArmor.getName());
			mDialogACSpinner.setSelection(mArmor.getACBonus() + AC_SPINNER_OFFSET);
			mDialogACPSpinner.setSelection(mArmor.getCheckPen());
			mDialogSizeSpinner.setSelection(mArmor.getSizeInt());
			mDialogMaxDexSpinner.setSelection(mArmor.getMaxDex());
			mDialogSpeedSpinner.setSelection(mArmor.getSpeed()/5);
			mDialogASFSpinner.setSelection(mArmor.getSpellFail() / ASF_INCREMENT);
			mDialogWeightET.setText(Double.toString(mArmor.getWeight()));
			mDialogSpecialPropertiesET.setText(mArmor.getSpecialProperties());
		}
		
		return view;
	}
	
	private void setupSpinner(Spinner spinner, int optionResourceId) {
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
				optionResourceId, android.R.layout.simple_spinner_item);
		
		if(mSpinnerOnTouchListener == null) {
			mSpinnerOnTouchListener = new OnTouchListener() {
	            @Override
	            public boolean onTouch(View v, MotionEvent event) {
	                closeKeyboard();
	                dismiss();
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
		iMM.hideSoftInputFromWindow(getView().getWindowToken(), 0);
	}
	
	public void onClick(DialogInterface dialogInterface, int selection) {
		updateArmorFromDialog();
		ArmorReturn returnValue = new ArmorReturn();
		returnValue.armor = mArmor;
		
		switch (selection) {
		case DialogInterface.BUTTON_POSITIVE:
			Log.v(TAG, "Add.edit armor OK: " + mDialogNameET.getText());
			returnValue.action = ArmorAction.ADD_EDIT;	
			break;
		
		case DialogInterface.BUTTON_NEUTRAL:
			Log.v(TAG, "Deleting an armor");
			returnValue.action = ArmorAction.DELETE;
			break;
		
		case DialogInterface.BUTTON_NEGATIVE:
			returnValue.action = ArmorAction.CANCEL;
			break;
		
		default:
			break;
		}
		Log.v(TAG, "Returning an armor");
		mListener.onArmorDialogReturn(returnValue);
		Log.v(TAG, "Dismissing armor dialog");
		dismiss();
	}
	
	private void updateArmorFromDialog() {
		String name = new String(mDialogNameET.getText().toString());
		
		if(name == null || name.contentEquals("")) {
			mArmor = null;
			return;
		}
		
		String specialProperties = new String(mDialogSpecialPropertiesET.getText().toString());
		int speed = mDialogSpeedSpinner.getSelectedItemPosition() * SPEED_INCREMENT;
		
		int spellFail = mDialogASFSpinner.getSelectedItemPosition() * ASF_INCREMENT;
		
		int weight;
		try {
			weight = Integer.parseInt(mDialogWeightET.getText().toString());
		} catch (NumberFormatException e) {
			weight = 1;
		}
		
		int size = mDialogSizeSpinner.getSelectedItemPosition();
		int ac = mDialogACSpinner.getSelectedItemPosition() - AC_SPINNER_OFFSET;
		int acp = mDialogACPSpinner.getSelectedItemPosition();
		int maxDex = mDialogMaxDexSpinner.getSelectedItemPosition();
		
		mArmor.setName(name);
		mArmor.setSpeed(speed);
		mArmor.setSpecialProperties(specialProperties);
		mArmor.setSpellFail(spellFail);
		mArmor.setWeight(weight);
		mArmor.setSize(size);
		mArmor.setACBonus(ac);
		mArmor.setCheckPen(acp);
		mArmor.setMaxDex(maxDex);
	}
}
