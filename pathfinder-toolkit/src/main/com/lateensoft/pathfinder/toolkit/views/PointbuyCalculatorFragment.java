package com.lateensoft.pathfinder.toolkit.views;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.db.dao.table.AbilityDAO;
import com.lateensoft.pathfinder.toolkit.db.dao.table.CharacterModelDAO;
import com.lateensoft.pathfinder.toolkit.db.dao.table.CharacterNameDAO;
import com.lateensoft.pathfinder.toolkit.db.dao.table.FluffDAO;
import com.lateensoft.pathfinder.toolkit.model.IdNamePair;
import com.lateensoft.pathfinder.toolkit.model.character.FluffInfo;
import com.lateensoft.pathfinder.toolkit.model.character.PathfinderCharacter;
import com.lateensoft.pathfinder.toolkit.model.character.stats.AbilitySet;
import com.lateensoft.pathfinder.toolkit.model.character.stats.AbilityType;
import com.lateensoft.pathfinder.toolkit.pref.GlobalPrefs;
import com.lateensoft.pathfinder.toolkit.pref.Preferences;
import com.lateensoft.pathfinder.toolkit.util.AbilitySetCalculator;
import com.lateensoft.pathfinder.toolkit.views.character.CharacterAbilitiesFragment;
import roboguice.RoboGuice;

public class PointbuyCalculatorFragment extends BasePageFragment {
    private static final String TAG = PointbuyCalculatorFragment.class.getSimpleName();
    
    static final int STR_IDX = 0;
    static final int DEX_IDX = 1;
    static final int CON_IDX = 2;
    static final int INT_IDX = 3;
    static final int WIS_IDX = 4;
    static final int CHA_IDX = 5;
    static final int NUM_ABILITIES = AbilityType.values().length;
    
    static final int CUSTOM_RACE_INDEX = 7;
    
    private static final int CUSTOM_RACE_MOD_SPINNER_OFFSET = 10;
    
    private AbilitySetCalculator abilityCalc;
    private HumanRaceModSelectedListener humanRaceListener;
    private boolean isHuman;
    
    private Button strIncBtn;
    private Button strDecBtn;
    private Button dexIncBtn;
    private Button dexDecBtn;
    private Button conIncBtn;
    private Button conDecBtn;
    private Button intIncBtn;
    private Button intDecBtn;
    private Button wisIncBtn;
    private Button wisDecBtn;
    private Button chaIncBtn;
    private Button chaDecBtn;
    
    private Spinner racesSpinner;
    
    private Spinner dialogStrSpinner;
    private Spinner dialogDexSpinner;
    private Spinner dialogConSpinner;
    private Spinner dialogIntSpinner;
    private Spinner dialogWisSpinner;
    private Spinner dialogChaSpinner;

    private CharacterModelDAO characterModelDao;
    private CharacterNameDAO characterNameDao;
    private FluffDAO fluffDao;
    private AbilityDAO abilityDao;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        characterModelDao = new CharacterModelDAO(getContext());
        characterNameDao = new CharacterNameDAO(getContext());
        fluffDao = new FluffDAO(getContext());
        abilityDao = new AbilityDAO(getContext());
    }
    
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        setRootView(inflater.inflate(R.layout.fragment_ability_calculator, container, false));

        isHuman = true;

        IncDecButtonListener buttonListener = new IncDecButtonListener();
        
        strIncBtn = (Button) getRootView().findViewById(R.id.btnIncStr);
        strIncBtn.setOnClickListener(buttonListener);
        strDecBtn = (Button) getRootView().findViewById(R.id.btnDecStr);
        strDecBtn.setOnClickListener(buttonListener);
        dexIncBtn = (Button) getRootView().findViewById(R.id.btnIncDex);
        dexIncBtn.setOnClickListener(buttonListener);
        dexDecBtn = (Button) getRootView().findViewById(R.id.btnDecDex);
        dexDecBtn.setOnClickListener(buttonListener);
        conIncBtn = (Button) getRootView().findViewById(R.id.btnIncCon);
        conIncBtn.setOnClickListener(buttonListener);
        conDecBtn = (Button) getRootView().findViewById(R.id.btnDecCon);
        conDecBtn.setOnClickListener(buttonListener);
        intIncBtn = (Button) getRootView().findViewById(R.id.btnIncInt);
        intIncBtn.setOnClickListener(buttonListener);
        intDecBtn = (Button) getRootView().findViewById(R.id.btnDecInt);
        intDecBtn.setOnClickListener(buttonListener);
        wisIncBtn = (Button) getRootView().findViewById(R.id.btnIncWis);
        wisIncBtn.setOnClickListener(buttonListener);
        wisDecBtn = (Button) getRootView().findViewById(R.id.btnDecWis);
        wisDecBtn.setOnClickListener(buttonListener);
        chaIncBtn = (Button) getRootView().findViewById(R.id.btnIncCha);
        chaIncBtn.setOnClickListener(buttonListener);
        chaDecBtn = (Button) getRootView().findViewById(R.id.btnDecCha);
        chaDecBtn.setOnClickListener(buttonListener);

        abilityCalc = new AbilitySetCalculator();

        racesSpinner = (Spinner) getRootView().findViewById(R.id.races_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.races_array, R.layout.spinner_centered);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_centered);
        // Apply the adapter to the spinner
        racesSpinner.setAdapter(adapter);
        racesSpinner.setOnItemSelectedListener(new RaceItemSelectedListener());

        humanRaceListener = new HumanRaceModSelectedListener();
        getRootView().findViewById(R.id.raceStr).setOnClickListener(humanRaceListener);
        getRootView().findViewById(R.id.raceDex).setOnClickListener(humanRaceListener);
        getRootView().findViewById(R.id.raceCon).setOnClickListener(humanRaceListener);
        getRootView().findViewById(R.id.raceInt).setOnClickListener(humanRaceListener);
        getRootView().findViewById(R.id.raceWis).setOnClickListener(humanRaceListener);
        getRootView().findViewById(R.id.raceCha).setOnClickListener(humanRaceListener);

        updateAbilityViews();

        return getRootView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.pointbuy_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void updateTitle() {
        setTitle(R.string.title_activity_ability_calc);
        setSubtitle(null);
    }

    private class RaceItemSelectedListener implements OnItemSelectedListener {
        @Override public void onItemSelected(AdapterView<?> parent, View view, 
                int pos, long id) {
            // An item was selected. You can retrieve the selected item using
            // parent.getItemAtPosition(pos)
            if(parent.getSelectedItemPosition() != CUSTOM_RACE_INDEX) {
                isHuman = abilityCalc.setRacialMods((int) parent.getSelectedItemId(), getContext());
            
                updateAbilityViews();
            } else {
                // Custom race
                showCustomRaceDialog();
            }
        }
        
        @Override public void onNothingSelected(AdapterView<?> parent) {}
    }
    
    private void showCustomRaceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        
        LayoutInflater inflater = LayoutInflater.from(getContext());
        
        View dialogView = inflater.inflate(R.layout.calculator_custom_race_dialog, null);
        dialogStrSpinner = (Spinner) dialogView.findViewById(R.id.spCustomStrMod);
        dialogDexSpinner = (Spinner) dialogView.findViewById(R.id.spCustomDexMod);
        dialogConSpinner = (Spinner) dialogView.findViewById(R.id.spCustomConMod);
        dialogIntSpinner = (Spinner) dialogView.findViewById(R.id.spCustomIntMod);
        dialogWisSpinner = (Spinner) dialogView.findViewById(R.id.spCustomWisMod);
        dialogChaSpinner = (Spinner) dialogView.findViewById(R.id.spCustomChaMod);
        
        setupCustomRaceSpinner(dialogStrSpinner);
        setupCustomRaceSpinner(dialogDexSpinner);
        setupCustomRaceSpinner(dialogConSpinner);
        setupCustomRaceSpinner(dialogIntSpinner);
        setupCustomRaceSpinner(dialogWisSpinner);
        setupCustomRaceSpinner(dialogChaSpinner);
        
        OnCustomRaceModSetListener customRaceModSetListener = new OnCustomRaceModSetListener();
        
        builder.setView(dialogView)
            .setPositiveButton(R.string.ok_button_text, customRaceModSetListener)
            .setTitle(R.string.calc_custom_race_dialog_title);
        
        AlertDialog alert = builder.create();
        alert.show();
    }
    
    private class OnCustomRaceModSetListener implements DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialogInterface, int selection) {    
            isHuman = false;
        
            getCustomRacialModSetFromDialog();
        }
    }
    
    private void getCustomRacialModSetFromDialog() {
        int[] mods = new int[NUM_ABILITIES];
                
        mods[STR_IDX] = dialogStrSpinner.getSelectedItemPosition() - CUSTOM_RACE_MOD_SPINNER_OFFSET;
        mods[DEX_IDX] = dialogDexSpinner.getSelectedItemPosition() - CUSTOM_RACE_MOD_SPINNER_OFFSET;
        mods[CON_IDX] = dialogConSpinner.getSelectedItemPosition() - CUSTOM_RACE_MOD_SPINNER_OFFSET;
        mods[INT_IDX] = dialogIntSpinner.getSelectedItemPosition() - CUSTOM_RACE_MOD_SPINNER_OFFSET;
        mods[WIS_IDX] = dialogWisSpinner.getSelectedItemPosition() - CUSTOM_RACE_MOD_SPINNER_OFFSET;
        mods[CHA_IDX] = dialogChaSpinner.getSelectedItemPosition() - CUSTOM_RACE_MOD_SPINNER_OFFSET;
        
        abilityCalc.setCustomRaceMods(mods);
        updateAbilityViews();
    }

    private void setupCustomRaceSpinner(Spinner spinner) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.calc_custom_race_mod_options, android.R.layout.simple_spinner_item);
        
        adapter.setDropDownViewResource(R.layout.spinner_plain);
        spinner.setAdapter(adapter);
        spinner.setSelection(CUSTOM_RACE_MOD_SPINNER_OFFSET);
    }

    private class HumanRaceModSelectedListener implements OnClickListener {
        public void onClick(View v) {
            int id = v.getId();
            
            if(!isHuman) {
                return;
            }
            
            switch (id) {
            case R.id.raceStr:
                abilityCalc.setHumanRacialMods(STR_IDX, getContext());
                break;
            case R.id.raceDex:
                abilityCalc.setHumanRacialMods(DEX_IDX, getContext());
                break;
            case R.id.raceCon:
                abilityCalc.setHumanRacialMods(CON_IDX, getContext());
                break;
            case R.id.raceInt:
                abilityCalc.setHumanRacialMods(INT_IDX, getContext());
                break;
            case R.id.raceWis:
                abilityCalc.setHumanRacialMods(WIS_IDX, getContext());
                break;
            case R.id.raceCha:
                abilityCalc.setHumanRacialMods(CHA_IDX, getContext());
                break;
            }
            
            updateAbilityViews();
        }
        
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
        case R.id.mi_export_to_new:
            PathfinderCharacter character = PathfinderCharacter.newDefaultCharacter("From calc");
            abilityCalc.setCalculatedAbilityScores(character.getAbilitySet());
            Resources r = getResources();
            character.getFluff().setRace(r.getStringArray(R.array.races_array)
                    [racesSpinner.getSelectedItemPosition()]);

            try {
                characterModelDao.add(character);
                setSelectedCharacter(character.getId());
                switchToPage(CharacterAbilitiesFragment.class);
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
            break;
        case R.id.mi_export_to_existing:
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(getString(R.string.select_character_dialog_header));
            List<IdNamePair> characterEntries = characterNameDao.findAll();
            String[] characterNames = IdNamePair.nameArray(characterEntries);

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

    private void setSelectedCharacter(long characterId) {
        Preferences preferences = RoboGuice.getInjector(getContext()).getInstance(Preferences.class);
        preferences.put(GlobalPrefs.SELECTED_CHARACTER_ID, characterId);
    }

    private class OnCharacterExportSelectListener implements DialogInterface.OnClickListener {
        long characterIdSelectedInDialog;
        List<IdNamePair> _characterList;
        
        public OnCharacterExportSelectListener(List<IdNamePair> characterIds) {
            _characterList = characterIds;
            characterIdSelectedInDialog = 0;
        }
        
        public void onClick(DialogInterface dialogInterface, int selection) {
            switch (selection) {
            case DialogInterface.BUTTON_POSITIVE:
                if (characterIdSelectedInDialog > 0) {
                    FluffInfo fluff = fluffDao.find(characterIdSelectedInDialog);
                    AbilitySet abilities = new AbilitySet(abilityDao.findAllForOwner(characterIdSelectedInDialog), null);
                    abilityCalc.setCalculatedAbilityScores(abilities);

                    Resources r = getResources();
                    fluff.setRace(r.getStringArray(R.array.races_array)
                            [racesSpinner.getSelectedItemPosition()]);

                    try {
                        for (int i = 0; i < abilities.size(); i++) {
                            abilityDao.update(characterIdSelectedInDialog, abilities.getAbilityAtIndex(i));
                        }
                        fluffDao.update(characterIdSelectedInDialog, fluff);

                        setSelectedCharacter(characterIdSelectedInDialog);
                        switchToPage(CharacterAbilitiesFragment.class);
                    } catch (DataAccessException e) {
                        String message = "Failed to update character";
                        Log.e(TAG, message, e);
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                break;
            default:
                // Set the currently selected character in the dialog
                characterIdSelectedInDialog = _characterList.get(selection).getId();
                break;

            }
        }
    }
    
    private void updateAbilityViews() {
        TextView textView;
        final int[] modIds = {R.id.strMod, R.id.dexMod, R.id.conMod, R.id.intMod, R.id.wisMod, R.id.chaMod};
        final int[] finalScoreIds = {R.id.finStr, R.id.finDex, R.id.finCon, R.id.finInt, R.id.finWis, R.id.finCha};
        final int[] baseScoreIds = {R.id.baseStr, R.id.baseDex, R.id.baseCon, R.id.baseInt, R.id.baseWis, R.id.baseCha};
        final int[] racialModIds = {R.id.raceStr, R.id.raceDex, R.id.raceCon, R.id.raceInt, R.id.raceWis, R.id.raceCha};
        
        for(int i = 0; i < NUM_ABILITIES; i++) {
            textView = (TextView) getRootView().findViewById(modIds[i]);
            textView.setText(Integer.toString(abilityCalc.getModPostRaceMod(i)));

            textView = (TextView) getRootView().findViewById(baseScoreIds[i]);
            textView.setText(Integer.toString(abilityCalc.getBaseScore(i)));

            textView = (TextView) getRootView().findViewById(racialModIds[i]);
            textView.setText(Integer.toString(abilityCalc.getRaceMod(i)));
            
            textView = (TextView) getRootView().findViewById(finalScoreIds[i]);
            textView.setText(Integer.toString(abilityCalc.getScorePostRaceMod(i)));

            textView = (TextView) getRootView().findViewById(R.id.textCost);
            textView.setText(Integer.toString(abilityCalc.getPointBuyCost()));
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
                abilityCalc.incAbilityScore(abilityIndex);
            } else {
                abilityCalc.decAbilityScore(abilityIndex);
            }
            updateAbilityViews();
        }
        
    }
}
