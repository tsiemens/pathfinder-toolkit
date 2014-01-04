package com.lateensoft.pathfinder.toolkit.views.character;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.items.PTWeapon;

public class PTCharacterWeaponEditActivity extends Activity {
	private static final String TAG = PTCharacterWeaponEditActivity.class.getSimpleName();
	
	public static final int RESULT_CUSTOM_DELETE = RESULT_FIRST_USER;
	public static final String INTENT_EXTRAS_KEY_WEAPON = "weapon";
	
	public static final int FLAG_NEW_WEAPON = 0x1;

	private static final int ATK_BONUS_OFFSET = 10;
	
	private Spinner m_highestAtkSpinner;
    private Spinner m_ammoSpinner;
    private Spinner m_sizeSpinner;
    private Spinner m_typeSpinner;
    private Spinner m_rangeSpinner;
    private EditText m_weightET;
    private EditText m_nameET;
    private EditText m_damageET;
    private EditText m_criticalET;
    private EditText m_specialPropertiesET;
	private OnTouchListener m_spinnerOnTouchListener;
	
	private PTWeapon m_weapon;
	private boolean m_weaponIsNew = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		m_weapon = getIntent().getExtras().getParcelable(INTENT_EXTRAS_KEY_WEAPON);
		if (m_weapon == null) {
			m_weaponIsNew = true;
		}
		
		setupContentView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.base_editor_menu, menu);
	    if (m_weaponIsNew) {
	    	menu.findItem(R.id.mi_delete).setVisible(false);
	    }
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.mi_done) {
			if (updateWeaponValues()) {
				Log.v(TAG, (m_weaponIsNew?"Add":"Edit")+" weapon done: " + m_nameET.getText());
				Intent resultData = new Intent();
				resultData.putExtra(INTENT_EXTRAS_KEY_WEAPON, m_weapon);
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
		if(m_weapon == null) {
			m_weapon = new PTWeapon();
		}

		setContentView(R.layout.character_weapon_editor);

		m_nameET = (EditText) findViewById(R.id.etWeaponName);
		m_highestAtkSpinner = (Spinner) findViewById(R.id.spWeaponHighestAtk);
		m_ammoSpinner = (Spinner) findViewById(R.id.spWeaponAmmo);
		m_typeSpinner = (Spinner) findViewById(R.id.spWeaponType);
		m_sizeSpinner = (Spinner) findViewById(R.id.spWeaponSize);
		m_rangeSpinner = (Spinner) findViewById(R.id.spWeaponRange);
		m_criticalET = (EditText) findViewById(R.id.etWeaponCrit);
		m_damageET = (EditText) findViewById(R.id.etWeaponDamage);
		m_weightET = (EditText) findViewById(R.id.etWeaponWeight);
		m_specialPropertiesET = (EditText) findViewById(
				R.id.etWeaponSpecialProperties);

		setupSpinner(m_highestAtkSpinner, R.array.weapon_attack_bonus_options);
		setupSpinner(m_ammoSpinner, R.array.selectable_values_string);
		setupSpinner(m_sizeSpinner, R.array.size_spinner_options);
		setupSpinner(m_typeSpinner, R.array.weapon_type_options);
		setupSpinner(m_rangeSpinner, R.array.weapon_range_options);

		if(m_weaponIsNew) {
			setTitle(R.string.new_weapon_title);
			m_highestAtkSpinner.setSelection(ATK_BONUS_OFFSET);
		} else {
			setTitle(R.string.edit_weapon_title);
			m_nameET.setText(m_weapon.getName());
			m_highestAtkSpinner.setSelection(m_weapon.getTotalAttackBonus() + ATK_BONUS_OFFSET);
			m_damageET.setText(m_weapon.getDamage());
			m_criticalET.setText(m_weapon.getCritical());
			m_ammoSpinner.setSelection(m_weapon.getAmmunition());
			m_sizeSpinner.setSelection(m_weapon.getSizeInt());
			m_rangeSpinner.setSelection(m_weapon.getRange()/5);
			m_typeSpinner.setSelection(m_weapon.getTypeInt(this));
			m_weightET.setText(Double.toString(m_weapon.getWeight()));
			m_specialPropertiesET.setText(m_weapon.getSpecialProperties());
		}
	}
	
	private boolean updateWeaponValues() {
		String name = new String(m_nameET.getText().toString());
        
        if(name == null || name.isEmpty()) {
                return false;
        }
        
        int attack = m_highestAtkSpinner.getSelectedItemPosition() - ATK_BONUS_OFFSET;;
		double weight;
		try {
			weight = Double.parseDouble(m_weightET.getText().toString());
		} catch (NumberFormatException e) {
			weight = 1;
		}
		
		int range = m_rangeSpinner.getSelectedItemPosition()*5;
		int ammo = m_ammoSpinner.getSelectedItemPosition();
		String damage = new String(m_damageET.getText().toString());
		String critical = new String(m_criticalET.getText().toString());
		String specialProperties = new String(m_specialPropertiesET.getText().toString());
		String type = new String(getStringByResourceAndIndex(R.array.weapon_type_options,
				m_typeSpinner.getSelectedItemPosition()));
		String size = new String(getStringByResourceAndIndex(R.array.size_spinner_options,
				m_sizeSpinner.getSelectedItemPosition()));
        
		m_weapon.setName(name);
		m_weapon.setTotalAttackBonus(attack);
		m_weapon.setWeight(weight);
		m_weapon.setRange(range);
		m_weapon.setAmmunition(ammo);
		m_weapon.setDamage(damage);
		m_weapon.setCritical(critical);
		m_weapon.setSpecialProperties(specialProperties);
		m_weapon.setType(type);
		m_weapon.setSize(size);
		
        return true;
	}

	private void setupSpinner(Spinner spinner, int optionResourceId) {
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
				optionResourceId, android.R.layout.simple_spinner_item);

		if(m_spinnerOnTouchListener == null) {
			m_spinnerOnTouchListener = new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					closeKeyboard();
					return false;
				}
			};
		}

		adapter.setDropDownViewResource(R.layout.spinner_plain);
		spinner.setAdapter(adapter);
		spinner.setOnTouchListener(m_spinnerOnTouchListener);
	}
	
	private void showDeleteConfirmation() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.delete_alert_title);
		builder.setMessage(R.string.delete_weapon_alert_message);
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
	
	private String getStringByResourceAndIndex(int resourceId, int position) {
		Resources r = getResources();
		String[] resource = r.getStringArray(resourceId);
		return resource[position];
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
