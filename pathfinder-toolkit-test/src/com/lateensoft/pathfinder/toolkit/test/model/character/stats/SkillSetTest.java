package com.lateensoft.pathfinder.toolkit.test.model.character.stats;

import android.test.AndroidTestCase;
import com.lateensoft.pathfinder.toolkit.model.character.stats.Skill;
import com.lateensoft.pathfinder.toolkit.model.character.stats.SkillSet;

/**
 * @author trevsiemens
 */
public class SkillSetTest extends AndroidTestCase {

    SkillSet m_skillSet;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        m_skillSet = new SkillSet();
    }

    public void testNewValidatedSkillSet() {
        m_skillSet.getSkillByIndex(4).setSkillKey(100);
        m_skillSet.getSkillByIndex(6).setSkillKey(-1);

        m_skillSet.deleteSkill(m_skillSet.getSkillByIndex(25));
        m_skillSet.getSkillByIndex(15).setAbilityKey(0);
        m_skillSet.getSkillByIndex(17).setAbilityKey(7);
        m_skillSet.getSkillByIndex(18).setAbilityKey(-1);

        TestCorrectionListener listener = new TestCorrectionListener();

        SkillSet newSkillSet = SkillSet.newValidatedSkillSet(m_skillSet.getSkills(), listener);

        assertEquals(2, listener.removes);
        assertEquals(3, listener.adds);
        assertEquals(3, listener.edits);
        assertEquals(SkillSet.getSkillNames().length, newSkillSet.size());

        listener.adds = 0;
        listener.removes = 0;
        listener.edits = 0;
        newSkillSet.validate(listener);

        assertEquals(0, listener.removes);
        assertEquals(0, listener.adds);
        assertEquals(0, listener.edits);
    }

    private class TestCorrectionListener implements SkillSet.CorrectionListener {
        public int removes = 0;
        public int adds = 0;
        public int edits = 0;

        @Override
        public void onInvalidSkillRemoved(Skill removedSkill) {
            removes++;
        }

        @Override
        public void onMissingSkillAdded(Skill addedSkill) {
            adds++;
        }

        @Override
        public void onSkillModified(Skill modifiedSkill) {
            edits++;
        }
    }
}
