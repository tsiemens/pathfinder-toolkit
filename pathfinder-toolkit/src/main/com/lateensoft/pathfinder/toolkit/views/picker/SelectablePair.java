package com.lateensoft.pathfinder.toolkit.views.picker;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.model.IdNamePair;

import java.util.List;

/**
 * @author tsiemens
 */
public class SelectablePair extends IdNamePair {
    private boolean m_isSelected;

    public static List<SelectablePair> createSelectableObjects(List<IdNamePair> objects) {
        List<SelectablePair> selectablePairs = Lists.newArrayListWithCapacity(objects.size());
        for (IdNamePair object : objects) {
            selectablePairs.add(new SelectablePair(object));
        }
        return selectablePairs;
    }

    public SelectablePair(IdNamePair pair) {
        super(pair);
        m_isSelected = false;
    }

    public SelectablePair(Parcel in) {
        super(in);
        boolean[] selected = new boolean[1];
        in.readBooleanArray(selected);
        m_isSelected = selected[0];
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        boolean[] selected = new boolean[1];
        selected[0] = m_isSelected;
        out.writeBooleanArray(selected);
    }

    public void setSelected(boolean selected) {
        m_isSelected = selected;
    }

    public boolean isSelected() {
        return m_isSelected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<SelectablePair> CREATOR = new Parcelable.Creator<SelectablePair>() {
        public SelectablePair createFromParcel(Parcel in) {
            return new SelectablePair(in);
        }

        public SelectablePair[] newArray(int size) {
            return new SelectablePair[size];
        }
    };
}
