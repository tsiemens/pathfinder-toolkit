package com.lateensoft.pathfinder.toolkit.model.character.items;

import android.content.res.Resources;
import com.lateensoft.pathfinder.toolkit.R;

public enum WeaponType {
    SLASHING("S", R.string.weapon_type_slash),
    PIERCING("P", R.string.weapon_type_pierce),
    BLUDGEONING("B", R.string.weapon_type_bludgeon),
    PIERCING_BLUDGEONING("PB", R.string.weapon_type_pierce_bludgeon),
    SLASHING_BLUDGEONING("SB", R.string.weapon_type_slash_bludgeon),
    SLASHING_PIERCING("SP", R.string.weapon_type_slash_pierce),
    SLASHING_PIERCING_BLUDGEONING("SPB", R.string.weapon_type_slash_pierce_bludgeon);

    private String key;
    private int displayNameResource;

    WeaponType(String key, int displayStringResId) {
        this.key = key;
        this.displayNameResource = displayStringResId;
    }

    public String getKey() {
        return key;
    }

    public int getDisplayNameResId() {
        return displayNameResource;
    }

    public static String[] getValuesSortedNames(Resources r) {
        WeaponType[] types = values();
        String[] names = new String[types.length];
        for (int i = 0; i < types.length; i++) {
            WeaponType type = types[i];
            names[i] = r.getString(type.getDisplayNameResId());
        }
        return names;
    }

    public static WeaponType forKey(String key) {
        for (WeaponType types : WeaponType.values()) {
            if (types.getKey().equals(key)) {
                return types;
            }
        }
        throw new IllegalArgumentException(key +
                " is not a valid " + WeaponType.class.getSimpleName());
    }
}
