package com.lateensoft.pathfinder.toolkit.serialize;

import com.lateensoft.pathfinder.toolkit.model.character.items.PTArmor;
import org.dom4j.Element;

import java.io.InvalidObjectException;

/**
 * @author trevsiemens
 */
public class ArmorXMLAdapter extends XMLObjectAdapter<PTArmor> {

    public static final String ELEMENT_NAME = "armor";
    public static final String ATTR_WORN = "worn";
    public static final String ATTR_AC_BONUS = "ac";
    public static final String ATTR_ARMOR_CHECK_PENALTY = "armor-check-penalty";
    public static final String ATTR_MAX_DEX = "max-dex";
    public static final String ATTR_SPELL_FAIL = "spell-failure";
    public static final String ATTR_SPEED = "speed";
    public static final String ATTR_SPEC_PROPS = "special-properties";
    public static final String ATTR_SIZE = "size";

    private final ItemXMLAdapter m_itemXMLAdapter = new ItemXMLAdapter();

    @Override
    public String getElementName() {
        return ELEMENT_NAME;
    }

    @Override
    protected PTArmor createObjectForElement(Element element) throws InvalidObjectException {
        PTArmor armor = new PTArmor();
        setObjectContentForElement(armor, element);
        return armor;
    }

    void setObjectContentForElement(PTArmor armor, Element element) throws InvalidObjectException {
        m_itemXMLAdapter.setObjectContentForElement(armor, element);
        armor.setWorn(getBooleanAttribute(element, ATTR_WORN));
        armor.setACBonus(getBoundedIntAttribute(element, ATTR_AC_BONUS, -20, 20));
        armor.setCheckPen(getBoundedIntAttribute(element, ATTR_ARMOR_CHECK_PENALTY, -20, 0));
        armor.setMaxDex(getBoundedIntAttribute(element, ATTR_MAX_DEX, 0, 40));
        armor.setSpellFail(getBoundedIntAttribute(element, ATTR_SPELL_FAIL, 0, 95));
        armor.setSpeed(getBoundedIntAttribute(element, ATTR_SPEED, 0, 120));
        armor.setSpecialProperties(getStringAttribute(element, ATTR_SPEC_PROPS));

        String sizeString = getStringAttribute(element, ATTR_SIZE);
        if (!PTArmor.isValidSize(sizeString)) {
            throw new InvalidObjectException("Armor attribute 'size' invalid. Must be 'S', 'M', or 'L'.");
        }
        armor.setSize(sizeString);
    }

    @Override
    protected void setElementContentForObject(Element element, PTArmor armor) {
        m_itemXMLAdapter.setElementContentForObject(element, armor);

        element.addAttribute(ATTR_WORN, Boolean.toString(armor.isWorn()));
        element.addAttribute(ATTR_AC_BONUS, Integer.toString(armor.getACBonus()));
        element.addAttribute(ATTR_ARMOR_CHECK_PENALTY, Integer.toString(armor.getCheckPen()));
        element.addAttribute(ATTR_MAX_DEX, Integer.toString(armor.getMaxDex()));
        element.addAttribute(ATTR_SPELL_FAIL, Integer.toString(armor.getSpellFail()));
        element.addAttribute(ATTR_SPEED, Integer.toString(armor.getSpeed()));
        element.addAttribute(ATTR_SIZE, armor.getSize());
        element.addAttribute(ATTR_SPEC_PROPS, armor.getSpecialProperties());
    }
}
