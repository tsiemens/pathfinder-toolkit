package com.lateensoft.pathfinder.toolkit.views.character;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.adapters.character.PTSpellBookAdapter;
import com.lateensoft.pathfinder.toolkit.model.character.PTSpell;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class PTCharacterSpellBookFragment extends PTCharacterSheetFragment implements
	OnClickListener, OnItemClickListener {
	
	private static final String TAG = PTCharacterSpellBookFragment.class.getSimpleName();
	
	private ListView mListView;
	int mSpellSelectedForEdit;
	private Button mAddButton;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setRootView(inflater.inflate(R.layout.character_spellbook_fragment,
				container, false));
		
		mAddButton = (Button) getRootView().findViewById(R.id.addSpell);
		mAddButton.setOnClickListener(this);
		
		mListView = new ListView(container.getContext());
		setListViews(getRootView());
		refreshSpellListView();
		setonItemClickListeners();
		
		return getRootView();
	}

	private void setListViews(View fragmentView) {
		mListView = (ListView) fragmentView.findViewById(R.id.spells);
	}

	private void setonItemClickListeners() {
		mListView.setOnItemClickListener(this);
	}


	private void refreshSpellListView() {
		
		PTSpellBookAdapter adapter = new PTSpellBookAdapter(getActivity(),
				R.layout.character_spellbook_row,
				mCharacter.getSpellBook().getSpells());
		
		Log.v(TAG, "Called refreshSpellListView");
		
		mListView.setAdapter(adapter);
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Log.v(TAG, "Item clicked: " + position);
		mSpellSelectedForEdit = position;
		showSpellEditor(mCharacter.getSpellBook().getSpell(position));
	}
	
	/**
	 * Add spell button was tapped
	 */
	public void onClick(View v) {
		Log.v(TAG, "Add button clicked");
		mSpellSelectedForEdit = -1;
		showSpellEditor(null);
	}
	
	private void showSpellEditor(PTSpell spell) {
		Intent spellEditIntent = new Intent(getActivity(),
				PTCharacterSpellEditActivity.class);
		spellEditIntent.putExtra(
				PTCharacterSpellEditActivity.INTENT_EXTRAS_KEY_EDITABLE_PARCELABLE, spell);
		startActivityForResult(spellEditIntent, 0);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case Activity.RESULT_OK:
			PTSpell spell = data.getExtras().getParcelable(
					PTCharacterSpellEditActivity.INTENT_EXTRAS_KEY_EDITABLE_PARCELABLE);
			Log.v(TAG, "Add/edit spell OK: " + spell.getName());
			if(mSpellSelectedForEdit < 0) {
				Log.v(TAG, "Adding a spell");
				if(spell != null) {
					mCharacter.getSpellBook().addSpell(spell);
					refreshSpellListView(); 
				}
			} else {
				Log.v(TAG, "Editing a spell");
				mCharacter.getSpellBook().setSpell(mSpellSelectedForEdit, spell);
				refreshSpellListView();
			}
			
			break;
		
		case PTCharacterSpellEditActivity.RESULT_DELETE:
			Log.v(TAG, "Deleting a spell");
			mCharacter.getSpellBook().deleteSpell(mSpellSelectedForEdit);
			refreshSpellListView();
			break;
		
		case Activity.RESULT_CANCELED:
			break;
		
		default:
			break;
		}
		updateCharacterDatabase();
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void updateFragmentUI() {
		refreshSpellListView();
		
	}
	
	@Override
	public String getFragmentTitle() {
		return getString(R.string.tab_character_spells);
	}

}
