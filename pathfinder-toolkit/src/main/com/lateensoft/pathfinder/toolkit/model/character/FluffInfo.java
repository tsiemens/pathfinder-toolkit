package com.lateensoft.pathfinder.toolkit.model.character;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.dao.Identifiable;

import android.content.Context;
import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;

public class FluffInfo implements Parcelable, Identifiable {
	private String m_name;
	private String m_alignment;
	private String m_XP;
	private String m_nextLevelXP;
	private String m_playerClass;
	private String m_race;
	private String m_deity;
	private String m_level;
	private String m_size;
	private String m_gender;
	private String m_height;
	private String m_weight;
	private String m_eyes;
	private String m_hair;
	private String m_languages;
	private String m_description;
	
	private long m_characterId;
	
	public FluffInfo() {
		m_name = "";
		m_alignment = "";
		m_XP = "";
		m_nextLevelXP = "";
		m_playerClass = "";
		m_race = "";
		m_deity = "";
		m_level = "";
		m_size = "";
		m_gender = "";
		m_height = "";
		m_weight = "";
		m_eyes = "";
		m_hair = "";
		m_languages = "";
		m_description = "";
		
		m_characterId = UNSET_ID;
	}
	
	public FluffInfo(Parcel in) {
		m_name = in.readString();
		m_alignment = in.readString();
		m_XP = in.readString();
		m_nextLevelXP = in.readString();
		m_playerClass = in.readString();
		m_race = in.readString();
		m_deity = in.readString();
		m_level = in.readString();
		m_size = in.readString();
		m_gender = in.readString();
		m_height = in.readString();
		m_weight = in.readString();
		m_eyes = in.readString();
		m_hair = in.readString();
		m_languages = in.readString();
		m_description = in.readString();
		
		m_characterId = in.readLong();
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(m_name);
		out.writeString(m_alignment);
		out.writeString(m_XP);
		out.writeString(m_nextLevelXP);
		out.writeString(m_playerClass);
		out.writeString(m_race);
		out.writeString(m_deity);
		out.writeString(m_level);
		out.writeString(m_size);
		out.writeString(m_gender);
		out.writeString(m_height);
		out.writeString(m_weight);
		out.writeString(m_eyes);
		out.writeString(m_hair);
		out.writeString(m_languages);
		out.writeString(m_description);
		
		out.writeLong(m_characterId);
	}
	
	public String[] getFluffArray() {
		String[] fluffArray = {m_name, m_alignment, m_XP, m_nextLevelXP, m_playerClass, m_race, m_deity,
			m_level, m_size, m_gender, m_height, m_weight, m_eyes, m_hair, m_languages, m_description};
		return fluffArray;
	}
	
	public void setFluffByIndex(int index, String fluffValue) {
		switch (index) {
		case 0:
			m_name = fluffValue;
			break;
		case 1:
			m_alignment = fluffValue;
			break;
		case 2:
			m_XP = fluffValue;
			break;
		case 3:
			m_nextLevelXP = fluffValue;
			break;
		case 4:
			m_playerClass = fluffValue;
			break;
		case 5:
			m_race = fluffValue;
			break;
		case 6:
			m_deity = fluffValue;
			break;
		case 7:
			m_level = fluffValue;
			break;
		case 8:
			m_size = fluffValue;
			break;
		case 9:
			m_gender = fluffValue;
			break;
		case 10:
			m_height = fluffValue;
			break;
		case 11:
			m_weight = fluffValue;
			break;
		case 12:
			m_eyes = fluffValue;
			break;
		case 13:
			m_hair = fluffValue;
			break;
		case 14:
			m_languages = fluffValue;
			break;
		case 15:
			m_description = fluffValue;
			break;
		}
	}
	
	public String[] getFluffFields(Context context) {
		Resources r = context.getResources();
		
		return r.getStringArray(R.array.fluff_fields);		
	}
	

	public String getName() {
		return m_name;
	}
	
	public void setName(String name) {
		m_name = name;
	}
	
	public String getAlignment() {
		return m_alignment;
	}
	
	public void setAlignment(String alignment) {
		m_alignment = alignment;
	}
	
	public String getXP() {
		return m_XP;
	}
	
	public void setXP(String XP) {
		m_XP = XP;
	}
	
	public String getNextLevelXP() {
		return m_nextLevelXP;
	}

	public void setNextLevelXP(String nextLevelXP) {
		m_nextLevelXP = nextLevelXP;
	}

	public String getPlayerClass() {
		return m_playerClass;
	}
	
	public void setPlayerClass(String playerClass) {
		m_playerClass = playerClass;
	}
	
	public String getRace() {
		return m_race;
	}
	
	public String getDeity() {
		return m_deity;
	}
	
	public void setDeity(String deity) {
		m_deity = deity;
	}
	
	public String getLevel() {
		return m_level;
	}
	
	public void setLevel(String level) {
		m_level = level;
	}
	
	public String getSize() {
		return m_size;
	}

	public void setSize(String size) {
		m_size = size;
	}

	public String getGender() {
		return m_gender;
	}
	
	public void setGender(String gender) {
		m_gender = gender;
	}
	
	public String getHeight() {
		return m_height;
	}
	
	public void setHeight(String height) {
		m_height = height;
	}
	
	public String getWeight() {
		return m_weight;
	}
	
	public void setWeight(String weight) {
		m_weight = weight;
	}
	
	public String getEyes() {
		return m_eyes;
	}
	
	public void setEyes(String eyes) {
		m_eyes = eyes;
	}
	
	public String getHair() {
		return m_hair;
	}
	
	public void setHair(String hair) {
		m_hair = hair;
	}
	
	public void setRace(String race) {
		m_race = race;
	}
	
	public String getLanguages() {
		return m_languages;
	}

	public void setLanguages(String languages) {
		m_languages = languages;
	}

	public String getDescription() {
		return m_description;
	}

	public void setDescription(String description) {
		m_description = description;
	}

	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void setId(long id) {
		m_characterId = id;
	}

	@Override
	public long getId() {
		return m_characterId;
	}
	
	public static final Parcelable.Creator<FluffInfo> CREATOR = new Parcelable.Creator<FluffInfo>() {
		public FluffInfo createFromParcel(Parcel in) {
			return new FluffInfo(in);
		}
		
		public FluffInfo[] newArray(int size) {
			return new FluffInfo[size];
		}
	};

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FluffInfo)) return false;

        FluffInfo fluffInfo = (FluffInfo) o;

        if (m_characterId != fluffInfo.m_characterId) return false;
        if (m_XP != null ? !m_XP.equals(fluffInfo.m_XP) : fluffInfo.m_XP != null) return false;
        if (m_alignment != null ? !m_alignment.equals(fluffInfo.m_alignment) : fluffInfo.m_alignment != null)
            return false;
        if (m_deity != null ? !m_deity.equals(fluffInfo.m_deity) : fluffInfo.m_deity != null) return false;
        if (m_description != null ? !m_description.equals(fluffInfo.m_description) : fluffInfo.m_description != null)
            return false;
        if (m_eyes != null ? !m_eyes.equals(fluffInfo.m_eyes) : fluffInfo.m_eyes != null) return false;
        if (m_gender != null ? !m_gender.equals(fluffInfo.m_gender) : fluffInfo.m_gender != null) return false;
        if (m_hair != null ? !m_hair.equals(fluffInfo.m_hair) : fluffInfo.m_hair != null) return false;
        if (m_height != null ? !m_height.equals(fluffInfo.m_height) : fluffInfo.m_height != null) return false;
        if (m_languages != null ? !m_languages.equals(fluffInfo.m_languages) : fluffInfo.m_languages != null)
            return false;
        if (m_level != null ? !m_level.equals(fluffInfo.m_level) : fluffInfo.m_level != null) return false;
        if (m_name != null ? !m_name.equals(fluffInfo.m_name) : fluffInfo.m_name != null) return false;
        if (m_nextLevelXP != null ? !m_nextLevelXP.equals(fluffInfo.m_nextLevelXP) : fluffInfo.m_nextLevelXP != null)
            return false;
        if (m_playerClass != null ? !m_playerClass.equals(fluffInfo.m_playerClass) : fluffInfo.m_playerClass != null)
            return false;
        if (m_race != null ? !m_race.equals(fluffInfo.m_race) : fluffInfo.m_race != null) return false;
        if (m_size != null ? !m_size.equals(fluffInfo.m_size) : fluffInfo.m_size != null) return false;
        if (m_weight != null ? !m_weight.equals(fluffInfo.m_weight) : fluffInfo.m_weight != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = m_name != null ? m_name.hashCode() : 0;
        result = 31 * result + (m_alignment != null ? m_alignment.hashCode() : 0);
        result = 31 * result + (m_XP != null ? m_XP.hashCode() : 0);
        result = 31 * result + (m_nextLevelXP != null ? m_nextLevelXP.hashCode() : 0);
        result = 31 * result + (m_playerClass != null ? m_playerClass.hashCode() : 0);
        result = 31 * result + (m_race != null ? m_race.hashCode() : 0);
        result = 31 * result + (m_deity != null ? m_deity.hashCode() : 0);
        result = 31 * result + (m_level != null ? m_level.hashCode() : 0);
        result = 31 * result + (m_size != null ? m_size.hashCode() : 0);
        result = 31 * result + (m_gender != null ? m_gender.hashCode() : 0);
        result = 31 * result + (m_height != null ? m_height.hashCode() : 0);
        result = 31 * result + (m_weight != null ? m_weight.hashCode() : 0);
        result = 31 * result + (m_eyes != null ? m_eyes.hashCode() : 0);
        result = 31 * result + (m_hair != null ? m_hair.hashCode() : 0);
        result = 31 * result + (m_languages != null ? m_languages.hashCode() : 0);
        result = 31 * result + (m_description != null ? m_description.hashCode() : 0);
        result = 31 * result + (int) (m_characterId ^ (m_characterId >>> 32));
        return result;
    }
}
