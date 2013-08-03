package com.lateensoft.pathfinder.toolkit.character;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class PTSpellBook implements Parcelable{
	private static final String PARCEL_BUNDLE_KEY_SPELLS = "spells"; 
	
	private static final int NUM_SPELL_LEVELS = 10;
	
	private ArrayList<PTSpell> mSpells;

	//LargestLevelSet mLargestLevelSet;
	
	public PTSpellBook() {
		mSpells = new ArrayList<PTSpell>();
		
		/*
		for(int i = 0; i < mSpells.size(); i++) {
			mSpells.set(i, new ArrayList<PTSpell>());
			// Dummy spell
			//mSpells.get(i).add(new PTSpell());
		}*/
		//mSpells.add(new PTSpell());
	}
	
	/*
	public PTSpellBook(PTSpell[][] spells) {
		setSpells(spells);
	}*/
	/*
	public PTSpell[] getSpells(int level) {
		if(level < 0 || mSpells.isEmpty() || mSpells == null || mSpells.get(level) == null
				|| mSpells.get(level).isEmpty()) {
			PTSpell[] newSpellArray = new PTSpell[1];
			newSpellArray[0] = new PTSpell();
			return newSpellArray;
		}
		PTSpell[] levelSpells = new PTSpell[mSpells.get(level).size()];
		return (PTSpell[]) ((ArrayList<PTSpell>)mSpells.get(level)).toArray(levelSpells);
	}*/
	
	public PTSpellBook(Parcel in) {
		Bundle objectBundle = in.readBundle();
		mSpells = objectBundle.getParcelableArrayList(PARCEL_BUNDLE_KEY_SPELLS);
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		Bundle objectBundle = new Bundle();
		objectBundle.putParcelableArrayList(PARCEL_BUNDLE_KEY_SPELLS, mSpells);
		out.writeBundle(objectBundle);
	}
	
	public PTSpell[] getSpells() {
		PTSpell[] spells = new PTSpell[mSpells.size()];
		return (PTSpell[]) mSpells.toArray(spells);
	}
	
	/*
	public void setSpells(PTSpell[][] spells) {
		mSpells = new PTSpell[NUM_SPELL_LEVELS][mLargestLevelSet.value];
		for(int j = 0; j < NUM_SPELL_LEVELS; j++) {
			for(int i = 0; i < spells[j].length; i++) {
				mSpells[j][i] = spells[j][i];
			}
			mSpellCount[j] = spells[j].length;
		}
	}*/
	
	public void deleteSpell(PTSpell spell) {
		//mSpells.get(spell.getLevel()).remove(mSpells.get(spell.getLevel()).indexOf(spell));
		
		mSpells.remove(mSpells.indexOf(spell));
	}
	
	public void deleteSpell(int index) {
		mSpells.remove(index);
	}
	
	/**
	 * 
	 * @param level
	 * @param index
	 * @return The specified spell
	 */
	public PTSpell getSpell(int index) {
		return (PTSpell) mSpells.get(index);
	}
	
	public void addSpell(PTSpell spell) {
		if(spell == null)
			return;
		
		if(mSpells.size() == 0) {
			mSpells.add(spell);
			return;
		}
		
		for(int i = 0; i < this.getSpellCount(); i++) {
			if(mSpells.get(i).getLevel() > spell.getLevel()) {
				mSpells.add(i, spell);
				return;
			}
		}
		mSpells.add(spell);
	}

	public int getSpellCount() {
		return mSpells.size();
	}
	
	public int getNumSpellLevels() {
		return NUM_SPELL_LEVELS;
	}
	
	public boolean contains(PTSpell spell) {
		if(mSpells.contains(spell))
			return true;
		return false;
	}

	public void setSpell(int mSpellSelectedForEdit, PTSpell spell) {
		if(spell.getLevel() == mSpells.get(mSpellSelectedForEdit).getLevel()) {
			mSpells.set(mSpellSelectedForEdit, spell);	
		} else {
			// Make sure the spells stay ordered by level
			mSpells.remove(mSpellSelectedForEdit);
			this.addSpell(spell);
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
