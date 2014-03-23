package com.lateensoft.pathfinder.toolkit.serialize;

import com.lateensoft.pathfinder.toolkit.model.character.PTInventory;
import com.lateensoft.pathfinder.toolkit.model.character.items.PTArmor;
import com.lateensoft.pathfinder.toolkit.model.character.items.PTItem;
import com.lateensoft.pathfinder.toolkit.model.character.items.PTWeapon;
import org.dom4j.Element;

import java.io.InvalidObjectException;
import java.util.List;

/**
 * @author trevsiemens
 */
public class InventoryXMLAdapter extends XMLObjectAdapter<PTInventory> {

    public static final String ELEMENT_NAME = "inventory";

    private final ItemXMLAdapter m_itemXMLAdapter = new ItemXMLAdapter();
    private final WeaponXMLAdapter m_weaponXMLAdapter = new WeaponXMLAdapter();
    private final ArmorXMLAdapter m_armorXMLAdapter = new ArmorXMLAdapter();

    @Override
    public String getElementName() {
        return ELEMENT_NAME;
    }

    @Override
    protected PTInventory createObjectForElement(Element element) throws InvalidObjectException {
        PTInventory inventory = new PTInventory();
        inventory.getItems().addAll(getSubObjects(element, m_itemXMLAdapter));
        inventory.getArmors().addAll(getSubObjects(element, m_armorXMLAdapter));
        inventory.getWeapons().addAll(getSubObjects(element, m_weaponXMLAdapter));
        return inventory;
    }

    @Override
    protected void setElementContentForObject(Element element, PTInventory inventory) {
        List<PTItem> items = inventory.getItems();
        for (PTItem item : items) {
            element.add(m_itemXMLAdapter.toXML(item));
        }

        List<PTArmor> armors = inventory.getArmors();
        for (PTArmor armor : armors) {
            element.add(m_armorXMLAdapter.toXML(armor));
        }

        List<PTWeapon> weapons = inventory.getWeapons();
        for (PTWeapon weapon : weapons) {
            element.add(m_weaponXMLAdapter.toXML(weapon));
        }
    }
}
