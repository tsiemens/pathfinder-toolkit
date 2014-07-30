package com.lateensoft.pathfinder.toolkit.db.dao.table;

import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.db.BaseDatabaseTest;
import com.lateensoft.pathfinder.toolkit.model.party.Encounter;
import com.lateensoft.pathfinder.toolkit.model.party.EncounterParticipant;
import com.lateensoft.pathfinder.toolkit.util.CharacterUtils;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@Config(manifest=Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class EncounterDAOTest extends BaseDatabaseTest {
    private EncounterDAO<EncounterParticipant> encounterDao;
    private EncounterParticipantDAO participantDao;

    private Encounter<EncounterParticipant> encounter1;

    private EncounterParticipant participant1a;
    private EncounterParticipant participant1b;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        participantDao = new EncounterParticipantDAO(Robolectric.application);
        encounterDao = new EncounterDAO<EncounterParticipant>(Robolectric.application, participantDao);

        participant1a = CharacterUtils.buildTestEncounterParticipant();
        participant1b = new EncounterParticipant.Builder().build();
        encounter1 = new Encounter<EncounterParticipant>("Encounter 1",
                Lists.newArrayList(participant1a, participant1b));
        encounter1.setInCombat(true);

        assertTrue(encounterDao.add(encounter1) != -1);
    }

    @After
    public void tearDown() throws Exception {
        if (encounterDao.exists(encounter1.getId())) {
            encounterDao.remove(encounter1);
        }
    }

    @Test
    public void removeValidEncounter() throws DataAccessException {
        encounterDao.remove(encounter1);
        assertNull(encounterDao.find(encounter1.getId()));
        assertNull(participantDao.find(encounter1.getId(), participant1a.getId()));
        assertNull(participantDao.find(encounter1.getId(), participant1b.getId()));

        CharacterNameDAO characterDAO = new CharacterNameDAO(Robolectric.application);
        assertTrue(characterDAO.exists(participant1a.getId()));
        assertTrue(characterDAO.exists(participant1b.getId()));
    }

    @Test(expected=DataAccessException.class)
    public void removeInvalidEncounter() throws DataAccessException {
        encounterDao.remove(encounter1);
        encounterDao.remove(encounter1);
    }

    @Test
    public void updateValidEncounter() throws DataAccessException {
        encounter1.setName("new name");
        encounter1.setCurrentTurn(participant1a);
        encounter1.setInCombat(false);
        encounterDao.updateFields(encounter1);
        Encounter<EncounterParticipant> updated = encounterDao.findList(encounter1.getId());
        assertEquals(encounter1, updated);

        encounter1.setCurrentTurn(null);
        encounterDao.updateFields(encounter1);
        updated = encounterDao.findList(encounter1.getId());
        assertEquals(encounter1, updated);
    }

    @Test(expected=DataAccessException.class)
    public void updateInvalidEncounter() throws DataAccessException {
        encounter1.setName("new name");
        encounter1.setId(-1L);
        encounterDao.updateFields(encounter1);
    }

    @Test
    public void findValidEncounter() {
        Encounter<EncounterParticipant> encounter = encounterDao.findList(encounter1.getId());
        assertEquals(encounter1, encounter);
    }

    @Test
    public void findInvalidEncounter() {
        assertNull(encounterDao.find(-1L));
    }

    @Test
    public void updateValidParticipant() throws DataAccessException {
        participant1a.setInitiativeScore(40);
        participant1b.setTurnOrder(34);
        participantDao.update(encounter1.getId(), participant1a);
        EncounterParticipant updated = participantDao.find(encounter1.getId(), participant1a.getId());
        assertEquals(participant1a, updated);
    }

    @Test
    public void findValidParticipant() {
        EncounterParticipant participant1a = participantDao.find(encounter1.getId(), this.participant1a.getId());
        EncounterParticipant participant1b = participantDao.find(encounter1.getId(), this.participant1b.getId());
        assertEquals(this.participant1a, participant1a);
        assertEquals(this.participant1b, participant1b);
    }

    @Test
    public void findInvalidParticipant() {
        assertNull(participantDao.find(encounter1.getId(), -1L));
        assertNull(participantDao.find(-1L, participant1a.getId()));
    }

    @Test
    public void removeValidParticipant() throws DataAccessException {
        participantDao.remove(encounter1.getId(), participant1a);
        assertNull(participantDao.find(encounter1.getId(), participant1a.getId()));

        CharacterModelDAO charRepo = new CharacterModelDAO(Robolectric.application);
        assertTrue(charRepo.exists(participant1a.getId()));
    }

    @Test(expected = DataAccessException.class)
    public void removeInvalidParticipant() throws DataAccessException {
        participantDao.remove(encounter1.getId(), participant1a);
        participantDao.remove(encounter1.getId(), participant1a);
    }
}
