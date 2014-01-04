package com.lateensoft.pathfinder.toolkit.views.character;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
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
import com.lateensoft.pathfinder.toolkit.items.PTArmor;

public class PTCharacterArmorEditActivity extends Activity {
	private static final String TAG = PTCharacterArmorEditActivity.class.getSimpleName();
	
	public static final int RESULT_CUSTOM_DELETE = RESULT_FIRST_USER;
	public static final String INTENT_EXTRAS_KEY_ARMOR = "armor";
	
	public static final int FLAG_NEW_ARMOR = 0x1;

	private static final int AC_SPINNER_OFFSET = 20;
    private static final int ASF_INCREMENT = 5;
    private static final int SPEED_INCREMENT = 5;
	
	private Spinner m_ACSpinner;
    private Spinner m_ACPSpinner;
    private Spinner m_sizeSpinner;
    private Spinner m_speedSpinner;
    private Spinner m_maxDexSpinner;
    private Spinner m_ASFSpinner;
    private EditText m_weightET;
    private EditText m_nameET;
    private EditText m_specialPropertiesET;
	private OnTouchListener m_spinnerOnTouchListener;
	
	private PTArmor m_armor;
	private boolean m_armorIsNew = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		m_armor = getIntent().getExtras().getParcelable(INTENT_EXTRAS_KEY_ARMOR);
		if (m_armor == null) {
			m_armorIsNew = true;
		}
		
		setupContentView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.base_editor_menu, menu);
	    if (m_armorIsNew) {
	    	menu.findItem(R.id.mi_delete).setVisible(false);
	    }
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.mi_done) {
			if (updateArmorValues()) {
				Log.v(TAG, (m_armorIsNew?"Add":"Edit")+" armor done: " + m_nameET.getText());
				Intent resultData = new Intent();
				resultData.putExtra(INTENT_EXTRAS_KEY_ARMOR, m_armor);
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
		if(m_armor == null) {
			m_armor = new PTArmor();
		}

		setContentView(R.layout.character_armor_editor);

		m_ACSpinner = (Spinner) findViewById(R.id.spArmorClass);
		m_ACPSpinner = (Spinner) findViewById(R.id.spArmorCheckPenalty);
		m_sizeSpinner = (Spinner) findViewById(R.id.spArmorSize);
		m_speedSpinner = (Spinner) findViewById(R.id.spArmorSpeed);
		m_ASFSpinner = (Spinner) findViewById(R.id.spArmorSpellFailure);
		m_weightET = (EditText) findViewById(R.id.etArmorWeight);
		m_specialPropertiesET = (EditText) findViewById(
				R.id.etArmorSpecialProperties);
		m_nameET = (EditText) findViewById(R.id.armorName);
		m_maxDexSpinner = (Spinner) findViewById(R.id.spArmorMaxDex);

		setupSpinner(m_ACSpinner, R.array.ac_spinner_options);
		setupSpinner(m_ACPSpinner, R.array.acp_spinner_options);
		setupSpinner(m_sizeSpinner, R.array.size_spinner_options);
		setupSpinner(m_speedSpinner, R.array.speed_spinner_options);
		setupSpinner(m_maxDexSpinner, R.array.acp_spinner_options);
		setupSpinner(m_ASFSpinner, R.array.armor_spell_fail_options);

		if(m_armorIsNew) {
			setTitle(R.string.new_armor_title);
			m_ACSpinner.setSelection(AC_SPINNER_OFFSET);
		} else {
			setTitle(R.string.edit_armor_title);
			m_nameET.setText(m_armor.getName());
			m_ACSpinner.setSelection(m_armor.getACBonus() + AC_SPINNER_OFFSET);
			m_ACPSpinner.setSelection(m_armor.getCheckPen());
			m_sizeSpinner.setSelection(m_armor.getSizeInt());
			m_maxDexSpinner.setSelection(m_armor.getMaxDex());
			m_speedSpinner.setSelection(m_armor.getSpeed()/5);
			m_ASFSpinner.setSelection(m_armor.getSpellFail() / ASF_INCREMENT);
			m_weightET.setText(Double.toString(m_armor.getWeight()));
			m_specialPropertiesET.setText(m_armor.getSpecialProperties());
		}
	}
	
	private boolean updateArmorValues() {
		String name = new String(m_nameET.getText().toString());
        
        if(name == null || name.isEmpty()) {
                return false;
        }
        
        String specialProperties = new String(m_specialPropertiesET.getText().toString());
        int speed = m_speedSpinner.getSelectedItemPosition() * SPEED_INCREMENT;
        
        int spellFail = m_ASFSpinner.getSelectedItemPosition() * ASF_INCREMENT;
        
        int weight;
        try {
                weight = Integer.parseInt(m_weightET.getText().toString());
        } catch (NumberFormatException e) {
                weight = 1;
        }
        
        int size = m_sizeSpinner.getSelectedItemPosition();
        int ac = m_ACSpinner.getSelectedItemPosition() - AC_SPINNER_OFFSET;
        int acp = m_ACPSpinner.getSelectedItemPosition();
        int maxDex = m_maxDexSpinner.getSelectedItemPosition();
        
        m_armor.setName(name);
        m_armor.setSpeed(speed);
        m_armor.setSpecialProperties(specialProperties);
        m_armor.setSpellFail(spellFail);
        m_armor.setWeight(weight);
        m_armor.setSize(size);
        m_armor.setACBonus(ac);
        m_armor.setCheckPen(acp);
        m_armor.setMaxDex(maxDex);
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
		builder.setMessage(R.string.delete_alert_message);
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
