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
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setRootView(inflater.inflate(R.layout.character_abilities_fragment,
				container, false));
		
		m_abilityItemSelectedListeners = new AbilityItemSelectedListener[modIds.length];
		m_tempAbilityItemSelectedListeners = new AbilityItemSelectedListener[modIds.length];
		
		updateFragmentUI();
		
		return getRootView();
	}
	
	public void updateInterfaceAbilities() {
		for(int i = 0; i < modIds.length; i++) {
			updateInterfaceAbility(i);
		}
	}
		
	public void updateInterfaceAbility(int key) {
    	Spinner s = new Spinner(getActivity());
    	TextView tv = new TextView(getActivity());
    	
		tv = (TextView) getRootView().findViewById(modIds[key]);
		tv.setText(m_baseAbilityScores.getAbilityScore(key).getModifier());
		
		s = (Spinner) getRootView().findViewById(baseScoreIds[key]);
		s.setSelection((m_baseAbilityScores.getAbilityScore(key).getScore()));
		
		s = (Spinner) getRootView().findViewById(tempScoreIds[key]);
		s.setSelection((m_tempAbilityScores
				.getAbilityScore(key).getScore()));
		
		tv = (TextView) getRootView().findViewById(tempModIds[key]);
		tv.setText((m_tempAbilityScores
				.getAbilityScore(key).getModifier()));
    }
	
	public void setupSpinners(Spinner[] spinners, int viewIds[], 
			AbilityItemSelectedListener[] listeners, boolean isTemp) {
		spinners = new Spinner[viewIds.length];
		//mSpinner = (Spinner) mView.findViewById(R.id.baseStr);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
				R.array.selectable_values_string, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(R.layout.spinner_plain);
		
		for(int i = 0; i < viewIds.length; i++) {
			listeners[i] = new AbilityItemSelectedListener(isTemp, i);
			spinners[i] = (Spinner) getRootView().findViewById(viewIds[i]); 
			
			spinners[i].setAdapter(adapter);

			spinners[i].setOnItemSelectedListener(listeners[i]);
			if(isTemp) {
				spinners[i].setSelection(m_tempAbilityScores.getAbilityScore(i)
						.getScore(), true);
			} else {
				spinners[i].setSelection(m_tempAbilityScores.getAbilityScore(i).getScore(),
					true);
			}
		}
	}
	
	public void updateMods(int viewIds[], boolean isTemp) {
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
	
	public class AbilityItemSelectedListener implements OnItemSelectedListener {
		int _sourceId;
		int[] _ids;
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
	
			updateMods(tempModIds, true);
			updateMods(modIds, false);		
			
		}
		
	}

	@Override
	public void updateFragmentUI() {
		setupSpinners(m_baseScoreSpinners, baseScoreIds, m_abilityItemSelectedListeners, false);
		setupSpinners(m_tempScoreSpinners, tempScoreIds, m_tempAbilityItemSelectedListeners, true);
		updateMods(modIds, false);
		updateMods(tempModIds, true);
	}
	
	@Override
	public String getFragmentTitle() {
		return getString(R.string.tab_character_abilities);
	}

	@Override
	public void updateDatabase() {
		// TODO optimize to update when needed
		for (int i = 0; i < m_baseAbilityScores.getLength(); i++) {
			m_abilityRepo.update(m_baseAbilityScores.getAbilityScore(i));
		}
		for (int i = 0; i < m_tempAbilityScores.getLength(); i++) {
			m_abilityRepo.update(m_tempAbilityScores.getAbilityScore(i));
		}
	}

	@Override
	public void loadFromDatabase() {
		m_baseAbilityScores = new PTAbilitySet(m_abilityRepo.querySet(getCurrentCharacterID(), false));
		m_tempAbilityScores = new PTAbilitySet(m_abilityRepo.querySet(getCurrentCharacterID(), true));
	}
}
