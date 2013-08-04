package com.lateensoft.pathfinder.toolkit;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.lateensoft.pathfinder.toolkit.datahelpers.PTDatabaseManager;
import com.lateensoft.pathfinder.toolkit.datahelpers.PTSharedPreferences;
import com.lateensoft.pathfinder.toolkit.functional.PTDiceSet;
import com.lateensoft.pathfinder.toolkit.party.PTParty;
import com.lateensoft.pathfinder.toolkit.party.PTPartyRollAdapter;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;


public class PTPartySkillCheckerFragment extends PTBasePageFragment implements OnClickListener, OnItemSelectedListener{

	private static final String TAG = PTPartySkillCheckerFragment.class.getSimpleName();
	
	public PTParty mParty;
	
	private PTDatabaseManager mSQLManager;
	
	private Button mRollButton;
	private Spinner mSkillSpinner;
	private ListView mPartyMemberList;
	
	private int mSkillSelectedForRoll;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mParentView = inflater.inflate(R.layout.fragment_skill_checker, container, false);
		
		mSkillSelectedForRoll = 0;
		
		mSQLManager = new PTDatabaseManager(getActivity());
		
		mRollButton = (Button) mParentView.findViewById(R.id.buttonRoll);
		mRollButton.setOnClickListener(this);
	
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
				R.array.checkable_skills_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(R.layout.spinner_plain);
		mSkillSpinner = (Spinner) mParentView.findViewById(R.id.spinnerSkillToRoll);
		mSkillSpinner.setAdapter(adapter);
		mSkillSpinner.setOnItemSelectedListener(this);
		mSkillSpinner.setSelection(mSkillSelectedForRoll);
		
		mPartyMemberList = (ListView) mParentView.findViewById(R.id.listViewPartyMembers);
		
		loadEncounterParty();
		resetPartyRolls();
		
		return mParentView;
    }
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
    }
    
    /**
   	 * Load the current encounter party in shared prefs
   	 * If there is no party set in user prefs, it pulls the default currently set in party manager
   	 * If there is not current party, an empty party is set
   	 */
   	public void loadEncounterParty(){
   		
   		PTParty currentEncounterParty = PTSharedPreferences.getSharedInstance().getEncounterParty();
   		//If there is no saved encounter party, get from party manager
   		//Also, if the encounter party was saved, but previously was empty, get from party manager.
   		//Thirdly, if the party in encounter is not rolled (not in an encounter) use default party
   		if(currentEncounterParty == null || currentEncounterParty.size() == 0 || !partyIsInEncounter(currentEncounterParty)){ 
   			loadDefaultParty();
   		}
   		else{
   			mParty = currentEncounterParty;
   			mParty.setName(mParty.getName() + " (in encounter)");
   			refreshPartyView();	
   		}
   	}
   	
	public void loadDefaultParty(){
		int currentPartyID = PTSharedPreferences.getSharedInstance().getSelectedParty();
		if(currentPartyID >= 0)
			mParty = mSQLManager.getParty(currentPartyID);
		else
			mParty = new PTParty("Empty Party");
		refreshPartyView();
	}
	
	private void refreshPartyView(){
		String[] memberNames = mParty.getNamesByRollValue();
		int[] memberRollValues = mParty.getRollValuesByRollValue();
		int[] indexesByRollValue = mParty.getIndexesByRollValue();
		int[] critValues = null;
		
		if(mParty.size() > 0) critValues = new int[mParty.size()];
		
		for(int i = 0; i < mParty.size(); i++){
			int naturalRollVal = memberRollValues[i] - getSkillModForMember(indexesByRollValue[i]);
			switch(naturalRollVal){
			case 20:
				critValues[i] = PTPartyRollAdapter.CRIT;
				break;
			case 1:
				critValues[i] = PTPartyRollAdapter.CRIT_FUMBLE;
				break;
			default:
				critValues[i] = PTPartyRollAdapter.NO_CRIT;
				break;
			}		
		}
		PTPartyRollAdapter adapter = new PTPartyRollAdapter(getActivity(), R.layout.party_roll_row, memberNames, memberRollValues, critValues);
		mPartyMemberList.setAdapter(adapter);
		setActionBarTitle(R.string.title_activity_skill_checker, mParty.getName());
	}
	
	public void resetPartyRolls(){
		for(int i = 0; i < mParty.size(); i++){
			mParty.getPartyMember(i).setRolledValue(0);
		}
	}
	
	/**
	 * 
	 * @param party
	 * @return true if party is in an encounter (has non zero roll values) false if in reset state
	 */
	private boolean partyIsInEncounter(PTParty party){
		for(int i = 0; i < party.size(); i++)
			if(party.getPartyMember(i).getRolledValue() != 0)
				return true;
		return false;
	}
	
	//When roll button is clicked
	public void onClick(View view) {
		PTDiceSet diceSet = new PTDiceSet();
		int skillMod;
		
		for(int i = 0; i < mParty.size(); i++){
			skillMod = getSkillModForMember(i);
			mParty.getPartyMember(i).setRolledValue(diceSet.singleRoll(20)+skillMod);
		}
		refreshPartyView();
	}

	private int getSkillModForMember(int partyMemberIndex){
		switch(mSkillSelectedForRoll){
		case 0:
			return mParty.getPartyMember(partyMemberIndex).getFortSave();
		case 1:
			return mParty.getPartyMember(partyMemberIndex).getReflexSave();
		case 2:
			return mParty.getPartyMember(partyMemberIndex).getWillSave();
		case 3:
			return mParty.getPartyMember(partyMemberIndex).getBluffSkillBonus();
		case 4:
			return mParty.getPartyMember(partyMemberIndex).getDisguiseSkillBonus();
		case 5:
			return mParty.getPartyMember(partyMemberIndex).getPerceptionSkillBonus();
		case 6:
			return mParty.getPartyMember(partyMemberIndex).getSenseMotiveSkillBonus();
		case 7:
			return mParty.getPartyMember(partyMemberIndex).getStealthSkillBonus();
		default:
			return 0;
		}
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		refreshPartyView();
	}

	public void onItemSelected(AdapterView<?> adapterView, View view, int position,
			long id) {
		mSkillSelectedForRoll = position;
		
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// Do nothing
		
	}
}
