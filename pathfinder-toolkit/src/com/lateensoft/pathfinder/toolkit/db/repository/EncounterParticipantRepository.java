package com.lateensoft.pathfinder.toolkit.db.repository;

import android.content.ContentValues;
import android.database.Cursor;
import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.db.repository.TableAttribute.SQLDataType;
import com.lateensoft.pathfinder.toolkit.dao.Identifiable;
import com.lateensoft.pathfinder.toolkit.model.party.EncounterParticipant;

import java.util.Hashtable;
import java.util.List;

public class EncounterParticipantRepository extends BaseRepository<EncounterParticipant> {

	public static final String TABLE = "EncounterParticipant";
	private static final String ENCOUNTER_ID = "encounter_id";
	private static final String INITIATIVE_SCORE = "InitiativeScore";
	private static final String TURN_ORDER = "TurnOrder";

	private CharacterRepository m_characterRepo = new CharacterRepository();

	public EncounterParticipantRepository() {
		super();
		TableAttribute character_id = new TableAttribute(CHARACTER_ID, SQLDataType.INTEGER, true);
		TableAttribute encounter_id = new TableAttribute(ENCOUNTER_ID, SQLDataType.INTEGER);
		TableAttribute initiative = new TableAttribute(INITIATIVE_SCORE, SQLDataType.INTEGER);
		TableAttribute turnOrder = new TableAttribute(TURN_ORDER, SQLDataType.INTEGER);

		TableAttribute[] columns = {encounter_id, character_id, initiative, turnOrder};
		m_tableInfo = new TableInfo(TABLE, columns);
	}
	
	public void populateBuilderFromHashTable(Hashtable<String, Object> hashTable, EncounterParticipant.Builder builder) {
        builder.setInitiativeScore(((Long) hashTable.get(INITIATIVE_SCORE)).intValue());
        builder.setTurnOrder(((Long) hashTable.get(TURN_ORDER)).intValue());
        builder.setEncounterId((Long) hashTable.get(ENCOUNTER_ID));
	}

    @Override
    protected EncounterParticipant buildFromHashTable(Hashtable<String, Object> hashTable) {
        EncounterParticipant.Builder builder = new EncounterParticipant.Builder();
        m_characterRepo.populateBuilderFromHashTable(hashTable, builder);
        populateBuilderFromHashTable(hashTable, builder);
        return builder.build();
    }
	
	@Override
	public long insert(EncounterParticipant object) {
        if (object.getEncounterId() == Identifiable.UNSET_ID) {
            throw new IllegalArgumentException("EncounterParticipant must have a set encounter ID");
        }
        if (!m_characterRepo.doesExist(object.getId())) {
            m_characterRepo.insert(object);
        }
		return super.insert(object);
	}

    /**
     * @param ids must be { character id, encounter id }
     * @return encounter participant identified by character id, encounter id
     */
	@Override
	public EncounterParticipant query(long ... ids) {
		String selector = String.format("%s.%s=%d AND %s.%s=%d AND "
				+"%s.%s=%s.%s", 
				TABLE, CHARACTER_ID, ids[0],
				TABLE, ENCOUNTER_ID, ids[1],
				TABLE, CHARACTER_ID, CharacterRepository.TABLE, CharacterRepository.CHARACTER_ID);
		
		String table = m_tableInfo.getTable()+", "+ CharacterRepository.TABLE;
		String[] columns = m_characterRepo.getCombinedTableColumns(m_tableInfo.getColumns());
		Cursor cursor = getDatabase().query(table, columns, selector);
		cursor.moveToFirst();
		if (!cursor.isAfterLast()) {
			Hashtable<String, Object> hashTable =  m_characterRepo.getTableOfValues(cursor);
			hashTable.putAll(getTableOfValues(cursor));
			return buildFromHashTable(hashTable);
		} else {
			return null;
		}
	}
	
	@Override
	public int update(EncounterParticipant object) {
		m_characterRepo.update(object);
		return super.update(object);
	}

    /**
     * @param participant
     * @return An SQL select statement for the participant
     */
    @Override
    protected String getSelector(EncounterParticipant participant) {
        return getSelector(participant.getId(), participant.getEncounterId());
    }

    /**
     * @param ids {character id, encounter id}
     * @return An SQL select statement for the participant
     */
    @Override
    protected String getSelector(long ... ids) {
        return ENCOUNTER_ID + "=" + ids[1] + " AND " + CHARACTER_ID + "=" + ids[0];
    }

	@Override
	protected ContentValues getContentValues(EncounterParticipant object) {
		ContentValues values = new ContentValues();
        values.put(CHARACTER_ID, object.getId());
        values.put(ENCOUNTER_ID, object.getEncounterId());
        values.put(INITIATIVE_SCORE, object.getInitiativeScore());
        values.put(TURN_ORDER, object.getTurnOrder());
		return values;
	}
	
	/**
	 * Returns all items for the character with encounterId
	 * @param encounterId
	 */
	public List<EncounterParticipant> querySet(long encounterId) {
		String table = CharacterRepository.TABLE + ", " + m_tableInfo.getTable();
		String selector = String.format("%s=%d AND %s.%s=%s.%s",
				ENCOUNTER_ID, encounterId,
				TABLE, CHARACTER_ID, CharacterRepository.TABLE, CharacterRepository.CHARACTER_ID);
		String orderBy = CharacterRepository.CHARACTER_ID + " ASC";
		String[] columns = m_characterRepo.getCombinedTableColumns(m_tableInfo.getColumns());
		
		Cursor cursor = getDatabase().query(true, table, columns, selector, 
				null, null, null, orderBy, null);
		
		List<EncounterParticipant> encounterParticipants = Lists.newArrayListWithCapacity(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Hashtable<String, Object> hashTable =  m_characterRepo.getTableOfValues(cursor);
			hashTable.putAll(getTableOfValues(cursor));
			encounterParticipants.add(buildFromHashTable(hashTable));
			cursor.moveToNext();
		}
		return encounterParticipants;
	}

    /**
     * Deletes all participants which have encounterid. Underlying characters are not deleted.
     * @return the number of participants deleted
     */
    public int deleteParticipants(long encounterId) {
        String selector = ENCOUNTER_ID + "=" + Long.toString(encounterId);
        return getDatabase().delete(EncounterParticipantRepository.TABLE, selector);
    }
}
