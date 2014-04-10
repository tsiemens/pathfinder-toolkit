package com.lateensoft.pathfinder.toolkit.serialize;

import com.lateensoft.pathfinder.toolkit.model.character.*;
import com.lateensoft.pathfinder.toolkit.model.character.stats.AbilitySet;
import com.lateensoft.pathfinder.toolkit.model.character.stats.CombatStatSet;
import com.lateensoft.pathfinder.toolkit.model.character.stats.SaveSet;
import com.lateensoft.pathfinder.toolkit.model.character.stats.SkillSet;
import org.dom4j.Element;

import java.io.InvalidObjectException;

/**
 * @author trevsiemens
 */
public class CharacterXMLAdapter extends XMLObjectAdapter<PathfinderCharacter> {
    public static final String ELEMENT_NAME = "character";
    public static final String GOLD_ATTR = "gold";

    private AbilitySetXMLAdapter m_abilitySetXMLAdapter = new AbilitySetXMLAdapter();
    private CombatStatSetXMLAdapter m_combatStatXMLAdapter = new CombatStatSetXMLAdapter();
    private FeatListXMLAdapter m_featListXMLAdapter = new FeatListXMLAdapter();
    private FluffXMLAdapter m_fluffXMLAdapter = new FluffXMLAdapter();
    private InventoryXMLAdapter m_inventoryXMLAdapter = new InventoryXMLAdapter();
    private SaveSetXMLAdapter m_saveSetXMLAdapter = new SaveSetXMLAdapter();
    private SkillSetXMLAdapter m_skillSetXMLAdapter = new SkillSetXMLAdapter();
    private SpellBookXMLAdapter m_spellbookXMLAdapter = new SpellBookXMLAdapter();

    @Override
    public String getElementName() {
        return ELEMENT_NAME;
    }

    @Override
    protected PathfinderCharacter createObjectForElement(Element element) throws InvalidObjectException {
        double gold = getBoundedDoubleAttribute(element, GOLD_ATTR, 0.0, Double.MAX_VALUE);
        FluffInfo fluff = getSubObject(element, m_fluffXMLAdapter);
        AbilitySet abilitySet = getSubObject(element, m_abilitySetXMLAdapter);
        CombatStatSet combatStatSet = getSubObject(element, m_combatStatXMLAdapter);
        FeatList feats = getSubObject(element, m_featListXMLAdapter);
        Inventory inv = getSubObject(element, m_inventoryXMLAdapter);
        SaveSet saves = getSubObject(element, m_saveSetXMLAdapter);
        SkillSet skillSet = getSubObject(element, m_skillSetXMLAdapter);
        SpellBook spells = getSubObject(element, m_spellbookXMLAdapter);
        return new PathfinderCharacter(gold, abilitySet, fluff, combatStatSet, saves, skillSet, inv, feats, spells);
    }

    @Override
    protected void setElementContentForObject(Element element, PathfinderCharacter character) {
        element.add(m_fluffXMLAdapter.toXML(character.getFluff()));
        element.addAttribute(GOLD_ATTR, Double.toString(character.getGold()));
        element.add(m_abilitySetXMLAdapter.toXML(character.getAbilitySet()));
        element.add(m_combatStatXMLAdapter.toXML(character.getCombatStatSet()));
        element.add(m_featListXMLAdapter.toXML(character.getFeatList()));
        element.add(m_inventoryXMLAdapter.toXML(character.getInventory()));
        element.add(m_saveSetXMLAdapter.toXML(character.getSaveSet()));
        element.add(m_skillSetXMLAdapter.toXML(character.getSkillSet()));
        element.add(m_spellbookXMLAdapter.toXML(character.getSpellBook()));
    }
}
