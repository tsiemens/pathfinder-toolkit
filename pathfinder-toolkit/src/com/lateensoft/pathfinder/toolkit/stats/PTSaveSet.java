package com.lateensoft.pathfinder.toolkit.stats;

import com.lateensoft.pathfinder.toolkit.R;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class PTSaveSet implements Parcelable{
	private static final String PARCEL_BUNDLE_KEY_SAVES = "saves";
	
	PTSave[] mSaves;
	
	public PTSaveSet(Context context) {
		Resources r = context.getResources();
		String[] names = r.getStringArray(R.array.save_names);
		
		mSaves = new PTSave[3];
		for(int i = 0; i < mSaves.length; i++) {
			mSaves[i] = new PTSave(names[i]);
		}
	}
	
	public PTSaveSet(Parcel in) {
		Bundle objectBundle = in.readBundle();
		mSaves = (PTSave[]) objectBundle.getParcelableArray(PARCEL_BUNDLE_KEY_SAVES);
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		Bundle objectBundle = new Bundle();
		objectBundle.putParcelableArray(PARCEL_BUNDLE_KEY_SAVES, mSaves);
		out.writeBundle(objectBundle);
	}
	
	public PTSave getSave(int index) {
		return mSaves[index];
	}
	
	public void setAbilityMods(PTAbilitySet abilities, Context context) {
		Resources r = context.getResources();
		
		mSaves[r.getInteger(R.integer.key_fort_save)].setAbilityMod(
				abilities.getAbilityScore(R.integer.key_constitution).getModifier());
		mSaves[r.getInteger(R.integer.key_ref_save)].setAbilityMod(
				abilities.getAbilityScore(R.integer.key_dexterity).getModifier());
		mSaves[r.getInteger(R.integer.key_will_save)].setAbilityMod(
				abilities.getAbilityScore(R.integer.key_wisdom).getModifier());
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	public static final Parcelable.Creator<PTSaveSet> CREATOR = new Parcelable.Creator<PTSaveSet>() {
		public PTSaveSet createFromParcel(Parcel in) {
			return new PTSaveSet(in);
		}
		
		public PTSaveSet[] newArray(int size) {
			return new PTSaveSet[size];
		}
	};
}
