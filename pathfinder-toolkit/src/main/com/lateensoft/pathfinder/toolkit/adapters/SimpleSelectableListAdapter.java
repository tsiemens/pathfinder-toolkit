package com.lateensoft.pathfinder.toolkit.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.lateensoft.pathfinder.toolkit.R;

import java.util.List;

public class SimpleSelectableListAdapter<T> extends ArrayAdapter<T>{

    public interface DisplayStringGetter<T> {
        public String getDisplayString(T object);
    }

    public interface ItemSelectionGetter {
        public boolean isItemSelected(int position);
    }

    private static int LAYOUT_RESOURCE_ID = R.layout.simple_list_item;
    private DisplayStringGetter<T> m_stringGetter;
    private ItemSelectionGetter m_selectionGetter;

    public SimpleSelectableListAdapter(Context context, List<T> objects, DisplayStringGetter<T> displayStringGetter) {
        super(context, LAYOUT_RESOURCE_ID, objects);
        m_stringGetter = displayStringGetter;
    }

    public void setItemSelectionGetter(ItemSelectionGetter selectionGetter) {
        m_selectionGetter = selectionGetter;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        ItemHolder holder;

        if(row == null) {
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();

            row = inflater.inflate(LAYOUT_RESOURCE_ID, parent, false);
            holder = new ItemHolder();

            holder.value = (TextView)row.findViewById(android.R.id.text1);
            row.setTag(holder);
        } else {
            holder = (ItemHolder)row.getTag();
        }

        holder.value.setText(m_stringGetter.getDisplayString(getItem(position)));
        if (m_selectionGetter != null) {
            row.setBackgroundColor(m_selectionGetter.isItemSelected(position) ?
                    getContext().getResources().getColor(R.color.holo_blue_light_translucent) :
                    getContext().getResources().getColor(android.R.color.transparent));
        }

        return row;
    }

    private static class ItemHolder {
        TextView value;
    }
}
