package com.lateensoft.pathfinder.toolkit.model.stats;

import com.lateensoft.pathfinder.toolkit.model.character.stats.AbilityType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import static com.lateensoft.pathfinder.toolkit.model.character.stats.AbilityType.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(JUnit4.class)
public class AbilityTypeTest {

    @Test
    public void keySortedValues() {
        List<AbilityType> sortedValues = AbilityType.getKeySortedValues();
        assertThat(sortedValues, contains(STR, DEX, CON, INT, WIS, CHA));
    }

    @Test
    public void typeForKey() {
        AbilityType[] values = AbilityType.values();
        AbilityType[] generatedAbilities = new AbilityType[values.length];
        for (int i = 0; i < values.length; i++) {
            generatedAbilities[i] = AbilityType.forKey(values[i].getKey());
        }

        assertThat(generatedAbilities, equalTo(values));
    }

    @Test
    public void typeForInvalidKey() {
        try {
            AbilityType.forKey(-1);
            fail();
        } catch (Exception ignored) {}
    }
}
