package com.lateensoft.pathfinder.toolkit.test.model.character.stats;

import android.test.AndroidTestCase;
import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTAbility;
import com.lateensoft.pathfinder.toolkit.model.character.stats.PTAbilitySet;

import java.util.List;

/**
 * @author trevsiemens
 */
public class PTAbilitySetTest extends AndroidTestCase {

    List<PTAbility> m_abilities;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        m_abilities = Lists.newArrayList();
        PTAbilitySet abilitySet = new PTAbilitySet();
        for (int i = 0; i < abilitySet.size(); i++) {
            m_abilities.add(abilitySet.getAbilityAtIndex(i));
        }
    }

    public void testNewValidatedAbilitySet() {
        m_abilities.get(0).setAbilityKey(8);
        m_abilities.remove(1);

        TestCorrectionListener listener = new TestCorrectionListener();

        PTAbilitySet newAbilitySet = PTAbilitySet.newValidatedAbilitySet(m_abilities, listener);

        assertEquals(1, listener.removes);
        assertEquals(2, listener.adds);
        assertEquals(6, newAbilitySet.size());

        listener.adds = 0;
        listener.removes = 0;
        newAbilitySet.validate(listener);

        assertEquals(0, listener.removes);
        assertEquals(0, listener.adds);
    }

    private class TestCorrectionListener implements PTAbilitySet.CorrectionListener {
        public int removes = 0;
        public int adds = 0;

        @Override
        public void onInvalidAbilityRemoved(PTAbility removedAbility) {
            removes++;
        }

        @Override
        public void onMissingAbilityAdded(PTAbility addedAbility) {
            adds++;
        }
    }
}
