package com.lateensoft.pathfinder.toolkit.serialize;

import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.model.character.PathfinderCharacter;
import com.lateensoft.pathfinder.toolkit.util.CharacterUtils;
import com.lateensoft.pathfinder.toolkit.util.DOMUtils;
import com.lateensoft.pathfinder.toolkit.util.ImportExportUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.*;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class XMLExportRootAdapterTest {

    private static final XMLExportRootAdapter adapter = new XMLExportRootAdapter();

    private static final ClassLoader classLoader = XMLExportRootAdapterTest.class.getClassLoader();
    private static final String XML_DIR_PATH = "com/lateensoft/pathfinder/toolkit/serialize/xml/";

    @Test
    public void toObjectFromV1() throws DocumentException, InvalidObjectException {
        InputStream is = classLoader.getResourceAsStream(XML_DIR_PATH + "v1_characters.xml");
        Document v1Doc = DOMUtils.newDocument(is);

        List<PathfinderCharacter> characters = adapter.toObject(v1Doc.getRootElement());
        assertEquals("Character name", characters.get(0).getName());
        assertEquals("New Adventurer", characters.get(1).getName());
        assertEquals("New 3", characters.get(2).getName());
    }

    @Test
    public void testImportExport() throws IOException, DocumentException {
        PathfinderCharacter fullCharacter = CharacterUtils.buildTestCharacter();
        PathfinderCharacter defaultCharacter = PathfinderCharacter.newDefaultCharacter("");

        Element element = adapter.toXML(Lists.newArrayList(fullCharacter, defaultCharacter));

        List<PathfinderCharacter> characters = adapter.toObject(element);

        assertEquals(2, characters.size());
        assertEquals(fullCharacter, characters.get(0));
        assertEquals(defaultCharacter, characters.get(1));
    }
}
