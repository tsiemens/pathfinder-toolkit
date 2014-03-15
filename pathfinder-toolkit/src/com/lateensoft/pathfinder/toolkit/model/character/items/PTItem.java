package com.lateensoft.pathfinder.toolkit.model.character.items;

import com.lateensoft.pathfinder.toolkit.db.repository.PTStorable;

import android.os.Parcel;
import android.os.Parcelable;

public class PTItem implements Parcelable, PTStorable, Comparable<PTItem> {
	String m_name;
	double m_weight;
	int m_quantity;
	//set if in a container such as a bag of holding (will be used to set effective weight to 0)
	boolean m_isContained;
	
	long m_id;
	long m_characterId;
	
	public PTItem(long id, long characterId, String name, double weight, int quantity, boolean contained) {
		m_id = id;
		m_characterId = characterId;
		m_name = name;
		m_weight = weight;
		m_quantity = quantity;
		m_isContained = contained;
	}
	
	public PTItem(long characterId, String name, double weight, int quantity, boolean contained) {
		this(UNSET_ID, characterId, name, weight, quantity, contained);
	}
	
	/**
	 * Creates new instance of an Item. Defaults quantity to 1 and contained to false. 
	 * @param name
	 * @param weight
	 */
	public PTItem(long characterId, String name, double weight) {
		this(characterId, name, weight, 1, false);
	}
	
	public PTItem(long characterId, String name) {
		this(characterId, name, 1);
	}
	
	public PTItem(long characterId) {
		this(characterId, "");
	}
	
	public PTItem() {
		this(UNSET_ID);
	}
	
	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(m_name);
		out.writeDouble(m_weight);
		out.writeInt(m_quantity);
		boolean[] contained = new boolean[1];
		contained[0] = m_isContained;
		out.writeBooleanArray(contained);
		out.writeLong(m_id);
		out.writeLong(m_characterId);
	}
	
	public PTItem(Parcel in) {
		m_name = in.readString();
		m_weight = in.readDouble();
		m_quantity = in.readInt();
		boolean[] contained = new boolean[1];
		in.readBooleanArray(contained);
		m_isContained = contained[0];
		m_id = in.readLong();
		m_characterId = in.readLong();
	}
	
	public String getName() {
		return m_name;
	}
	
	public void setName(String name) {
		m_name = name;
	}
	
	public double getWeight() {
		return m_weight;
	}
	
	public void setWeight(double weight) {
		m_weight = weight;
	}
	
	public int getQuantity() {
		return m_quantity;
	}
	
	public void setQuantity(int quantity) {
		m_quantity = quantity;
	}
	
	public boolean isContained(){
		return m_isContained;
	}
	
	public void setIsContained(boolean isContained){
		m_isContained = isContained;
	}
	
	@Override
	public void setID(long id) {
		m_id = id;
	}

	@Override
	public long getID() {
		return m_id;
	}
	
	public void setCharacterID (long id) {
		m_characterId = id;
	}
	
	public long getCharacterID() {
		return m_characterId;
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

    @Override
    public int compareTo(PTItem another) {
        return this.getName().compareToIgnoreCase(another.getName());
    }
}
