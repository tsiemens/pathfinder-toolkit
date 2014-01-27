package com.lateensoft.pathfinder.toolkit.views.character;

import android.content.res.Resources;
import android.os.Parcelable;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.Spinner;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.model.character.items.PTWeapon;
import com.lateensoft.pathfinder.toolkit.views.PTParcelableEditorActivity;

public class PTCharacterWeaponEditActivity extends PTParcelableEditorActivity {
	@SuppressWarnings("unused")
	private static final String TAG = PTCharacterWeaponEditActivity.class.getSimpleName();

	private static final int ATK_BONUS_OFFSET = 10;
	
	private Spinner m_highestAtkSpinner;
    private EditText m_ammoET;
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
	protected void setupContentView() {
		setContentView(R.layout.character_weapon_editor);

		m_nameET = (EditText) findViewById(R.id.etWeaponName);
		m_highestAtkSpinner = (Spinner) findViewById(R.id.spWeaponHighestAtk);
		m_ammoET = (EditText) findViewById(R.id.etWeaponAmmo);
		m_typeSpinner = (Spinner) findViewById(R.id.spWeaponType);
		m_sizeSpinner = (Spinner) findViewById(R.id.spWeaponSize);
		m_rangeSpinner = (Spinner) findViewById(R.id.spWeaponRange);
		m_criticalET = (EditText) findViewById(R.id.etWeaponCrit);
		m_damageET = (EditText) findViewById(R.id.etWeaponDamage);
		m_weightET = (EditText) findViewById(R.id.etWeaponWeight);
		m_specialPropertiesET = (EditText) findViewById(
				R.id.etWeaponSpecialProperties);
		
		setupSpinner(m_highestAtkSpinner, R.array.weapon_attack_bonus_options,
				ATK_BONUS_OFFSET, m_spinnerOnTouchListener);
		setupSpinner(m_sizeSpinner, R.array.size_spinner_options, 0, m_spinnerOnTouchListener);
		setupSpinner(m_typeSpinner, R.array.weapon_type_options, 0, m_spinnerOnTouchListener);
		setupSpinner(m_rangeSpinner, R.array.weapon_range_options, 0, m_spinnerOnTouchListener);

		if(m_weaponIsNew) {
			setTitle(R.string.new_weapon_title);
			m_highestAtkSpinner.setSelection(ATK_BONUS_OFFSET);
		} else {
			setTitle(R.string.edit_weapon_title);
			m_nameET.setText(m_weapon.getName());
			m_ammoET.setText(Integer.toString(m_weapon.getAmmunition()));			
			m_highestAtkSpinner.setSelection(m_weapon.getTotalAttackBonus() + ATK_BONUS_OFFSET);
			m_damageET.setText(m_weapon.getDamage());
			m_criticalET.setText(m_weapon.getCritical());
			m_sizeSpinner.setSelection(m_weapon.getSizeInt());
			m_rangeSpinner.setSelection(m_weapon.getRange()/5);
			m_typeSpinner.setSelection(m_weapon.getTypeInt(this));
			m_weightET.setText(Double.toString(m_weapon.getWeight()));
			m_specialPropertiesET.setText(m_weapon.getSpecialProperties());
		}
	}
	
	private String getStringByResourceAndIndex(int resourceId, int position) {
		Resources r = getResources();
		String[] resource = r.getStringArray(resourceId);
		return resource[position];
	}

	@Override
	protected void updateEditedParcelableValues() throws InvalidValueException {
String name = new String(m_nameET.getText().toString());
        
        if(name == null || name.isEmpty()) {
        	throw new InvalidValueException(getString(R.string.editor_name_required_alert));
        }
        
        int attack = m_highestAtkSpinner.getSelectedItemPosition() - ATK_BONUS_OFFSET;;
		double weight;
		try {
			weight = Double.parseDouble(m_weightET.getText().toString());
		} catch (NumberFormatException e) {
			weight = 1;
		}
		
		int range = m_rangeSpinner.getSelectedItemPosition()*5;
		int ammo = m_weapon.getAmmunition();
		try {
			ammo = Integer.parseInt(m_ammoET.getText().toString());
		} catch (NumberFormatException e) {
			// Do not change ammo
		}
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
	}

	@Override
	protected Parcelable getEditedParcelable() {
		return m_weapon;
	}

	@Override
	protected void setParcelableToEdit(Parcelable p) {
		if (p == null) {
			m_weaponIsNew = true;
			m_weapon = new PTWeapon();
		} else {
			m_weapon = (PTWeapon) p;
		}
	}

	@Override
	protected boolean isParcelableDeletable() {
		return !m_weaponIsNew;
	}

}
