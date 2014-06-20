package com.lateensoft.pathfinder.toolkit.views.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.List;


public abstract class DynamicArrayAdapter<T> extends BaseArrayAdapter<T> {

    public DynamicArrayAdapter(Context context, int textViewResourceId, List<T> objects) {
        super(context, textViewResourceId, objects);
    }

    public abstract void swapItems(int pos1, int pos2);

    public Bitmap getBitmapForMobileRow(View row) {
        return getBitmapFromView(row);
    }

    private Bitmap getBitmapFromView(View v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas (bitmap);
        v.draw(canvas);
        return bitmap;
    }
}
