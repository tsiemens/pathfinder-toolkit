package com.lateensoft.pathfinder.toolkit.serialize;

import org.dom4j.Element;

import java.io.InvalidObjectException;
import java.util.List;

/**
 * @author tsiemens
 */
public abstract class ListXMLAdapter<L extends List<E>, E, A extends XMLObjectAdapter<E>> extends XMLObjectAdapter<L> {

    public abstract A getItemAdapter();
    protected abstract L createFromItems(List<E> items);

    @Override
    protected L convertToObject(Element element) throws InvalidObjectException {
        List<E> items = getSubObjects(element, getItemAdapter());
        return createFromItems(items);
    }

    @Override
    protected void setContentForObject(Element element, L list) {
        A adapter = getItemAdapter();
        for (E item : list) {
            element.add(adapter.toXML(item));
        }
    }
}
