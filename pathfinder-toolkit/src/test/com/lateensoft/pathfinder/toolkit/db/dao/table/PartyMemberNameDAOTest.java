package com.lateensoft.pathfinder.toolkit.db.dao.table;

import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.db.BaseDatabaseTest;
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
public class PartyMemberNameDAOTest extends BaseDatabaseTest {
    private PartyDAO partyDao;
    private PartyMemberNameDAO memberDao;
    
    private CharacterModelDAO charDao;

    private NamedList<IdNamePair> party1;

    private PathfinderCharacter character1a;
    private IdNamePair member1a;
    
    private PathfinderCharacter character1b;
    private IdNamePair member1b;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        partyDao = new PartyDAO(Robolectric.application);
        memberDao = new PartyMemberNameDAO(Robolectric.application);
        charDao = new CharacterModelDAO(Robolectric.application);

        character1a = CharacterUtils.buildTestCharacter();
        character1b = new PathfinderCharacter.Builder().build();
        charDao.add(character1a);
        charDao.add(character1b);

        member1a = new IdNamePair(character1a.getId(), character1a.getName());
        member1b = new IdNamePair(character1b.getId(), character1b.getName());
        
        party1 = new NamedList<IdNamePair>("Party 1",
                Lists.newArrayList(member1a, member1b));
        
        partyDao.add(party1, memberDao);
    }

    @After
    public void tearDown() throws Exception {
        partyDao.remove(party1);
        charDao.remove(character1a);
        charDao.remove(character1b);
    }

    @Test
    public void findValid() {
        IdNamePair Member1a = memberDao.find(party1.getId(), this.member1a.getId());
        IdNamePair Member1b = memberDao.find(party1.getId(), this.member1b.getId());
        assertEquals(this.member1a, Member1a);
        assertEquals(this.member1b, Member1b);
    }

    @Test
    public void findInvalid() {
        assertNull(memberDao.find(party1.getId(), -1L));
        assertNull(memberDao.find(-1L, member1a.getId()));
    }

    @Test
    public void removeValid() throws DataAccessException {
        memberDao.remove(party1.getId(), member1a);
        assertNull(memberDao.find(party1.getId(), member1a.getId()));

        assertTrue(charDao.exists(member1a.getId()));
    }

    @Test(expected = DataAccessException.class)
    public void removeInvalid() throws DataAccessException {
        memberDao.remove(party1.getId(), member1a);
        memberDao.remove(party1.getId(), member1a);
    }
}
