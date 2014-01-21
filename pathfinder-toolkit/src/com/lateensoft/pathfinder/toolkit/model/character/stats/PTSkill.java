package com.lateensoft.pathfinder.toolkit.model.character.stats;

import com.lateensoft.pathfinder.toolkit.db.repository.PTStorable;

import android.os.Parcel;
import android.os.Parcelable;

public class PTSkill implements Parcelable, PTStorable {
	
	boolean m_classSkill;
	int m_rank;
	int m_miscMod;
	int m_armorCheckPenalty;
	long m_abilityId;
	
	long m_skillId;
	long m_characterId;
	
	public PTSkill(long skillId, long abilityKey) {
		this(skillId, UNSET_ID, false, 0, 0, 0, abilityKey);
	}
	
	public PTSkill(long skillId, long characterId, Boolean classSkill, int rank,
			int miscMod, int armorCheckPenalty, long abilityId) {
		m_classSkill = classSkill;
		m_rank = rank;
		m_miscMod = miscMod;
		m_armorCheckPenalty = armorCheckPenalty;
		m_abilityId = abilityId;
		m_skillId = skillId;
		m_characterId = characterId;
	}

	public PTSkill(Parcel in) {
		boolean[] classSkill = new boolean[1];
		in.readBooleanArray(classSkill);
		m_classSkill = classSkill[0];
		m_rank = in.readInt();
		m_miscMod = in.readInt();
		m_armorCheckPenalty = in.readInt();
		m_abilityId = in.readLong();
		m_skillId = in.readLong();
		m_characterId = in.readLong();
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		boolean[] classSkill = new boolean[1];
		classSkill[0] = m_classSkill;
		out.writeBooleanArray(classSkill);
		out.writeInt(m_rank);
		out.writeInt(m_miscMod);
		out.writeInt(m_armorCheckPenalty);
		out.writeLong(m_abilityId);
		out.writeLong(m_skillId);
		out.writeLong(m_characterId);
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

	/**
	 * @param abilitySet The ability set of the character shared by the skill set
	 * @param maxDex maximum dex mod for the character
	 * @return the value of the ability mod for the skill
	 */
	public int getAbilityMod(PTAbilitySet abilitySet, int maxDex) {
		int abilityMod = abilitySet.getAbility(m_abilityId).getTempModifier();
		if (m_abilityId == PTAbilitySet.ID_DEX && abilityMod > maxDex) {
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
	
	public long getAbilityId() {
		return m_abilityId;
	}
	
	public void setAbilityId(long abilityId) {
		m_abilityId = abilityId;
	}

	@Override
	public void setID(long id) {
		m_skillId = id;
	}
	
	@Override
	public long getID() {
		return m_skillId;
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
}
