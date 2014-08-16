package com.lateensoft.pathfinder.toolkit.views.character;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import com.google.inject.internal.Nullable;
import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.dao.Identifiable;
import com.lateensoft.pathfinder.toolkit.dao.IdentifiableGenericDAO;
import com.lateensoft.pathfinder.toolkit.dao.OwnedGenericDAO;
import com.lateensoft.pathfinder.toolkit.views.ParcelableEditorActivity;
import org.jetbrains.annotations.NotNull;

public abstract class ParcelableListFragment<T extends Parcelable & Identifiable,
        DAO extends OwnedGenericDAO<Long, ?, T> & IdentifiableGenericDAO<T>>
        extends AbstractCharacterSheetFragment {

    private static final String TAG = ParcelableListFragment.class.getSimpleName();

    protected void showEditorActivityForNewObject() {
        showEditorActivity(null);
    }

    protected void showEditorActivity(T toEdit) {
        Intent editorIntent = new Intent(getContext(),
                getParcelableEditorActivity());
        editorIntent.putExtra(
                ParcelableEditorActivity.INTENT_EXTRAS_KEY_EDITABLE_PARCELABLE, toEdit);
        addCustomExtrasToEditorActivityIntent(editorIntent, toEdit);

        startActivityForResult(editorIntent, ParcelableEditorActivity.DEFAULT_REQUEST_CODE);
    }

    protected abstract Class<? extends ParcelableEditorActivity> getParcelableEditorActivity();

    protected void addCustomExtrasToEditorActivityIntent(Intent intent, T toEdit) { }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ParcelableEditorActivity.DEFAULT_REQUEST_CODE) {
            onParcelableEditorActivityResult(resultCode, data);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void onParcelableEditorActivityResult(int resultCode, Intent data) {
        try {
            boolean shouldUpdateUI = false;
            if (resultCode == Activity.RESULT_OK) {
                T editedParcelable = ParcelableEditorActivity.getParcelableFromIntent(data);
                shouldUpdateUI = handleEditedResult(editedParcelable);
            } else if (resultCode == FeatEditActivity.RESULT_DELETE) {
                T toDelete = getObjectMarkedForDeletion();
                shouldUpdateUI = handleResultToDelete(toDelete);
            }

            if (shouldUpdateUI) {
                updateFragmentUI();
            }
        } catch (DataAccessException e) {
            Log.e(TAG, "DAO operation failed", e);
        }
    }

    private boolean handleEditedResult(@Nullable T editedParcelable) throws DataAccessException {
        boolean didEditModel = false;
        if (editedParcelable != null) {
            EditAction action = getActionForResult(editedParcelable);
            if (action == EditAction.ADD) {
                getDAO().add(getCurrentCharacterID(), editedParcelable);
                didEditModel = true;
            } else if (action == EditAction.UPDATE) {
                getDAO().update(getCurrentCharacterID(), editedParcelable);
                didEditModel = true;
            }

            if (didEditModel) {
                updateModel(action, editedParcelable);
            }
        }
        return didEditModel;
    }

    private boolean handleResultToDelete(@Nullable T toDelete) throws DataAccessException {
        if (toDelete != null) {
            getDAO().remove(toDelete);
            removeFromModel(toDelete);
            return true;
        }
        return false;
    }

    protected enum EditAction {NONE, ADD, UPDATE}

    protected abstract EditAction getActionForResult(@NotNull T result);

    protected abstract DAO getDAO();

    protected abstract void updateModel(EditAction action, T updatedParcelable);

    protected abstract T getObjectMarkedForDeletion();

    protected abstract void removeFromModel(T toRemove);

    @Override
    public void updateDatabase() {
        // Done in onActivityResult
    }
}
