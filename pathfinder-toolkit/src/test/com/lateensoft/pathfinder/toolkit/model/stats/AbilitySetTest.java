package com.lateensoft.pathfinder.toolkit.model.stats;

import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.model.character.stats.Ability;
import com.lateensoft.pathfinder.toolkit.model.character.stats.AbilitySet;
import com.lateensoft.pathfinder.toolkit.model.character.stats.AbilityType;
import com.lateensoft.pathfinder.toolkit.model.character.stats.ValidatedTypedSet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class AbilitySetTest {

    List<Ability> defaultAbilities;

    @Before
    public void setUp() throws Exception {
        defaultAbilities = Lists.newArrayList();
        AbilitySet abilitySet = new AbilitySet();
        for (int i = 0; i < abilitySet.size(); i++) {
            defaultAbilities.add(abilitySet.getAbilityAtIndex(i));
        }
    }

    @Test
    public void newValidatedAbilitySetWithCorrections() {
        defaultAbilities.add(new Ability(AbilityType.STR));
        defaultAbilities.remove(1);

        TestCorrectionListener listener = new TestCorrectionListener();
        AbilitySet newAbilitySet = new AbilitySet(defaultAbilities, listener);

        assertEquals(1, listener.removes);
        assertEquals(1, listener.adds);
        assertEquals(6, newAbilitySet.size());
    }

    @Test
    public void newValidatedAbilitySetCorrect() {
        TestCorrectionListener listener = new TestCorrectionListener();
        AbilitySet newAbilitySet = new AbilitySet(defaultAbilities, listener);

        assertEquals(0, listener.removes);
        assertEquals(0, listener.adds);
        assertEquals(6, newAbilitySet.size());
    }

    private class TestCorrectionListener implements ValidatedTypedSet.CorrectionListener<Ability> {
        public int removes = 0;
        public int adds = 0;

        @Override
        public void onInvalidItemRemoved(Ability removedAbility) {
            removes++;
        }

        @Override
        public void onMissingItemAdded(Ability addedAbility) {
            adds++;
        }
    }
}
