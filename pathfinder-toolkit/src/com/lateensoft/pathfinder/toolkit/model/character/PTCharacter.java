package com.lateensoft.pathfinder.toolkit.model.character;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.lateensoft.pathfinder.toolkit.db.repository.PTStorable;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTAbilitySet;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTCombatStatSet;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTSaveSet;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTSkillSet;

public class PTCharacter implements Parcelable, PTStorable {
	private static final String PARCEL_BUNDLE_KEY_ABILITIES = "abilities";
	private static final String PARCEL_BUNDLE_KEY_TEMP_ABILITIES = "abilities_temp";
	private static final String PARCEL_BUNDLE_KEY_COMBAT_STATS = "combat_stats";
	private static final String PARCEL_BUNDLE_KEY_SKILLS = "skills";
	private static final String PARCEL_BUNDLE_KEY_SAVES = "saves";
	private static final String PARCEL_BUNDLE_KEY_FLUFF = "fluff";
	private static final String PARCEL_BUNDLE_KEY_INVENTORY = "inventory";
	private static final String PARCEL_BUNDLE_KEY_FEATS = "feats";
	private static final String PARCEL_BUNDLE_KEY_SPELLS = "spells";
	
	private PTAbilitySet m_abilitySet;
	private PTAbilitySet m_tempAbilitySet;
	private PTCombatStatSet m_combatStatSet;
	private PTSkillSet m_skillSet;
	private PTSaveSet m_saveSet;
	private PTFluffInfo m_fluffInfo;
	private PTInventory m_inventory;
	private double m_gold;
	private PTFeatList m_feats;
	private PTSpellBook m_spellBook;
	
	private long m_id;
	
	public PTCharacter(long characterId, String name, Context context) {
		this(name, context);
		m_id = characterId;
	}
	
	/**
	 * Only use for instantiation from database
	 */
	public PTCharacter(long characterId, double gold, PTAbilitySet abilitySet, PTAbilitySet tempAbilitySet, 
			PTFluffInfo fluff, PTCombatStatSet combatStats, PTSaveSet saves, PTSkillSet skills,
			PTInventory inventory, PTFeatList feats, PTSpellBook spells) {
		m_id = characterId;
		m_gold = gold;
		m_abilitySet = abilitySet;
		m_tempAbilitySet = tempAbilitySet;
		m_fluffInfo = fluff;
		m_combatStatSet = combatStats;
		m_saveSet = saves;
		m_skillSet = skills;
		m_inventory = inventory;
		m_feats = feats;
		m_spellBook = spells;
	}
	
	public PTCharacter(String name, Context context) {
		m_id = UNSET_ID;
		m_abilitySet = new PTAbilitySet();
		m_tempAbilitySet = new PTAbilitySet();
		m_combatStatSet = new PTCombatStatSet();
		m_skillSet = new PTSkillSet(context);
		m_saveSet = new PTSaveSet(context);
		m_fluffInfo = new PTFluffInfo();
		m_inventory = new PTInventory();
		m_feats = new PTFeatList();
		m_spellBook = new PTSpellBook();
		m_gold = 0;
		setName(name);
	}
	
	public PTCharacter(Parcel in) {
		Bundle objectBundle = in.readBundle();
		m_abilitySet = (PTAbilitySet) objectBundle.getParcelable(PARCEL_BUNDLE_KEY_ABILITIES);
		m_tempAbilitySet = (PTAbilitySet) objectBundle.getParcelable(PARCEL_BUNDLE_KEY_TEMP_ABILITIES);
		m_combatStatSet = (PTCombatStatSet) objectBundle.getParcelable(PARCEL_BUNDLE_KEY_COMBAT_STATS);
		m_skillSet = (PTSkillSet) objectBundle.getParcelable(PARCEL_BUNDLE_KEY_SKILLS);
		m_saveSet = (PTSaveSet) objectBundle.getParcelable(PARCEL_BUNDLE_KEY_SAVES);
		m_fluffInfo = (PTFluffInfo) objectBundle.getParcelable(PARCEL_BUNDLE_KEY_FLUFF);
		m_inventory = (PTInventory) objectBundle.getParcelable(PARCEL_BUNDLE_KEY_INVENTORY);
		m_feats = (PTFeatList) objectBundle.getParcelable(PARCEL_BUNDLE_KEY_FEATS);
		m_spellBook = (PTSpellBook) objectBundle.getParcelable(PARCEL_BUNDLE_KEY_SPELLS);
		m_gold = in.readDouble();
		m_id = in.readLong();
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		Bundle objectBundle = new Bundle();
		objectBundle.putParcelable(PARCEL_BUNDLE_KEY_ABILITIES, m_abilitySet);
		objectBundle.putParcelable(PARCEL_BUNDLE_KEY_TEMP_ABILITIES, m_tempAbilitySet);
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
	
	public void setAbilitySet(PTAbilitySet abilitySet) {
		m_abilitySet = abilitySet;
	}
	
	public PTAbilitySet getAbilitySet() {
		return m_abilitySet;
	}
	
	public PTAbilitySet getTempAbilitySet() {
		return m_tempAbilitySet;
	}
	
	public PTCombatStatSet getCombatStatSet(){
		return m_combatStatSet;
	}
	
	public PTSkillSet getSkillSet() {
		return m_skillSet;
	}
	
	public PTInventory getInventory(){
		return m_inventory;
	}
	
	public void setInventory(PTInventory newInventory){
		m_inventory = newInventory;
	}
	
	public PTFeatList getFeatList(){
		return m_feats;
	}
	
	public void setFeatList(PTFeatList newFeats){
		m_feats = newFeats;
	}

	public PTFluffInfo getFluff() {
		return m_fluffInfo;
	}
	
	public PTSaveSet getSaveSet(){
		return m_saveSet;
	}
	
	public String getName(){
		return m_fluffInfo.getName();
	}
	
	public void setName(String name){
		if(name != null && name != "")
			m_fluffInfo.setName(name);
	}

	public PTSpellBook getSpellBook() {
		return m_spellBook;
	}
	
	public void setSpellBook(PTSpellBook spellBook) {
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
		m_tempAbilitySet.setCharacterID(id);
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
	
	public static final Parcelable.Creator<PTCharacter> CREATOR = new Parcelable.Creator<PTCharacter>() {
		public PTCharacter createFromParcel(Parcel in) {
			return new PTCharacter(in);
		}
		
		public PTCharacter[] newArray(int size) {
			return new PTCharacter[size];
		}
	};
}
