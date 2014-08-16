package com.lateensoft.pathfinder.toolkit.patching.v2;

import android.content.Context;
import android.util.Log;
import com.lateensoft.pathfinder.toolkit.patching.Patch;
import com.lateensoft.pathfinder.toolkit.patching.v6.PostV5Patch;
import org.jetbrains.annotations.Nullable;

public class PreV5Patch extends Patch {
    private static final String TAG = PreV5Patch.class.getSimpleName();

    public PreV5Patch(Context context) {
        super(context);
    }

    @Override
    public boolean apply() {
        Log.i(TAG, "Applying pre v5 patch...");
        Context appContext = context.getApplicationContext();
        com.lateensoft.pathfinder.toolkit.deprecated.v1.db.PTDatabaseManager oldDBManager = new com.lateensoft.pathfinder.toolkit.deprecated.v1.db.PTDatabaseManager(appContext);
        oldDBManager.performUpdates(appContext);
        Log.i(TAG, "Pre v5 patch complete");
        return true;
    }

    public @Nullable Patch getNext() {
        return new PostV5Patch(context);
    }
}
