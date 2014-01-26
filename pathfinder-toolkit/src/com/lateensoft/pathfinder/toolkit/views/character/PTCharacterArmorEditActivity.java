package com.lateensoft.pathfinder.toolkit.views.character;

import android.os.Parcelable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.model.character.items.PTArmor;
import com.lateensoft.pathfinder.toolkit.views.PTParcelableEditorActivity;

public class PTCharacterArmorEditActivity extends PTParcelableEditorActivity {
	@SuppressWarnings("unused")
	private static final String TAG = PTCharacterArmorEditActivity.class.getSimpleName();

	private static final int AC_SPINNER_OFFSET = 20;
	private static final int ACP_SPINNER_OFFSET = 20;
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
    private CheckBox m_wornCheckbox;
	private OnTouchListener m_spinnerOnTouchListener;
	
	private PTArmor m_armor;
	private boolean m_armorIsNew = false;

	@Override
	protected void setupContentView() {
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
		m_wornCheckbox = (CheckBox) findViewById(R.id.checkboxIsWorn);
		
		m_spinnerOnTouchListener = new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				closeKeyboard();
				return false;
			}
		};

		setupSpinner(m_ACSpinner, R.array.ac_spinner_options, AC_SPINNER_OFFSET, m_spinnerOnTouchListener);
		setupSpinner(m_ACPSpinner, R.array.selectable_negative_values_strings, 0, m_spinnerOnTouchListener);
		setupSpinner(m_sizeSpinner, R.array.size_spinner_options, 0, m_spinnerOnTouchListener);
		setupSpinner(m_speedSpinner, R.array.speed_spinner_options, 0, m_spinnerOnTouchListener);
		setupSpinner(m_maxDexSpinner, R.array.selectable_whole_values_strings, 0, m_spinnerOnTouchListener);
		setupSpinner(m_ASFSpinner, R.array.armor_spell_fail_options, 0, m_spinnerOnTouchListener);

		if(m_armorIsNew) {
			setTitle(R.string.new_armor_title);
		} else {
			setTitle(R.string.edit_armor_title);
			m_nameET.setText(m_armor.getName());
		}
		m_ACSpinner.setSelection(m_armor.getACBonus() + AC_SPINNER_OFFSET);
		m_ACPSpinner.setSelection(m_armor.getCheckPen() + ACP_SPINNER_OFFSET);
		m_sizeSpinner.setSelection(m_armor.getSizeInt());
		m_maxDexSpinner.setSelection(m_armor.getMaxDex());
		m_speedSpinner.setSelection(m_armor.getSpeed()/5);
		m_ASFSpinner.setSelection(m_armor.getSpellFail() / ASF_INCREMENT);
		m_weightET.setText(Double.toString(m_armor.getWeight()));
		m_specialPropertiesET.setText(m_armor.getSpecialProperties());
		m_wornCheckbox.setChecked(m_armor.isWorn());
	}

	@Override
	protected void updateEditedParcelableValues() throws InvalidValueException {
		String name = new String(m_nameET.getText().toString());

		if(name == null || name.isEmpty()) {
			throw new InvalidValueException(getString(R.string.editor_name_required_alert));
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
        int acp = m_ACPSpinner.getSelectedItemPosition() - ACP_SPINNER_OFFSET;
        int maxDex = m_maxDexSpinner.getSelectedItemPosition();
        boolean worn = m_wornCheckbox.isChecked();
        
        m_armor.setName(name);
        m_armor.setSpeed(speed);
        m_armor.setSpecialProperties(specialProperties);
        m_armor.setSpellFail(spellFail);
        m_armor.setWeight(weight);
        m_armor.setSizeInt(size);
        m_armor.setACBonus(ac);
        m_armor.setCheckPen(acp);
        m_armor.setMaxDex(maxDex);
        m_armor.setWorn(worn);
	}

	@Override
	protected Parcelable getEditedParcelable() {
		return m_armor;
	}

	@Override
	protected void setParcelableToEdit(Parcelable p) {
		if(p == null) {
			m_armorIsNew = true;
			m_armor = new PTArmor();
		} else {
			m_armor = (PTArmor) p;
		}
	}

	@Override
	protected boolean isParcelableDeletable() {
		return !m_armorIsNew;
	}
}
