package com.lateensoft.pathfinder.toolkit.views.picker;

import android.os.Parcel;
import android.os.Parcelable;
import com.lateensoft.pathfinder.toolkit.model.IdNamePair;

import java.util.ArrayList;
import java.util.List;

/**
* @author tsiemens
*/
public class PickerList extends ArrayList<SelectablePair> implements Parcelable {

    private String m_key;
    private String m_name;

    public PickerList(String key, String name, List<IdNamePair> items) {
        this(SelectablePair.createSelectableObjects(items), key, name);
    }

    public PickerList(List<SelectablePair> items, String key, String name) {
        super(items);
        m_key = key;
        m_name = name;
    }

    public PickerList(Parcel in) {
        m_key = in.readString();
        m_name = in.readString();
        in.readTypedList(this, SelectablePair.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(m_key);
        out.writeString(m_name);
        out.writeTypedList(this);
    }

    public String getKey() {
        return m_key;
    }

    public String getName() {
        return m_name;
    }

    public void setName(String name) {
        m_name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PickerList> CREATOR = new Creator<PickerList>() {
        public PickerList createFromParcel(Parcel in) {
            return new PickerList(in);
        }

        public PickerList[] newArray(int size) {
            return new PickerList[size];
        }
    };
}
