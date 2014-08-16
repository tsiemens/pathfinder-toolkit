package com.lateensoft.pathfinder.toolkit.patching;

import android.content.Context;
import org.jetbrains.annotations.Nullable;

public abstract class Patch {

    protected final Context context;

    public Patch(Context context) {
        this.context = context;
    }

    /** @return true if the patch encountered no errors */
    public abstract boolean apply();

    public abstract @Nullable Patch getNext();
}
