package com.lateensoft.pathfinder.toolkit.db.dao.table;

import android.content.Context;
import com.lateensoft.pathfinder.toolkit.dao.Identifiable;
import com.lateensoft.pathfinder.toolkit.db.dao.RootIdentifiableTableDAO;
import com.lateensoft.pathfinder.toolkit.db.dao.Table;

public abstract class AbstractCharacterDAO<T extends Identifiable> extends RootIdentifiableTableDAO<T> {
    public static final String TABLE = "Character";

    protected static final String CHARACTER_ID = "character_id";
    protected static final String NAME = "Name";
    protected static final String GOLD = "Gold";

    public AbstractCharacterDAO(Context context) {
        super(context);
    }

    @Override
    protected Table initTable() {
        return new Table(TABLE, CHARACTER_ID, GOLD);
    }

    @Override
    protected String getIdSelector(Long id) {
        return CHARACTER_ID + "=" + id;
    }
}
