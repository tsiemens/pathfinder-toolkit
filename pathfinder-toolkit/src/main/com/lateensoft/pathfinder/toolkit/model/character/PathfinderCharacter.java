package com.lateensoft.pathfinder.toolkit.model.character;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.common.base.Objects;
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

    private String name;
    private double gold;

    private AbilitySet abilitySet;
    private CombatStatSet combatStatSet;
    private SkillSet skillSet;
    private SaveSet saveSet;
    private FluffInfo fluffInfo;
    private Inventory inventory;
    private FeatList feats;
    private SpellBook spellBook;
    
    private long id;

    protected PathfinderCharacter(Builder builder) {
        abilitySet = builder.abilitySet != null ? builder.abilitySet : new AbilitySet();
        combatStatSet = builder.combatStatSet != null ? builder.combatStatSet : new CombatStatSet();
        skillSet = builder.skillSet != null ? builder.skillSet : new SkillSet();
        saveSet = builder.saveSet != null ? builder.saveSet : new SaveSet();
        fluffInfo = builder.fluffInfo != null ? builder.fluffInfo : new FluffInfo();
        inventory = builder.inventory != null ? builder.inventory : new Inventory();
        feats = builder.feats != null ? builder.feats : new FeatList();
        spellBook = builder.spellBook != null ? builder.spellBook : new SpellBook();
        gold = builder.gold;
        name = builder.name != null && !builder.name.isEmpty()? builder.name : "Character Name";
        this.setId(builder.id);
    }

    public static PathfinderCharacter newDefaultCharacter(String name) {
        Builder builder = new Builder().setName(name);
        return new PathfinderCharacter(builder);
    }
    
    public PathfinderCharacter(Parcel in) {
        name = in.readString();
        Bundle objectBundle = in.readBundle();
        abilitySet = (AbilitySet) objectBundle.getParcelable(PARCEL_BUNDLE_KEY_ABILITIES);
        combatStatSet = (CombatStatSet) objectBundle.getParcelable(PARCEL_BUNDLE_KEY_COMBAT_STATS);
        skillSet = (SkillSet) objectBundle.getParcelable(PARCEL_BUNDLE_KEY_SKILLS);
        saveSet = (SaveSet) objectBundle.getParcelable(PARCEL_BUNDLE_KEY_SAVES);
        fluffInfo = (FluffInfo) objectBundle.getParcelable(PARCEL_BUNDLE_KEY_FLUFF);
        inventory = (Inventory) objectBundle.getParcelable(PARCEL_BUNDLE_KEY_INVENTORY);
        feats = (FeatList) objectBundle.getParcelable(PARCEL_BUNDLE_KEY_FEATS);
        spellBook = (SpellBook) objectBundle.getParcelable(PARCEL_BUNDLE_KEY_SPELLS);
        gold = in.readDouble();
        id = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        Bundle objectBundle = new Bundle();
        objectBundle.putParcelable(PARCEL_BUNDLE_KEY_ABILITIES, abilitySet);
        objectBundle.putParcelable(PARCEL_BUNDLE_KEY_COMBAT_STATS, combatStatSet);
        objectBundle.putParcelable(PARCEL_BUNDLE_KEY_SKILLS, skillSet);
        objectBundle.putParcelable(PARCEL_BUNDLE_KEY_SAVES, saveSet);
        objectBundle.putParcelable(PARCEL_BUNDLE_KEY_FLUFF, fluffInfo);
        objectBundle.putParcelable(PARCEL_BUNDLE_KEY_INVENTORY, inventory);
        objectBundle.putParcelable(PARCEL_BUNDLE_KEY_FEATS, feats);
        objectBundle.putParcelable(PARCEL_BUNDLE_KEY_SPELLS, spellBook);
        out.writeBundle(objectBundle);
        out.writeDouble(gold);
        out.writeLong(id);
    }
    
    public void setAbilitySet(AbilitySet abilitySet) {
        this.abilitySet = abilitySet;
    }
    
    public AbilitySet getAbilitySet() {
        return abilitySet;
    }
    
    public CombatStatSet getCombatStatSet(){
        return combatStatSet;
    }
    
    public SkillSet getSkillSet() {
        return skillSet;
    }
    
    public Inventory getInventory(){
        return inventory;
    }
    
    public void setInventory(Inventory newInventory){
        inventory = newInventory;
    }
    
    public FeatList getFeatList(){
        return feats;
    }
    
    public void setFeatList(FeatList newFeats){
        feats = newFeats;
    }

    public FluffInfo getFluff() {
        return fluffInfo;
    }
    
    public SaveSet getSaveSet(){
        return saveSet;
    }
    
    public String getName(){
        return name;
    }
    
    public void setName(String name){
        if(name != null && !name.isEmpty()) {
            this.name = name;
        }
    }

    public SpellBook getSpellBook() {
        return spellBook;
    }
    
    public void setSpellBook(SpellBook spellBook) {
        this.spellBook = spellBook;
    }

    public double getGold() {
        return gold;
    }

    public void setGold(double gold) {
        this.gold = gold;
    }

    @Override
    public long getId() {
        return id;
    }
    
    /**
     * Sets the character ID, and all character IDs of components
     */
    @Override
    public void setId(long id) {
        this.id = id;
        abilitySet.setCharacterID(id);
        combatStatSet.setId(id);
        skillSet.setCharacterID(id);
        saveSet.setCharacterID(id);
        fluffInfo.setId(id);
        inventory.setCharacterID(id);
        feats.setCharacterID(id);
        spellBook.setCharacterID(id);
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

        if (!Objects.equal(this.name, character.name)) return false;
        if (Double.compare(character.gold, gold) != 0) return false;
        if (id != character.id) return false;
        if (!abilitySet.equals(character.abilitySet)) return false;
        if (!combatStatSet.equals(character.combatStatSet)) return false;
        if (!feats.equals(character.feats)) return false;
        if (!fluffInfo.equals(character.fluffInfo)) return false;
        if (!inventory.equals(character.inventory)) return false;
        if (!saveSet.equals(character.saveSet)) return false;
        if (!skillSet.equals(character.skillSet)) return false;
        if (!spellBook.equals(character.spellBook)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = abilitySet.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + combatStatSet.hashCode();
        result = 31 * result + skillSet.hashCode();
        result = 31 * result + saveSet.hashCode();
        result = 31 * result + fluffInfo.hashCode();
        result = 31 * result + inventory.hashCode();
        temp = Double.doubleToLongBits(gold);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + feats.hashCode();
        result = 31 * result + spellBook.hashCode();
        result = 31 * result + (int) (id ^ (id >>> 32));
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
