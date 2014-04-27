package com.lateensoft.pathfinder.toolkit.test.util;

import android.test.AndroidTestCase;
import com.google.common.base.Charsets;
import com.lateensoft.pathfinder.toolkit.util.DOMUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;

import java.io.ByteArrayInputStream;

/**
 * @author tsiemens
 */
public class DOMUtilsTest extends AndroidTestCase {

    public void testNewDocument() throws DocumentException {
        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<resources attr1=\"sdfklj\" attr2=\"sdf\">" +
                "<string name=\"app_name\">Pathfinder Toolkit Test</string>" +
                "</resources>";
        Document document = DOMUtils.newDocument(new ByteArrayInputStream(xml.getBytes(Charsets.UTF_8)));

        assertEquals(xml.toLowerCase(), document.asXML().toLowerCase());
    }
}
