package com.lateensoft.pathfinder.toolkit.adapters.character;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.model.character.PTSpell;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PTSpellBookAdapter extends ArrayAdapter<PTSpell>{
	Context mContext;
	int mLayoutResourceId;
	PTSpell[] mSpells = null;
	String TAG = "PTSpellBookAdapter";
	
	public PTSpellBookAdapter(Context context, int layoutResourceId,
			PTSpell[] spells) {
		super(context, layoutResourceId, spells);
		Log.v(TAG, "Constructing"); //TODO:Debug log
		mLayoutResourceId = layoutResourceId;
		mContext = context;
		mSpells = spells;		
		Log.v(TAG, "Finished constructing"); //TODO:Debug log
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		/*if(convertView == null)
			row = new View(parent.getContext());
		else
			row = convertView;*/
		/*
		if(row == null) {
	        row = ((LayoutInflater)((Activity) parent.getContext()).getLayoutInflater())
	        .inflate(mLayoutResourceId,null);
		}*/
		
		SpellItemHolder holder;
		
		if(row == null) {
			LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
			
			row = inflater.inflate(mLayoutResourceId, parent, false);
			holder = new SpellItemHolder();
			
			holder.level = (TextView)row.findViewById(R.id.spellLevel);
			holder.name = (TextView)row.findViewById(R.id.spellName);
			holder.prepared = (TextView)row.findViewById(R.id.spellPrepared);
			
			row.setTag(holder);
		}
		else {
			holder = (SpellItemHolder)row.getTag();
		}
		
		holder.level.setText(Integer.toString(mSpells[position].getLevel()));
		holder.name.setText(mSpells[position].getName());
		holder.prepared.setText(Integer.toString(mSpells[position].getPrepared()));
		
		Log.v(TAG, "Finishing getView"); //TODO:Debug log
		return row;
	}
	
	static class SpellItemHolder {
		TextView prepared;
		TextView name;
		TextView level;
	}
}
