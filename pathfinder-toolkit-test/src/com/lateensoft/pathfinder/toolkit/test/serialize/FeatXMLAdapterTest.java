package com.lateensoft.pathfinder.toolkit.test.serialize;

import android.test.AndroidTestCase;
import com.lateensoft.pathfinder.toolkit.model.character.Feat;
import com.lateensoft.pathfinder.toolkit.serialize.FeatXMLAdapter;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.InvalidObjectException;

/**
 * @author trevsiemens
 */
public class FeatXMLAdapterTest extends AndroidTestCase {

    private static final FeatXMLAdapter m_adapter = new FeatXMLAdapter();

    public void testConversion() throws InvalidObjectException, DocumentException {
        Feat expectedFeat = new Feat();
        expectedFeat.setName("<feat1>");
        expectedFeat.setDescription("description and \nStuff");
        Element featElement = m_adapter.toXML(expectedFeat);

        Feat generatedSpell = m_adapter.toObject(featElement);
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

    private void assertEquals(Feat expected, Feat actual) {
        assertNotNull(actual);
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());
    }
}
