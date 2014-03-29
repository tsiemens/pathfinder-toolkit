package com.lateensoft.pathfinder.toolkit.test.db.repository;

import com.lateensoft.pathfinder.toolkit.db.repository.SpellRepository;
import com.lateensoft.pathfinder.toolkit.model.character.Spell;

import java.util.List;

public class SpellRepositoryTest extends BaseRepositoryTest {
	
	private Spell m_spell1;
	private Spell m_spell2;
	private SpellRepository m_repo;
	
	public void setUp() {
		super.setUp();
		m_repo = new SpellRepository();

		m_spell1 = new Spell(m_characterId, "B Spell", 1, 4, "description 1");
		m_spell2 = new Spell(m_characterId, "A Spell", 2, 5, "description 2");
		
		m_repo.insert(m_spell2);
		m_repo.insert(m_spell1);
	}
	
	public void testInsert() {
		Spell insertSpell = new Spell(m_characterId, "sdfsdfsdf", 4, 6, "sadfasdehrh");
		long id = m_repo.insert(insertSpell);
		assertTrue(id != INSERT_FAIL);
	}
	
	public void testQuery() {
		Spell queried = m_repo.query(m_spell1.getID());
		assertEquals(m_spell1, queried);
	}
	
	public void testUpdate() {
		Spell toUpdate = new Spell(m_spell2.getID(), m_characterId, "23425", 5, 7, "sdfsdf");
		m_repo.update(toUpdate);
		Spell updated = m_repo.query(m_spell2.getID());
		assertEquals(updated, toUpdate);
	}
	
	public void testDelete() {
		m_repo.delete(m_spell1.getID());
		assertTrue (m_repo.query(m_spell1.getID()) == null);
	}
	
	public void testQuerySet() {
		List<Spell> queriedSpells = m_repo.querySet(m_characterId);
		assertEquals(queriedSpells.get(0), m_spell1);
		assertEquals(queriedSpells.get(1), m_spell2);
	}
	
	private static void assertEquals(Spell spell1, Spell spell2) {
		assertEquals(spell1.getID(), spell2.getID());
		assertEquals(spell1.getCharacterID(), spell2.getCharacterID());
		assertEquals(spell1.getName(), spell2.getName());
		assertEquals(spell1.getLevel(), spell2.getLevel());
		assertEquals(spell1.getPrepared(), spell2.getPrepared());
		assertEquals(spell1.getDescription(), spell2.getDescription());
	}

}
