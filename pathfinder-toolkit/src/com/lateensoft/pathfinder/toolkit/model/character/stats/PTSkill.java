package com.lateensoft.pathfinder.toolkit.model.character.stats;

import com.lateensoft.pathfinder.toolkit.db.repository.PTStorable;

import android.os.Parcel;
import android.os.Parcelable;

public class PTSkill implements Parcelable, PTStorable {
	
	String m_name;
	boolean m_classSkill;
	String m_keyAbility;
	int m_abilityMod;
	int m_rank;
	int m_miscMod;
	int m_armorCheckPenalty;
	int m_keyAbilityKey;
	long m_id;
	long m_characterId;
	
	public PTSkill() {
		m_name = "";
		m_classSkill = false;
		m_keyAbility = "";
		m_abilityMod = 0;
		m_rank = 0;
		m_miscMod = 0;
		m_armorCheckPenalty = 0;
		m_keyAbilityKey = 0;
	}
	
	public PTSkill(int id, int characterId, String name, int abilityKey, String abilityString) {
		this(id, name, abilityKey, abilityString);
		m_characterId = characterId;
	}
	
	public PTSkill(int id, String name, int abilityKey, String abilityString) {
		this(name, abilityKey, abilityString);
		m_id = id;
	}
	
	public PTSkill(String name, int abilityKey, String abilityString) {
		m_name = name;
		m_classSkill = false;
		m_abilityMod = 0;
		m_rank = 0;
		m_miscMod = 0;
		m_armorCheckPenalty = 0;
		m_keyAbilityKey = abilityKey;
		m_keyAbility = abilityString;
	}
	
	public PTSkill(long id, long characterId, String name, Boolean classSkill, int abilityMod, int rank,
			int miscMod, int armorCheckPenalty, int abilityKey, String keyAbility) {
		m_name = name;
		m_classSkill = false;
		m_abilityMod = 0;
		m_rank = 0;
		m_miscMod = 0;
		m_armorCheckPenalty = 0;
		m_keyAbilityKey = abilityKey;
		m_keyAbility = keyAbility;
		m_id = id;
		m_characterId = characterId;
	}

	public PTSkill(Parcel in) {
		m_name = in.readString();
		boolean[] classSkill = new boolean[1];
		in.readBooleanArray(classSkill);
		m_classSkill = classSkill[0];
		m_keyAbility = in.readString();
		m_abilityMod = in.readInt();
		m_rank = in.readInt();
		m_miscMod = in.readInt();
		m_armorCheckPenalty = in.readInt();
		m_keyAbilityKey = in.readInt();
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(m_name);
		boolean[] classSkill = new boolean[1];
		classSkill[0] = m_classSkill;
		out.writeBooleanArray(classSkill);
		out.writeString(m_keyAbility);
		out.writeInt(m_abilityMod);
		out.writeInt(m_rank);
		out.writeInt(m_miscMod);
		out.writeInt(m_armorCheckPenalty);
		out.writeInt(m_keyAbilityKey);
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return m_name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		m_name = name;
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
	 * @return the keyAbility
	 */
	public String getKeyAbility() {
		return m_keyAbility;
	}
	/**
	 * @param keyAbility the keyAbility to set
	 */
	public void setKeyAbility(String keyAbility) {
		m_keyAbility = keyAbility;
	}
	/**
	 * @return the total skill Mod
	 */
	public int getSkillMod() {
		int skillMod = 0;
		skillMod = skillMod + m_abilityMod + m_rank + m_miscMod + m_armorCheckPenalty;
		
		if(m_classSkill && m_rank > 0)
			skillMod += 3;
		
		return skillMod;
	}

	/**
	 * @return the abilityMod
	 */
	public int getAbilityMod() {
		return m_abilityMod;
	}
	/**
	 * @param abilityMod the abilityMod to set
	 */
	public void setAbilityMod(int abilityMod) {
		m_abilityMod = abilityMod;
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
	
	public int getKeyAbilityKey() {
		return m_keyAbilityKey;
	}
	
	public void setKeyAbilityKey(int keyAbilityKey) {
		m_keyAbilityKey = keyAbilityKey;
	}

	@Override
	public void setID(long id) {
		m_id = id;
	}
	
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
}
