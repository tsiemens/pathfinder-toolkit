package com.lateensoft.pathfinder.toolkit.model.character.stats;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;
import android.os.Parcel;
import android.os.Parcelable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.lateensoft.pathfinder.toolkit.model.character.stats.AbilityType.*;

public class AbilitySet extends ValidatedTypedSet<Ability, AbilityType> implements Parcelable, Iterable<Ability> {

	private List<Ability> abilities;

    public AbilitySet() {
        super();
    }

    public AbilitySet(List<Ability> items, @Nullable ValidatedTypedSet.CorrectionListener<Ability> listener) {
        super(items, listener);
    }

	public AbilitySet(Parcel in) {
        abilities = Lists.newArrayListWithCapacity(values().length);
        in.readTypedList(abilities, Ability.CREATOR);
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeTypedList(abilities);
	}

    @Override
    protected void initWithValidatedItems(List<Ability> items) {
        abilities = items;
    }

    @Override
    protected List<AbilityType> getSortedTypes() {
        return AbilityType.getKeySortedValues();
    }

    @Override
    protected Ability newItemOfType(AbilityType type) {
        return new Ability(type);
    }

    @Override
    protected ItemValidityChecker newItemValidityChecker() {
        return new ItemValidityChecker() {
            @Override public boolean isValid(Ability item) {
                return numberOfTypePreviouslyFound(item.getType()) == 0;
            }
        };
    }

	public Ability getAbility(@NotNull AbilityType type) {
        for (Ability ability : abilities) {
            if (type == ability.getType()) {
                return ability;
            }
        }
		throw new NullPointerException();
	}
	
	/**
	 * @return the ability at index. Note: indexes of the set are defined by ABILITY_KEYS
	 */
	public Ability getAbilityAtIndex(int index) {
		return abilities.get(index);
	}
	
	/**
	 * @param maxDex maximum dex mod for the character
	 * @return the final mod value of the ability
	 */
	public int getTotalAbilityMod(AbilityType type, int maxDex) {
		int abilityMod = getAbility(type).getTempModifier();
		if (type == DEX && abilityMod > maxDex) {
			return maxDex;
		} else {
			return abilityMod;
		}
	}

    @Override
	public int size(){
		return abilities.size();
	}

    @Deprecated
	public void setCharacterID(long id) {
		for (Ability ability : abilities) {
			ability.setCharacterID(id);
		}
	}

    @Override
    public Iterator<Ability> iterator() {
        return new Iterator<Ability>() {
            Iterator<Ability> iterator = abilities.iterator();
            @Override public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override public Ability next() {
                return iterator.next();
            }

            @Override public void remove() {
                throw new UnsupportedOperationException(AbilitySet.class.getSimpleName() + ".iterator.remove");
            }
        };
    }
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	public static final Parcelable.Creator<AbilitySet> CREATOR = new Parcelable.Creator<AbilitySet>() {
		public AbilitySet createFromParcel(Parcel in) {
			return new AbilitySet(in);
		}
		
		public AbilitySet[] newArray(int size) {
			return new AbilitySet[size];
		}
	};

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbilitySet)) return false;

        AbilitySet that = (AbilitySet) o;

        if (!abilities.equals(that.abilities)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return abilities.hashCode();
    }
}
