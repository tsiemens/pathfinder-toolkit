package com.lateensoft.pathfinder.toolkit.serialize;

import com.lateensoft.pathfinder.toolkit.model.character.stats.Skill;
import com.lateensoft.pathfinder.toolkit.model.character.stats.SkillSet;

import java.util.List;

/**
 * @author tsiemens
 */
public class SkillSetXMLAdapter extends IterableXMLAdapter<SkillSet, Skill, SkillXMLAdapter> {

    public static final String ELEMENT_NAME = "skills";

    private SkillXMLAdapter m_skillAdapter = new SkillXMLAdapter();

    @Override
    public String getElementName() {
        return ELEMENT_NAME;
    }

    @Override
    public SkillXMLAdapter getItemAdapter() {
        return m_skillAdapter;
    }

    @Override
    protected SkillSet createObjectFromItems(List<Skill> items) {
        return new SkillSet(items, null);
    }
}
