package com.lateensoft.pathfinder.toolkit.db.dao.table;

import android.content.ContentValues;
import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.db.dao.Table;
import com.lateensoft.pathfinder.toolkit.db.dao.WeakIdentifiableTableDAO;
import com.lateensoft.pathfinder.toolkit.model.character.items.Armor;
import org.jetbrains.annotations.Nullable;

import java.util.Hashtable;

public class ArmorDAO extends WeakIdentifiableTableDAO<Long, Armor> {

    protected static final String TABLE = "Armor";
    protected static final String ID = "item_id";
    private static final String WORN = "Worn";
    private static final String AC_BONUS = "ACBonus";
    private static final String CHECK_PEN = "CheckPen";
    private static final String MAX_DEX = "MaxDex";
    private static final String SPELL_FAIL = "SpellFail";
    private static final String SPEED = "Speed";
    private static final String SPEC_PROPERTIES = "SpecialProperties";
    private static final String SIZE = "Size";

    private ItemDAO itemDAO = new ItemDAO();

    @Override
    protected Table initTable() {
        return new Table(TABLE,
            ID, WORN, AC_BONUS, CHECK_PEN, MAX_DEX, SPELL_FAIL,
                SPEED, SPEC_PROPERTIES, SIZE);
    }

    @Override
    protected String getIdSelector(IdPair<Long, Long> idPair) {
        return TABLE + "." + ID + "=" + idPair.getWeakId();
    }

    @Override
    public String getStrongIdSelector(Long characterId) {
        return ItemDAO.TABLE + "." + ItemDAO.CHARACTER_ID + "=" + characterId;
    }

    @Override
    @Nullable protected String getBaseSelector() {
        return String.format("%s.%s=%s.%s",
                TABLE, ID, ItemDAO.TABLE, ItemDAO.ID);
    }

    @Override
    public Long add(Long characterId, Armor entity) throws DataAccessException {
        itemDAO.add(characterId, entity);
        return super.add(characterId, entity);
    }

    @Override
    protected Armor buildFromHashTable(Hashtable<String, Object> hashTable) {
        Armor armor = new Armor();
        itemDAO.populateFromHashTable(hashTable, armor);
        populateFromHashTable(hashTable, armor);
        return armor;
    }

    protected void populateFromHashTable(Hashtable<String, Object> hashTable, Armor armor) {
        boolean worn = ((Long) hashTable.get(WORN)).intValue() != 0;
        int acBonus = ((Long) hashTable.get(AC_BONUS)).intValue();
        int checkPen = ((Long) hashTable.get(CHECK_PEN)).intValue();
        int maxDex = ((Long) hashTable.get(MAX_DEX)).intValue();
        int spellFail = ((Long) hashTable.get(SPELL_FAIL)).intValue();
        int speed = ((Long) hashTable.get(SPEED)).intValue();
        String specialProperties = (String) hashTable.get(SPEC_PROPERTIES);
        String size = (String) hashTable.get(SIZE);

        armor.setWorn(worn);
        armor.setACBonus(acBonus);
        armor.setCheckPen(checkPen);
        armor.setMaxDex(maxDex);
        armor.setSpellFail(spellFail);
        armor.setSpeed(speed);
        armor.setSpecialProperties(specialProperties);
        armor.setSize(size);
    }

    @Override
    protected ContentValues getContentValues(Long characterId, Armor entity) {
        ContentValues values = new ContentValues();
        values.put(ID, entity.getId());
        values.put(WORN, entity.isWorn());
        values.put(AC_BONUS, entity.getACBonus());
        values.put(CHECK_PEN, entity.getCheckPen());
        values.put(MAX_DEX, entity.getMaxDex());
        values.put(SPELL_FAIL, entity.getSpellFail());
        values.put(SPEED, entity.getSpeed());
        values.put(SPEC_PROPERTIES, entity.getSpecialProperties());
        values.put(SIZE, entity.getSize());
        return values;
    }
}
