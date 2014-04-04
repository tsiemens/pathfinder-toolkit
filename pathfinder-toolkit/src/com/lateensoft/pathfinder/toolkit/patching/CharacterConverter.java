package com.lateensoft.pathfinder.toolkit.patching;

import com.lateensoft.pathfinder.toolkit.model.character.*;
import com.lateensoft.pathfinder.toolkit.model.character.PathfinderCharacter;
import com.lateensoft.pathfinder.toolkit.model.character.items.Armor;
import com.lateensoft.pathfinder.toolkit.model.character.items.Item;
import com.lateensoft.pathfinder.toolkit.model.character.items.Weapon;
import com.lateensoft.pathfinder.toolkit.model.character.stats.*;
import com.lateensoft.pathfinder.toolkit.model.character.stats.Skill;

public class CharacterConverter {

	public static PathfinderCharacter convertCharacter(com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.PTCharacter oldCharacter) {
		PathfinderCharacter newCharacter = new PathfinderCharacter("");
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
	
	private static void setFluff(FluffInfo newFluff, com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.PTCharacterFluffInfo oldFluff) {
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
	
	private static void setAbilities(AbilitySet newAbilities, com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.stats.PTAbilitySet oldAbilities,
			com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.stats.PTAbilitySet tempAbilities) {
		for (int i = 0; i < newAbilities.size(); i++) {
			newAbilities.getAbilityAtIndex(i).setScore(oldAbilities.getAbilityScore(i).getScore());
			newAbilities.getAbilityAtIndex(i).setTempBonus(tempAbilities.getAbilityScore(i).getScore() - 10);
		}
	}
	
	private static void setCombatStats(CombatStatSet newCombatStats, com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.stats.PTCombatStatSet oldCombatStats) {
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
	
	private static void setFeats(FeatList newFeatList, com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.PTCharacterFeatList oldFeatList) {
		Feat newFeat;
		for (int i = 0; i < oldFeatList.getNumberOfFeats(); i++) {
			newFeat = new Feat();
			newFeat.setName(oldFeatList.getFeat(i).getName());
			newFeat.setDescription(oldFeatList.getFeat(i).getDescription());
			newFeatList.add(newFeat);
		}
	}
	
	private static void setInventory(Inventory newInv, com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.PTCharacterInventory oldInv) {
		com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.items.PTItem[] oldItems = oldInv.getItems();
		Item newItem;
        for (com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.items.PTItem oldItem : oldItems) {
            newItem = new Item();
            setItem(newItem, oldItem);
            newInv.getItems().add(newItem);
        }
		
		com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.items.PTWeapon[] oldWeapons = oldInv.getWeaponArray();
		Weapon newWeapon;
        for (com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.items.PTWeapon oldWeapon : oldWeapons) {
            newWeapon = new Weapon();
            setWeapon(newWeapon, oldWeapon);
            newInv.getWeapons().add(newWeapon);
        }
		
		com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.items.PTArmor[] oldArmors = oldInv.getArmorArray();
		Armor newArmor;
        for (com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.items.PTArmor oldArmor : oldArmors) {
            newArmor = new Armor();
            setArmor(newArmor, oldArmor);
            newInv.getArmors().add(newArmor);
        }
	}
	
	private static void setItem(Item newItem, com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.items.PTItem oldItem) {
		newItem.setName(oldItem.getName());
		newItem.setQuantity(oldItem.getQuantity());
		newItem.setWeight(oldItem.getWeight());
		newItem.setContained(oldItem.isContained());
	}
	
	private static void setWeapon(Weapon newWeapon, com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.items.PTWeapon oldWeapon) {
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
	
	private static void setArmor(Armor newArmor, com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.items.PTArmor oldArmor) {
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
	
	private static void setSaves(SaveSet newSaves, com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.stats.PTSaveSet oldSaves) {
		// This should work, assuming the order does not change
		for (int i = 0; i < newSaves.size() && i < oldSaves.size(); i++) {
			setSave(newSaves.getSaveByIndex(i), oldSaves.getSave(i));
		}
	}
	
	private static void setSave(Save newSave, com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.stats.PTSave oldSave) {
		newSave.setBaseSave(oldSave.getBase());
		newSave.setMagicMod(oldSave.getMagicMod());
		newSave.setMiscMod(oldSave.getMiscMod());
		newSave.setTempMod(oldSave.getTempMod());
	}
	
	private static void setSkills(SkillSet newSkills, com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.stats.PTSkillSet oldSkills) {
		com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.stats.PTSkill[] oldSkillArray = oldSkills.getSkills();
		// This should work, assuming the order does not change
		for (int i = 0; i < newSkills.size() && i < oldSkillArray.length; i++) {
			setSkill(newSkills.getSkillByIndex(i), oldSkillArray[i]);
		}
	}
	
	private static void setSkill(Skill newSkill, com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.stats.PTSkill oldSkill) {
		newSkill.setRank(oldSkill.getRank());
		newSkill.setMiscMod(oldSkill.getMiscMod());
		newSkill.setClassSkill(oldSkill.isClassSkill());
	}
	
	private static void setSpells(SpellBook newSpells, com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.PTSpellBook oldSpells) {
		com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.PTSpell[] oldSpellArray = oldSpells.getSpells();
		Spell newSpell;
        for (com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.PTSpell anOldSpellArray : oldSpellArray) {
            newSpell = new Spell();
            setSpell(newSpell, anOldSpellArray);
            newSpells.add(newSpell);
        }
	}
	
	private static void setSpell(Spell newSpell, com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.PTSpell oldSpell) {
		newSpell.setName(oldSpell.getName());
		newSpell.setDescription(oldSpell.getDescription());
		newSpell.setLevel(oldSpell.getLevel());
		newSpell.setPrepared(oldSpell.getPrepared());
	}
}
