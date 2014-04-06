package com.lateensoft.pathfinder.toolkit.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * @author tsiemens
 */
public class FileUtils {

    public static final String XML_MIME = "text/xml";

    public static @Nullable Intent getNewFileActivityIntent(String mimeType) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
            intent.setType(mimeType);
            return intent;
        } else {
            return null;
        }
    }

    public static boolean canUseNewFileActivity() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    /**
     * Gets the next file with the fileBase's base name which does not exist.
     * Eg. .../text.xml -> .../text(1).xml if that file already exists.
     */
    public static File getNextAvailableFileForBase(File fileBase) {
        if (!fileBase.exists()) {
            return fileBase;
        } else {
            File newFile;
            String baseName = fileBase.getName().replaceAll("\\..*$", "");
            String extension = fileBase.getName().replaceAll("^[^.]*", "");
            int i = 1;
            do {
                newFile = new File(fileBase.getParent(), baseName + "(" + i + ")" + extension);
                i++;
            } while (newFile.exists());
            return newFile;
        }
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}
