package com.lateensoft.pathfinder.toolkit.views.character;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.adapters.character.FluffListAdapter;
import com.lateensoft.pathfinder.toolkit.db.repository.FluffInfoRepository;
import com.lateensoft.pathfinder.toolkit.model.character.FluffInfo;

public class CharacterFluffFragment extends AbstractCharacterSheetFragment implements
OnItemClickListener, android.content.DialogInterface.OnClickListener{
	@SuppressWarnings("unused")
	private static final String TAG = CharacterFluffFragment.class.getSimpleName();
	private ListView m_fluffList;
	private int m_fluffSelectedForEdit;
	
	private FluffInfoRepository m_fluffRepo;
	private FluffInfo m_fluffModel;
	
	private EditText m_dialogET;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		m_fluffRepo = new FluffInfoRepository();
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

	private void showItemDialog(int fluffIndex) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		//Set up dialog layout
		LayoutInflater inflater = LayoutInflater.from(getContext());

		View dialogView = inflater.inflate(R.layout.character_fluff_dialog, null);
		m_dialogET = (EditText) dialogView.findViewById(R.id.dialogFluffText);

		builder.setTitle(m_fluffModel.getFluffFields(getContext())[fluffIndex]);
		m_dialogET.append(m_fluffModel.getFluffArray()[fluffIndex]);
		
		builder.setView(dialogView)
				.setPositiveButton(R.string.ok_button_text, this)
				.setNegativeButton(R.string.cancel_button_text, this);
		
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	private void refreshFluffListView() {
		if (m_fluffModel != null) {
			FluffListAdapter adapter = new FluffListAdapter(getContext(),
					R.layout.character_fluff_row, 
					m_fluffModel.getFluffArray());
			m_fluffList.setAdapter(adapter);
			setTitle(m_fluffModel.getName());
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
		hideKeyboardDelayed(0);
	}
	
	private String getFluffValueFromDialog() {
		return m_dialogET.getText().toString();
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
		if (m_fluffModel != null) {
			m_fluffRepo.update(m_fluffModel);
		}
	}

	@Override
	public void loadFromDatabase() {
		m_fluffModel = m_fluffRepo.query(getCurrentCharacterID());
	}
	
}
