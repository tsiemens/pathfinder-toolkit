package com.lateensoft.pathfinder.toolkit.test.db.repository;

import com.lateensoft.pathfinder.toolkit.db.repository.PTFeatRepository;
import com.lateensoft.pathfinder.toolkit.model.character.PTFeat;

import java.util.List;

public class PTFeatRepositoryTest extends PTBaseRepositoryTest {
	
	private PTFeat m_feat1;
	private PTFeat m_feat2;
	private PTFeatRepository m_repo;
	
	public void setUp() {
		super.setUp();
		m_repo = new PTFeatRepository();

		m_feat1 = new PTFeat(m_characterId, "A Feat", "description 1");
		m_feat2 = new PTFeat(m_characterId, "B Feat", "description 2");
		
		m_repo.insert(m_feat1);
		m_repo.insert(m_feat2);
	}
	
	public void testInsert() {
		PTFeat insertFeat = new PTFeat(m_characterId, "sdfsdfsdf", "sadfasdehrh");
		long id = m_repo.insert(insertFeat);
		assertTrue(id != INSERT_FAIL);
	}
	
	public void testQuery() {
		PTFeat queried = m_repo.query(m_feat1.getID());
		assertEquals(m_feat1, queried);
	}
	
	public void testUpdate() {
		PTFeat toUpdate = new PTFeat(m_feat2.getID(), m_characterId, "sidkjf", "sdfsdf");
		m_repo.update(toUpdate);
		PTFeat updated = m_repo.query(m_feat2.getID());
		assertEquals(updated, toUpdate);
	}
	
	public void testDelete() {
		m_repo.delete(m_feat1.getID());
		assertTrue (m_repo.query(m_feat1.getID()) == null);
	}
	
	public void testQuerySet() {
		List<PTFeat> queriedFeats = m_repo.querySet(m_characterId);
		assertEquals(queriedFeats.get(0), m_feat1);
		assertEquals(queriedFeats.get(1), m_feat2);
	}
	
	private static void assertEquals(PTFeat feat1, PTFeat feat2) {
		assertEquals(feat1.getID(), feat2.getID());
		assertEquals(feat1.getCharacterID(), feat2.getCharacterID());
		assertEquals(feat1.getName(), feat2.getName());
		assertEquals(feat1.getDescription(), feat2.getDescription());
	}

}
