package com.lateensoft.pathfinder.toolkit.views.picker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import com.lateensoft.pathfinder.toolkit.R;

import java.util.List;

public class PickerListAdapter extends ArrayAdapter<SelectablePair> {
    private static int ROW_LAYOUT_RESOURCE = R.layout.picker_row;

    private OnPairSelectionChangedListener m_selectedListener;

    public interface OnPairSelectionChangedListener {
        public void onSelectionChanged(ArrayAdapter adapter, SelectablePair pair, boolean isSelected);
    }

    public PickerListAdapter(Context context, List<SelectablePair> items, OnPairSelectionChangedListener listener) {
        super(context, ROW_LAYOUT_RESOURCE, items);
        m_selectedListener = listener;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RowHolder holder;
        SelectablePair itemAtPosition = getItem(position);

        if(row == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            row = inflater.inflate(ROW_LAYOUT_RESOURCE, parent, false);
            holder = new RowHolder();

            holder.checkBox = (CheckBox)row.findViewById(R.id.checkbox_is_selected);
            holder.checkBox.setOnCheckedChangeListener(new RowCheckedChangeListener(itemAtPosition));

            row.setTag(holder);
        } else {
            holder = (RowHolder)row.getTag();
        }

        holder.checkBox.setText(itemAtPosition.getValue());
        holder.checkBox.setChecked(itemAtPosition.isSelected());

        return row;
    }

    private static class RowHolder {
        CheckBox checkBox;
    }

    private class RowCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {
        SelectablePair m_selectablePair;

        public RowCheckedChangeListener(SelectablePair selectablePair) {
            m_selectablePair = selectablePair;
        }


        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            m_selectablePair.setSelected(isChecked);
            if (m_selectedListener != null) {
                m_selectedListener.onSelectionChanged(PickerListAdapter.this, m_selectablePair, isChecked);
            }
        }
    }
}
