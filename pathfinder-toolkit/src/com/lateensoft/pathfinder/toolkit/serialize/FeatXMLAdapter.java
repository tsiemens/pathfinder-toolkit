package com.lateensoft.pathfinder.toolkit.serialize;

import com.lateensoft.pathfinder.toolkit.model.character.PTFeat;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

import java.io.InvalidObjectException;

/**
 * @author tsiemens
 */
public class FeatXMLAdapter extends XMLObjectAdapter<PTFeat> {

    public static final String ELEMENT_NAME = "feat";
    private static final String NAME_ATTR = "name";
    private static final String DESC_ELEMENT = "desc";


    @Override
    public String getElementName() {
        return ELEMENT_NAME;
    }

    @Override
    protected void setContentForObject(Element element, PTFeat feat) {
        element.addAttribute(NAME_ATTR, feat.getName());

        Element description = new DefaultElement(DESC_ELEMENT);
        description.setText(feat.getDescription());
        element.add(description);
    }

    @Override
    protected PTFeat convertToObject(Element element) throws InvalidObjectException {
        PTFeat feat = new PTFeat();
        feat.setName(getStringAttribute(element, NAME_ATTR));
        feat.setDescription(getSubElementContent(element, DESC_ELEMENT));
        return feat;
    }


}
