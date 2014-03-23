package com.lateensoft.pathfinder.toolkit.serialize;

import com.lateensoft.pathfinder.toolkit.PTBaseApplication;
import com.lateensoft.pathfinder.toolkit.model.character.items.PTWeapon;
import org.dom4j.Element;

import java.io.InvalidObjectException;

/**
 * @author trevsiemens
 */
public class WeaponXMLAdapter extends XMLObjectAdapter<PTWeapon> {

    public static final String ELEMENT_NAME = "weapon";
    public static final String ATTR_ATTACK_BONUS = "attack-bonus";
    public static final String ATTR_DAMAGE = "damage";
    public static final String ATTR_CRIT = "crit";
    public static final String ATTR_RANGE = "range";
    public static final String ATTR_SPEC_PROPS = "special-properties";
    public static final String ATTR_AMMO = "ammo";
    public static final String ATTR_TYPE = "type";
    public static final String ATTR_SIZE = "size";

    private final ItemXMLAdapter m_itemXMLAdapter = new ItemXMLAdapter();

    @Override
    public String getElementName() {
        return ELEMENT_NAME;
    }

    @Override
    protected PTWeapon createObjectForElement(Element element) throws InvalidObjectException {
        PTWeapon weapon = new PTWeapon();
        setObjectContentForElement(weapon, element);
        return weapon;
    }

    void setObjectContentForElement(PTWeapon weapon, Element element) throws InvalidObjectException {
        m_itemXMLAdapter.setObjectContentForElement(weapon, element);
        weapon.setTotalAttackBonus(getBoundedIntAttribute(element, ATTR_ATTACK_BONUS, -10, 40));
        weapon.setDamage(getStringAttribute(element, ATTR_DAMAGE));
        weapon.setCritical(getStringAttribute(element, ATTR_CRIT));
        weapon.setRange(getBoundedIntAttribute(element, ATTR_RANGE, 0, Integer.MAX_VALUE));
        weapon.setAmmunition(getBoundedIntAttribute(element, ATTR_AMMO, 0, Integer.MAX_VALUE));
        weapon.setSpecialProperties(getStringAttribute(element, ATTR_SPEC_PROPS));

        String typeString = getStringAttribute(element, ATTR_TYPE);
        if (!PTWeapon.isValidType(PTBaseApplication.getAppContext(), typeString)) {
            throw new InvalidObjectException("Weapon attribute 'type' invalid. Reference weapon screens for valid options.");
        }
        weapon.setType(typeString);


        String sizeString = getStringAttribute(element, ATTR_SIZE);
        if (!PTWeapon.isValidSize(PTBaseApplication.getAppContext(), sizeString)) {
            throw new InvalidObjectException("Weapon attribute 'size' invalid. Must be 'S', 'M', or 'L'.");
        }
        weapon.setSize(sizeString);
    }

    @Override
    protected void setElementContentForObject(Element element, PTWeapon weapon) {
        m_itemXMLAdapter.setElementContentForObject(element, weapon);
        element.addAttribute(ATTR_ATTACK_BONUS, Integer.toString(weapon.getTotalAttackBonus()));
        element.addAttribute(ATTR_DAMAGE, weapon.getDamage());
        element.addAttribute(ATTR_CRIT, weapon.getCritical());
        element.addAttribute(ATTR_RANGE, Integer.toString(weapon.getRange()));
        element.addAttribute(ATTR_AMMO, Integer.toString(weapon.getAmmunition()));
        element.addAttribute(ATTR_TYPE, weapon.getType());
        element.addAttribute(ATTR_SIZE, weapon.getSize());
        element.addAttribute(ATTR_SPEC_PROPS, weapon.getSpecialProperties());
    }
}
