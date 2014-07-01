package com.lateensoft.pathfinder.toolkit.test.db.repository;

import com.lateensoft.pathfinder.toolkit.db.repository.FeatRepository;
import com.lateensoft.pathfinder.toolkit.model.character.Feat;

import java.util.List;

public class FeatRepositoryTest extends BaseRepositoryTest {
    
    private Feat m_feat1;
    private Feat m_feat2;
    private FeatRepository m_repo;
    
    public void setUp() {
        super.setUp();
        m_repo = new FeatRepository();

        m_feat1 = new Feat(m_characterId, "A Feat", "description 1");
        m_feat2 = new Feat(m_characterId, "B Feat", "description 2");
        
        m_repo.insert(m_feat1);
        m_repo.insert(m_feat2);
    }
    
    public void testInsert() {
        Feat insertFeat = new Feat(m_characterId, "sdfsdfsdf", "sadfasdehrh");
        long id = m_repo.insert(insertFeat);
        assertTrue(id != INSERT_FAIL);
    }
    
    public void testQuery() {
        Feat queried = m_repo.query(m_feat1.getId());
        assertEquals(m_feat1, queried);
    }
    
    public void testUpdate() {
        Feat toUpdate = new Feat(m_feat2.getId(), m_characterId, "sidkjf", "sdfsdf");
        m_repo.update(toUpdate);
        Feat updated = m_repo.query(m_feat2.getId());
        assertEquals(updated, toUpdate);
    }
    
    public void testDelete() {
        m_repo.delete(m_feat1.getId());
        assertTrue (m_repo.query(m_feat1.getId()) == null);
    }
    
    public void testQuerySet() {
        List<Feat> queriedFeats = m_repo.querySet(m_characterId);
        assertEquals(queriedFeats.get(0), m_feat1);
        assertEquals(queriedFeats.get(1), m_feat2);
    }
    
    private static void assertEquals(Feat feat1, Feat feat2) {
        assertEquals(feat1.getId(), feat2.getId());
        assertEquals(feat1.getCharacterID(), feat2.getCharacterID());
        assertEquals(feat1.getName(), feat2.getName());
        assertEquals(feat1.getDescription(), feat2.getDescription());
    }

}
