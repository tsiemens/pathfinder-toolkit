package com.lateensoft.pathfinder.toolkit.model.character;

import android.os.Parcel;
import android.os.Parcelable;

public class PTSpell implements Parcelable{
	static final String TAG = PTSpell.class.getSimpleName();
	String mName;
	int mPrepared;
	int mLevel;
	String mDescription;
	
	public PTSpell() {
		mName = new String("");
		mLevel = 0;
		mPrepared = 0;
		mDescription = new String("");
	}
	
	public PTSpell(String name) {
		this();
		mName = name;
	}
	
	public PTSpell(String name, int level) {
		this(name);
		mLevel = level;
	}
	
	/**
	 * 
	 * @param name
	 * @param level
	 * @param prepared
	 * @param description
	 */
	public PTSpell(String name, int level, int prepared, String description) {
		this(name, level);
		mPrepared = prepared;
		mDescription = description;
	}
	
	public PTSpell(PTSpell spell) {
		this(spell.getName(), spell.getLevel(), spell.getPrepared(), spell.getDescription());
	}
	
	public PTSpell(Parcel in) {
		mName = in.readString();
		mLevel = in.readInt();
		mPrepared = in.readInt();
		mDescription = in.readString();
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(mName);
		out.writeInt(mLevel);
		out.writeInt(mPrepared);
		out.writeString(mDescription);	
	}
	
	public String getName() {
		return mName;
	}
	
	public void setName(String name) {
		mName = name;
	}
	
	public String getDescription() {
		return mDescription;
	}
	
	public void setDescription(String description) {
		mDescription = description;
	}
	
	public int getLevel() {
		return mLevel;
	}
	
	public void setLevel(int level) {
		mLevel = level;
	}
	
	public int getPrepared() {
		return mPrepared;
	}
	
	public boolean isPrepared() {
		if(mPrepared >= 1)
			return true;
		else
			return false;
	}
	
	public void setPrepared(int prepared) {
		mPrepared = prepared;
	}
	
	public void setAsOtherSpell(PTSpell spell) {
		mName = spell.getName();
		mLevel = spell.getLevel();
		mPrepared = spell.getPrepared();
		mDescription = spell.getDescription();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((mDescription == null) ? 0 : mDescription.hashCode());
		result = prime * result + mLevel;
		result = prime * result + ((mName == null) ? 0 : mName.hashCode());
		result = prime * result + mPrepared;
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
		if (mDescription == null) {
			if (other.mDescription != null)
				return false;
		} else if (!mDescription.equals(other.mDescription))
			return false;
		if (mLevel != other.mLevel)
			return false;
		if (mName == null) {
			if (other.mName != null)
				return false;
		} else if (!mName.equals(other.mName))
			return false;
		if (mPrepared != other.mPrepared)
			return false;
		return true;
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
