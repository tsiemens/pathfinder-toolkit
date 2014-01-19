package com.lateensoft.pathfinder.toolkit.views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
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
import com.lateensoft.pathfinder.toolkit.db.IDNamePair;
import com.lateensoft.pathfinder.toolkit.db.repository.PTAbilityScoreRepository;
import com.lateensoft.pathfinder.toolkit.db.repository.PTCharacterRepository;
import com.lateensoft.pathfinder.toolkit.db.repository.PTFluffInfoRepository;
import com.lateensoft.pathfinder.toolkit.model.character.PTCharacter;
import com.lateensoft.pathfinder.toolkit.model.character.PTFluffInfo;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTAbilityModSet;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTAbilitySet;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTAbilitySetCalc;
import com.lateensoft.pathfinder.toolkit.utils.PTDiceSet;

public class PTPointbuyCalculatorFragment extends PTBasePageFragment {
	
	static final int STR_KEY = 0;
	static final int DEX_KEY = 1;
	static final int CON_KEY = 2;
	static final int INT_KEY = 3;
	static final int WIS_KEY = 4;
	static final int CHA_KEY = 5;
	static final int NUM_ABILITIES = 6;
	
	static final int MENU_ITEM_EXPORT_TO_NEW = 0;
	private static final int MENU_ITEM_EXPORT_TO_EXISTING = 1;
	
	static final int CUSTOM_RACE_INDEX = 7;
	
	private PTAbilitySetCalc m_abilitySet;
	private PTAbilityModSet m_racialModSet;
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
	private PTAbilityScoreRepository m_abilityRepo;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		m_characterRepo = new PTCharacterRepository();
		m_fluffRepo = new PTFluffInfoRepository();
		m_abilityRepo = new PTAbilityScoreRepository();
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

		m_abilitySet = new PTAbilitySetCalc();
		m_racialModSet = new PTAbilityModSet();

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

		for(int i = 0; i < NUM_ABILITIES; i++) {
			updateAbilityViews(i);
		}
		updateRaceModsColumn();

		return getRootView();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem exportAbilitySetToNew = menu.add(Menu.NONE,
				MENU_ITEM_EXPORT_TO_NEW, Menu.NONE,
				R.string.export_to_new_character);
		exportAbilitySetToNew.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		
		MenuItem exportAbilitySetToExisting = menu.add(Menu.NONE,
				MENU_ITEM_EXPORT_TO_EXISTING, Menu.NONE,
				R.string.export_to_existing_character);
		exportAbilitySetToExisting.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		
		super.onCreateOptionsMenu(menu);
		return true;
	}
	
	private class RaceItemSelectedListener implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> parent, View view, 
		        int pos, long id) {
		    // An item was selected. You can retrieve the selected item using
		    // parent.getItemAtPosition(pos)
			if(parent.getSelectedItemPosition() != CUSTOM_RACE_INDEX) {
				m_isHuman = m_racialModSet.setRacialMods((int) parent.getSelectedItemId(),
						getActivity());
			
				m_abilitySet.applyMods(m_racialModSet);
				
				updateRaceModsColumn();
				for(int i = 0; i < NUM_ABILITIES; i++) {
					updateAbilityViews(i);
				}
			} else {
				// Custom race
				showCustomRaceDialog();
			}
		}
		
		public void onNothingSelected(AdapterView<?> parent) {
		    // Another interface callback
		}
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
		
		onCustomRaceModSetListener customRaceModSetListener = new onCustomRaceModSetListener();
		
		builder.setView(dialogView)
			.setPositiveButton(R.string.ok_button_text, customRaceModSetListener)
			.setTitle(R.string.calc_custom_race_dialog_title);
		
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	private class onCustomRaceModSetListener implements DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialogInterface, int selection) {
			m_racialModSet = getRacialModSetFromDialog();
			
			m_isHuman = false;
		
			m_abilitySet.applyMods(m_racialModSet);
			
			updateRaceModsColumn();
			for(int i = 0; i < NUM_ABILITIES; i++) {
				updateAbilityViews(i);
			}
		}
	}
	
	private PTAbilityModSet getRacialModSetFromDialog() {
		int[] mods = new int[NUM_ABILITIES];
		
		Resources r = getResources();
		int offset = r.getInteger(R.integer.calc_custom_option_offset);
				
		mods[STR_KEY] = m_dialogStrSpinner.getSelectedItemPosition() - offset;
		mods[DEX_KEY] = m_dialogDexSpinner.getSelectedItemPosition() - offset;
		mods[CON_KEY] = m_dialogConSpinner.getSelectedItemPosition() - offset;
		mods[INT_KEY] = m_dialogIntSpinner.getSelectedItemPosition() - offset;
		mods[WIS_KEY] = m_dialogWisSpinner.getSelectedItemPosition() - offset;
		mods[CHA_KEY] = m_dialogChaSpinner.getSelectedItemPosition() - offset;
		
		PTAbilityModSet racialModSet = new PTAbilityModSet();
		racialModSet.setMods(mods);
				
		return racialModSet;
	}

	private void setupCustomRaceSpinner(Spinner spinner) {
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
				R.array.calc_custom_race_mod_options, android.R.layout.simple_spinner_item);
		
		Resources r = getResources();
		
		adapter.setDropDownViewResource(R.layout.spinner_plain);
		spinner.setAdapter(adapter);
		spinner.setSelection(r.getInteger(R.integer.calc_custom_option_offset));
	}

	private class HumanRaceModSelectedListener implements OnClickListener {
		public void onClick(View v) {
			Resources r = getResources();
			int id = v.getId();
			
			if(m_isHuman == false) {
				return;
			}
			
			switch (id) {
			case R.id.raceStr:
				m_racialModSet.setHumanRacialMods(r.getInteger(R.integer.key_strength), 
						getActivity());
				break;
			case R.id.raceDex:
				m_racialModSet.setHumanRacialMods(r.getInteger(R.integer.key_dexterity), 
						getActivity());
				break;
			case R.id.raceCon:
				m_racialModSet.setHumanRacialMods(r.getInteger(R.integer.key_constitution), 
						getActivity());
				break;
			case R.id.raceInt:
				m_racialModSet.setHumanRacialMods(r.getInteger(R.integer.key_intelligence), 
						getActivity());
				break;
			case R.id.raceWis:
				m_racialModSet.setHumanRacialMods(r.getInteger(R.integer.key_wisdom), 
						getActivity());
				break;
			case R.id.raceCha:
				m_racialModSet.setHumanRacialMods(r.getInteger(R.integer.key_charisma), 
						getActivity());
				break;
			}
			
			m_abilitySet.applyMods(m_racialModSet);
			updateRaceModsColumn();
			for(int i = 0; i < NUM_ABILITIES; i++) {
				updateAbilityViews(i);
			}
		}
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case MENU_ITEM_EXPORT_TO_NEW:
			PTCharacter character = new PTCharacter("From calc", getActivity());
			character.setAbilitySet(m_abilitySet.getAbilitySetPostMods());
			Resources r = getResources();
			character.getFluff().setRace(r.getStringArray(R.array.races_array)
					[m_racesSpinner.getSelectedItemPosition()]);

			if (m_characterRepo.insert(character) != -1) {
				PTSharedPreferences.getInstance().putLong(
						PTSharedPreferences.KEY_LONG_SELECTED_CHARACTER_ID, character.getID());
				((PTMainActivity) getActivity()).showView(PTNavDrawerAdapter.ABILITIES_ID);
			}
			break;
		case MENU_ITEM_EXPORT_TO_EXISTING:
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle(getString(R.string.select_character_dialog_header));
			IDNamePair[] characterIDs = m_characterRepo.queryList();
			String[] characterList = IDNamePair.toNameArray(characterIDs);

			onCharacterExportSelectListener exportListener = 
					new onCharacterExportSelectListener(characterIDs);

			builder.setSingleChoiceItems(characterList, -1,
					exportListener).setPositiveButton(R.string.ok_button_text, exportListener)
					.setNegativeButton(R.string.cancel_button_text, exportListener);

			AlertDialog alert = builder.create();
			alert.show();
			break;

		}
		return true;
	}

	private class onCharacterExportSelectListener implements DialogInterface.OnClickListener {
		long _characterSelectedInDialog;
		IDNamePair[] _characterList;
		
		public onCharacterExportSelectListener(IDNamePair[] characterIds) {
			_characterList = characterIds;
		}
		
		public void onClick(DialogInterface dialogInterface, int selection) {
			switch (selection) {
			case DialogInterface.BUTTON_POSITIVE:
				PTFluffInfo fluff = m_fluffRepo.query(_characterSelectedInDialog);
				PTAbilitySet abilities = new PTAbilitySet(m_abilityRepo.querySet(_characterSelectedInDialog, false));
				abilities.setScores(m_abilitySet.getScoresPostMods());
				Resources r = getResources();
				fluff.setRace(r.getStringArray(R.array.races_array)
						[m_racesSpinner.getSelectedItemPosition()]);
				
				for (int i = 0; i < abilities.size(); i++) {
					m_abilityRepo.update(abilities.getAbilityAtIndex(i));
				}
				m_fluffRepo.update(fluff);
				
				PTSharedPreferences.getInstance().putLong(
						PTSharedPreferences.KEY_LONG_SELECTED_CHARACTER_ID, _characterSelectedInDialog);
				((PTMainActivity) getActivity()).showView(PTNavDrawerAdapter.ABILITIES_ID);
				break;
			case DialogInterface.BUTTON_NEGATIVE:
				break;
			default:
				// Set the currently selected character in the dialog
				_characterSelectedInDialog = _characterList[selection].getID();
				break;

			}
		}
	}
    
    private void updateRaceModsColumn() {
		TextView t = new TextView(getActivity());
		int[] abilityMods = m_racialModSet.getMods();
		int[] racialModIds = {R.id.raceStr, R.id.raceDex, R.id.raceCon, R.id.raceInt, R.id.raceWis, R.id.raceCha};
		
		for(int i = 0; i < NUM_ABILITIES; i++) {
			t = (TextView) getRootView().findViewById(racialModIds[i]);
			t.setText(Integer.toString(abilityMods[i]));
    	}
	}
    
    private void updateAbilityViews(int abilityKey) {
    	TextView t = new TextView(getActivity());
		int[] modIds = {R.id.strMod, R.id.dexMod, R.id.conMod, R.id.intMod, R.id.wisMod, R.id.chaMod};
		int[] finalScoreIds = {R.id.finStr, R.id.finDex, R.id.finCon, R.id.finInt, R.id.finWis, R.id.finCha};
		int[] baseScoreIds = {R.id.baseStr, R.id.baseDex, R.id.baseCon, R.id.baseInt, R.id.baseWis, R.id.baseCha};
		m_abilitySet.applyMods(m_racialModSet);
    	
		t = (TextView) getRootView().findViewById(modIds[abilityKey]);
		t.setText(Integer.toString(m_abilitySet.getAbilityScorePostMod(abilityKey).getAbilityModifier()));
		
		t = (TextView) getRootView().findViewById(baseScoreIds[abilityKey]);
		t.setText(Integer.toString(m_abilitySet.getAbilityAtIndex(abilityKey).getScore()));
		
		t = (TextView) getRootView().findViewById(finalScoreIds[abilityKey]);
		t.setText(Integer.toString(m_abilitySet.getAbilityScorePostMod(abilityKey).getScore()));
		
		t = (TextView) getRootView().findViewById(R.id.textCost);
		t.setText(Integer.toString(m_abilitySet.getPointBuyCost()));
    }
    
    private PTAbilitySetCalc rollXdYDropZ(int x, int y, int z) {
    	PTAbilitySetCalc abilitySet = new PTAbilitySetCalc();
    	PTDiceSet dice = new PTDiceSet();
    	int[] rollSet = dice.multiRollWithResults(x, y);
    	int[] finalRollSet = new int[x - z];
    	int temp = 1;
    	for(int i = 0; i < rollSet.length; i++) {
    		while(temp > 0) {
    			temp = arrayInsertKeepHighest(rollSet[i], finalRollSet);
    		}
    	}
    	
    	return abilitySet;
    }
    
    private int arrayInsertKeepHighest(int insert, int[] array) {
    	int temp = -1;
    	for(int i = 0; i < array.length; i++) {
    		if(insert > array[i]){
    			temp = array[i];
    			array[i] = insert;
    		}
    	}
    	
    	return temp;
    }
    
    private class IncDecButtonListener implements Button.OnClickListener {

		@Override
		public void onClick(View v) {
			int abilityKey = 0;
			boolean isInc = true;
			switch(v.getId()) {
			case R.id.btnIncStr:
				abilityKey = STR_KEY;
				break;
			case R.id.btnDecStr:
				abilityKey = STR_KEY;
				isInc = false;
				break;
			case R.id.btnIncDex:
				abilityKey = DEX_KEY;
				break;
			case R.id.btnDecDex:
				abilityKey = DEX_KEY;
				isInc = false;
				break;
			case R.id.btnIncCon:
				abilityKey = CON_KEY;
				break;
			case R.id.btnDecCon:
				abilityKey = CON_KEY;
				isInc = false;
				break;
			case R.id.btnIncInt:
				abilityKey = INT_KEY;
				break;
			case R.id.btnDecInt:
				abilityKey = INT_KEY;
				isInc = false;
				break;
			case R.id.btnIncWis:
				abilityKey = WIS_KEY;
				break;
			case R.id.btnDecWis:
				abilityKey = WIS_KEY;
				isInc = false;
				break;
			case R.id.btnIncCha:
				abilityKey = CHA_KEY;
				break;
			case R.id.btnDecCha:
				abilityKey = CHA_KEY;
				isInc = false;
				break;
			}
			
			if (isInc) {
				m_abilitySet.getAbilityAtIndex(abilityKey).incScore();
			} else {
				m_abilitySet.getAbilityAtIndex(abilityKey).decScore();
			}
	    	updateAbilityViews(abilityKey);
		}
    	
    }
}
