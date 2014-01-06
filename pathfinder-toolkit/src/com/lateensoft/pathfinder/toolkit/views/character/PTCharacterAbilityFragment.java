package com.lateensoft.pathfinder.toolkit.views.character;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.lateensoft.pathfinder.toolkit.R;

// Untested
public class PTCharacterAbilityFragment extends PTCharacterSheetFragment {
	private static final String TAG = PTCharacterAbilityFragment.class.getSimpleName();

	Context mContext;
	AbilityItemSelectedListener[] mAbilityItemSelectedListeners;
	AbilityItemSelectedListener[] mTempAbilityItemSelectedListeners;
	//LayoutInflater mInflater;
	Spinner[] mBaseScoreSpinners;
	Spinner[] mTempScoreSpinners;
	
	final int[] modIds = {R.id.strMod, R.id.dexMod, R.id.conMod, R.id.intMod, 
			R.id.wisMod, R.id.chaMod};
	final int[] tempScoreIds = {R.id.strTempScore, R.id.dexTempScore, R.id.conTempScore, 
			R.id.intTempScore, R.id.wisTempScore, R.id.chaTempScore};
	final int[] baseScoreIds = {R.id.baseStr, R.id.baseDex, R.id.baseCon, R.id.baseInt,
			R.id.baseWis, R.id.baseCha};
	final int[] tempModIds = {R.id.strTempMod, R.id.dexTempMod, R.id.conTempMod,
			R.id.intTempMod, R.id.wisTempMod, R.id.chaTempMod};	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mContext = container.getContext();
		
		setRootView(inflater.inflate(R.layout.character_abilities_fragment,
				container, false));
		
		mAbilityItemSelectedListeners = new AbilityItemSelectedListener[modIds.length];
		mTempAbilityItemSelectedListeners = new AbilityItemSelectedListener[modIds.length];
		
		updateFragmentUI();
		
		return getRootView();
	}
	
	public void updateInterfaceAbilities() {
		for(int i = 0; i < modIds.length; i++) {
			updateInterfaceAbility(i);
		}
	}
		
	public void updateInterfaceAbility(int key) {
    	Spinner s = new Spinner(mContext);
    	TextView tv = new TextView(mContext);
    	
		tv = (TextView) getRootView().findViewById(modIds[key]);
		tv.setText(mCharacter.getAbilitySet().getAbilityScore(key).getModifier());
		
		s = (Spinner) getRootView().findViewById(baseScoreIds[key]);
		s.setSelection((mCharacter.getAbilitySet().getAbilityScore(key).getScore()));
		
		s = (Spinner) getRootView().findViewById(tempScoreIds[key]);
		s.setSelection((mCharacter.getTempAbilitySet()
				.getAbilityScore(key).getScore()));
		
		tv = (TextView) getRootView().findViewById(tempModIds[key]);
		tv.setText((mCharacter.getTempAbilitySet()
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
				spinners[i].setSelection(mCharacter.getTempAbilitySet().getAbilityScore(i)
						.getScore(), true);
			} else {
				spinners[i].setSelection(mCharacter.getAbilitySet().getAbilityScore(i).getScore(),
					true);
			}
		}
	}
	
	public void updateMods(int viewIds[], boolean isTemp) {
		TextView tv;
		
		for(int i = 0; i < viewIds.length; i++) {
			tv = (TextView) getRootView().findViewById(viewIds[i]); 
			if(isTemp) {
				tv.setText(Integer.toString(mCharacter.getTempAbilitySet().getAbilityScore(i)
					.getModifier()));
			}
			else {
				tv.setText(Integer.toString(mCharacter.getAbilitySet().getAbilityScore(i)
						.getModifier()));
			}
		}
	}
	
	public class AbilityItemSelectedListener implements OnItemSelectedListener {
		int mSourceId;
		int[] mIds;
		boolean mIsTemp;
		int mAbilityIndex;
		
		public AbilityItemSelectedListener(boolean isTemp, int abilityIndex) {
			super();
			mIsTemp = isTemp;
			mAbilityIndex = abilityIndex;
		}

		public void onNothingSelected(AdapterView<?> parent) {
			
		}

		public void onItemSelected(AdapterView<?> parent, View view, 
		        int pos, long id) {

			if(mIsTemp) {
				mCharacter.getTempAbilitySet().setScore(mAbilityIndex, pos);
			}
			else {
				mCharacter.getAbilitySet().setScore(mAbilityIndex, pos);
			}
	
			updateMods(tempModIds, true);
			updateMods(modIds, false);		
			
		}
		
	}

	@Override
	public void updateFragmentUI() {
		setupSpinners(mBaseScoreSpinners, baseScoreIds, mAbilityItemSelectedListeners, false);
		setupSpinners(mTempScoreSpinners, tempScoreIds, mTempAbilityItemSelectedListeners, true);
		updateMods(modIds, false);
		updateMods(tempModIds, true);
	}
	
	@Override
	public String getFragmentTitle() {
		return getString(R.string.tab_character_abilities);
	}
}
