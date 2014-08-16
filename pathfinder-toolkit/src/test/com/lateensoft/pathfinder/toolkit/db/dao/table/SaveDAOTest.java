package com.lateensoft.pathfinder.toolkit.db.dao.table;

import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.model.character.stats.AbilityType;
import com.lateensoft.pathfinder.toolkit.model.character.stats.Save;
import com.lateensoft.pathfinder.toolkit.model.character.stats.SaveType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@Config(manifest=Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class SaveDAOTest extends CharacterComponentDAOTest {

    private Save save;
    private SaveDAO dao;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        initAndTestValidFindAndUpdate();
    }

    private void initAndTestValidFindAndUpdate() throws DataAccessException {
        dao = new SaveDAO(Robolectric.application);
        save = dao.find(getTestCharacterId(), SaveType.REF.getKey());
        save.setBaseSave(8);
        save.setAbilityType(AbilityType.CHA);
        save.setMagicMod(5);
        save.setMiscMod(-2);
        save.setTempMod(0);
        dao.update(getTestCharacterId(), save);
        assertEquals(save, dao.find(getTestCharacterId(), save.getType().getKey()));
    }

    @Test
    public void findInvalid() {
        assertNull(dao.find(getTestCharacterId(), -1));
    }

    @Test(expected = DataAccessException.class)
    public void addInvalid() throws DataAccessException {
        dao.add(getTestCharacterId(), save);
    }

    @Test(expected = DataAccessException.class)
    public void updateInvalid() throws DataAccessException {
        dao.update(-1L, save);
    }
}
