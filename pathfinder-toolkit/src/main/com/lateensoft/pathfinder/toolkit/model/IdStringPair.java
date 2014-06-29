package com.lateensoft.pathfinder.toolkit.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.common.collect.Lists;
import com.google.common.primitives.Longs;
import com.lateensoft.pathfinder.toolkit.dao.Identifiable;

import java.util.List;

/**
 * @author tsiemens
 */
public class IdStringPair implements Parcelable, Comparable<IdStringPair>, Identifiable {

    private long id;
    private String value;

    public IdStringPair(long id, String value) {
        this.id = id;
        this.value = value;
    }

    public IdStringPair(IdStringPair toCopy) {
        this(toCopy.id, toCopy.value);
    }

    public IdStringPair(Parcel in) {
        id = in.readLong();
        value = in.readString();
    }

    @Override
     public void writeToParcel(Parcel out, int flags) {
        out.writeLong(id);
        out.writeString(value);
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<IdStringPair> CREATOR = new Parcelable.Creator<IdStringPair>() {
        public IdStringPair createFromParcel(Parcel in) {
            return new IdStringPair(in);
        }

        public IdStringPair[] newArray(int size) {
            return new IdStringPair[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IdStringPair)) return false;

        IdStringPair that = (IdStringPair) o;

        if (id != that.id) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(IdStringPair another) {
        int comp = this.value.compareTo(another.value);
        return comp != 0 ? comp : Longs.compare(this.id, another.id);
    }

    @Override
    public String toString() {
        return Long.toString(id) + " : " + value;
    }

    public static List<String> valueList(List<IdStringPair> idStringPairs) {
        List<String> values = Lists.newArrayListWithCapacity(idStringPairs.size());
        for (IdStringPair pair : idStringPairs) {
            values.add(pair.getValue());
        }
        return values;
    }

    public static String[] valueArray(List<IdStringPair> idStringPairs) {
        String[] values = new String[idStringPairs.size()];
        for (int i = 0; i < idStringPairs.size(); i++) {
            values[i] = idStringPairs.get(i).getValue();
        }
        return values;
    }
}
