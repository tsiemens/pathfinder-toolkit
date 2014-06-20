package com.lateensoft.pathfinder.toolkit.serialize;

import com.google.common.collect.ImmutableBiMap;
import com.lateensoft.pathfinder.toolkit.model.character.stats.AbilityType;
import com.lateensoft.pathfinder.toolkit.model.character.stats.Skill;
import com.lateensoft.pathfinder.toolkit.model.character.stats.SkillType;
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

    public static final ImmutableBiMap<SkillType, String> SKILL_TYPE_STRINGS = buildSkillKeyStrings();

    @Override
    public String getElementName() {
        return ELEMENT_NAME;
    }

    @Override
    protected Skill createObjectForElement(Element element) throws InvalidObjectException {
        String skillKeyString = getStringAttribute(element, SKILL_KEY_ATTR);
        ImmutableBiMap<String, SkillType> skillKeyStrings = SKILL_TYPE_STRINGS.inverse();
        SkillType skillType = skillKeyStrings.get(skillKeyString);
        if (skillType == null) {
            throw new InvalidObjectException("Invalid skill: " + skillKeyString);
        }

        String abilityKeyString = getStringAttribute(element, ABILITY_ATTR);
        ImmutableBiMap<String, AbilityType> abilityKeyStrings = AbilityXMLAdapter.ABILITY_TYPE_STRINGS.inverse();
        AbilityType abilityType = abilityKeyStrings.get(abilityKeyString);
        if (abilityType == null) {
            throw new InvalidObjectException("Invalid ability \"" + abilityKeyString + "\" for skill: " + skillKeyString);
        }

        Skill skill = new Skill(skillType, abilityType);
        skill.setClassSkill(getBooleanAttribute(element, CLASS_SKILL_ATTR));
        skill.setRank(getBoundedIntAttribute(element, RANK_ATTR, -10, 40));
        skill.setMiscMod(getBoundedIntAttribute(element, MISC_MOD_ATTR, -10, 40));

        if (skillType.canBeSubTyped() && element.element(SUB_TYPE_ELMT) != null) {
            skill.setSubType(getSubElementText(element, SUB_TYPE_ELMT));
        }
        return skill;
    }

    @Override
    protected void setElementContentForObject(Element element, Skill skill) {
        element.addAttribute(SKILL_KEY_ATTR, SKILL_TYPE_STRINGS.get(skill.getType()));
        element.addAttribute(ABILITY_ATTR, AbilityXMLAdapter.ABILITY_TYPE_STRINGS.get(skill.getAbility()));
        element.addAttribute(CLASS_SKILL_ATTR, Boolean.toString(skill.isClassSkill()));
        element.addAttribute(RANK_ATTR, Integer.toString(skill.getRank()));
        element.addAttribute(MISC_MOD_ATTR, Integer.toString(skill.getMiscMod()));

        if (skill.canBeSubTyped() && skill.getSubType() != null) {
            addSubElementText(element, SUB_TYPE_ELMT, skill.getSubType());
        }
    }

    private static ImmutableBiMap<SkillType, String> buildSkillKeyStrings() {
        ImmutableBiMap.Builder<SkillType, String> builder = ImmutableBiMap.builder();
        builder.put(SkillType.ACRO, "Acrobatics");
        builder.put(SkillType.APPRAISE, "Appraise");
        builder.put(SkillType.BLUFF, "Bluff");
        builder.put(SkillType.CLIMB, "Climb");
        builder.put(SkillType.CRAFT, "Craft");
        builder.put(SkillType.DIPLOM, "Diplomacy");
        builder.put(SkillType.DISABLE_DEV, "DisableDevice");
        builder.put(SkillType.DISGUISE, "Disguise");
        builder.put(SkillType.ESCAPE, "EscapeArtist");
        builder.put(SkillType.FLY, "Fly");
        builder.put(SkillType.HANDLE_ANIMAL, "Handle Animal");
        builder.put(SkillType.HEAL, "Heal");
        builder.put(SkillType.INTIMIDATE, "Intimidate");
        builder.put(SkillType.KNOW_ARCANA, "KnowledgeArcana");
        builder.put(SkillType.KNOW_DUNGEON, "KnowledgeDungeoneering");
        builder.put(SkillType.KNOW_ENG, "KnowledgeEngineering");
        builder.put(SkillType.KNOW_GEO, "KnowledgeGeography");
        builder.put(SkillType.KNOW_HIST, "KnowledgeHistory");
        builder.put(SkillType.KNOW_LOCAL, "KnowledgeLocal");
        builder.put(SkillType.KNOW_NATURE, "KnowledgeNature");
        builder.put(SkillType.KNOW_NOBILITY, "KnowledgeNobility");
        builder.put(SkillType.KNOW_PLANES, "KnowledgePlanes");
        builder.put(SkillType.KNOW_RELIGION, "KnowledgeReligion");
        builder.put(SkillType.LING, "Linguistics");
        builder.put(SkillType.PERCEPT, "Perception");
        builder.put(SkillType.PERFORM, "Perform");
        builder.put(SkillType.PROF, "Profession");
        builder.put(SkillType.RIDE, "Ride");
        builder.put(SkillType.SENSE_MOTIVE, "SenseMotive");
        builder.put(SkillType.SLEIGHT_OF_HAND, "SleightOfHand");
        builder.put(SkillType.SPELLCRAFT, "Spellcraft");
        builder.put(SkillType.STEALTH, "Stealth");
        builder.put(SkillType.SURVIVAL, "Survival");
        builder.put(SkillType.SWIM, "Swim");
        builder.put(SkillType.USE_MAGIC_DEVICE, "UseMagicDevice");
        return builder.build();
    }
}