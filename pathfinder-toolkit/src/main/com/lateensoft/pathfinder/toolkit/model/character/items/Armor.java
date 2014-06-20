package com.lateensoft.pathfinder.toolkit.model.character.items;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.common.collect.Lists;

import java.util.List;

public class Armor extends Item {
	boolean m_worn;
	int m_ACBonus;
	int m_checkPen;
	int m_maxDex;
	int m_spellFail;
	int m_speed;
	String m_specialProperties;
	String m_size;
	
	public Armor() {
		super();
		m_worn =  true;
		m_ACBonus = 0;
		m_checkPen = 0;
		m_maxDex = 10;
		m_spellFail = 0;
		m_speed = 30;
		m_specialProperties = "";
		m_size = "M";
	}
	
	public Armor(Parcel in) {
		super(in);
		boolean[] worn = new boolean[1];
		in.readBooleanArray(worn);
		m_worn = worn[0];
		m_ACBonus = in.readInt();
		m_checkPen = in.readInt();
		m_maxDex = in.readInt();
		m_spellFail = in.readInt();
		m_speed = in.readInt();
		m_specialProperties = in.readString();
		m_size = in.readString();
	}
	
	@Override
	public void writeToParcel(Parcel out, int flags) {
		super.writeToParcel(out, flags);
		boolean[] worn = new boolean[1];
		worn[0] = m_worn;
		out.writeBooleanArray(worn);
		out.writeInt(m_ACBonus);
		out.writeInt(m_checkPen);
		out.writeInt(m_maxDex);
		out.writeInt(m_spellFail);
		out.writeInt(m_speed);
		out.writeString(m_specialProperties);
		out.writeString(m_size);
	}
	
	public void setSize(String size) {
		m_size = size;
	}
	
	public String getSize() {
		return m_size;
	}
	
	public void setSpecialProperties(String properties) {
		m_specialProperties = properties;
	}
	
	public String getSpecialProperties() {
		return m_specialProperties;
	}
		
	public boolean isWorn() {
		return m_worn;
	}

	public void setWorn(boolean worn) {
		this.m_worn = worn;
	}

	public int getACBonus() {
		return m_ACBonus;
	}

	public void setACBonus(int ACBonus) {
		this.m_ACBonus = ACBonus;
	}
	
	public int getCheckPen() {
		return m_checkPen;
	}
	
	public void setCheckPen(int checkPen) {
		this.m_checkPen = checkPen;
	}
	
	public int getMaxDex() {
		return m_maxDex;
	}
	
	public void setMaxDex(int maxDex) {
		this.m_maxDex = maxDex;
	}
	
	public int getSpellFail() {
		return m_spellFail;
	}
	
	public void setSpellFail(int spellFail) {
		this.m_spellFail = spellFail;
	}
	
	public int getSpeed() {
		return m_speed;
	}
	
	public void setSpeed(int speed) {
		this.m_speed = speed;
	}
	
	public String getSpeedString() {
		String speedString = Integer.toString(m_speed) + " ft.";
		return speedString;
	}

	public int getSizeInt() {
		String[] sizeArray = getSizeArray();
		for(int i = 0; i < sizeArray.length; i++) {
			if(m_size.equals(sizeArray[i]))
				return i;
		}
		return 1;
	}

	public void setSizeInt(int size) {
		String[] sizeArray = getSizeArray();
		m_size = sizeArray[size];
	}

    public static boolean isValidSize(String sizeString) {
        List<String> types = Lists.newArrayList(getSizeArray());
        return types.contains(sizeString);
    }

    private static String[] getSizeArray() {
        return new String[] {"S", "M", "L"};
    }
	
	public static final Parcelable.Creator<Armor> CREATOR = new Parcelable.Creator<Armor>() {
		public Armor createFromParcel(Parcel in) {
			return new Armor(in);
		}
		
		public Armor[] newArray(int size) {
			return new Armor[size];
		}
	};

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Armor)) return false;
        if (!super.equals(o)) return false;

        Armor armor = (Armor) o;

        if (m_ACBonus != armor.m_ACBonus) return false;
        if (m_checkPen != armor.m_checkPen) return false;
        if (m_maxDex != armor.m_maxDex) return false;
        if (m_speed != armor.m_speed) return false;
        if (m_spellFail != armor.m_spellFail) return false;
        if (m_worn != armor.m_worn) return false;
        if (m_size != null ? !m_size.equals(armor.m_size) : armor.m_size != null) return false;
        if (m_specialProperties != null ? !m_specialProperties.equals(armor.m_specialProperties) : armor.m_specialProperties != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (m_worn ? 1 : 0);
        result = 31 * result + m_ACBonus;
        result = 31 * result + m_checkPen;
        result = 31 * result + m_maxDex;
        result = 31 * result + m_spellFail;
        result = 31 * result + m_speed;
        result = 31 * result + (m_specialProperties != null ? m_specialProperties.hashCode() : 0);
        result = 31 * result + (m_size != null ? m_size.hashCode() : 0);
        return result;
    }
}
