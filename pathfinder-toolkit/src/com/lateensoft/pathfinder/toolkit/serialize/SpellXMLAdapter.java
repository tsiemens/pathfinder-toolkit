package com.lateensoft.pathfinder.toolkit.serialize;

import com.lateensoft.pathfinder.toolkit.model.character.PTSpell;
import com.lateensoft.pathfinder.toolkit.model.character.PTSpellBook;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

import java.io.InvalidObjectException;

/**
 * @author tsiemens
 */
public class SpellXMLAdapter extends XMLObjectAdapter<PTSpell> {

    public static final String ELEMENT_NAME = "spell";
    private static final String NAME_ATTR = "name";
    private static final String LEVEL_ATTR = "level";
    private static final String PREPARED_ATTR = "prepared";
    private static final String DESC_ELEMENT = "desc";


    @Override
    public String getElementName() {
        return ELEMENT_NAME;
    }

    @Override
    protected void setContentForObject(Element element, PTSpell spell) {
        element.addAttribute(NAME_ATTR, spell.getName());
        element.addAttribute(LEVEL_ATTR, Integer.toString(spell.getLevel()));
        element.addAttribute(PREPARED_ATTR, Integer.toString(spell.getPrepared()));

        Element description = new DefaultElement(DESC_ELEMENT);
        description.setText(spell.getDescription());
        element.add(description);
    }

    @Override
    protected PTSpell convertToObject(Element element) throws InvalidObjectException {
        PTSpell spell = new PTSpell();
        spell.setName(getStringAttribute(element, NAME_ATTR));
        spell.setLevel(getBoundedIntAttribute(element, LEVEL_ATTR, 0, 10));
        spell.setPrepared(getBoundedIntAttribute(element, PREPARED_ATTR, 0, 40));
        spell.setDescription(getSubElementContent(element, DESC_ELEMENT));
        return spell;
    }


}
