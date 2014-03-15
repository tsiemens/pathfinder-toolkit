package com.lateensoft.pathfinder.toolkit.adapters.character;

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

public class PTFeatListAdapter extends ArrayAdapter<PTFeat>{
	Context mContext;
	int mLayoutResourceId;
	PTFeat[] mFeats = null;
	String TAG = "PTFeatListAdapter";

	public PTFeatListAdapter(Context context, int layoutResourceId, PTFeat[] feats) {
		super(context, layoutResourceId, feats);
		Log.v(TAG, "Constructing"); //TODO:Debug log
		mLayoutResourceId = layoutResourceId;
		mContext = context;
		mFeats = feats;
		Log.v(TAG, "Finished constructing"); //TODO:Debug log
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;

		FeatItemHolder holder;

		if(row == null) {
			LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();

			row = inflater.inflate(mLayoutResourceId, parent, false);
			holder = new FeatItemHolder();

			holder.name = (TextView)row.findViewById(R.id.featName);
			holder.description = (TextView)row.findViewById(R.id.featDescription);

			row.setTag(holder);
		}
		else {
			holder = (FeatItemHolder)row.getTag();
		}

		holder.name.setText(mFeats[position].getName());
		holder.description.setText(mFeats[position].getDescription().split("\\r?\\n")[0]);

		Log.v(TAG, "Finishing getView"); //TODO:Debug log
		return row;
	}

	static class FeatItemHolder {
		TextView name;
		TextView description;
	}
}
