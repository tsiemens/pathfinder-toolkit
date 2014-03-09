package com.lateensoft.pathfinder.toolkit.serialize;

import com.google.common.collect.Lists;
import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

import java.util.List;

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
        return getBoundedIntAttribute(element, attrName, Integer.MIN_VALUE, Integer.MAX_VALUE, defaultVal);
    }

    public static int getBoundedIntAttribute(Element element, String attrName, int lowestVal, int highestVal, int defaultVal) {
        try {
            int val = Integer.parseInt(element.attributeValue(attrName));
            if (val < lowestVal) {
                return lowestVal;
            } else if (val > highestVal) {
                return highestVal;
            } else {
                return val;
            }
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

    public static boolean getBooleanAttribute(Element element, String attrName, boolean defaultVal) {
        String boolString = element.attributeValue(attrName);
        if ("true".equalsIgnoreCase(boolString)) return true;
        else if ("false".equalsIgnoreCase(boolString)) return false;
        else return defaultVal;
    }

    public static String getSubElementContent(Element element, String subElementName, String defaultVal) {
        Element subElement = element.element(subElementName);
        if (subElement != null && subElement.getText() != null) {
            return subElement.getText();
        } else {
            return defaultVal;
        }
    }

    public static <T> List<T> getSubObjects(Element element, XMLObjectAdapter<T> adapter) {
        List<Element> subElements = element.elements(adapter.getElementName());
        List<T> subObjects = Lists.newArrayListWithCapacity(subElements.size());
        for (Element subElement : subElements) {
            subObjects.add(adapter.toObject(subElement));
        }
        return subObjects;
    }
}
