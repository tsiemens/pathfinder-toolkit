package com.lateensoft.pathfinder.toolkit.model.character;

import com.lateensoft.pathfinder.toolkit.db.repository.Storable;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Representation for both Feats and Special  abilities
 * @author trevsiemens
 *
 */
public class Feat implements Parcelable, Storable, Comparable<Feat> {
	private String m_name;
	private String m_description;
	
	private long m_id;
	private long m_characterId;
	
	public Feat(){
		this(UNSET_ID, UNSET_ID, "", "");
	}
	
	public Feat(long characterId, String name, String description){
		this(UNSET_ID, characterId, name, description);
	}
	
	public Feat(long id, long characterId, String name, String description){
		m_id = id;
		m_characterId = characterId;
		m_name = name;
		m_description = description;
	}
	
	public Feat(Feat otherFeat){
		m_name = otherFeat.getName();
		m_description = otherFeat.getDescription();
		m_id = otherFeat.getID();
		m_characterId = otherFeat.getCharacterID();
	}
	
	public Feat(Parcel in) {
		m_name = in.readString();
		m_description = in.readString();
		m_id = in.readLong();
		m_characterId = in.readLong();
	}
	
	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(m_name);
		out.writeString(m_description);
		out.writeLong(m_id);
		out.writeLong(m_characterId);
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
	
	public void setCharacterID(long characterId) {
		m_characterId = characterId;
	}
	
	public long getCharacterID() {
		return m_characterId;
	}
	
	@Override
	public void setID(long id) {
		m_id = id;
	}

	@Override
	public long getID() {
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
}
