package com.lateensoft.pathfinder.toolkit.model.character;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class PTFeatList  extends ArrayList<PTFeat> implements Parcelable {
	public PTFeatList(){
		super();
	}
	
	public PTFeatList(Collection<? extends PTFeat> feats) {
		super(feats);
	}
	
	public PTFeatList(Parcel in) {
        in.readTypedList(this, PTFeat.CREATOR);
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
        out.writeTypedList(this);
	}
	
	public void setCharacterID(long id) {
		for (PTFeat feat : this) {
			feat.setCharacterID(id);
		}
	}

	@Override
	public int describeContents() {
		return 0;
	}
	
	public static final Parcelable.Creator<PTFeatList> CREATOR = new Parcelable.Creator<PTFeatList>() {
		public PTFeatList createFromParcel(Parcel in) {
			return new PTFeatList(in);
		}
		
		public PTFeatList[] newArray(int size) {
			return new PTFeatList[size];
		}
	};
}
