package com.lateensoft.pathfinder.toolkit.model.character.stats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.util.SparseIntArray;
import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.PTBaseApplication;
import com.lateensoft.pathfinder.toolkit.R;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;
import org.jetbrains.annotations.Nullable;

public class PTAbilitySet implements Parcelable{
	@SuppressWarnings("unused")
	private static final String TAG = PTAbilitySet.class.getSimpleName();
	private static final String PARCEL_BUNDLE_KEY_ABILITIES = "abilities";
	
	public static final int KEY_STR = 1;
	public static final int KEY_DEX = 2;
	public static final int KEY_CON = 3;
	public static final int KEY_INT = 4;
	public static final int KEY_WIS = 5;
	public static final int KEY_CHA = 6;
	
	private List<PTAbility> m_abilities;
	
	/**
	 * @return an unmodifiable list of the skill keys, in order. This matches the order for string resources, and how the abilities are stored
	 * in the set.
	 */
	public static List<Integer> ABILITY_KEYS() {
		Integer[] keys = {KEY_STR, KEY_DEX, KEY_CON, KEY_INT, KEY_WIS, KEY_CHA};
		return Collections.unmodifiableList(Arrays.asList(keys));
	}

    public interface CorrectionListener {
        public void onInvalidAbilityRemoved(PTAbility removedAbility);
        public void onMissingAbilityAdded(PTAbility addedAbility);
    }

    /**
     * Creates a valid ability set with abilities
     * If an ability does not exist, will be added and set to default.
     * Invalid abilities are removed
     */
    public static PTAbilitySet newValidAbilitySet(List<PTAbility> abilities, @Nullable CorrectionListener listener) {
        PTAbilitySet newAbilitySet = new PTAbilitySet(abilities);
        newAbilitySet.validate(listener);
        return newAbilitySet;
    }
	
	public PTAbilitySet() {
		List<Integer> constAbilityKeys = ABILITY_KEYS();
		m_abilities = Lists.newArrayListWithCapacity(constAbilityKeys.size());

        for (Integer abilityKey : constAbilityKeys) {
            m_abilities.add(new PTAbility(abilityKey));
        }
	}

	private PTAbilitySet(List<PTAbility> abilities) {
        m_abilities = abilities;
	}
	
	public PTAbilitySet(Parcel in) {
        m_abilities = Lists.newArrayListWithCapacity(ABILITY_KEYS().size());
        in.readTypedList(m_abilities, PTAbility.CREATOR);
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeTypedList(m_abilities);
	}

    public void validate(CorrectionListener listener) {
        List<Integer> constAbilityKeys = ABILITY_KEYS();
        List<PTAbility> abilitiesToRemove = Lists.newArrayList();
        for (PTAbility ability : m_abilities) {
            if (!constAbilityKeys.contains(ability.getAbilityKey())) {
                abilitiesToRemove.add(ability);
            }
        }
        m_abilities.removeAll(abilitiesToRemove);
        if (listener != null) {
            for (PTAbility removedAbility : abilitiesToRemove) {
                listener.onInvalidAbilityRemoved(removedAbility);
            }
        }

        boolean found;
        for (Integer abilityKey : constAbilityKeys) {
            found = false;
            for (PTAbility ability : m_abilities) {
                if (ability.getAbilityKey() == abilityKey) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                PTAbility newAbility = new PTAbility(abilityKey);
                newAbility.setCharacterID(m_abilities.get(0).getCharacterID());
                m_abilities.add(newAbility);
                if (listener != null) {
                    listener.onMissingAbilityAdded(newAbility);
                }
            }
        }

        Collections.sort(m_abilities);
    }
	
	/**
	 * @return The ability in the set with abilityKey. null if no such ability exists.
	 */
	public PTAbility getAbility(int abilityKey) throws IllegalArgumentException {
        for (PTAbility m_ability : m_abilities) {
            if (abilityKey == m_ability.getAbilityKey()) {
                return m_ability;
            }
        }
		throw new IllegalArgumentException("Invalid ability key: "+abilityKey);
	}
	
	/**
	 * @return the ability at index. Note: indexes of the set are defined by ABILITY_KEYS
	 */
	public PTAbility getAbilityAtIndex(int index) {
		return m_abilities.get(index);
	}
	
	/**
	 * @param maxDex maximum dex mod for the character
	 * @return the final mod value of the ability
	 */
	public int getTotalAbilityMod(int abilityKey, int maxDex) {
		int abilityMod = getAbility(abilityKey).getTempModifier();
		if (abilityKey == KEY_DEX && abilityMod > maxDex) {
			return maxDex;
		} else {
			return abilityMod;
		}
	}
	
	/**
	 * @return the short ability names, in the order as defined by ABILITY_KEYS
	 */
	public static String[] getShortAbilityNames() {
		Resources res = PTBaseApplication.getAppContext().getResources();
		return res.getStringArray(R.array.abilities_short);
	}
	
	/**
	 * @return a map of the ability keys to their short name
	 */
	public static SparseArray<String> getAbilityShortNameMap() {
		List<Integer> constAbilityKeys = ABILITY_KEYS();
		SparseArray<String> map = new SparseArray<String>(constAbilityKeys.size());
		String[] names = getShortAbilityNames();
		for (int i = 0; i < names.length; i++) {
			map.append(constAbilityKeys.get(i), names[i]);
		}
		return map;
	}	

	
	public int size(){
		return m_abilities.size();
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

    public static boolean isValidAbility(int abilityKey) {
        List<Integer> constAbilityKeys = ABILITY_KEYS();
        for (int key : constAbilityKeys) {
            if (key == abilityKey) return true;
        }
        return false;
    }
}
