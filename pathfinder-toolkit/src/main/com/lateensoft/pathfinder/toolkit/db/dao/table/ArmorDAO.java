package com.lateensoft.pathfinder.toolkit.db.dao.table;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.db.dao.OwnedIdentifiableTableDAO;
import com.lateensoft.pathfinder.toolkit.db.dao.OwnedObject;
import com.lateensoft.pathfinder.toolkit.db.dao.Table;
import com.lateensoft.pathfinder.toolkit.model.character.items.Armor;
import com.lateensoft.pathfinder.toolkit.model.character.items.Item;
import com.lateensoft.pathfinder.toolkit.model.character.items.Size;
import org.jetbrains.annotations.Nullable;

import java.util.Hashtable;
import java.util.List;

public class ArmorDAO extends OwnedIdentifiableTableDAO<Long, Armor> {

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

    private ItemDAO itemDAO;

    public ArmorDAO(Context context) {
        super(context);
        itemDAO = new ItemDAO(context);
    }

    @Override
    protected Table initTable() {
        return new Table(TABLE,
            ID, WORN, AC_BONUS, CHECK_PEN, MAX_DEX, SPELL_FAIL,
                SPEED, SPEC_PROPERTIES, SIZE);
    }

    @Override
    protected String getIdSelector(Long id) {
        return TABLE + "." + ID + "=" + id;
    }

    @Override
    protected String getOwnerIdSelector(Long characterId) {
        return ItemDAO.TABLE + "." + ItemDAO.CHARACTER_ID + "=" + characterId;
    }

    @Override
    @Nullable protected String getBaseSelector() {
        return String.format("%s.%s=%s.%s",
                TABLE, ID, ItemDAO.TABLE, ItemDAO.ID);
    }

    @Override
    protected List<String> getTablesForQuery() {
        return Lists.newArrayList(TABLE, ItemDAO.TABLE);
    }

    @Override
    protected String[] getColumnsForQuery() {
        return getTable().union(itemDAO.getTable(), ID, ItemDAO.ID);
    }

    @Override
    public Long add(OwnedObject<Long, Armor> rowData) throws DataAccessException {
        long id = -1;
        try {
            beginTransaction();
            itemDAO.add(OwnedObject.of(rowData.getOwnerId(), (Item) rowData.getObject()));
            id = super.add(rowData);
            setTransactionSuccessful();
        } finally {
            endTransaction();
        }
        return id;
    }

    @Override
    public void update(OwnedObject<Long, Armor> rowData) throws DataAccessException {
        try {
            beginTransaction();
            itemDAO.update(OwnedObject.of(rowData.getOwnerId(), (Item) rowData.getObject()));
            super.update(rowData);
            setTransactionSuccessful();
        } finally {
            endTransaction();
        }
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
        armor.setArmorCheckPenalty(checkPen);
        armor.setMaxDex(maxDex);
        armor.setSpellFail(spellFail);
        armor.setSpeed(speed);
        armor.setSpecialProperties(specialProperties);
        armor.setSize(Size.forKey(size));
    }

    @Override
    protected ContentValues getContentValues(OwnedObject<Long, Armor> rowData) {
        Armor armor = rowData.getObject();
        ContentValues values = new ContentValues();
        values.put(ID, armor.getId());
        values.put(WORN, armor.isWorn());
        values.put(AC_BONUS, armor.getACBonus());
        values.put(CHECK_PEN, armor.getArmorCheckPenalty());
        values.put(MAX_DEX, armor.getMaxDex());
        values.put(SPELL_FAIL, armor.getSpellFail());
        values.put(SPEED, armor.getSpeed());
        values.put(SPEC_PROPERTIES, armor.getSpecialProperties());
        values.put(SIZE, armor.getSize().getKey());
        return values;
    }

    /** the max dex permitted by of all worn armors */
    public int getMaxDexForCharacter(long characterId) {
        List<Armor> armors = findAllForOwner(characterId);
        return Armor.maxDexForAll(armors);
    }

    /**  the total armor check penalty (negative) of all worn armor */
    public int getArmorCheckPenaltyForCharacter(long characterId) {
        String table = getFromQueryClause();
        String selector = String.format("%s=%d AND %s.%s=%s.%s AND %s<>0",
                ItemDAO.CHARACTER_ID, characterId,
                TABLE, ID, ItemDAO.TABLE, ItemDAO.ID,
                WORN);
        String[] columns = {"SUM("+CHECK_PEN+")"};
        Cursor cursor = getDatabase().query(table, columns, selector);

        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            return cursor.getInt(0);
        }
        return 0;
    }
}
