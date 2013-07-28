package com.lateensoft.pathfinder.toolkit.character;

import java.util.ArrayList;

import com.lateensoft.pathfinder.toolkit.items.PTArmor;
import com.lateensoft.pathfinder.toolkit.items.PTItem;
import com.lateensoft.pathfinder.toolkit.items.PTWeapon;

public class PTInventory {
	private ArrayList<PTItem> mItems;
	private ArrayList<PTArmor> mArmor;
	private ArrayList<PTWeapon> mWeapons;
	
	public PTInventory(){
		mItems = new ArrayList<PTItem>();
		mArmor = new ArrayList<PTArmor>();
		mWeapons = new ArrayList<PTWeapon>();
		
		//For new inventory, give user sample items
		addItem(new PTItem("Sample Item", 10, 1, false));
		addItem(new PTItem("Contained Item (ex. In bag of holding)", 30, 1, true));
	
	}
	
	/**
	 * Adds the item into the inventory. It is placed such that the list is alphabetically listed.
	 * @param newItem
	 */
	public void addItem(PTItem newItem){
		if( newItem != null ){
			//If the item already exists in inventory, add to its quantity
			int i = getIndexOf(newItem.getName());
			if(i >= 0){
				int quantity = newItem.getQuantity() + getItem(i).getQuantity();
				setQuantity(i, quantity);
				return;
			}
			
			for(i = 0; i < mItems.size(); i++){
				//Places in alphabetical position
				if(newItem.getName().compareToIgnoreCase(getItem(i).getName()) < 0 ){
					mItems.add(i, newItem);
					return;
				}
			}
			//If item is to go at the end of the list
			mItems.add(newItem);
		}
	}
	
	public void addArmor(PTArmor newArmor) {
		if( newArmor != null ){

			mArmor.add(newArmor);
		}
	}
	
	public void addWeapon(PTWeapon newWeapon) {
		if( newWeapon != null ){

			mWeapons.add(newWeapon);
		}
	}
		
	/**
	 * Deletes the item at index.
	 * @param index
	 */
	public void deleteItem(int index){
		if(index >= 0 && index < mItems.size())
			mItems.remove(index);
	}
	
	public void deleteArmor(int index) {
		if(index >=0 && index < mArmor.size())
			mArmor.remove(index);
	}
	
	public void deleteWeapon(int index) {
		if(index >= 0 && index < mWeapons.size())
			mWeapons.remove(index);
	}
	
	/**
	 * 
	 * @param index
	 * @return the item at index
	 */
	public PTItem getItem(int index){
		if(index >= 0 && index < mItems.size())
			return mItems.get(index);
		else return null; 
	}
	
	public PTArmor getArmor(int index) {
		if(index>= 0 && index < mArmor.size())
			return mArmor.get(index);
		else return null;
	}
	
	public PTWeapon getWeapon(int index) {
		if(index >= 0 && index < mWeapons.size())
			return mWeapons.get(index);
		else return null;
	}	
	
	/**
	 * 
	 * @return new array of armor
	 */
	public PTArmor[] getArmorArray() {
		PTArmor[] armorArray = new PTArmor[mArmor.size()];
		return (PTArmor[]) mArmor.toArray(armorArray);
	}
	
	public PTWeapon[] getWeaponArray() {
		PTWeapon[] weaponArray = new PTWeapon[mWeapons.size()];
		return (PTWeapon[]) mWeapons.toArray(weaponArray);
	}
	
	/**
	 * Replaces the item at index with newItem. If index is outside the bounds of this, or null, it will do nothing. Resorts the inventory alphabetically
	 * @param newItem
	 * @param index
	 */
	public void setItem(PTItem newItem, int index){
		if(index >= 0 && index < mItems.size() && newItem != null){
			deleteItem(index);
			addItem(newItem);
		}
	}
	
	public void setArmor(PTArmor newArmor, int index) {
		if(index >= 0 && index < mArmor.size() && newArmor != null) {
			deleteArmor(index);
			addArmor(newArmor);
		}
	}
	
	public void setWeapon(PTWeapon newWeapon, int index) {
		if(index >= 0 && index < mWeapons.size() && newWeapon != null) {
			deleteWeapon(index);
			addWeapon(newWeapon);
		}
	}
	
	/**
	 * returns an array of all the objects in the inventory
	 * @return an array of PTItem objects
	 */
	public PTItem[] getItems(){
		return mItems.toArray(new PTItem[mItems.size()]);		
	}
	
	/**
	 * Sets the weight of the item at index
	 * @param index
	 * @param weight
	 */
	public void setWeight(int index, int weight){
		if(index >= 0 && index < mItems.size())
			mItems.get(index).setWeight(weight);
	}
	
	/**
	 * Sets the quantity of the item at index
	 * @param index
	 * @param quantity
	 */
	public void setQuantity(int index, int quantity){
		if(index >= 0 && index < mItems.size())
			mItems.get(index).setQuantity(quantity);
	}
	
	/**
	 * Sets the item flag of the item at index if it is contained by another container 
	 * @param index
	 * @param isContained
	 */
	public void setContained(int index, boolean isContained){
		if(index >= 0 && index < mItems.size())
			mItems.get(index).setIsContained(isContained);
	}
	
	/**
	 * 
	 * @param itemName
	 * @return the index of the object with itemName. returns -1 if the item is not in the inventory.
	 */
	public int getIndexOf(String itemName){
		for(int i = 0; i < mItems.size(); i++){
			if(itemName.contentEquals(mItems.get(i).getName())){
				return i;
			}
		}
		return -1;
	}
	
	public int getIndexOfArmor(String itemName){
		for(int i = 0; i < mArmor.size(); i++){
			if(itemName.contentEquals(mArmor.get(i).getName())){
				return i;
			}
		}
		return -1;
	}
	
	public int getNumberOfItems(){
		return mItems.size();
	}
	
	public int getNumberOfArmor() {
		return mArmor.size();
	}
	
	public int getNumberofWeapons() {
		return mWeapons.size();
	}
	
	public double getTotalWeight() {
		double totalWeight = 0;
		for(int i = 0; i < mItems.size(); i++) {
			if(!mItems.get(i).isContained())
				totalWeight += (mItems.get(i).getWeight())*(mItems.get(i).getQuantity());
		}
		for(int i = 0; i < mWeapons.size(); i++) {
			if(!mWeapons.get(i).isContained())
				totalWeight += (mWeapons.get(i).getWeight())*(mWeapons.get(i).getQuantity());
		}
		for(int i = 0; i < mArmor.size(); i++) {
			if(!mArmor.get(i).isContained())
				totalWeight += (mArmor.get(i).getWeight())*(mArmor.get(i).getQuantity());
		}
		
		return totalWeight;
	}
	
}
