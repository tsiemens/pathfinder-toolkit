package com.lateensoft.pathfinder.toolkit.db.repository;

import java.util.Hashtable;

import android.content.ContentValues;
import android.database.Cursor;

import com.lateensoft.pathfinder.toolkit.db.IdNamePair;
import com.lateensoft.pathfinder.toolkit.db.repository.PTTableAttribute.SQLDataType;
import com.lateensoft.pathfinder.toolkit.model.party.PTParty;
import com.lateensoft.pathfinder.toolkit.model.party.PTPartyMember;

public class PTPartyRepository extends PTBaseRepository<PTParty> {
	private static final String TABLE = "Party";
	private static final String PARTY_ID = "party_id";
	private static final String NAME = "Name";
	private static final String IN_ENCOUNTER = "InEncounter";
	
	public PTPartyRepository() {
		super();
		PTTableAttribute id = new PTTableAttribute(PARTY_ID, SQLDataType.INTEGER, true);
		PTTableAttribute name = new PTTableAttribute(NAME, SQLDataType.TEXT);
		PTTableAttribute isInEncounter = new PTTableAttribute(IN_ENCOUNTER, SQLDataType.INTEGER);
		PTTableAttribute[] columns = {id, name, isInEncounter};
		m_tableInfo = new PTTableInfo(TABLE, columns);
	}
	
	/**
	 * Inserts the character, and all subcomponents into database
	 * 
	 * @return the id of the character inserted, or -1 if failure occurred.
	 */
	public long insert(PTParty object, boolean isEncounterParty) {
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
			PTPartyMemberRepository memberRepo = new PTPartyMemberRepository();
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
	public long insert(PTParty object) {
		return insert(object, false);
	}

	@Override
	protected PTParty buildFromHashTable(Hashtable<String, Object> hashTable) {
		long id = (Long) hashTable.get(PARTY_ID);
		String name = (String) hashTable.get(NAME);
			
		// PartyMembers
		PTPartyMemberRepository memberRepo = new PTPartyMemberRepository();
		PTPartyMember[] members = memberRepo.querySet(id);
		
		PTParty party = new PTParty(id, name, members);
		return party;
	}

	@Override
	protected ContentValues getContentValues(PTParty object) {
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
	public IdNamePair[] queryList() {
		String selector = IN_ENCOUNTER + "=0";
		String orderBy = NAME + " ASC";
		String table = m_tableInfo.getTable();
		String[] columns = m_tableInfo.getColumns();
		Cursor cursor = getDatabase().query(true, table, columns, selector, 
				null, null, null, orderBy, null);
		
		IdNamePair[] members = new IdNamePair[cursor.getCount()];
		cursor.moveToFirst();
		int i = 0;
		while (!cursor.isAfterLast()) {
			Hashtable<String, Object> hashTable =  getTableOfValues(cursor);
			members[i] = new IdNamePair((Long)hashTable.get(PARTY_ID), 
					(String)hashTable.get(NAME));
			cursor.moveToNext();
			i++;
		}
		return members;
	}
	
	/**
	 * Returns party which is marked as encounter party
	 * @return the encounter party or null if none is set
	 */
	public PTParty queryEncounterParty() {
		String selector = IN_ENCOUNTER + "<>0";
		String table = m_tableInfo.getTable();
		String[] columns = m_tableInfo.getColumns();
		Cursor cursor = getDatabase().query(true, table, columns, selector, 
				null, null, null, null, null);
		
		PTParty encounterParty = null;
		cursor.moveToFirst();
		if (!cursor.isAfterLast()) {
			Hashtable<String, Object> hashTable =  getTableOfValues(cursor);
			encounterParty = buildFromHashTable(hashTable);
		}
		return encounterParty;
	}

}
