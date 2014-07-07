package com.lateensoft.pathfinder.toolkit.db.dao;

import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.db.BaseDatabaseTest;
import com.lateensoft.pathfinder.toolkit.db.dao.table.*;
import com.lateensoft.pathfinder.toolkit.db.repository.CharacterRepository;
import com.lateensoft.pathfinder.toolkit.model.IdStringPair;
import com.lateensoft.pathfinder.toolkit.model.NamedList;
import com.lateensoft.pathfinder.toolkit.model.character.PathfinderCharacter;
import com.lateensoft.pathfinder.toolkit.util.CharacterUtils;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

@Config(manifest=Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class PartySummaryDAOTest extends BaseDatabaseTest {
    private PartySummaryDAO partyDao;
    private PartyMemberNameDAO memberDao;
    
    private CharacterModelDAO charDao;

    private NamedList<IdStringPair> party1;

    private PathfinderCharacter character1a;
    private IdStringPair member1a;
    
    private PathfinderCharacter character1b;
    private IdStringPair member1b;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        partyDao = new PartySummaryDAO(Robolectric.application);
        memberDao = new PartyMemberNameDAO(Robolectric.application);
        charDao = new CharacterModelDAO(Robolectric.application);

        character1a = CharacterUtils.buildTestCharacter();
        character1b = new PathfinderCharacter.Builder().build();
        charDao.add(character1a);
        charDao.add(character1b);

        member1a = new IdStringPair(character1a.getId(), character1a.getName());
        member1b = new IdStringPair(character1b.getId(), character1b.getName());
        
        party1 = new NamedList<IdStringPair>("Party 1",
                Lists.newArrayList(member1a, member1b));
        
        assertTrue(partyDao.add(party1) != -1);
    }

    @After
    public void tearDown() throws Exception {
        if (partyDao.exists(party1.getId())) {
            partyDao.remove(party1);
        }
        charDao.remove(character1a);
        charDao.remove(character1b);
    }

    @Test
    public void removeValidParty() throws DataAccessException {
        partyDao.remove(party1);
        assertNull(partyDao.find(party1.getId()));
        assertNull(memberDao.find(party1.getId(), member1a.getId()));
        assertNull(memberDao.find(party1.getId(), member1b.getId()));

        CharacterNameDAO characterDAO = new CharacterNameDAO(Robolectric.application);
        assertTrue(characterDAO.exists(member1a.getId()));
        assertTrue(characterDAO.exists(member1b.getId()));
    }

    @Test(expected=DataAccessException.class)
    public void removeInvalidParty() throws DataAccessException {
        partyDao.remove(party1);
        partyDao.remove(party1);
    }

    @Test
    public void updateValidParty() throws DataAccessException {
        party1.setName("new name");
        partyDao.update(party1);
        NamedList<IdStringPair> updated = partyDao.find(party1.getId());
        assertEquals(party1, updated);
    }

    @Test(expected=DataAccessException.class)
    public void updateInvalidParty() throws DataAccessException {
        party1.setName("new name");
        party1.setId(-1L);
        partyDao.update(party1);
    }

    @Test
    public void findValidParty() {
        NamedList<IdStringPair> foundParty = partyDao.find(party1.getId());
        assertEquals(party1, foundParty);
    }

    @Test
    public void findInvalidParty() {
        assertNull(partyDao.find(-1L));
    }

    @Test
    public void findValidMember() {
        IdStringPair Member1a = memberDao.find(party1.getId(), this.member1a.getId());
        IdStringPair Member1b = memberDao.find(party1.getId(), this.member1b.getId());
        assertEquals(this.member1a, Member1a);
        assertEquals(this.member1b, Member1b);
    }

    @Test
    public void findInvalidMember() {
        assertNull(memberDao.find(party1.getId(), -1L));
        assertNull(memberDao.find(-1L, member1a.getId()));
    }

    @Test
    public void removeValidMember() throws DataAccessException {
        memberDao.remove(party1.getId(), member1a);
        assertNull(memberDao.find(party1.getId(), member1a.getId()));

        CharacterRepository charRepo = new CharacterRepository();
        assertTrue(charRepo.doesExist(member1a.getId()));
    }

    @Test(expected = DataAccessException.class)
    public void removeInvalidMember() throws DataAccessException {
        memberDao.remove(party1.getId(), member1a);
        memberDao.remove(party1.getId(), member1a);
    }
}
