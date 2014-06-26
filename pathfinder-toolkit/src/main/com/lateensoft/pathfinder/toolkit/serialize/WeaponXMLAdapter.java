package com.lateensoft.pathfinder.toolkit.serialize;

import com.lateensoft.pathfinder.toolkit.model.character.items.Size;
import com.lateensoft.pathfinder.toolkit.model.character.items.Weapon;
import com.lateensoft.pathfinder.toolkit.model.character.items.WeaponType;
import org.dom4j.Element;

import java.io.InvalidObjectException;

/**
 * @author trevsiemens
 */
public class WeaponXMLAdapter extends XMLObjectAdapter<Weapon> {

    public static final String ELEMENT_NAME = "weapon";
    public static final String ATTR_ATTACK_BONUS = "attack-bonus";
    public static final String ELMT_DAMAGE = "damage";
    public static final String ELMT_CRIT = "crit";
    public static final String ATTR_RANGE = "range";
    public static final String ELMT_SPEC_PROPS = "special-properties";
    public static final String ATTR_AMMO = "ammo";
    public static final String ATTR_TYPE = "type";
    public static final String ATTR_SIZE = "size";

    private final ItemXMLAdapter m_itemXMLAdapter = new ItemXMLAdapter();

    @Override
    public String getElementName() {
        return ELEMENT_NAME;
    }

    @Override
    protected Weapon createObjectForElement(Element element) throws InvalidObjectException {
        Weapon weapon = new Weapon();
        setObjectContentForElement(weapon, element);
        return weapon;
    }

    void setObjectContentForElement(Weapon weapon, Element element) throws InvalidObjectException {
        m_itemXMLAdapter.setObjectContentForElement(weapon, element);
        weapon.setTotalAttackBonus(getBoundedIntAttribute(element, ATTR_ATTACK_BONUS, -10, 40));
        weapon.setDamage(getSubElementText(element, ELMT_DAMAGE));
        weapon.setCritical(getSubElementText(element, ELMT_CRIT));
        weapon.setRange(getBoundedIntAttribute(element, ATTR_RANGE, 0, Integer.MAX_VALUE));
        weapon.setAmmunition(getBoundedIntAttribute(element, ATTR_AMMO, 0, Integer.MAX_VALUE));
        weapon.setSpecialProperties(getSubElementText(element, ELMT_SPEC_PROPS));

        String typeString = getStringAttribute(element, ATTR_TYPE);
        try {
            WeaponType type = WeaponType.forKey(typeString);
            weapon.setType(type);
        } catch (Exception e) {
            throw new InvalidObjectException("Weapon attribute 'type' invalid. Reference weapon screens for valid options.");
        }

        String sizeString = getStringAttribute(element, ATTR_SIZE);
        try {
            Size size = Size.forKey(sizeString);
            weapon.setSize(size);
        } catch (Exception e) {
            throw new InvalidObjectException("Weapon attribute 'size' invalid. Must be 'S', 'M', or 'L'.");
        }
    }

    @Override
    protected void setElementContentForObject(Element element, Weapon weapon) {
        m_itemXMLAdapter.setElementContentForObject(element, weapon);
        element.addAttribute(ATTR_ATTACK_BONUS, Integer.toString(weapon.getTotalAttackBonus()));
        addSubElementText(element, ELMT_DAMAGE, weapon.getDamage());
        addSubElementText(element, ELMT_CRIT, weapon.getCritical());
        element.addAttribute(ATTR_RANGE, Integer.toString(weapon.getRange()));
        element.addAttribute(ATTR_AMMO, Integer.toString(weapon.getAmmunition()));
        element.addAttribute(ATTR_TYPE, weapon.getType().getKey());
        element.addAttribute(ATTR_SIZE, weapon.getSize().getKey());
        addSubElementText(element, ELMT_SPEC_PROPS, weapon.getSpecialProperties());
    }
}
