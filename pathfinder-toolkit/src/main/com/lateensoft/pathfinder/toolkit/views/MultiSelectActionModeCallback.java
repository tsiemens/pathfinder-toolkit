package com.lateensoft.pathfinder.toolkit.views;

import android.util.SparseBooleanArray;
import android.view.*;
import android.widget.ListView;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author tsiemens
 */
public abstract class MultiSelectActionModeCallback implements ActionMode.Callback {

    // Integrated checking/selection in ListView is not behaving as expected, so this is needed for now
    private SparseBooleanArray m_actionModeSelections;
    private int m_menuResId;

    public MultiSelectActionModeCallback(int menuResId) {
        m_actionModeSelections = new SparseBooleanArray();
        m_menuResId = menuResId;
    }

    public boolean isListItemSelected(int position) {
        return m_actionModeSelections != null && m_actionModeSelections.get(position);
    }

    public void selectListItem(int position, boolean select) {
        m_actionModeSelections.put(position, select);
        getListView().invalidateViews();
    }

    public void toggleListItemSelection(int position) {
        m_actionModeSelections.put(position, !m_actionModeSelections.get(position));
        getListView().invalidateViews();
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(m_menuResId, menu);
        return true;
    }

    // Called each time the action mode is shown. Always called after onCreateActionMode, but
    // may be called multiple times if the mode is invalidated.
    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false; // Return false if nothing is done
    }

    // Called when the user exits the action mode
    @Override
    public void onDestroyActionMode(ActionMode mode) {
        m_actionModeSelections = null;
        getListView().invalidateViews();
    }

    /** @param listItems the items which are being represented in the listView
      * @return items which are currently selected
      **/
    public <T> List<T> getSelectedItems(List<T> listItems) {
        List<T> selectedItems = Lists.newArrayListWithCapacity(m_actionModeSelections.size());
        for (int i = 0; i < m_actionModeSelections.size(); i++) {
            int key = m_actionModeSelections.keyAt(i);
            if (m_actionModeSelections.get(key)) {
                selectedItems.add(listItems.get(key));
            }
        }
        return selectedItems;
    }

    public abstract ListView getListView();
}
