package com.lateensoft.pathfinder.toolkit.test.serialize;

import com.lateensoft.pathfinder.toolkit.model.character.Feat;
import com.lateensoft.pathfinder.toolkit.serialize.FeatXMLAdapter;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.InvalidObjectException;

/**
 * @author trevsiemens
 */
public class FeatXMLAdapterTest extends AbstractXMLAdapterTestCase {

    private static final FeatXMLAdapter m_adapter = new FeatXMLAdapter();

    public void testToObject() throws InvalidObjectException, DocumentException {
        Element element = DocumentHelper.parseText("<feat name=\"feat1\">" +
                "<desc>description and \nStuff</desc></feat>").getRootElement();
        Feat expectedFeat = new Feat();
        expectedFeat.setName("feat1");
        expectedFeat.setDescription("description and \nStuff");

        Feat generatedSpell = m_adapter.toObject(element);
        assertEquals(expectedFeat, generatedSpell);
    }

    public void testToObjectInvalid() throws InvalidObjectException, DocumentException {

        try {
            Element invalidElement1 = DocumentHelper.parseText("<feat >" +
                    "</feat>").getRootElement();
            m_adapter.toObject(invalidElement1);
            fail();
        } catch (InvalidObjectException e) {}

        try {
            Element invalidElement2 = DocumentHelper.parseText("<feat name=\"feat2\">" +
                    "</feat>").getRootElement();
            m_adapter.toObject(invalidElement2);
            fail();
        } catch (InvalidObjectException e) {}
    }

    public void testToXML() throws InvalidObjectException, DocumentException {
        Element expectedElement = DocumentHelper.parseText("<feat name=\"&lt;feat1&lt;\">" +
                "<desc>description &lt;and&gt; \nStuff</desc></feat>").getRootElement();
        Feat spell = new Feat();
        spell.setName("<feat1>");
        spell.setDescription("description <and> \nStuff");

        Element generatedElement = m_adapter.toXML(spell);
        assertEquals(expectedElement, generatedElement);
    }

    private void assertEquals(Feat expected, Feat actual) {
        assertNotNull(actual);
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());
    }
}
