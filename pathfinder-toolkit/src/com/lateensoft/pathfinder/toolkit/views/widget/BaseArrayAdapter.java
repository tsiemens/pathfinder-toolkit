package com.lateensoft.pathfinder.toolkit.views.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class BaseArrayAdapter<T> extends ArrayAdapter<T> {

    private ListViewRowFactory rowFactory;

    public BaseArrayAdapter(Context context, int resource) {
        super(context, resource);
        init();
    }

    public BaseArrayAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
        init();
    }

    public BaseArrayAdapter(Context context, int resource, T[] objects) {
        super(context, resource, objects);
        init();
    }

    public BaseArrayAdapter(Context context, int resource, int textViewResourceId, T[] objects) {
        super(context, resource, textViewResourceId, objects);
        init();
    }

    public BaseArrayAdapter(Context context, int resource, List<T> objects) {
        super(context, resource, objects);
        init();
    }

    public BaseArrayAdapter(Context context, int resource, int textViewResourceId, List<T> objects) {
        super(context, resource, textViewResourceId, objects);
        init();
    }

    private void init() {
        rowFactory = newRowFactory();
    }

    protected abstract ListViewRowFactory newRowFactory();

    @Nullable
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return rowFactory.rowFrom(getContext(), position, convertView, parent);
    }
}
