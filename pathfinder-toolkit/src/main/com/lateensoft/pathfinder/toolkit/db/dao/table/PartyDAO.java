package com.lateensoft.pathfinder.toolkit.db.dao.table;

import android.content.ContentValues;
import android.content.Context;
import com.lateensoft.pathfinder.toolkit.dao.OwnedGenericDAO;
import com.lateensoft.pathfinder.toolkit.db.dao.Table;
import com.lateensoft.pathfinder.toolkit.model.IdNamePair;
import com.lateensoft.pathfinder.toolkit.model.NamedList;

import java.util.Hashtable;
import java.util.List;

public class PartyDAO<T> extends ListBasedDAO<NamedList<T>, T, IdNamePair> {
    private static final String TABLE = "Party";

    private static final String PARTY_ID = "party_id";
    private static final String NAME = "Name";

    private AbstractPartyMembershipDAO memberDao;
    private OwnedGenericDAO<Long, ?, T> itemDao;

    public PartyDAO(Context context, OwnedGenericDAO<Long, ?, T> itemDao) {
        super(context);
        memberDao = new PartyMemberIdDAO(context);
        this.itemDao = itemDao;
    }

    @Override
    protected Table initTable() {
        return new Table(TABLE, PARTY_ID, NAME);
    }

    @Override
    protected String getIdSelector(Long id) {
        return PARTY_ID + "=" + id;
    }

    @Override
    protected ContentValues getContentValues(IdNamePair party) {
        ContentValues values = new ContentValues();
        if (isIdSet(party)) {
            values.put(PARTY_ID, party.getId());
        }
        values.put(NAME, party.getName());
        return values;
    }

    @Override
    protected IdNamePair buildFromHashTable(Hashtable<String, Object> hashTable) {
        long id = (Long) hashTable.get(PARTY_ID);
        String name = (String) hashTable.get(NAME);

        return new IdNamePair(id, name);
    }

    @Override
    protected IdNamePair getListFields(NamedList<T> list) {
        return list.idNamePair();
    }

    @Override
    protected OwnedGenericDAO<Long, ?, T> getItemDao() {
        return itemDao;
    }

    @Override
    protected NamedList<T> buildList(IdNamePair idNamePair, List<T> items) {
        return new NamedList<T>(idNamePair, items);
    }

    @Override
    protected String getDefaultOrderBy() {
        return NAME + " ASC";
    }

    public List<IdNamePair> findAllWithMember(Long characterId) {
        String tables = TABLE + ", " + AbstractPartyMembershipDAO.TABLE;
        String[] columns = getTable().union(memberDao.getTable(), PARTY_ID, AbstractPartyMembershipDAO.PARTY_ID);

        String partyMatchSelector = String.format("%s.%s=%s.%s", TABLE, PARTY_ID,
                AbstractPartyMembershipDAO.TABLE, AbstractPartyMembershipDAO.PARTY_ID);
        String characterSelector = AbstractPartyMembershipDAO.CHARACTER_ID + "=" + characterId;
        String selector = andSelectors(partyMatchSelector, characterSelector);

        return findFiltered(tables, columns, selector, null);
    }
}
