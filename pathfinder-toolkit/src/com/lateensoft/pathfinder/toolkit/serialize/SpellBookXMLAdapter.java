package com.lateensoft.pathfinder.toolkit.serialize;

import com.lateensoft.pathfinder.toolkit.model.character.PTSpell;
import com.lateensoft.pathfinder.toolkit.model.character.PTSpellBook;
import org.dom4j.Element;

import java.util.List;

/**
 * @author trevsiemens
 */
public class SpellBookXMLAdapter extends XMLObjectAdapter<PTSpellBook> {

    public static final String ELEMENT_NAME = "spellbook";

    private final SpellXMLAdapter m_spellAdapter = new SpellXMLAdapter();

    @Override
    public String getElementName() {
        return ELEMENT_NAME;
    }

    @Override
    protected PTSpellBook convertToObject(Element element) {
        List<PTSpell> spells = getSubObjects(element, m_spellAdapter);
        return new PTSpellBook(spells);
    }

    @Override
    protected void setContentForObject(Element element, PTSpellBook spellBook) {
        for (PTSpell spell : spellBook) {
            element.add(m_spellAdapter.toXML(spell));
        }
    }
}
