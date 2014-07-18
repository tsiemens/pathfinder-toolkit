package com.lateensoft.pathfinder.toolkit.model.character;

import java.util.*;

import android.os.Parcel;
import android.os.Parcelable;

public class SpellBook extends ArrayList<Spell> implements Parcelable {
    public static final int NUM_SPELL_LEVELS = 10;

    public SpellBook() {
        super();
    }

    public SpellBook(Collection<? extends Spell> collection) {
        super(collection);
    }

    public SpellBook(Parcel in) {
        in.readTypedList(this, Spell.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeTypedList(this);
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    public static final Parcelable.Creator<SpellBook> CREATOR = new Parcelable.Creator<SpellBook>() {
        public SpellBook createFromParcel(Parcel in) {
            return new SpellBook(in);
        }
        
        public SpellBook[] newArray(int size) {
            return new SpellBook[size];
        }
    };
}
