package com.lateensoft.pathfinder.toolkit.pref;

import static com.lateensoft.pathfinder.toolkit.pref.Preference.*;

public class GlobalPrefs {

    public static final LongPreference LAST_RATE_PROMPT_TIME = new LongPreference("lastRateTime");
    public static final IntPreference LAST_RATED_VERSION = new IntPreference("lastRateVersion");
    public static final IntPreference LAST_USED_VERSION = new IntPreference("lastUsedVersion");
    public static final LongPreference SELECTED_CHARACTER_ID = new LongPreference("selectedCharacter");
    public static final LongPreference SELECTED_PARTY_ID = new LongPreference("selectedParty");
    public static final LongPreference SELECTED_ENCOUNTER_ID = new LongPreference("selectedEncounter");
}
