package com.lateensoft.pathfinder.toolkit.model.character.stats;

import com.lateensoft.pathfinder.toolkit.db.repository.PTStorable;

import android.os.Parcel;
import android.os.Parcelable;

public class PTSave implements Parcelable, PTStorable {
	private long m_characterId;
	
	private int m_saveKey;

	private int m_baseSave;
	private int m_abilityKey;
	private int m_magicMod;
	private int m_miscMod;
	private int m_tempMod;
	
	public PTSave(int saveKey, int abilityKey) {
		this(saveKey, UNSET_ID, abilityKey);
	}
	
	public PTSave(int saveKey, long characterId, int abilityKey) {
		this(saveKey, characterId, 0, abilityKey, 0, 0, 0);
	}
	
	public PTSave(int saveKey, long characterId, int baseSave, int abilityKey, int magicMod,
			int miscMod, int tempMod) {
		m_characterId = characterId;
		m_saveKey = saveKey;
		m_abilityKey = abilityKey;
		m_baseSave = baseSave;
		m_magicMod = magicMod;
		m_miscMod = miscMod;
		m_tempMod = tempMod;
	}
	
	public PTSave(PTSave other) {
		m_saveKey = other.getSaveKey();
		m_abilityKey = other.getAbilityKey();
		m_magicMod = other.getMagicMod();
		m_miscMod = other.getMiscMod();
		m_tempMod = other.getTempMod();
		m_characterId = other.getCharacterID();
	}
	
	public PTSave(Parcel in) {
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
	
	public int getTotal(PTAbilitySet abilitySet, int maxDex) {
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
	
	public static final Parcelable.Creator<PTSave> CREATOR = new Parcelable.Creator<PTSave>() {
		public PTSave createFromParcel(Parcel in) {
			return new PTSave(in);
		}
		
		public PTSave[] newArray(int size) {
			return new PTSave[size];
		}
	};

	@Override
	public void setID(long id) {
		setSaveKey((int) id);
	}

	@Override
	public long getID() {
		return getSaveKey();
	}
}
