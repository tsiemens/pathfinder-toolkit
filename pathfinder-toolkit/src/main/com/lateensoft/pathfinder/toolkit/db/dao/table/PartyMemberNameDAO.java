package com.lateensoft.pathfinder.toolkit.db.dao.table;

import android.content.ContentValues;
import android.content.Context;
import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.db.dao.OwnedObject;
import com.lateensoft.pathfinder.toolkit.model.IdNamePair;
import org.jetbrains.annotations.Nullable;

import java.util.Hashtable;
import java.util.List;

public class PartyMemberNameDAO extends AbstractPartyMembershipDAO<IdNamePair> {

    private CharacterNameDAO characterDao;

    public PartyMemberNameDAO(Context context) {
        super(context);
        characterDao = new CharacterNameDAO(context);
    }

    @Override
    @Nullable
    protected String getBaseSelector() {
        return String.format("%s.%s=%s.%s",
                TABLE, CHARACTER_ID, CharacterModelDAO.TABLE, CharacterModelDAO.CHARACTER_ID);
    }

    @Override
    protected String[] getColumnsForQuery() {
        return getTable().union(characterDao.getTable(), CHARACTER_ID, CharacterModelDAO.CHARACTER_ID);
    }

    @Override
    protected List<String> getTablesForQuery() {
        return Lists.newArrayList(TABLE, CharacterModelDAO.TABLE);
    }

    @Override
    protected OwnedObject<Long, Long> getIdFromRowData(OwnedObject<Long, IdNamePair> rowData) {
        return OwnedObject.of(rowData.getOwnerId(), rowData.getObject().getId());
    }

    @Override
    protected ContentValues getContentValues(OwnedObject<Long, IdNamePair> rowData) {
        ContentValues values = new ContentValues();
        values.put(PARTY_ID, rowData.getOwnerId());
        values.put(CHARACTER_ID, rowData.getObject().getId());

        return values;
    }

    @Override
    protected IdNamePair buildFromHashTable(Hashtable<String, Object> hashTable) {
        long characterId = ((Long) hashTable.get(CHARACTER_ID));
        String name = (String) hashTable.get(AbstractCharacterDAO.NAME);

        return new IdNamePair(characterId, name);
    }
}
