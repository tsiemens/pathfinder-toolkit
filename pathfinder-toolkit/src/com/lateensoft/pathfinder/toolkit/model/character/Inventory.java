package com.lateensoft.pathfinder.toolkit.model.character;

import java.util.List;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.model.character.items.Armor;
import com.lateensoft.pathfinder.toolkit.model.character.items.Item;
import com.lateensoft.pathfinder.toolkit.model.character.items.Weapon;

public class Inventory implements Parcelable {
	private static final String PARCEL_BUNDLE_KEY_ITEMS = "items";
	private static final String PARCEL_BUNDLE_KEY_ARMOR = "armor";
	private static final String PARCEL_BUNDLE_KEY_WEAPONS = "weapons";
	
	private List<Item> m_items;
	private List<Armor> m_armor;
	private List<Weapon> m_weapons;



    public Inventory(){
		m_items = Lists.newArrayList();
		m_armor = Lists.newArrayList();
		m_weapons = Lists.newArrayList();
	}
	
	public Inventory(Parcel in) {
		Bundle objectBundle = in.readBundle();
		m_items = objectBundle.getParcelableArrayList(PARCEL_BUNDLE_KEY_ITEMS);
		m_armor = objectBundle.getParcelableArrayList(PARCEL_BUNDLE_KEY_ARMOR);
		m_weapons = objectBundle.getParcelableArrayList(PARCEL_BUNDLE_KEY_WEAPONS);
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		Bundle objectBundle = new Bundle();
		objectBundle.putParcelableArrayList(PARCEL_BUNDLE_KEY_ITEMS, Lists.newArrayList(m_items));
		objectBundle.putParcelableArrayList(PARCEL_BUNDLE_KEY_ARMOR, Lists.newArrayList(m_armor));
		objectBundle.putParcelableArrayList(PARCEL_BUNDLE_KEY_WEAPONS, Lists.newArrayList(m_weapons));
		out.writeBundle(objectBundle);
	}

    public List<Item> getItems() {
        return m_items;
    }

    public List<Armor> getArmors() {
        return m_armor;
    }

    public List<Weapon> getWeapons() {
        return m_weapons;
    }

    public double getTotalWeight() {
		double totalWeight = 0;
        for (Item item : m_items) {
            if (!item.isContained())
                totalWeight += (item.getWeight()) * (item.getQuantity());
        }
        for (Weapon weapon : m_weapons) {
            if (!weapon.isContained())
                totalWeight += (weapon.getWeight()) * (weapon.getQuantity());
        }
        for (Armor armor : m_armor) {
            if (!armor.isContained())
                totalWeight += (armor.getWeight()) * (armor.getQuantity());
        }
		
		return totalWeight;
	}
	
	/**
	 * Sets character id of all items in inventory
	 * @param id
	 */
	public void setCharacterID(long id) {
		for(Item item : m_items) {
			item.setCharacterID(id);
		}
		for(Item item : m_weapons) {
			item.setCharacterID(id);
		}
		for(Item item : m_armor) {
			item.setCharacterID(id);
		}
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	public static final Parcelable.Creator<Inventory> CREATOR = new Parcelable.Creator<Inventory>() {
		public Inventory createFromParcel(Parcel in) {
			return new Inventory(in);
		}
		
		public Inventory[] newArray(int size) {
			return new Inventory[size];
		}
	};
}
