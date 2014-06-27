package com.lateensoft.pathfinder.toolkit.model.items;

import com.lateensoft.pathfinder.toolkit.model.character.items.WeaponType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(JUnit4.class)
public class WeaponTypeTest {

    @Test
    public void typeForKey() {
        WeaponType[] values = WeaponType.values();
        WeaponType[] generatedAbilities = new WeaponType[values.length];
        for (int i = 0; i < values.length; i++) {
            generatedAbilities[i] = WeaponType.forKey(values[i].getKey());
        }

        assertThat(generatedAbilities, equalTo(values));
    }

    @Test
    public void typeForInvalidKey() {
        try {
            WeaponType.forKey("");
            fail();
        } catch (Exception ignored) {}
    }
}
