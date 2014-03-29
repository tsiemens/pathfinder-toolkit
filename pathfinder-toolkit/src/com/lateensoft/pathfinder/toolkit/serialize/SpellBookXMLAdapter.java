package com.lateensoft.pathfinder.toolkit.serialize;

import com.lateensoft.pathfinder.toolkit.model.character.Spell;
import com.lateensoft.pathfinder.toolkit.model.character.SpellBook;

import java.util.List;

/**
 * @author tsiemens
 */
public class SpellBookXMLAdapter extends ListXMLAdapter<SpellBook, Spell, SpellXMLAdapter> {

    public static final String ELEMENT_NAME = "spellbook";

    private final SpellXMLAdapter m_spellAdapter = new SpellXMLAdapter();

    @Override
    public String getElementName() {
        return ELEMENT_NAME;
    }

    @Override
    public SpellXMLAdapter getItemAdapter() {
        return m_spellAdapter;
    }

    @Override
    protected SpellBook createObjectFromItems(List<Spell> items) {
        return new SpellBook(items);
    }
}
