package com.lateensoft.pathfinder.toolkit.model.stats;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lateensoft.pathfinder.toolkit.model.character.stats.Skill;
import com.lateensoft.pathfinder.toolkit.model.character.stats.SkillSet;
import com.lateensoft.pathfinder.toolkit.model.character.stats.SkillType;
import com.lateensoft.pathfinder.toolkit.model.character.stats.ValidatedTypedSet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(JUnit4.class)
public class SkillSetTest {

    SkillSet defaultSkillSet;

    @Before
    public void setUp() throws Exception {
        defaultSkillSet = new SkillSet();
    }

    @Test
    public void defaultConstructionHasAllSkills() {
        Map<SkillType, Integer> skillMap = mapOfNumberOfEachSkillType();

        assertThat(skillMap.keySet(), containsInAnyOrder(SkillType.values()));
    }

    @Test
    public void defaultConstructionHasOneOfEachSkill() {
        Map<SkillType, Integer> skillMap = mapOfNumberOfEachSkillType();

        assertThat(skillMap.values(), everyItem(equalTo(1)));
    }

    private Map<SkillType, Integer> mapOfNumberOfEachSkillType() {
        Map<SkillType, Integer> skillMap = Maps.newHashMap();
        List<SkillType> types = SkillType.getKeySortedValues();
        for (SkillType type : types) {
            skillMap.put(type, 0);
        }

        for (Skill skill : defaultSkillSet) {
            SkillType type = skill.getType();
            skillMap.put(type, skillMap.get(type) + 1);
        }
        return skillMap;
    }

    @Test
    public void validatingConstructorInvalidDuplicates() {
        List<Skill> invalidSkills = Lists.newArrayList(new Skill(SkillType.BLUFF), new Skill(SkillType.BLUFF));

        TestCorrectionListener listener = new TestCorrectionListener();
        SkillSet skillSet = new SkillSet(invalidSkills, listener);

        assertEquals(defaultSkillSet.size(), skillSet.size());
        assertEquals(1, listener.removes);
        assertEquals(defaultSkillSet.size() - 1, listener.adds);
    }

    @Test
    public void validatingConstructorValidDuplicates() {
        List<Skill> validSkills = Lists.newArrayList(new Skill(SkillType.CRAFT), new Skill(SkillType.CRAFT));

        TestCorrectionListener listener = new TestCorrectionListener();
        SkillSet skillSet = new SkillSet(validSkills, listener);

        assertEquals(defaultSkillSet.size() + 1, skillSet.size());
        assertEquals(0, listener.removes);
        assertEquals(defaultSkillSet.size() - 1, listener.adds);
    }

    @Test
    public void validatingConstructorValid() {
        TestCorrectionListener listener = new TestCorrectionListener();
        SkillSet skillSet = new SkillSet(defaultSkillSet.getSkills(), listener);

        assertEquals(defaultSkillSet.size(), skillSet.size());
        assertEquals(0, listener.removes);
        assertEquals(0, listener.adds);
    }

    private class TestCorrectionListener implements ValidatedTypedSet.CorrectionListener<Skill> {
        public int removes = 0;
        public int adds = 0;

        @Override
        public void onInvalidItemRemoved(Skill removedSkill) {
            removes++;
        }

        @Override
        public void onMissingItemAdded(Skill addedSkill) {
            adds++;
        }
    }
}
