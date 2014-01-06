package com.lateensoft.pathfinder.toolkit.model.character.stats;

import com.lateensoft.pathfinder.toolkit.db.repository.PTStorable;

import android.os.Parcel;
import android.os.Parcelable;

public class PTSave extends PTStat implements Parcelable, PTStorable {
	private long mCharacterId;

	int mAbilityMod;
	int mMagicMod;
	int mMiscMod;
	int mTempMod;
	
	public PTSave() {
		super();
		mAbilityMod = 0;
		mMagicMod = 0;
		mMiscMod = 0;
		mTempMod = 0;
		mCharacterId = 0;
	}
	
	public PTSave(String name) {
		super(name);
		mAbilityMod = 0;
		mMagicMod = 0;
		mMiscMod = 0;
		mTempMod = 0;
		mCharacterId = 0;
	}
	
	public PTSave(int id, long characterId, String name, int baseValue, int abilityMod, int magicMod,
			int miscMod, int tempMod) {
		this(name, baseValue, abilityMod, magicMod, miscMod, tempMod, id);
		mCharacterId = characterId;
	}
	
	public PTSave(long characterId, String name, int baseValue,
			int abilityMod, int magicMod, int miscMod, int tempMod) {
		this(name, baseValue, abilityMod, magicMod, miscMod, tempMod);
		mCharacterId = characterId;
	}
	
	public PTSave(String name, int baseValue, int abilityMod, int magicMod,
			int miscMod, int tempMod) {
		super(name, baseValue);
		mAbilityMod = abilityMod;
		mMagicMod = magicMod;
		mMiscMod = miscMod;
		mTempMod = tempMod;
		mCharacterId = 0;
	}
	
	public PTSave(String name, int baseValue, int abilityMod, int magicMod,
			int miscMod, int tempMod, int id) {
		super(id, name, baseValue);
		mAbilityMod = abilityMod;
		mMagicMod = magicMod;
		mMiscMod = miscMod;
		mTempMod = tempMod;
		mCharacterId = 0;
	}
	
	public PTSave(String name, int baseValue, int[] modArray) {
		super(name, baseValue);
		
		if(modArray.length >= 4) {
			mAbilityMod = modArray[0];
			mMagicMod = modArray[1];
			mMiscMod = modArray[2];
			mTempMod = modArray[3];
		}
		mCharacterId = 0;
	}
	
	public PTSave(PTSave other) {
		super(other);
		mAbilityMod = other.getAbilityMod();
		mMagicMod = other.getMagicMod();
		mMiscMod = other.getMiscMod();
		mTempMod = other.getTempMod();
		mCharacterId = other.getCharacterID();
	}
	
	public PTSave(Parcel in) {
		super(in.readLong(), in.readString(), in.readInt());
		mAbilityMod = in.readInt();
		mMagicMod = in.readInt();
		mMiscMod = in.readInt();
		mTempMod = in.readInt();
		mCharacterId = in.readLong();
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeLong(getID());
		out.writeString(getName());
		out.writeInt(getBase());
		out.writeInt(mAbilityMod);
		out.writeInt(mMagicMod);
		out.writeInt(mMiscMod);
		out.writeInt(mTempMod);
		out.writeLong(mCharacterId);
	}
	
	public int getAbilityMod() {
		return mAbilityMod;
	}
	
	public int getMagicMod() {
		return mMagicMod;
	}
	
	public int getMiscMod() {
		return mMiscMod;
	}
	
	public int getTempMod() {
		return mTempMod;
	}
	
	public int getBase() {
		return super.getBaseValue();
	}
	
	public int getTotal() {
		int total = 0;
		total += super.getBaseValue();
		total += mAbilityMod;
		total += mMagicMod;
		total += mMiscMod;
		total += mTempMod;
		return total;
	}
	
	public void setAbilityMod(int abilityMod) {
		mAbilityMod = abilityMod;
	}
	
	public void setBase(int base) {
		super.setBaseValue(base);
	}
	
	public void setMagicMod(int magicMod) {
		mMagicMod = magicMod;
	}
	
	public void setMiscMod(int miscMod) {
		mMiscMod = miscMod;
	}
	
	public void setTempMod(int tempMod) {
		mTempMod = tempMod;
	}
	
	@Override
	public Long getCharacterID() {
		return mCharacterId;
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
