package com.lateensoft.pathfinder.toolkit.test.util;

import android.test.AndroidTestCase;
import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.model.character.Feat;
import com.lateensoft.pathfinder.toolkit.model.character.FluffInfo;
import com.lateensoft.pathfinder.toolkit.model.character.PathfinderCharacter;
import com.lateensoft.pathfinder.toolkit.model.character.Spell;
import com.lateensoft.pathfinder.toolkit.model.character.items.Armor;
import com.lateensoft.pathfinder.toolkit.model.character.items.Item;
import com.lateensoft.pathfinder.toolkit.model.character.items.Weapon;
import com.lateensoft.pathfinder.toolkit.model.character.stats.*;
import com.lateensoft.pathfinder.toolkit.util.ImportExportUtils;
import org.dom4j.DocumentException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

/**
 * @author trevsiemens
 */
public class ImportExportUtilsTest extends AndroidTestCase {

    public void testImportExport() throws IOException, DocumentException {
        PathfinderCharacter fullCharacter = CharacterUtils.buildTestCharacter();
        PathfinderCharacter defaultCharacter = PathfinderCharacter.newDefaultCharacter("");

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImportExportUtils.exportCharactersToStream(Lists.newArrayList(fullCharacter, defaultCharacter), os);
        String xmlText = new String(os.toByteArray());

        InputStream is = new ByteArrayInputStream(xmlText.getBytes());
        List<PathfinderCharacter> characters =ImportExportUtils.importCharactersFromStream(is);

        assertEquals(2, characters.size());
        assertEquals(fullCharacter, characters.get(0));
        assertEquals(defaultCharacter, characters.get(1));
    }
}
