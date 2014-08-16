package com.lateensoft.pathfinder.toolkit.model.character.items;

import android.os.Parcel;
import android.os.Parcelable;

public class Weapon extends Item implements Parcelable {
    private int totalAttackBonus;
    private String damage;
    private String critical;
    private int range;
    private String specialProperties;
    private int ammunition;
    private WeaponType type;
    private Size size;
    
    public Weapon() {
        super();
        totalAttackBonus = 0;
        damage = "";
        critical = "x2";
        range = 5;
        specialProperties = "";
        ammunition = 0;
        type = WeaponType.SLASHING;
        size = Size.MEDIUM;
    }
    
    public Weapon(String name) {
        super(name);
    }

    public Weapon(Parcel in) {
        super(in);
        totalAttackBonus = in.readInt();
        damage = in.readString();
        critical = in.readString();
        range = in.readInt();
        specialProperties = in.readString();
        ammunition = in.readInt();
        type = WeaponType.forKey(in.readString());
        size = Size.forKey(in.readString());
    }
    
    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeInt(totalAttackBonus);
        out.writeString(damage);
        out.writeString(critical);
        out.writeInt(range);
        out.writeString(specialProperties);
        out.writeInt(ammunition);
        out.writeString(type.getKey());
        out.writeString(size.getKey());
    }

    public int getTotalAttackBonus() {
        return totalAttackBonus;
    }

    public void setTotalAttackBonus(int totalAttackBonus) {
        this.totalAttackBonus = totalAttackBonus;
    }
    
    public String getDamage() {
        return damage;
    }

    public void setDamage(String damage) {
        this.damage = damage;
    }

    public String getCritical() {
        return critical;
    }

    public void setCritical(String critical) {
        this.critical = critical;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public String getSpecialProperties() {
        return specialProperties;
    }

    public void setSpecialProperties(String specialProperties) {
        this.specialProperties = specialProperties;
    }

    public int getAmmunition() {
        return ammunition;
    }

    public void setAmmunition(int ammunition) {
        this.ammunition = ammunition;
    }

    public WeaponType getType() {
        return type;
    }

    public void setType(WeaponType type) {
        this.type = type;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
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

        if (ammunition != weapon.ammunition) return false;
        if (range != weapon.range) return false;
        if (totalAttackBonus != weapon.totalAttackBonus) return false;
        if (critical != null ? !critical.equals(weapon.critical) : weapon.critical != null) return false;
        if (damage != null ? !damage.equals(weapon.damage) : weapon.damage != null) return false;
        if (size != null ? !size.equals(weapon.size) : weapon.size != null) return false;
        if (specialProperties != null ? !specialProperties.equals(weapon.specialProperties) : weapon.specialProperties != null)
            return false;
        if (type != null ? !type.equals(weapon.type) : weapon.type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + totalAttackBonus;
        result = 31 * result + (damage != null ? damage.hashCode() : 0);
        result = 31 * result + (critical != null ? critical.hashCode() : 0);
        result = 31 * result + range;
        result = 31 * result + (specialProperties != null ? specialProperties.hashCode() : 0);
        result = 31 * result + ammunition;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (size != null ? size.hashCode() : 0);
        return result;
    }
}
