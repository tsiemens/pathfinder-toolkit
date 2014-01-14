package com.lateensoft.pathfinder.toolkit.test.db.repository;

import android.test.AndroidTestCase;

import com.lateensoft.pathfinder.toolkit.db.IDNamePair;
import com.lateensoft.pathfinder.toolkit.db.repository.PTPartyRepository;
import com.lateensoft.pathfinder.toolkit.model.party.PTParty;

public class PTPartyRepositoryTest extends AndroidTestCase {
	
	private PTPartyRepository m_repo;
	
	private PTParty m_party1;
	private PTParty m_party2;
	private PTParty m_partyE;
	
	@Override
	public void setUp() {
		m_repo = new PTPartyRepository();
		
		m_party1 = new PTParty("A party");
		m_party2 = new PTParty("B party");
		m_partyE = new PTParty("E party");
		assertTrue(m_repo.insert(m_party1)!= -1);
		assertTrue(m_repo.insert(m_party2)!= -1);
		assertTrue(m_repo.insert(m_partyE, true)!= -1);
	}
	
	@Override
	protected void tearDown() throws Exception {
		m_repo.delete(m_party1.getID());
		m_repo.delete(m_party2.getID());
		m_repo.delete(m_partyE.getID());
		assertTrue (m_repo.query(m_party1.getID()) == null);
		super.tearDown();
	}

	public void testQuery() {
		PTParty queried = m_repo.query(m_party1.getID());
		assertEquals(m_party1.getID(), queried.getID());
		assertEquals(m_party1.getName(), queried.getName());
	}
	
	public void testQueryList() {
		IDNamePair[] names = m_repo.queryList();
		assertEquals(m_party1.getName(), names[0].getName());
		assertEquals(m_party2.getName(), names[1].getName());
		assertEquals(2, names.length);
	}

	public void testQueryEncounter() {
		PTParty encParty = m_repo.queryEncounterParty();
		assertEquals(m_partyE.getID(), encParty.getID());
		assertEquals(m_partyE.getName(), encParty.getName());
	}
}
