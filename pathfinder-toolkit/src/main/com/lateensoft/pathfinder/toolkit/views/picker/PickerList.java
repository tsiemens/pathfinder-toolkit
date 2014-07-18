package com.lateensoft.pathfinder.toolkit.views.picker;

import android.os.Parcel;
import android.os.Parcelable;
import com.lateensoft.pathfinder.toolkit.model.IdNamePair;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
* @author tsiemens
*/
public class PickerList extends ArrayList<SelectablePair> implements Parcelable {

    private String key;
    private String name;

    public PickerList(String key, String name, List<IdNamePair> items, @Nullable List<IdNamePair> selectedItems) {
        this(SelectablePair.createSelectableObjects(items, selectedItems), key, name);
    }

    public PickerList(List<SelectablePair> items, String key, String name) {
        super(items);
        this.key = key;
        this.name = name;
    }

    public PickerList(Parcel in) {
        key = in.readString();
        name = in.readString();
        in.readTypedList(this, SelectablePair.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(key);
        out.writeString(name);
        out.writeTypedList(this);
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
