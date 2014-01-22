package com.lateensoft.pathfinder.toolkit.views.character;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTAbilitySet;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;

public class PTAbilitySelectionDialog {

	private OnAbilitySelectedListener m_listener;
	private AlertDialog.Builder m_builder;
	
	public PTAbilitySelectionDialog(Context context, int checkedAbilityKey, int defaultAbilityKey) {
		m_builder = new AlertDialog.Builder(context);
		Resources r = m_builder.getContext().getResources();
		String[] abilityNames = r.getStringArray(R.array.abilities_short);
		
		// Making the default visible to user
		int defaultAbilityIndex = getIndexForAbilityKey(defaultAbilityKey);
		abilityNames[defaultAbilityIndex] = abilityNames[defaultAbilityIndex] +
				r.getString(R.string.default_ability_label);
		
		OnClickListener clickListener = new OnClickListener() {	
			private int m_selectedAbilityKey = 0;
			
			@Override public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					if (m_listener != null) {
						m_listener.onAbilitySelected(m_selectedAbilityKey);
					}
					break;
				case DialogInterface.BUTTON_NEGATIVE:
					break;
				default:
					// Set the currently selected ability
					m_selectedAbilityKey = PTAbilitySet.ABILITY_KEYS[which];
					break;

				}
			}
		};
		m_builder.setSingleChoiceItems(abilityNames, 
				getIndexForAbilityKey(checkedAbilityKey), clickListener);
		m_builder.setTitle(R.string.select_ability_dialog_title);
		m_builder.setPositiveButton(R.string.ok_button_text, clickListener);
		m_builder.setNegativeButton(R.string.cancel_button_text, clickListener);
	}
	
	public PTAbilitySelectionDialog setOnAbilitySelectedListener(OnAbilitySelectedListener listener) {
		m_listener = listener;
		return this;
	}
	
	public void show() {
		m_builder.show();
	}

	protected int getIndexForAbilityKey(int abilityKey) {
		for (int i = 0; i < PTAbilitySet.ABILITY_KEYS.length; i++) {
			if (PTAbilitySet.ABILITY_KEYS[i] == abilityKey) {
				return i;
			}
		}
		return -1;
	}
	
	public static interface OnAbilitySelectedListener {
		
		/**
		 * @param abilityKey the id of the ability selected, or 0 if none selected.
		 */
		public void onAbilitySelected(int abilityKey);
	}
}
