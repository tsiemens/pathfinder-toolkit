package com.lateensoft.pathfinder.toolkit.test.serialize;

import com.lateensoft.pathfinder.toolkit.model.character.PTSpell;
import com.lateensoft.pathfinder.toolkit.serialize.SpellXMLAdapter;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * @author trevsiemens
 */
public class SpellXMLAdapterTest extends AbstractXMLAdapterTestCase {

    private static final SpellXMLAdapter m_adapter = new SpellXMLAdapter();

    public void testToObject() throws DocumentException {
        Element element = DocumentHelper.parseText("<spell name=\"fire\" level=\"3\" prepared=\"20\">" +
                "<desc>description and \nStuff</desc></spell>").getRootElement();
        PTSpell expectedSpell = new PTSpell("fire", 3);
        expectedSpell.setPrepared(20);
        expectedSpell.setDescription("description and \nStuff");

        PTSpell generatedSpell = m_adapter.toObject(element);
        assertEquals(expectedSpell, generatedSpell);
    }

    public void testToObjectInvalid() throws DocumentException {
        Element invalidElement1 = DocumentHelper.parseText("<spell >" +
                "</spell>").getRootElement();
        PTSpell expectedSpell1 = new PTSpell("", 0);
        expectedSpell1.setPrepared(0);
        expectedSpell1.setDescription("");

        PTSpell generatedSpell1 = m_adapter.toObject(invalidElement1);
        assertEquals(expectedSpell1, generatedSpell1);

        Element invalidElement2 = DocumentHelper.parseText("<spell name=\"fire\" level=\"1.5\" prepared=\"abc\">" +
                "<desc></desc></spell>").getRootElement();
        PTSpell expectedSpell2 = new PTSpell("fire", 0);
        expectedSpell2.setPrepared(0);
        expectedSpell2.setDescription("");

        PTSpell generatedSpell2 = m_adapter.toObject(invalidElement2);
        assertEquals(expectedSpell2, generatedSpell2);

        Element invalidElement3 = DocumentHelper.parseText("<spell name=\"fire\" level=\"20\" prepared=\"-1\">" +
                "<desc></desc></spell>").getRootElement();
        PTSpell expectedSpell3 = new PTSpell("fire", 10);
        expectedSpell3.setPrepared(0);
        expectedSpell3.setDescription("");

        PTSpell generatedSpell3 = m_adapter.toObject(invalidElement3);
        assertEquals(expectedSpell3, generatedSpell3);
    }

    public void testToXML() throws DocumentException {
        Element expectedElement = DocumentHelper.parseText("<spell name=\"fire\" level=\"3\" prepared=\"20\">" +
                "<desc>description and \nStuff</desc></spell>").getRootElement();
        PTSpell spell = new PTSpell("fire", 3);
        spell.setPrepared(20);
        spell.setDescription("description and \nStuff");

        Element generatedElement = m_adapter.toXML(spell);
        assertEquals(expectedElement, generatedElement);
    }

    private void assertEquals(PTSpell expected, PTSpell actual) {
        assertNotNull(actual);
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getLevel(), actual.getLevel());
        assertEquals(expected.getPrepared(), actual.getPrepared());
        assertEquals(expected.getDescription(), actual.getDescription());
    }
}
