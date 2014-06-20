package com.lateensoft.pathfinder.toolkit.serialize;

import com.google.common.collect.ImmutableBiMap;
import com.lateensoft.pathfinder.toolkit.model.character.stats.CombatStatSet;
import org.dom4j.Element;

import java.io.InvalidObjectException;

/**
 * @author trevsiemens
 */
public class CombatStatSetXMLAdapter extends XMLObjectAdapter<CombatStatSet> {

    public static final String ELEMENT_NAME = "combat-stats";
    private static final String HP_ATTR = "total-HP";
    private static final String WOUNDS_ATTR = "wounds";
    private static final String NON_LETHAL_DMG_ATTR = "non-lethal-dmg";
    private static final String DMG_REDUCT_ATTR = "dmg-reduct";
    private static final String SPEED_ATTR = "base-spd";

    private static final String INIT_ABILITY_ATTR = "init-ability";
    private static final String INIT_MISC_ATTR = "init-misc-mod";

    private static final String AC_ARMOR_ATTR = "AC-armour";
    private static final String AC_SHIELD_ATTR = "AC-shield";
    private static final String AC_ABILITY_ATTR = "AC-ability";
    private static final String SIZE_ATTR = "size-mod";
    private static final String AC_NATURAL_ATTR = "AC-natural-armour";
    private static final String DEFLECT_ATTR = "deflect-mod";
    private static final String AC_MISC_ATTR = "AC-misc-mod";
    private static final String SPELL_RESIST_ATTR = "spell-resist";

    private static final String BAB_P_ATTR = "BAB-primary";
    private static final String BAB_S_ATTR = "BAB-secondary";

    private static final String CMB_ABILITY_ATTR = "CMB-ability";

    private static final String CMD_ABILITY_ATTR = "CMD-ability";
    private static final String CMD_MISC_ATTR = "CMD-misc-mod";

    @Override
    public String getElementName() {
        return ELEMENT_NAME;
    }

    @Override
    protected CombatStatSet createObjectForElement(Element element) throws InvalidObjectException {
        ImmutableBiMap<String, Integer> abilityStringsToKeys = AbilityXMLAdapter.ABILITY_KEY_STRINGS.inverse();

        CombatStatSet combatStats = new CombatStatSet();
        combatStats.setTotalHP(getIntAttribute(element, HP_ATTR));
        combatStats.setWounds(getIntAttribute(element, WOUNDS_ATTR));
        combatStats.setNonLethalDamage(getIntAttribute(element, NON_LETHAL_DMG_ATTR));
        combatStats.setDamageReduction(getIntAttribute(element, DMG_REDUCT_ATTR));
        combatStats.setBaseSpeed(getIntAttribute(element, SPEED_ATTR));

        combatStats.setInitAbilityKey(getAbilityKey(abilityStringsToKeys,
                getStringAttribute(element, INIT_ABILITY_ATTR)));
        combatStats.setInitiativeMiscMod(getIntAttribute(element, INIT_MISC_ATTR));

        combatStats.setACArmourBonus(getIntAttribute(element, AC_ARMOR_ATTR));
        combatStats.setACShieldBonus(getIntAttribute(element, AC_SHIELD_ATTR));
        combatStats.setACAbilityKey(getAbilityKey(abilityStringsToKeys,
                getStringAttribute(element, AC_ABILITY_ATTR)));
        combatStats.setSizeModifier(getIntAttribute(element, SIZE_ATTR));
        combatStats.setNaturalArmour(getIntAttribute(element, AC_NATURAL_ATTR));
        combatStats.setDeflectionMod(getIntAttribute(element, DEFLECT_ATTR));
        combatStats.setACMiscMod(getIntAttribute(element, AC_MISC_ATTR));

        combatStats.setSpellResistance(getIntAttribute(element, SPELL_RESIST_ATTR));
        combatStats.setBABPrimary(getIntAttribute(element, BAB_P_ATTR));

        if (element.attribute(BAB_S_ATTR) != null) {
            combatStats.setBABSecondary(getStringAttribute(element, BAB_S_ATTR));
        }

        combatStats.setCMBAbilityKey(getAbilityKey(abilityStringsToKeys,
                getStringAttribute(element, CMB_ABILITY_ATTR)));
        combatStats.setCMDAbilityKey(getAbilityKey(abilityStringsToKeys,
                getStringAttribute(element, CMD_ABILITY_ATTR)));
        combatStats.setCMDMiscMod(getIntAttribute(element, CMD_MISC_ATTR));
        return combatStats;
    }

    @Override
    protected void setElementContentForObject(Element element, CombatStatSet combatStats) {
        element.addAttribute(HP_ATTR, Integer.toString(combatStats.getTotalHP()));
        element.addAttribute(WOUNDS_ATTR, Integer.toString(combatStats.getWounds()));
        element.addAttribute(NON_LETHAL_DMG_ATTR, Integer.toString(combatStats.getNonLethalDamage()));
        element.addAttribute(DMG_REDUCT_ATTR, Integer.toString(combatStats.getDamageReduction()));
        element.addAttribute(SPEED_ATTR, Integer.toString(combatStats.getBaseSpeed()));
        element.addAttribute(INIT_ABILITY_ATTR, AbilityXMLAdapter.ABILITY_KEY_STRINGS.get(combatStats.getInitAbilityKey()));
        element.addAttribute(INIT_MISC_ATTR, Integer.toString(combatStats.getInitiativeMiscMod()));
        element.addAttribute(AC_ARMOR_ATTR, Integer.toString(combatStats.getACArmourBonus()));
        element.addAttribute(AC_SHIELD_ATTR, Integer.toString(combatStats.getACShieldBonus()));
        element.addAttribute(AC_ABILITY_ATTR, AbilityXMLAdapter.ABILITY_KEY_STRINGS.get(combatStats.getACAbilityKey()));
        element.addAttribute(SIZE_ATTR, Integer.toString(combatStats.getSizeModifier()));
        element.addAttribute(AC_NATURAL_ATTR, Integer.toString(combatStats.getNaturalArmour()));
        element.addAttribute(DEFLECT_ATTR, Integer.toString(combatStats.getDeflectionMod()));
        element.addAttribute(AC_MISC_ATTR, Integer.toString(combatStats.getACMiscMod()));
        element.addAttribute(SPELL_RESIST_ATTR, Integer.toString(combatStats.getSpellResist()));
        element.addAttribute(BAB_P_ATTR, Integer.toString(combatStats.getBABPrimary()));
        element.addAttribute(BAB_S_ATTR, combatStats.getBABSecondary());
        element.addAttribute(CMB_ABILITY_ATTR, AbilityXMLAdapter.ABILITY_KEY_STRINGS.get(combatStats.getCMBAbilityKey()));
        element.addAttribute(CMD_ABILITY_ATTR, AbilityXMLAdapter.ABILITY_KEY_STRINGS.get(combatStats.getCMDAbilityKey()));
        element.addAttribute(CMD_MISC_ATTR, Integer.toString(combatStats.getCMDMiscMod()));
    }

    private int getAbilityKey(ImmutableBiMap<String, Integer> abilityStringsToKeys, String abilityString) throws InvalidObjectException {
        Integer abilityKey = abilityStringsToKeys.get(abilityString);
        if (abilityKey == null) {
            throw new InvalidObjectException("Invalid ability \"" + abilityString + "\" in combat stats");
        }
        return abilityKey;
    }
}
