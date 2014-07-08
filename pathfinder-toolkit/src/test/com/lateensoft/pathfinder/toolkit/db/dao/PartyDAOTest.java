package com.lateensoft.pathfinder.toolkit.db.dao;

import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.db.BaseDatabaseTest;
import com.lateensoft.pathfinder.toolkit.db.dao.table.*;
import com.lateensoft.pathfinder.toolkit.model.IdNamePair;
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
public class PartyDAOTest extends BaseDatabaseTest {
    private PartyDAO partyDao;
    private PartyMemberNameDAO memberDao;
    
    private CharacterModelDAO charDao;

    private NamedList<IdNamePair> party1;

    private PathfinderCharacter character1a;
    private IdNamePair member1a;
    
    @Override
    public void setUp() throws Exception {
        super.setUp();
        partyDao = new PartyDAO(Robolectric.application);
        memberDao = new PartyMemberNameDAO(Robolectric.application);
        charDao = new CharacterModelDAO(Robolectric.application);

        character1a = CharacterUtils.buildTestCharacter();
        charDao.add(character1a);

        member1a = new IdNamePair(character1a.getId(), character1a.getName());

        party1 = new NamedList<IdNamePair>("Party 1", Lists.newArrayList(member1a));

        assertTrue(partyDao.add(party1, memberDao) != -1);
    }

    @After
    public void tearDown() throws Exception {
        if (partyDao.exists(party1.getId())) {
            partyDao.remove(party1);
        }
        charDao.remove(character1a);
    }

    @Test
    public void removeValid() throws DataAccessException {
        partyDao.remove(party1);
        assertNull(partyDao.find(party1.getId()));
        assertNull(memberDao.find(party1.getId(), member1a.getId()));

        assertTrue(charDao.exists(member1a.getId()));
    }

    @Test(expected=DataAccessException.class)
    public void removeInvalid() throws DataAccessException {
        partyDao.remove(party1);
        partyDao.remove(party1);
    }

    @Test
    public void updateValid() throws DataAccessException {
        party1.setName("new name");
        partyDao.update(party1.idNamePair());
        NamedList<IdNamePair> updated = partyDao.find(party1.getId(), memberDao);
        assertEquals(party1, updated);
    }

    @Test(expected=DataAccessException.class)
    public void updateInvalid() throws DataAccessException {
        party1.setName("new name");
        party1.setId(-1L);
        partyDao.update(party1.idNamePair());
    }

    @Test
    public void findValidIdNamePair() {
        IdNamePair foundParty = partyDao.find(party1.getId());
        assertEquals(party1.idNamePair(), foundParty);
    }

    @Test
    public void findInvalidIdNamePair() {
        assertNull(partyDao.find(-1L));
    }

    @Test
    public void findValidModel() {
        NamedList<IdNamePair> foundParty = partyDao.find(party1.getId(), memberDao);
        assertEquals(party1, foundParty);
    }

    @Test
    public void findInvalidModel() {
        assertNull(partyDao.find(-1L, memberDao));
    }
}
