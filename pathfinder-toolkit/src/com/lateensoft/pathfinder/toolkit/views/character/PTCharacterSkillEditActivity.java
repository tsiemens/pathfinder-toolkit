package com.lateensoft.pathfinder.toolkit.views.character;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.stats.PTSkill;

public class PTCharacterSkillEditActivity extends SherlockActivity{
	private static final String TAG = PTCharacterSkillEditActivity.class.getSimpleName();
	
	public static final String INTENT_EXTRAS_KEY_SKILL = "skill";
	
	private TextView m_skillNameText;
	private Spinner m_abilityModSpinner;
	private Spinner m_rankSpinner;
	private Spinner m_miscModSpinner;
	private Spinner m_armorCheckSpinner;
	private CheckBox m_classSkillCheckBox;
	
	private PTSkill m_skill;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		m_skill = getIntent().getExtras().getParcelable(INTENT_EXTRAS_KEY_SKILL);
		if (m_skill == null) {
			// Should not happen usually, but could possibly due to lifecycle, in which case just leave
			finish();
		}
		
		setupContentView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
	    inflater.inflate(R.menu.base_editor_menu, menu);

	    // Cannot delete skills
	    menu.findItem(R.id.mi_delete).setVisible(false);

		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.mi_done) {
			updateSkillValues();
			Log.v(TAG, "Edit skill done: " + m_skill.getName());
			Intent resultData = new Intent();
			resultData.putExtra(INTENT_EXTRAS_KEY_SKILL, m_skill);
			setResult(RESULT_OK, resultData);
			finish();
		} else if (item.getItemId() == R.id.mi_cancel || 
				item.getItemId() == android.R.id.home) {
			setResult(RESULT_CANCELED);
			finish();
		} else {
			return super.onOptionsItemSelected(item);
		}
		return true;
	}
	
	private void setupContentView() {
		setContentView(R.layout.character_skill_editor);

		m_skillNameText = (TextView) findViewById(R.id.tvSkillName);
		m_abilityModSpinner = (Spinner) findViewById(R.id.spinnerSkillAbility);
		m_rankSpinner = (Spinner) findViewById(R.id.spinnerSkillRank);
		m_miscModSpinner = (Spinner) findViewById(R.id.spinnerSkillMisc);
		m_armorCheckSpinner = (Spinner) findViewById(R.id.spinnerSkillACP);
		m_classSkillCheckBox = (CheckBox) findViewById(R.id.checkboxClassSkill);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.skills_selectable_values_string,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(R.layout.spinner_plain);

		m_skillNameText.setText(m_skill.getName() + " ("
				+ m_skill.getKeyAbility() + ")");

		setupSpinner(m_abilityModSpinner, adapter,
				m_skill.getAbilityMod());
		setupSpinner(m_rankSpinner, adapter,
				m_skill.getRank());
		setupSpinner(m_miscModSpinner, adapter,
				m_skill.getMiscMod());
		setupSpinner(m_armorCheckSpinner, adapter,
				m_skill.getArmorCheckPenalty());

		m_classSkillCheckBox.setChecked(m_skill.isClassSkill());
	}
	
	private void updateSkillValues() {
		m_skill.setAbilityMod(m_abilityModSpinner
				.getSelectedItemPosition() - 10);
		m_skill.setRank(m_rankSpinner
				.getSelectedItemPosition() - 10);
		m_skill.setMiscMod(m_miscModSpinner
				.getSelectedItemPosition() - 10);
		m_skill.setArmorCheckPenalty(m_armorCheckSpinner
				.getSelectedItemPosition() - 10);
		m_skill.setClassSkill(m_classSkillCheckBox
				.isChecked());
	}

	private void setupSpinner(Spinner spinner,
			ArrayAdapter<CharSequence> adapter, int currentValue) {

		spinner.setAdapter(adapter);
		spinner.setSelection(currentValue + 10, true); // +10 is because at
														// position 0, is value
														// = -10
	}
}
