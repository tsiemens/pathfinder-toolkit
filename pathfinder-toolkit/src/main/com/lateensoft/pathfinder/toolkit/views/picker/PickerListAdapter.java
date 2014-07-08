package com.lateensoft.pathfinder.toolkit.views.picker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.R;

import java.util.List;

public class PickerListAdapter extends ArrayAdapter<SelectablePair> {
    private static int ROW_LAYOUT_RESOURCE = R.layout.picker_row;

    private List<SelectablePair> m_items;
    private OnPairSelectionChangedListener m_selectedListener;
    private Filter m_filter;

    public interface OnPairSelectionChangedListener {
        public void onSelectionChanged(ArrayAdapter adapter, SelectablePair pair, boolean isSelected);
    }

    public PickerListAdapter(Context context, List<SelectablePair> items, OnPairSelectionChangedListener listener) {
        super(context, ROW_LAYOUT_RESOURCE, Lists.newArrayList(items));
        m_items = items;
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

        holder.checkBox.setText(itemAtPosition.getName());
        holder.checkBox.setChecked(itemAtPosition.isSelected());

        return row;
    }

    @Override
    public Filter getFilter() {
        if (m_filter == null) {
            m_filter = new SelectablePairFilter();
        }
        return m_filter;
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

    private class SelectablePairFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterSeq = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if (filterSeq != null && filterSeq.length() > 0) {
                List<SelectablePair> filteredPairs = Lists.newArrayList();

                for (SelectablePair pair : m_items) {
                    if (pair.getName().toLowerCase().contains(filterSeq))
                        filteredPairs.add(pair);
                }
                result.count = filteredPairs.size();
                result.values = filteredPairs;
            } else {
                synchronized (this) {
                    result.values = m_items;
                    result.count = m_items.size();
                }
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<SelectablePair> filtered = (List<SelectablePair>) results.values;
            clear();
            for (SelectablePair filteredPair : filtered) {
                add(filteredPair);
            }
            notifyDataSetChanged();
        }
    }
}
