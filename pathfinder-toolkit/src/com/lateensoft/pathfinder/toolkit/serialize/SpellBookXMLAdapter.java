package com.lateensoft.pathfinder.toolkit.serialize;

import com.lateensoft.pathfinder.toolkit.model.character.PTSpell;
import com.lateensoft.pathfinder.toolkit.model.character.PTSpellBook;

import java.util.List;

/**
 * @author tsiemens
 */
public class SpellBookXMLAdapter extends ListXMLAdapter<PTSpellBook, PTSpell, SpellXMLAdapter> {

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
    protected PTSpellBook createObjectFromItems(List<PTSpell> items) {
        return new PTSpellBook(items);
    }
}
