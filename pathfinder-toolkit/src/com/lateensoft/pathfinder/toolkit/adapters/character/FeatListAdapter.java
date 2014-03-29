package com.lateensoft.pathfinder.toolkit.adapters.character;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.model.character.Feat;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FeatListAdapter extends ArrayAdapter<Feat>{
	Context mContext;
	int mLayoutResourceId;
	Feat[] mFeats = null;
	String TAG = "FeatListAdapter";

	public FeatListAdapter(Context context, int layoutResourceId, Feat[] feats) {
		super(context, layoutResourceId, feats);
		Log.v(TAG, "Constructing");
		mLayoutResourceId = layoutResourceId;
		mContext = context;
		mFeats = feats;
		Log.v(TAG, "Finished constructing");
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

		Log.v(TAG, "Finishing getView");
		return row;
	}

	static class FeatItemHolder {
		TextView name;
		TextView description;
	}
}
