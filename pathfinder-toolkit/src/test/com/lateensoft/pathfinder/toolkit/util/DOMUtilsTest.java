package com.lateensoft.pathfinder.toolkit.util;

import com.google.common.base.Charsets;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.ByteArrayInputStream;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class DOMUtilsTest {

    @Test
    public void testNewDocument() throws DocumentException {
        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<resources attr1=\"sdfklj\" attr2=\"sdf\">" +
                "<string name=\"app_name\">Pathfinder Toolkit Test</string>" +
                "</resources>";
        Document document = DOMUtils.newDocument(new ByteArrayInputStream(xml.getBytes(Charsets.UTF_8)));

        assertThat(document.asXML().toLowerCase(), equalTo(xml.toLowerCase()));
    }
}
