package com.lateensoft.pathfinder.toolkit.views.character;

import android.os.Parcelable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.lateensoft.pathfinder.toolkit.dao.Identifiable;
import com.lateensoft.pathfinder.toolkit.dao.IdentifiableGenericDAO;
import com.lateensoft.pathfinder.toolkit.dao.OwnedGenericDAO;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class IndexedParcelableListFragment<T extends Parcelable & Identifiable,
        DAO extends OwnedGenericDAO<Long,?,T> & IdentifiableGenericDAO<T>>
        extends ParcelableListFragment<T, DAO> {

    private int parcelableIndexSelectedForEdit;

    protected OnClickListener getAddButtonClickListener() {
        return addButtonClickListener;
    }

    private OnClickListener addButtonClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            parcelableIndexSelectedForEdit = -1;
            showEditorActivityForNewObject();
        }
    };

    protected OnItemClickListener getListItemClickListener() {
        return listItemClickListener;
    }

    private OnItemClickListener listItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            parcelableIndexSelectedForEdit = position;
            showEditorActivity(getModel().get(position));
        }
    };

    protected abstract List<T> getModel();

    @Override
    protected EditAction getActionForResult(@NotNull T result) {
        return parcelableIndexSelectedForEdit < 0 ? EditAction.ADD : EditAction.UPDATE;
    }

    @Override
    protected void updateModel(EditAction action, T updatedParcelable) {
        if (action == EditAction.ADD){
            getModel().add(updatedParcelable);
        } else if (action == EditAction.UPDATE) {
            getModel().set(parcelableIndexSelectedForEdit, updatedParcelable);
        }
    }

    @Override
    protected T getObjectMarkedForDeletion() {
        return parcelableIndexSelectedForEdit >= 0 ? getModel().get(parcelableIndexSelectedForEdit) : null;
    }

    @Override
    protected void removeFromModel(T toRemove) {
        getModel().remove(parcelableIndexSelectedForEdit);
    }
}
