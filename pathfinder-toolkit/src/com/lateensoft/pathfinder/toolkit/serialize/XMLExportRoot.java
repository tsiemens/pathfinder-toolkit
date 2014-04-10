package com.lateensoft.pathfinder.toolkit.serialize;

import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.model.character.PathfinderCharacter;
import org.dom4j.Element;

import java.io.InvalidObjectException;
import java.util.List;

/**
 * @author trevsiemens
 */
public class XMLExportRoot extends XMLObjectAdapter<List<PathfinderCharacter>>{
    private static final int VERSION = 1;

    public static final String ELEMENT_NAME = "ptx";
    public static final String VERSION_ATTR = "version";

    private CharacterXMLAdapter m_characterXMLAdapter = new CharacterXMLAdapter();

    @Override
    public String getElementName() {
        return ELEMENT_NAME;
    }

    @Override
    protected List<PathfinderCharacter> createObjectForElement(Element element) throws InvalidObjectException {
        updateToCurrentVersion(element);
        return getSubObjects(element, m_characterXMLAdapter);
    }

    @Override
    protected void setElementContentForObject(Element element, List<PathfinderCharacter> characters) {
        element.addAttribute(VERSION_ATTR, Integer.toString(VERSION));
        for (PathfinderCharacter character : characters) {
            element.add(m_characterXMLAdapter.toXML(character));
        }
    }

    public Element toXML(PathfinderCharacter character) {
        return toXML(Lists.newArrayList(character));
    }

    private void updateToCurrentVersion(Element element) {
        // No out of date versions yet
    }
}
