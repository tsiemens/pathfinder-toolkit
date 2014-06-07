package com.lateensoft.pathfinder.toolkit.db.repository;

import android.content.ContentValues;
import android.database.Cursor;
import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.db.QueryUtils;
import com.lateensoft.pathfinder.toolkit.db.repository.TableAttribute.SQLDataType;
import com.lateensoft.pathfinder.toolkit.model.IdStringPair;
import com.lateensoft.pathfinder.toolkit.model.NamedList;
import com.lateensoft.pathfinder.toolkit.model.party.CampaignParty;

import java.util.Hashtable;
import java.util.List;

import static com.lateensoft.pathfinder.toolkit.db.repository.PartyMembershipRepository.Membership;

public abstract class AbstractPartyRepository<T> extends BaseRepository<NamedList<T>> {
	private static final String TABLE = "Party";
	private static final String PARTY_ID = "party_id";
	private static final String NAME = "Name";

    final PartyMembershipRepository m_membersRepo = new PartyMembershipRepository();

	public AbstractPartyRepository() {
		super();
		TableAttribute id = new TableAttribute(PARTY_ID, SQLDataType.INTEGER, true);
		TableAttribute name = new TableAttribute(NAME, SQLDataType.TEXT);
		TableAttribute[] columns = {id, name};
		m_tableInfo = new TableInfo(TABLE, columns);
	}
	
	/**
	 * Inserts the character, and all subcomponents into database
	 * 
	 * @return the id of the character inserted, or -1 if failure occurred.
	 */
    @Override
	public long insert(NamedList<T> party) {
		ContentValues values = getContentValues(party);

		String table = m_tableInfo.getTable();
		long id = getDatabase().insert(table, values);
		if (id != -1 && !isIDSet(party)) {
			party.setID(id);
		}

        if (id != -1) {
            party.setID(id);

            if (insertMembers(party) == -1) {
                delete(id);
                return -1;
            }
        }
		return id;
	}

    /**
     * Inserts members into their database.
     * @return -1 if failed to add all members, 0 otherwise.
     */
    protected abstract long insertMembers(NamedList<T> party);

	@Override
	protected NamedList<T> buildFromHashTable(Hashtable<String, Object> hashTable) {
		long id = (Long) hashTable.get(PARTY_ID);
		String name = (String) hashTable.get(NAME);
			
		List<T> members = queryMembers(id);
		return new NamedList<T>(id, name, members);
	}

    protected abstract List<T> queryMembers(long partyId);

	@Override
	protected ContentValues getContentValues(NamedList<T> object) {
		ContentValues values = new ContentValues();
		if (isIDSet(object)) { 
			values.put(PARTY_ID, object.getID());
		}
		values.put(NAME, object.getName());
		return values;
	}

    public PartyMembershipRepository getMembersRepo() { return m_membersRepo; }

	/** @return ids and names of all parties **/
	public List<IdStringPair> queryIdNameList() {
		return queryFilteredIdNameList(null);
	}

    public List<IdStringPair> queryFilteredIdNameList(String selector) {
        String orderBy = NAME + " ASC";
        String table = m_tableInfo.getTable();
        String[] columns = m_tableInfo.getColumns();
        Cursor cursor = getDatabase().query(true, table, columns, selector,
                null, null, null, orderBy, null);

        List<IdStringPair> members = Lists.newArrayListWithCapacity(cursor.getCount());
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Hashtable<String, Object> hashTable =  getTableOfValues(cursor);
            members.add(new IdStringPair((Long)hashTable.get(PARTY_ID),
                    (String)hashTable.get(NAME)));
            cursor.moveToNext();
        }
        return members;
    }
	
	@Deprecated
	public CampaignParty queryEncounterParty() {
		throw new UnsupportedOperationException();
	}

    public int removeCharactersFromParty(long partyId, List<Long> characterIds) {
        int numRemoved = 0;
        for (Long characterId : characterIds) {
            numRemoved += m_membersRepo.delete(partyId, characterId);
        }

        return numRemoved;
    }

    public void addCharactersToParty(long partyId, List<Long> characterIds) {
        for (Long characterId : characterIds) {
            m_membersRepo.insert(new Membership(partyId, characterId));
        }
    }

    public List<IdStringPair> queryPartyNamesForCharacter(long characterId) {
        List<Long> partyIds = m_membersRepo.queryPartiesForCharacter(characterId);
        String selector = QueryUtils.selectorForAll(PARTY_ID, partyIds);
        return queryFilteredIdNameList(selector);
    }

    // TODO this should be just query()
    public List<IdStringPair> queryCharacterNamesForParty(long partyId) {
        List<Long> charactersInParty = m_membersRepo.queryCharactersInParty(partyId);

        CharacterRepository charRepo = new CharacterRepository();
        String characterIdCol = CharacterRepository.TABLE + "." + CharacterRepository.CHARACTER_ID;
        String selector = QueryUtils.selectorForAll(characterIdCol, charactersInParty);
        return charRepo.queryFilteredIdNameList(selector);
    }
}
