package com.lateensoft.pathfinder.toolkit.views.party;

import com.lateensoft.pathfinder.toolkit.PTSharedPreferences;
import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.adapters.party.PTPartyRollAdapter;
import com.lateensoft.pathfinder.toolkit.db.repository.PTPartyRepository;
import com.lateensoft.pathfinder.toolkit.model.party.PTParty;
import com.lateensoft.pathfinder.toolkit.utils.PTDiceSet;
import com.lateensoft.pathfinder.toolkit.views.PTBasePageFragment;

import android.os.Bundle;
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

	@SuppressWarnings("unused")
	private static final String TAG = PTPartySkillCheckerFragment.class.getSimpleName();
	
	public PTParty m_party;
	
	private Button m_rollButton;
	private Spinner m_skillSpinner;
	private ListView m_partyMemberList;
	
	private int m_skillSelectedForRoll;
	
	private PTPartyRepository m_partyRepo;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        m_partyRepo = new PTPartyRepository();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		setRootView(inflater.inflate(R.layout.fragment_skill_checker, container, false));

		m_skillSelectedForRoll = 0;
		
		m_rollButton = (Button) getRootView().findViewById(R.id.buttonRoll);
		m_rollButton.setOnClickListener(this);
	
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
				R.array.checkable_skills_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(R.layout.spinner_plain);
		m_skillSpinner = (Spinner) getRootView().findViewById(R.id.spinnerSkillToRoll);
		m_skillSpinner.setAdapter(adapter);
		m_skillSpinner.setOnItemSelectedListener(this);
		m_skillSpinner.setSelection(m_skillSelectedForRoll);
		
		m_partyMemberList = (ListView) getRootView().findViewById(R.id.listViewPartyMembers);
		
		loadEncounterParty();
		resetPartyRolls();
		
		return getRootView();
    }

    @Override
    public void updateTitle() {
        setTitle(R.string.title_activity_skill_checker);
        if (m_party != null) {
            setSubtitle(m_party.getName());
        } else {
            setSubtitle(null);
        }
    }
    
    /**
   	 * Load the current encounter party in shared prefs
   	 * If there is no party set in user prefs, it pulls the default currently set in party manager
   	 * If there is not current party, an empty party is set
   	 */
   	public void loadEncounterParty(){
   		
   		PTParty currentEncounterParty = m_partyRepo.queryEncounterParty();
   		//If there is no saved encounter party, get from party manager
   		//Also, if the encounter party was saved, but previously was empty, get from party manager.
   		//Thirdly, if the party in encounter is not rolled (not in an encounter) use default party
   		if(currentEncounterParty == null || currentEncounterParty.size() == 0 || !partyIsInEncounter(currentEncounterParty)){ 
   			loadDefaultParty();
   		}
   		else{
   			m_party = currentEncounterParty;
   			m_party.setName(m_party.getName() + " (in encounter)");
   			refreshPartyView();	
   		}
   	}
   	
	public void loadDefaultParty(){
		long currentPartyID = PTSharedPreferences.getInstance().getLong(
				PTSharedPreferences.KEY_LONG_SELECTED_PARTY_ID, -1);
		PTParty party = null;
		if(currentPartyID > 0) {
			party = m_partyRepo.query(currentPartyID);
		}
		
		if (party == null) {
			party = new PTParty("Empty Party");
		}
		
		m_party = party;
		refreshPartyView();
	}
	
	private void refreshPartyView(){
		String[] memberNames = m_party.getNamesByRollValue();
		int[] memberRollValues = m_party.getRollValuesByRollValue();
		int[] indexesByRollValue = m_party.getIndexesByRollValue();
		int[] critValues = null;
		
		if(m_party.size() > 0) critValues = new int[m_party.size()];
		
		for(int i = 0; i < m_party.size(); i++){
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
		PTPartyRollAdapter adapter = new PTPartyRollAdapter(getContext(), R.layout.party_roll_row, memberNames, memberRollValues, critValues);
		m_partyMemberList.setAdapter(adapter);
		updateTitle();
	}
	
	public void resetPartyRolls(){
		for(int i = 0; i < m_party.size(); i++){
			m_party.getPartyMember(i).setLastRolledValue(0);
		}
	}
	
	/**
	 * @return true if party is in an encounter (has non zero roll values) false if in reset state
	 */
	private boolean partyIsInEncounter(PTParty party){
		for(int i = 0; i < party.size(); i++)
			if(party.getPartyMember(i).getLastRolledValue() != 0)
				return true;
		return false;
	}
	
	//When roll button is clicked
	public void onClick(View view) {
		PTDiceSet diceSet = new PTDiceSet();
		int skillMod;
		
		for(int i = 0; i < m_party.size(); i++){
			skillMod = getSkillModForMember(i);
			m_party.getPartyMember(i).setLastRolledValue(diceSet.singleRoll(20)+skillMod);
		}
		refreshPartyView();
	}

	private int getSkillModForMember(int partyMemberIndex){
		switch(m_skillSelectedForRoll){
		case 0:
			return m_party.getPartyMember(partyMemberIndex).getFortSave();
		case 1:
			return m_party.getPartyMember(partyMemberIndex).getReflexSave();
		case 2:
			return m_party.getPartyMember(partyMemberIndex).getWillSave();
		case 3:
			return m_party.getPartyMember(partyMemberIndex).getBluffSkillBonus();
		case 4:
			return m_party.getPartyMember(partyMemberIndex).getDisguiseSkillBonus();
		case 5:
			return m_party.getPartyMember(partyMemberIndex).getPerceptionSkillBonus();
		case 6:
			return m_party.getPartyMember(partyMemberIndex).getSenseMotiveSkillBonus();
		case 7:
			return m_party.getPartyMember(partyMemberIndex).getStealthSkillBonus();
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
		m_skillSelectedForRoll = position;
		
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// Do nothing
		
	}
}
