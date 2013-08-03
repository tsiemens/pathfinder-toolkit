package com.lateensoft.pathfinder.toolkit.views.character;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.items.PTArmor;

public class PTCharacterArmorEditActivity extends SherlockActivity {
	private static final String TAG = PTCharacterArmorEditActivity.class.getSimpleName();
	
	public static final int RESULT_CUSTOM_DELETE = RESULT_FIRST_USER;
	public static final String INTENT_EXTRAS_KEY_ARMOR = "armor";
	
	public static final int FLAG_NEW_ARMOR = 0x1;

	private static final int AC_SPINNER_OFFSET = 20;
    private static final int ASF_INCREMENT = 5;
    private static final int SPEED_INCREMENT = 5;
	
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
	
	private PTArmor mArmor;
	private boolean mArmorIsNew = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		mArmor = getIntent().getExtras().getParcelable(INTENT_EXTRAS_KEY_ARMOR);
		if (mArmor == null) {
			mArmorIsNew = true;
		}
		
		setupContentView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
	    inflater.inflate(R.menu.base_editor_menu, menu);
	    if (mArmorIsNew) {
	    	menu.findItem(R.id.mi_delete).setVisible(false);
	    }
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.mi_done) {
			if (updateArmorValues()) {
			 Log.v(TAG, (mArmorIsNew?"Add":"Edit")+" armor done: " + mDialogNameET.getText());
             Intent resultData = new Intent();
             resultData.putExtra(INTENT_EXTRAS_KEY_ARMOR, mArmor);
			 setResult(RESULT_OK, resultData);
             finish();
			} else {
				showInvalidNameDialog();
			}
		} else if (item.getItemId() == R.id.mi_cancel || 
				item.getItemId() == android.R.id.home) {
			setResult(RESULT_CANCELED);
			finish();
		} else if (item.getItemId() == R.id.mi_delete) {
			showDeleteConfirmation();
		} else {
			return super.onOptionsItemSelected(item);
		}
		return true;
	}
	
	private void setupContentView() {
		if(mArmor == null) {
			mArmor = new PTArmor();
		}

		setContentView(R.layout.character_armor_editor);

		mDialogACSpinner = (Spinner) findViewById(R.id.spArmorClass);
		mDialogACPSpinner = (Spinner) findViewById(R.id.spArmorCheckPenalty);
		mDialogSizeSpinner = (Spinner) findViewById(R.id.spArmorSize);
		mDialogSpeedSpinner = (Spinner) findViewById(R.id.spArmorSpeed);
		mDialogASFSpinner = (Spinner) findViewById(R.id.spArmorSpellFailure);
		mDialogWeightET = (EditText) findViewById(R.id.etArmorWeight);
		mDialogSpecialPropertiesET = (EditText) findViewById(
				R.id.etArmorSpecialProperties);
		mDialogNameET = (EditText) findViewById(R.id.armorName);
		mDialogMaxDexSpinner = (Spinner) findViewById(R.id.spArmorMaxDex);

		setupSpinner(mDialogACSpinner, R.array.ac_spinner_options);
		setupSpinner(mDialogACPSpinner, R.array.acp_spinner_options);
		setupSpinner(mDialogSizeSpinner, R.array.size_spinner_options);
		setupSpinner(mDialogSpeedSpinner, R.array.speed_spinner_options);
		setupSpinner(mDialogMaxDexSpinner, R.array.acp_spinner_options);
		setupSpinner(mDialogASFSpinner, R.array.armor_spell_fail_options);

		if(mArmorIsNew) {
			setTitle(R.string.new_armor_title);
			mDialogACSpinner.setSelection(AC_SPINNER_OFFSET);
		} else {
			setTitle(R.string.edit_armor_title);
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
	}
	
	private boolean updateArmorValues() {
		String name = new String(mDialogNameET.getText().toString());
        
        if(name == null || name.contentEquals("")) {
                return false;
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
        return true;
	}

	private void setupSpinner(Spinner spinner, int optionResourceId) {
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
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
	
	private void showDeleteConfirmation() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.delete_armor_alert_title);
		builder.setMessage(R.string.delete_armor_alert_message);
		OnClickListener ocl = new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == DialogInterface.BUTTON_NEGATIVE) {
					setResult(RESULT_CUSTOM_DELETE);
					finish();
				}
			}
		};
		builder.setPositiveButton(R.string.cancel_button_text, ocl);
		builder.setNegativeButton(R.string.ok_button_text, ocl);
		builder.show();
	}
	
	private void showInvalidNameDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.error_alert_title);
		builder.setMessage(R.string.editor_name_required_alert);
		builder.setNeutralButton(R.string.ok_button_text, null);
		builder.show();
	}

	private void closeKeyboard() {
		InputMethodManager iMM = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
		iMM.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
	}
}
