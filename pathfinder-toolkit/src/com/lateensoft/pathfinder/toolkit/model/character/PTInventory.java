package com.lateensoft.pathfinder.toolkit.model.character;

import java.util.ArrayList;
import java.util.Arrays;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.lateensoft.pathfinder.toolkit.model.character.items.PTArmor;
import com.lateensoft.pathfinder.toolkit.model.character.items.PTItem;
import com.lateensoft.pathfinder.toolkit.model.character.items.PTWeapon;

public class PTInventory implements Parcelable {
	private static final String PARCEL_BUNDLE_KEY_ITEMS = "items";
	private static final String PARCEL_BUNDLE_KEY_ARMOR = "armor";
	private static final String PARCEL_BUNDLE_KEY_WEAPONS = "weapons";
	
	private ArrayList<PTItem> m_items;
	private ArrayList<PTArmor> m_armor;
	private ArrayList<PTWeapon> m_weapons;
	
	public PTInventory(){
		m_items = new ArrayList<PTItem>();
		m_armor = new ArrayList<PTArmor>();
		m_weapons = new ArrayList<PTWeapon>();
	}
	
	public PTInventory(Parcel in) {
		Bundle objectBundle = in.readBundle();
		m_items = objectBundle.getParcelableArrayList(PARCEL_BUNDLE_KEY_ITEMS);
		m_armor = objectBundle.getParcelableArrayList(PARCEL_BUNDLE_KEY_ARMOR);
		m_weapons = objectBundle.getParcelableArrayList(PARCEL_BUNDLE_KEY_WEAPONS);
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		Bundle objectBundle = new Bundle();
		objectBundle.putParcelableArrayList(PARCEL_BUNDLE_KEY_ITEMS, m_items);
		objectBundle.putParcelableArrayList(PARCEL_BUNDLE_KEY_ARMOR, m_armor);
		objectBundle.putParcelableArrayList(PARCEL_BUNDLE_KEY_WEAPONS, m_weapons);
		out.writeBundle(objectBundle);
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
			
			for(i = 0; i < m_items.size(); i++){
				//Places in alphabetical position
				if(newItem.getName().compareToIgnoreCase(getItem(i).getName()) < 0 ){
					m_items.add(i, newItem);
					return;
				}
			}
			//If item is to go at the end of the list
			m_items.add(newItem);
		}
	}
	
	public void addArmor(PTArmor newArmor) {
		if( newArmor != null ){

			m_armor.add(newArmor);
		}
	}
	
	public void addWeapon(PTWeapon newWeapon) {
		if( newWeapon != null ){

			m_weapons.add(newWeapon);
		}
	}
	
	/**
	 * Add an alphabetically sorted array of PTItems
	 * For use of database for population
	 * @param items
	 */
	public void setItems(PTItem[] items) {
		m_items =  new ArrayList<PTItem>(Arrays.asList(items));
	}
	
	/**
	 * Add an alphabetically sorted array of PTWeapons
	 * For use of database for population
	 * @param weapons
	 */
	public void setWeapons(PTWeapon[] weapons) {
		m_weapons =  new ArrayList<PTWeapon>(Arrays.asList(weapons));
	}
	
	/**
	 * Add an alphabetically sorted array of PTArmors
	 * For use of database for population
	 * @param armors
	 */
	public void setArmor(PTArmor[] armors) {
		m_armor = new ArrayList<PTArmor>(Arrays.asList(armors));
	}
		
	/**
	 * Deletes the item at index.
	 * @param index
	 */
	public void deleteItem(int index){
		if(index >= 0 && index < m_items.size())
			m_items.remove(index);
	}
	
	public void deleteArmor(int index) {
		if(index >=0 && index < m_armor.size())
			m_armor.remove(index);
	}
	
	public void deleteWeapon(int index) {
		if(index >= 0 && index < m_weapons.size())
			m_weapons.remove(index);
	}
	
	/**
	 * 
	 * @param index
	 * @return the item at index
	 */
	public PTItem getItem(int index){
		if(index >= 0 && index < m_items.size())
			return m_items.get(index);
		else return null; 
	}
	
	public PTArmor getArmor(int index) {
		if(index>= 0 && index < m_armor.size())
			return m_armor.get(index);
		else return null;
	}
	
	public PTWeapon getWeapon(int index) {
		if(index >= 0 && index < m_weapons.size())
			return m_weapons.get(index);
		else return null;
	}	
	
	/**
	 * 
	 * @return new array of armor
	 */
	public PTArmor[] getArmorArray() {
		PTArmor[] armorArray = new PTArmor[m_armor.size()];
		return (PTArmor[]) m_armor.toArray(armorArray);
	}
	
	public PTWeapon[] getWeaponArray() {
		PTWeapon[] weaponArray = new PTWeapon[m_weapons.size()];
		return (PTWeapon[]) m_weapons.toArray(weaponArray);
	}
	
	/**
	 * Replaces the item at index with newItem. If index is outside the bounds of this, or null, it will do nothing. Resorts the inventory alphabetically
	 * @param newItem
	 * @param index
	 */
	public void setItem(PTItem newItem, int index){
		if(index >= 0 && index < m_items.size() && newItem != null){
			deleteItem(index);
			addItem(newItem);
		}
	}
	
	public void setArmor(PTArmor newArmor, int index) {
		if(index >= 0 && index < m_armor.size() && newArmor != null) {
			deleteArmor(index);
			addArmor(newArmor);
		}
	}
	
	public void setWeapon(PTWeapon newWeapon, int index) {
		if(index >= 0 && index < m_weapons.size() && newWeapon != null) {
			deleteWeapon(index);
			addWeapon(newWeapon);
		}
	}
	
	/**
	 * returns an array of all the objects in the inventory
	 * @return an array of PTItem objects
	 */
	public PTItem[] getItems(){
		return m_items.toArray(new PTItem[m_items.size()]);		
	}
	
	/**
	 * Sets the weight of the item at index
	 * @param index
	 * @param weight
	 */
	public void setWeight(int index, int weight){
		if(index >= 0 && index < m_items.size())
			m_items.get(index).setWeight(weight);
	}
	
	/**
	 * Sets the quantity of the item at index
	 * @param index
	 * @param quantity
	 */
	public void setQuantity(int index, int quantity){
		if(index >= 0 && index < m_items.size())
			m_items.get(index).setQuantity(quantity);
	}
	
	/**
	 * Sets the item flag of the item at index if it is contained by another container 
	 * @param index
	 * @param isContained
	 */
	public void setContained(int index, boolean isContained){
		if(index >= 0 && index < m_items.size())
			m_items.get(index).setIsContained(isContained);
	}
	
	/**
	 * 
	 * @param itemName
	 * @return the index of the object with itemName. returns -1 if the item is not in the inventory.
	 */
	public int getIndexOf(String itemName){
		for(int i = 0; i < m_items.size(); i++){
			if(itemName.contentEquals(m_items.get(i).getName())){
				return i;
			}
		}
		return -1;
	}
	
	public int getIndexOfArmor(String itemName){
		for(int i = 0; i < m_armor.size(); i++){
			if(itemName.contentEquals(m_armor.get(i).getName())){
				return i;
			}
		}
		return -1;
	}
	
	public int getNumberOfItems(){
		return m_items.size();
	}
	
	public int getNumberOfArmor() {
		return m_armor.size();
	}
	
	public int getNumberofWeapons() {
		return m_weapons.size();
	}
	
	public double getTotalWeight() {
		double totalWeight = 0;
		for(int i = 0; i < m_items.size(); i++) {
			if(!m_items.get(i).isContained())
				totalWeight += (m_items.get(i).getWeight())*(m_items.get(i).getQuantity());
		}
		for(int i = 0; i < m_weapons.size(); i++) {
			if(!m_weapons.get(i).isContained())
				totalWeight += (m_weapons.get(i).getWeight())*(m_weapons.get(i).getQuantity());
		}
		for(int i = 0; i < m_armor.size(); i++) {
			if(!m_armor.get(i).isContained())
				totalWeight += (m_armor.get(i).getWeight())*(m_armor.get(i).getQuantity());
		}
		
		return totalWeight;
	}
	
	/**
	 * Sets character id of all items in inventory
	 * @param id
	 */
	public void setCharacterID(long id) {
		for(PTItem item : m_items) {
			item.setCharacterID(id);
		}
		for(PTItem item : m_weapons) {
			item.setCharacterID(id);
		}
		for(PTItem item : m_armor) {
			item.setCharacterID(id);
		}
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	public static final Parcelable.Creator<PTInventory> CREATOR = new Parcelable.Creator<PTInventory>() {
		public PTInventory createFromParcel(Parcel in) {
			return new PTInventory(in);
		}
		
		public PTInventory[] newArray(int size) {
			return new PTInventory[size];
		}
	};
}
