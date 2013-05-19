package com.lateensoft.pathfinder.toolkit.party;

import com.lateensoft.pathfinder.toolkit.PTSharedMenu;
import com.lateensoft.pathfinder.toolkit.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

public class PTBasePartyMemberEditorActivity extends Activity implements OnClickListener, OnItemClickListener{

	private final String TAG = "PTBasePartyMemberEditorActivity";
	private final int MENU_ITEM_DONE = 0;
	private final int MENU_ITEM_DELETE_MEMBER = 1;
	protected final int DIALOG_MODE_EDIT_MEMBER = 0;
	protected final int DIALOG_MODE_DELETE_MEMBER = 1;
	
	protected PTPartyMember mPartyMember = null;
	protected int mPartyMemberIndex = 0;
	protected PTParty mParty = null;
	
	protected EditText mPartyMemberNameEditText;
	private ListView mStatList;
	
	protected int mDialogMode;
	private int mStatSelectedForEdit;
	private EditText mDialogStatValue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		setContentView(R.layout.activity_party_member_editor);
		
		//Defaults to prevent null pointers
		mPartyMember = new PTPartyMember("");
		
		
		mPartyMemberNameEditText = (EditText) findViewById(R.id.editTextPartyMemberName);
		mPartyMemberNameEditText.setText(mPartyMember.getName());
		
		mStatList = (ListView) findViewById(R.id.listViewPartyMemberStats);		
		PTPartyMemberStatAdapter adapter = new PTPartyMemberStatAdapter(this, R.layout.party_member_stat_row, 
				mPartyMember.getStatFields(this), mPartyMember.getStatsArray());
		mStatList.setAdapter(adapter);
		mStatList.setOnItemClickListener(this);
		
		mDialogMode = DIALOG_MODE_EDIT_MEMBER;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (PTSharedMenu.onOptionsItemSelected(item, this) == false) {
			// handle local menu items here

			switch (item.getItemId()) {
			case android.R.id.home: // Tapped the back button on the action bar to return to previous screen
				finish();
				break;
			case MENU_ITEM_DONE:
				finish();
				break;
			case MENU_ITEM_DELETE_MEMBER:
				//Delete party
				mDialogMode = DIALOG_MODE_DELETE_MEMBER;
				showMemberDeleteDialog();
				break;
			}
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		MenuItem partyListItem = menu.add(Menu.NONE,
				MENU_ITEM_DONE, Menu.NONE,
				R.string.menu_item_done);
		partyListItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		
		MenuItem deletePartyItem = menu.add(Menu.NONE,
				MENU_ITEM_DELETE_MEMBER, Menu.NONE,
				R.string.menu_item_delete_member);
		deletePartyItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		
		PTSharedMenu.onCreateOptionsMenu(menu, getApplicationContext());
		return true;
	}

	public void showStatEditDialog(int position){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		//Set up dialog layout
		LayoutInflater inflater = getLayoutInflater();

		View dialogView = inflater.inflate(R.layout.party_member_stat_dialog, null);
		mDialogStatValue = (EditText) dialogView.findViewById(R.id.dialogStatText);
		
		builder.setTitle(mPartyMember.getStatFields(this)[position]);
		mDialogStatValue.setText(Integer.toString(mPartyMember.getValueByIndex(position)));
		
		builder.setView(dialogView)
				.setPositiveButton(R.string.ok_button_text, this)
				.setNegativeButton(R.string.cancel_button_text, this);
		
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	private void showMemberDeleteDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		//Set up dialog layout
		builder.setTitle("Delete Party Member");
		builder.setMessage(getString(R.string.delete_character_dialog_message_1)+mPartyMember.getName()+ 
				getString(R.string.delete_character_dialog_message_2))
			.setPositiveButton(R.string.delete_button_text, this)
			.setNegativeButton(R.string.cancel_button_text, this);
		
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		mStatSelectedForEdit = position;
		mDialogMode = DIALOG_MODE_EDIT_MEMBER;
		mPartyMember.setName(mPartyMemberNameEditText.getText().toString()); //Ensures name is not overwritten
		showStatEditDialog(position);
		
	}

	public void onClick(DialogInterface dialogInterface, int selection) {
		switch (selection) {
		//OK button tapped
		case DialogInterface.BUTTON_POSITIVE:
			
			if(mDialogMode == DIALOG_MODE_EDIT_MEMBER){
				try{
					mPartyMember.setStatByIndex(mStatSelectedForEdit, Integer.parseInt(mDialogStatValue.getText().toString()));
					updateStatsViews();
				}catch (NumberFormatException e){
					//Do nothing
				}
			}
			else if(mDialogMode == DIALOG_MODE_DELETE_MEMBER){
				mParty.deletePartyMember(mPartyMemberIndex);
				finish();
			}
			break;
		//Cancel Button	tapped
		case DialogInterface.BUTTON_NEGATIVE:
			mDialogMode = DIALOG_MODE_EDIT_MEMBER;
			break;
		default:
			mDialogMode = DIALOG_MODE_EDIT_MEMBER;
			break;
		}
	}
	
	public void updateStatsViews(){
		((PTPartyMemberStatAdapter)mStatList.getAdapter()).updateValues(mPartyMember.getStatsArray());
		mPartyMemberNameEditText.setText(mPartyMember.getName());
	}
	
	
}
