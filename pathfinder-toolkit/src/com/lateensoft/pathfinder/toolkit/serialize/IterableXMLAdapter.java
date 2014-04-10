package com.lateensoft.pathfinder.toolkit.serialize;

import org.dom4j.Element;

import java.io.InvalidObjectException;
import java.util.List;

/**
 * @author tsiemens
 */
public abstract class IterableXMLAdapter<I extends Iterable<E>, E, A extends XMLObjectAdapter<E>> extends XMLObjectAdapter<I> {

    public abstract A getItemAdapter();
    protected abstract I createObjectFromItems(List<E> items);

    @Override
    protected I createObjectForElement(Element element) throws InvalidObjectException {
        List<E> items = getSubObjects(element, getItemAdapter());
        return createObjectFromItems(items);
    }

    @Override
    protected void setElementContentForObject(Element element, I iterable) {
        A adapter = getItemAdapter();
        for (E item : iterable) {
            element.add(adapter.toXML(item));
        }
    }
}