package com.lateensoft.pathfinder.toolkit.views.character;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.adapters.character.SpellBookListAdapter;
import com.lateensoft.pathfinder.toolkit.db.repository.SpellRepository;
import com.lateensoft.pathfinder.toolkit.model.character.Spell;
import com.lateensoft.pathfinder.toolkit.model.character.SpellBook;

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
import com.lateensoft.pathfinder.toolkit.views.ParcelableEditorActivity;

import java.util.Collections;

public class CharacterSpellBookFragment extends AbstractCharacterSheetFragment {
	
	private static final String TAG = CharacterSpellBookFragment.class.getSimpleName();
	
	private ListView m_listView;
	private int m_spellSelectedForEdit;

    private SpellRepository m_spellRepo;
	private SpellBook m_spellBook;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		m_spellRepo = new SpellRepository();
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setRootView(inflater.inflate(R.layout.character_spellbook_fragment,
				container, false));

        Button m_addButton = (Button) getRootView().findViewById(R.id.addSpell);
		m_addButton.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                m_spellSelectedForEdit = -1;
                showSpellEditor(null);
            }
        });
		
		m_listView = (ListView) getRootView().findViewById(R.id.spells);
		m_listView.setOnItemClickListener(new OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                m_spellSelectedForEdit = position;
                showSpellEditor(m_spellBook.get(position));
            }
        });
		
		return getRootView();
	}

	private void refreshSpellListView() {
        Collections.sort(m_spellBook);
		SpellBookListAdapter adapter = new SpellBookListAdapter(getContext(),
				R.layout.character_spellbook_row,
				m_spellBook);
		
		Log.v(TAG, "Called refreshSpellListView");
		
		m_listView.setAdapter(adapter);
	}
	
	private void showSpellEditor(Spell spell) {
		Intent spellEditIntent = new Intent(getContext(),
				SpellEditActivity.class);
		spellEditIntent.putExtra(
				SpellEditActivity.INTENT_EXTRAS_KEY_EDITABLE_PARCELABLE, spell);
		startActivityForResult(spellEditIntent, ParcelableEditorActivity.DEFAULT_REQUEST_CODE);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != ParcelableEditorActivity.DEFAULT_REQUEST_CODE) {
            return;
        }
		switch (resultCode) {
		case Activity.RESULT_OK:
			Spell spell = ParcelableEditorActivity.getParcelableFromIntent(data);
            if (spell != null) {
                if(m_spellSelectedForEdit < 0) {
                    Log.v(TAG, "Adding a spell");
                    spell.setCharacterID(getCurrentCharacterID());
                    if (m_spellRepo.insert(spell) != -1 ) {
                        m_spellBook.add(spell);
                        refreshSpellListView();
                    }
                } else {
                    Log.v(TAG, "Editing a spell");
                    if (m_spellRepo.update(spell) != 0 ) {
                        m_spellBook.set(m_spellSelectedForEdit, spell);
                        refreshSpellListView();
                    }
                }
            }

            break;
		
		case SpellEditActivity.RESULT_DELETE:
			Log.v(TAG, "Deleting a spell");
			Spell spellToDelete = m_spellBook.get(m_spellSelectedForEdit);
			if (spellToDelete != null && m_spellRepo.delete(spellToDelete) != 0) {
				m_spellBook.remove(m_spellSelectedForEdit);
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
		m_spellBook = new SpellBook(m_spellRepo.querySet(getCurrentCharacterID()));
	}

}
