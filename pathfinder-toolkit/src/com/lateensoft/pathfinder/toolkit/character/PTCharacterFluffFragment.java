package com.lateensoft.pathfinder.toolkit.character;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.lateensoft.pathfinder.toolkit.PTCharacterSheetActivity;
import com.lateensoft.pathfinder.toolkit.R;

public class PTCharacterFluffFragment extends PTCharacterSheetFragment implements 
OnItemClickListener, android.content.DialogInterface.OnClickListener{
	final String TAG = "PTCharacterFluffFragment";
	private ListView lv;
	int mFluffSelectedForEdit;
	
	private EditText mDialogFluff;
	
	private ViewGroup mContainer;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		mContainer = container;
		/*Resources r = getActivity().getBaseContext().getResources();
		
		String[] tempValues = new String[r.getStringArray(R.array.fluff_fields).length];
		tempValues = r.getStringArray(R.array.fluff_fields);*/
		
		View view = inflater.inflate(R.layout.character_fluff_fragment, container, false);
		
		lv = (ListView) view.findViewById(R.id.fluff_list);
		refreshFluffListView();
		lv.setOnItemClickListener(this);
		
		return view;		
	}
	
	//An items has been clicked in the list
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		mFluffSelectedForEdit = position;
		showItemDialog(position);
	}
	
	/**
	 * Shows a dialog to edit fluff field.
	 * @param item
	 */
	private void showItemDialog(int fluffIndex) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		//Set up dialog layout
		LayoutInflater inflater = getActivity().getLayoutInflater();

		View dialogView = inflater.inflate(R.layout.character_fluff_dialog, null);
		mDialogFluff = (EditText) dialogView.findViewById(R.id.dialogFluffText);
		
		builder.setTitle(mCharacter.getFluff().getFluffFields(getActivity())[fluffIndex]);
		mDialogFluff.setText(mCharacter.getFluff().getFluffArray()[fluffIndex]);
		
		builder.setView(dialogView)
				.setPositiveButton(R.string.ok_button_text, this)
				.setNegativeButton(R.string.cancel_button_text, this);
		
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	private void refreshFluffListView() {		
		PTFluffAdapter adapter = new PTFluffAdapter(mContainer.getContext(),
				R.layout.character_fluff_row, 
				mCharacter.getFluff().getFluffArray());
		lv.setAdapter(adapter);
	}
	
	public void onClick(DialogInterface dialogInterface, int selection) {
		switch (selection) {
		//OK button tapped
		case DialogInterface.BUTTON_POSITIVE:
			
			mCharacter.getFluff().setFluffByIndex(mFluffSelectedForEdit, 
					getFluffValueFromDialog());
			((PTCharacterSheetActivity)getActivity()).updateCharacterDatabase();
			refreshFluffListView();

			
			break;
		//Cancel Button	tapped
		case DialogInterface.BUTTON_NEGATIVE:
			break;
		default:
			break;
		}
		
		//Close keyboard
		InputMethodManager iMM = (InputMethodManager)getActivity().
				getSystemService(Context.INPUT_METHOD_SERVICE);
		if(mDialogFluff.hasFocus())
			iMM.hideSoftInputFromInputMethod(mDialogFluff.getWindowToken(), 0);
	}
	
	private String getFluffValueFromDialog() {
		String fluffValue = new String(mDialogFluff.getText().toString());
		return fluffValue;
	}

	@Override
	public void onResume() {
		super.onResume();
		refreshFluffListView();
	}
	
	
	
}
