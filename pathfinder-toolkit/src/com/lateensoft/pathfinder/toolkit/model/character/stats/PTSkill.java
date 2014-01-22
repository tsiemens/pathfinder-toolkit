package com.lateensoft.pathfinder.toolkit.model.character.stats;

import com.lateensoft.pathfinder.toolkit.db.repository.PTStorable;

import android.os.Parcel;
import android.os.Parcelable;

public class PTSkill implements Parcelable, PTStorable, Comparable<PTSkill> {
	
	boolean m_classSkill;
	int m_rank;
	int m_miscMod;
	int m_armorCheckPenalty;
	int m_abilityKey;
	
	// For use with skills such as craft, professions, perform
	String m_subType;
	
	/** Defines the skill type/name, etc
	 * These are defined in PTSkillSet
	 */
	int m_skillKey;
	
	// A unique id for all skills in the database
	long m_id;
	long m_characterId;
	
	public PTSkill(int skillKey, int abilityKey) {
		this(UNSET_ID, UNSET_ID, skillKey, false, 0, 0, 0, abilityKey);
	}
	
	public PTSkill(long id, long characterId, int skillId, Boolean classSkill, int rank,
			int miscMod, int armorCheckPenalty, int abilityId) {
		this(id, characterId, skillId, null, classSkill, rank, miscMod,
				armorCheckPenalty, abilityId);
	}
	
	public PTSkill(long id, long characterId, int skillId, String subtype, Boolean classSkill,
			int rank, int miscMod, int armorCheckPenalty, int abilityId) {
		m_classSkill = classSkill;
		m_rank = rank;
		m_miscMod = miscMod;
		m_armorCheckPenalty = armorCheckPenalty;
		m_abilityKey = abilityId;
		m_skillKey = skillId;
		m_id = id;
		m_characterId = characterId;
		m_subType = subtype;
	}

	public PTSkill(Parcel in) {
		boolean[] classSkill = new boolean[1];
		in.readBooleanArray(classSkill);
		m_classSkill = classSkill[0];
		m_rank = in.readInt();
		m_miscMod = in.readInt();
		m_armorCheckPenalty = in.readInt();
		m_abilityKey = in.readInt();
		m_skillKey = in.readInt();
		m_characterId = in.readLong();
		m_id = in.readLong();
		m_subType = in.readString();
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		boolean[] classSkill = new boolean[1];
		classSkill[0] = m_classSkill;
		out.writeBooleanArray(classSkill);
		out.writeInt(m_rank);
		out.writeInt(m_miscMod);
		out.writeInt(m_armorCheckPenalty);
		out.writeInt(m_abilityKey);
		out.writeInt(m_skillKey);
		out.writeLong(m_characterId);
		out.writeLong(m_id);
		out.writeString(m_subType);
	}
	
	/**
	 * @return the classSkill
	 */
	public boolean isClassSkill() {
		return m_classSkill;
	}
	/**
	 * @param classSkill the classSkill to set
	 */
	public void setClassSkill(boolean classSkill) {
		m_classSkill = classSkill;
	}

	/**
	 * @param abilitySet The ability set of the character shared by the skill set
	 * @param maxDex maximum dex mod for the character
	 * @return the total skill mod for the skill
	 */
	public int getSkillMod(PTAbilitySet abilitySet, int maxDex) {
		int skillMod = getAbilityMod(abilitySet, maxDex) + m_rank 
			+ m_miscMod + m_armorCheckPenalty;
		
		if(m_classSkill && m_rank > 0)
			skillMod += 3;
		
		return skillMod;
	}

	public String getSubType() {
		return m_subType;
	}

	public void setSubType(String subType) {
		m_subType = subType;
	}

	/**
	 * @param abilitySet The ability set of the character shared by the skill set
	 * @param maxDex maximum dex mod for the character
	 * @return the value of the ability mod for the skill
	 */
	public int getAbilityMod(PTAbilitySet abilitySet, int maxDex) {
		int abilityMod = abilitySet.getAbility(m_abilityKey).getTempModifier();
		if (m_abilityKey == PTAbilitySet.KEY_DEX && abilityMod > maxDex) {
			return maxDex;
		} else {
			return abilityMod;
		}
	}
	
	/**
	 * @return the rank
	 */
	public int getRank() {
		return m_rank;
	}
	/**
	 * @param rank the rank to set
	 */
	public void setRank(int rank) {
		m_rank = rank;
	}
	/**
	 * @return the miscMod
	 */
	public int getMiscMod() {
		return m_miscMod;
	}
	/**
	 * @param miscMod the miscMod to set
	 */
	public void setMiscMod(int miscMod) {
		m_miscMod = miscMod;
	}
	/**
	 * @return the armorCheckPenalty
	 */
	public int getArmorCheckPenalty() {
		return m_armorCheckPenalty;
	}
	/**
	 * @param armorCheckPenalty the armorCheckPenalty to set
	 */
	public void setArmorCheckPenalty(int armorCheckPenalty) {
		m_armorCheckPenalty = armorCheckPenalty;
	}
	
	public int getAbilityKey() {
		return m_abilityKey;
	}
	
	public void setAbilityKey(int abilityKey) {
		m_abilityKey = abilityKey;
	}

	public int getSkillKey() {
		return m_skillKey;
	}

	public void setSkillKey(int skillId) {
		m_skillKey = skillId;
	}

	@Override
	public void setID(long id) {
		m_id = id;
	}
	
	/**
	 * This should not be used for identifying the type of skill, only the instance.
	 */
	@Override
	public long getID() {
		return m_id;
	}
	
	public void setCharacterID(long id) {
		m_characterId = id;
	}
	
	public long getCharacterID() {
		return m_characterId;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	public static final Parcelable.Creator<PTSkill> CREATOR = new Parcelable.Creator<PTSkill>() {
		public PTSkill createFromParcel(Parcel in) {
			return new PTSkill(in);
		}
		
		public PTSkill[] newArray(int size) {
			return new PTSkill[size];
		}
	};

	@Override
	public int compareTo(PTSkill another) {
		if (another.getSkillKey() == this.getSkillKey()) {
			if (this.getSubType() != null) {
				if (another.getSubType() != null && !another.getSubType().isEmpty()) {
					return this.getSubType().compareTo(another.getSubType());
				} else {
					return -1;
				}
			} else {
				return 1;
			}
		} else if (another.getSkillKey() > this.getSkillKey()) {
			return -1;
		} else {
			return 1;
		}
	}
}
