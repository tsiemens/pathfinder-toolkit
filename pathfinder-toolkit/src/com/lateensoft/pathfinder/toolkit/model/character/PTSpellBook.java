package com.lateensoft.pathfinder.toolkit.model.character;

import java.util.*;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;

public class PTSpellBook extends ArrayList<PTSpell> implements Parcelable {
	public static final int NUM_SPELL_LEVELS = 10;

    public PTSpellBook() {
        super();
    }

    public PTSpellBook(Collection<? extends PTSpell> collection) {
        super(collection);
    }

    public PTSpellBook(Parcel in) {
		in.readTypedList(this, PTSpell.CREATOR);
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
        out.writeTypedList(this);
	}
	
	public void setCharacterID(long id) {
		for (PTSpell spell : this) {
			spell.setCharacterID(id);
		}
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	public static final Parcelable.Creator<PTSpellBook> CREATOR = new Parcelable.Creator<PTSpellBook>() {
		public PTSpellBook createFromParcel(Parcel in) {
			return new PTSpellBook(in);
		}
		
		public PTSpellBook[] newArray(int size) {
			return new PTSpellBook[size];
		}
	};
}
