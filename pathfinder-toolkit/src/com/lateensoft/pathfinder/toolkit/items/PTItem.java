package com.lateensoft.pathfinder.toolkit.items;

import android.os.Parcel;
import android.os.Parcelable;

public class PTItem implements Parcelable {
	String mName;
	double mWeight;
	int mQuantity;
	boolean mIsContained; //set if in a container such as a bag of holding (will be used to set effective weight to 0)
	
	public PTItem(String name, double weight, int quantity, boolean contained) {
		mName = name;
		mWeight = weight;
		mQuantity = quantity;
		mIsContained = contained;
	}
	
	/**
	 * Creates new instance of an Item. Defaults quantity to 1 and contained to false. 
	 * @param name
	 * @param weight
	 */
	public PTItem(String name, double weight) {
		mName = name;
		mWeight = weight;
		mQuantity = 1;
		mIsContained = false;
	}
	
	public PTItem() {
		mName = "";
		mWeight = 1;
		mQuantity = 1;
	}
	
	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(mName);
		out.writeDouble(mWeight);
		out.writeInt(mQuantity);
		boolean[] contained = new boolean[1];
		contained[0] = mIsContained;
		out.writeBooleanArray(contained);
	}
	
	public PTItem(Parcel in) {
		mName = in.readString();
		mWeight = in.readDouble();
		mQuantity = in.readInt();
		boolean[] contained = new boolean[1];
		in.readBooleanArray(contained);
		mIsContained = contained[0];
	}
	
	public String getName() {
		return mName;
	}
	
	public void setName(String name) {
		mName = name;
	}
	
	public double getWeight() {
		return mWeight;
	}
	
	public void setWeight(double weight) {
		mWeight = weight;
	}
	
	public int getQuantity() {
		return mQuantity;
	}
	
	public void setQuantity(int quantity) {
		mQuantity = quantity;
	}
	
	public boolean isContained(){
		return mIsContained;
	}
	
	public void setIsContained(boolean isContained){
		mIsContained = isContained;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<PTItem> CREATOR = new Parcelable.Creator<PTItem>() {
		public PTItem createFromParcel(Parcel in) {
			return new PTItem(in);
		}
		
		public PTItem[] newArray(int size) {
			return new PTItem[size];
		}
	};
}
