package com.lateensoft.pathfinder.toolkit.model.character;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.lateensoft.pathfinder.toolkit.db.repository.Storable;
import com.lateensoft.pathfinder.toolkit.model.character.stats.AbilitySet;
import com.lateensoft.pathfinder.toolkit.model.character.stats.CombatStatSet;
import com.lateensoft.pathfinder.toolkit.model.character.stats.SaveSet;
import com.lateensoft.pathfinder.toolkit.model.character.stats.SkillSet;

public class PathfinderCharacter implements Parcelable, Storable {
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
	
	public PathfinderCharacter(long characterId, String name) {
		this(name);
		m_id = characterId;
	}
	
	/**
	 * Only use for instantiation from database
	 */
	public PathfinderCharacter(long characterId, double gold, AbilitySet abilitySet,
                               FluffInfo fluff, CombatStatSet combatStats, SaveSet saves, SkillSet skills,
                               Inventory inventory, FeatList feats, SpellBook spells) {
		m_id = characterId;
		m_gold = gold;
		m_abilitySet = abilitySet;
		m_fluffInfo = fluff;
		m_combatStatSet = combatStats;
		m_saveSet = saves;
		m_skillSet = skills;
		m_inventory = inventory;
		m_feats = feats;
		m_spellBook = spells;
	}

    public PathfinderCharacter(double gold, AbilitySet abilitySet,
                               FluffInfo fluff, CombatStatSet combatStats, SaveSet saves, SkillSet skills,
                               Inventory inventory, FeatList feats, SpellBook spells) {
        this(UNSET_ID, gold, abilitySet, fluff, combatStats, saves, skills, inventory, feats, spells);
    }
	
	public PathfinderCharacter(String name) {
		m_id = UNSET_ID;
		m_abilitySet = new AbilitySet();
		m_combatStatSet = new CombatStatSet();
		m_skillSet = new SkillSet();
		m_saveSet = new SaveSet();
		m_fluffInfo = new FluffInfo();
		m_inventory = new Inventory();
		m_feats = new FeatList();
		m_spellBook = new SpellBook();
		m_gold = 0;
		setName(name);
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
		if(name != null && name != "")
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
	public long getID() {
		return m_id;
	}
	
	/**
	 * Sets the character ID, and all character IDs of components
	 */
	@Override
	public void setID(long id) {
		m_id = id;
		m_abilitySet.setCharacterID(id);
		m_combatStatSet.setID(id);
		m_skillSet.setCharacterID(id);
		m_saveSet.setCharacterID(id);
		m_fluffInfo.setID(id);
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
}
