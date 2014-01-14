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
import android.widget.Spinner;
import android.widget.TextView;

import com.lateensoft.pathfinder.toolkit.PTMainActivity;
import com.lateensoft.pathfinder.toolkit.PTSharedPreferences;
import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.R.array;
import com.lateensoft.pathfinder.toolkit.R.id;
import com.lateensoft.pathfinder.toolkit.R.integer;
import com.lateensoft.pathfinder.toolkit.R.layout;
import com.lateensoft.pathfinder.toolkit.R.string;
import com.lateensoft.pathfinder.toolkit.adapters.PTNavDrawerAdapter;
import com.lateensoft.pathfinder.toolkit.db.PTDatabaseManager;
import com.lateensoft.pathfinder.toolkit.model.character.PTCharacter;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTAbilityModSet;
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
	
	PTAbilitySetCalc mAbilitySet;
	PTAbilityModSet mRacialModSet;
	HumanRaceModSelectedListener mHumanRaceListener;
	boolean isHuman;
	
	Spinner mRacesSpinner;
	
	Spinner mDialogStrSpinner;
	Spinner mDialogDexSpinner;
	Spinner mDialogConSpinner;
	Spinner mDialogIntSpinner;
	Spinner mDialogWisSpinner;
	Spinner mDialogChaSpinner;
	
	PTDatabaseManager mSQLManager;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		setRootView(inflater.inflate(R.layout.fragment_ability_calculator, container, false));
		setTitle(R.string.title_activity_ability_calc);
		setSubtitle(null);

		isHuman = true;
		
		mAbilitySet = new PTAbilitySetCalc();
		mRacialModSet = new PTAbilityModSet();
		
		mRacesSpinner = (Spinner) getRootView().findViewById(R.id.races_spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
		        R.array.races_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(R.layout.spinner_plain);
		// Apply the adapter to the spinner
		mRacesSpinner.setAdapter(adapter);
		mRacesSpinner.setOnItemSelectedListener(new RaceItemSelectedListener());
		
		mHumanRaceListener = new HumanRaceModSelectedListener();
		getRootView().findViewById(R.id.raceStr).setOnClickListener(mHumanRaceListener);
		getRootView().findViewById(R.id.raceDex).setOnClickListener(mHumanRaceListener);
		getRootView().findViewById(R.id.raceCon).setOnClickListener(mHumanRaceListener);
		getRootView().findViewById(R.id.raceInt).setOnClickListener(mHumanRaceListener);
		getRootView().findViewById(R.id.raceWis).setOnClickListener(mHumanRaceListener);
		getRootView().findViewById(R.id.raceCha).setOnClickListener(mHumanRaceListener);
		
		for(int i = 0; i < NUM_ABILITIES; i++) {
			updateInterfaceAbility(i);
		}
		updateInterfaceRaceMods();
		
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
	
	public class RaceItemSelectedListener implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> parent, View view, 
		        int pos, long id) {
		    // An item was selected. You can retrieve the selected item using
		    // parent.getItemAtPosition(pos)
			if(parent.getSelectedItemPosition() != CUSTOM_RACE_INDEX) {
				isHuman = mRacialModSet.setRacialMods((int) parent.getSelectedItemId(),
						getActivity());
			
				mAbilitySet.applyMods(mRacialModSet);
				
				updateInterfaceRaceMods();
				for(int i = 0; i < NUM_ABILITIES; i++) {
					updateInterfaceAbility(i);
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
		mDialogStrSpinner = (Spinner) dialogView.findViewById(R.id.spCustomStrMod);
		mDialogDexSpinner = (Spinner) dialogView.findViewById(R.id.spCustomDexMod);
		mDialogConSpinner = (Spinner) dialogView.findViewById(R.id.spCustomConMod);
		mDialogIntSpinner = (Spinner) dialogView.findViewById(R.id.spCustomIntMod);
		mDialogWisSpinner = (Spinner) dialogView.findViewById(R.id.spCustomWisMod);
		mDialogChaSpinner = (Spinner) dialogView.findViewById(R.id.spCustomChaMod);
		
		setupCustomRaceSpinner(mDialogStrSpinner);
		setupCustomRaceSpinner(mDialogDexSpinner);
		setupCustomRaceSpinner(mDialogConSpinner);
		setupCustomRaceSpinner(mDialogIntSpinner);
		setupCustomRaceSpinner(mDialogWisSpinner);
		setupCustomRaceSpinner(mDialogChaSpinner);
		
		onCustomRaceModSetListener customRaceModSetListener = new onCustomRaceModSetListener();
		
		builder.setView(dialogView)
			.setPositiveButton(R.string.ok_button_text, customRaceModSetListener)
			.setTitle(R.string.calc_custom_race_dialog_title);
		
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	public class onCustomRaceModSetListener implements DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialogInterface, int selection) {
			mRacialModSet = getRacialModSetFromDialog();
			
			isHuman = false;
		
			mAbilitySet.applyMods(mRacialModSet);
			
			updateInterfaceRaceMods();
			for(int i = 0; i < NUM_ABILITIES; i++) {
				updateInterfaceAbility(i);
			}
		}
	}
	
	private PTAbilityModSet getRacialModSetFromDialog() {
		int[] mods = new int[NUM_ABILITIES];
		
		Resources r = getResources();
		int offset = r.getInteger(R.integer.calc_custom_option_offset);
				
		mods[STR_KEY] = mDialogStrSpinner.getSelectedItemPosition() - offset;
		mods[DEX_KEY] = mDialogDexSpinner.getSelectedItemPosition() - offset;
		mods[CON_KEY] = mDialogConSpinner.getSelectedItemPosition() - offset;
		mods[INT_KEY] = mDialogIntSpinner.getSelectedItemPosition() - offset;
		mods[WIS_KEY] = mDialogWisSpinner.getSelectedItemPosition() - offset;
		mods[CHA_KEY] = mDialogChaSpinner.getSelectedItemPosition() - offset;
		
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

	public class HumanRaceModSelectedListener implements OnClickListener {
		public void onClick(View v) {
			Resources r = getResources();
			int id = v.getId();
			
			if(isHuman == false) {
				return;
			}
			
			switch (id) {
			case R.id.raceStr:
				mRacialModSet.setHumanRacialMods(r.getInteger(R.integer.key_strength), 
						getActivity());
				break;
			case R.id.raceDex:
				mRacialModSet.setHumanRacialMods(r.getInteger(R.integer.key_dexterity), 
						getActivity());
				break;
			case R.id.raceCon:
				mRacialModSet.setHumanRacialMods(r.getInteger(R.integer.key_constitution), 
						getActivity());
				break;
			case R.id.raceInt:
				mRacialModSet.setHumanRacialMods(r.getInteger(R.integer.key_intelligence), 
						getActivity());
				break;
			case R.id.raceWis:
				mRacialModSet.setHumanRacialMods(r.getInteger(R.integer.key_wisdom), 
						getActivity());
				break;
			case R.id.raceCha:
				mRacialModSet.setHumanRacialMods(r.getInteger(R.integer.key_charisma), 
						getActivity());
				break;
			}
			
			mAbilitySet.applyMods(mRacialModSet);
			updateInterfaceRaceMods();
			for(int i = 0; i < NUM_ABILITIES; i++) {
				updateInterfaceAbility(i);
			}
		}
		
		public void onNothingSelected(AdapterView<?> parent) {
			//deerrrr
		}

	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		mSQLManager = new PTDatabaseManager(getActivity());

		switch (item.getItemId()) {
		case MENU_ITEM_EXPORT_TO_NEW:
			PTCharacter character = mSQLManager.addNewCharacter("From calc", getActivity());
			character.setAbilitySet(mAbilitySet.getAbilitySetPostMods());
			Resources r = getResources();
			character.getFluff().setRace(r.getStringArray(R.array.races_array)
					[mRacesSpinner.getSelectedItemPosition()]);

			PTSharedPreferences.getInstance().putLong(
					PTSharedPreferences.KEY_LONG_SELECTED_CHARACTER_ID, character.getID());
			mSQLManager.updateCharacter(character);
			((PTMainActivity) getActivity()).showView(PTNavDrawerAdapter.ABILITIES_ID);
			break;
		case MENU_ITEM_EXPORT_TO_EXISTING:
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle(getString(R.string.select_character_dialog_header));
			String[] characterList = mSQLManager.getCharacterNames();

			onCharacterExportSelectListener exportListener = 
					new onCharacterExportSelectListener();

			builder.setSingleChoiceItems(characterList, -1,
					exportListener).setPositiveButton(R.string.ok_button_text, exportListener)
					.setNegativeButton(R.string.cancel_button_text, exportListener);

			AlertDialog alert = builder.create();
			alert.show();
			break;

		}
		return true;
	}

	public class onCharacterExportSelectListener implements DialogInterface.OnClickListener {
		int mCharacterSelectedInDialog;
		
		public void onClick(DialogInterface dialogInterface, int selection) {
			switch (selection) {
			case DialogInterface.BUTTON_POSITIVE:
				PTCharacter character = mSQLManager.getCharacter(mCharacterSelectedInDialog);
				character.setAbilitySet(mAbilitySet.getAbilitySetPostMods());
				Resources r = getResources();
				character.getFluff().setRace(r.getStringArray(R.array.races_array)
						[mRacesSpinner.getSelectedItemPosition()]);
				
				PTSharedPreferences.getInstance().putLong(
						PTSharedPreferences.KEY_LONG_SELECTED_CHARACTER_ID, character.getID());
				mSQLManager.updateCharacter(character);
				((PTMainActivity) getActivity()).showView(PTNavDrawerAdapter.ABILITIES_ID);
				break;
			case DialogInterface.BUTTON_NEGATIVE:
				break;
			default:
				// Set the currently selected character in the dialog
				mCharacterSelectedInDialog = mSQLManager.getCharacterIDs()[selection];
				break;

			}
		}
	}
    
    public void updateInterfaceRaceMods() {
		TextView t = new TextView(getActivity());
		int[] abilityMods = mRacialModSet.getMods();
		int[] racialModIds = {R.id.raceStr, R.id.raceDex, R.id.raceCon, R.id.raceInt, R.id.raceWis, R.id.raceCha};
		
		for(int i = 0; i < NUM_ABILITIES; i++) {
			t = (TextView) getRootView().findViewById(racialModIds[i]);
			t.setText(Integer.toString(abilityMods[i]));
    	}
	}
    
    public void updateInterfaceAbility(int key) {
    	TextView t = new TextView(getActivity());
		int[] modIds = {R.id.strMod, R.id.dexMod, R.id.conMod, R.id.intMod, R.id.wisMod, R.id.chaMod};
		int[] finalScoreIds = {R.id.finStr, R.id.finDex, R.id.finCon, R.id.finInt, R.id.finWis, R.id.finCha};
		int[] baseScoreIds = {R.id.baseStr, R.id.baseDex, R.id.baseCon, R.id.baseInt, R.id.baseWis, R.id.baseCha};
		mAbilitySet.applyMods(mRacialModSet);
    	
		t = (TextView) getRootView().findViewById(modIds[key]);
		t.setText(Integer.toString(mAbilitySet.getAbilityScorePostMod(key).getModifier()));
		
		t = (TextView) getRootView().findViewById(baseScoreIds[key]);
		t.setText(Integer.toString(mAbilitySet.getAbilityScore(key).getScore()));
		
		t = (TextView) getRootView().findViewById(finalScoreIds[key]);
		t.setText(Integer.toString(mAbilitySet.getAbilityScorePostMod(key).getScore()));
		
		t = (TextView) getRootView().findViewById(R.id.textCost);
		t.setText(Integer.toString(mAbilitySet.getPointBuyCost()));
    }
    
    public void incStr(View v) {
    	mAbilitySet.getAbilityScore(STR_KEY).incScore();
    	updateInterfaceAbility(STR_KEY);
    }
    
    public void decStr(View v) {
    	mAbilitySet.getAbilityScore(STR_KEY).decScore();
    	updateInterfaceAbility(STR_KEY);
    }
    
    public void incDex(View v) {
    	mAbilitySet.getAbilityScore(DEX_KEY).incScore();
    	updateInterfaceAbility(DEX_KEY);
    }
    
    public void decDex(View v) {
    	mAbilitySet.getAbilityScore(DEX_KEY).decScore();
    	updateInterfaceAbility(DEX_KEY);
    }
    
    public void incCon(View v) {
    	mAbilitySet.getAbilityScore(CON_KEY).incScore();
    	updateInterfaceAbility(CON_KEY);
    }
    
    public void decCon(View v) {
    	mAbilitySet.getAbilityScore(CON_KEY).decScore();
    	updateInterfaceAbility(CON_KEY);
    }
    
    public void incInt(View v) {
    	mAbilitySet.getAbilityScore(INT_KEY).incScore();
    	updateInterfaceAbility(INT_KEY);
    }
    
    public void decInt(View v) {
    	mAbilitySet.getAbilityScore(INT_KEY).decScore();
    	updateInterfaceAbility(INT_KEY);
    }
    
    public void incWis(View v) {
    	mAbilitySet.getAbilityScore(WIS_KEY).incScore();
    	updateInterfaceAbility(WIS_KEY);
    }
    
    public void decWis(View v) {
    	mAbilitySet.getAbilityScore(WIS_KEY).decScore();
    	updateInterfaceAbility(WIS_KEY);
    }
    
    public void incCha(View v) {
    	mAbilitySet.getAbilityScore(CHA_KEY).incScore();
    	updateInterfaceAbility(CHA_KEY);
    }
    
    public void decCha(View v) {
    	mAbilitySet.getAbilityScore(CHA_KEY).decScore();
    	updateInterfaceAbility(CHA_KEY);
    }
    
    public PTAbilitySetCalc rollXdYDropZ(int x, int y, int z) {
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
}
