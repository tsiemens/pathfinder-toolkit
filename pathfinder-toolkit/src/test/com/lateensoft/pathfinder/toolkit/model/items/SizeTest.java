package com.lateensoft.pathfinder.toolkit.model.items;

import com.lateensoft.pathfinder.toolkit.model.character.items.Size;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(JUnit4.class)
public class SizeTest {

    @Test
    public void typeForKey() {
        Size[] values = Size.values();
        Size[] generatedAbilities = new Size[values.length];
        for (int i = 0; i < values.length; i++) {
            generatedAbilities[i] = Size.forKey(values[i].getKey());
        }

        assertThat(generatedAbilities, equalTo(values));
    }

    @Test
    public void typeForInvalidKey() {
        try {
            Size.forKey("");
            fail();
        } catch (Exception ignored) {}
    }
}
