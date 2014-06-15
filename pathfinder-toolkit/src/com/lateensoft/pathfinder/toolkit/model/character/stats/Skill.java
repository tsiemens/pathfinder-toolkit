package com.lateensoft.pathfinder.toolkit.model.character.stats;

import com.lateensoft.pathfinder.toolkit.db.dao.Identifiable;

import android.os.Parcel;
import android.os.Parcelable;

public class Skill implements Parcelable, Identifiable, Comparable<Skill> {
	
	boolean m_classSkill;
	int m_rank;
	int m_miscMod;
	int m_abilityKey;
	
	// For use with skills such as craft, professions, perform
	String m_subType;
	
	/** Defines the skill type/name, etc
	 * These are defined in SkillSet
	 */
	int m_skillKey;
	
	// A unique id for all skills in the database
	long m_id;
	long m_characterId;
	
	public Skill(int skillKey, int abilityKey) {
		this(UNSET_ID, UNSET_ID, skillKey, false, 0, 0, abilityKey);
	}
	
	public Skill(long id, long characterId, int skillId, Boolean classSkill, int rank,
                 int miscMod, int abilityId) {
		this(id, characterId, skillId, null, classSkill, rank, miscMod, abilityId);
	}
	
	public Skill(long id, long characterId, int skillId, String subtype, Boolean classSkill,
                 int rank, int miscMod, int abilityId) {
		m_classSkill = classSkill;
		m_rank = rank;
		m_miscMod = miscMod;
		m_abilityKey = abilityId;
		m_skillKey = skillId;
		m_id = id;
		m_characterId = characterId;
		m_subType = subtype;
	}

	public Skill(Parcel in) {
		boolean[] classSkill = new boolean[1];
		in.readBooleanArray(classSkill);
		m_classSkill = classSkill[0];
		m_rank = in.readInt();
		m_miscMod = in.readInt();
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
	 * @param armorCheckPenalty a negative value to be applied when DEX or STR based
	 * @return the total skill mod for the skill
	 */
	public int getSkillMod(AbilitySet abilitySet, int maxDex, int armorCheckPenalty) {
		int skillMod = abilitySet.getTotalAbilityMod(m_abilityKey, maxDex) + m_rank 
			+ m_miscMod;
		
		if(m_classSkill && m_rank > 0)
			skillMod += 3;
		
		if (m_abilityKey == AbilitySet.KEY_DEX || m_abilityKey == AbilitySet.KEY_STR) {
			skillMod += armorCheckPenalty;
		}
		
		return skillMod;
	}

	public String getSubType() {
		return m_subType;
	}

	public void setSubType(String subType) {
		m_subType = subType;
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
	public void setId(long id) {
		m_id = id;
	}
	
	/**
	 * This should not be used for identifying the type of skill, only the instance.
	 */
	@Override
	public long getId() {
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
	
	public static final Parcelable.Creator<Skill> CREATOR = new Parcelable.Creator<Skill>() {
		public Skill createFromParcel(Parcel in) {
			return new Skill(in);
		}
		
		public Skill[] newArray(int size) {
			return new Skill[size];
		}
	};

	@Override
	public int compareTo(Skill another) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Skill)) return false;

        Skill skill = (Skill) o;

        if (m_abilityKey != skill.m_abilityKey) return false;
        if (m_characterId != skill.m_characterId) return false;
        if (m_classSkill != skill.m_classSkill) return false;
        if (m_id != skill.m_id) return false;
        if (m_miscMod != skill.m_miscMod) return false;
        if (m_rank != skill.m_rank) return false;
        if (m_skillKey != skill.m_skillKey) return false;
        if (m_subType != null ? !m_subType.equals(skill.m_subType) : skill.m_subType != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (m_classSkill ? 1 : 0);
        result = 31 * result + m_rank;
        result = 31 * result + m_miscMod;
        result = 31 * result + m_abilityKey;
        result = 31 * result + (m_subType != null ? m_subType.hashCode() : 0);
        result = 31 * result + m_skillKey;
        result = 31 * result + (int) (m_id ^ (m_id >>> 32));
        result = 31 * result + (int) (m_characterId ^ (m_characterId >>> 32));
        return result;
    }
}
