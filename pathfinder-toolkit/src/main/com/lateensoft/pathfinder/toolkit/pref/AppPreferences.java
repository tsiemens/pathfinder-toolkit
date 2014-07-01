package com.lateensoft.pathfinder.toolkit.pref;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Preferences for the application, which are stored in SharedPreferences
 */
public class AppPreferences implements Preferences {
    private static final String KEY_APP_SHARED_PREFS_NAME = "ptUserPrefs";
        
    private SharedPreferences sharedPreferences;
    private Editor editor;
        
    public AppPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(
                KEY_APP_SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    @Override
    public <T> boolean put(Preference<T> pref, T value) {
        if (pref.getType() == Boolean.class) {
            return putBoolean(pref.getKey(), (Boolean) value);
        } else if (pref.getType() == Integer.class) {
            return putInt(pref.getKey(), (Integer) value);
        } else if (pref.getType() == Long.class) {
            return putLong(pref.getKey(), (Long) value);
        } else if (pref.getType() == String.class) {
            return putString(pref.getKey(), (String) value);
        } else {
            throw new IllegalArgumentException("Preferences of type " + pref.getType() + " are not supported.");
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(Preference<T> pref, T defaultVal) {
        if (pref.getType() == Boolean.class) {
            return (T) Boolean.valueOf(getBoolean(pref.getKey(), (Boolean) defaultVal));
        } else if (pref.getType() == Integer.class) {
            return (T) Integer.valueOf(getInt(pref.getKey(), (Integer) defaultVal));
        } else if (pref.getType() == Long.class) {
            return (T) Long.valueOf(getLong(pref.getKey(), (Long) defaultVal));
        } else if (pref.getType() == String.class) {
            return (T) getString(pref.getKey(), (String) defaultVal);
        } else {
            throw new IllegalArgumentException("Preferences of type " + pref.getType() + " are not supported.");
        }
    }
    
    @Override
    public boolean putInt(String key, int value) {
        editor.putInt(key, value);
        return editor.commit();
    }
    
    @Override
    public boolean putLong(String key, long value) {
        editor.putLong(key, value);
        return editor.commit();
    }
    
    @Override
    public boolean putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        return editor.commit();
    }
    
    @Override
    public boolean putString(String key, String value) {
        editor.putString(key, value);
        return editor.commit();
    }
    
    @Override
    public int getInt(String key, int defValue) {
        return sharedPreferences.getInt(key, defValue);
    }
    
    @Override
    public long getLong(String key, long defValue) {
        return sharedPreferences.getLong(key, defValue);
    }
    
    @Override
    public boolean getBoolean(String key, boolean defValue) {
        return sharedPreferences.getBoolean(key, defValue);
    }
    
    @Override
    public String getString(String key, String defValue) {
        return sharedPreferences.getString(key, defValue);
    }
    
    @Override
    public void remove(String key) {
        editor.remove(key);
    }

    @Override
    public void remove(Preference pref) {
        remove(pref.getKey());
    }
}
