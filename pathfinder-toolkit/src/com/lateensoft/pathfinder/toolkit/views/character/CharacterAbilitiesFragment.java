package com.lateensoft.pathfinder.toolkit.views.character;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.db.repository.AbilityRepository;
import com.lateensoft.pathfinder.toolkit.db.repository.ArmorRepository;
import com.lateensoft.pathfinder.toolkit.model.character.stats.AbilitySet;

// Untested
public class CharacterAbilitiesFragment extends AbstractCharacterSheetFragment {
	@SuppressWarnings("unused")
	private static final String TAG = CharacterAbilitiesFragment.class.getSimpleName();

	private final int[] baseModViewIds = {R.id.strMod, R.id.dexMod, R.id.conMod, R.id.intMod, 
			R.id.wisMod, R.id.chaMod};
	private final int[] tempBonusSpinnerIds = {R.id.strTempScore, R.id.dexTempScore, R.id.conTempScore, 
			R.id.intTempScore, R.id.wisTempScore, R.id.chaTempScore};
	private final int[] baseScoreSpinnerIds = {R.id.baseStr, R.id.baseDex, R.id.baseCon, R.id.baseInt,
			R.id.baseWis, R.id.baseCha};
	private final int[] tempModViewIds = {R.id.strTempMod, R.id.dexTempMod, R.id.conTempMod,
			R.id.intTempMod, R.id.wisTempMod, R.id.chaTempMod};	

	// selected index - TEMP_SPINNER_INDEX_OFFSET = selected value
	private final int TEMP_SPINNER_INDEX_OFFSET = 20;

	private AbilityItemSelectedListener[] m_abilityItemSelectedListeners;
	private AbilityItemSelectedListener[] m_tempAbilityItemSelectedListeners;
	//LayoutInflater mInflater;
	private Spinner[] m_baseScoreSpinners;
	private Spinner[] m_tempScoreSpinners;

	private AbilityRepository m_abilityRepo;

	private ArmorRepository m_armorRepo;
	private int m_maxDex = Integer.MAX_VALUE;

	private AbilitySet m_abilityScores;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		m_abilityRepo = new AbilityRepository();
		m_armorRepo = new ArmorRepository();

		m_baseScoreSpinners = new Spinner[baseScoreSpinnerIds.length];
		m_tempScoreSpinners = new Spinner[tempBonusSpinnerIds.length];

		m_abilityItemSelectedListeners = new AbilityItemSelectedListener[baseModViewIds.length];
		m_tempAbilityItemSelectedListeners = new AbilityItemSelectedListener[baseModViewIds.length];
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setRootView(inflater.inflate(R.layout.character_abilities_fragment,
				container, false));

		setupSpinners(m_baseScoreSpinners, baseScoreSpinnerIds, m_abilityItemSelectedListeners, false);
		setupSpinners(m_tempScoreSpinners, tempBonusSpinnerIds, m_tempAbilityItemSelectedListeners, true);

		return getRootView();
	}

	private void updateSpinners() {
		for(int i = 0; i < baseModViewIds.length; i++) {
			updateSpinnerValues(i);
		}
	}

	private void updateSpinnerValues(int abilityIndex) {
    	Spinner s;
    	TextView tv;

		tv = (TextView) getRootView().findViewById(baseModViewIds[abilityIndex]);
		tv.setText(String.valueOf(m_abilityScores.getAbilityAtIndex(abilityIndex).getAbilityModifier()));
		
		s = (Spinner) getRootView().findViewById(baseScoreSpinnerIds[abilityIndex]);
		s.setSelection((m_abilityScores.getAbilityAtIndex(abilityIndex).getScore()));
		
		s = (Spinner) getRootView().findViewById(tempBonusSpinnerIds[abilityIndex]);
		s.setSelection((m_abilityScores
				.getAbilityAtIndex(abilityIndex).getTempBonus() + TEMP_SPINNER_INDEX_OFFSET));
		
		tv = (TextView) getRootView().findViewById(tempModViewIds[abilityIndex]);
		tv.setText(String.valueOf(m_abilityScores
				.getAbilityAtIndex(abilityIndex).getTempModifier()));
	}

	private void setupSpinners(Spinner[] spinners, int viewIds[], 
		AbilityItemSelectedListener[] listeners, boolean isTemp) {
		ArrayAdapter<CharSequence> adapter;
		if (isTemp) {
			adapter = ArrayAdapter.createFromResource(getContext(),
					R.array.selectable_integer_values_strings, R.layout.spinner_centered);
			adapter.setDropDownViewResource(R.layout.spinner_centered);
		} else {
			adapter = ArrayAdapter.createFromResource(getContext(),
					R.array.selectable_whole_values_strings, R.layout.spinner_centered);
			adapter.setDropDownViewResource(R.layout.spinner_centered);
		}

		for(int i = 0; i < viewIds.length; i++) {
			listeners[i] = new AbilityItemSelectedListener(isTemp, i);
			spinners[i] = (Spinner) getRootView().findViewById(viewIds[i]); 
			
			spinners[i].setAdapter(adapter);
			spinners[i].setOnItemSelectedListener(listeners[i]);
		}
	}

	public void updateModsViews(int viewIds[], boolean isTemp) {
		TextView tv;
		for(int i = 0; i < viewIds.length; i++) {
			tv = (TextView) getRootView().findViewById(viewIds[i]); 
			if(isTemp) {
				if(m_abilityScores.getAbilityAtIndex(i).getID() == AbilitySet.KEY_DEX &&
						m_abilityScores.getAbilityAtIndex(i).getTempModifier() > m_maxDex) {
					tv.setText(Integer.toString(m_maxDex)+"\n"+getString(R.string.max_dex_warning));
				} else {
					tv.setText(Integer.toString(m_abilityScores.getAbilityAtIndex(i)
						.getTempModifier()));
				}
			}
			else {
				tv.setText(Integer.toString(m_abilityScores.getAbilityAtIndex(i)
						.getAbilityModifier()));
			}
		}
	}

	private class AbilityItemSelectedListener implements OnItemSelectedListener {
		boolean _isTemp;
		int _abilityIndex;
		
		public AbilityItemSelectedListener(boolean isTemp, int abilityIndex) {
			super();
			_isTemp = isTemp;
			_abilityIndex = abilityIndex;
		}

		public void onNothingSelected(AdapterView<?> parent) {
			
		}

		public void onItemSelected(AdapterView<?> parent, View view, 
			int pos, long id) {

			if(_isTemp) {
				m_abilityScores.getAbilityAtIndex(_abilityIndex).setTempBonus(pos - TEMP_SPINNER_INDEX_OFFSET);
			}
			else {
				m_abilityScores.getAbilityAtIndex(_abilityIndex).setScore(pos);
			}

			updateModsViews(tempModViewIds, true);
			updateModsViews(baseModViewIds, false);

		}
		
	}

	@Override
	public void updateFragmentUI() {
		updateSpinners();
		updateModsViews(baseModViewIds, false);
		updateModsViews(tempModViewIds, true);
	}
	
	@Override
	public String getFragmentTitle() {
		return getString(R.string.tab_character_abilities);
	}

	@Override
	public void updateDatabase() {
		if (m_abilityScores != null) {
			for (int i = 0; i < m_abilityScores.size(); i++) {
				m_abilityRepo.update(m_abilityScores.getAbilityAtIndex(i));
			}
		}
	}

	@Override
	public void loadFromDatabase() {
		m_abilityScores = m_abilityRepo.querySet(getCurrentCharacterID());
		m_maxDex = m_armorRepo.getMaxDex(getCurrentCharacterID());
	}
}
