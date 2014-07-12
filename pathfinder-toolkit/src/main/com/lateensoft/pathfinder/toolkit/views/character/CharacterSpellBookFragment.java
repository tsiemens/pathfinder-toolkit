package com.lateensoft.pathfinder.toolkit.views.character;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.adapters.character.SpellBookListAdapter;
import com.lateensoft.pathfinder.toolkit.db.dao.table.SpellDAO;
import com.lateensoft.pathfinder.toolkit.model.character.Spell;
import com.lateensoft.pathfinder.toolkit.model.character.SpellBook;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import com.lateensoft.pathfinder.toolkit.views.ParcelableEditorActivity;

import java.util.Collections;
import java.util.List;

public class CharacterSpellBookFragment extends IndexedParcelableListFragment<Spell, SpellDAO> {
    private ListView spellList;

    private SpellDAO spellDao;
    private SpellBook spellBook;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        spellDao = new SpellDAO(getContext());
    }
    
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        
        setRootView(inflater.inflate(R.layout.character_spellbook_fragment,
                container, false));

        Button m_addButton = (Button) getRootView().findViewById(R.id.addSpell);
        m_addButton.setOnClickListener(getAddButtonClickListener());
        
        spellList = (ListView) getRootView().findViewById(R.id.spells);
        spellList.setOnItemClickListener(getListItemClickListener());
        
        return getRootView();
    }

    @Override
    protected Class<? extends ParcelableEditorActivity> getParcelableEditorActivity() {
        return SpellEditActivity.class;
    }

    @Override
    protected SpellDAO getDAO() {
        return spellDao;
    }

    @Override
    protected List<Spell> getModel() {
        return spellBook;
    }

    @Override
    public void updateFragmentUI() {
        refreshSpellListView();
    }

    private void refreshSpellListView() {
        Collections.sort(spellBook);
        SpellBookListAdapter adapter = new SpellBookListAdapter(getContext(),
                R.layout.character_spellbook_row,
                spellBook);

        spellList.setAdapter(adapter);
    }
    
    @Override
    public String getFragmentTitle() {
        return getString(R.string.tab_character_spells);
    }

    @Override
    public void loadFromDatabase() {
        spellBook = new SpellBook(spellDao.findAllForOwner(getCurrentCharacterID()));
    }

}
