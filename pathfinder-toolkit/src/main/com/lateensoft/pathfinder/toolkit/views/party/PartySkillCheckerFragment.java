package com.lateensoft.pathfinder.toolkit.views.party;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.adapters.party.PartyRollAdapter;
import com.lateensoft.pathfinder.toolkit.db.repository.PartyRepository;
import com.lateensoft.pathfinder.toolkit.model.party.CampaignParty;
import com.lateensoft.pathfinder.toolkit.model.party.PartyMember;
import com.lateensoft.pathfinder.toolkit.pref.GlobalPrefs;
import com.lateensoft.pathfinder.toolkit.pref.Preferences;
import com.lateensoft.pathfinder.toolkit.util.DiceSet;
import com.lateensoft.pathfinder.toolkit.views.BasePageFragment;

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
import roboguice.RoboGuice;

import java.util.Collections;


public class PartySkillCheckerFragment extends BasePageFragment implements OnClickListener, OnItemSelectedListener{
    @SuppressWarnings("unused")
    private static final String TAG = PartySkillCheckerFragment.class.getSimpleName();
    
    public CampaignParty m_party;
    
    private Button m_rollButton;
    private Spinner m_skillSpinner;
    private ListView m_partyMemberList;
    
    private int m_skillSelectedForRoll;
    
    private PartyRepository m_partyRepo;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        m_partyRepo = new PartyRepository();
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
           
           CampaignParty currentEncounterParty = m_partyRepo.queryEncounterParty();
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
        Preferences preferences = RoboGuice.getInjector(getContext()).getInstance(Preferences.class);
        long currentPartyID = preferences.get(GlobalPrefs.SELECTED_PARTY_ID, -1L);
        CampaignParty party = null;
        if(currentPartyID > 0) {
//            party = m_partyRepo.query(currentPartyID); // TODO this should use encounter repo
        }
        
        if (party == null) {
            party = new CampaignParty("Empty Party");
        }
        
        m_party = party;
        refreshPartyView();
    }
    
    private void refreshPartyView(){
        Collections.sort(m_party, new PartyMember.RollComparator());
        PartyRollAdapter adapter = new PartyRollAdapter(getContext(), R.layout.party_roll_row, m_party, new PartyRollAdapter.CritTypeValueGetter() {
            @Override
            public PartyRollAdapter.CritType getCritTypeForMember(PartyMember member) {
                int naturalRollVal = member.getLastRolledValue() - getSkillModForMember(member);
                switch(naturalRollVal){
                    case 20:
                        return PartyRollAdapter.CritType.CRIT;
                    case 1:
                        return PartyRollAdapter.CritType.CRIT_FUMBLE;
                    default:
                        return PartyRollAdapter.CritType.NO_CRIT;
                }
            }
        });
        m_partyMemberList.setAdapter(adapter);
        updateTitle();
    }
    
    public void resetPartyRolls(){
        for (PartyMember member : m_party) {
            member.setLastRolledValue(0);
        }
    }

    private static boolean partyIsInEncounter(CampaignParty party){
        for (PartyMember member : party) {
            if (member.getLastRolledValue() != 0) {
                return true;
            }
        }
        return false;
    }
    
    //When roll button is clicked
    public void onClick(View view) {
        DiceSet diceSet = new DiceSet();

        for (PartyMember member : m_party) {
            member.setLastRolledValue(diceSet.singleRoll(20) + getSkillModForMember(member));
        }
        refreshPartyView();
    }

    private int getSkillModForMember(PartyMember member){
        switch(m_skillSelectedForRoll){
        case 0:
            return member.getFortSave();
        case 1:
            return member.getReflexSave();
        case 2:
            return member.getWillSave();
        case 3:
            return member.getBluffSkillBonus();
        case 4:
            return member.getDisguiseSkillBonus();
        case 5:
            return member.getPerceptionSkillBonus();
        case 6:
            return member.getSenseMotiveSkillBonus();
        case 7:
            return member.getStealthSkillBonus();
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
