package com.lateensoft.pathfinder.toolkit.model.character;

import java.util.ArrayList;
import java.util.Arrays;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class PTFeatList implements Parcelable {
	private static final String PARCEL_BUNDLE_KEY_FEATS = "feats";
	
	private ArrayList<PTFeat> m_feats;
	
	public PTFeatList(){
		m_feats = new ArrayList<PTFeat>();
	}
	
	public PTFeatList(PTFeat[] feats) {
		m_feats = new ArrayList<PTFeat>(Arrays.asList(feats));
	}
	
	public PTFeatList(Parcel in) {
		Bundle objectBundle = in.readBundle();
		m_feats = objectBundle.getParcelableArrayList(PARCEL_BUNDLE_KEY_FEATS);
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		Bundle objectBundle = new Bundle();
		objectBundle.putParcelableArrayList(PARCEL_BUNDLE_KEY_FEATS, m_feats);
		out.writeBundle(objectBundle);
	}
	
	public void setCharacterID(long id) {
		for (PTFeat feat : m_feats) {
			feat.setCharacterID(id);
		}
	}
	
	/**
	 * adds the feat to the list, if there is no feat by the same name
	 * @param newFeat
	 */
	public void addFeat(PTFeat newFeat){
		if( newFeat != null ){
			//If the feat already exists do nothing
			int i = getIndexOf(newFeat.getName());
			if(i >= 0){
				return;
			}
			
			for(i = 0; i < m_feats.size(); i++){
				//Places in alphabetical position
				if(newFeat.getName().compareToIgnoreCase(getFeat(i).getName()) < 0 ){
					m_feats.add(i, newFeat);
					return;
				}
			}
			//If item is to go at the end of the list
			m_feats.add(newFeat);
		}
	}
	
	/**
	 * Deletes the feat at index.
	 * @param index
	 */
	public void deleteFeat(int index){
		if(index >= 0 && index < m_feats.size())
			m_feats.remove(index);
	}
	
	/**
	 * Warning: list will become unsorted if the name is changed.
	 * @param index
	 * @return the feat at index
	 */
	public PTFeat getFeat(int index){
		if(index >= 0 && index < m_feats.size())
			return m_feats.get(index);
		else return null; 
	}
	
	/**
	 * Replaces the feat at index with newFeat. If index is outside the bounds of this, or null, it will do nothing. Resorts the list alphabetically
	 * @param newFeat
	 * @param index
	 */
	public void setFeat(PTFeat newFeat, int index){
		if(index >= 0 && index < m_feats.size() && newFeat != null){
			deleteFeat(index);
			addFeat(newFeat);
		}
	}
	
	/**
	 * returns an array of all the feats in the list
	 * @return an array of PTFeat objects
	 */
	public PTFeat[] getFeats(){
		return m_feats.toArray(new PTFeat[m_feats.size()]);		
	}
	
	/**
	 * returns an array of all the feats names in the list
	 * @return an array of PTFeat objects
	 */
	public String[] getFeatNames(){
		String[] featNames = new String[m_feats.size()];
		for(int i = 0; i < m_feats.size(); i++){
			featNames[i] = new String(m_feats.get(i).getName());
		}
		return featNames;		
	}
	
	/**
	 * Sets the description of the feat at index
	 * @param index
	 * @param quantity
	 */
	public void setDescription(int index, String description){
		if(index >= 0 && index < m_feats.size() && description != null)
			m_feats.get(index).setDescription(description);
	}

	
	/**
	 * 
	 * @param featName
	 * @return the index of the object with itemName. returns -1 if the item is not in the inventory.
	 */
	public int getIndexOf(String featName){
		for(int i = 0; i < m_feats.size(); i++){
			if(featName.contentEquals(m_feats.get(i).getName())){
				return i;
			}
		}
		return -1;
	}
	
	public int getNumberOfFeats(){
		return m_feats.size();
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
