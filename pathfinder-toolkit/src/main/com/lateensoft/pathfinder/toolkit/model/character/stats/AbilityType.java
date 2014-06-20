package com.lateensoft.pathfinder.toolkit.model.character.stats;

import android.content.res.Resources;
import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.R;

import java.util.Collections;
import java.util.List;

public enum AbilityType {
    STR(1, R.string.ability_name_str),
    DEX(2, R.string.ability_name_dex),
    CON(3, R.string.ability_name_con),
    INT(4, R.string.ability_name_int),
    WIS(5, R.string.ability_name_wis),
    CHA(6, R.string.ability_name_cha);

    private final int key;
    private final int nameResId;

    AbilityType(int key, int nameResId) {
        this.key = key;
        this.nameResId = nameResId;
    }

    public int getKey() {
        return key;
    }

    public int getNameResId() {
        return nameResId;
    }

    public static List<AbilityType> getKeySortedValues() {
        List<AbilityType> types = Lists.newArrayList(AbilityType.values());
        Collections.sort(types);
        return types;
    }

    public static String[] getKeySortedNames(Resources r) {
        List<AbilityType> types = getKeySortedValues();
        String[] names = new String[types.size()];
        for (int i = 0; i < types.size(); i++) {
            AbilityType type = types.get(i);
            names[i] = r.getString(type.getNameResId());
        }
        return names;
    }

    public static AbilityType forKey(int key) {
        for (AbilityType type : AbilityType.values()) {
            if (type.getKey() == key) {
                return type;
            }
        }
        throw new IllegalArgumentException(Integer.toString(key) +
                " is not a valid " + AbilityType.class.getSimpleName());
    }
}
