package com.lateensoft.pathfinder.toolkit.model.character.stats;

import com.lateensoft.pathfinder.toolkit.db.repository.PTStorable;

import android.os.Parcel;
import android.os.Parcelable;

public class PTSave extends PTStat implements Parcelable, PTStorable {
	private long m_characterId;

	int m_abilityMod;
	int m_magicMod;
	int m_miscMod;
	int m_tempMod;
	
	public PTSave() {
		super();
		m_abilityMod = 0;
		m_magicMod = 0;
		m_miscMod = 0;
		m_tempMod = 0;
		m_characterId = 0;
	}
	
	public PTSave(String name) {
		super(name);
		m_abilityMod = 0;
		m_magicMod = 0;
		m_miscMod = 0;
		m_tempMod = 0;
		m_characterId = 0;
	}
	
	public PTSave(long id, long characterId, String name, int baseValue, int abilityMod, int magicMod,
			int miscMod, int tempMod) {
		this(name, baseValue, abilityMod, magicMod, miscMod, tempMod, id);
		m_characterId = characterId;
	}
	
	public PTSave(long characterId, String name, int baseValue,
			int abilityMod, int magicMod, int miscMod, int tempMod) {
		this(name, baseValue, abilityMod, magicMod, miscMod, tempMod);
		m_characterId = characterId;
	}
	
	public PTSave(String name, int baseValue, int abilityMod, int magicMod,
			int miscMod, int tempMod) {
		super(name, baseValue);
		m_abilityMod = abilityMod;
		m_magicMod = magicMod;
		m_miscMod = miscMod;
		m_tempMod = tempMod;
		m_characterId = 0;
	}
	
	public PTSave(String name, int baseValue, int abilityMod, int magicMod,
			int miscMod, int tempMod, long id) {
		super(id, name, baseValue);
		m_abilityMod = abilityMod;
		m_magicMod = magicMod;
		m_miscMod = miscMod;
		m_tempMod = tempMod;
		m_characterId = 0;
	}
	
	public PTSave(String name, int baseValue, int[] modArray) {
		super(name, baseValue);
		
		if(modArray.length >= 4) {
			m_abilityMod = modArray[0];
			m_magicMod = modArray[1];
			m_miscMod = modArray[2];
			m_tempMod = modArray[3];
		}
		m_characterId = 0;
	}
	
	public PTSave(PTSave other) {
		super(other);
		m_abilityMod = other.getAbilityMod();
		m_magicMod = other.getMagicMod();
		m_miscMod = other.getMiscMod();
		m_tempMod = other.getTempMod();
		m_characterId = other.getCharacterID();
	}
	
	public PTSave(Parcel in) {
		super(in.readLong(), in.readString(), in.readInt());
		m_abilityMod = in.readInt();
		m_magicMod = in.readInt();
		m_miscMod = in.readInt();
		m_tempMod = in.readInt();
		m_characterId = in.readLong();
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeLong(getID());
		out.writeString(getName());
		out.writeInt(getBase());
		out.writeInt(m_abilityMod);
		out.writeInt(m_magicMod);
		out.writeInt(m_miscMod);
		out.writeInt(m_tempMod);
		out.writeLong(m_characterId);
	}
	
	public int getAbilityMod() {
		return m_abilityMod;
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
	
	public int getBase() {
		return super.getBaseValue();
	}
	
	public int getTotal() {
		int total = 0;
		total += super.getBaseValue();
		total += m_abilityMod;
		total += m_magicMod;
		total += m_miscMod;
		total += m_tempMod;
		return total;
	}
	
	public void setAbilityMod(int abilityMod) {
		m_abilityMod = abilityMod;
	}
	
	public void setBase(int base) {
		super.setBaseValue(base);
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
}
