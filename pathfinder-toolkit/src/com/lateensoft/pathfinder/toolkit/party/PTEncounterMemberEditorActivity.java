package com.lateensoft.pathfinder.toolkit.party;

import android.os.Bundle;
import android.util.Log;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.datahelpers.PTSharedPreferences;

/**
 * requires an intent with extras: R.string.party_member_index_key (int)
 * @author trevsiemens
 *
 */
public class PTEncounterMemberEditorActivity extends PTBasePartyMemberEditorActivity{

	private final String TAG = PTEncounterMemberEditorActivity.class.getSimpleName();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getDataFromExtras();
	}
	
	protected void getDataFromExtras(){
		Bundle extras = getIntent().getExtras();
		if(extras != null){
			mParty = PTSharedPreferences.getSharedInstance().getEncounterParty();
			if(mParty == null)
				finish();
			mPartyMemberIndex = extras.getInt(getString(R.string.party_member_index_key));
			mPartyMember = mParty.getPartyMember(mPartyMemberIndex);
			updateStatsViews();
		}
		else finish();
	}
	
	@Override
	protected void onPause() {
		if(mParty != null){
			if(mDialogMode != DIALOG_MODE_DELETE_MEMBER){
				mPartyMember.setName(mPartyMemberNameEditText.getText().toString());
				mPartyMemberIndex = mParty.setPartyMember(mPartyMemberIndex, mPartyMember);
			}
			PTSharedPreferences.getSharedInstance().setEncounterParty(mParty);
		}
		super.onPause();
	}
	
}
