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
import com.lateensoft.pathfinder.toolkit.db.repository.PTAbilityScoreRepository;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTAbilitySet;

// Untested
public class PTCharacterAbilityFragment extends PTCharacterSheetFragment {
	@SuppressWarnings("unused")
	private static final String TAG = PTCharacterAbilityFragment.class.getSimpleName();
	
	private final int[] modIds = {R.id.strMod, R.id.dexMod, R.id.conMod, R.id.intMod, 
			R.id.wisMod, R.id.chaMod};
	private final int[] tempScoreIds = {R.id.strTempScore, R.id.dexTempScore, R.id.conTempScore, 
			R.id.intTempScore, R.id.wisTempScore, R.id.chaTempScore};
	private final int[] baseScoreIds = {R.id.baseStr, R.id.baseDex, R.id.baseCon, R.id.baseInt,
			R.id.baseWis, R.id.baseCha};
	private final int[] tempModIds = {R.id.strTempMod, R.id.dexTempMod, R.id.conTempMod,
			R.id.intTempMod, R.id.wisTempMod, R.id.chaTempMod};	
	
	private AbilityItemSelectedListener[] m_abilityItemSelectedListeners;
	private AbilityItemSelectedListener[] m_tempAbilityItemSelectedListeners;
	//LayoutInflater mInflater;
	private Spinner[] m_baseScoreSpinners;
	private Spinner[] m_tempScoreSpinners;
	
	private PTAbilityScoreRepository m_abilityRepo;
	
	private PTAbilitySet m_baseAbilityScores;
	private PTAbilitySet m_tempAbilityScores;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		m_abilityRepo = new PTAbilityScoreRepository();
		
		m_baseScoreSpinners = new Spinner[baseScoreIds.length];
		m_tempScoreSpinners = new Spinner[tempScoreIds.length];
		
		m_abilityItemSelectedListeners = new AbilityItemSelectedListener[modIds.length];
		m_tempAbilityItemSelectedListeners = new AbilityItemSelectedListener[modIds.length];
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setRootView(inflater.inflate(R.layout.character_abilities_fragment,
				container, false));
		
		setupSpinners(m_baseScoreSpinners, baseScoreIds, m_abilityItemSelectedListeners, false);
		setupSpinners(m_tempScoreSpinners, tempScoreIds, m_tempAbilityItemSelectedListeners, true);
		
		return getRootView();
	}
	
	private void updateSpinners() {
		for(int i = 0; i < modIds.length; i++) {
			updateSpinnerValues(i);
		}
	}
		
	private void updateSpinnerValues(int abilityKey) {
    	Spinner s = new Spinner(getActivity());
    	TextView tv = new TextView(getActivity());
    	
		tv = (TextView) getRootView().findViewById(modIds[abilityKey]);
		tv.setText(String.valueOf(m_baseAbilityScores.getAbilityScore(abilityKey).getModifier()));
		
		s = (Spinner) getRootView().findViewById(baseScoreIds[abilityKey]);
		s.setSelection((m_baseAbilityScores.getAbilityScore(abilityKey).getScore()));
		
		s = (Spinner) getRootView().findViewById(tempScoreIds[abilityKey]);
		s.setSelection((m_tempAbilityScores
				.getAbilityScore(abilityKey).getScore()));
		
		tv = (TextView) getRootView().findViewById(tempModIds[abilityKey]);
		tv.setText(String.valueOf(m_tempAbilityScores
				.getAbilityScore(abilityKey).getModifier()));
    }
	
	private void setupSpinners(Spinner[] spinners, int viewIds[], 
			AbilityItemSelectedListener[] listeners, boolean isTemp) {
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
				R.array.selectable_values_string, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(R.layout.spinner_plain);
		
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
				tv.setText(Integer.toString(m_tempAbilityScores.getAbilityScore(i)
					.getModifier()));
			}
			else {
				tv.setText(Integer.toString(m_baseAbilityScores.getAbilityScore(i)
						.getModifier()));
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
				m_tempAbilityScores.setScore(_abilityIndex, pos);
			}
			else {
				m_baseAbilityScores.setScore(_abilityIndex, pos);
			}
	
			updateModsViews(tempModIds, true);
			updateModsViews(modIds, false);		
			
		}
		
	}

	@Override
	public void updateFragmentUI() {
		updateSpinners();
		updateModsViews(modIds, false);
		updateModsViews(tempModIds, true);
	}
	
	@Override
	public String getFragmentTitle() {
		return getString(R.string.tab_character_abilities);
	}

	@Override
	public void updateDatabase() {
		if (m_baseAbilityScores != null && m_tempAbilityScores != null) {
			for (int i = 0; i < m_baseAbilityScores.getLength(); i++) {
				m_abilityRepo.update(m_baseAbilityScores.getAbilityScore(i));
			}
			for (int i = 0; i < m_tempAbilityScores.getLength(); i++) {
				m_abilityRepo.update(m_tempAbilityScores.getAbilityScore(i));
			}
		}
	}

	@Override
	public void loadFromDatabase() {
		m_baseAbilityScores = new PTAbilitySet(m_abilityRepo.querySet(getCurrentCharacterID(), false));
		m_tempAbilityScores = new PTAbilitySet(m_abilityRepo.querySet(getCurrentCharacterID(), true));
	}
}
