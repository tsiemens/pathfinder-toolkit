package com.lateensoft.pathfinder.toolkit.test.db.repository;

import android.test.AndroidTestCase;
import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.db.repository.CharacterRepository;
import com.lateensoft.pathfinder.toolkit.db.repository.EncounterParticipantRepository;
import com.lateensoft.pathfinder.toolkit.db.repository.EncounterRepository;
import com.lateensoft.pathfinder.toolkit.model.NamedList;
import com.lateensoft.pathfinder.toolkit.model.party.EncounterParticipant;
import com.lateensoft.pathfinder.toolkit.test.util.CharacterUtils;

/**
 * @author tsiemens
 */
public class EncounterRepositoryTest extends AndroidTestCase {

    private EncounterRepository m_encounterRepo;
    private EncounterParticipantRepository m_participantRepo;

    private NamedList<EncounterParticipant> m_encounter1;

    private EncounterParticipant m_participant1a;
    private EncounterParticipant m_participant1b;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        m_encounterRepo = new EncounterRepository();
        m_participantRepo = new EncounterParticipantRepository();

        m_participant1a = CharacterUtils.buildTestEncounterParticipant();
        m_participant1b = new EncounterParticipant.Builder().build();
        m_encounter1 = new NamedList<EncounterParticipant>("Encounter 1",
                Lists.newArrayList(m_participant1a, m_participant1b));

        assertTrue(m_encounterRepo.insert(m_encounter1) != -1);
    }

    @Override
    protected void tearDown() throws Exception {
        m_encounterRepo.delete(m_encounter1);
        super.tearDown();
    }

    public void testDeleteEncounter() {
        m_encounterRepo.delete(m_encounter1);
        assertTrue(m_encounterRepo.query(m_encounter1.getID()) == null);
        assertTrue(m_participantRepo.query(m_participant1a.getID(), m_participant1a.getEncounterId()) == null);
        assertTrue(m_participantRepo.query(m_participant1b.getID(), m_participant1a.getEncounterId()) == null);

        CharacterRepository charRepo = new CharacterRepository();
        assertTrue(charRepo.doesExist(m_participant1a.getID()));
        assertTrue(charRepo.doesExist(m_participant1b.getID()));
    }

    public void testQueryEncounter() {
        NamedList<EncounterParticipant> encounter = m_encounterRepo.query(m_encounter1.getID());
        assertEquals(m_encounter1, encounter);
    }

    public void testQueryParticipant() {
        EncounterParticipant participant1a = m_participantRepo.query(m_participant1a.getID(), m_encounter1.getID());
        EncounterParticipant participant1b = m_participantRepo.query(m_participant1b.getID(), m_encounter1.getID());
        assertEquals(m_participant1a, participant1a);
        assertEquals(m_participant1b, participant1b);
    }

    public void testDeleteParticipant() {
        m_participantRepo.delete(m_participant1a);
        assertTrue(m_participantRepo.query(m_participant1a.getID(), m_encounter1.getID()) == null);

        CharacterRepository charRepo = new CharacterRepository();
        assertTrue(charRepo.doesExist(m_participant1a.getID()));
    }
}
