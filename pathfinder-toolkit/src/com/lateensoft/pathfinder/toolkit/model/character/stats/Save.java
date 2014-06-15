package com.lateensoft.pathfinder.toolkit.model.character.stats;

import com.lateensoft.pathfinder.toolkit.db.dao.Identifiable;

import android.os.Parcel;
import android.os.Parcelable;

public class Save implements Parcelable, Identifiable {
	private long m_characterId;
	
	private int m_saveKey;

	private int m_baseSave;
	private int m_abilityKey;
	private int m_magicMod;
	private int m_miscMod;
	private int m_tempMod;
	
	public Save(int saveKey, int abilityKey) {
		this(saveKey, UNSET_ID, abilityKey);
	}
	
	public Save(int saveKey, long characterId, int abilityKey) {
		this(saveKey, characterId, 0, abilityKey, 0, 0, 0);
	}
	
	public Save(int saveKey, long characterId, int baseSave, int abilityKey, int magicMod,
                int miscMod, int tempMod) {
		m_characterId = characterId;
		m_saveKey = saveKey;
		m_abilityKey = abilityKey;
		m_baseSave = baseSave;
		m_magicMod = magicMod;
		m_miscMod = miscMod;
		m_tempMod = tempMod;
	}
	
	public Save(Save other) {
		m_saveKey = other.getSaveKey();
		m_abilityKey = other.getAbilityKey();
		m_magicMod = other.getMagicMod();
		m_miscMod = other.getMiscMod();
		m_tempMod = other.getTempMod();
		m_characterId = other.getCharacterID();
	}
	
	public Save(Parcel in) {
		m_saveKey = in.readInt();
		m_baseSave = in.readInt();
		m_abilityKey = in.readInt();
		m_magicMod = in.readInt();
		m_miscMod = in.readInt();
		m_tempMod = in.readInt();
		m_characterId = in.readLong();
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(m_saveKey);
		out.writeInt(m_baseSave);
		out.writeInt(m_abilityKey);
		out.writeInt(m_magicMod);
		out.writeInt(m_miscMod);
		out.writeInt(m_tempMod);
		out.writeLong(m_characterId);
	}
	
	public int getBaseSave() {
		return m_baseSave;
	}

	public void setBaseSave(int baseSave) {
		m_baseSave = baseSave;
	}

	public int getAbilityKey() {
		return m_abilityKey;
	}
	
	public int getMagicMod() {
		return m_magicMod;
	}
	
	public int getMiscMod() {
		return m_miscMod;
	}
	
	public int getTempMod() {
		return m_tempMod;
	}
	
	public int getTotal(AbilitySet abilitySet, int maxDex) {
		int total = 0;
		total += m_baseSave;
		total += abilitySet.getTotalAbilityMod(m_abilityKey, maxDex);
		total += m_magicMod;
		total += m_miscMod;
		total += m_tempMod;
		return total;
	}
	
	public void setAbilityKey(int abilityKey) {
		m_abilityKey = abilityKey;
	}
	
	public void setMagicMod(int magicMod) {
		m_magicMod = magicMod;
	}
	
	public void setMiscMod(int miscMod) {
		m_miscMod = miscMod;
	}
	
	public void setTempMod(int tempMod) {
		m_tempMod = tempMod;
	}
	
	public int getSaveKey() {
		return m_saveKey;
	}
	
	public void setSaveKey(int saveKey) {
		m_saveKey = saveKey;
	}
	
	public void setCharacterID(long id) {
		m_characterId = id;
	}
	
	public long getCharacterID() {
		return m_characterId;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	public static final Parcelable.Creator<Save> CREATOR = new Parcelable.Creator<Save>() {
		public Save createFromParcel(Parcel in) {
			return new Save(in);
		}
		
		public Save[] newArray(int size) {
			return new Save[size];
		}
	};

	@Override
	public void setId(long id) {
		setSaveKey((int) id);
	}

	@Override
	public long getId() {
		return getSaveKey();
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Save)) return false;

        Save save = (Save) o;

        if (m_abilityKey != save.m_abilityKey) return false;
        if (m_baseSave != save.m_baseSave) return false;
        if (m_characterId != save.m_characterId) return false;
        if (m_magicMod != save.m_magicMod) return false;
        if (m_miscMod != save.m_miscMod) return false;
        if (m_saveKey != save.m_saveKey) return false;
        if (m_tempMod != save.m_tempMod) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (m_characterId ^ (m_characterId >>> 32));
        result = 31 * result + m_saveKey;
        result = 31 * result + m_baseSave;
        result = 31 * result + m_abilityKey;
        result = 31 * result + m_magicMod;
        result = 31 * result + m_miscMod;
        result = 31 * result + m_tempMod;
        return result;
    }
}
