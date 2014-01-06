package com.lateensoft.pathfinder.toolkit.test.db.repository;

import com.lateensoft.pathfinder.toolkit.db.repository.PTCharacterRepository;
import com.lateensoft.pathfinder.toolkit.model.character.PTCharacter;

public class PTCharacterRepositoryTest extends PTBaseRepositoryTest {
	public void testCharacterQuery() {
		PTCharacterRepository repo = new PTCharacterRepository();
		PTCharacter joe = repo.query(mCharacterId);
		assertEquals(CHARACTER_TAG, joe.getTag());
	}
}
