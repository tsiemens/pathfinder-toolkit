package com.lateensoft.pathfinder.toolkit.test.db.repository;

import com.lateensoft.pathfinder.toolkit.db.repository.FluffInfoRepository;
import com.lateensoft.pathfinder.toolkit.model.character.FluffInfo;

public class FluffInfoRepositoryTest extends BaseRepositoryTest {
	private FluffInfo m_fluff;
	private FluffInfoRepository m_repo;
	
	@Override
	public void setUp() {
		super.setUp();
		m_repo = new FluffInfoRepository();
		
		m_fluff = m_repo.query(m_characterId);
		m_fluff.setAlignment("Chaotic Evil");
		m_fluff.setXP("1000");
		m_fluff.setNextLevelXP("45");
		m_fluff.setPlayerClass("Rogue");
		m_fluff.setRace("Human");
		m_fluff.setDeity("Glob");
		m_fluff.setLevel("20");
		m_fluff.setSize("med");
		m_fluff.setGender("M");
		m_fluff.setHeight("5'11\"");
		m_fluff.setWeight("150 lbs");
		m_fluff.setEyes("Red");
		m_fluff.setHair("None");
		m_fluff.setLanguages("Demonic");
		m_fluff.setDescription("Pretty much worse than Sauron");
		
		m_repo.update(m_fluff);
	}
	
	public void testQuery() {
		FluffInfo queried = m_repo.query(m_characterId);
		
		assertEquals(m_fluff.getId(), queried.getId());
		assertEquals(m_fluff.getName(), queried.getName());
		assertEquals(m_fluff.getAlignment(), queried.getAlignment());
		assertEquals(m_fluff.getXP(), queried.getXP());
		assertEquals(m_fluff.getNextLevelXP(), queried.getNextLevelXP());
		assertEquals(m_fluff.getPlayerClass(), queried.getPlayerClass());
		assertEquals(m_fluff.getRace(), queried.getRace());
		assertEquals(m_fluff.getDeity(), queried.getDeity());
		assertEquals(m_fluff.getLevel(), queried.getLevel());
		assertEquals(m_fluff.getSize(), queried.getSize());
		assertEquals(m_fluff.getGender(), queried.getGender());
		assertEquals(m_fluff.getHeight(), queried.getHeight());
		assertEquals(m_fluff.getWeight(), queried.getWeight());
		assertEquals(m_fluff.getEyes(), queried.getEyes());
		assertEquals(m_fluff.getHair(), queried.getHair());
		assertEquals(m_fluff.getLanguages(), queried.getLanguages());
		assertEquals(m_fluff.getDescription(), queried.getDescription());
	}
	
	public void testUpdate() {
		FluffInfo toUpdate = new FluffInfo();
		
		toUpdate.setId(m_characterId);
		toUpdate.setName("Bob");
		toUpdate.setAlignment("Lawful Good");
		toUpdate.setXP("1045");
		toUpdate.setNextLevelXP("A lot");
		toUpdate.setPlayerClass("Paladin");
		toUpdate.setRace("Devine Human");
		toUpdate.setDeity("None");
		toUpdate.setLevel("21");
		toUpdate.setSize("small");
		toUpdate.setGender("F");
		toUpdate.setHeight("5'7\"");
		toUpdate.setWeight("120 lbs");
		toUpdate.setEyes("White");
		toUpdate.setHair("Long, blond");
		toUpdate.setLanguages("Choir");
		toUpdate.setDescription("Went to rehab... and got a sex change...");
		
		m_repo.update(toUpdate);
		FluffInfo updated = m_repo.query(m_characterId);
		assertEquals(toUpdate.getId(), updated.getId());
		assertEquals(toUpdate.getName(), updated.getName());
		assertEquals(toUpdate.getAlignment(), updated.getAlignment());
		assertEquals(toUpdate.getXP(), updated.getXP());
		assertEquals(toUpdate.getNextLevelXP(), updated.getNextLevelXP());
		assertEquals(toUpdate.getPlayerClass(), updated.getPlayerClass());
		assertEquals(toUpdate.getRace(), updated.getRace());
		assertEquals(toUpdate.getDeity(), updated.getDeity());
		assertEquals(toUpdate.getLevel(), updated.getLevel());
		assertEquals(toUpdate.getSize(), updated.getSize());
		assertEquals(toUpdate.getGender(), updated.getGender());
		assertEquals(toUpdate.getHeight(), updated.getHeight());
		assertEquals(toUpdate.getWeight(), updated.getWeight());
		assertEquals(toUpdate.getEyes(), updated.getEyes());
		assertEquals(toUpdate.getHair(), updated.getHair());
		assertEquals(toUpdate.getLanguages(), updated.getLanguages());
		assertEquals(toUpdate.getDescription(), updated.getDescription());
	}

}
