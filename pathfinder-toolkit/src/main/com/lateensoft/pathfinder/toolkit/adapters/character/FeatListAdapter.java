package com.lateensoft.pathfinder.toolkit.adapters.character;

import com.google.common.base.Splitter;
import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.model.character.Feat;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class FeatListAdapter extends ArrayAdapter<Feat>{
    private Context m_context;
    private int m_layoutResourceId;

    public FeatListAdapter(Context context, int layoutResourceId, List<Feat> feats) {
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

    private static class FeatItemHolder {
        TextView name;
        TextView description;
    }
}
