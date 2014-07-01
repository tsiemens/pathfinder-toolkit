package com.lateensoft.pathfinder.toolkit.db.repository;

import com.lateensoft.pathfinder.toolkit.db.repository.TableAttribute.SQLDataType;
import com.lateensoft.pathfinder.toolkit.model.NamedList;
import com.lateensoft.pathfinder.toolkit.model.party.EncounterParticipant;

import java.util.Hashtable;
import java.util.List;

public class EncounterRepository extends AbstractNamedListRepository<EncounterParticipant> {
    private static final String TABLE = "Encounter";
    private static final String ENCOUNTER_ID = "encounter_id";
    private static final String NAME = "Name";

    public EncounterParticipantRepository m_participantRepo = new EncounterParticipantRepository();

    public EncounterRepository() {
        super();
        TableAttribute id = new TableAttribute(ENCOUNTER_ID, SQLDataType.INTEGER, true);
        TableAttribute name = new TableAttribute(NAME, SQLDataType.TEXT);
        TableAttribute[] columns = {id, name};
        m_tableInfo = new TableInfo(TABLE, columns);
    }

    @Override
    protected NamedList<EncounterParticipant> buildFromHashTable(Hashtable<String, Object> hashTable) {
        long id = (Long) hashTable.get(ENCOUNTER_ID);
        String name = (String) hashTable.get(NAME);

        List<EncounterParticipant> participants = m_participantRepo.querySet(id);;

        return new NamedList<EncounterParticipant>(id, name, participants);
    }

    @Override
    public int delete(long... ids) {
        m_participantRepo.deleteParticipants(ids[0]);
        return super.delete(ids);
    }

    @Override
    public int delete(NamedList<EncounterParticipant> object) {
        return delete(object.getId());
    }

    @Override
    protected String getIdColumn() {
       return ENCOUNTER_ID;
    }

    @Override
    protected String getNameColumn() {
        return NAME;
    }

    @Override
    protected long insertListItem(NamedList<EncounterParticipant> list, EncounterParticipant item) {
        item.setEncounterId(list.getId());
        return m_participantRepo.insert(item);
    }
}
