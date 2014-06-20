package com.lateensoft.pathfinder.toolkit.model.character.stats;

import java.util.*;

import com.google.common.collect.Lists;
import android.os.Parcel;
import android.os.Parcelable;
import org.jetbrains.annotations.Nullable;

public class SaveSet extends ValidatedTypedSet<Save, SaveType> implements Parcelable, Iterable<Save> {

	private List<Save> saves;

    public SaveSet() {
        super();
    }

    public SaveSet(List<Save> items, @Nullable CorrectionListener<Save> listener) {
        super(items, listener);
    }

    public SaveSet(Parcel in) {
        saves = Lists.newArrayListWithCapacity(getSortedTypes().size());
        in.readTypedList(saves, Save.CREATOR);
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
        out.writeTypedList(saves);
	}

    @Override
    protected void initWithValidatedItems(List<Save> items) {
        saves = items;
    }

    @Override
    protected List<SaveType> getSortedTypes() {
        return SaveType.getKeySortedValues();
    }

    @Override
    protected Save newItemOfType(SaveType type) {
        return new Save(type);
    }

    @Override
    protected ItemValidityChecker newItemValidityChecker() {
        return new ItemValidityChecker() {
            @Override public boolean isValid(Save item) {
                return numberOfTypePreviouslyFound(item.getType()) == 0;
            }
        };
    }

    public Save getSave(SaveType saveKey) {
		for (Save save : saves) {
			if (save.getType() == saveKey) {
				return save;
			}
		}
		return null;
	}
	
	public Save getSaveByIndex(int index) {
		return saves.get(index);
	}

    @Override
	public int size() {
		return saves.size();
	}
	
    @Deprecated
	public void setCharacterID(long id) {
		for (Save save : saves) {
			save.setCharacterID(id);
		}
	}

    @Override
     public Iterator<Save> iterator() {
        return new Iterator<Save>() {
            Iterator<Save> _iterator = saves.iterator();
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

        if (!this.saves.equals(saves.saves)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return saves.hashCode();
    }
}
