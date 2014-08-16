package com.lateensoft.pathfinder.toolkit.db.dao.table;

import android.content.ContentValues;
import android.content.Context;
import com.lateensoft.pathfinder.toolkit.dao.Identifiable;
import com.lateensoft.pathfinder.toolkit.dao.OwnedGenericDAO;
import com.lateensoft.pathfinder.toolkit.dao.OwnedWeakGenericDAO;
import com.lateensoft.pathfinder.toolkit.db.dao.Table;
import com.lateensoft.pathfinder.toolkit.model.IdNamePair;
import com.lateensoft.pathfinder.toolkit.model.party.Encounter;

import java.util.Hashtable;
import java.util.List;

public class EncounterDAO<P extends Identifiable> extends ListBasedDAO<Encounter<P>, P, EncounterDAO.EncounterFields<P>> {
    private static final String TABLE = "Encounter";

    private static final String ENCOUNTER_ID = "encounter_id";
    private static final String NAME = "Name";
    private static final String IS_IN_COMBAT = "IsInCombat";
    private static final String CURRENT_TURN_PARTICIPANT_ID = "CurrentTurnParticipantId";

    public final OwnedWeakGenericDAO<Long, Long, P> participantDao;

    public EncounterDAO(Context context, OwnedWeakGenericDAO<Long, Long, P> participantDao) {
        super(context);
        this.participantDao = participantDao;
    }

    @Override
    protected Table initTable() {
        return new Table(TABLE, ENCOUNTER_ID, NAME,
                IS_IN_COMBAT, CURRENT_TURN_PARTICIPANT_ID
        );
    }

    @Override
    protected String getIdSelector(Long id) {
        return ENCOUNTER_ID + "=" + id;
    }

    @Override
    protected EncounterFields<P> getListFields(Encounter<P> list) {
        return new EncounterFields<P>(list);
    }

    @Override
    protected OwnedGenericDAO<Long, ?, P> getItemDao() {
        return participantDao;
    }

    @Override
    protected Encounter<P> buildList(EncounterFields<P> encounterFields, List<P> items) {
        Encounter<P> encounter = new Encounter<P>(encounterFields.getId(), encounterFields.getName(), items);
        encounter.setInCombat(encounterFields.isInCombat);
        encounter.setCurrentTurn(encounterFields.currentTurn);
        return encounter;
    }

    @Override
    protected ContentValues getContentValues(EncounterFields<P> object) {
        ContentValues values = new ContentValues();
        if (isIdSet(object)) {
            values.put(ENCOUNTER_ID, object.getId());
        }
        values.put(NAME, object.getName());
        values.put(IS_IN_COMBAT, object.isInCombat);
        values.put(CURRENT_TURN_PARTICIPANT_ID, object.currentTurn != null ? object.currentTurn.getId() : null);
        return values;
    }

    @Override
    protected EncounterFields<P> buildFromHashTable(Hashtable<String, Object> hashTable) {
        long id = (Long) hashTable.get(ENCOUNTER_ID);
        String name = (String) hashTable.get(NAME);
        boolean isInCombat = ((Long) hashTable.get(IS_IN_COMBAT)).intValue() == 1;
        Long currentTurnParticipantId = (Long) hashTable.get(CURRENT_TURN_PARTICIPANT_ID);

        P currentTurn = null;
        if (currentTurnParticipantId != null && participantDao.exists(id, currentTurnParticipantId)) {
            currentTurn = participantDao.find(id, currentTurnParticipantId);
        }

        return new EncounterFields<P>(id, name, isInCombat, currentTurn);
    }

    @Override
    protected String getDefaultOrderBy() {
        return NAME + " ASC";
    }

    public static class EncounterFields<P> extends IdNamePair {
        public boolean isInCombat;
        public P currentTurn;

        public EncounterFields(Encounter<P> encounter) {
            this(encounter.getId(), encounter.getName(), encounter.isInCombat(), encounter.getCurrentTurn());
        }

        public EncounterFields(long id, String name, boolean isInCombat, P currentTurn) {
            super(id, name);
            this.isInCombat = isInCombat;
            this.currentTurn = currentTurn;
        }
    }
}
