package com.lateensoft.pathfinder.toolkit.serialize;

import com.google.common.collect.ImmutableBiMap;
import com.lateensoft.pathfinder.toolkit.model.character.stats.Ability;
import com.lateensoft.pathfinder.toolkit.model.character.stats.AbilityType;
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

    public static final ImmutableBiMap<AbilityType, String> ABILITY_TYPE_STRINGS = buildAbilityKeyStrings();

    @Override
    public String getElementName() {
        return ELEMENT_NAME;
    }

    @Override
    protected Ability createObjectForElement(Element element) throws InvalidObjectException {
        String abilityKeyString = getStringAttribute(element, ABILITY_ATTR);

        ImmutableBiMap<String, AbilityType> abilityKeyStrings = ABILITY_TYPE_STRINGS.inverse();
        AbilityType abilityKey = abilityKeyStrings.get(abilityKeyString);
        if (abilityKey == null) {
            throw new InvalidObjectException("Invalid ability: " + abilityKeyString);
        }

        Ability ability = new Ability(abilityKey);
        ability.setScore(getBoundedIntAttribute(element, SCORE_ATTR, 0, 40));
        ability.setTempBonus(getBoundedIntAttribute(element, TEMP_BONUS_ATTR, -20, 20));
        return ability;
    }

    @Override
    protected void setElementContentForObject(Element element, Ability ability) {
        element.addAttribute(ABILITY_ATTR, ABILITY_TYPE_STRINGS.get(ability.getType()));
        element.addAttribute(SCORE_ATTR, Integer.toString(ability.getScore()));
        element.addAttribute(TEMP_BONUS_ATTR, Integer.toString(ability.getTempBonus()));
    }

    private static ImmutableBiMap<AbilityType, String> buildAbilityKeyStrings() {
        ImmutableBiMap.Builder<AbilityType, String> builder = ImmutableBiMap.builder();
        builder.put(AbilityType.STR, "STR");
        builder.put(AbilityType.DEX, "DEX");
        builder.put(AbilityType.CON, "CON");
        builder.put(AbilityType.INT, "INT");
        builder.put(AbilityType.WIS, "WIS");
        builder.put(AbilityType.CHA, "CHA");
        return builder.build();
    }
}