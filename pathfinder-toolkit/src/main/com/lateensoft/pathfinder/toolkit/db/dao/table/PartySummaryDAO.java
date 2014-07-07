package com.lateensoft.pathfinder.toolkit.db.dao.table;

import android.content.Context;
import com.lateensoft.pathfinder.toolkit.dao.OwnedWeakGenericDAO;
import com.lateensoft.pathfinder.toolkit.model.IdStringPair;

public class PartySummaryDAO extends AbstractPartyDAO<IdStringPair> {

    private PartyMemberNameDAO memberDao;

    public PartySummaryDAO(Context context) {
        super(context);
        memberDao = new PartyMemberNameDAO(context);
    }

    @Override
    protected OwnedWeakGenericDAO<Long, ?, IdStringPair> getElementDAO() {
        return memberDao;
    }
}
