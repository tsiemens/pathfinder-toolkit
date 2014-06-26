package com.lateensoft.pathfinder.toolkit.model.character.items;

import android.content.res.Resources;
import com.google.common.base.Preconditions;
import com.lateensoft.pathfinder.toolkit.R;

public enum Size {
    SMALL("S", R.string.item_size_small),
    MEDIUM("M", R.string.item_size_med),
    LARGE("L", R.string.item_size_large);

    private String key;
    private int displayNameResource;

    Size(String key, int displayStringResId) {
        this.key = key;
        this.displayNameResource = displayStringResId;
    }

    public String getKey() {
        return key;
    }

    public int getDisplayNameResId() {
        return displayNameResource;
    }

    public int getValuesIndex() {
        Size[] sizes = values();
        for(int i = 0; i < sizes.length; i++) {
            if(this == sizes[i])
                return i;
        }
        throw new IllegalStateException("Invalid size");
    }

    public static String[] getValuesSortedNames(Resources r) {
        Size[] sizes = values();
        String[] names = new String[sizes.length];
        for (int i = 0; i < sizes.length; i++) {
            Size type = sizes[i];
            names[i] = r.getString(type.getDisplayNameResId());
        }
        return names;
    }

    public static Size forValuesIndex(int index) {
        return values()[index];
    }

    public static Size forKey(String key) {
        for (Size size : Size.values()) {
            if (size.getKey().equals(key)) {
                return size;
            }
        }
        throw new IllegalArgumentException(key +
                " is not a valid " + Size.class.getSimpleName());
    }
}
