package com.lateensoft.pathfinder.toolkit.db.repository;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;

import android.content.ContentValues;
import android.database.Cursor;

import com.lateensoft.pathfinder.toolkit.db.repository.TableAttribute.SQLDataType;
import com.lateensoft.pathfinder.toolkit.model.party.CampaignParty;
import com.lateensoft.pathfinder.toolkit.model.party.PartyMember;

public class PartyRepository extends BaseRepository<CampaignParty> {
	private static final String TABLE = "Party";
	private static final String PARTY_ID = "party_id";
	private static final String NAME = "Name";
	private static final String IN_ENCOUNTER = "InEncounter";
	
	public PartyRepository() {
		super();
		TableAttribute id = new TableAttribute(PARTY_ID, SQLDataType.INTEGER, true);
		TableAttribute name = new TableAttribute(NAME, SQLDataType.TEXT);
		TableAttribute isInEncounter = new TableAttribute(IN_ENCOUNTER, SQLDataType.INTEGER);
		TableAttribute[] columns = {id, name, isInEncounter};
		m_tableInfo = new TableInfo(TABLE, columns);
	}
	
	/**
	 * Inserts the character, and all subcomponents into database
	 * 
	 * @return the id of the character inserted, or -1 if failure occurred.
	 */
	public long insert(CampaignParty object, boolean isEncounterParty) {
		ContentValues values = getContentValues(object);
		values.put(IN_ENCOUNTER, isEncounterParty);
		
		String table = m_tableInfo.getTable();
		long id = getDatabase().insert(table, values);
		if (id != -1 && !isIDSet(object)) {
			object.setID(id);
		}
		
		long subCompId = 0;
		
		if (id != -1) {
			// Sets all party ids of members
			object.setID(id);
			
			// PartyMembers
			PartyMemberRepository memberRepo = new PartyMemberRepository();
			for(int i = 0; i < object.size(); i++) {
				subCompId = memberRepo.insert(object.getPartyMember(i));
				if (subCompId == -1) {
					delete(id);
					return subCompId;
				}
			}
		}
		return id;
	}
	
	/**
	 * Same as insert(object, false)
	 */
	@Override
	public long insert(CampaignParty object) {
		return insert(object, false);
	}

	@Override
	protected CampaignParty buildFromHashTable(Hashtable<String, Object> hashTable) {
		long id = (Long) hashTable.get(PARTY_ID);
		String name = (String) hashTable.get(NAME);
			
		// PartyMembers
		PartyMemberRepository memberRepo = new PartyMemberRepository();
		PartyMember[] members = memberRepo.querySet(id);
		
		CampaignParty party = new CampaignParty(id, name, members);
		return party;
	}

	@Override
	protected ContentValues getContentValues(CampaignParty object) {
		ContentValues values = new ContentValues();
		if (isIDSet(object)) { 
			values.put(PARTY_ID, object.getID());
		}
		values.put(NAME, object.getName());
		return values;
	}
	
	/**
	 * Returns all parties
	 * @return Array of IdNamePair, ordered alphabetically by name
	 */
	public List<Entry<Long, String>> queryList() {
		String selector = IN_ENCOUNTER + "=0";
		String orderBy = NAME + " ASC";
		String table = m_tableInfo.getTable();
		String[] columns = m_tableInfo.getColumns();
		Cursor cursor = getDatabase().query(true, table, columns, selector, 
				null, null, null, orderBy, null);
		
		ArrayList<Entry<Long, String>> members = new ArrayList<Entry<Long, String>>(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Hashtable<String, Object> hashTable =  getTableOfValues(cursor);
			members.add(new SimpleEntry<Long, String>((Long)hashTable.get(PARTY_ID), 
					(String)hashTable.get(NAME)));
			cursor.moveToNext();
		}
		return members;
	}
	
	/**
	 * Returns party which is marked as encounter party
	 * @return the encounter party or null if none is set
	 */
	public CampaignParty queryEncounterParty() {
		String selector = IN_ENCOUNTER + "<>0";
		String table = m_tableInfo.getTable();
		String[] columns = m_tableInfo.getColumns();
		Cursor cursor = getDatabase().query(true, table, columns, selector, 
				null, null, null, null, null);
		
		CampaignParty encounterParty = null;
		cursor.moveToFirst();
		if (!cursor.isAfterLast()) {
			Hashtable<String, Object> hashTable =  getTableOfValues(cursor);
			encounterParty = buildFromHashTable(hashTable);
		}
		return encounterParty;
	}

}
