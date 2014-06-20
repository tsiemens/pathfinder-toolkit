package com.lateensoft.pathfinder.toolkit.model.character;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.lateensoft.pathfinder.toolkit.dao.Identifiable;
import com.lateensoft.pathfinder.toolkit.model.character.stats.AbilitySet;
import com.lateensoft.pathfinder.toolkit.model.character.stats.CombatStatSet;
import com.lateensoft.pathfinder.toolkit.model.character.stats.SaveSet;
import com.lateensoft.pathfinder.toolkit.model.character.stats.SkillSet;

public class PathfinderCharacter implements Parcelable, Identifiable {
	private static final String PARCEL_BUNDLE_KEY_ABILITIES = "abilities";
	private static final String PARCEL_BUNDLE_KEY_COMBAT_STATS = "combat_stats";
	private static final String PARCEL_BUNDLE_KEY_SKILLS = "skills";
	private static final String PARCEL_BUNDLE_KEY_SAVES = "saves";
	private static final String PARCEL_BUNDLE_KEY_FLUFF = "fluff";
	private static final String PARCEL_BUNDLE_KEY_INVENTORY = "inventory";
	private static final String PARCEL_BUNDLE_KEY_FEATS = "feats";
	private static final String PARCEL_BUNDLE_KEY_SPELLS = "spells";
	
	private AbilitySet m_abilitySet;
	private CombatStatSet m_combatStatSet;
	private SkillSet m_skillSet;
	private SaveSet m_saveSet;
	private FluffInfo m_fluffInfo;
	private Inventory m_inventory;
	private double m_gold;
	private FeatList m_feats;
	private SpellBook m_spellBook;
	
	private long m_id;

    protected PathfinderCharacter(Builder builder) {
        m_abilitySet = builder.abilitySet != null ? builder.abilitySet : new AbilitySet();
        m_combatStatSet = builder.combatStatSet != null ? builder.combatStatSet : new CombatStatSet();
        m_skillSet = builder.skillSet != null ? builder.skillSet : new SkillSet();
        m_saveSet = builder.saveSet != null ? builder.saveSet : new SaveSet();
        m_fluffInfo = builder.fluffInfo != null ? builder.fluffInfo : new FluffInfo();
        m_inventory = builder.inventory != null ? builder.inventory : new Inventory();
        m_feats = builder.feats != null ? builder.feats : new FeatList();
        m_spellBook = builder.spellBook != null ? builder.spellBook : new SpellBook();
        m_gold = builder.gold;
        if (builder.name != null) {
            setName(builder.name);
        }
        this.setId(builder.id);
    }

    public static PathfinderCharacter newDefaultCharacter(String name) {
        Builder builder = new Builder().setName(name);
        return new PathfinderCharacter(builder);
    }
	
	public PathfinderCharacter(Parcel in) {
		Bundle objectBundle = in.readBundle();
		m_abilitySet = (AbilitySet) objectBundle.getParcelable(PARCEL_BUNDLE_KEY_ABILITIES);
		m_combatStatSet = (CombatStatSet) objectBundle.getParcelable(PARCEL_BUNDLE_KEY_COMBAT_STATS);
		m_skillSet = (SkillSet) objectBundle.getParcelable(PARCEL_BUNDLE_KEY_SKILLS);
		m_saveSet = (SaveSet) objectBundle.getParcelable(PARCEL_BUNDLE_KEY_SAVES);
		m_fluffInfo = (FluffInfo) objectBundle.getParcelable(PARCEL_BUNDLE_KEY_FLUFF);
		m_inventory = (Inventory) objectBundle.getParcelable(PARCEL_BUNDLE_KEY_INVENTORY);
		m_feats = (FeatList) objectBundle.getParcelable(PARCEL_BUNDLE_KEY_FEATS);
		m_spellBook = (SpellBook) objectBundle.getParcelable(PARCEL_BUNDLE_KEY_SPELLS);
		m_gold = in.readDouble();
		m_id = in.readLong();
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		Bundle objectBundle = new Bundle();
		objectBundle.putParcelable(PARCEL_BUNDLE_KEY_ABILITIES, m_abilitySet);
		objectBundle.putParcelable(PARCEL_BUNDLE_KEY_COMBAT_STATS, m_combatStatSet);
		objectBundle.putParcelable(PARCEL_BUNDLE_KEY_SKILLS, m_skillSet);
		objectBundle.putParcelable(PARCEL_BUNDLE_KEY_SAVES, m_saveSet);
		objectBundle.putParcelable(PARCEL_BUNDLE_KEY_FLUFF, m_fluffInfo);
		objectBundle.putParcelable(PARCEL_BUNDLE_KEY_INVENTORY, m_inventory);
		objectBundle.putParcelable(PARCEL_BUNDLE_KEY_FEATS, m_feats);
		objectBundle.putParcelable(PARCEL_BUNDLE_KEY_SPELLS, m_spellBook);
		out.writeBundle(objectBundle);
		out.writeDouble(m_gold);
		out.writeLong(m_id);
	}
	
	public void setAbilitySet(AbilitySet abilitySet) {
		m_abilitySet = abilitySet;
	}
	
	public AbilitySet getAbilitySet() {
		return m_abilitySet;
	}
	
	public CombatStatSet getCombatStatSet(){
		return m_combatStatSet;
	}
	
	public SkillSet getSkillSet() {
		return m_skillSet;
	}
	
	public Inventory getInventory(){
		return m_inventory;
	}
	
	public void setInventory(Inventory newInventory){
		m_inventory = newInventory;
	}
	
	public FeatList getFeatList(){
		return m_feats;
	}
	
	public void setFeatList(FeatList newFeats){
		m_feats = newFeats;
	}

	public FluffInfo getFluff() {
		return m_fluffInfo;
	}
	
	public SaveSet getSaveSet(){
		return m_saveSet;
	}
	
	public String getName(){
		return m_fluffInfo.getName();
	}
	
	public void setName(String name){
		if(name != null && !name.isEmpty())
			m_fluffInfo.setName(name);
	}

	public SpellBook getSpellBook() {
		return m_spellBook;
	}
	
	public void setSpellBook(SpellBook spellBook) {
		m_spellBook = spellBook;
	}

	public double getGold() {
		return m_gold;
	}

	public void setGold(double gold) {
		m_gold = gold;
	}

	@Override
	public long getId() {
		return m_id;
	}
	
	/**
	 * Sets the character ID, and all character IDs of components
	 */
	@Override
	public void setId(long id) {
		m_id = id;
		m_abilitySet.setCharacterID(id);
		m_combatStatSet.setId(id);
		m_skillSet.setCharacterID(id);
		m_saveSet.setCharacterID(id);
		m_fluffInfo.setId(id);
		m_inventory.setCharacterID(id);
		m_feats.setCharacterID(id);
		m_spellBook.setCharacterID(id);
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	public static final Parcelable.Creator<PathfinderCharacter> CREATOR = new Parcelable.Creator<PathfinderCharacter>() {
		public PathfinderCharacter createFromParcel(Parcel in) {
			return new PathfinderCharacter(in);
		}
		
		public PathfinderCharacter[] newArray(int size) {
			return new PathfinderCharacter[size];
		}
	};

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PathfinderCharacter)) return false;

        PathfinderCharacter character = (PathfinderCharacter) o;

        if (Double.compare(character.m_gold, m_gold) != 0) return false;
        if (m_id != character.m_id) return false;
        if (!m_abilitySet.equals(character.m_abilitySet)) return false;
        if (!m_combatStatSet.equals(character.m_combatStatSet)) return false;
        if (!m_feats.equals(character.m_feats)) return false;
        if (!m_fluffInfo.equals(character.m_fluffInfo)) return false;
        if (!m_inventory.equals(character.m_inventory)) return false;
        if (!m_saveSet.equals(character.m_saveSet)) return false;
        if (!m_skillSet.equals(character.m_skillSet)) return false;
        if (!m_spellBook.equals(character.m_spellBook)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = m_abilitySet.hashCode();
        result = 31 * result + m_combatStatSet.hashCode();
        result = 31 * result + m_skillSet.hashCode();
        result = 31 * result + m_saveSet.hashCode();
        result = 31 * result + m_fluffInfo.hashCode();
        result = 31 * result + m_inventory.hashCode();
        temp = Double.doubleToLongBits(m_gold);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + m_feats.hashCode();
        result = 31 * result + m_spellBook.hashCode();
        result = 31 * result + (int) (m_id ^ (m_id >>> 32));
        return result;
    }

    @SuppressWarnings("unchecked")
    public static class Builder<T extends Builder> {
        private long id = UNSET_ID;
        private String name;
        private AbilitySet abilitySet;
        private CombatStatSet combatStatSet;
        private SkillSet skillSet;
        private SaveSet saveSet;
        private FluffInfo fluffInfo;
        private Inventory inventory;
        private double gold = 0;
        private FeatList feats;
        private SpellBook spellBook;

        public Builder() {}

        public PathfinderCharacter build() {
            return new PathfinderCharacter(this);
        }

        public T setId(long id) {
            this.id = id;
            return (T) this;
        }

        public T setName(String name) {
            this.name = name;
            return (T) this;
        }

        public T setAbilitySet(AbilitySet abilitySet) {
            this.abilitySet = abilitySet;
            return (T) this;
        }

        public T setCombatStatSet(CombatStatSet combatStatSet) {
            this.combatStatSet = combatStatSet;
            return (T) this;
        }

        public T setSkillSet(SkillSet skillSet) {
            this.skillSet = skillSet;
            return (T) this;
        }

        public T setSaveSet(SaveSet saveSet) {
            this.saveSet = saveSet;
            return (T) this;
        }

        public T setFluffInfo(FluffInfo fluffInfo) {
            this.fluffInfo = fluffInfo;
            return (T) this;
        }

        public T setInventory(Inventory inventory) {
            this.inventory = inventory;
            return (T) this;
        }

        public T setGold(double gold) {
            this.gold = gold;
            return (T) this;
        }

        public T setFeats(FeatList feats) {
            this.feats = feats;
            return (T) this;
        }

        public T setSpellBook(SpellBook spellBook) {
            this.spellBook = spellBook;
            return (T) this;
        }
    }
}
