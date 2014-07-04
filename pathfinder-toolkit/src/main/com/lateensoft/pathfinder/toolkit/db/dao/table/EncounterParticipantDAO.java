package com.lateensoft.pathfinder.toolkit.db.dao.table;

import android.content.ContentValues;
import android.content.Context;
import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.db.dao.OwnedObject;
import com.lateensoft.pathfinder.toolkit.db.dao.OwnedWeakTableDAO;
import com.lateensoft.pathfinder.toolkit.db.dao.Table;
import com.lateensoft.pathfinder.toolkit.model.party.EncounterParticipant;
import org.jetbrains.annotations.Nullable;

import java.util.Hashtable;
import java.util.List;

public class EncounterParticipantDAO extends OwnedWeakTableDAO<Long, Long, EncounterParticipant> {
    public static final String TABLE = "EncounterParticipant";

    private static final String ENCOUNTER_ID = "encounter_id";
    private static final String CHARACTER_ID = "character_id";
    private static final String INITIATIVE_SCORE = "InitiativeScore";
    private static final String TURN_ORDER = "TurnOrder";

    private CharacterModelDAO characterDAO;

    public EncounterParticipantDAO(Context context) {
        super(context);
        characterDAO = new CharacterModelDAO(context);
    }

    @Override
    protected Table initTable() {
        return new Table(TABLE, ENCOUNTER_ID, CHARACTER_ID, INITIATIVE_SCORE, TURN_ORDER);
    }

    @Override
    protected String getOwnerIdSelector(Long characterId) {
        return TABLE + "." + ENCOUNTER_ID + "=" + characterId;
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
        return getTable().union(characterDAO.getTable(), CHARACTER_ID, CharacterModelDAO.CHARACTER_ID);
    }

    @Override
    protected OwnedObject<Long, Long> getIdFromRowData(OwnedObject<Long, EncounterParticipant> rowData) {
        return new OwnedObject<Long, Long>(rowData.getOwnerId(), rowData.getObject().getId());
    }

    @Override
    public OwnedObject<Long, Long> add(OwnedObject<Long, EncounterParticipant> rowData) throws DataAccessException {
        OwnedObject<Long, Long> id = null;
        boolean characterExists = characterDAO.exists(rowData.getObject().getId());
        try {
            beginTransaction();
            if (!characterExists) {
                characterDAO.add(rowData.getObject());
            }
            id = super.add(rowData);
            setTransactionSuccessful();
        } finally {
            endTransaction();
        }
        return id;
    }

    @Override
    public void update(OwnedObject<Long, EncounterParticipant> rowData) throws DataAccessException {
        try {
            beginTransaction();
            characterDAO.update(rowData.getObject());
            super.update(rowData);
            setTransactionSuccessful();
        } finally {
            endTransaction();
        }
    }

    @Override
    protected ContentValues getContentValues(OwnedObject<Long, EncounterParticipant> rowData) {
        EncounterParticipant object = rowData.getObject();
        ContentValues values = new ContentValues();
        values.put(CHARACTER_ID, object.getId());
        values.put(ENCOUNTER_ID, rowData.getOwnerId());
        values.put(INITIATIVE_SCORE, object.getInitiativeScore());
        values.put(TURN_ORDER, object.getTurnOrder());
        return values;
    }

    @Override
    protected EncounterParticipant buildFromHashTable(Hashtable<String, Object> hashTable) {
        EncounterParticipant.Builder builder = new EncounterParticipant.Builder();
        characterDAO.populateBuilderFromHashTable(hashTable, builder);
        populateBuilderFromHashTable(hashTable, builder);
        return builder.build();
    }

    public void populateBuilderFromHashTable(Hashtable<String, Object> hashTable, EncounterParticipant.Builder builder) {
        builder.setInitiativeScore(((Long) hashTable.get(INITIATIVE_SCORE)).intValue());
        builder.setTurnOrder(((Long) hashTable.get(TURN_ORDER)).intValue());
        builder.setEncounterId((Long) hashTable.get(ENCOUNTER_ID));
    }
}
