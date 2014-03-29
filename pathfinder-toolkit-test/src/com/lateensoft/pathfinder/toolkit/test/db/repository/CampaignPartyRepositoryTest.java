package com.lateensoft.pathfinder.toolkit.test.db.repository;

import java.util.List;
import java.util.Map.Entry;

import android.test.AndroidTestCase;

import com.lateensoft.pathfinder.toolkit.db.repository.PartyRepository;
import com.lateensoft.pathfinder.toolkit.model.party.CampaignParty;

public class CampaignPartyRepositoryTest extends AndroidTestCase {
	
	private PartyRepository m_repo;
	
	private CampaignParty m_party1;
	private CampaignParty m_party2;
	private CampaignParty m_partyE;
	
	@Override
	public void setUp() {
		m_repo = new PartyRepository();
		
		m_party1 = new CampaignParty("A party");
		m_party2 = new CampaignParty("B party");
		m_partyE = new CampaignParty("E party");
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
		CampaignParty queried = m_repo.query(m_party1.getID());
		assertEquals(m_party1.getID(), queried.getID());
		assertEquals(m_party1.getName(), queried.getName());
	}
	
	public void testQueryList() {
		List<Entry<Long, String>> names = m_repo.queryIdNameList();
		assertEquals(m_party1.getName(), names.get(0).getValue());
		assertEquals(m_party2.getName(), names.get(1).getValue());
		assertEquals(2, names.size());
	}

	public void testQueryEncounter() {
		CampaignParty encParty = m_repo.queryEncounterParty();
		assertEquals(m_partyE.getID(), encParty.getID());
		assertEquals(m_partyE.getName(), encParty.getName());
	}
}
