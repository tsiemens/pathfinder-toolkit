package com.lateensoft.pathfinder.toolkit.model.character;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.db.repository.PTStorable;

import android.content.Context;
import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;

public class PTFluffInfo implements Parcelable, PTStorable {
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
	
	public PTFluffInfo() {
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
		m_weight = new String("");
		m_eyes = new String("");
		m_hair = new String("");
		m_languages = new String("");
		m_description = new String("");
		
		m_characterId = 0;
	}
	
	public PTFluffInfo(Parcel in) {
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
	public void setID(long id) {
		m_characterId = id;
	}

	@Override
	public long getID() {
		return m_characterId;
	}
	
	public static final Parcelable.Creator<PTFluffInfo> CREATOR = new Parcelable.Creator<PTFluffInfo>() {
		public PTFluffInfo createFromParcel(Parcel in) {
			return new PTFluffInfo(in);
		}
		
		public PTFluffInfo[] newArray(int size) {
			return new PTFluffInfo[size];
		}
	};
}
