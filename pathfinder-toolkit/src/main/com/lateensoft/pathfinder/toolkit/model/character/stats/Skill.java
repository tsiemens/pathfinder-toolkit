package com.lateensoft.pathfinder.toolkit.model.character.stats;

import com.lateensoft.pathfinder.toolkit.dao.Identifiable;

import android.os.Parcel;
import android.os.Parcelable;

import static com.lateensoft.pathfinder.toolkit.model.character.stats.AbilityType.*;

public class Skill implements TypedStat<SkillType>, Parcelable, Identifiable, Comparable<Skill> {

    private final SkillType type;

    private boolean m_classSkill;
    private int m_rank;
    private int m_miscMod;
    private AbilityType ability;
    
    // For use with skills such as craft, professions, perform
    private String m_subType;
    
    // A unique id for all skills in the database
    long m_id;
    @Deprecated
    long m_characterId;
    
    public Skill(SkillType type) {
        this(type, type.getDefaultAbility());
    }

    public Skill(SkillType type, AbilityType abilityType) {
        this(UNSET_ID, UNSET_ID, type, false, 0, 0, abilityType);
    }
    
    public Skill(long id, long characterId, SkillType type, Boolean classSkill, int rank,
                 int miscMod, AbilityType ability) {
        this(id, characterId, type, null, classSkill, rank, miscMod, ability);
    }
    
    public Skill(long id, long characterId, SkillType type, String subtype, Boolean classSkill,
                 int rank, int miscMod, AbilityType ability) {
        m_classSkill = classSkill;
        m_rank = rank;
        m_miscMod = miscMod;
        this.ability = ability;
        this.type = type;
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
        ability = forKey(in.readInt());
        type = SkillType.forKey(in.readInt());
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
        out.writeInt(ability.getKey());
        out.writeInt(type.getKey());
        out.writeLong(m_characterId);
        out.writeLong(m_id);
        out.writeString(m_subType);
    }
    
    public boolean isClassSkill() {
        return m_classSkill;
    }

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
        int skillMod = abilitySet.getTotalAbilityMod(ability, maxDex) + m_rank
            + m_miscMod;
        
        if(m_classSkill && m_rank > 0)
            skillMod += 3;
        
        if (ability == DEX || ability == STR) {
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
    
    public int getRank() {
        return m_rank;
    }

    public void setRank(int rank) {
        m_rank = rank;
    }

    public int getMiscMod() {
        return m_miscMod;
    }

    public void setMiscMod(int miscMod) {
        m_miscMod = miscMod;
    }
    
    public AbilityType getAbility() {
        return ability;
    }
    
    public void setAbility(AbilityType ability) {
        this.ability = ability;
    }

    @Override
    public SkillType getType() {
        return type;
    }

    public boolean canBeSubTyped() {
        return type.canBeSubTyped();
    }

    @Override
    public void setId(long id) {
        m_id = id;
    }
    
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
        if (another.getType() == this.getType()) {
            return compareStringsEmptyAndNullLast(this.getSubType(), another.getSubType());
        } else {
            return this.getType().compareTo(another.getType());
        }
    }

    private int compareStringsEmptyAndNullLast(String lhs, String rhs) {
        if (this.getSubType() != null) {
            if (rhs != null && !rhs.isEmpty()) {
                return lhs.compareTo(rhs);
            } else {
                return -1;
            }
        } else {
            return 1;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Skill)) return false;

        Skill skill = (Skill) o;

        if (ability != skill.ability) return false;
        if (m_characterId != skill.m_characterId) return false;
        if (m_classSkill != skill.m_classSkill) return false;
        if (m_id != skill.m_id) return false;
        if (m_miscMod != skill.m_miscMod) return false;
        if (m_rank != skill.m_rank) return false;
        if (type != skill.type) return false;
        if (m_subType != null ? !m_subType.equals(skill.m_subType) : skill.m_subType != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (m_classSkill ? 1 : 0);
        result = 31 * result + m_rank;
        result = 31 * result + m_miscMod;
        result = 31 * result + ability.hashCode();
        result = 31 * result + (m_subType != null ? m_subType.hashCode() : 0);
        result = 31 * result + type.hashCode();
        result = 31 * result + (int) (m_id ^ (m_id >>> 32));
        result = 31 * result + (int) (m_characterId ^ (m_characterId >>> 32));
        return result;
    }
}
