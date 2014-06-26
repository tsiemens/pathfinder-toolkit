package com.lateensoft.pathfinder.toolkit.pref;

public interface Preferences {
    public <T> boolean put(Preference<T> pref, T value);
    public <T> T get(Preference<T> pref, T defaultVal);

    public boolean putInt(String key, int value);
    public int getInt(String key, int defValue);

    public boolean putLong(String key, long value);
    public long getLong(String key, long defValue);

    public boolean putBoolean(String key, boolean value);
    public boolean getBoolean(String key, boolean defValue);

    public boolean putString(String key, String value);
    public String getString(String key, String defValue);

    public void remove(Preference pref);
    public void remove(String key);
}
