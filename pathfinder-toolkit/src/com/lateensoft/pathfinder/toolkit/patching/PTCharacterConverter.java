package com.lateensoft.pathfinder.toolkit.patching;

import com.lateensoft.pathfinder.toolkit.PTBaseApplication;
import com.lateensoft.pathfinder.toolkit.model.character.PTCharacter;
import com.lateensoft.pathfinder.toolkit.model.character.PTFeat;
import com.lateensoft.pathfinder.toolkit.model.character.PTFeatList;
import com.lateensoft.pathfinder.toolkit.model.character.PTFluffInfo;
import com.lateensoft.pathfinder.toolkit.model.character.PTInventory;
import com.lateensoft.pathfinder.toolkit.model.character.PTSpell;
import com.lateensoft.pathfinder.toolkit.model.character.PTSpellBook;
import com.lateensoft.pathfinder.toolkit.model.character.items.PTArmor;
import com.lateensoft.pathfinder.toolkit.model.character.items.PTItem;
import com.lateensoft.pathfinder.toolkit.model.character.items.PTWeapon;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTAbilitySet;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTCombatStatSet;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTSave;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTSaveSet;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTSkill;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTSkillSet;

public class PTCharacterConverter {

	public static PTCharacter convertCharacter(com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.PTCharacter oldCharacter) {
		PTCharacter newCharacter = new PTCharacter("", PTBaseApplication.getAppContext());
		newCharacter.setGold(oldCharacter.mGold);
		setFluff(newCharacter.getFluff(), oldCharacter.getFluff());
		setAbilities(newCharacter.getAbilitySet(), oldCharacter.getAbilitySet(), oldCharacter.getTempAbilitySet());
		setCombatStats(newCharacter.getCombatStatSet(), oldCharacter.getCombatStatSet());
		setFeats(newCharacter.getFeatList(), oldCharacter.getFeatList());
		setInventory(newCharacter.getInventory(), oldCharacter.getInventory());
		setSaves(newCharacter.getSaveSet(), oldCharacter.getSaveSet());
		setSkills(newCharacter.getSkillSet(), oldCharacter.getSkillSet());
		setSpells(newCharacter.getSpellBook(), oldCharacter.getSpellBook());
		return newCharacter;
	}
	
	private static void setFluff(PTFluffInfo newFluff, com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.PTCharacterFluffInfo oldFluff) {
		newFluff.setName(oldFluff.getName());
		newFluff.setAlignment(oldFluff.getAlignment());
		newFluff.setXP(oldFluff.getXP());
		newFluff.setNextLevelXP(oldFluff.getNextLevelXP());
		newFluff.setPlayerClass(oldFluff.getPlayerClass());
		newFluff.setRace(oldFluff.getRace());
		newFluff.setDeity(oldFluff.getDeity());
		newFluff.setLevel(oldFluff.getLevel());
		newFluff.setSize(oldFluff.getSize());
		newFluff.setGender(oldFluff.getGender());
		newFluff.setHeight(oldFluff.getHeight());
		newFluff.setWeight(oldFluff.getWeight());
		newFluff.setEyes(oldFluff.getEyes());
		newFluff.setHair(oldFluff.getHair());
		newFluff.setLanguages(oldFluff.getLanguages());
		newFluff.setDescription(oldFluff.getDescription());
	}
	
	private static void setAbilities(PTAbilitySet newAbilities, com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.stats.PTAbilitySet oldAbilities,
			com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.stats.PTAbilitySet tempAbilities) {
		for (int i = 0; i < newAbilities.size(); i++) {
			newAbilities.getAbilityAtIndex(i).setScore(oldAbilities.getAbilityScore(i).getScore());
			newAbilities.getAbilityAtIndex(i).setTempBonus(tempAbilities.getAbilityScore(i).getScore() - 10);
		}
	}
	
	private static void setCombatStats(PTCombatStatSet newCombatStats, com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.stats.PTCombatStatSet oldCombatStats) {
		newCombatStats.setTotalHP(oldCombatStats.getTotalHP());
		newCombatStats.setWounds(oldCombatStats.getWounds());
		newCombatStats.setNonLethalDamage(oldCombatStats.getNonLethalDamage());
		newCombatStats.setDamageReduction(oldCombatStats.getDamageReduction());
		newCombatStats.setBaseSpeed(oldCombatStats.getBaseSpeed());
		
		// Abilities are set to defaults
		newCombatStats.setInitiativeMiscMod(oldCombatStats.getInitiativeMiscMod());
		
		newCombatStats.setACArmourBonus(oldCombatStats.getACArmourBonus());
		newCombatStats.setACShieldBonus(oldCombatStats.getACShieldBonus());
		newCombatStats.setSizeModifier(oldCombatStats.getSizeModifier());
		newCombatStats.setNaturalArmour(oldCombatStats.getNaturalArmour());
		newCombatStats.setDeflectionMod(oldCombatStats.getDeflectionMod());
		newCombatStats.setACMiscMod(oldCombatStats.getACMiscMod());
		
		newCombatStats.setSpellResistance(oldCombatStats.getSpellResist());
		
		newCombatStats.setBABPrimary(oldCombatStats.getBABPrimary());
		newCombatStats.setBABSecondary(oldCombatStats.getBABSecondary());
	}
	
	private static void setFeats(PTFeatList newFeatList, com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.PTCharacterFeatList oldFeatList) {
		PTFeat newFeat;
		for (int i = 0; i < oldFeatList.getNumberOfFeats(); i++) {
			newFeat = new PTFeat();
			newFeat.setName(oldFeatList.getFeat(i).getName());
			newFeat.setDescription(oldFeatList.getFeat(i).getDescription());
			newFeatList.addFeat(newFeat);
		}
	}
	
	private static void setInventory(PTInventory newInv, com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.PTCharacterInventory oldInv) {
		com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.items.PTItem[] oldItems = oldInv.getItems();
		PTItem newItem;
		for (int i = 0; i < oldItems.length; i++) {
			newItem = new PTItem();
			setItem(newItem, oldItems[i]);
			newInv.addItem(newItem);
		}
		
		com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.items.PTWeapon[] oldWeapons = oldInv.getWeaponArray();
		PTWeapon newWeapon;
		for (int i = 0; i < oldWeapons.length; i++) {
			newWeapon = new PTWeapon();
			setWeapon(newWeapon, oldWeapons[i]);
			newInv.addWeapon(newWeapon);
		}
		
		com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.items.PTArmor[] oldArmors = oldInv.getArmorArray();
		PTArmor newArmor;
		for (int i = 0; i < oldArmors.length; i++) {
			newArmor = new PTArmor();
			setArmor(newArmor, oldArmors[i]);
			newInv.addArmor(newArmor);
		}
	}
	
	private static void setItem(PTItem newItem, com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.items.PTItem oldItem) {
		newItem.setName(oldItem.getName());
		newItem.setQuantity(oldItem.getQuantity());
		newItem.setWeight(oldItem.getWeight());
		newItem.setIsContained(oldItem.isContained());
	}
	
	private static void setWeapon(PTWeapon newWeapon, com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.items.PTWeapon oldWeapon) {
		setItem(newWeapon, oldWeapon);
		newWeapon.setAmmunition(oldWeapon.getAmmunition());
		newWeapon.setCritical(oldWeapon.getCritical());
		newWeapon.setDamage(oldWeapon.getDamage());
		newWeapon.setRange(oldWeapon.getRange());
		newWeapon.setSize(oldWeapon.getSize());
		newWeapon.setSpecialProperties(oldWeapon.getSpecialProperties());
		newWeapon.setTotalAttackBonus(oldWeapon.getTotalAttackBonus());
		newWeapon.setType(oldWeapon.getType());
	}
	
	private static void setArmor(PTArmor newArmor, com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.items.PTArmor oldArmor) {
		setItem(newArmor, oldArmor);
		newArmor.setACBonus(oldArmor.getACBonus());
		newArmor.setCheckPen(0 - oldArmor.getCheckPen()); // Changes this to negatives
		newArmor.setMaxDex(oldArmor.getMaxDex());
		newArmor.setSize(oldArmor.getSize());
		newArmor.setSpecialProperties(oldArmor.getSpecialProperties());
		newArmor.setSpeed(oldArmor.getSpeed());
		newArmor.setSpellFail(oldArmor.getSpellFail());
		newArmor.setWorn(oldArmor.isWorn());
	}
	
	private static void setSaves(PTSaveSet newSaves, com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.stats.PTSaveSet oldSaves) {
		// This should work, assuming the order does not change
		for (int i = 0; i < newSaves.size() && i < oldSaves.size(); i++) {
			setSave(newSaves.getSaveByIndex(i), oldSaves.getSave(i));
		}
	}
	
	private static void setSave(PTSave newSave, com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.stats.PTSave oldSave) {
		newSave.setBaseSave(oldSave.getBase());
		newSave.setMagicMod(oldSave.getMagicMod());
		newSave.setMiscMod(oldSave.getMiscMod());
		newSave.setTempMod(oldSave.getTempMod());
	}
	
	private static void setSkills(PTSkillSet newSkills, com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.stats.PTSkillSet oldSkills) {
		com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.stats.PTSkill[] oldSkillArray = oldSkills.getSkills();
		// This should work, assuming the order does not change
		for (int i = 0; i < newSkills.size() && i < oldSkillArray.length; i++) {
			setSkill(newSkills.getSkillByIndex(i), oldSkillArray[i]);
		}
	}
	
	private static void setSkill(PTSkill newSkill, com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.stats.PTSkill oldSkill) {
		newSkill.setRank(oldSkill.getRank());
		newSkill.setMiscMod(oldSkill.getMiscMod());
		newSkill.setClassSkill(oldSkill.isClassSkill());
	}
	
	private static void setSpells(PTSpellBook newSpells, com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.PTSpellBook oldSpells) {
		com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.PTSpell[] oldSpellArray = oldSpells.getSpells();
		PTSpell newSpell;
		for (int i = 0; i < oldSpellArray.length; i++) {
			newSpell = new PTSpell();
			setSpell(newSpell, oldSpellArray[i]);
			newSpells.add(newSpell);
		}
	}
	
	private static void setSpell(PTSpell newSpell, com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.PTSpell oldSpell) {
		newSpell.setName(oldSpell.getName());
		newSpell.setDescription(oldSpell.getDescription());
		newSpell.setLevel(oldSpell.getLevel());
		newSpell.setPrepared(oldSpell.getPrepared());
	}
}
