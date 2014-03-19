package com.lateensoft.pathfinder.toolkit.serialize;

import com.lateensoft.pathfinder.toolkit.model.character.PTFeat;
import com.lateensoft.pathfinder.toolkit.model.character.PTFeatList;
import java.util.List;

/**
 * @author tsiemens
 */
public class FeatListXMLAdapter extends ListXMLAdapter<PTFeatList, PTFeat, FeatXMLAdapter> {

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
    protected PTFeatList createFromItems(List<PTFeat> items) {
        return new PTFeatList(items);
    }
}
