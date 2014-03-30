package com.lateensoft.pathfinder.toolkit.serialize;

import com.google.common.collect.ImmutableBiMap;
import com.lateensoft.pathfinder.toolkit.model.character.stats.AbilitySet;
import com.lateensoft.pathfinder.toolkit.model.character.stats.Ability;
import org.dom4j.Element;

import java.io.InvalidObjectException;

/**
 * @author tsiemens
 */
public class AbilityXMLAdapter extends XMLObjectAdapter<Ability> {

    public static final String ELEMENT_NAME = "ability";
    private static final String ABILITY_ATTR = "ability-key";
    private static final String SCORE_ATTR = "score";
    private static final String TEMP_BONUS_ATTR = "temp-bonus";

    public static final ImmutableBiMap<Integer, String> ABILITY_KEY_STRINGS = buildAbilityKeyStrings();

    @Override
    public String getElementName() {
        return ELEMENT_NAME;
    }

    @Override
    protected Ability createObjectForElement(Element element) throws InvalidObjectException {
        String abilityKeyString = getStringAttribute(element, ABILITY_ATTR);

        ImmutableBiMap<String, Integer> abilityKeyStrings = ABILITY_KEY_STRINGS.inverse();
        Integer abilityKey = abilityKeyStrings.get(abilityKeyString);
        if (abilityKey == null) {
            throw new InvalidObjectException("Invalid ability: " + abilityKeyString);
        }

        Ability ability = new Ability(abilityKey);
        ability.setScore(getBoundedIntAttribute(element, SCORE_ATTR, 0, 40));
        ability.setTempBonus(getBoundedIntAttribute(element, TEMP_BONUS_ATTR, -20, 20));
        return ability;
    }

    @Override
    protected void setElementContentForObject(Element element, Ability skill) {
        element.addAttribute(ABILITY_ATTR, ABILITY_KEY_STRINGS.get(skill.getAbilityKey()));
        element.addAttribute(SCORE_ATTR, Integer.toString(skill.getScore()));
        element.addAttribute(TEMP_BONUS_ATTR, Integer.toString(skill.getTempBonus()));
    }

    private static ImmutableBiMap<Integer, String> buildAbilityKeyStrings() {
        ImmutableBiMap.Builder<Integer, String> builder = ImmutableBiMap.builder();
        builder.put(AbilitySet.KEY_STR, "STR");
        builder.put(AbilitySet.KEY_DEX, "DEX");
        builder.put(AbilitySet.KEY_CON, "CON");
        builder.put(AbilitySet.KEY_INT, "INT");
        builder.put(AbilitySet.KEY_WIS, "WIS");
        builder.put(AbilitySet.KEY_CHA, "CHA");
        return builder.build();
    }
}