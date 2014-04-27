package com.lateensoft.pathfinder.toolkit.serialize;

import com.lateensoft.pathfinder.toolkit.model.character.Feat;
import org.dom4j.Element;

import java.io.InvalidObjectException;

/**
 * @author tsiemens
 */
public class FeatXMLAdapter extends XMLObjectAdapter<Feat> {

    public static final String ELEMENT_NAME = "feat";
    private static final String NAME_ELMT = "name";
    private static final String DESC_ELMT = "desc";


    @Override
    public String getElementName() {
        return ELEMENT_NAME;
    }

    @Override
    protected void setElementContentForObject(Element element, Feat feat) {
        addSubElementText(element, NAME_ELMT, feat.getName());
        addSubElementText(element, DESC_ELMT, feat.getDescription());
    }

    @Override
    protected Feat createObjectForElement(Element element) throws InvalidObjectException {
        Feat feat = new Feat();
        feat.setName(getSubElementText(element, NAME_ELMT));
        feat.setDescription(getSubElementText(element, DESC_ELMT));
        return feat;
    }


}
