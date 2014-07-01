package com.lateensoft.pathfinder.toolkit.model.character.items;

import android.os.Parcel;
import android.os.Parcelable;

public class Armor extends Item {
    private boolean worn;
    private int ACBonus;
    private int checkPen;
    private int maxDex;
    private int spellFail;
    private int speed;
    private String specialProperties;
    private Size size;
    
    public Armor() {
        super();
        worn =  true;
        ACBonus = 0;
        checkPen = 0;
        maxDex = 10;
        spellFail = 0;
        speed = 30;
        specialProperties = "";
        size = Size.MEDIUM;
    }
    
    public Armor(Parcel in) {
        super(in);
        boolean[] wornAr = new boolean[1];
        in.readBooleanArray(wornAr);
        this.worn = wornAr[0];
        ACBonus = in.readInt();
        checkPen = in.readInt();
        maxDex = in.readInt();
        spellFail = in.readInt();
        speed = in.readInt();
        specialProperties = in.readString();
        size = Size.forKey(in.readString());
    }
    
    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        boolean[] wornAr = new boolean[1];
        wornAr[0] = worn;
        out.writeBooleanArray(wornAr);
        out.writeInt(ACBonus);
        out.writeInt(checkPen);
        out.writeInt(maxDex);
        out.writeInt(spellFail);
        out.writeInt(speed);
        out.writeString(specialProperties);
        out.writeString(size.getKey());
    }
    
    public void setSize(Size size) {
        this.size = size;
    }
    
    public Size getSize() {
        return size;
    }
    
    public void setSpecialProperties(String properties) {
        specialProperties = properties;
    }
    
    public String getSpecialProperties() {
        return specialProperties;
    }
        
    public boolean isWorn() {
        return worn;
    }

    public void setWorn(boolean worn) {
        this.worn = worn;
    }

    public int getACBonus() {
        return ACBonus;
    }

    public void setACBonus(int ACBonus) {
        this.ACBonus = ACBonus;
    }
    
    public int getCheckPen() {
        return checkPen;
    }
    
    public void setCheckPen(int checkPen) {
        this.checkPen = checkPen;
    }
    
    public int getMaxDex() {
        return maxDex;
    }
    
    public void setMaxDex(int maxDex) {
        this.maxDex = maxDex;
    }
    
    public int getSpellFail() {
        return spellFail;
    }
    
    public void setSpellFail(int spellFail) {
        this.spellFail = spellFail;
    }
    
    public int getSpeed() {
        return speed;
    }
    
    public void setSpeed(int speed) {
        this.speed = speed;
    }
    
    public String getSpeedString() {
        String speedString = Integer.toString(speed) + " ft.";
        return speedString;
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

        if (ACBonus != armor.ACBonus) return false;
        if (checkPen != armor.checkPen) return false;
        if (maxDex != armor.maxDex) return false;
        if (speed != armor.speed) return false;
        if (spellFail != armor.spellFail) return false;
        if (worn != armor.worn) return false;
        if (size != null ? !size.equals(armor.size) : armor.size != null) return false;
        if (specialProperties != null ? !specialProperties.equals(armor.specialProperties) : armor.specialProperties != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (worn ? 1 : 0);
        result = 31 * result + ACBonus;
        result = 31 * result + checkPen;
        result = 31 * result + maxDex;
        result = 31 * result + spellFail;
        result = 31 * result + speed;
        result = 31 * result + (specialProperties != null ? specialProperties.hashCode() : 0);
        result = 31 * result + (size != null ? size.hashCode() : 0);
        return result;
    }
}
