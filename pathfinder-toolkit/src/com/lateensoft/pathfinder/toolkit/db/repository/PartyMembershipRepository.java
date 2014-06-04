package com.lateensoft.pathfinder.toolkit.db.repository;

import android.content.ContentValues;
import android.database.Cursor;
import com.google.common.collect.Lists;

import java.util.Hashtable;
import java.util.List;

/**
 * @author tsiemens
 */
public class PartyMembershipRepository extends BaseRepository<PartyMembershipRepository.Membership> {

    private static final String TABLE = "PartyMembership";
    private static final String PARTY_ID = "party_id";

    public static class Membership implements Storable {
        public long partyId;
        public long characterId;

        public Membership(long partyId, long characterId) {
            this.partyId = partyId;
            this.characterId = characterId;
        }

        @Override public void setID(long id) { partyId = id; }

        @Override public long getID() { return partyId; }
    }

    public PartyMembershipRepository() {
        super();
        TableAttribute party_id = new TableAttribute(PARTY_ID, TableAttribute.SQLDataType.INTEGER, true);
        TableAttribute character_id = new TableAttribute(CHARACTER_ID, TableAttribute.SQLDataType.INTEGER);
        TableAttribute[] columns = {party_id, character_id};
        m_tableInfo = new TableInfo(TABLE, columns);
    }

    @Override
    protected Membership buildFromHashTable(Hashtable<String, Object> hashTable) {
        long partyId = (Long) hashTable.get(PARTY_ID);
        long characterId = (Long) hashTable.get(CHARACTER_ID);
        return new Membership(partyId, characterId);
    }

    @Override
    protected ContentValues getContentValues(Membership object) {
        ContentValues values = new ContentValues();
        values.put(PARTY_ID, object.getID());
        values.put(CHARACTER_ID, object.characterId);
        return values;
    }

    /**
     * Return selector for membership.
     * @param ids must be { party id, character id }
     */
    @Override
    protected String getSelector(long ... ids) {
        if (ids.length >= 2) {
            return PARTY_ID + "=" + ids[0] + " AND " +
                    CHARACTER_ID + "=" + ids[1];
        } else {
            throw new IllegalArgumentException("membership but be between a party and character");
        }
    }

    @Override
    protected String getSelector(Membership object) {
        return getSelector(object.getID(), object.characterId);
    }

    public List<Long> queryCharactersInParty(long partyId) {
        String selector = PARTY_ID + "=" + partyId;
        String orderBy = CHARACTER_ID + " ASC";
        String table = TABLE;
        String[] columns = new String[] {CHARACTER_ID};
        Cursor cursor = getDatabase().query(true, table, columns, selector,
                null, null, null, orderBy, null);

        return getLongsFromCursor(cursor, CHARACTER_ID);
    }

    public List<Long> queryPartiesForCharacter(long characterId) {
        String selector = CHARACTER_ID + "=" + characterId;
        String orderBy = PARTY_ID + " ASC";
        String table = TABLE;
        String[] columns = new String[] {PARTY_ID};
        Cursor cursor = getDatabase().query(true, table, columns, selector,
                null, null, null, orderBy, null);

        return getLongsFromCursor(cursor, PARTY_ID);
    }

    private static List<Long> getLongsFromCursor(Cursor cursor, String column) {
        List<Long> vals = Lists.newArrayListWithCapacity(cursor.getCount());
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Hashtable<String, Object> hashTable =  getTableOfValues(cursor);
            vals.add((Long) hashTable.get(column));
            cursor.moveToNext();
        }
        return vals;
    }
}
