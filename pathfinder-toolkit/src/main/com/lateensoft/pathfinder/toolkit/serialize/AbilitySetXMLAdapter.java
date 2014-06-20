package com.lateensoft.pathfinder.toolkit.serialize;

import com.lateensoft.pathfinder.toolkit.model.character.stats.Ability;
import com.lateensoft.pathfinder.toolkit.model.character.stats.AbilitySet;

import java.util.List;

/**
 * @author tsiemens
 */
public class AbilitySetXMLAdapter extends IterableXMLAdapter<AbilitySet, Ability, AbilityXMLAdapter> {

    public static final String ELEMENT_NAME = "abilities";

    private AbilityXMLAdapter m_abilityAdapter = new AbilityXMLAdapter();

    @Override
    public String getElementName() {
        return ELEMENT_NAME;
    }

    @Override
    public AbilityXMLAdapter getItemAdapter() {
        return m_abilityAdapter;
    }

    @Override
    protected AbilitySet createObjectFromItems(List<Ability> items) {
        return new AbilitySet(items, null);
    }
}
