package com.lateensoft.pathfinder.toolkit.model.character;

import com.lateensoft.pathfinder.toolkit.db.repository.PTStorable;

import android.os.Parcel;
import android.os.Parcelable;

public class PTSpell implements Parcelable, PTStorable {
	@SuppressWarnings("unused")
	private static final String TAG = PTSpell.class.getSimpleName();
	
	private String m_name;
	private int m_prepared;
	private int m_level;
	private String m_description;
	
	private long m_id;
	private long m_characterId;
	
	public PTSpell() {
		this("");
	}
	
	public PTSpell(String name) {
		this(name, 0);
	}
	
	public PTSpell(String name, int level) {
		this(UNSET_ID, name, level, 0, "");
	}
	
	public PTSpell(long characterId, String name, int level, int prepared, String description) {
		this(UNSET_ID, characterId, name, level, prepared, description);
	}
	
	public PTSpell(long id, long characterId, String name, int level, int prepared, String description) {
		m_id = id;
		m_characterId = characterId;
		m_name = name;
		m_level = level;
		m_prepared = prepared;
		m_description = description;
	}
	
	public PTSpell(PTSpell spell) {
		this(spell.getID(), spell.getCharacterID(), spell.getName(),
				spell.getLevel(), spell.getPrepared(), spell.getDescription());
	}
	
	public PTSpell(Parcel in) {
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
	
	public void setAsOtherSpell(PTSpell spell) {
		m_name = spell.getName();
		m_level = spell.getLevel();
		m_prepared = spell.getPrepared();
		m_description = spell.getDescription();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((m_description == null) ? 0 : m_description.hashCode());
		result = prime * result + m_level;
		result = prime * result + ((m_name == null) ? 0 : m_name.hashCode());
		result = prime * result + m_prepared;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PTSpell other = (PTSpell) obj;
		if (m_description == null) {
			if (other.m_description != null)
				return false;
		} else if (!m_description.equals(other.m_description))
			return false;
		if (m_level != other.m_level)
			return false;
		if (m_name == null) {
			if (other.m_name != null)
				return false;
		} else if (!m_name.equals(other.m_name))
			return false;
		if (m_prepared != other.m_prepared)
			return false;
		return true;
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
	
	public static final Parcelable.Creator<PTSpell> CREATOR = new Parcelable.Creator<PTSpell>() {
		public PTSpell createFromParcel(Parcel in) {
			return new PTSpell(in);
		}
		
		public PTSpell[] newArray(int size) {
			return new PTSpell[size];
		}
	};
	
}
