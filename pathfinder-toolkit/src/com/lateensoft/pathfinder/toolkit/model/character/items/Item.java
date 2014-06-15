package com.lateensoft.pathfinder.toolkit.model.character.items;

import android.content.Context;
import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.db.dao.Identifiable;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable, Identifiable, Comparable<Item> {
    private String m_name;
    private double m_weight;
    private int m_quantity;
	//set if in a container such as a bag of holding (will be used to set effective weight to 0)
    private boolean m_isContained;

    private long m_id;

    @Deprecated
    private long m_characterId;
	
	public Item(long id, long characterId, String name, double weight, int quantity, boolean contained) {
		m_id = id;
		m_characterId = characterId;
		m_name = name;
		m_weight = weight;
		m_quantity = quantity;
		m_isContained = contained;
	}
	
	public Item(long characterId, String name, double weight, int quantity, boolean contained) {
		this(UNSET_ID, characterId, name, weight, quantity, contained);
	}
	
	/**
	 * Creates new instance of an Item. Defaults quantity to 1 and contained to false.
	 */
	public Item(long characterId, String name, double weight) {
		this(characterId, name, weight, 1, false);
	}
	
	public Item(long characterId, String name) {
		this(characterId, name, 1);
	}
	
	public Item(long characterId) {
		this(characterId, "");
	}
	
	public Item() {
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
	
	public Item(Parcel in) {
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
	
	public void setContained(boolean isContained){
		m_isContained = isContained;
	}
	
	@Override
	public void setId(long id) {
		m_id = id;
	}

	@Override
	public long getId() {
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

	public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
		public Item createFromParcel(Parcel in) {
			return new Item(in);
		}
		
		public Item[] newArray(int size) {
			return new Item[size];
		}
	};

    @Override
    public int compareTo(Item another) {
        return this.getName().compareToIgnoreCase(another.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;

        Item item = (Item) o;

        if (m_characterId != item.m_characterId) return false;
        if (m_id != item.m_id) return false;
        if (m_isContained != item.m_isContained) return false;
        if (m_quantity != item.m_quantity) return false;
        if (Double.compare(item.m_weight, m_weight) != 0) return false;
        if (m_name != null ? !m_name.equals(item.m_name) : item.m_name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = m_name != null ? m_name.hashCode() : 0;
        temp = Double.doubleToLongBits(m_weight);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + m_quantity;
        result = 31 * result + (m_isContained ? 1 : 0);
        result = 31 * result + (int) (m_id ^ (m_id >>> 32));
        result = 31 * result + (int) (m_characterId ^ (m_characterId >>> 32));
        return result;
    }

    /**
     * Builds a concise string representation of the weight, quantity and contained state of the item.
     */
    public String getWeightQuantityContainedText(Context context) {
        String weightText = Double.toString(getWeight());
        if (getQuantity() != 1) {
            weightText = weightText + String.format(" x%d", getQuantity());
        }
        if (isContained()) {
            weightText = weightText +
                    " (" + context.getString(R.string.item_contained_abrev)+ ")";
        }
        return weightText;
    }
}
