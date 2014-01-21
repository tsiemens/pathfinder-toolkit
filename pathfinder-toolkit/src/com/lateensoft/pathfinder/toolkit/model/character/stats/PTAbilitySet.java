package com.lateensoft.pathfinder.toolkit.model.character.stats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lateensoft.pathfinder.toolkit.PTBaseApplication;
import com.lateensoft.pathfinder.toolkit.R;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class PTAbilitySet implements Parcelable{
	@SuppressWarnings("unused")
	private static final String TAG = PTAbilitySet.class.getSimpleName();
	private static final String PARCEL_BUNDLE_KEY_ABILITIES = "abilities";
	
	public static final long ID_STR = 1;
	public static final long ID_DEX = 2;
	public static final long ID_CON = 3;
	public static final long ID_INT = 4;
	public static final long ID_WIS = 5;
	public static final long ID_CHA = 6;
	
	/**
	* This matches the order for string resources, and how the abilities are stored
	* in the set.
	*/
	public static final long[] ABILITY_IDS = {ID_STR, ID_DEX, ID_CON, ID_INT, ID_WIS, ID_CHA};
	
	private PTAbility[] m_abilities;
	
	public PTAbilitySet() {
		m_abilities = new PTAbility[ABILITY_IDS.length];
		
		for(int i = 0; i < ABILITY_IDS.length; i++) {
			m_abilities[i] = new PTAbility(ABILITY_IDS[i], PTAbility.BASE_ABILITY_SCORE, 0);
		}
	}
	
	/**
	 * Safely populates the ability set with scores 
	 * If an ability does not exist in scores, will be set to default.
	 * @param abilities
	 */
	public PTAbilitySet(PTAbility[] abilities) {
		m_abilities = new PTAbility[ABILITY_IDS.length];
		List<PTAbility> scoresList = new ArrayList<PTAbility>(Arrays.asList(abilities));
		
		for(int i = 0; i < ABILITY_IDS.length; i++) {
			for (PTAbility score : scoresList) {
				if(score.getID() == ABILITY_IDS[i]) {
					scoresList.remove(score);
					m_abilities[i] = score;
					break;
				}
			}
			if (m_abilities[i] == null) {
				m_abilities[i] = new PTAbility(ABILITY_IDS[i]);
			}
		}
	}
	
	public PTAbilitySet(Parcel in) {
		Bundle objectBundle = in.readBundle();
		m_abilities = (PTAbility[]) objectBundle.getParcelableArray(PARCEL_BUNDLE_KEY_ABILITIES);
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		Bundle objectBundle = new Bundle();
		objectBundle.putParcelableArray(PARCEL_BUNDLE_KEY_ABILITIES, m_abilities);
		out.writeBundle(objectBundle);
	}
	
	/**
	 * @param abilityId
	 * @return The ability in the set with abilityId. null if no such ability exists.
	 */
	public PTAbility getAbility(long abilityId) {
		for(int i = 0; i < m_abilities.length; i++) {
			if(abilityId == m_abilities[i].getID()) {
				return m_abilities[i];
			}
		}
		return null;
	}
	
	/**
	 * @param index
	 * @return the ability at index. Note: indexes of the set are defined by ABILITY_IDS
	 */
	public PTAbility getAbilityAtIndex(int index) {
		if( index >=0 && index <= m_abilities.length) {
			return m_abilities[index];
		} else {
			throw new IndexOutOfBoundsException("No ability for index "+index);
		}
	}

	/**
	 * @return the long ability names, in the order as defined by ABILITY_IDS
	 */
	public static String[] getLongAbilityNames() {
		return getStringArray(R.array.abilities_long);
	}
	
	/**
	 * @return the short ability names, in the order as defined by ABILITY_IDS
	 */
	public static String[] getShortAbilityNames() {
		return getStringArray(R.array.abilities_short);
	}
	
	/**
	 * @param arrayResId resource id of long or short name array
	 * @return Array of the names
	 */
	private static String[] getStringArray(int arrayResId) {
		Resources res = PTBaseApplication.getAppContext().getResources();
		return res.getStringArray(arrayResId);
	}
	
	/**
	 * @return a map of the ability ids to their short name
	 */
	public static Map<Long, String> getAbilityShortNameMap() {
		Map<Long, String> map = new HashMap<Long, String>(ABILITY_IDS.length);
		String[] names = getShortAbilityNames();
		for (int i = 0; i < names.length; i++) {
			map.put(ABILITY_IDS[i], names[i]);
		}
		return map;
	}	

	
	public int size(){
		return m_abilities.length;
	}
	
	public void setCharacterID(long id) {
		for (PTAbility ability : m_abilities) {
			ability.setCharacterID(id);
		}
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	public static final Parcelable.Creator<PTAbilitySet> CREATOR = new Parcelable.Creator<PTAbilitySet>() {
		public PTAbilitySet createFromParcel(Parcel in) {
			return new PTAbilitySet(in);
		}
		
		public PTAbilitySet[] newArray(int size) {
			return new PTAbilitySet[size];
		}
	};
}
