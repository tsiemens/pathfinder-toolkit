package com.lateensoft.pathfinder.toolkit.serialize;

import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.model.character.PathfinderCharacter;
import org.dom4j.Element;

import java.io.InvalidObjectException;
import java.util.List;

/**
 * @author trevsiemens
 */
public class XMLExportRootAdapter extends XMLObjectAdapter<List<PathfinderCharacter>>{
    private static final int VERSION = 2;

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

    private void updateToCurrentVersion(Element element) throws InvalidObjectException {
        int version = getVersion(element);
        if (version == 1) {
            updateFromV1ToV2(element);
        }
    }

    private int getVersion(Element element) throws InvalidObjectException {
        return getIntAttribute(element, VERSION_ATTR);
    }

    private void updateFromV1ToV2(Element element) throws InvalidObjectException {
        List characterElements = element.elements(CharacterXMLAdapter.ELEMENT_NAME);
        for (Object o : characterElements) {
            Element characterElement = (Element) o;
            try {
                String name = getSubElementText(characterElement.element(FluffXMLAdapter.ELEMENT_NAME), "name");
                addSubElementText(characterElement, "name", name);
            } catch (InvalidObjectException e) {
                throw e;
            } catch (Exception e) {
                throw new InvalidObjectException("Could not update from v1 to v2 (" + e.getMessage() + ")");
            }
        }

    }
}
