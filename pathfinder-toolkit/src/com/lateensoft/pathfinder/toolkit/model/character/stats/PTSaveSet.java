package com.lateensoft.pathfinder.toolkit.model.character.stats;

import com.lateensoft.pathfinder.toolkit.R;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class PTSaveSet implements Parcelable{
	private static final String PARCEL_BUNDLE_KEY_SAVES = "saves";
	public static final int SIZE = 3;
	
	PTSave[] m_saves;
	
	public PTSaveSet(Context context) {
		Resources r = context.getResources();
		String[] names = r.getStringArray(R.array.save_names);
		
		m_saves = new PTSave[SIZE];
		for(int i = 0; i < m_saves.length; i++) {
			m_saves[i] = new PTSave(names[i]);
		}
	}
	
	/**
	 * Creates new save set with saves
	 * Dangerous; should only be used by database
	 * @param saves
	 */
	public PTSaveSet(PTSave[] saves) {
		m_saves = saves;
	}
	
	public PTSaveSet(Parcel in) {
		Bundle objectBundle = in.readBundle();
		m_saves = (PTSave[]) objectBundle.getParcelableArray(PARCEL_BUNDLE_KEY_SAVES);
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		Bundle objectBundle = new Bundle();
		objectBundle.putParcelableArray(PARCEL_BUNDLE_KEY_SAVES, m_saves);
		out.writeBundle(objectBundle);
	}
	
	/**
	 * Sets the saves in the set.
	 * Dangerous; should only be used by database
	 * @param saves
	 */
	public void setSaves(PTSave[] saves) {
		m_saves = saves;
	}
	
	public PTSave[] getSaves(){
		return m_saves;
	}
	
	public PTSave getSave(int index) {
		return m_saves[index];
	}
	
	public void setAbilityMods(PTAbilitySet abilities, Context context) {
		Resources r = context.getResources();
		
		m_saves[r.getInteger(R.integer.key_fort_save)].setAbilityMod(
				abilities.getAbilityAtIndex(R.integer.key_constitution).getAbilityModifier());
		m_saves[r.getInteger(R.integer.key_ref_save)].setAbilityMod(
				abilities.getAbilityAtIndex(R.integer.key_dexterity).getAbilityModifier());
		m_saves[r.getInteger(R.integer.key_will_save)].setAbilityMod(
				abilities.getAbilityAtIndex(R.integer.key_wisdom).getAbilityModifier());
	}
	
	public void setCharacterID(long id) {
		for (PTSave save : m_saves) {
			save.setCharacterID(id);
		}
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	public static final Parcelable.Creator<PTSaveSet> CREATOR = new Parcelable.Creator<PTSaveSet>() {
		public PTSaveSet createFromParcel(Parcel in) {
			return new PTSaveSet(in);
		}
		
		public PTSaveSet[] newArray(int size) {
			return new PTSaveSet[size];
		}
	};
}
