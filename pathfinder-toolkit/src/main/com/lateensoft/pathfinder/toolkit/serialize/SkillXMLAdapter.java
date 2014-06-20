package com.lateensoft.pathfinder.toolkit.serialize;

import com.google.common.collect.ImmutableBiMap;
import com.lateensoft.pathfinder.toolkit.model.character.stats.Skill;
import com.lateensoft.pathfinder.toolkit.model.character.stats.SkillSet;
import org.dom4j.Element;

import java.io.InvalidObjectException;

/**
 * @author tsiemens
 */
public class SkillXMLAdapter extends XMLObjectAdapter<Skill> {

    public static final String ELEMENT_NAME = "skill";
    private static final String SKILL_KEY_ATTR = "skill-key";
    private static final String CLASS_SKILL_ATTR = "class-skill";
    private static final String RANK_ATTR = "rank";
    private static final String ABILITY_ATTR = "ability";
    private static final String MISC_MOD_ATTR = "misc";
    private static final String SUB_TYPE_ELMT = "sub-type";

    public static final ImmutableBiMap<Integer, String> SKILL_KEY_STRINGS = buildSkillKeyStrings();

    @Override
    public String getElementName() {
        return ELEMENT_NAME;
    }

    @Override
    protected Skill createObjectForElement(Element element) throws InvalidObjectException {
        String skillKeyString = getStringAttribute(element, SKILL_KEY_ATTR);
        ImmutableBiMap<String, Integer> skillKeyStrings = SKILL_KEY_STRINGS.inverse();
        Integer skillKey = skillKeyStrings.get(skillKeyString);
        if (skillKey == null) {
            throw new InvalidObjectException("Invalid skill: " + skillKeyString);
        }

        String abilityKeyString = getStringAttribute(element, ABILITY_ATTR);
        ImmutableBiMap<String, Integer> abilityKeyStrings = AbilityXMLAdapter.ABILITY_KEY_STRINGS.inverse();
        Integer abilityKey = abilityKeyStrings.get(abilityKeyString);
        if (abilityKey == null) {
            throw new InvalidObjectException("Invalid ability \"" + abilityKeyString + "\" for skill: " + skillKeyString);
        }

        Skill skill = new Skill(skillKey, abilityKey);
        skill.setClassSkill(getBooleanAttribute(element, CLASS_SKILL_ATTR));
        skill.setRank(getBoundedIntAttribute(element, RANK_ATTR, -10, 40));
        skill.setMiscMod(getBoundedIntAttribute(element, MISC_MOD_ATTR, -10, 40));

        if (SkillSet.isSubtypedSkill(skillKey) && element.element(SUB_TYPE_ELMT) != null) {
            skill.setSubType(getSubElementText(element, SUB_TYPE_ELMT));
        }
        return skill;
    }

    @Override
    protected void setElementContentForObject(Element element, Skill skill) {
        element.addAttribute(SKILL_KEY_ATTR, SKILL_KEY_STRINGS.get(skill.getSkillKey()));
        element.addAttribute(ABILITY_ATTR, AbilityXMLAdapter.ABILITY_KEY_STRINGS.get(skill.getAbilityKey()));
        element.addAttribute(CLASS_SKILL_ATTR, Boolean.toString(skill.isClassSkill()));
        element.addAttribute(RANK_ATTR, Integer.toString(skill.getRank()));
        element.addAttribute(MISC_MOD_ATTR, Integer.toString(skill.getMiscMod()));

        if (SkillSet.isSubtypedSkill(skill.getSkillKey()) && skill.getSubType() != null) {
            addSubElementText(element, SUB_TYPE_ELMT, skill.getSubType());
        }
    }

    private static ImmutableBiMap<Integer, String> buildSkillKeyStrings() {
        ImmutableBiMap.Builder<Integer, String> builder = ImmutableBiMap.builder();
        builder.put(SkillSet.ACRO, "Acrobatics");
        builder.put(SkillSet.APPRAISE, "Appraise");
        builder.put(SkillSet.BLUFF, "Bluff");
        builder.put(SkillSet.CLIMB, "Climb");
        builder.put(SkillSet.CRAFT, "Craft");
        builder.put(SkillSet.DIPLOM, "Diplomacy");
        builder.put(SkillSet.DISABLE_DEV, "DisableDevice");
        builder.put(SkillSet.DISGUISE, "Disguise");
        builder.put(SkillSet.ESCAPE, "EscapeArtist");
        builder.put(SkillSet.FLY, "Fly");
        builder.put(SkillSet.HANDLE_ANIMAL, "Handle Animal");
        builder.put(SkillSet.HEAL, "Heal");
        builder.put(SkillSet.INTIMIDATE, "Intimidate");
        builder.put(SkillSet.KNOW_ARCANA, "KnowledgeArcana");
        builder.put(SkillSet.KNOW_DUNGEON, "KnowledgeDungeoneering");
        builder.put(SkillSet.KNOW_ENG, "KnowledgeEngineering");
        builder.put(SkillSet.KNOW_GEO, "KnowledgeGeography");
        builder.put(SkillSet.KNOW_HIST, "KnowledgeHistory");
        builder.put(SkillSet.KNOW_LOCAL, "KnowledgeLocal");
        builder.put(SkillSet.KNOW_NATURE, "KnowledgeNature");
        builder.put(SkillSet.KNOW_NOBILITY, "KnowledgeNobility");
        builder.put(SkillSet.KNOW_PLANES, "KnowledgePlanes");
        builder.put(SkillSet.KNOW_RELIGION, "KnowledgeReligion");
        builder.put(SkillSet.LING, "Linguistics");
        builder.put(SkillSet.PERCEPT, "Perception");
        builder.put(SkillSet.PERFORM, "Perform");
        builder.put(SkillSet.PROF, "Profession");
        builder.put(SkillSet.RIDE, "Ride");
        builder.put(SkillSet.SENSE_MOTIVE, "SenseMotive");
        builder.put(SkillSet.SLEIGHT_OF_HAND, "SleightOfHand");
        builder.put(SkillSet.SPELLCRAFT, "Spellcraft");
        builder.put(SkillSet.STEALTH, "Stealth");
        builder.put(SkillSet.SURVIVAL, "Survival");
        builder.put(SkillSet.SWIM, "Swim");
        builder.put(SkillSet.USE_MAGIC_DEVICE, "UseMagicDevice");
        return builder.build();
    }
}