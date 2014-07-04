package com.lateensoft.pathfinder.toolkit.db.dao.table;

import android.content.ContentValues;
import android.content.Context;
import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.db.dao.RootIdentifiableTableDAO;
import com.lateensoft.pathfinder.toolkit.db.dao.Table;
import com.lateensoft.pathfinder.toolkit.model.NamedList;
import com.lateensoft.pathfinder.toolkit.model.party.EncounterParticipant;

import java.util.Hashtable;
import java.util.List;

public class EncounterDAO extends RootIdentifiableTableDAO<NamedList<EncounterParticipant>> {
    private static final String TABLE = "Encounter";

    private static final String ENCOUNTER_ID = "encounter_id";
    private static final String NAME = "Name";

    private EncounterParticipantDAO participantDAO;

    public EncounterDAO(Context context) {
        super(context);
        participantDAO = new EncounterParticipantDAO(context);
    }

    @Override
    protected Table initTable() {
        return new Table(TABLE, ENCOUNTER_ID, NAME);
    }

    @Override
    protected String getIdSelector(Long id) {
        return ENCOUNTER_ID + "=" + id;
    }

    @Override
    public Long add(NamedList<EncounterParticipant> encounterParticipants) throws DataAccessException {
        long id = -1;
        try {
            beginTransaction();
            id = super.add(encounterParticipants);
            for (EncounterParticipant participant : encounterParticipants) {
                participantDAO.add(id, participant);
            }
            setTransactionSuccessful();
        } finally {
            endTransaction();
        }

        return id;
    }

    @Override
    protected ContentValues getContentValues(NamedList<EncounterParticipant> object) {
        ContentValues values = new ContentValues();
        if (isIdSet(object)) {
            values.put(ENCOUNTER_ID, object.getId());
        }
        values.put(NAME, object.getName());
        return values;
    }

    @Override
    protected NamedList<EncounterParticipant> buildFromHashTable(Hashtable<String, Object> hashTable) {
        long id = (Long) hashTable.get(ENCOUNTER_ID);
        String name = (String) hashTable.get(NAME);

        List<EncounterParticipant> participants = participantDAO.findAllForOwner(id);

        return new NamedList<EncounterParticipant>(id, name, participants);
    }

    @Override
    protected String getDefaultOrderBy() {
        return NAME + " ASC";
    }
}
