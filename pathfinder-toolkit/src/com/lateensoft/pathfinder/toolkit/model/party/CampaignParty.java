package com.lateensoft.pathfinder.toolkit.model.party;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.db.dao.Identifiable;

import android.os.Parcel;
import android.os.Parcelable;
import org.jetbrains.annotations.NotNull;

@Deprecated
public class CampaignParty extends ArrayList<PartyMember> implements Parcelable, Identifiable {
	@SuppressWarnings("unused")
	private final String TAG = CampaignParty.class.getSimpleName();

	private String m_partyName;
	private long m_id;

    public CampaignParty(String name) {
        this(UNSET_ID, name);
    }

    public CampaignParty(long id, String name) {
        super();
        m_id = id;
        m_partyName = (name != null) ? name : "";
    }
	
	public CampaignParty(long id, String name, @NotNull Collection<PartyMember> partyMembers) {
        super(partyMembers);
		m_id = id;
		m_partyName = (name != null) ? name : "";
	}
	
	public CampaignParty(Parcel in) {
        in.readTypedList(this, PartyMember.CREATOR);
		m_partyName = in.readString();
		m_id = in.readLong();
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
        out.writeTypedList(this);
		out.writeString(m_partyName);
		out.writeLong(m_id);
	}
	
	/**
	 * @return an array of all the party members' names in the currently sorted order
	 */
	public List<String> getPartyMemberNames(){
		List<String> names = Lists.newArrayListWithCapacity(this.size());
		for (PartyMember member : this) {
            names.add(member.getName());
        }
		return names;
	}
	
	public void setName(@NotNull String name){
        Preconditions.checkNotNull(name);
        m_partyName = name;
	}

	public String getName(){
		return m_partyName;
	}

	/**
	 * Sets ID and IDs of all members
	 */
	@Override
	public void setId(long id) {
		m_id = id;
		for (PartyMember member : this) {
			member.setPartyID(id);
		}
	}

	@Override
	public long getId() {
		return m_id;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	public static final Parcelable.Creator<CampaignParty> CREATOR = new Parcelable.Creator<CampaignParty>() {
		public CampaignParty createFromParcel(Parcel in) {
			return new CampaignParty(in);
		}
		
		public CampaignParty[] newArray(int size) {
			return new CampaignParty[size];
		}
	};
}
