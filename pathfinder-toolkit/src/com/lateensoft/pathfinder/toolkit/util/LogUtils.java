package com.lateensoft.pathfinder.toolkit.util;

import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author tsiemens
 */
public class LogUtils {

    public static String getStacktrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }

    public static void logStackTraceError(String tag, Throwable throwable) {
        Log.e(tag, getStacktrace(throwable));
    }
}
