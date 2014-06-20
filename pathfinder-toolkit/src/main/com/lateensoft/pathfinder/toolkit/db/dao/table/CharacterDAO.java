package com.lateensoft.pathfinder.toolkit.db.dao.table;

import android.content.Context;
import com.lateensoft.pathfinder.toolkit.db.dao.RootIdentifiableTableDAO;
import com.lateensoft.pathfinder.toolkit.model.character.PathfinderCharacter;

public abstract class CharacterDAO extends RootIdentifiableTableDAO<PathfinderCharacter> {
    public CharacterDAO(Context context) {
        super(context);
    }
    //TODO
}
