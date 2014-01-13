package com.lateensoft.pathfinder.toolkit.model.party;

import java.util.ArrayList;
import java.util.Arrays;

import com.lateensoft.pathfinder.toolkit.db.repository.PTStorable;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class PTParty implements Parcelable, PTStorable {	
	@SuppressWarnings("unused")
	private final String TAG = PTParty.class.getSimpleName();
	private static final String PARCEL_BUNDLE_KEY_MEMBERS = "party_members";
	
	private ArrayList<PTPartyMember> m_partyMembers;
	private String m_partyName;
	
	private long m_id;
	
	public PTParty(String name){
		this(UNSET_ID, name);
	}
	
	public PTParty(long id, String name) {
		this(id, name, new PTPartyMember[0]);
	}
	
	public PTParty(long id, String name, PTPartyMember[] partyMembers) {
		m_id = id;
		
		if(name != null)
			m_partyName = name;
		else m_partyName = "";
		
		m_partyMembers = new ArrayList<PTPartyMember>(Arrays.asList(partyMembers));
	}
	
	public PTParty(Parcel in) {
		Bundle objectBundle = in.readBundle();
		m_partyMembers = objectBundle.getParcelableArrayList(PARCEL_BUNDLE_KEY_MEMBERS);
		m_partyName = in.readString();
		m_id = in.readLong();
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		Bundle objectBundle = new Bundle();
		objectBundle.putParcelableArrayList(PARCEL_BUNDLE_KEY_MEMBERS, m_partyMembers);
		out.writeBundle(objectBundle);
		out.writeString(m_partyName);
		out.writeLong(m_id);
	}
	
	/**
	 * Adds party member into the party. The party member is inserted into the list alphabetically.
	 * @param newPartyMember
	 * @return the index of the character in the list. -1 if the addition failed
	 */
	public int addPartyMember(PTPartyMember newPartyMember){
		if( newPartyMember != null ){
			
			for(int i = 0; i < m_partyMembers.size(); i++){
				//Places in alphabetical position
				if(newPartyMember.getName().compareToIgnoreCase(getPartyMember(i).getName()) < 0 ){
					m_partyMembers.add(i, newPartyMember);
					return i;
				}
			}
			//If party member is to go at the end of the list
			m_partyMembers.add(newPartyMember);
			return m_partyMembers.size() - 1;
		}
		else return -1;
	}
	
	/**
	 * 
	 * @param index
	 * @return returns the party member at index, in an alphabetical list of all party members
	 */
	public PTPartyMember getPartyMember(int index){
		if(index >= 0 && index < m_partyMembers.size())
			return m_partyMembers.get(index);
		else return null;
	}
	
	/**
	 * Replaces the party member at i with the entered one. 
	 * The list remains alphabetical, even if the entered party member's name is of different priority than the one previously at i.
	 * @param i
	 * @param partyMember
	 * @return the new index of the character in the list. -1 if the set failed
	 */
	public int setPartyMember(int index, PTPartyMember partyMember){
		if(index >= 0 && index < m_partyMembers.size() && partyMember != null){
			m_partyMembers.remove(index);
			return addPartyMember(partyMember);
		}
		else return -1;
	}
	
	/**
	 * deletes the party member at index
	 * @param index
	 */
	public void deletePartyMember(int index){
		if(index >= 0 && index < m_partyMembers.size())
			m_partyMembers.remove(index);
	}
	
	/**
	 * 
	 * @return an array of all the party members' names
	 */
	public String[] getPartyMemberNames(){
		String names[] = new String[m_partyMembers.size()];
		for(int i = 0; i < m_partyMembers.size(); i++){
			names[i] = m_partyMembers.get(i).getName();
		}
		
		return names;
	}
	
	public void setName(String name){
		if(name != null)
			m_partyName = name;
	}
	
	public String getName(){
		return m_partyName;
	}
	
	/**
	 * 
	 * @return an array of the names of party members, ordered from highest roll, to lowest. 
	 * If there is a tie, sorts by default order
	 */
	public String[] getNamesByRollValue(){
		PTPartyMember[] orderedMembers = getPartyMembersByRollValue();
		String[] names = new String[m_partyMembers.size()];
		
		//Set each value in the array
		for(int i = 0; i < m_partyMembers.size(); i++){
			names[i] = orderedMembers[i].getName();
		}	
		return names;
	}
	
	/**
	 * 
	 * @return an array of the roll values of party members, ordered from highest roll, to lowest. 
	 * If there is a tie, sorts by default order
	 */
	public int[] getRollValuesByRollValue(){
		PTPartyMember[] orderedMembers = getPartyMembersByRollValue();
		int[] rollValues = new int[m_partyMembers.size()];
		
		//Set each value in the array
		for(int i = 0; i < m_partyMembers.size(); i++){
			rollValues[i] = orderedMembers[i].getLastRolledValue();	
		}	
		return rollValues;
	}
	
	
	/**
	 * 
	 * @param index
	 * @return the "actual" index of the party, based on its index in the list sorted by roll
	 */
	public int getPartyMemberIndexByRollValueIndex(int index){
		if(index < m_partyMembers.size()){
			int[] indexes = getIndexesByRollValue();
			return indexes[index];	
		}
		else return -1;
	}
	
	/** 
	 * @return an array of party members, ordered from highest roll, to lowest. 
	 * If there is a tie, sorts by default order
	 */
	private PTPartyMember[] getPartyMembersByRollValue(){
		ArrayList<PTPartyMember> tempMembers = cloneMemberList();
		PTPartyMember[] sortedMembers = new PTPartyMember[m_partyMembers.size()];
		
		int currentLargestRoll;
		int indexWithLargestRoll;
		
		//Set each value in the array
		for(int currentNamesIndex = 0; currentNamesIndex < m_partyMembers.size(); currentNamesIndex++){
			currentLargestRoll = Integer.MIN_VALUE;
			indexWithLargestRoll = 0;
			
			//Find the largest value still in arraylist
			for(int i = 0; i < tempMembers.size(); i++){
				if(tempMembers.get(i).getLastRolledValue() > currentLargestRoll){
					indexWithLargestRoll = i;
					currentLargestRoll = tempMembers.get(i).getLastRolledValue();
				}		
			}
			sortedMembers[currentNamesIndex] = new PTPartyMember(tempMembers.get(indexWithLargestRoll));	
			tempMembers.remove(indexWithLargestRoll);
		}	
		return sortedMembers;
	}
	
	/** 
	 * @return an array of the unsorted indexes of the party members, ordered as the characters are when sorted by roll
	 */
	public int[] getIndexesByRollValue(){
		ArrayList<PTPartyMember> tempMembers = cloneMemberList();
		ArrayList<Integer> tempIndexes = new ArrayList<Integer>();
		int[] sortedIndexes = new int[m_partyMembers.size()];
		for(int j = 0; j < m_partyMembers.size(); j++){
			tempIndexes.add(Integer.valueOf(j));
		}
		
		int currentLargestRoll;
		int indexWithLargestRoll;
		
		//Set each value in the array
		for(int currentNamesIndex = 0; currentNamesIndex < m_partyMembers.size(); currentNamesIndex++){
			currentLargestRoll = Integer.MIN_VALUE;
			indexWithLargestRoll = 0;
			
			//Find the largest value still in arraylist
			for(int i = 0; i < tempMembers.size(); i++){
				if(tempMembers.get(i).getLastRolledValue() > currentLargestRoll){
					indexWithLargestRoll = i;
					currentLargestRoll = tempMembers.get(i).getLastRolledValue();
				}		
			}
			sortedIndexes[currentNamesIndex] = tempIndexes.get(indexWithLargestRoll).intValue();	
			tempMembers.remove(indexWithLargestRoll);
			tempIndexes.remove(indexWithLargestRoll);
		}	
		return sortedIndexes;
	}
	
	/**
	 * 
	 * @return a deep copy of the member array list
	 */
	private ArrayList<PTPartyMember> cloneMemberList(){
		int numberOfMembers = m_partyMembers.size();
		ArrayList<PTPartyMember> membersListClone = new ArrayList<PTPartyMember>();
		
		for(int i = 0; i < numberOfMembers; i++){
			PTPartyMember tempMember = new PTPartyMember(m_partyMembers.get(i));
			membersListClone.add(tempMember);
		}
		return membersListClone;
	}
	
	/**
	 * 
	 * @return the number of party members in the party
	 */
	public int size(){
		return m_partyMembers.size();
	}
	
	@Override
	public void setID(long id) {
		m_id = id;
	}

	@Override
	public long getID() {
		return m_id;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	public static final Parcelable.Creator<PTParty> CREATOR = new Parcelable.Creator<PTParty>() {
		public PTParty createFromParcel(Parcel in) {
			return new PTParty(in);
		}
		
		public PTParty[] newArray(int size) {
			return new PTParty[size];
		}
	};

}
