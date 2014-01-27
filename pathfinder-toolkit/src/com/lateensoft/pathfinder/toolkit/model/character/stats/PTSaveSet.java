package com.lateensoft.pathfinder.toolkit.model.character.stats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.lateensoft.pathfinder.toolkit.PTBaseApplication;
import com.lateensoft.pathfinder.toolkit.R;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;
import android.util.SparseIntArray;

public class PTSaveSet implements Parcelable {
	private static final String PARCEL_BUNDLE_KEY_SAVES = "saves";
	
	public static final int KEY_FORT = 1;
	public static final int KEY_REF = 2;
	public static final int KEY_WILL = 3;
	
	PTSave[] m_saves;
	
	/**
	 * @return an unmodifiable list of saves in order.
	 */
	public static List<Integer> SAVE_KEYS() {
		Integer[] keys = {KEY_FORT, KEY_REF, KEY_WILL};
		return Collections.unmodifiableList(Arrays.asList(keys));
	}
	
	/**
	 * @return an unmodifiable list of default keys for saves in order.
	 */
	public static List<Integer> DEFAULT_ABILITIES() {
		Integer[] keys = {PTAbilitySet.KEY_CON, PTAbilitySet.KEY_DEX, PTAbilitySet.KEY_WIS};
		return Collections.unmodifiableList(Arrays.asList(keys));
	}
	
	public PTSaveSet() {
		List<Integer> constSaveKeys = SAVE_KEYS();
		List<Integer> constDefaultAbilities = DEFAULT_ABILITIES();
		m_saves = new PTSave[constSaveKeys.size()];
		
		for(int i = 0; i < constSaveKeys.size(); i++) {
			m_saves[i] = new PTSave(constSaveKeys.get(i), constDefaultAbilities.get(i));
		}
	}
	
	/**
	 * Safely populates the save set with saves 
	 * If an save does not exist in saves, will be set to default.
	 * @param saves
	 */
	public PTSaveSet(PTSave[] saves) {
		List<Integer> constSaveKeys = SAVE_KEYS();
		List<Integer> constDefaultAbilities = DEFAULT_ABILITIES();
		m_saves = new PTSave[constSaveKeys.size()];
		List<PTSave> savesList = new ArrayList<PTSave>(Arrays.asList(saves));
		
		for(int i = 0; i < constSaveKeys.size(); i++) {
			for (PTSave save : savesList) {
				if(save.getSaveKey() == constSaveKeys.get(i).intValue()) {
					savesList.remove(save);
					m_saves[i] = save;
					break;
				}
			}
			if (m_saves[i] == null) {
				m_saves[i] = new PTSave(constSaveKeys.get(i), constDefaultAbilities.get(i));
			}
		}
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
	
	public PTSave getSave(int saveKey) {
		for (PTSave save : m_saves) {
			if (save.getSaveKey() == saveKey) {
				return save;
			}
		}
		return null;
	}
	
	public PTSave[] getSaves(){
		return m_saves;
	}
	
	public PTSave getSaveByIndex(int index) {
		return m_saves[index];
	}
	
	public int size() {
		return m_saves.length;
	}
	
	/**
	 * @return a map of the save key to the ability key
	 */
	public static SparseIntArray getDefaultAbilityKeyMap() {
		List<Integer> constSaveKeys = SAVE_KEYS();
		List<Integer> constDefaultAbilities = DEFAULT_ABILITIES();
		SparseIntArray map = new SparseIntArray(constSaveKeys.size());
		for(int i = 0; i < constSaveKeys.size(); i++) {
			map.append(constSaveKeys.get(i), constDefaultAbilities.get(i));
		}
		return map;
	}
	
	/**
	 * @return the skill names, in the order as defined by SAVE_KEYS
	 */
	public static String[] getSaveNames() {
		Resources res = PTBaseApplication.getAppContext().getResources();
		return res.getStringArray(R.array.save_names);
	}
	
	/**
	 * @return a map of the save keys to their name
	 */
	public static SparseArray<String> getSaveNameMap() {
		List<Integer> constSaveKeys = SAVE_KEYS();
		SparseArray<String> map = new SparseArray<String>(constSaveKeys.size());
		String[] names = getSaveNames();
		for (int i = 0; i < names.length; i++) {
			map.append(constSaveKeys.get(i), names[i]);
		}
		return map;
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
