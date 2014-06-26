package com.lateensoft.pathfinder.toolkit.model.character.items;

import android.content.Context;
import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.dao.Identifiable;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable, Identifiable, Comparable<Item> {
    private String name;
    private double weight;
    private int quantity;
	//set if in a container such as a bag of holding (will be used to set effective weight to 0)
    private boolean isContained;

    private long id;

    @Deprecated // once the DAOs are implemented, no owned objects will reference the character.
    private long characterId;
	
	public Item(long id, long characterId, String name, double weight, int quantity, boolean contained) {
		this.id = id;
		this.characterId = characterId;
		this.name = name;
		this.weight = weight;
		this.quantity = quantity;
		isContained = contained;
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
		out.writeString(name);
		out.writeDouble(weight);
		out.writeInt(quantity);
		boolean[] contained = new boolean[1];
		contained[0] = isContained;
		out.writeBooleanArray(contained);
		out.writeLong(id);
		out.writeLong(characterId);
	}
	
	public Item(Parcel in) {
		name = in.readString();
		weight = in.readDouble();
		quantity = in.readInt();
		boolean[] contained = new boolean[1];
		in.readBooleanArray(contained);
		isContained = contained[0];
		id = in.readLong();
		characterId = in.readLong();
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public double getWeight() {
		return weight;
	}
	
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public boolean isContained(){
		return isContained;
	}
	
	public void setContained(boolean isContained){
		this.isContained = isContained;
	}
	
	@Override
	public void setId(long id) {
		this.id = id;
	}

	@Override
	public long getId() {
		return id;
	}
	
	public void setCharacterID (long id) {
		characterId = id;
	}
	
	public long getCharacterID() {
		return characterId;
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

        if (characterId != item.characterId) return false;
        if (id != item.id) return false;
        if (isContained != item.isContained) return false;
        if (quantity != item.quantity) return false;
        if (Double.compare(item.weight, weight) != 0) return false;
        if (name != null ? !name.equals(item.name) : item.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = name != null ? name.hashCode() : 0;
        temp = Double.doubleToLongBits(weight);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + quantity;
        result = 31 * result + (isContained ? 1 : 0);
        result = 31 * result + (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (characterId ^ (characterId >>> 32));
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
