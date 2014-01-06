package com.lateensoft.pathfinder.toolkit.deprecated.v1.model.party;

import java.util.ArrayList;

public class PTParty {
	private ArrayList<PTPartyMember> mPartyMembers;
	private String mPartyName;
	public int mID; //Used in SQL
	
	public PTParty(String name){
		mPartyMembers = new ArrayList<PTPartyMember>();
		
		if(name != null)
			mPartyName = name;
		else mPartyName = "";
		
		mID = 0;
	}
	
	/**
	 * Adds party member into the party. The party member is inserted into the list alphabetically.
	 * @param newPartyMember
	 * @return the index of the character in the list. -1 if the addition failed
	 */
	public int addPartyMember(PTPartyMember newPartyMember){
		if( newPartyMember != null ){
			
			for(int i = 0; i < mPartyMembers.size(); i++){
				//Places in alphabetical position
				if(newPartyMember.getName().compareToIgnoreCase(getPartyMember(i).getName()) < 0 ){
					mPartyMembers.add(i, newPartyMember);
					return i;
				}
			}
			//If party member is to go at the end of the list
			mPartyMembers.add(newPartyMember);
			return mPartyMembers.size() - 1;
		}
		else return -1;
	}
	
	/**
	 * 
	 * @param index
	 * @return returns the party member at index, in an alphabetical list of all party members
	 */
	public PTPartyMember getPartyMember(int index){
		if(index >= 0 && index < mPartyMembers.size())
			return mPartyMembers.get(index);
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
		if(index >= 0 && index < mPartyMembers.size() && partyMember != null){
			mPartyMembers.remove(index);
			return addPartyMember(partyMember);
		}
		else return -1;
	}
	
	/**
	 * deletes the party member at index
	 * @param index
	 */
	public void deletePartyMember(int index){
		if(index >= 0 && index < mPartyMembers.size())
			mPartyMembers.remove(index);
	}
	
	/**
	 * 
	 * @return an array of all the party members' names
	 */
	public String[] getPartyMemberNames(){
		String names[] = new String[mPartyMembers.size()];
		for(int i = 0; i < mPartyMembers.size(); i++){
			names[i] = mPartyMembers.get(i).getName();
		}
		
		return names;
	}
	
	public void setName(String name){
		if(name != null)
			mPartyName = name;
	}
	
	public String getName(){
		return mPartyName;
	}
	
	/**
	 * 
	 * @return an array of the names of party members, ordered from highest roll, to lowest. 
	 * If there is a tie, sorts by default order
	 */
	public String[] getNamesByRollValue(){
		PTPartyMember[] orderedMembers = getPartyMembersByRollValue();
		String[] names = new String[mPartyMembers.size()];
		
		//Set each value in the array
		for(int i = 0; i < mPartyMembers.size(); i++){
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
		int[] rollValues = new int[mPartyMembers.size()];
		
		//Set each value in the array
		for(int i = 0; i < mPartyMembers.size(); i++){
			rollValues[i] = orderedMembers[i].getRolledValue();	
		}	
		return rollValues;
	}
	
	
	/**
	 * 
	 * @param index
	 * @return the "actual" index of the party, based on its index in the list sorted by roll
	 */
	public int getPartyMemberIndexByRollValueIndex(int index){
		if(index < mPartyMembers.size()){
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
		PTPartyMember[] sortedMembers = new PTPartyMember[mPartyMembers.size()];
		
		int currentLargestRoll;
		int indexWithLargestRoll;
		
		//Set each value in the array
		for(int currentNamesIndex = 0; currentNamesIndex < mPartyMembers.size(); currentNamesIndex++){
			currentLargestRoll = Integer.MIN_VALUE;
			indexWithLargestRoll = 0;
			
			//Find the largest value still in arraylist
			for(int i = 0; i < tempMembers.size(); i++){
				if(tempMembers.get(i).getRolledValue() > currentLargestRoll){
					indexWithLargestRoll = i;
					currentLargestRoll = tempMembers.get(i).getRolledValue();
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
		int[] sortedIndexes = new int[mPartyMembers.size()];
		for(int j = 0; j < mPartyMembers.size(); j++){
			tempIndexes.add(Integer.valueOf(j));
		}
		
		int currentLargestRoll;
		int indexWithLargestRoll;
		
		//Set each value in the array
		for(int currentNamesIndex = 0; currentNamesIndex < mPartyMembers.size(); currentNamesIndex++){
			currentLargestRoll = Integer.MIN_VALUE;
			indexWithLargestRoll = 0;
			
			//Find the largest value still in arraylist
			for(int i = 0; i < tempMembers.size(); i++){
				if(tempMembers.get(i).getRolledValue() > currentLargestRoll){
					indexWithLargestRoll = i;
					currentLargestRoll = tempMembers.get(i).getRolledValue();
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
		int numberOfMembers = mPartyMembers.size();
		ArrayList<PTPartyMember> membersListClone = new ArrayList<PTPartyMember>();
		
		for(int i = 0; i < numberOfMembers; i++){
			PTPartyMember tempMember = new PTPartyMember(mPartyMembers.get(i));
			membersListClone.add(tempMember);
		}
		return membersListClone;
	}
	
	/**
	 * 
	 * @return the number of party members in the party
	 */
	public int size(){
		return mPartyMembers.size();
	}
}
