package com.lateensoft.pathfinder.toolkit.character;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Representation for both Feats and Special  abilities
 * @author trevsiemens
 *
 */
public class PTFeat implements Parcelable{
	private String mName;
	private String mDescription;
	
	public PTFeat(){
		mName = "";
		mDescription = "";
	}
	
	public PTFeat(String name, String description){
		mName = new String(name);
		mDescription = new String (description);
	}
	
	public PTFeat(PTFeat otherFeat){
		mName = new String(otherFeat.getName());
		mDescription = new String (otherFeat.getDescription());
		
	}
	
	public PTFeat(Parcel in) {
		mName = in.readString();
		mDescription = in.readString();
	}
	
	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(mName);
		out.writeString(mDescription);
	}
	
	public void setName(String name){
		if(name != null){
			mName = name;
		}
	}
	
	public String getName(){
		return mName;
	}
	
	public void setDescription(String description){
		if(description != null){
			mDescription = description;
		}
	}
	
	public String getDescription(){
		return mDescription;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	public static final Parcelable.Creator<PTFeat> CREATOR = new Parcelable.Creator<PTFeat>() {
		public PTFeat createFromParcel(Parcel in) {
			return new PTFeat(in);
		}
		
		public PTFeat[] newArray(int size) {
			return new PTFeat[size];
		}
	};

}
