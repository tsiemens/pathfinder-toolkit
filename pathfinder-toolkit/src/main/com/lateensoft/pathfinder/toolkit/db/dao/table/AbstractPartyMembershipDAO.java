package com.lateensoft.pathfinder.toolkit.db.dao.table;

import android.content.Context;
import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.db.dao.OwnedObject;
import com.lateensoft.pathfinder.toolkit.db.dao.OwnedWeakTableDAO;
import com.lateensoft.pathfinder.toolkit.db.dao.Table;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class AbstractPartyMembershipDAO<Member> extends OwnedWeakTableDAO<Long, Long, Member> {
    private static final String TABLE = "PartyMembership";

    protected static final String PARTY_ID = "party_id";
    protected static final String CHARACTER_ID = "character_id";

    public AbstractPartyMembershipDAO(Context context) {
        super(context);
    }

    @Override
    protected Table initTable() {
        return new Table(TABLE, PARTY_ID, CHARACTER_ID);
    }

    @Override
    protected String getOwnerIdSelector(Long partyId) {
        return PARTY_ID + "=" + partyId;
    }

    @Override
    protected String getIdSelector(OwnedObject<Long, Long> rowId) {
        return andSelectors(getOwnerIdSelector(rowId.getOwnerId()),
                TABLE + "." + CHARACTER_ID + "=" + rowId.getObject());
    }

    @Override
    @Nullable
    protected String getBaseSelector() {
        return String.format("%s.%s=%s.%s",
                TABLE, CHARACTER_ID, CharacterModelDAO.TABLE, CharacterModelDAO.CHARACTER_ID);
    }

    @Override
    protected List<String> getTablesForQuery() {
        return Lists.newArrayList(TABLE, CharacterModelDAO.TABLE);
    }

    @Override
    protected String[] getColumnsForQuery() {
        return getTable().union(getCharacterDAO().getTable(), CHARACTER_ID, CharacterModelDAO.CHARACTER_ID);
    }

    protected abstract AbstractCharacterDAO getCharacterDAO();
}