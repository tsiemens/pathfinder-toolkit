package com.lateensoft.pathfinder.toolkit.serialize;

import com.lateensoft.pathfinder.toolkit.model.character.items.Item;
import org.dom4j.Element;

import java.io.InvalidObjectException;

/**
 * @author trevsiemens
 */
public class ItemXMLAdapter extends XMLObjectAdapter<Item> {

    public static final String ELEMENT_NAME = "item";
    private static final String NAME_ATTR = "name";
    private static final String WEIGHT_ATTR = "weight";
    private static final String QUANTITY_ATTR = "quantity";
    private static final String CONTAINED_ATTR = "contained";

    @Override
    public String getElementName() {
        return ELEMENT_NAME;
    }

    @Override
    protected Item createObjectForElement(Element element) throws InvalidObjectException {
        Item item = new Item();
        setObjectContentForElement(item, element);
        return item;
    }

    protected void setObjectContentForElement(Item item, Element element) throws InvalidObjectException {
        item.setName(getStringAttribute(element, NAME_ATTR));
        item.setWeight(getBoundedDoubleAttribute(element, WEIGHT_ATTR, 0.0, Double.MAX_VALUE));
        item.setQuantity(getBoundedIntAttribute(element, QUANTITY_ATTR, 0, Integer.MAX_VALUE));
        item.setContained(getBooleanAttribute(element, CONTAINED_ATTR));
    }

    @Override
    protected void setElementContentForObject(Element element, Item item) {
        element.addAttribute(NAME_ATTR, item.getName());
        element.addAttribute(WEIGHT_ATTR, Double.toString(item.getWeight()));
        element.addAttribute(QUANTITY_ATTR, Integer.toString(item.getQuantity()));
        element.addAttribute(CONTAINED_ATTR, Boolean.toString(item.isContained()));
    }
}
