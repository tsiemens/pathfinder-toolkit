package com.lateensoft.pathfinder.toolkit.views.character;

import android.os.Parcelable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.model.character.items.Armor;
import com.lateensoft.pathfinder.toolkit.model.character.items.Size;
import com.lateensoft.pathfinder.toolkit.util.InputMethodUtils;
import com.lateensoft.pathfinder.toolkit.views.ParcelableEditorActivity;

public class ArmorEditActivity extends ParcelableEditorActivity {
	@SuppressWarnings("unused")
	private static final String TAG = ArmorEditActivity.class.getSimpleName();

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
    private EditText m_quantityET;
    private CheckBox m_itemContainedCheckbox;
    private CheckBox m_wornCheckbox;
	private OnTouchListener m_spinnerOnTouchListener;
	
	private Armor m_armor;
	private boolean m_armorIsNew = false;

	@Override
	protected void setupContentView() {
		setContentView(R.layout.armor_editor);

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
        m_quantityET = (EditText) findViewById(R.id.etItemQuantity);
        m_itemContainedCheckbox = (CheckBox) findViewById(R.id.checkboxItemContained);
		
		m_spinnerOnTouchListener = new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
                InputMethodUtils.hideSoftKeyboard(ArmorEditActivity.this);
				return false;
			}
		};

		setupSpinner(m_ACSpinner, R.array.ac_spinner_options, AC_SPINNER_OFFSET, m_spinnerOnTouchListener);
		setupSpinner(m_ACPSpinner, R.array.selectable_negative_values_strings, 0, m_spinnerOnTouchListener);
		setupSpinner(m_sizeSpinner, Size.getValuesSortedNames(getResources()), 0, m_spinnerOnTouchListener);
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
		m_sizeSpinner.setSelection(m_armor.getSize().getValuesIndex());
		m_maxDexSpinner.setSelection(m_armor.getMaxDex());
		m_speedSpinner.setSelection(m_armor.getSpeed()/5);
		m_ASFSpinner.setSelection(m_armor.getSpellFail() / ASF_INCREMENT);
		m_weightET.setText(Double.toString(m_armor.getWeight()));
        m_quantityET.setText(Integer.toString(m_armor.getQuantity()));
		m_specialPropertiesET.setText(m_armor.getSpecialProperties());
		m_wornCheckbox.setChecked(m_armor.isWorn());
        m_itemContainedCheckbox.setChecked(m_armor.isContained());
	}

	@Override
	protected void updateEditedParcelableValues() throws InvalidValueException {
		String name = m_nameET.getText().toString();

		if(name == null || name.isEmpty()) {
			throw new InvalidValueException(getString(R.string.editor_name_required_alert));
		}

		String specialProperties = m_specialPropertiesET.getText().toString();
        if (specialProperties == null) {
            specialProperties = "";
        }

		int speed = m_speedSpinner.getSelectedItemPosition() * SPEED_INCREMENT;

		int spellFail = m_ASFSpinner.getSelectedItemPosition() * ASF_INCREMENT;
        
        int weight;
        try {
                weight = Integer.parseInt(m_weightET.getText().toString());
        } catch (NumberFormatException e) {
                weight = 1;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(m_quantityET.getText().toString());
        } catch (NumberFormatException e) {
            quantity = 1;
        }
        
        int sizeIndex = m_sizeSpinner.getSelectedItemPosition();
        int ac = m_ACSpinner.getSelectedItemPosition() - AC_SPINNER_OFFSET;
        int acp = m_ACPSpinner.getSelectedItemPosition() - ACP_SPINNER_OFFSET;
        int maxDex = m_maxDexSpinner.getSelectedItemPosition();

        m_armor.setName(name);
        m_armor.setSpeed(speed);
        m_armor.setSpecialProperties(specialProperties);
        m_armor.setSpellFail(spellFail);
        m_armor.setWeight(weight);
        m_armor.setQuantity(quantity);
        m_armor.setSize(Size.forValuesIndex(sizeIndex));
        m_armor.setACBonus(ac);
        m_armor.setCheckPen(acp);
        m_armor.setMaxDex(maxDex);
        m_armor.setWorn(m_wornCheckbox.isChecked());
        m_armor.setContained(m_itemContainedCheckbox.isChecked());
	}

	@Override
	protected Parcelable getEditedParcelable() {
		return m_armor;
	}

	@Override
	protected void setParcelableToEdit(Parcelable p) {
		if(p == null) {
			m_armorIsNew = true;
			m_armor = new Armor();
		} else {
			m_armor = (Armor) p;
		}
	}

	@Override
	protected boolean isParcelableDeletable() {
		return !m_armorIsNew;
	}
}
