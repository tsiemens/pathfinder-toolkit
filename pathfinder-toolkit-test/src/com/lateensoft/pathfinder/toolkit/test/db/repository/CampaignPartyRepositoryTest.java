package com.lateensoft.pathfinder.toolkit.test.db.repository;

import java.util.List;

import android.test.AndroidTestCase;

import com.lateensoft.pathfinder.toolkit.db.repository.PartyRepository;
import com.lateensoft.pathfinder.toolkit.model.IdStringPair;
import com.lateensoft.pathfinder.toolkit.model.NamedList;
import com.lateensoft.pathfinder.toolkit.model.character.PathfinderCharacter;

public class CampaignPartyRepositoryTest extends AndroidTestCase {
	
	private PartyRepository m_repo;
	
	private NamedList<PathfinderCharacter> m_party1;
	private NamedList<PathfinderCharacter> m_party2;

	@Override
	public void setUp() {
		m_repo = new PartyRepository();
		
		m_party1 = new NamedList<PathfinderCharacter>("A party");
		m_party2 = new NamedList<PathfinderCharacter>("B party");
		assertTrue(m_repo.insert(m_party1)!= -1);
		assertTrue(m_repo.insert(m_party2)!= -1);
	}
	
	@Override
	protected void tearDown() throws Exception {
		m_repo.delete(m_party1.getId());
		m_repo.delete(m_party2.getId());
		assertTrue (m_repo.query(m_party1.getId()) == null);
		super.tearDown();
	}

	public void testQuery() {
        NamedList<PathfinderCharacter> queried = m_repo.query(m_party1.getId());
		assertEquals(m_party1.getId(), queried.getId());
		assertEquals(m_party1.getName(), queried.getName());
	}
	
	public void testQueryList() {
		List<IdStringPair> names = m_repo.queryIdNameList();
		assertEquals(m_party1.getName(), names.get(0).getValue());
		assertEquals(m_party2.getName(), names.get(1).getValue());
		assertEquals(2, names.size());
	}
}
