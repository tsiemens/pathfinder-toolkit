package com.lateensoft.pathfinder.toolkit.repository.test;

import com.lateensoft.pathfinder.toolkit.character.sheet.PTCharacter;
import com.lateensoft.pathfinder.toolkit.repository.PTCharacterRepository;

public class PTCharacterRepositoryTest extends PTBaseRepositoryTest {
	public void testCharacterQuery() {
		PTCharacterRepository repo = new PTCharacterRepository();
		PTCharacter joe = repo.query(mCharacterId);
		assertEquals(CHARACTER_TAG, joe.getTag());
	}
}
