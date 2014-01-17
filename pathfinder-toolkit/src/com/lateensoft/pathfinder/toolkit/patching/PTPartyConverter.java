package com.lateensoft.pathfinder.toolkit.patching;

import com.lateensoft.pathfinder.toolkit.model.party.PTParty;
import com.lateensoft.pathfinder.toolkit.model.party.PTPartyMember;

public class PTPartyConverter {
	
	public static PTParty convertParty(com.lateensoft.pathfinder.toolkit.deprecated.v1.model.party.PTParty oldParty) {
		PTParty newParty = new PTParty(oldParty.getName());
		for (int i = 0; i < oldParty.getPartyMemberNames().length; i++) {
			newParty.addPartyMember(convertMember(oldParty.getPartyMember(i)));
		}
		
		return newParty;
	}
	
	private static PTPartyMember convertMember(com.lateensoft.pathfinder.toolkit.deprecated.v1.model.party.PTPartyMember oldMember) {
		PTPartyMember newMember = new PTPartyMember(oldMember.getName());
		newMember.setInitiative(oldMember.getInitiative());
		newMember.setAC(oldMember.getAC());
		newMember.setTouch(oldMember.getTouch());
		newMember.setFlatFooted(oldMember.getFlatFooted());
		newMember.setSpellResist(oldMember.getSpellResist());
		newMember.setDamageReduction(oldMember.getDamageReduction());
		newMember.setCMD(oldMember.getCMD());
		newMember.setFortSave(oldMember.getFortSave());
		newMember.setReflexSave(oldMember.getReflexSave());
		newMember.setWillSave(oldMember.getWillSave());
		newMember.setBluffSkillBonus(oldMember.getBluffSkillBonus());
		newMember.setDisguiseSkillBonus(oldMember.getDisguiseSkillBonus());
		newMember.setPerceptionSkillBonus(oldMember.getPerceptionSkillBonus());
		newMember.setSenseMotiveSkillBonus(oldMember.getSenseMotiveSkillBonus());
		newMember.setStealthSkillBonus(oldMember.getStealthSkillBonus());
		newMember.setLastRolledValue(oldMember.getRolledValue());
		
		return newMember;
	}
}
