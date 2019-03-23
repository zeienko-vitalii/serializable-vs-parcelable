package com.zeenko.serializablevsparcelable.utility;

import android.util.Log;

public class Logger {
    public static void logD(String tag, String msg) {
        if (tag == null) {
            Log.d("[duck]", msg);
        } else {
            Log.d(tag, msg);
        }
    }
}
