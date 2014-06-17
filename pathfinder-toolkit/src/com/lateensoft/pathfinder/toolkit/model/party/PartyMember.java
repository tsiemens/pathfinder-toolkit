package com.lateensoft.pathfinder.toolkit.model.party;

import com.google.common.base.Preconditions;
import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.dao.Identifiable;

import android.content.Context;
import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

@Deprecated
public class PartyMember implements Parcelable, Identifiable {
	
	private String m_name;
	private int m_initiative;
	private int m_AC;
	private int m_touch;
	private int m_flatFooted;
	private int m_spellResist;
	private int m_damageReduction;
	private int m_CMD;
	private int m_fortSave;
	private int m_reflexSave;
	private int m_willSave;
	private int m_bluffSkillBonus;
	private int m_disguiseSkillBonus;
	private int m_perceptionSkillBonus;
	private int m_senseMotiveSkillBonus;
	private int m_stealthSkillBonus;
	
	private int m_lastRolledValue;
	
	private long m_id;
	private long m_partyId;
	
	public PartyMember(String name){
		m_name = (name != null) ? name : "";
		m_initiative = 0;
		m_AC = 0;
		m_touch = 0;
		m_flatFooted = 0;
		m_spellResist = 0;
		m_damageReduction = 0;
		m_CMD = 0;
		m_fortSave = 0;
		m_reflexSave = 0;
		m_willSave = 0;
		m_bluffSkillBonus = 0;
		m_disguiseSkillBonus = 0;
		m_perceptionSkillBonus = 0;
		m_senseMotiveSkillBonus = 0;
		m_stealthSkillBonus = 0;
		
		m_lastRolledValue = 0; //Will be set in roller activities only
	}

	/**
	 * returns a deep copy of memberToCopy
	 */
	public PartyMember(PartyMember memberToCopy){
		m_name = memberToCopy.getName();
		m_initiative = memberToCopy.getInitiative();
		m_AC = memberToCopy.getAC();
		m_touch = memberToCopy.getTouch();
		m_flatFooted = memberToCopy.getFlatFooted();
		m_spellResist = memberToCopy.getSpellResist();
		m_damageReduction = memberToCopy.getDamageReduction();
		m_CMD = memberToCopy.getCMD();
		m_fortSave = memberToCopy.getFortSave();
		m_reflexSave = memberToCopy.getReflexSave();
		m_willSave = memberToCopy.getWillSave();
		m_bluffSkillBonus = memberToCopy.getBluffSkillBonus();
		m_disguiseSkillBonus = memberToCopy.getDisguiseSkillBonus();
		m_perceptionSkillBonus = memberToCopy.getPerceptionSkillBonus();
		m_senseMotiveSkillBonus = memberToCopy.getSenseMotiveSkillBonus();
		m_stealthSkillBonus = memberToCopy.getStealthSkillBonus();
		m_lastRolledValue = memberToCopy.getLastRolledValue();
	}
	
	public PartyMember(Parcel in) {
		m_name = in.readString();
		m_initiative = in.readInt();
		m_AC = in.readInt();
		m_touch = in.readInt();
		m_flatFooted = in.readInt();
		m_spellResist = in.readInt();
		m_damageReduction = in.readInt();
		m_CMD = in.readInt();
		m_fortSave = in.readInt();
		m_reflexSave = in.readInt();
		m_willSave = in.readInt();
		m_bluffSkillBonus = in.readInt();
		m_disguiseSkillBonus = in.readInt();
		m_perceptionSkillBonus = in.readInt();
		m_senseMotiveSkillBonus = in.readInt();
		m_stealthSkillBonus = in.readInt();
		m_lastRolledValue = in.readInt();
		
		m_id = in.readLong();
		m_partyId = in.readLong();
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(m_name);
		out.writeInt(m_initiative);
		out.writeInt(m_AC);
		out.writeInt(m_touch);
		out.writeInt(m_flatFooted);
		out.writeInt(m_spellResist);
		out.writeInt(m_damageReduction);
		out.writeInt(m_CMD);
		out.writeInt(m_fortSave);
		out.writeInt(m_reflexSave);
		out.writeInt(m_willSave);
		out.writeInt(m_bluffSkillBonus);
		out.writeInt(m_disguiseSkillBonus);
		out.writeInt(m_perceptionSkillBonus);
		out.writeInt(m_senseMotiveSkillBonus);
		out.writeInt(m_stealthSkillBonus);
		out.writeInt(m_lastRolledValue);
		
		out.writeLong(m_id);
		out.writeLong(m_partyId);
	}


	/**
	 * 
	 * @return an array of the party member's stats, ordered as: 
	 * 0:Initiative,
	 * 1:AC, 
	 * 2:Touch, 
	 * 3:Flat Footed, 
	 * 4:Spell Resist, 
	 * 5:Damage Reduction, 
	 * 6:CMD,
	 * 7:Fort save
	 * 8:Reflex save
	 * 9:Will Save
	 * 10:Bluff,
	 * 11:Disguise, 
	 * 12:Perception Skill Bonus,
	 * 13:Sense Motive,
	 * 14:Stealth
	 */
	public int[] getStatsArray(){
		int[] stats = {m_initiative, m_AC, m_touch, m_flatFooted, m_spellResist, m_damageReduction, m_CMD, m_fortSave, m_reflexSave, m_willSave, 
				m_bluffSkillBonus, m_disguiseSkillBonus, m_perceptionSkillBonus, m_senseMotiveSkillBonus, m_stealthSkillBonus};
		return stats;
	}
	
	/**
	 * sets a stat by its assigned "index", ordered as:
	 * 0:Initiative
	 * 1:AC, 
	 * 2:Touch, 
	 * 3:Flat Footed, 
	 * 4:Spell Resist, 
	 * 5:Damage Reduction, 
	 * 6:CMD, 
	 * 7:Bluff,
	 * 8:Disguise, 
	 * 9:Perception Skill Bonus,
	 * 10:Sense Motive,
	 * 11:Stealth
	 * @param index
	 * @param value
	 */
	public void setStatByIndex(int index, int value){
		switch(index){
		case 0:
			m_initiative = value;
			break;
		case 1:
			m_AC = value;
			break;
		case 2:
			m_touch = value;
			break;
		case 3:
			m_flatFooted = value;
			break;
		case 4:
			m_spellResist = value;
			break;
		case 5:
			m_damageReduction = value;
			break;
		case 6:
			m_CMD = value;
			break;
		case 7:
			m_fortSave = value;
			break;
		case 8:
			m_reflexSave = value;
			break;
		case 9:
			m_willSave = value;
			break;
		case 10:
			m_bluffSkillBonus = value;
			break;
		case 11:
			m_disguiseSkillBonus = value;
			break;
		case 12:
			m_perceptionSkillBonus = value;
			break;
		case 13:
			m_senseMotiveSkillBonus = value;
			break;
		case 14:
			m_stealthSkillBonus = value;
			break;
		}
	}
	
	/**
	 * 
	 * @param index
	 * @return the value of the stat with "index", as follows:
	 * 0:Initiative
	 * 1:AC, 
	 * 2:Touch, 
	 * 3:Flat Footed, 
	 * 4:Spell Resist, 
	 * 5:Damage Reduction, 
	 * 6:CMD,
	 * 7:Fort save
	 * 8:Reflex save
	 * 9:Will save
	 * 10:Bluff,
	 * 11:Disguise, 
	 * 12:Perception Skill Bonus,
	 * 13:Sense Motive,
	 * 14:Stealth
	 * 
	 */
	public int getValueByIndex(int index){
		switch(index){
		case 0:
			return m_initiative;
		case 1:
			return m_AC;
		case 2:
			return m_touch;
		case 3:
			return m_flatFooted;
		case 4:
			return m_spellResist;
		case 5:
			return m_damageReduction;
		case 6:
			return m_CMD;
		case 7:
			return m_fortSave;
		case 8:
			return m_reflexSave;
		case 9:
			return m_willSave;
		case 10:
			return m_bluffSkillBonus;
		case 11:
			return m_disguiseSkillBonus;
		case 12:
			return m_perceptionSkillBonus;
		case 13:
			return m_senseMotiveSkillBonus;
		case 14:
			return m_stealthSkillBonus;
		default:
			return 0;
		}
	}
	
	public String[] getStatFields(Context context){
		Resources r = context.getResources();
		return r.getStringArray(R.array.party_member_stats);
	}
	
	public String getName() {
		return m_name;
	}

	public void setName(@NotNull String name) {
        Preconditions.checkNotNull(name);
	    m_name = name;
	}
	
	public int getInitiative() {
		return m_initiative;
	}

	public void setInitiative(int initiative) {
		this.m_initiative = initiative;
	}

	public int getAC() {
		return m_AC;
	}

	public void setAC(int AC) {
		this.m_AC = AC;
	}

	public int getTouch() {
		return m_touch;
	}

	public void setTouch(int touch) {
		this.m_touch = touch;
	}

	public int getFlatFooted() {
		return m_flatFooted;
	}

	public void setFlatFooted(int flatFooted) {
		this.m_flatFooted = flatFooted;
	}

	public int getSpellResist() {
		return m_spellResist;
	}

	public void setSpellResist(int spellResist) {
		this.m_spellResist = spellResist;
	}

	public int getDamageReduction() {
		return m_damageReduction;
	}

	public void setDamageReduction(int damageReduction) {
		this.m_damageReduction = damageReduction;
	}

	public int getCMD() {
		return m_CMD;
	}

	public void setCMD(int CMD) {
		this.m_CMD = CMD;
	}
	
	public int getFortSave() {
		return m_fortSave;
	}

	public void setFortSave(int FortSave) {
		this.m_fortSave = FortSave;
	}
	
	public int getReflexSave() {
		return m_reflexSave;
	}

	public void setReflexSave(int reflexSave) {
		this.m_reflexSave = reflexSave;
	}
	
	public int getWillSave() {
		return m_willSave;
	}

	public void setWillSave(int willSave) {
		this.m_willSave = willSave;
	}

	public int getBluffSkillBonus() {
		return m_bluffSkillBonus;
	}

	public void setBluffSkillBonus(int bluffSkillBonus) {
		this.m_bluffSkillBonus = bluffSkillBonus;
	}
	
	public int getDisguiseSkillBonus() {
		return m_disguiseSkillBonus;
	}

	public void setDisguiseSkillBonus(int disguiseSkillBonus) {
		this.m_disguiseSkillBonus = disguiseSkillBonus;
	}
	
	public int getPerceptionSkillBonus() {
		return m_perceptionSkillBonus;
	}

	public void setPerceptionSkillBonus(int perceptionSkillBonus) {
		this.m_perceptionSkillBonus = perceptionSkillBonus;
	}
	
	public int getSenseMotiveSkillBonus() {
		return m_senseMotiveSkillBonus;
	}

	public void setSenseMotiveSkillBonus(int senseMotiveSkillBonus) {
		this.m_senseMotiveSkillBonus = senseMotiveSkillBonus;
	}
	
	public int getStealthSkillBonus() {
		return m_stealthSkillBonus;
	}

	public void setStealthSkillBonus(int stealthSkillBonus) {
		this.m_stealthSkillBonus = stealthSkillBonus;
	}

	public int getLastRolledValue() {
		return m_lastRolledValue;
	}

	public void setLastRolledValue(int rolledVal) {
		this.m_lastRolledValue = rolledVal;
	}
	
	public void setPartyID(long id) {
		m_partyId = id;
	}

	public long getPartyID() {
		return m_partyId;
	}
	
	@Override
	public void setId(long id) {
		m_id = id;
	}

	@Override
	public long getId() {
		return m_id;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	public static final Parcelable.Creator<PartyMember> CREATOR = new Parcelable.Creator<PartyMember>() {
		public PartyMember createFromParcel(Parcel in) {
			return new PartyMember(in);
		}
		
		public PartyMember[] newArray(int size) {
			return new PartyMember[size];
		}
	};

    public static class NameComparator implements Comparator<PartyMember> {
        @Override public int compare(PartyMember lhs, PartyMember rhs) {
            return lhs.getName().compareTo(rhs.getName());
        }
    }

    public static class RollComparator implements Comparator<PartyMember> {
        @Override public int compare(PartyMember lhs, PartyMember rhs) {
            return rhs.getLastRolledValue() - lhs.getLastRolledValue();
        }
    }
}
