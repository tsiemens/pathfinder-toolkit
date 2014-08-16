package com.lateensoft.pathfinder.toolkit.model.character;

import com.lateensoft.pathfinder.toolkit.dao.Identifiable;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Representation for both Feats and Special  abilities
 * @author trevsiemens
 *
 */
public class Feat implements Parcelable, Identifiable, Comparable<Feat> {
    private String m_name;
    private String m_description;
    
    private long m_id;
    
    public Feat(){
        this("", "");
    }

    public Feat(String name, String description){
        this(UNSET_ID, name, description);
    }

    public Feat(long id, String name, String description){
        m_id = id;
        m_name = name;
        m_description = description;
    }
    
    public Feat(Feat otherFeat){
        m_name = otherFeat.getName();
        m_description = otherFeat.getDescription();
        m_id = otherFeat.getId();
    }
    
    public Feat(Parcel in) {
        m_name = in.readString();
        m_description = in.readString();
        m_id = in.readLong();
    }
    
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(m_name);
        out.writeString(m_description);
        out.writeLong(m_id);
    }
    
    public void setName(String name){
        if(name != null){
            m_name = name;
        }
    }
    
    public String getName(){
        return m_name;
    }
    
    public void setDescription(String description){
        if(description != null){
            m_description = description;
        }
    }
    
    public String getDescription(){
        return m_description;
    }

    @Override
    public void setId(long id) {
        m_id = id;
    }

    @Override
    public long getId() {
        return m_id;
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    public static final Parcelable.Creator<Feat> CREATOR = new Parcelable.Creator<Feat>() {
        public Feat createFromParcel(Parcel in) {
            return new Feat(in);
        }
        
        public Feat[] newArray(int size) {
            return new Feat[size];
        }
    };

    @Override
    public int compareTo(Feat another) {
        return this.getName().compareToIgnoreCase(another.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Feat)) return false;

        Feat feat = (Feat) o;

        if (m_id != feat.m_id) return false;
        if (m_description != null ? !m_description.equals(feat.m_description) : feat.m_description != null)
            return false;
        if (m_name != null ? !m_name.equals(feat.m_name) : feat.m_name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = m_name != null ? m_name.hashCode() : 0;
        result = 31 * result + (m_description != null ? m_description.hashCode() : 0);
        result = 31 * result + (int) (m_id ^ (m_id >>> 32));
        return result;
    }
}
