package com.lateensoft.pathfinder.toolkit.character;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.lateensoft.pathfinder.toolkit.R;


public class PTCharacterFeatsFragment extends PTCharacterSheetFragment implements 
OnClickListener, OnItemClickListener, android.content.DialogInterface.OnClickListener {

	final String TAG = "PTCharacterFeatsFragment";
	private ListView mFeatsListView;
	private Button mAddButton;
	
	private EditText mDialogFeatNameEditText;
	private EditText mDialogFeatDescEditText;
	
	private ViewGroup mContainer;
	
	private int mFeatSelectedForEdit;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		
		mContainer = container;
		
		View fragmentView = inflater.inflate(R.layout.character_feats_fragment, container, false);
		
		mAddButton = (Button) fragmentView.findViewById(R.id.buttonAddFeat);
		mAddButton.setOnClickListener(this);
	
		mFeatsListView = (ListView) fragmentView.findViewById(R.id.listViewFeats);	
		refreshFeatsListView();
		mFeatsListView.setOnItemClickListener(this);
		
		return fragmentView;
	}
	

	private void refreshFeatsListView(){
		String[] featNames = mCharacter.getFeatList().getFeatNames();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContainer.getContext(), android.R.layout.simple_list_item_1, featNames);
		mFeatsListView.setAdapter(adapter);
		
	}

	//Add Feat button was tapped
	public void onClick(View button) {
		mFeatSelectedForEdit = -1;
		showFeatDialog(null);
		
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		mFeatSelectedForEdit = position;
		showFeatDialog(mCharacter.getFeatList().getFeat(position));
		
	}
	
	/**
	 * Shows a dialog to add or edit a feat.
	 * @param item
	 */
	private void showFeatDialog(PTFeat feat) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		//Set up dialog layout
		LayoutInflater inflater = getActivity().getLayoutInflater();

		View dialogView = inflater.inflate(R.layout.character_feats_dialog, null);
		mDialogFeatNameEditText = (EditText) dialogView.findViewById(R.id.etDialogFeatName);
		mDialogFeatDescEditText = (EditText) dialogView.findViewById(R.id.etDialogFeatDescription);
		
		//Determine if add or edit
		if(mFeatSelectedForEdit < 0){
			builder.setTitle("Add New Feat");
		}
		else{
			builder.setTitle("Edit/View Feat")
					.setNeutralButton(R.string.delete_button_text, this);
			mDialogFeatNameEditText.setText(feat.getName());
			mDialogFeatDescEditText.setText(feat.getDescription());
			
		}
		
		
		builder.setView(dialogView)
				.setPositiveButton(R.string.ok_button_text, this)
				.setNegativeButton(R.string.cancel_button_text, this);
		
		

		AlertDialog alert = builder.create();
		alert.show();
	}

	public void onClick(DialogInterface dialogInterface, int selection) {
		switch (selection) {
		//OK button tapped
		case DialogInterface.BUTTON_POSITIVE:
			
			if(mFeatSelectedForEdit < 0){
				PTFeat newFeat = getFeatFromDialog();
				if(newFeat != null){
					mCharacter.getFeatList().addFeat(newFeat);
					refreshFeatsListView();
				}
			}
			else{
				PTFeat editedFeat = getFeatFromDialog();
				mCharacter.getFeatList().setFeat(editedFeat, mFeatSelectedForEdit);
				refreshFeatsListView();
			}
			
			break;
		//Cancel Button	tapped
		case DialogInterface.BUTTON_NEGATIVE:
			break;
		//Delete Button tapped
		case DialogInterface.BUTTON_NEUTRAL:
			mCharacter.getFeatList().deleteFeat(mFeatSelectedForEdit);
			refreshFeatsListView();
		default:
			break;
		}
		
		//Close keyboard
		InputMethodManager iMM = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		if(mDialogFeatNameEditText.hasFocus())
			iMM.hideSoftInputFromInputMethod(mDialogFeatNameEditText.getWindowToken(), 0);
		else if(mDialogFeatDescEditText.hasFocus())
			iMM.hideSoftInputFromInputMethod(mDialogFeatDescEditText.getWindowToken(), 0);
		
	}


	private PTFeat getFeatFromDialog() {
		String name = new String(mDialogFeatNameEditText.getText().toString());
		if(name == null || name.contentEquals("")){
			return null;
		}
		
		String description = new String(mDialogFeatDescEditText.getText().toString());
		
		return new PTFeat(name, description);
	}

	@Override
	public void onResume() {
		super.onResume();
		refreshFeatsListView();
	}
	
	
}
