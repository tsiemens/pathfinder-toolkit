package com.lateensoft.pathfinder.toolkit.character;

import com.lateensoft.pathfinder.toolkit.R;

import android.content.Context;
import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;

public class PTFluffInfo implements Parcelable {
	String mName;
	String mAlignment;
	String mXP;
	String mNextLevelXP;
	String mXPChange;
	String mPlayerClass;
	String mRace;
	String mDeity;
	String mLevel;
	String mSize;
	String mGender;
	String mHeight;
	String mWeight;
	String mEyes;
	String mHair;
	String mLanguages;
	String mDescription;
	
	public PTFluffInfo() {
		mName = "";
		mAlignment = "";
		mXP = "";
		mNextLevelXP = "";
		mXPChange = "";
		mPlayerClass = "";
		mRace = "";
		mDeity = "";
		mLevel = "";
		mSize = "";
		mGender = "";
		mHeight = "";
		mWeight = new String("");
		mEyes = new String("");
		mHair = new String("");
		mLanguages = new String("");
		mDescription = new String("");
	}
	
	public PTFluffInfo(Parcel in) {
		mName = in.readString();
		mAlignment = in.readString();
		mXP = in.readString();
		mNextLevelXP = in.readString();
		mXPChange = in.readString();
		mPlayerClass = in.readString();
		mRace = in.readString();
		mDeity = in.readString();
		mLevel = in.readString();
		mSize = in.readString();
		mGender = in.readString();
		mHeight = in.readString();
		mWeight = in.readString();
		mEyes = in.readString();
		mHair = in.readString();
		mLanguages = in.readString();
		mDescription = in.readString();
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(mName);
		out.writeString(mAlignment);
		out.writeString(mXP);
		out.writeString(mNextLevelXP);
		out.writeString(mXPChange);
		out.writeString(mPlayerClass);
		out.writeString(mRace);
		out.writeString(mDeity);
		out.writeString(mLevel);
		out.writeString(mSize);
		out.writeString(mGender);
		out.writeString(mHeight);
		out.writeString(mWeight);
		out.writeString(mEyes);
		out.writeString(mHair);
		out.writeString(mLanguages);
		out.writeString(mDescription);
	}
	
	public String[] getFluffArray() {
		String[] fluffArray = {mName, mAlignment, mXP, mNextLevelXP, mPlayerClass, mRace, mDeity,
			mLevel, mSize, mGender, mHeight, mWeight, mEyes, mHair, mLanguages, mDescription};
		return fluffArray;
	}
	
	public void setFluffByIndex(int index, String fluffValue) {
		switch (index) {
		case 0:
			mName = fluffValue;
			break;
		case 1:
			mAlignment = fluffValue;
			break;
		case 2:
			mXP = fluffValue;
			break;
		case 3:
			mNextLevelXP = fluffValue;
			break;
		case 4:
			mPlayerClass = fluffValue;
			break;
		case 5:
			mRace = fluffValue;
			break;
		case 6:
			mDeity = fluffValue;
			break;
		case 7:
			mLevel = fluffValue;
			break;
		case 8:
			mSize = fluffValue;
			break;
		case 9:
			mGender = fluffValue;
			break;
		case 10:
			mHeight = fluffValue;
			break;
		case 11:
			mWeight = fluffValue;
			break;
		case 12:
			mEyes = fluffValue;
			break;
		case 13:
			mHair = fluffValue;
			break;
		case 14:
			mLanguages = fluffValue;
			break;
		case 15:
			mDescription = fluffValue;
			break;
		}
	}
	
	public String[] getFluffFields(Context context) {
		Resources r = context.getResources();
		
		return r.getStringArray(R.array.fluff_fields);		
	}
	

	public String getName() {
		return mName;
	}
	
	public void setName(String name) {
		mName = name;
	}
	
	public String getAlignment() {
		return mAlignment;
	}
	
	public void setAlignment(String alignment) {
		mAlignment = alignment;
	}
	
	public String getXP() {
		return mXP;
	}
	
	public void setXp(String XP) {
		mXP = XP;
	}
	
	public String getXPChange() {
		return mXPChange;
	}
	
	public void setXPChange(String XPChange) {
		mXPChange = XPChange;
	}
	
	public String getPlayerClass() {
		return mPlayerClass;
	}
	
	public void setPlayerClass(String playerClass) {
		mPlayerClass = playerClass;
	}
	
	public String getRace() {
		return mRace;
	}
	
	public String getDeity() {
		return mDeity;
	}
	
	public void setDeity(String deity) {
		mDeity = deity;
	}
	
	public String getLevel() {
		return mLevel;
	}
	
	public void setLevel(String level) {
		mLevel = level;
	}
	
	public String getGender() {
		return mGender;
	}
	
	public void setGender(String gender) {
		mGender = gender;
	}
	
	public String getHeight() {
		return mHeight;
	}
	
	public void setHeight(String height) {
		mHeight = height;
	}
	
	public String getWeight() {
		return mWeight;
	}
	
	public void setWeight(String weight) {
		mWeight = weight;
	}
	
	public String getEyes() {
		return mEyes;
	}
	
	public void setEyes(String eyes) {
		mEyes = eyes;
	}
	
	public String getHair() {
		return mHair;
	}
	
	public void setHair(String hair) {
		mHair = hair;
	}
	
	public void setRace(String race) {
		mRace = race;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	public static final Parcelable.Creator<PTFluffInfo> CREATOR = new Parcelable.Creator<PTFluffInfo>() {
		public PTFluffInfo createFromParcel(Parcel in) {
			return new PTFluffInfo(in);
		}
		
		public PTFluffInfo[] newArray(int size) {
			return new PTFluffInfo[size];
		}
	};
}
