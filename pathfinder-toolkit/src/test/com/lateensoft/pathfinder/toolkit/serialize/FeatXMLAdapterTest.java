package com.lateensoft.pathfinder.toolkit.serialize;

import com.lateensoft.pathfinder.toolkit.model.character.Feat;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.InvalidObjectException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(JUnit4.class)
public class FeatXMLAdapterTest {

    private static final FeatXMLAdapter m_adapter = new FeatXMLAdapter();

    @Test
    public void testConversion() throws InvalidObjectException, DocumentException {
        Feat expectedFeat = new Feat();
        expectedFeat.setName("<feat1>");
        expectedFeat.setDescription("description and \nStuff");
        Element featElement = m_adapter.toXML(expectedFeat);

        Feat generatedSpell = m_adapter.toObject(featElement);
        assertEquals(expectedFeat, generatedSpell);
    }

    @Test
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
}
