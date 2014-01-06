package com.lateensoft.pathfinder.toolkit.items;

import android.os.Parcel;
import android.os.Parcelable;

public class PTArmor extends PTItem {
	boolean mWorn;
	int mACBonus;
	int mCheckPen;
	int mMaxDex;
	int mSpellFail;
	int mSpeed;
	String mSpecialProperties;
	String mSize;
	
	public PTArmor() {
		super();
		mWorn =  true;
		mACBonus = 0;
		mCheckPen = 0;
		mMaxDex = 10;
		mSpellFail = 0;
		mSpeed = 30;
		mSpecialProperties = new String("");
		mSize = "M";
	}
	
	public PTArmor(Parcel in) {
		super(in);
		boolean[] worn = new boolean[1];
		in.readBooleanArray(worn);
		mWorn = worn[0];
		mACBonus = in.readInt();
		mCheckPen = in.readInt();
		mMaxDex = in.readInt();
		mSpellFail = in.readInt();
		mSpeed = in.readInt();
		mSpecialProperties = in.readString();
		mSize = in.readString();
	}
	
	@Override
	public void writeToParcel(Parcel out, int flags) {
		super.writeToParcel(out, flags);
		boolean[] worn = new boolean[1];
		worn[0] = mWorn;
		out.writeBooleanArray(worn);
		out.writeInt(mACBonus);
		out.writeInt(mCheckPen);
		out.writeInt(mMaxDex);
		out.writeInt(mSpellFail);
		out.writeInt(mSpeed);
		out.writeString(mSpecialProperties);
		out.writeString(mSize);
	}
	
	public void setSize(String size) {
		mSize = size;
	}
	
	public String getSize() {
		return mSize;
	}
	
	public void setSpecialProperties(String properties) {
		mSpecialProperties = properties;
	}
	
	public String getSpecialProperties() {
		return mSpecialProperties;
	}
		
	/**
	 * @return the mWorn
	 */
	public boolean isWorn() {
		return mWorn;
	}
	/**
	 * @param mWorn the mWorn to set
	 */
	public void setWorn(boolean mWorn) {
		this.mWorn = mWorn;
	}
	/**
	 * @return the mACBonus
	 */
	public int getACBonus() {
		return mACBonus;
	}
	/**
	 * @param mACBonus the mACBonus to set
	 */
	public void setACBonus(int mACBonus) {
		this.mACBonus = mACBonus;
	}
	/**
	 * @return the mCheckPen
	 */
	public int getCheckPen() {
		return mCheckPen;
	}
	/**
	 * @param mCheckPen the mCheckPen to set
	 */
	public void setCheckPen(int mCheckPen) {
		this.mCheckPen = mCheckPen;
	}
	/**
	 * @return the mMaxDex
	 */
	public int getMaxDex() {
		return mMaxDex;
	}
	/**
	 * @param mMaxDex the mMaxDex to set
	 */
	public void setMaxDex(int mMaxDex) {
		this.mMaxDex = mMaxDex;
	}
	/**
	 * @return the mSpellFail
	 */
	public int getSpellFail() {
		return mSpellFail;
	}
	/**
	 * @param mSpellFail the mSpellFail to set
	 */
	public void setSpellFail(int mSpellFail) {
		this.mSpellFail = mSpellFail;
	}
	/**
	 * @return the mSpeed
	 */
	public int getSpeed() {
		return mSpeed;
	}
	/**
	 * @param mSpeed the mSpeed to set
	 */
	public void setSpeed(int mSpeed) {
		this.mSpeed = mSpeed;
	}
	
	public String getSpeedString() {
		String speedString = new String();
		speedString = Integer.toString(mSpeed) + " ft.";
		return speedString;
	}

	public int getSizeInt() {
		String[] sizeArray = {"S", "M", "L"};
		for(int i = 0; i < sizeArray.length; i++) {
			if(mSize.equals(sizeArray[i]))
				return i;
		}
		return 1;
	}

	public void setSize(int size) {
		String[] sizeArray = {"S", "M", "L"};
		mSize = sizeArray[size];
	}
	
	public static final Parcelable.Creator<PTArmor> CREATOR = new Parcelable.Creator<PTArmor>() {
		public PTArmor createFromParcel(Parcel in) {
			return new PTArmor(in);
		}
		
		public PTArmor[] newArray(int size) {
			return new PTArmor[size];
		}
	};
}
