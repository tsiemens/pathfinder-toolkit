package com.lateensoft.pathfinder.toolkit.serialize;

import com.lateensoft.pathfinder.toolkit.model.character.Spell;
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
public class SpellXMLAdapterTest {

    private static final SpellXMLAdapter m_adapter = new SpellXMLAdapter();

    @Test
    public void tesConvert() throws InvalidObjectException, DocumentException {
        Spell expectedSpell = new Spell("fire", 3);
        expectedSpell.setPrepared(20);
        expectedSpell.setDescription("description and \nStuff");
        Element spellElement = m_adapter.toXML(expectedSpell);

        Spell generatedSpell = m_adapter.toObject(spellElement);
        assertEquals(expectedSpell, generatedSpell);
    }

    @Test
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
}
