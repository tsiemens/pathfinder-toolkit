package com.lateensoft.pathfinder.toolkit.model.character.stats;

import com.lateensoft.pathfinder.toolkit.dao.Identifiable;

import android.os.Parcel;
import android.os.Parcelable;

public class Save implements TypedStat<SaveType>, Parcelable, Identifiable, Comparable<Save> {
	private long m_characterId;
	
	private final SaveType type;

	private int m_baseSave;
	private AbilityType ability;
	private int m_magicMod;
	private int m_miscMod;
	private int m_tempMod;

    public Save(SaveType saveType) {
        this(saveType, saveType.getDefaultAbility());
    }

	public Save(SaveType saveKey, AbilityType abilityKey) {
		this(saveKey, UNSET_ID, abilityKey);
	}
	
	public Save(SaveType saveKey, long characterId, AbilityType abilityKey) {
		this(saveKey, characterId, 0, abilityKey, 0, 0, 0);
	}
	
	public Save(SaveType saveKey, long characterId, int baseSave, AbilityType abilityKey, int magicMod,
                int miscMod, int tempMod) {
		m_characterId = characterId;
		type = saveKey;
		ability = abilityKey;
		m_baseSave = baseSave;
		m_magicMod = magicMod;
		m_miscMod = miscMod;
		m_tempMod = tempMod;
	}
	
	public Save(Parcel in) {
		type = SaveType.forKey(in.readInt());
		m_baseSave = in.readInt();
		ability = AbilityType.forKey(in.readInt());
		m_magicMod = in.readInt();
		m_miscMod = in.readInt();
		m_tempMod = in.readInt();
		m_characterId = in.readLong();
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(type.getKey());
		out.writeInt(m_baseSave);
		out.writeInt(ability.getKey());
		out.writeInt(m_magicMod);
		out.writeInt(m_miscMod);
		out.writeInt(m_tempMod);
		out.writeLong(m_characterId);
	}

    @Override
    public SaveType getType() {
        return type;
    }
	
	public int getBaseSave() {
		return m_baseSave;
	}

	public void setBaseSave(int baseSave) {
		m_baseSave = baseSave;
	}

	public AbilityType getAbilityType() {
		return ability;
	}
	
	public int getMagicMod() {
		return m_magicMod;
	}
	
	public int getMiscMod() {
		return m_miscMod;
	}
	
	public int getTempMod() {
		return m_tempMod;
	}
	
	public int getTotal(AbilitySet abilitySet, int maxDex) {
		int total = 0;
		total += m_baseSave;
		total += abilitySet.getTotalAbilityMod(ability, maxDex);
		total += m_magicMod;
		total += m_miscMod;
		total += m_tempMod;
		return total;
	}
	
	public void setAbilityType(AbilityType abilityType) {
		ability = abilityType;
	}
	
	public void setMagicMod(int magicMod) {
		m_magicMod = magicMod;
	}
	
	public void setMiscMod(int miscMod) {
		m_miscMod = miscMod;
	}
	
	public void setTempMod(int tempMod) {
		m_tempMod = tempMod;
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
	
	public static final Parcelable.Creator<Save> CREATOR = new Parcelable.Creator<Save>() {
		public Save createFromParcel(Parcel in) {
			return new Save(in);
		}
		
		public Save[] newArray(int size) {
			return new Save[size];
		}
	};

	@Override
    @Deprecated // Is a owned weak item. does not need to be identifiable
	public void setId(long id) {
//		setSaveKey((int) id);
	}

	@Override
	public long getId() {
		return getType().getKey();
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Save)) return false;

        Save save = (Save) o;

        if (ability != save.ability) return false;
        if (m_baseSave != save.m_baseSave) return false;
        if (m_characterId != save.m_characterId) return false;
        if (m_magicMod != save.m_magicMod) return false;
        if (m_miscMod != save.m_miscMod) return false;
        if (type != save.type) return false;
        if (m_tempMod != save.m_tempMod) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (m_characterId ^ (m_characterId >>> 32));
        result = 31 * result + type.getKey();
        result = 31 * result + m_baseSave;
        result = 31 * result + ability.getKey();
        result = 31 * result + m_magicMod;
        result = 31 * result + m_miscMod;
        result = 31 * result + m_tempMod;
        return result;
    }

    @Override
    public int compareTo(Save another) {
        return this.getType().compareTo(another.getType());
    }
}
