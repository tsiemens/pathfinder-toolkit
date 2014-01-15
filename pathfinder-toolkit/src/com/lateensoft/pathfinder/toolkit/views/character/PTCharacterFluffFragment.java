package com.lateensoft.pathfinder.toolkit.views.character;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.adapters.character.PTFluffAdapter;
import com.lateensoft.pathfinder.toolkit.db.repository.PTFluffInfoRepository;
import com.lateensoft.pathfinder.toolkit.model.character.PTFluffInfo;

public class PTCharacterFluffFragment extends PTCharacterSheetFragment implements 
OnItemClickListener, android.content.DialogInterface.OnClickListener{
	@SuppressWarnings("unused")
	private static final String TAG = PTCharacterFluffFragment.class.getSimpleName();
	private ListView m_fluffList;
	private int m_fluffSelectedForEdit;
	
	private PTFluffInfoRepository m_fluffRepo;
	private PTFluffInfo m_fluffModel;
	
	private EditText m_dialogET;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		m_fluffRepo = new PTFluffInfoRepository();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		setRootView(inflater.inflate(R.layout.character_fluff_fragment, 
				container, false));
		
		m_fluffList = (ListView) getRootView().findViewById(R.id.fluff_list);
		m_fluffList.setOnItemClickListener(this);
		
		return getRootView();		
	}
	
	//An items has been clicked in the list
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		m_fluffSelectedForEdit = position;
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
		m_dialogET = (EditText) dialogView.findViewById(R.id.dialogFluffText);
		
		builder.setTitle(m_fluffModel.getFluffFields(getActivity())[fluffIndex]);
		m_dialogET.setText(m_fluffModel.getFluffArray()[fluffIndex]);
		
		builder.setView(dialogView)
				.setPositiveButton(R.string.ok_button_text, this)
				.setNegativeButton(R.string.cancel_button_text, this);
		
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	private void refreshFluffListView() {
		if (m_fluffModel != null) {
			PTFluffAdapter adapter = new PTFluffAdapter(getActivity(),
					R.layout.character_fluff_row, 
					m_fluffModel.getFluffArray());
			m_fluffList.setAdapter(adapter);
		}
	}
	
	public void onClick(DialogInterface dialogInterface, int selection) {
		switch (selection) {
		//OK button tapped
		case DialogInterface.BUTTON_POSITIVE:
			
			m_fluffModel.setFluffByIndex(m_fluffSelectedForEdit, 
					getFluffValueFromDialog());
			updateDatabase();
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
		if(m_dialogET.hasFocus())
			iMM.hideSoftInputFromInputMethod(m_dialogET.getWindowToken(), 0);
	}
	
	private String getFluffValueFromDialog() {
		String fluffValue = new String(m_dialogET.getText().toString());
		return fluffValue;
	}

	@Override
	public void updateFragmentUI() {
		refreshFluffListView();
	}
	
	@Override
	public String getFragmentTitle() {
		return getString(R.string.tab_character_fluff);
	}

	@Override
	public void updateDatabase() {
		m_fluffRepo.update(m_fluffModel);
	}

	@Override
	public void loadFromDatabase() {
		m_fluffModel = m_fluffRepo.query(getCurrentCharacterID());
	}
	
}
