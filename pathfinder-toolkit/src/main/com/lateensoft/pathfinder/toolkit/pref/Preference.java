package com.lateensoft.pathfinder.toolkit.pref;

public class Preference<T> {

    private final String key;
    private final Class<T> type;

    private Preference(String key, Class<T> type) {
        this.key = key;
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public Class<T> getType() {
        return type;
    }

    public static class BoolPreference extends Preference<Boolean> {
        public BoolPreference(String key) {
            super(key, Boolean.class);
        }
    }

    public static class IntPreference extends Preference<Integer> {
        public IntPreference(String key) {
            super(key, Integer.class);
        }
    }

    public static class LongPreference extends Preference<Long> {
        public LongPreference(String key) {
            super(key, Long.class);
        }
    }

    public static class StringPreference extends Preference<String> {
        public StringPreference(String key) {
            super(key, String.class);
        }
    }
}
