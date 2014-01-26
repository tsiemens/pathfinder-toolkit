package com.lateensoft.pathfinder.toolkit.model.character.items;

import com.lateensoft.pathfinder.toolkit.R;

import android.content.Context;
import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;

public class PTWeapon extends PTItem implements Parcelable {
	int m_totalAttackBonus;
	String m_damage;
	String m_critical;
	int m_range;
	String m_specialProperties;
	int m_ammunition;
	String m_type;
	String m_size;
	
	public PTWeapon() {
		super();
		m_totalAttackBonus = 0;
		m_damage = new String("");
		m_critical = new String("x2");
		m_range = 5;
		m_specialProperties = new String("");
		m_ammunition = 0;
		m_type = new String("");
		m_size = "M";
	}
	
	public PTWeapon(long characterId, String name) {
		super(characterId, name);
	}
	
	public PTWeapon(Parcel in) {
		super(in);
		m_totalAttackBonus = in.readInt();
		m_damage = in.readString();
		m_critical = in.readString();
		m_range = in.readInt();
		m_specialProperties = in.readString();
		m_ammunition = in.readInt();
		m_type = in.readString();
		m_size = in.readString();
	}
	
	@Override
	public void writeToParcel(Parcel out, int flags) {
		super.writeToParcel(out, flags);
		out.writeInt(m_totalAttackBonus);
		out.writeString(m_damage);
		out.writeString(m_critical);
		out.writeInt(m_range);
		out.writeString(m_specialProperties);
		out.writeInt(m_ammunition);
		out.writeString(m_type);
		out.writeString(m_size);
	}

	/**
	 * @return the mTotalAttackBonus
	 */
	public int getTotalAttackBonus() {
		return m_totalAttackBonus;
	}

	/**
	 * @param totalAttackBonus
	 */
	public void setTotalAttackBonus(int totalAttackBonus) {
		this.m_totalAttackBonus = totalAttackBonus;
	}
	
	public int getSizeInt() {
		String[] sizeArray = {"S", "M", "L"};
		for(int i = 0; i < sizeArray.length; i++) {
			if(m_size.equals(sizeArray[i]))
				return i;
		}
		return 1;
	}
	
	/**
	 * @return the mDamage
	 */
	public String getDamage() {
		return m_damage;
	}

	/**
	 * @param damage
	 */
	public void setDamage(String damage) {
		this.m_damage = damage;
	}

	/**
	 * @return the mCritical
	 */
	public String getCritical() {
		return m_critical;
	}

	/**
	 * @param critical
	 */
	public void setCritical(String critical) {
		this.m_critical = critical;
	}

	/**
	 * @return the mRange
	 */
	public int getRange() {
		return m_range;
	}

	/**
	 * @param range
	 */
	public void setRange(int range) {
		this.m_range = range;
	}

	/**
	 * @return the mSpecialProperties
	 */
	public String getSpecialProperties() {
		return m_specialProperties;
	}

	/**
	 * @param specialProperties
	 */
	public void setSpecialProperties(String specialProperties) {
		this.m_specialProperties = specialProperties;
	}

	/**
	 * @return the mAmmunition
	 */
	public int getAmmunition() {
		return m_ammunition;
	}

	/**
	 * @param ammunition
	 */
	public void setAmmunition(int ammunition) {
		this.m_ammunition = ammunition;
	}

	/**
	 * @return the mType
	 */
	public String getType() {
		return m_type;
	}

	/**
	 * @param type
	 */
	public void setType(String type) {
		this.m_type = type;
	}

	/**
	 * @return the mSize
	 */
	public String getSize() {
		return m_size;
	}

	/**
	 * @param size
	 */
	public void setSize(String size) {
		this.m_size = size;
	}

	public int getTypeInt(Context context) {
		Resources r = context.getResources();
		String[] options = r.getStringArray(R.array.weapon_type_options);
		for(int i = 0; i < options.length; i++) {
			if(m_type.equals(options[i]))
				return i;
		}
		return 0;
	}
	
	public static final Parcelable.Creator<PTWeapon> CREATOR = new Parcelable.Creator<PTWeapon>() {
		public PTWeapon createFromParcel(Parcel in) {
			return new PTWeapon(in);
		}
		
		public PTWeapon[] newArray(int size) {
			return new PTWeapon[size];
		}
	};
}
