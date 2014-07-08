package com.lateensoft.pathfinder.toolkit.adapters;

/**
 * Created by trevsiemens on 08/07/2014.
 */
public interface SelectableItemAdapter {
    public interface ItemSelectionGetter {
        public boolean isItemSelected(int position);
    }

    public void setItemSelectionGetter(ItemSelectionGetter getter);
}
