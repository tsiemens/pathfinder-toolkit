package com.lateensoft.pathfinder.toolkit.patching;

import com.lateensoft.pathfinder.toolkit.model.party.CampaignParty;
import com.lateensoft.pathfinder.toolkit.model.party.PartyMember;

public class PartyConverter {
    
    public static CampaignParty convertParty(com.lateensoft.pathfinder.toolkit.deprecated.v1.model.party.PTParty oldParty) {
        CampaignParty newParty = new CampaignParty(oldParty.getName());
        for (int i = 0; i < oldParty.getPartyMemberNames().length; i++) {
            newParty.add(convertMember(oldParty.getPartyMember(i)));
        }
        
        return newParty;
    }
    
    private static PartyMember convertMember(com.lateensoft.pathfinder.toolkit.deprecated.v1.model.party.PTPartyMember oldMember) {
        PartyMember newMember = new PartyMember(oldMember.getName());
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
