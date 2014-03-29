package com.lateensoft.pathfinder.toolkit.serialize;

import com.lateensoft.pathfinder.toolkit.model.character.Feat;
import com.lateensoft.pathfinder.toolkit.model.character.FeatList;

import java.util.List;

/**
 * @author tsiemens
 */
public class FeatListXMLAdapter extends ListXMLAdapter<FeatList, Feat, FeatXMLAdapter> {

    public static final String ELEMENT_NAME = "featlist";

    private final FeatXMLAdapter m_featAdapter = new FeatXMLAdapter();

    @Override
    public String getElementName() {
        return ELEMENT_NAME;
    }

    @Override
    public FeatXMLAdapter getItemAdapter() {
        return m_featAdapter;
    }

    @Override
    protected FeatList createObjectFromItems(List<Feat> items) {
        return new FeatList(items);
    }
}
