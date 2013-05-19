package com.lateensoft.pathfinder.toolkit.party;


import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.datahelpers.PTDatabaseManager;


import android.os.Bundle;
import android.util.Log;


/**
 * requires an intent with extras: R.string.party_member_index_key (int) and R.string.party_id_key (int)
 * @author trevsiemens
 *
 */
public class PTPartyMemberEditorActivity extends PTBasePartyMemberEditorActivity{
	private final String TAG = "PTPartyMemberEditorActivity";
	
	private PTDatabaseManager mSQLManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mSQLManager = new PTDatabaseManager(this.getApplicationContext());
		getDataFromExtras();
	}
	
	protected void getDataFromExtras(){
		Bundle extras = getIntent().getExtras();
		if(extras != null){
			mPartyMemberIndex = extras.getInt(getString(R.string.party_member_index_key));
			int partyID = extras.getInt(getString(R.string.party_id_key));
			mParty = mSQLManager.getParty(partyID);
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
			mSQLManager.updateParty(mParty);
		}
		super.onPause();
	}
}
