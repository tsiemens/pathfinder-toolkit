package com.lateensoft.pathfinder.toolkit.stats;

import android.os.Parcel;
import android.os.Parcelable;

public class PTSave extends PTStat {
	int mTotal;
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
		calculateTotal();
	}
	
	public PTSave(String name) {
		super(name);
		mAbilityMod = 0;
		mMagicMod = 0;
		mMiscMod = 0;
		mTempMod = 0;
		calculateTotal();
	}
	
	public PTSave(String name, int baseValue, int abilityMod, int magicMod,
			int miscMod, int tempMod) {
		super(name, baseValue);
		mAbilityMod = abilityMod;
		mMagicMod = magicMod;
		mMiscMod = miscMod;
		mTempMod = tempMod;
	}
	
	public PTSave(String name, int baseValue, int[] modArray) {
		super(name, baseValue);
		
		if(modArray.length >= 4) {
			mAbilityMod = modArray[0];
			mMagicMod = modArray[1];
			mMiscMod = modArray[2];
			mTempMod = modArray[3];
		}
	}
	
	public PTSave(PTSave other) {
		super(other);
		mAbilityMod = other.getAbilityMod();
		mMagicMod = other.getMagicMod();
		mMiscMod = other.getMiscMod();
		mTempMod = other.getTempMod();
		calculateTotal();
	}
	
	public PTSave(Parcel in) {
		super(in);
		mAbilityMod = in.readInt();
		mMagicMod = in.readInt();
		mMiscMod = in.readInt();
		mTempMod = in.readInt();
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		super.writeToParcel(out, flags);
		out.writeInt(mAbilityMod);
		out.writeInt(mMagicMod);
		out.writeInt(mMiscMod);
		out.writeInt(mTempMod);
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
		calculateTotal();
		return mTotal;
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
	
	private void calculateTotal() {
		mTotal = 0;
		mTotal += super.getBaseValue();
		mTotal += mAbilityMod;
		mTotal += mMagicMod;
		mTotal += mMiscMod;
		mTotal += mTempMod;
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
