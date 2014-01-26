package com.lateensoft.pathfinder.toolkit.model.character;

import java.util.ArrayList;
import java.util.Arrays;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class PTSpellBook implements Parcelable {
	private static final String PARCEL_BUNDLE_KEY_SPELLS = "spells"; 
	
	private static final int NUM_SPELL_LEVELS = 10;
	
	private ArrayList<PTSpell> m_spells;

	public PTSpellBook() {
		m_spells = new ArrayList<PTSpell>();
	}
	
	/**
	 * Spells must be sorted by level
	 * @param spells
	 */
	public PTSpellBook(PTSpell[] spells) {
		m_spells = new ArrayList<PTSpell>(Arrays.asList(spells));
	}
	
	public PTSpellBook(Parcel in) {
		Bundle objectBundle = in.readBundle();
		m_spells = objectBundle.getParcelableArrayList(PARCEL_BUNDLE_KEY_SPELLS);
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		Bundle objectBundle = new Bundle();
		objectBundle.putParcelableArrayList(PARCEL_BUNDLE_KEY_SPELLS, m_spells);
		out.writeBundle(objectBundle);
	}
	
	public PTSpell[] getSpells() {
		PTSpell[] spells = new PTSpell[m_spells.size()];
		return (PTSpell[]) m_spells.toArray(spells);
	}

	

	public void deleteSpell(PTSpell spell) {
		m_spells.remove(m_spells.indexOf(spell));
	}
	
	public void deleteSpell(int index) {
		m_spells.remove(index);
	}
	
	/**
	 * 
	 * @param level
	 * @param index
	 * @return The specified spell
	 */
	public PTSpell getSpell(int index) {
		return (PTSpell) m_spells.get(index);
	}
	
	public void addSpell(PTSpell spell) {
		if(spell == null)
			return;
		
		if(m_spells.size() == 0) {
			m_spells.add(spell);
			return;
		}
		
		for(int i = 0; i < this.getSpellCount(); i++) {
			if(m_spells.get(i).getLevel() > spell.getLevel()) {
				m_spells.add(i, spell);
				return;
			}
		}
		m_spells.add(spell);
	}

	public int getSpellCount() {
		return m_spells.size();
	}
	
	public int getNumSpellLevels() {
		return NUM_SPELL_LEVELS;
	}
	
	public boolean contains(PTSpell spell) {
		if(m_spells.contains(spell))
			return true;
		return false;
	}

	public void setSpell(int mSpellSelectedForEdit, PTSpell spell) {
		if(spell.getLevel() == m_spells.get(mSpellSelectedForEdit).getLevel()) {
			m_spells.set(mSpellSelectedForEdit, spell);	
		} else {
			// Make sure the spells stay ordered by level
			m_spells.remove(mSpellSelectedForEdit);
			this.addSpell(spell);
		}
	}
	
	public void setCharacterID(long id) {
		for (PTSpell spell : m_spells) {
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
