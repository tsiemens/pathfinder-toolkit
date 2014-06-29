package com.lateensoft.pathfinder.toolkit.db.dao.table;

import android.content.ContentValues;
import android.content.Context;
import com.google.common.base.Preconditions;
import com.lateensoft.pathfinder.toolkit.model.IdStringPair;

import java.util.Hashtable;

public class CharacterNameDAO extends AbstractCharacterDAO<IdStringPair> {

    public CharacterNameDAO(Context context) {
        super(context);
    }

    @Override
    protected ContentValues getContentValues(IdStringPair object) {
        Preconditions.checkArgument(isIdSet(object), "To add characters, use " + CharacterModelDAO.class.getSimpleName());
        ContentValues values = new ContentValues();
        values.put(CHARACTER_ID, object.getId());
        values.put(NAME, object.getValue());
        return values;
    }

    @Override
    protected IdStringPair buildFromHashTable(Hashtable<String, Object> hashTable) {
        long id = (Long) hashTable.get(CHARACTER_ID);
        String name = (String) hashTable.get(NAME);
        return new IdStringPair(id, name);
    }


}
