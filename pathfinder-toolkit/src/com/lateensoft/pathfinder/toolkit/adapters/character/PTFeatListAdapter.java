package com.lateensoft.pathfinder.toolkit.adapters.character;

import com.google.common.base.Splitter;
import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.model.character.PTFeat;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class PTFeatListAdapter extends ArrayAdapter<PTFeat>{
    private static final String TAG = PTFeatListAdapter.class.getSimpleName();
	Context m_context;
	int m_layoutResourceId;

	public PTFeatListAdapter(Context context, int layoutResourceId, List<PTFeat> feats) {
		super(context, layoutResourceId, feats);
		m_layoutResourceId = layoutResourceId;
		m_context = context;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;

		FeatItemHolder holder;

		if(row == null) {
			LayoutInflater inflater = ((Activity) m_context).getLayoutInflater();

			row = inflater.inflate(m_layoutResourceId, parent, false);
			holder = new FeatItemHolder();

			holder.name = (TextView)row.findViewById(R.id.featName);
			holder.description = (TextView)row.findViewById(R.id.featDescription);

			row.setTag(holder);
		}
		else {
			holder = (FeatItemHolder)row.getTag();
		}

		holder.name.setText(getItem(position).getName());
		holder.description.setText(Splitter.onPattern("\\r?\\n")
                .splitToList(getItem(position).getDescription()).get(0));

		return row;
	}

	static class FeatItemHolder {
		TextView name;
		TextView description;
	}
}
