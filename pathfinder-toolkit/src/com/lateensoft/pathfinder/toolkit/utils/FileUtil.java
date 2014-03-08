package com.lateensoft.pathfinder.toolkit.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

/**
 * Created by trevsiemens on 04/03/14.
 */
public class FileUtil {

    public static final String XML_MIME = "text/xml";

    public static final int NEW_FILE_REQ_CODE = 309485039;

    /**
     *
     * @param activity
     * @param mimeType
     * @return The request code for activity started. -1 if the activity failed to start.
     */
    public static int startNewFileActivityForResult(Activity activity, String mimeType) {
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
            intent.setType(mimeType);
            activity.startActivityForResult(intent, NEW_FILE_REQ_CODE);
           return NEW_FILE_REQ_CODE;
        }
        return -1;
    }
}
