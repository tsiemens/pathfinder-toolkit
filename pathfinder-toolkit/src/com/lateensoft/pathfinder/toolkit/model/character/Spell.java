package com.lateensoft.pathfinder.toolkit.model.character;

import com.lateensoft.pathfinder.toolkit.dao.Identifiable;

import android.os.Parcel;
import android.os.Parcelable;

public class Spell implements Parcelable, Identifiable, Comparable<Spell> {
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
		this(spell.getId(), spell.getCharacterID(), spell.getName(),
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
	public void setId(long id) {
		m_id = id;
	}

	@Override
	public long getId() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Spell)) return false;

        Spell spell = (Spell) o;

        if (m_characterId != spell.m_characterId) return false;
        if (m_id != spell.m_id) return false;
        if (m_level != spell.m_level) return false;
        if (m_prepared != spell.m_prepared) return false;
        if (m_description != null ? !m_description.equals(spell.m_description) : spell.m_description != null)
            return false;
        if (m_name != null ? !m_name.equals(spell.m_name) : spell.m_name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = m_name != null ? m_name.hashCode() : 0;
        result = 31 * result + m_prepared;
        result = 31 * result + m_level;
        result = 31 * result + (m_description != null ? m_description.hashCode() : 0);
        result = 31 * result + (int) (m_id ^ (m_id >>> 32));
        result = 31 * result + (int) (m_characterId ^ (m_characterId >>> 32));
        return result;
    }
}
