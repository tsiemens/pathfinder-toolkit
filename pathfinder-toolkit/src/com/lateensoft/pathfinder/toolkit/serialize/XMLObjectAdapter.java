package com.lateensoft.pathfinder.toolkit.serialize;

import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

/**
 * @author trevsiemens
 */
public abstract class XMLObjectAdapter<E> {

    public abstract String getElementName();

    protected abstract E convertToObject(Element element);
    protected abstract void setContentForObject(Element element, E object);

    public Element toXML(E object) {
        Element element = new DefaultElement(getElementName());
        setContentForObject(element, object);
        return element;
    }

    public E toObject(Element element) {
        if (element.getName().equals(getElementName())) {
            return convertToObject(element);
        } else {
            throw new IllegalArgumentException("Element type " + element.getName() +
                    " does not match " + getElementName());
        }
    }

    public static int getIntAttribute(Element element, String attrName, int defaultVal) {
        try {
            return Integer.parseInt(element.attributeValue(attrName));
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

    public static long getLongAttribute(Element element, String attrName, long defaultVal) {
        try {
            return Long.parseLong(element.attributeValue(attrName));
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

    public static String getStringAttribute(Element element, String attrName, String defaultVal) {
        String val = element.attributeValue(attrName);
        return (val != null) ? val : defaultVal;
    }

    public static String getSubElementContent(Element element, String subElementName, String defaultVal) {
        Element subElement = element.element(subElementName);
        if (subElement != null && subElement.getText() != null) {
            return subElement.getText();
        } else {
            return defaultVal;
        }
    }
}
