package com.lateensoft.pathfinder.toolkit.model.character;

import com.lateensoft.pathfinder.toolkit.db.repository.Storable;

import android.os.Parcel;
import android.os.Parcelable;

public class Spell implements Parcelable, Storable, Comparable<Spell> {
	@SuppressWarnings("unused")
	private static final String TAG = Spell.class.getSimpleName();
	
	private String m_name;
	private int m_prepared;
	private int m_level;
	private String m_description;
	
	private long m_id;
	private long m_characterId;
	
	public Spell() {
		this("");
	}
	
	public Spell(String name) {
		this(name, 0);
	}
	
	public Spell(String name, int level) {
		this(UNSET_ID, name, level, 0, "");
	}
	
	public Spell(long characterId, String name, int level, int prepared, String description) {
		this(UNSET_ID, characterId, name, level, prepared, description);
	}
	
	public Spell(long id, long characterId, String name, int level, int prepared, String description) {
		m_id = id;
		m_characterId = characterId;
		m_name = name;
		m_level = level;
		m_prepared = prepared;
		m_description = description;
	}
	
	public Spell(Spell spell) {
		this(spell.getID(), spell.getCharacterID(), spell.getName(),
				spell.getLevel(), spell.getPrepared(), spell.getDescription());
	}
	
	public Spell(Parcel in) {
		m_name = in.readString();
		m_level = in.readInt();
		m_prepared = in.readInt();
		m_description = in.readString();
		m_id = in.readLong();
		m_characterId = in.readLong();
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(m_name);
		out.writeInt(m_level);
		out.writeInt(m_prepared);
		out.writeString(m_description);
		out.writeLong(m_id);
		out.writeLong(m_characterId);
	}
	
	public String getName() {
		return m_name;
	}
	
	public void setName(String name) {
		m_name = name;
	}
	
	public String getDescription() {
		return m_description;
	}
	
	public void setDescription(String description) {
		m_description = description;
	}
	
	public int getLevel() {
		return m_level;
	}
	
	public void setLevel(int level) {
		m_level = level;
	}
	
	public int getPrepared() {
		return m_prepared;
	}
	
	public boolean isPrepared() {
		if(m_prepared >= 1)
			return true;
		else
			return false;
	}
	
	public void setPrepared(int prepared) {
		m_prepared = prepared;
	}
	
	public void setCharacterID(long characterId) {
		m_characterId = characterId;
	}
	
	public long getCharacterID() {
		return m_characterId;
	}
	
	@Override
	public void setID(long id) {
		m_id = id;
	}

	@Override
	public long getID() {
		return m_id;
	}

	@Override
	public int describeContents() {
		return 0;
	}
	
	public static final Parcelable.Creator<Spell> CREATOR = new Parcelable.Creator<Spell>() {
		public Spell createFromParcel(Parcel in) {
			return new Spell(in);
		}
		
		public Spell[] newArray(int size) {
			return new Spell[size];
		}
	};

    @Override
    public int compareTo(Spell another) {
        int comparison = this.getLevel() - another.getLevel();
        return (comparison != 0) ? comparison : this.getName().compareToIgnoreCase(another.getName());
    }
}
