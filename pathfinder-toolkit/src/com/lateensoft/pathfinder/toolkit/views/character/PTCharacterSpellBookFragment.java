package com.lateensoft.pathfinder.toolkit.views.character;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.adapters.character.PTSpellBookAdapter;
import com.lateensoft.pathfinder.toolkit.db.repository.PTSpellRepository;
import com.lateensoft.pathfinder.toolkit.model.character.PTSpell;
import com.lateensoft.pathfinder.toolkit.model.character.PTSpellBook;

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
	
	private ListView m_listView;
	private int m_spellSelectedForEdit;
	private Button m_addButton;
	
	private PTSpellRepository m_spellRepo;
	private PTSpellBook m_spellBook;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		m_spellRepo = new PTSpellRepository();
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setRootView(inflater.inflate(R.layout.character_spellbook_fragment,
				container, false));
		
		m_addButton = (Button) getRootView().findViewById(R.id.addSpell);
		m_addButton.setOnClickListener(this);
		
		m_listView = (ListView) getRootView().findViewById(R.id.spells);
		m_listView.setOnItemClickListener(this);
		
		return getRootView();
	}

	private void refreshSpellListView() {
		
		PTSpellBookAdapter adapter = new PTSpellBookAdapter(getActivity(),
				R.layout.character_spellbook_row,
				m_spellBook.getSpells());
		
		Log.v(TAG, "Called refreshSpellListView");
		
		m_listView.setAdapter(adapter);
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Log.v(TAG, "Item clicked: " + position);
		m_spellSelectedForEdit = position;
		showSpellEditor(m_spellBook.getSpell(position));
	}
	
	/**
	 * Add spell button was tapped
	 */
	public void onClick(View v) {
		Log.v(TAG, "Add button clicked");
		m_spellSelectedForEdit = -1;
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
			if(m_spellSelectedForEdit < 0) {
				Log.v(TAG, "Adding a spell");
				if(spell != null) {
					spell.setCharacterID(getCurrentCharacterID());
					if (m_spellRepo.insert(spell) != -1 ) {
						m_spellBook.addSpell(spell);
						refreshSpellListView(); 
					}
				}
			} else {
				Log.v(TAG, "Editing a spell");
				if (m_spellRepo.update(spell) != 0 ) {
					m_spellBook.setSpell(m_spellSelectedForEdit, spell);
					refreshSpellListView();
				}
			}
			
			break;
		
		case PTCharacterSpellEditActivity.RESULT_DELETE:
			Log.v(TAG, "Deleting a spell");
			PTSpell spellToDelete = m_spellBook.getSpell(m_spellSelectedForEdit);
			if (spellToDelete != null && m_spellRepo.delete(spellToDelete) != 0) {
				m_spellBook.deleteSpell(m_spellSelectedForEdit);
				refreshSpellListView();
			}
			break;
		
		case Activity.RESULT_CANCELED:
			break;
		
		default:
			break;
		}
		updateDatabase();
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

	@Override
	public void updateDatabase() {
		// Done dynamically
	}

	@Override
	public void loadFromDatabase() {
		m_spellBook = new PTSpellBook(m_spellRepo.querySet(getCurrentCharacterID()));
	}

}
