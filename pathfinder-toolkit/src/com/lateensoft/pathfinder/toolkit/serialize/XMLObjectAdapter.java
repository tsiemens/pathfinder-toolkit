package com.lateensoft.pathfinder.toolkit.serialize;

import com.google.common.collect.Lists;
import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

import java.io.InvalidObjectException;
import java.util.List;

/**
 * @author trevsiemens
 */
public abstract class XMLObjectAdapter<E> {

    public abstract String getElementName();

    protected abstract E createObjectForElement(Element element) throws InvalidObjectException;
    protected abstract void setElementContentForObject(Element element, E object);

    public Element toXML(E object) {
        Element element = new DefaultElement(getElementName());
        setElementContentForObject(element, object);
        return element;
    }

    public E toObject(Element element) throws InvalidObjectException {
        if (element.getName().equals(getElementName())) {
            return createObjectForElement(element);
        } else {
            throw new IllegalArgumentException("Element type " + element.getName() +
                    " does not match " + getElementName());
        }
    }

    public static int getIntAttribute(Element element, String attrName) throws InvalidObjectException {
        return getBoundedIntAttribute(element, attrName, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public static int getBoundedIntAttribute(Element element, String attrName, int lowestVal, int highestVal) throws InvalidObjectException {
        try {
            String intString = element.attributeValue(attrName);
            if (intString == null) {
                throw new InvalidObjectException(String.format("Missing integer attribute '%s' in '%s'", attrName, element.getName()));
            }
            int val = Integer.parseInt(intString);
            if (val < lowestVal || val > highestVal) {
                throw new InvalidObjectException(String.format("'%s' in '%s' must be between %d and %d",
                        attrName, element.getName(), lowestVal, highestVal));
            } else {
                return val;
            }
        } catch (NumberFormatException e) {
            throw new InvalidObjectException(String.format("Malformed integer attribute '%s' in '%s'", attrName, element.getName()));
        }
    }

    public static double getDoubleAttribute(Element element, String attrName) throws InvalidObjectException {
        return getBoundedDoubleAttribute(element, attrName, Double.MIN_VALUE, Double.MAX_VALUE);
    }

    public static double getBoundedDoubleAttribute(Element element, String attrName, double lowestVal, double highestVal) throws InvalidObjectException {
        try {
            String doubleString = element.attributeValue(attrName);
            if (doubleString == null) {
                throw new InvalidObjectException(String.format("Missing decimal attribute '%s' in '%s'", attrName, element.getName()));
            }
            double val = Double.parseDouble(doubleString);
            if (val < lowestVal || val > highestVal) {
                throw new InvalidObjectException(String.format("'%s' in '%s' must be between %f and %f",
                        attrName, element.getName(), lowestVal, highestVal));
            } else {
                return val;
            }
        } catch (NumberFormatException e) {
            throw new InvalidObjectException(String.format("Malformed decimal attribute '%s' in '%s'", attrName, element.getName()));
        }
    }

    public static long getLongAttribute(Element element, String attrName) throws InvalidObjectException {
        try {
            String longString = element.attributeValue(attrName);
            if (longString == null) {
                throw new InvalidObjectException(String.format("Missing large integer attribute '%s' in '%s'", attrName, element.getName()));
            }
            return Long.parseLong(longString);
        } catch (NumberFormatException e) {
            throw new InvalidObjectException(String.format("Malformed large integer attribute '%s' in '%s'", attrName, element.getName()));
        }
    }

    public static String getStringAttribute(Element element, String attrName) throws InvalidObjectException {
        String val = element.attributeValue(attrName);
        if (val == null) {
            throw new InvalidObjectException(String.format("Missing text attribute '%s' in '%s'", attrName, element.getName()));
        } else {
            return val;
        }
    }

    public static boolean getBooleanAttribute(Element element, String attrName) throws InvalidObjectException {
        String boolString = element.attributeValue(attrName);
        if (boolString == null) {
            throw new InvalidObjectException(String.format("Missing boolean attribute '%s' in '%s'", attrName, element.getName()));
        }
        if ("true".equalsIgnoreCase(boolString)) return true;
        else if ("false".equalsIgnoreCase(boolString)) return false;
        else throw new InvalidObjectException(String.format("Attribute '%s' in '%s' must contain 'true' or 'false'", attrName, element.getName()));
    }

    public static String getSubElementContent(Element element, String subElementName) throws InvalidObjectException {
        Element subElement = element.element(subElementName);
        if (subElement == null) {
            throw new InvalidObjectException(String.format("Required element '%s' not found in '%s'", subElementName, element.getName()));
        }
        return (subElement.getText() != null) ? subElement.getText() : "";
    }

    public static <T> List<T> getSubObjects(Element element, XMLObjectAdapter<T> adapter) throws InvalidObjectException {
        @SuppressWarnings("unchecked")
        List<Element> subElements = element.elements(adapter.getElementName());
        List<T> subObjects = Lists.newArrayListWithCapacity(subElements.size());
        for (Element subElement : subElements) {
            subObjects.add(adapter.toObject(subElement));
        }
        return subObjects;
    }

    public static <T> T getSubObject(Element element, XMLObjectAdapter<T> adapter) throws InvalidObjectException {
        @SuppressWarnings("unchecked")
        Element subElement = element.element(adapter.getElementName());
        if (subElement == null) {
            throw new InvalidObjectException("Required element " + adapter.getElementName() + " not found in " + element.getName());
        }
        return adapter.toObject(element);
    }
}
