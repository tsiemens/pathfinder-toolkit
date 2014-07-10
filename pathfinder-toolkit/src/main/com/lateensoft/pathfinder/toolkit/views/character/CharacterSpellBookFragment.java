package com.lateensoft.pathfinder.toolkit.views.character;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.adapters.character.SpellBookListAdapter;
import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.db.dao.table.SpellDAO;
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
    
    private ListView spellList;
    private int spellIndexSelectedForEdit;

    private SpellDAO spellDao;
    private SpellBook spellBook;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        spellDao = new SpellDAO(getContext());
    }
    
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setRootView(inflater.inflate(R.layout.character_spellbook_fragment,
                container, false));

        Button m_addButton = (Button) getRootView().findViewById(R.id.addSpell);
        m_addButton.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                spellIndexSelectedForEdit = -1;
                showSpellEditor(null);
            }
        });
        
        spellList = (ListView) getRootView().findViewById(R.id.spells);
        spellList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                spellIndexSelectedForEdit = position;
                showSpellEditor(spellBook.get(position));
            }
        });
        
        return getRootView();
    }

    private void refreshSpellListView() {
        Collections.sort(spellBook);
        SpellBookListAdapter adapter = new SpellBookListAdapter(getContext(),
                R.layout.character_spellbook_row,
                spellBook);
        
        Log.v(TAG, "Called refreshSpellListView");
        
        spellList.setAdapter(adapter);
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
                if(spellIndexSelectedForEdit < 0) {
                    Log.v(TAG, "Adding a spell");
                    spell.setCharacterID(getCurrentCharacterID());
                    try {
                        spellDao.add(getCurrentCharacterID(), spell);
                        spellBook.add(spell);
                        refreshSpellListView();
                    } catch (DataAccessException e) {
                        Log.e(TAG, "Failed to add spell", e);
                    }

                } else {
                    Log.v(TAG, "Editing a spell");
                    try {
                        spellDao.update(getCurrentCharacterID(), spell);
                        spellBook.set(spellIndexSelectedForEdit, spell);
                        refreshSpellListView();
                    } catch (DataAccessException e) {
                        Log.e(TAG, "Failed to update spell", e);
                    }

                }
            }

            break;
        
        case SpellEditActivity.RESULT_DELETE:
            Log.v(TAG, "Deleting a spell");
            Spell spellToDelete = spellBook.get(spellIndexSelectedForEdit);
            if (spellToDelete != null) {
                try {
                    spellDao.remove(spellToDelete);
                    spellBook.remove(spellIndexSelectedForEdit);
                    refreshSpellListView();
                } catch (DataAccessException e) {
                    Log.e(TAG, "Failed to remove spell " + spellToDelete.getId(), e);
                }
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
        spellBook = new SpellBook(spellDao.findAllForOwner(getCurrentCharacterID()));
    }

}
