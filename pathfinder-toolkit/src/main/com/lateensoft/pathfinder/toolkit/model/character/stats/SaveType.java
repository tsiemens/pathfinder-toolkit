package com.lateensoft.pathfinder.toolkit.model.character.stats;

import android.content.res.Resources;
import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.R;

import java.util.Collections;
import java.util.List;

public enum SaveType {
    FORT(1, AbilityType.CON, R.string.save_name_fort),
    REF(2, AbilityType.DEX, R.string.save_name_ref),
    WILL(3, AbilityType.WIS, R.string.save_name_will);

    private final int key;
    private final AbilityType defaultAbility;
    private final int nameResId;

    SaveType(int key, AbilityType defaultAbility, int nameResId) {
        this.key = key;
        this.defaultAbility = defaultAbility;
        this.nameResId = nameResId;
    }

    public int getKey() {
        return key;
    }

    public AbilityType getDefaultAbility() {
        return defaultAbility;
    }

    public int getNameResId() {
        return nameResId;
    }

    public static List<SaveType> getKeySortedValues() {
        List<SaveType> types = Lists.newArrayList(SaveType.values());
        Collections.sort(types);
        return types;
    }

    public static String[] getKeySortedNames(Resources r) {
        List<SaveType> types = getKeySortedValues();
        String[] names = new String[types.size()];
        for (int i = 0; i < types.size(); i++) {
            SaveType type = types.get(i);
            names[i] = r.getString(type.getNameResId());
        }
        return names;
    }

    public static SaveType forKey(int key) {
        for (SaveType type : SaveType.values()) {
            if (type.getKey() == key) {
                return type;
            }
        }
        throw new IllegalArgumentException(Integer.toString(key) +
                " is not a valid " + SaveType.class.getSimpleName());
    }
}
