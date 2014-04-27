package com.lateensoft.pathfinder.toolkit.model.character.items;

import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.R;

import android.content.Context;
import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Weapon extends Item implements Parcelable {
	private int m_totalAttackBonus;
    private String m_damage;
    private String m_critical;
    private int m_range;
    private String m_specialProperties;
    private int m_ammunition;
    private String m_type;
    private String m_size;
	
	public Weapon() {
		super();
		m_totalAttackBonus = 0;
		m_damage = "";
		m_critical = "x2";
		m_range = 5;
		m_specialProperties = "";
		m_ammunition = 0;
		m_type = "";
		m_size = "M";
	}
	
	public Weapon(long characterId, String name) {
		super(characterId, name);
	}
	
	public Weapon(Parcel in) {
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

	public int getTotalAttackBonus() {
		return m_totalAttackBonus;
	}

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

    public static boolean isValidSize(Context context, String sizeString) {
        Resources r = context.getResources();
        List<String> types = Lists.newArrayList(r.getStringArray(R.array.size_spinner_options));
        return types.contains(sizeString);
    }

	public String getDamage() {
		return m_damage;
	}

	public void setDamage(String damage) {
		this.m_damage = damage;
	}

	public String getCritical() {
		return m_critical;
	}

	public void setCritical(String critical) {
		this.m_critical = critical;
	}

	public int getRange() {
		return m_range;
	}

	public void setRange(int range) {
		this.m_range = range;
	}

	public String getSpecialProperties() {
		return m_specialProperties;
	}

	public void setSpecialProperties(String specialProperties) {
		this.m_specialProperties = specialProperties;
	}

	public int getAmmunition() {
		return m_ammunition;
	}

	public void setAmmunition(int ammunition) {
		this.m_ammunition = ammunition;
	}

	public String getType() {
		return m_type;
	}

	public void setType(String type) {
		this.m_type = type;
	}

	public String getSize() {
		return m_size;
	}

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

    public static boolean isValidType(Context context, String typeString) {
        Resources r = context.getResources();
        List<String> types = Lists.newArrayList(r.getStringArray(R.array.weapon_type_options));
        return types.contains(typeString);
    }
	
	public static final Parcelable.Creator<Weapon> CREATOR = new Parcelable.Creator<Weapon>() {
		public Weapon createFromParcel(Parcel in) {
			return new Weapon(in);
		}
		
		public Weapon[] newArray(int size) {
			return new Weapon[size];
		}
	};

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Weapon)) return false;
        if (!super.equals(o)) return false;

        Weapon weapon = (Weapon) o;

        if (m_ammunition != weapon.m_ammunition) return false;
        if (m_range != weapon.m_range) return false;
        if (m_totalAttackBonus != weapon.m_totalAttackBonus) return false;
        if (m_critical != null ? !m_critical.equals(weapon.m_critical) : weapon.m_critical != null) return false;
        if (m_damage != null ? !m_damage.equals(weapon.m_damage) : weapon.m_damage != null) return false;
        if (m_size != null ? !m_size.equals(weapon.m_size) : weapon.m_size != null) return false;
        if (m_specialProperties != null ? !m_specialProperties.equals(weapon.m_specialProperties) : weapon.m_specialProperties != null)
            return false;
        if (m_type != null ? !m_type.equals(weapon.m_type) : weapon.m_type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + m_totalAttackBonus;
        result = 31 * result + (m_damage != null ? m_damage.hashCode() : 0);
        result = 31 * result + (m_critical != null ? m_critical.hashCode() : 0);
        result = 31 * result + m_range;
        result = 31 * result + (m_specialProperties != null ? m_specialProperties.hashCode() : 0);
        result = 31 * result + m_ammunition;
        result = 31 * result + (m_type != null ? m_type.hashCode() : 0);
        result = 31 * result + (m_size != null ? m_size.hashCode() : 0);
        return result;
    }
}
