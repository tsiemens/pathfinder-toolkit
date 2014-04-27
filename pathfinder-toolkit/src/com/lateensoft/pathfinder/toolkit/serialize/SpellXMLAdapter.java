package com.lateensoft.pathfinder.toolkit.serialize;

import com.lateensoft.pathfinder.toolkit.model.character.Spell;
import org.dom4j.Element;

import java.io.InvalidObjectException;

/**
 * @author tsiemens
 */
public class SpellXMLAdapter extends XMLObjectAdapter<Spell> {

    public static final String ELEMENT_NAME = "spell";
    private static final String NAME_ELMT = "name";
    private static final String LEVEL_ATTR = "level";
    private static final String PREPARED_ATTR = "prepared";
    private static final String DESC_ELMT = "desc";


    @Override
    public String getElementName() {
        return ELEMENT_NAME;
    }

    @Override
    protected Spell createObjectForElement(Element element) throws InvalidObjectException {
        Spell spell = new Spell();
        spell.setName(getSubElementText(element, NAME_ELMT));
        spell.setLevel(getBoundedIntAttribute(element, LEVEL_ATTR, 0, 10));
        spell.setPrepared(getBoundedIntAttribute(element, PREPARED_ATTR, 0, 40));
        spell.setDescription(getSubElementText(element, DESC_ELMT));
        return spell;
    }

    @Override
    protected void setElementContentForObject(Element element, Spell spell) {
        addSubElementText(element, NAME_ELMT, spell.getName());
        element.addAttribute(LEVEL_ATTR, Integer.toString(spell.getLevel()));
        element.addAttribute(PREPARED_ATTR, Integer.toString(spell.getPrepared()));
        addSubElementText(element, DESC_ELMT, spell.getDescription());
    }
}