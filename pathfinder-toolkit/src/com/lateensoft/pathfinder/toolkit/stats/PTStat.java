package com.lateensoft.pathfinder.toolkit.stats;

import android.os.Parcel;
import android.os.Parcelable;

public class PTStat implements Parcelable{
	private String mName;
	private int mBaseValue;

	public PTStat() {
		mName = "";
		mBaseValue = 0;
	}
	
	public PTStat(String name) {
		mName = name;
		mBaseValue = 0;
	}
	
	public PTStat(String name, int baseValue) {
		mName = name;
		mBaseValue = baseValue;
	}
	
	public PTStat(PTStat other) {
		mName = other.getName();
		mBaseValue = other.getBaseValue();
	}
	
	public PTStat(Parcel in) {
		mName = in.readString();
		mBaseValue = in.readInt();
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(mName);
		out.writeInt(mBaseValue);
	}
	
	public String getName() {
		return mName;
	}
	
	public int getBaseValue() {
		return mBaseValue;
	}
	
	public void setName(String name) {
		mName = name;
	}
	
	public void setBaseValue(int baseValue) {
		mBaseValue = baseValue;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	public static final Parcelable.Creator<PTStat> CREATOR = new Parcelable.Creator<PTStat>() {
		public PTStat createFromParcel(Parcel in) {
			return new PTStat(in);
		}
		
		public PTStat[] newArray(int size) {
			return new PTStat[size];
		}
	};
}
