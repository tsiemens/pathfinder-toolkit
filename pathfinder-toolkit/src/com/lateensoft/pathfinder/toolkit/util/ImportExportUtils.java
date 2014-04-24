package com.lateensoft.pathfinder.toolkit.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;
import com.google.common.base.CharMatcher;
import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.model.character.PathfinderCharacter;
import com.lateensoft.pathfinder.toolkit.serialize.XMLExportRootAdapter;
import org.dom4j.Document;
import org.dom4j.tree.DefaultDocument;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @author trevsiemens
 */
public class ImportExportUtils {
    public static final File EXTERNAL_EXPORT_DIR = new File(Environment.getExternalStorageDirectory(), "PathfinderToolkit/export");
    public static final File TEMP_EXPORT_DIR = new File(Environment.getExternalStorageDirectory(), "PathfinderToolkit/tmp");

    public static File createExportFileForName(String name, boolean isFileTemp) {
        name = toFileNameSafeString(name);
        File exportDir;
        if(isFileTemp) {
            clearTempDir();
            if (!TEMP_EXPORT_DIR.exists()) {
                TEMP_EXPORT_DIR.mkdirs();
            }
            exportDir = TEMP_EXPORT_DIR;
        } else {
            if (!EXTERNAL_EXPORT_DIR.exists()) {
                EXTERNAL_EXPORT_DIR.mkdirs();
            }
            exportDir = EXTERNAL_EXPORT_DIR;
        }
        return FileUtils.getNextAvailableFileForBase(new File(exportDir, name + ".xml"));
    }

    public static void clearTempDir() {
        if (TEMP_EXPORT_DIR.exists()) {
            File[] files = TEMP_EXPORT_DIR.listFiles();
            if (files != null) {
                for (File file : files) {
                    file.delete();
                }
            }
            TEMP_EXPORT_DIR.delete();
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

    public static void importCharacterFromContent(ActivityLauncher activityLauncher, int requestCode) {
        Intent getContentIntent = new Intent();
        getContentIntent.setAction(Intent.ACTION_GET_CONTENT);
        getContentIntent.setType(FileUtils.XML_MIME);
        activityLauncher.startActivityForResult(getContentIntent, requestCode);
    }

    public static void exportCharacterToStream(PathfinderCharacter character, OutputStream outputStream) throws IOException {
        Document doc = new DefaultDocument(new XMLExportRootAdapter().toXML(character));
        DOMUtils.write(doc, outputStream);
    }

    public static void exportCharactersToStream(List<PathfinderCharacter> characters, OutputStream outputStream) throws IOException {
        Document doc = new DefaultDocument(new XMLExportRootAdapter().toXML(characters));
        DOMUtils.write(doc, outputStream);
    }

    public static File exportCharacterToFile(@NotNull PathfinderCharacter character, boolean isFileTemp) throws IOException {
        File file = ImportExportUtils.createExportFileForName(character.getName(), isFileTemp);
        FileOutputStream fos = new FileOutputStream(file);
        ImportExportUtils.exportCharacterToStream(character, fos);
        fos.close();
        return file;
    }

    public static File exportCharactersToFile(@NotNull List<PathfinderCharacter> characters, String exportName, boolean isFileTemp) throws IOException {
        File file = ImportExportUtils.createExportFileForName(exportName, isFileTemp);
        FileOutputStream fos = new FileOutputStream(file);
        ImportExportUtils.exportCharactersToStream(characters, fos);
        fos.close();
        return file;
    }

    public static void exportCharacterWithDialog(Context context, PathfinderCharacter character, ActivityLauncher activityLauncher) {
        ExportCharactersDialogListener listener = new ExportCharactersDialogListener(context, character, activityLauncher);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Select Export Location");
        builder.setMessage("Please select a location to export the character to.");
        builder.setPositiveButton("Default", listener);
        builder.setNegativeButton("Share", listener);
        builder.show();
    }

    public static void exportCharactersWithDialog(Context context, List<PathfinderCharacter> character, String exportName, ActivityLauncher activityLauncher) {
        ExportCharactersDialogListener listener = new ExportCharactersDialogListener(context, character, exportName, activityLauncher);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Select Export Location");
        builder.setMessage("Please select a location to export " + exportName + " to.");
        builder.setPositiveButton("Default", listener);
        builder.setNegativeButton("Share", listener);
        builder.show();
    }

    private static class ExportCharactersDialogListener implements AlertDialog.OnClickListener {
        private Context m_context;
        private  List<PathfinderCharacter> m_characters;
        private ActivityLauncher m_activityLauncher;
        private String m_exportName;

        public ExportCharactersDialogListener(Context context, List<PathfinderCharacter> characters, String exportName, ActivityLauncher activityLauncher) {
            m_context = context;
            m_characters = characters;
            m_activityLauncher = activityLauncher;
            m_exportName = exportName;
        }

        public ExportCharactersDialogListener(Context context, PathfinderCharacter character, ActivityLauncher activityLauncher) {
            this(context, Lists.newArrayList(character), character.getName(), activityLauncher);
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (which == AlertDialog.BUTTON_POSITIVE) {
                try {
                    File exportFile = exportCharactersToFile(m_characters, m_exportName, true);
                    Toast.makeText(m_context, "Character exported to " + exportFile.getPath(), Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(m_context, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else if (which == AlertDialog.BUTTON_NEGATIVE) {
                try {
                    File exportFile = exportCharactersToFile(m_characters, m_exportName, false);
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(exportFile));
                    shareIntent.setType(FileUtils.XML_MIME);
                    m_activityLauncher.startActivity(Intent.createChooser(shareIntent, "Export to"));
                } catch (IOException e) {
                    Toast.makeText(m_context, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
