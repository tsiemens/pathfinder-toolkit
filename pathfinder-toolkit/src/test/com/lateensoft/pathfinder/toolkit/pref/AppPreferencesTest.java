package com.lateensoft.pathfinder.toolkit.pref;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class AppPreferencesTest {

    private static final Preference<Boolean> BOOL_PREF = new Preference.BoolPreference("boolPref");
    private static final Preference<Integer> INT_PREF = new Preference.IntPreference("intPref");
    private static final Preference<Long> LONG_PREF = new Preference.LongPreference("longPref");
    private static final Preference<String> STRING_PREF = new Preference.StringPreference("stringPref");

    private Preferences preferences;

    @Before
    public void setUp() {
        preferences = new AppPreferences(Robolectric.application);
    }

    @Test
    public void booleanPref() {
        preferences.put(BOOL_PREF, true);
        assertTrue(preferences.get(BOOL_PREF, false));
    }

    @Test
    public void booleanPrefDefault() {
        assertTrue(preferences.get(BOOL_PREF, true));
    }

    @Test
    public void intPref() {
        int val = 4;
        preferences.put(INT_PREF, val);
        assertEquals(val, (int)preferences.get(INT_PREF, -1));
    }

    @Test
    public void intPrefDefault() {
        int val = 6;
        assertEquals(val, (int)preferences.get(INT_PREF, val));
    }

    @Test
    public void longPref() {
        long val = 100000000000000L;
        preferences.put(LONG_PREF, val);
        assertEquals(val, (long)preferences.get(LONG_PREF, -1L));
    }

    @Test
    public void longPrefDefault() {
        long val = 300000000000000L;
        assertEquals(val, (long)preferences.get(LONG_PREF, val));
    }

    @Test
    public void stringPref() {
        String val = "A";
        preferences.put(STRING_PREF, val);
        assertEquals(val, preferences.get(STRING_PREF, null));
    }

    @Test
    public void stringPrefDefault() {
        String val = "B";
        assertEquals(val, preferences.get(STRING_PREF, val));
    }
}
