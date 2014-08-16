package com.lateensoft.pathfinder.toolkit.patching.v10;

import com.lateensoft.pathfinder.toolkit.model.character.PathfinderCharacter;
import com.lateensoft.pathfinder.toolkit.model.character.stats.*;
import com.lateensoft.pathfinder.toolkit.model.party.EncounterParticipant;

public class PreV10PartyMemberConverter {

    public static EncounterParticipant convertEncounterParticipant(PreV10PartyMember member) {
        EncounterParticipant.Builder builder = EncounterParticipant.builder();
        transferStatsFromMember(member, builder);
        builder.setInitiativeScore(member.lastRolledValue);
        return builder.build();
    }

    public static PathfinderCharacter convertPartyMember(PreV10PartyMember member) {
        PathfinderCharacter.Builder builder = PathfinderCharacter.builder();
        transferStatsFromMember(member, builder);
        return builder.build();
    }

    // Many of the calculations here are using large assumptions, just to the same modifier is achieved
    private static void transferStatsFromMember(PreV10PartyMember member, PathfinderCharacter.Builder builder) {
        AbilitySet abilitySet = createAbilitySetFromMember(member);

        builder.setName(member.name)
                .setAbilitySet(abilitySet)
                .setCombatStatSet(createCombatStatsFromMember(member, abilitySet))
                .setSaveSet(createSavesFromMember(member, abilitySet))
                .setSkillSet(createSkillsFromMember(member, abilitySet));
    }

    private static AbilitySet createAbilitySetFromMember(PreV10PartyMember member) {
        AbilitySet abilitySet = new AbilitySet();
        abilitySet.getAbility(AbilityType.DEX).setScore(member.initiative);
        return abilitySet;
    }

    private static CombatStatSet createCombatStatsFromMember(PreV10PartyMember member, AbilitySet abilities) {
        CombatStatSet combatStatSet = new CombatStatSet();
        combatStatSet.setACArmourBonus(member.AC);
        combatStatSet.setSizeModifier(10 - getDexMod(abilities));
        combatStatSet.setNaturalArmour(10 - member.AC - getDexMod(abilities));
        combatStatSet.setSpellResistance(member.spellResist);
        return combatStatSet;
    }

    private static int getDexMod(AbilitySet abilities) {
        return abilities.getAbility(AbilityType.DEX).getTempModifier();
    }

    private static SaveSet createSavesFromMember(PreV10PartyMember member, AbilitySet abilities) {
        SaveSet saveSet = new SaveSet();
        saveSet.getSave(SaveType.FORT).setBaseSave(member.fortSave);
        saveSet.getSave(SaveType.REF).setBaseSave(member.reflexSave - getDexMod(abilities));
        saveSet.getSave(SaveType.WILL).setBaseSave(member.willSave);
        return saveSet;
    }

    private static SkillSet createSkillsFromMember(PreV10PartyMember member, AbilitySet abilities) {
        SkillSet skillSet = new SkillSet();
        skillSet.getSkillByType(SkillType.BLUFF).setRank(member.bluffSkillBonus);
        skillSet.getSkillByType(SkillType.DISGUISE).setRank(member.disguiseSkillBonus);
        skillSet.getSkillByType(SkillType.PERCEPTION).setRank(member.perceptionSkillBonus);
        skillSet.getSkillByType(SkillType.SENSE_MOTIVE).setRank(member.senseMotiveSkillBonus);
        skillSet.getSkillByType(SkillType.STEALTH).setRank(member.stealthSkillBonus - getDexMod(abilities));
        return skillSet;
    }
}
