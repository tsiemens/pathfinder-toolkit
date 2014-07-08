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
public class IdNamePair implements Parcelable, Comparable<IdNamePair>, Identifiable {

    private long id;
    private String name;

    public IdNamePair(String name) {
        this(UNSET_ID, name);
    }

    public IdNamePair(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public IdNamePair(IdNamePair toCopy) {
        this(toCopy.id, toCopy.name);
    }

    public IdNamePair(Parcel in) {
        id = in.readLong();
        name = in.readString();
    }

    @Override
     public void writeToParcel(Parcel out, int flags) {
        out.writeLong(id);
        out.writeString(name);
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
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

    public static final Parcelable.Creator<IdNamePair> CREATOR = new Parcelable.Creator<IdNamePair>() {
        public IdNamePair createFromParcel(Parcel in) {
            return new IdNamePair(in);
        }

        public IdNamePair[] newArray(int size) {
            return new IdNamePair[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IdNamePair)) return false;

        IdNamePair that = (IdNamePair) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(IdNamePair another) {
        int comp = this.name.compareTo(another.name);
        return comp != 0 ? comp : Longs.compare(this.id, another.id);
    }

    @Override
    public String toString() {
        return Long.toString(id) + " : " + name;
    }

    public static List<String> nameList(List<IdNamePair> idNamePairs) {
        List<String> values = Lists.newArrayListWithCapacity(idNamePairs.size());
        for (IdNamePair pair : idNamePairs) {
            values.add(pair.getName());
        }
        return values;
    }

    public static String[] nameArray(List<IdNamePair> idNamePairs) {
        String[] values = new String[idNamePairs.size()];
        for (int i = 0; i < idNamePairs.size(); i++) {
            values[i] = idNamePairs.get(i).getName();
        }
        return values;
    }
}
