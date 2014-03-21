package com.lateensoft.pathfinder.toolkit.serialize;

import org.dom4j.Element;

import java.io.InvalidObjectException;
import java.util.List;

/**
 * @author tsiemens
 */
public abstract class ListXMLAdapter<L extends List<E>, E, A extends XMLObjectAdapter<E>> extends XMLObjectAdapter<L> {

    public abstract A getItemAdapter();
    protected abstract L createObjectFromItems(List<E> items);

    @Override
    protected L createObjectForElement(Element element) throws InvalidObjectException {
        List<E> items = getSubObjects(element, getItemAdapter());
        return createObjectFromItems(items);
    }

    @Override
    protected void setElementContentForObject(Element element, L list) {
        A adapter = getItemAdapter();
        for (E item : list) {
            element.add(adapter.toXML(item));
        }
    }
}
