package com.lateensoft.pathfinder.toolkit.util;

import android.os.Environment;
import com.google.common.base.CharMatcher;
import com.lateensoft.pathfinder.toolkit.BaseApplication;
import com.lateensoft.pathfinder.toolkit.model.character.PathfinderCharacter;
import com.lateensoft.pathfinder.toolkit.serialize.CharacterXMLAdapter;
import org.dom4j.Document;
import org.dom4j.tree.DefaultDocument;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author trevsiemens
 */
public class ImportExportUtils {
    public static final File EXTERNAL_EXPORT_DIR = new File(Environment.getExternalStorageDirectory(), "PathfinderToolkit/export");
    public static final File CACHE_EXPORT_DIR = new File(BaseApplication.getAppContext().getCacheDir(), "export");

    public static File createExportFileForName(String name, boolean isFileExternal) {
        name = toFileNameSafeString(name);
        File exportDir;
        if(isFileExternal) {
            if (!EXTERNAL_EXPORT_DIR.exists()) {
                EXTERNAL_EXPORT_DIR.mkdirs();
            }
            exportDir = EXTERNAL_EXPORT_DIR;
        } else {
            if (!CACHE_EXPORT_DIR.exists()) {
                CACHE_EXPORT_DIR.mkdirs();
            }
            exportDir = CACHE_EXPORT_DIR;
        }
        return FileUtils.getNextAvailableFileForBase(new File(exportDir, name + ".xml"));
    }

    public static void clearExportCache() {
        if (CACHE_EXPORT_DIR.exists()) {
            CACHE_EXPORT_DIR.delete();
        }
    }

    public static String toFileNameSafeString(String s) {
        String safeString = CharMatcher.JAVA_LETTER_OR_DIGIT.or(CharMatcher.anyOf(" ")).retainFrom(s);
        safeString = CharMatcher.WHITESPACE.trimFrom(safeString).replace(' ', '_');
        if (safeString.isEmpty()) {
            safeString = "pathfinder_toolkit_export";
        }
        return safeString;
    }

    public static void exportCharacterToFile(PathfinderCharacter character, OutputStream outputStream) throws IOException {
        Document doc = new DefaultDocument(new CharacterXMLAdapter().toXML(character));
        DOMUtils.write(doc, outputStream);
    }
}
