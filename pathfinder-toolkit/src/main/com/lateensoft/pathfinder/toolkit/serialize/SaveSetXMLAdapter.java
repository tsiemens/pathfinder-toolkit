package com.lateensoft.pathfinder.toolkit.serialize;

import com.lateensoft.pathfinder.toolkit.model.character.stats.Save;
import com.lateensoft.pathfinder.toolkit.model.character.stats.SaveSet;

import java.util.List;

/**
 * @author tsiemens
 */
public class SaveSetXMLAdapter extends IterableXMLAdapter<SaveSet, Save, SaveXMLAdapter> {

    public static final String ELEMENT_NAME = "saves";

    private SaveXMLAdapter m_saveAdapter = new SaveXMLAdapter();

    @Override
    public String getElementName() {
        return ELEMENT_NAME;
    }

    @Override
    public SaveXMLAdapter getItemAdapter() {
        return m_saveAdapter;
    }

    @Override
    protected SaveSet createObjectFromItems(List<Save> items) {
        return SaveSet.newValidatedSaveSet(items, null);
    }
}
