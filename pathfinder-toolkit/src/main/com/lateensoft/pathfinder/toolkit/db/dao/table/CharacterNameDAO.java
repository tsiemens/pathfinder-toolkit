package com.lateensoft.pathfinder.toolkit.db.dao.table;

import android.content.ContentValues;
import android.content.Context;
import com.google.common.base.Preconditions;
import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.model.IdNamePair;

import java.util.Hashtable;

public class CharacterNameDAO extends AbstractCharacterDAO<IdNamePair> {

    public CharacterNameDAO(Context context) {
        super(context);
    }

    @Override
    public Long add(IdNamePair idNamePair) throws DataAccessException {
        throw new UnsupportedOperationException("To add characters, use " + CharacterModelDAO.class.getSimpleName());
    }

    @Override
    protected ContentValues getContentValues(IdNamePair object) {
        ContentValues values = new ContentValues();
        values.put(CHARACTER_ID, object.getId());
        values.put(NAME, object.getName());
        return values;
    }

    @Override
    protected IdNamePair buildFromHashTable(Hashtable<String, Object> hashTable) {
        long id = (Long) hashTable.get(CHARACTER_ID);
        String name = (String) hashTable.get(NAME);
        return new IdNamePair(id, name);
    }
}
