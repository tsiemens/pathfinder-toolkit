package com.lateensoft.pathfinder.toolkit.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.common.primitives.Longs;

/**
 * @author tsiemens
 */
public class IdStringPair implements Parcelable, Comparable<IdStringPair> {

    private long m_id;
    private String m_value;

    public IdStringPair(long id, String value) {
        m_id = id;
        m_value = value;
    }

    public IdStringPair(IdStringPair toCopy) {
        this(toCopy.m_id, toCopy.m_value);
    }

    public IdStringPair(Parcel in) {
        m_id = in.readLong();
        m_value = in.readString();
    }

    @Override
     public void writeToParcel(Parcel out, int flags) {
        out.writeLong(m_id);
        out.writeString(m_value);
    }

    public long getId() {
        return m_id;
    }

    public void setId(long id) {
        m_id = id;
    }

    public String getValue() {
        return m_value;
    }

    public void setValue(String value) {
        m_value = value;
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

        if (m_id != that.m_id) return false;
        if (m_value != null ? !m_value.equals(that.m_value) : that.m_value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (m_id ^ (m_id >>> 32));
        result = 31 * result + (m_value != null ? m_value.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(IdStringPair another) {
        int comp = this.m_value.compareTo(another.m_value);
        return comp != 0 ? comp : Longs.compare(this.m_id, another.m_id);
    }

    @Override
    public String toString() {
        return Long.toString(m_id) + " : " + m_value;
    }
}
