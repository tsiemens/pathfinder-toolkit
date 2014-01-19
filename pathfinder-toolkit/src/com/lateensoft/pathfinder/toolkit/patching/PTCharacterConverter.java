package com.lateensoft.pathfinder.toolkit.patching;

import com.lateensoft.pathfinder.toolkit.PTBaseApplication;
import com.lateensoft.pathfinder.toolkit.model.character.PTCharacter;
import com.lateensoft.pathfinder.toolkit.model.character.PTFeat;
import com.lateensoft.pathfinder.toolkit.model.character.PTFeatList;
import com.lateensoft.pathfinder.toolkit.model.character.PTFluffInfo;
import com.lateensoft.pathfinder.toolkit.model.character.PTInventory;
import com.lateensoft.pathfinder.toolkit.model.character.PTSpellBook;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTAbilitySet;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTCombatStatSet;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTSaveSet;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTSkillSet;

public class PTCharacterConverter {

	public static PTCharacter convertCharacter(com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.PTCharacter oldCharacter) {
		PTCharacter newCharacter = new PTCharacter("", PTBaseApplication.getAppContext());
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
		newFluff.setNextLevelXP(oldFluff.getXPChange());
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
		// TODO
	}
	
	private static void setCombatStats(PTCombatStatSet newFluff, com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.stats.PTCombatStatSet oldCombatStats) {
		// TODO
	}
	
	private static void setFeats(PTFeatList newFeatList, com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.PTCharacterFeatList oldFeatList) {
		// TODO
	}
	
	private static void setInventory(PTInventory newInv, com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.PTCharacterInventory oldInv) {
		// TODO
	}
	
	private static void setSaves(PTSaveSet newSaves, com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.stats.PTSaveSet oldSaves) {
		// TODO
	}
	
	private static void setSkills(PTSkillSet newSkills, com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.stats.PTSkillSet oldSkills) {
		// TODO
	}
	
	private static void setSpells(PTSpellBook newSpells, com.lateensoft.pathfinder.toolkit.deprecated.v1.model.character.PTSpellBook oldSpells) {
		// TODO
	}
}
