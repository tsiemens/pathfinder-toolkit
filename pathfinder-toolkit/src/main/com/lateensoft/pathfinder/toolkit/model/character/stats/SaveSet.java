package com.lateensoft.pathfinder.toolkit.model.character.stats;

import java.util.*;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.BaseApplication;
import com.lateensoft.pathfinder.toolkit.R;

import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;
import android.util.SparseIntArray;
import org.jetbrains.annotations.Nullable;

public class SaveSet implements Parcelable, Iterable<Save> {
	public static final int KEY_FORT = 1;
	public static final int KEY_REF = 2;
	public static final int KEY_WILL = 3;

    /**
     * list of saves keys in order.
     */
    public static final ImmutableList<Integer> SAVE_KEYS;
    public static final ImmutableList<Integer> DEFAULT_ABILITIES;

    static {
        ImmutableList.Builder<Integer> builder = ImmutableList.builder();
        builder.add(KEY_FORT, KEY_REF, KEY_WILL);
        SAVE_KEYS = builder.build();

        builder = ImmutableList.builder();
        builder.add(AbilitySet.KEY_CON, AbilitySet.KEY_DEX, AbilitySet.KEY_WIS);
        DEFAULT_ABILITIES = builder.build();
    }
	
	private List<Save> m_saves;

    public interface CorrectionListener {
        public void onInvalidSaveRemoved(Save removedSkill);
        public void onMissingSaveAdded(Save addedSkill);
    }
	
    public static SaveSet newValidatedSaveSet(List<Save> saves, @Nullable CorrectionListener listener) {
        SaveSet saveSet = new SaveSet(saves);
        saveSet.validate(listener);
        return saveSet;
    }

	private SaveSet(List<Save> saves) {
        m_saves = saves;
	}

    public SaveSet() {
        m_saves = Lists.newArrayListWithCapacity(SAVE_KEYS.size());

        for(int i = 0; i < SAVE_KEYS.size(); i++) {
            m_saves.add(new Save(SAVE_KEYS.get(i), DEFAULT_ABILITIES.get(i)));
        }
    }
	
	public SaveSet(Parcel in) {
        m_saves = Lists.newArrayListWithCapacity(SAVE_KEYS.size());
        in.readTypedList(m_saves, Save.CREATOR);
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
        out.writeTypedList(m_saves);
	}

    public void validate(@Nullable CorrectionListener listener) {
        List<Save> validSaves = Lists.newArrayListWithCapacity(SAVE_KEYS.size());
        List<Save> invalidSaves = Lists.newArrayList();

        for (Save save : m_saves) {
            if(!SAVE_KEYS.contains(save.getSaveKey())) {
                invalidSaves.add(save);
            }
        }
        m_saves.removeAll(invalidSaves);
        if (listener != null) {
            for (Save removedSave : invalidSaves) {
                listener.onInvalidSaveRemoved(removedSave);
            }
        }

        for(int i = 0; i < SAVE_KEYS.size(); i++) {
            boolean found = false;
            for (Save save : m_saves) {
                if(save.getSaveKey() == SAVE_KEYS.get(i)) {
                    validSaves.add(save);
                    found = true;
                    break;
                }
            }
            if (!found) {
                Save newSave = new Save(SAVE_KEYS.get(i), DEFAULT_ABILITIES.get(i));
                newSave.setCharacterID(m_saves.get(0).getCharacterID());
                validSaves.add(newSave);
                if (listener != null) {
                    listener.onMissingSaveAdded(newSave);
                }
            }
        }

        m_saves = validSaves;
    }
	
	public Save getSave(int saveKey) {
		for (Save save : m_saves) {
			if (save.getSaveKey() == saveKey) {
				return save;
			}
		}
		return null;
	}
	
	public Save getSaveByIndex(int index) {
		return m_saves.get(index);
	}
	
	public int size() {
		return m_saves.size();
	}
	
	/**
	 * @return a map of the save key to the ability key
	 */
	public static SparseIntArray getDefaultAbilityKeyMap() {
        SparseIntArray map = new SparseIntArray(SAVE_KEYS.size());
		for(int i = 0; i < SAVE_KEYS.size(); i++) {
			map.append(SAVE_KEYS.get(i), DEFAULT_ABILITIES.get(i));
		}
		return map;
	}

	/**
	 * @return the skill names, in the order as defined by SAVE_KEYS
	 */
	public static String[] getSaveNames() {
		Resources res = BaseApplication.getAppContext().getResources();
		return res.getStringArray(R.array.save_names);
	}

	/**
	 * @return a map of the save keys to their name
	 */
	public static SparseArray<String> getSaveNameMap() {
        SparseArray<String> map = new SparseArray<String>(SAVE_KEYS.size());
		String[] names = getSaveNames();
		for (int i = 0; i < names.length; i++) {
			map.append(SAVE_KEYS.get(i), names[i]);
		}
		return map;
	}
	
	public void setCharacterID(long id) {
		for (Save save : m_saves) {
			save.setCharacterID(id);
		}
	}

    @Override
     public Iterator<Save> iterator() {
        return new Iterator<Save>() {
            Iterator<Save> _iterator = m_saves.iterator();
            @Override public boolean hasNext() {
                return _iterator.hasNext();
            }

            @Override public Save next() {
                return _iterator.next();
            }

            @Override public void remove() {
                throw new UnsupportedOperationException(SaveSet.class.getSimpleName() + ".iterator.remove");
            }
        };
    }
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	public static final Parcelable.Creator<SaveSet> CREATOR = new Parcelable.Creator<SaveSet>() {
		public SaveSet createFromParcel(Parcel in) {
			return new SaveSet(in);
		}
		
		public SaveSet[] newArray(int size) {
			return new SaveSet[size];
		}
	};

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SaveSet)) return false;

        SaveSet saves = (SaveSet) o;

        if (!m_saves.equals(saves.m_saves)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return m_saves.hashCode();
    }
}
