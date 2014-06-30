package com.lateensoft.pathfinder.toolkit.db.dao;

import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.db.dao.table.AbilityDAO;
import com.lateensoft.pathfinder.toolkit.model.character.stats.Ability;
import com.lateensoft.pathfinder.toolkit.model.character.stats.AbilitySet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class AbilityDAOTest extends CharacterComponentDAOTest {

	private Ability m_ability;
	private AbilityDAO dao;

	@Override
	public void setUp() throws Exception {
        super.setUp();
        dao = new AbilityDAO(Robolectric.application);

        m_ability = dao.findAllForOwner(getTestCharacterId()).get(2);
	}

    @Test
    public void findAbility() {
        Ability queried = dao.find(getTestCharacterId(), m_ability.getType());
        assertEquals(m_ability, queried);
    }

    @Test
    public void testUpdate() throws DataAccessException {
        Ability toUpdate = new Ability(m_ability.getType(), 8, -2);
        dao.update(getTestCharacterId(), toUpdate);
        Ability updated = dao.find(getTestCharacterId(), m_ability.getType());
        assertEquals(updated, toUpdate);
    }

    @Test
    public void findAllForCharacter() {
        List<Ability> foundAbilities = dao.findAllForOwner(getTestCharacterId());
        AbilitySet abilitySet = getTestCharacter().getAbilitySet();

        assertThat(abilitySet, hasItems(foundAbilities.toArray(new Ability[foundAbilities.size()])));
    }
}
