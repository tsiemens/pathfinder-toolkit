package com.lateensoft.pathfinder.toolkit.util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author tsiemens
 */
public class DOMUtils {

    private static final String INDENT = "    ";

    public static Document newDocument(InputStream inputStream) throws DocumentException {
        SAXReader saxReader = new SAXReader();
        Document saxDoc = saxReader.read(inputStream);

        DOMWriter domWriter = new DOMWriter();
        org.w3c.dom.Document domObject = domWriter.write(saxDoc);

        DOMReader domReader = new DOMReader();
        return domReader.read(domObject);
    }

    public static void write(Document document, OutputStream outputStream) throws IOException {
        OutputFormat outputFormat = new OutputFormat();
        outputFormat.setIndent(INDENT);
        outputFormat.setNewlines(true);
        XMLWriter xmlWriter = new XMLWriter(outputStream, outputFormat);
        xmlWriter.setEscapeText(true);
        xmlWriter.write(document);
        outputStream.flush();
    }
}
