package com.lateensoft.pathfinder.toolkit.views.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

import java.util.List;


public abstract class DynamicArrayAdapter<T> extends BaseArrayAdapter<T> {

    private OnItemsSwappedListener swapListener;

    public interface OnItemsSwappedListener {
        public void onItemsSwapped(int pos1, int pos2);
    }

    public DynamicArrayAdapter(Context context, int textViewResourceId, List<T> objects) {
        super(context, textViewResourceId, objects);
    }

    public void setOnItemsSwappedListener(OnItemsSwappedListener listener) {
        swapListener = listener;
    }

    public void swapItems(int pos1, int pos2) {
        doItemSwap(pos1, pos2);
        if (swapListener != null) {
            swapListener.onItemsSwapped(pos1, pos2);
        }
    }

    protected abstract void doItemSwap(int pos1, int pos2);

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
