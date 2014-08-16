package com.lateensoft.pathfinder.toolkit.views;

import android.app.Activity;
import android.view.ActionMode;
import android.view.MenuItem;
import android.widget.ListView;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import java.util.List;

public abstract class MultiSelectActionModeController {

    private ActionMode actionMode;
    private ActionModeCallback actionModeCallback;

    public boolean isActionModeStarted() {
        return actionMode != null;
    }

    public void startActionModeWithInitialSelection(int position) {
        startActionMode();
        actionModeCallback.selectListItem(position, true);
    }

    public void startActionMode() {
        Preconditions.checkState(actionMode == null);
        actionModeCallback = new ActionModeCallback();
        actionMode = getActivity().startActionMode(actionModeCallback);
    }

    public abstract Activity getActivity();

    public void toggleListItemSelection(int position) {
        Preconditions.checkState(actionModeCallback != null);
        actionModeCallback.toggleListItemSelection(position);
    }

    public boolean isListItemSelected(int position) {
        return actionModeCallback != null &&
                actionModeCallback.isListItemSelected(position);
    }

    public <T> List<T> getSelectedItems(List<T> listItems) {
        if (actionModeCallback != null) {
            return actionModeCallback.getSelectedItems(listItems);
        } else {
            return Lists.newArrayList();
        }
    }

    private class ActionModeCallback extends MultiSelectActionModeCallback {

        public ActionModeCallback() {
            super(getActionMenuResourceId());
        }

        @Override
        public ListView getListView() {
            return MultiSelectActionModeController.this.getListView();
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return MultiSelectActionModeController.this
                    .onActionItemClicked(MultiSelectActionModeController.this, item);
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
            actionModeCallback = null;
            super.onDestroyActionMode(mode);
        }
    }

    public abstract int getActionMenuResourceId();

    public void finishActionMode() {
        Preconditions.checkState(actionMode != null);
        actionMode.finish();
    }

    public abstract ListView getListView();

    public abstract boolean onActionItemClicked(MultiSelectActionModeController controller,
                                                MenuItem item);
}
