package com.lateensoft.pathfinder.toolkit.adapters.character;

import com.google.common.base.Splitter;
import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.model.character.Spell;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.lateensoft.pathfinder.toolkit.model.character.SpellBook;

public class SpellBookListAdapter extends ArrayAdapter<Spell>{
    Context m_context;
    int m_layoutResourceId;
    SpellBook m_spellbook;

    public SpellBookListAdapter(Context context, int layoutResourceId,
                                SpellBook spellBook) {
        super(context, layoutResourceId, spellBook);
        m_layoutResourceId = layoutResourceId;
        m_context = context;
        m_spellbook = spellBook;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        SpellItemHolder holder;

        if(row == null) {
            LayoutInflater inflater = LayoutInflater.from(m_context);
            
            row = inflater.inflate(m_layoutResourceId, parent, false);
            holder = new SpellItemHolder();
            
            holder.level = (TextView)row.findViewById(R.id.spellLevel);
            holder.name = (TextView)row.findViewById(R.id.spellName);
            holder.prepared = (TextView)row.findViewById(R.id.spellPrepared);
            holder.description = (TextView)row.findViewById(R.id.spellDescription);

            row.setTag(holder);
        }
        else {
            holder = (SpellItemHolder)row.getTag();
        }

        holder.level.setText(Integer.toString(m_spellbook.get(position).getLevel()));
        holder.name.setText(m_spellbook.get(position).getName());
        holder.prepared.setText(Integer.toString(m_spellbook.get(position).getPrepared()));
        holder.description.setText(Splitter.onPattern("\\r?\\n")
                .splitToList(m_spellbook.get(position).getDescription()).get(0));
        return row;
    }

    static class SpellItemHolder {
        TextView prepared;
        TextView name;
        TextView level;
        TextView description;
    }
}
