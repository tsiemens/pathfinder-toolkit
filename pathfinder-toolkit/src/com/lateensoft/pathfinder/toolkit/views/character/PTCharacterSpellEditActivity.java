package com.lateensoft.pathfinder.toolkit.views.character;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.character.PTSpell;
import com.lateensoft.pathfinder.toolkit.views.PTParcelableEditorActivity;

import android.os.Parcelable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.Spinner;

public class PTCharacterSpellEditActivity extends PTParcelableEditorActivity {

	@SuppressWarnings("unused")
	private static final String TAG = PTCharacterSpellEditActivity.class.getSimpleName();

	private EditText m_nameET;
	private Spinner m_levelSpinner;
	private Spinner m_preparedSpinner;
	private EditText m_descriptionET;
	private OnTouchListener m_spinnerOnTouchListener;

	private PTSpell m_spell;
	private boolean m_spellIsNew = false;

	@Override
	protected void setupContentView() {
		setContentView(R.layout.character_spell_editor);

		m_nameET = (EditText) findViewById(R.id.etSpellName);
		m_levelSpinner = (Spinner) findViewById(R.id.spSpellLevel);
		m_preparedSpinner = (Spinner) findViewById(R.id.spSpellPrepared);
		m_descriptionET = (EditText) findViewById(R.id.etSpellDescription);

		m_spinnerOnTouchListener = new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				closeKeyboard();
				return false;
			}
		};

		setupSpinner(m_levelSpinner, R.array.spell_level_selectable_values_string, m_spell.getLevel(), m_spinnerOnTouchListener);
		setupSpinner(m_preparedSpinner, R.array.selectable_values_string, m_spell.getPrepared(), m_spinnerOnTouchListener);

		if(m_spellIsNew) {
			setTitle(R.string.new_spell_title);
		} else {
			setTitle(R.string.edit_spell_title);
			m_nameET.setText(m_spell.getName());
			m_descriptionET.setText(m_spell.getDescription());
		}
	}

	@Override
	protected void updateEditedParcelableValues() throws InvalidValueException {
		String name = new String(m_nameET.getText().toString());
		if(name == null || name.contentEquals("")) {
			throw new InvalidValueException(getString(R.string.editor_name_required_alert));
		}

		String description = new String(m_descriptionET.getText().toString());

		int level = m_levelSpinner.getSelectedItemPosition();
		int prepared = m_preparedSpinner.getSelectedItemPosition();

		m_spell.setName(name);
		m_spell.setDescription(description);
		m_spell.setLevel(level);
		m_spell.setPrepared(prepared);
	}

	@Override
	protected Parcelable getEditedParcelable() {
		return m_spell;
	}

	@Override
	protected void setParcelableToEdit(Parcelable p) {
		if (p == null) {
			m_spellIsNew = true;
			m_spell = new PTSpell();
		} else {
			m_spell = (PTSpell) p;
		}
	}

	@Override
	protected boolean isParcelableDeletable() {
		return !m_spellIsNew;
	}

}
