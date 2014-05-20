package com.lateensoft.pathfinder.toolkit.test.serialize;

import android.test.AndroidTestCase;
import com.lateensoft.pathfinder.toolkit.model.character.Spell;
import com.lateensoft.pathfinder.toolkit.serialize.SpellXMLAdapter;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.InvalidObjectException;

/**
 * @author trevsiemens
 */
public class SpellXMLAdapterTest extends AndroidTestCase {

    private static final SpellXMLAdapter m_adapter = new SpellXMLAdapter();

    public void tesConvert() throws InvalidObjectException, DocumentException {
        Spell expectedSpell = new Spell("fire", 3);
        expectedSpell.setPrepared(20);
        expectedSpell.setDescription("description and \nStuff");
        Element spellElement = m_adapter.toXML(expectedSpell);

        Spell generatedSpell = m_adapter.toObject(spellElement);
        assertEquals(expectedSpell, generatedSpell);
    }

    public void testToObjectInvalid() throws InvalidObjectException, DocumentException {
        try {
            Element invalidElement1 = DocumentHelper.parseText("<spell >" +
                    "</spell>").getRootElement();
            m_adapter.toObject(invalidElement1);
            fail();
        } catch (InvalidObjectException e) {} catch (DocumentException e) {
            e.printStackTrace();
        }

        try {
            Element invalidElement2 = DocumentHelper.parseText("<spell name=\"fire\" level=\"1.5\" prepared=\"abc\">" +
                    "<desc></desc></spell>").getRootElement();
            m_adapter.toObject(invalidElement2);
            fail();
        } catch (InvalidObjectException e) {}

        try {
            Element invalidElement3 = DocumentHelper.parseText("<spell name=\"fire\" level=\"20\" prepared=\"-1\">" +
                    "<desc></desc></spell>").getRootElement();
            m_adapter.toObject(invalidElement3);
            fail();
        } catch (InvalidObjectException e) {}

        try {
            Element invalidElement4 = DocumentHelper.parseText("<spell name=\"fire\" level=\"3.0\" prepared=\"20.0\">" +
                    "<desc>description and \nStuff</desc></spell>").getRootElement();
            m_adapter.toObject(invalidElement4);
        } catch (InvalidObjectException e) {}
    }

    private void assertEquals(Spell expected, Spell actual) {
        assertNotNull(actual);
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getLevel(), actual.getLevel());
        assertEquals(expected.getPrepared(), actual.getPrepared());
        assertEquals(expected.getDescription(), actual.getDescription());
    }
}
