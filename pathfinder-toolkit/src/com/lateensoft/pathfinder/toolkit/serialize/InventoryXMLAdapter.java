package com.lateensoft.pathfinder.toolkit.serialize;

import com.lateensoft.pathfinder.toolkit.model.character.Inventory;
import com.lateensoft.pathfinder.toolkit.model.character.items.Armor;
import com.lateensoft.pathfinder.toolkit.model.character.items.Item;
import com.lateensoft.pathfinder.toolkit.model.character.items.Weapon;
import org.dom4j.Element;

import java.io.InvalidObjectException;
import java.util.List;

/**
 * @author trevsiemens
 */
public class InventoryXMLAdapter extends XMLObjectAdapter<Inventory> {

    public static final String ELEMENT_NAME = "inventory";

    private final ItemXMLAdapter m_itemXMLAdapter = new ItemXMLAdapter();
    private final WeaponXMLAdapter m_weaponXMLAdapter = new WeaponXMLAdapter();
    private final ArmorXMLAdapter m_armorXMLAdapter = new ArmorXMLAdapter();

    @Override
    public String getElementName() {
        return ELEMENT_NAME;
    }

    @Override
    protected Inventory createObjectForElement(Element element) throws InvalidObjectException {
        Inventory inventory = new Inventory();
        inventory.getItems().addAll(getSubObjects(element, m_itemXMLAdapter));
        inventory.getArmors().addAll(getSubObjects(element, m_armorXMLAdapter));
        inventory.getWeapons().addAll(getSubObjects(element, m_weaponXMLAdapter));
        return inventory;
    }

    @Override
    protected void setElementContentForObject(Element element, Inventory inventory) {
        List<Item> items = inventory.getItems();
        for (Item item : items) {
            element.add(m_itemXMLAdapter.toXML(item));
        }

        List<Armor> armors = inventory.getArmors();
        for (Armor armor : armors) {
            element.add(m_armorXMLAdapter.toXML(armor));
        }

        List<Weapon> weapons = inventory.getWeapons();
        for (Weapon weapon : weapons) {
            element.add(m_weaponXMLAdapter.toXML(weapon));
        }
    }
}
