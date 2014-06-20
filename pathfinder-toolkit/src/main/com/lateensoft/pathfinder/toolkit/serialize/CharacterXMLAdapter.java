package com.lateensoft.pathfinder.toolkit.serialize;

import com.lateensoft.pathfinder.toolkit.model.character.*;
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
        PathfinderCharacter.Builder builder = new PathfinderCharacter.Builder();
        setBuilderContentForElement(builder, element);
        return builder.build();
    }

    protected void setBuilderContentForElement(PathfinderCharacter.Builder builder, Element element) throws InvalidObjectException {
        builder.setGold(getBoundedDoubleAttribute(element, GOLD_ATTR, 0.0, Double.MAX_VALUE))
            .setFluffInfo(getSubObject(element, m_fluffXMLAdapter))
            .setAbilitySet(getSubObject(element, m_abilitySetXMLAdapter))
            .setCombatStatSet(getSubObject(element, m_combatStatXMLAdapter))
            .setFeats(getSubObject(element, m_featListXMLAdapter))
            .setInventory(getSubObject(element, m_inventoryXMLAdapter))
            .setSaveSet(getSubObject(element, m_saveSetXMLAdapter))
            .setSkillSet(getSubObject(element, m_skillSetXMLAdapter))
            .setSpellBook(getSubObject(element, m_spellbookXMLAdapter));
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
