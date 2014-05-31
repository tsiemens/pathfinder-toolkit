package com.lateensoft.pathfinder.toolkit.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.lateensoft.pathfinder.toolkit.model.IdStringPair;

import java.util.List;

public class IdStringPairListAdapter extends ArrayAdapter<IdStringPair>{
	private int m_layoutResourceId;

    /**
     * @param context
     * @param layoutResourceId must contain an android.R.id.text1 textview
     * @param idStringPairs
     */
	public IdStringPairListAdapter(Context context, int layoutResourceId, List<IdStringPair> idStringPairs) {
		super(context, layoutResourceId, idStringPairs);
		m_layoutResourceId = layoutResourceId;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;

		IdStringPairItemHolder holder;

		if(row == null) {
			LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();

			row = inflater.inflate(m_layoutResourceId, parent, false);
			holder = new IdStringPairItemHolder();

			holder.value = (TextView)row.findViewById(android.R.id.text1);
			row.setTag(holder);
		} else {
			holder = (IdStringPairItemHolder)row.getTag();
		}

		holder.value.setText(getItem(position).getValue());

		return row;
	}

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    private static class IdStringPairItemHolder {
		TextView value;
	}
}
