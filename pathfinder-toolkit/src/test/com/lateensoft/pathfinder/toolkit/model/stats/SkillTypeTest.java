package com.lateensoft.pathfinder.toolkit.model.stats;

import com.lateensoft.pathfinder.toolkit.model.character.stats.SkillType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import static com.lateensoft.pathfinder.toolkit.model.character.stats.SkillType.*;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(JUnit4.class)
public class SkillTypeTest {

    @Test
    public void keySortedValues() {
        List<SkillType> sortedValues = SkillType.getKeySortedValues();
        assertThat(sortedValues, contains(ACROBATICS, APPRAISE, BLUFF, CLIMB, CRAFT, DIPLOMACY,
                DISABLE_DEV, DISGUISE, ESCAPE, FLY, HANDLE_ANIMAL, HEAL, INTIMIDATE,
                KNOW_ARCANA, KNOW_DUNGEON, KNOW_ENG, KNOW_GEO, KNOW_HIST, KNOW_LOCAL,
                KNOW_NATURE, KNOW_NOBILITY, KNOW_PLANES, KNOW_RELIGION, LINGUISTICS, PERCEPTION,
                PERFORM, PROFESSION, RIDE, SENSE_MOTIVE, SLEIGHT_OF_HAND, SPELLCRAFT, STEALTH,
                SURVIVAL, SWIM, USE_MAGIC_DEVICE));
    }

    @Test
    public void typeForKey() {
        SkillType[] values = SkillType.values();
        SkillType[] generatedAbilities = new SkillType[values.length];
        for (int i = 0; i < values.length; i++) {
            generatedAbilities[i] = SkillType.forKey(values[i].getKey());
        }

        assertThat(generatedAbilities, equalTo(values));
    }

    @Test
    public void typeForInvalidKey() {
        try {
            SkillType.forKey(-1);
            fail();
        } catch (Exception ignored) {}
    }
}
