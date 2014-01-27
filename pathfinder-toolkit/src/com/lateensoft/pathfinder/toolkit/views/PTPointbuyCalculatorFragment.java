package com.lateensoft.pathfinder.toolkit.views;

import java.util.List;
import java.util.Map.Entry;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.lateensoft.pathfinder.toolkit.PTMainActivity;
import com.lateensoft.pathfinder.toolkit.PTSharedPreferences;
import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.adapters.PTNavDrawerAdapter;
import com.lateensoft.pathfinder.toolkit.db.repository.PTAbilityRepository;
import com.lateensoft.pathfinder.toolkit.db.repository.PTCharacterRepository;
import com.lateensoft.pathfinder.toolkit.db.repository.PTFluffInfoRepository;
import com.lateensoft.pathfinder.toolkit.model.character.PTCharacter;
import com.lateensoft.pathfinder.toolkit.model.character.PTFluffInfo;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTAbilitySet;
import com.lateensoft.pathfinder.toolkit.utils.EntryUtils;
import com.lateensoft.pathfinder.toolkit.utils.PTAbilitySetCalculator;

public class PTPointbuyCalculatorFragment extends PTBasePageFragment {
	
	static final int STR_IDX = 0;
	static final int DEX_IDX = 1;
	static final int CON_IDX = 2;
	static final int INT_IDX = 3;
	static final int WIS_IDX = 4;
	static final int CHA_IDX = 5;
	static final int NUM_ABILITIES = PTAbilitySet.ABILITY_KEYS().size();
	
	static final int CUSTOM_RACE_INDEX = 7;
	
	private static final int CUSTOM_RACE_MOD_SPINNER_OFFSET = 10;
	
	private PTAbilitySetCalculator m_abilityCalc;
	private HumanRaceModSelectedListener m_humanRaceListener;
	private boolean m_isHuman;
	
	private Button m_strIncBtn;
	private Button m_strDecBtn;
	private Button m_dexIncBtn;
	private Button m_dexDecBtn;
	private Button m_conIncBtn;
	private Button m_conDecBtn;
	private Button m_intIncBtn;
	private Button m_intDecBtn;
	private Button m_wisIncBtn;
	private Button m_wisDecBtn;
	private Button m_chaIncBtn;
	private Button m_chaDecBtn;
	
	private Spinner m_racesSpinner;
	
	private Spinner m_dialogStrSpinner;
	private Spinner m_dialogDexSpinner;
	private Spinner m_dialogConSpinner;
	private Spinner m_dialogIntSpinner;
	private Spinner m_dialogWisSpinner;
	private Spinner m_dialogChaSpinner;
	
	private PTCharacterRepository m_characterRepo;
	private PTFluffInfoRepository m_fluffRepo;
	private PTAbilityRepository m_abilityRepo;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		m_characterRepo = new PTCharacterRepository();
		m_fluffRepo = new PTFluffInfoRepository();
		m_abilityRepo = new PTAbilityRepository();
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		setRootView(inflater.inflate(R.layout.fragment_ability_calculator, container, false));
		setTitle(R.string.title_activity_ability_calc);
		setSubtitle(null);

		m_isHuman = true;

		IncDecButtonListener buttonListener = new IncDecButtonListener();
		
		m_strIncBtn = (Button) getRootView().findViewById(R.id.btnIncStr);
		m_strIncBtn.setOnClickListener(buttonListener);
		m_strDecBtn = (Button) getRootView().findViewById(R.id.btnDecStr);
		m_strDecBtn.setOnClickListener(buttonListener);
		m_dexIncBtn = (Button) getRootView().findViewById(R.id.btnIncDex);
		m_dexIncBtn.setOnClickListener(buttonListener);
		m_dexDecBtn = (Button) getRootView().findViewById(R.id.btnDecDex);
		m_dexDecBtn.setOnClickListener(buttonListener);
		m_conIncBtn = (Button) getRootView().findViewById(R.id.btnIncCon);
		m_conIncBtn.setOnClickListener(buttonListener);
		m_conDecBtn = (Button) getRootView().findViewById(R.id.btnDecCon);
		m_conDecBtn.setOnClickListener(buttonListener);
		m_intIncBtn = (Button) getRootView().findViewById(R.id.btnIncInt);
		m_intIncBtn.setOnClickListener(buttonListener);
		m_intDecBtn = (Button) getRootView().findViewById(R.id.btnDecInt);
		m_intDecBtn.setOnClickListener(buttonListener);
		m_wisIncBtn = (Button) getRootView().findViewById(R.id.btnIncWis);
		m_wisIncBtn.setOnClickListener(buttonListener);
		m_wisDecBtn = (Button) getRootView().findViewById(R.id.btnDecWis);
		m_wisDecBtn.setOnClickListener(buttonListener);
		m_chaIncBtn = (Button) getRootView().findViewById(R.id.btnIncCha);
		m_chaIncBtn.setOnClickListener(buttonListener);
		m_chaDecBtn = (Button) getRootView().findViewById(R.id.btnDecCha);
		m_chaDecBtn.setOnClickListener(buttonListener);

		m_abilityCalc = new PTAbilitySetCalculator();

		m_racesSpinner = (Spinner) getRootView().findViewById(R.id.races_spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
				R.array.races_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(R.layout.spinner_plain);
		// Apply the adapter to the spinner
		m_racesSpinner.setAdapter(adapter);
		m_racesSpinner.setOnItemSelectedListener(new RaceItemSelectedListener());

		m_humanRaceListener = new HumanRaceModSelectedListener();
		getRootView().findViewById(R.id.raceStr).setOnClickListener(m_humanRaceListener);
		getRootView().findViewById(R.id.raceDex).setOnClickListener(m_humanRaceListener);
		getRootView().findViewById(R.id.raceCon).setOnClickListener(m_humanRaceListener);
		getRootView().findViewById(R.id.raceInt).setOnClickListener(m_humanRaceListener);
		getRootView().findViewById(R.id.raceWis).setOnClickListener(m_humanRaceListener);
		getRootView().findViewById(R.id.raceCha).setOnClickListener(m_humanRaceListener);

		updateAbilityViews();

		return getRootView();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		if (inflater != null) {
			inflater.inflate(R.menu.pointbuy_menu, menu);
		}
		
		super.onCreateOptionsMenu(menu);
		return true;
	}
	
	private class RaceItemSelectedListener implements OnItemSelectedListener {
		@Override public void onItemSelected(AdapterView<?> parent, View view, 
		        int pos, long id) {
		    // An item was selected. You can retrieve the selected item using
		    // parent.getItemAtPosition(pos)
			if(parent.getSelectedItemPosition() != CUSTOM_RACE_INDEX) {
				m_isHuman = m_abilityCalc.setRacialMods((int) parent.getSelectedItemId(),
						getActivity());
			
				updateAbilityViews();
			} else {
				// Custom race
				showCustomRaceDialog();
			}
		}
		
		@Override public void onNothingSelected(AdapterView<?> parent) {}
	}
	
	private void showCustomRaceDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		LayoutInflater inflater = getActivity().getLayoutInflater();
		
		View dialogView = inflater.inflate(R.layout.calculator_custom_race_dialog, null);
		m_dialogStrSpinner = (Spinner) dialogView.findViewById(R.id.spCustomStrMod);
		m_dialogDexSpinner = (Spinner) dialogView.findViewById(R.id.spCustomDexMod);
		m_dialogConSpinner = (Spinner) dialogView.findViewById(R.id.spCustomConMod);
		m_dialogIntSpinner = (Spinner) dialogView.findViewById(R.id.spCustomIntMod);
		m_dialogWisSpinner = (Spinner) dialogView.findViewById(R.id.spCustomWisMod);
		m_dialogChaSpinner = (Spinner) dialogView.findViewById(R.id.spCustomChaMod);
		
		setupCustomRaceSpinner(m_dialogStrSpinner);
		setupCustomRaceSpinner(m_dialogDexSpinner);
		setupCustomRaceSpinner(m_dialogConSpinner);
		setupCustomRaceSpinner(m_dialogIntSpinner);
		setupCustomRaceSpinner(m_dialogWisSpinner);
		setupCustomRaceSpinner(m_dialogChaSpinner);
		
		OnCustomRaceModSetListener customRaceModSetListener = new OnCustomRaceModSetListener();
		
		builder.setView(dialogView)
			.setPositiveButton(R.string.ok_button_text, customRaceModSetListener)
			.setTitle(R.string.calc_custom_race_dialog_title);
		
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	private class OnCustomRaceModSetListener implements DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialogInterface, int selection) {	
			m_isHuman = false;
		
			getCustomRacialModSetFromDialog();
		}
	}
	
	private void getCustomRacialModSetFromDialog() {
		int[] mods = new int[NUM_ABILITIES];
				
		mods[STR_IDX] = m_dialogStrSpinner.getSelectedItemPosition() - CUSTOM_RACE_MOD_SPINNER_OFFSET;
		mods[DEX_IDX] = m_dialogDexSpinner.getSelectedItemPosition() - CUSTOM_RACE_MOD_SPINNER_OFFSET;
		mods[CON_IDX] = m_dialogConSpinner.getSelectedItemPosition() - CUSTOM_RACE_MOD_SPINNER_OFFSET;
		mods[INT_IDX] = m_dialogIntSpinner.getSelectedItemPosition() - CUSTOM_RACE_MOD_SPINNER_OFFSET;
		mods[WIS_IDX] = m_dialogWisSpinner.getSelectedItemPosition() - CUSTOM_RACE_MOD_SPINNER_OFFSET;
		mods[CHA_IDX] = m_dialogChaSpinner.getSelectedItemPosition() - CUSTOM_RACE_MOD_SPINNER_OFFSET;
		
		m_abilityCalc.setCustomRaceMods(mods);
		updateAbilityViews();
	}

	private void setupCustomRaceSpinner(Spinner spinner) {
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
				R.array.calc_custom_race_mod_options, android.R.layout.simple_spinner_item);
		
		adapter.setDropDownViewResource(R.layout.spinner_plain);
		spinner.setAdapter(adapter);
		spinner.setSelection(CUSTOM_RACE_MOD_SPINNER_OFFSET);
	}

	private class HumanRaceModSelectedListener implements OnClickListener {
		public void onClick(View v) {
			int id = v.getId();
			
			if(m_isHuman == false) {
				return;
			}
			
			switch (id) {
			case R.id.raceStr:
				m_abilityCalc.setHumanRacialMods(STR_IDX, getActivity());
				break;
			case R.id.raceDex:
				m_abilityCalc.setHumanRacialMods(DEX_IDX, getActivity());
				break;
			case R.id.raceCon:
				m_abilityCalc.setHumanRacialMods(CON_IDX, getActivity());
				break;
			case R.id.raceInt:
				m_abilityCalc.setHumanRacialMods(INT_IDX, getActivity());
				break;
			case R.id.raceWis:
				m_abilityCalc.setHumanRacialMods(WIS_IDX, getActivity());
				break;
			case R.id.raceCha:
				m_abilityCalc.setHumanRacialMods(CHA_IDX, getActivity());
				break;
			}
			
			updateAbilityViews();
		}
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.mi_export_to_new:
			PTCharacter character = new PTCharacter("From calc", getActivity());
			m_abilityCalc.setCalculatedAbilityScores(character.getAbilitySet());
			Resources r = getResources();
			character.getFluff().setRace(r.getStringArray(R.array.races_array)
					[m_racesSpinner.getSelectedItemPosition()]);

			if (m_characterRepo.insert(character) != -1) {
				PTSharedPreferences.getInstance().putLong(
						PTSharedPreferences.KEY_LONG_SELECTED_CHARACTER_ID, character.getID());
				((PTMainActivity) getActivity()).showView(PTNavDrawerAdapter.ABILITIES_ID);
			}
			break;
		case R.id.mi_export_to_existing:
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle(getString(R.string.select_character_dialog_header));
			List<Entry<Long, String>> characterEntries = m_characterRepo.queryList();
			String[] characterNames = EntryUtils.valueArray(characterEntries);

			OnCharacterExportSelectListener exportListener = 
					new OnCharacterExportSelectListener(characterEntries);

			builder.setSingleChoiceItems(characterNames, -1,
					exportListener).setPositiveButton(R.string.ok_button_text, exportListener)
					.setNegativeButton(R.string.cancel_button_text, exportListener);

			AlertDialog alert = builder.create();
			alert.show();
			break;

		}
		return true;
	}

	private class OnCharacterExportSelectListener implements DialogInterface.OnClickListener {
		long _characterIdSelectedInDialog;
		List<Entry<Long, String>> _characterList;
		
		public OnCharacterExportSelectListener(List<Entry<Long, String>> characterIds) {
			_characterList = characterIds;
			_characterIdSelectedInDialog = 0;
		}
		
		public void onClick(DialogInterface dialogInterface, int selection) {
			switch (selection) {
			case DialogInterface.BUTTON_POSITIVE:
				if (_characterIdSelectedInDialog > 0) {
					PTFluffInfo fluff = m_fluffRepo.query(_characterIdSelectedInDialog);
					PTAbilitySet abilities = new PTAbilitySet(m_abilityRepo.querySet(_characterIdSelectedInDialog));
					m_abilityCalc.setCalculatedAbilityScores(abilities);

					Resources r = getResources();
					fluff.setRace(r.getStringArray(R.array.races_array)
							[m_racesSpinner.getSelectedItemPosition()]);

					for (int i = 0; i < abilities.size(); i++) {
						m_abilityRepo.update(abilities.getAbilityAtIndex(i));
					}
					m_fluffRepo.update(fluff);

					PTSharedPreferences.getInstance().putLong(
							PTSharedPreferences.KEY_LONG_SELECTED_CHARACTER_ID, _characterIdSelectedInDialog);
					((PTMainActivity) getActivity()).showView(PTNavDrawerAdapter.ABILITIES_ID);
				}
				break;
			case DialogInterface.BUTTON_NEGATIVE:
				break;
			default:
				// Set the currently selected character in the dialog
				_characterIdSelectedInDialog = _characterList.get(selection).getKey();
				break;

			}
		}
	}
    
    private void updateAbilityViews() {
    	TextView t = new TextView(getActivity());
		final int[] modIds = {R.id.strMod, R.id.dexMod, R.id.conMod, R.id.intMod, R.id.wisMod, R.id.chaMod};
		final int[] finalScoreIds = {R.id.finStr, R.id.finDex, R.id.finCon, R.id.finInt, R.id.finWis, R.id.finCha};
		final int[] baseScoreIds = {R.id.baseStr, R.id.baseDex, R.id.baseCon, R.id.baseInt, R.id.baseWis, R.id.baseCha};
		final int[] racialModIds = {R.id.raceStr, R.id.raceDex, R.id.raceCon, R.id.raceInt, R.id.raceWis, R.id.raceCha};
    	
		for(int i = 0; i < NUM_ABILITIES; i++) {
			t = (TextView) getRootView().findViewById(modIds[i]);
			t.setText(Integer.toString(m_abilityCalc.getModPostRaceMod(i)));

			t = (TextView) getRootView().findViewById(baseScoreIds[i]);
			t.setText(Integer.toString(m_abilityCalc.getBaseScore(i)));

			t = (TextView) getRootView().findViewById(racialModIds[i]);
			t.setText(Integer.toString(m_abilityCalc.getRaceMod(i)));
			
			t = (TextView) getRootView().findViewById(finalScoreIds[i]);
			t.setText(Integer.toString(m_abilityCalc.getScorePostRaceMod(i)));

			t = (TextView) getRootView().findViewById(R.id.textCost);
			t.setText(Integer.toString(m_abilityCalc.getPointBuyCost()));
		}
    }
    
    private class IncDecButtonListener implements Button.OnClickListener {

		@Override
		public void onClick(View v) {
			int abilityIndex = 0;
			boolean isInc = true;
			switch(v.getId()) {
			case R.id.btnIncStr:
				abilityIndex = STR_IDX;
				break;
			case R.id.btnDecStr:
				abilityIndex = STR_IDX;
				isInc = false;
				break;
			case R.id.btnIncDex:
				abilityIndex = DEX_IDX;
				break;
			case R.id.btnDecDex:
				abilityIndex = DEX_IDX;
				isInc = false;
				break;
			case R.id.btnIncCon:
				abilityIndex = CON_IDX;
				break;
			case R.id.btnDecCon:
				abilityIndex = CON_IDX;
				isInc = false;
				break;
			case R.id.btnIncInt:
				abilityIndex = INT_IDX;
				break;
			case R.id.btnDecInt:
				abilityIndex = INT_IDX;
				isInc = false;
				break;
			case R.id.btnIncWis:
				abilityIndex = WIS_IDX;
				break;
			case R.id.btnDecWis:
				abilityIndex = WIS_IDX;
				isInc = false;
				break;
			case R.id.btnIncCha:
				abilityIndex = CHA_IDX;
				break;
			case R.id.btnDecCha:
				abilityIndex = CHA_IDX;
				isInc = false;
				break;
			}
			
			if (isInc) {
				m_abilityCalc.incAbilityScore(abilityIndex);
			} else {
				m_abilityCalc.decAbilityScore(abilityIndex);
			}
	    	updateAbilityViews();
		}
    	
    }
}
