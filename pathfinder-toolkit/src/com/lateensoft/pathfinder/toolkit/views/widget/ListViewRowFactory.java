package com.lateensoft.pathfinder.toolkit.views.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class ListViewRowFactory<RowHolder> {

    public View rowFrom(Context context, final int position, View convertView, ViewGroup parent) {
        View row;
        RowHolder holder;
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            int layoutId = getRowLayoutResourceIdForPosition(position);
            row = inflater.inflate(layoutId, parent, false);
            holder = holderFrom(row, layoutId);
            row.setTag(holder);
        } else {
            row = convertView;
            holder = (RowHolder)row.getTag();
        }
        setRowContent(holder, position);
        return row;
    }

    protected abstract int getRowLayoutResourceIdForPosition(int position);
    protected abstract RowHolder holderFrom(View row, int layoutResourceId);
    protected abstract void setRowContent(RowHolder holder, int position);
}
