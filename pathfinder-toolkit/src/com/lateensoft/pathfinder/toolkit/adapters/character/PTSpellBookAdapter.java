package com.lateensoft.pathfinder.toolkit.adapters.character;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.model.character.PTSpell;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.lateensoft.pathfinder.toolkit.model.character.PTSpellBook;

public class PTSpellBookAdapter extends ArrayAdapter<PTSpell>{
	Context m_context;
	int m_layoutResourceId;
	PTSpellBook m_spellbook;

	public PTSpellBookAdapter(Context context, int layoutResourceId,
			PTSpellBook spellBook) {
		super(context, layoutResourceId, spellBook);
		m_layoutResourceId = layoutResourceId;
		m_context = context;
		m_spellbook = spellBook;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		SpellItemHolder holder;
		
		if(row == null) {
			LayoutInflater inflater = ((Activity) m_context).getLayoutInflater();
			
			row = inflater.inflate(m_layoutResourceId, parent, false);
			holder = new SpellItemHolder();
			
			holder.level = (TextView)row.findViewById(R.id.spellLevel);
			holder.name = (TextView)row.findViewById(R.id.spellName);
			holder.prepared = (TextView)row.findViewById(R.id.spellPrepared);
			
			row.setTag(holder);
		}
		else {
			holder = (SpellItemHolder)row.getTag();
		}
		
		holder.level.setText(Integer.toString(m_spellbook.get(position).getLevel()));
		holder.name.setText(m_spellbook.get(position).getName());
		holder.prepared.setText(Integer.toString(m_spellbook.get(position).getPrepared()));
		
		return row;
	}
	
	static class SpellItemHolder {
		TextView prepared;
		TextView name;
		TextView level;
	}
}
