package com.lateensoft.pathfinder.toolkit.db.dao.table;

import android.content.ContentValues;
import android.content.Context;
import com.lateensoft.pathfinder.toolkit.db.dao.OwnedObject;
import com.lateensoft.pathfinder.toolkit.model.IdStringPair;

import java.util.Hashtable;

public class PartyMemberNameDAO extends AbstractPartyMembershipDAO<IdStringPair> {

    private CharacterNameDAO characterDao;

    public PartyMemberNameDAO(Context context) {
        super(context);
        characterDao = new CharacterNameDAO(context);
    }

    @Override
    protected AbstractCharacterDAO getCharacterDAO() {
        return characterDao;
    }

    @Override
    protected OwnedObject<Long, Long> getIdFromRowData(OwnedObject<Long, IdStringPair> rowData) {
        return OwnedObject.of(rowData.getOwnerId(), rowData.getObject().getId());
    }

    @Override
    protected ContentValues getContentValues(OwnedObject<Long, IdStringPair> rowData) {
        ContentValues values = new ContentValues();
        values.put(PARTY_ID, rowData.getOwnerId());
        values.put(CHARACTER_ID, rowData.getObject().getId());

        return values;
    }

    @Override
    protected IdStringPair buildFromHashTable(Hashtable<String, Object> hashTable) {
        long characterId = ((Long) hashTable.get(CHARACTER_ID)).intValue();
        String name = (String) hashTable.get(AbstractCharacterDAO.NAME);

        return new IdStringPair(characterId, name);
    }
}
