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
		m_specialProperties = new String("");
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
		String speedString = new String();
		speedString = Integer.toString(m_speed) + " ft.";
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
}
