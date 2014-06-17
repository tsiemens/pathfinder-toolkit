package com.lateensoft.pathfinder.toolkit.db.dao.table;

import android.content.ContentValues;
import com.lateensoft.pathfinder.toolkit.db.dao.OwnedIdentifiableTableDAO;
import com.lateensoft.pathfinder.toolkit.db.dao.OwnedObject;
import com.lateensoft.pathfinder.toolkit.db.dao.Table;
import com.lateensoft.pathfinder.toolkit.model.character.items.Item;
import org.jetbrains.annotations.Nullable;

import java.util.Hashtable;

public class ItemDAO extends OwnedIdentifiableTableDAO<Long, Item> {

    protected static final String TABLE = "Item";
    protected static final String CHARACTER_ID = "character_id";
    protected static final String ID = "item_id";
    private static final String NAME = "Name";
    private static final String WEIGHT = "Weight";
    private static final String QUANTITY = "Quantity";
    private static final String IS_CONTAINED = "IsContained";

    @Override
    protected Table initTable() {
        return new Table(TABLE,
            ID, CHARACTER_ID, NAME, WEIGHT, QUANTITY, IS_CONTAINED);
    }

    @Override
    protected String getIdSelector(Long id) {
        return ID + "=" + id;
    }

    @Override
    protected String getOwnerIdSelector(Long characterId) {
        return CHARACTER_ID + "=" + characterId;
    }

    @Override
    @Nullable protected String getBaseSelector() {
        String itemId = TABLE + "." + ID;
        return String.format("NOT EXISTS (SELECT * FROM %s WHERE %s=%s) "
                +"AND NOT EXISTS (SELECT * FROM %s WHERE %s=%s)",
                ArmorDAO.TABLE, ArmorDAO.TABLE + "." + ArmorDAO.ID, itemId,
                WeaponDAO.TABLE, WeaponDAO.TABLE + "." + WeaponDAO.ID, itemId);
    }

    @Override
    protected String getDefaultOrderBy() {
        return NAME + " ASC";
    }

    @Override
    protected Item buildFromHashTable(Hashtable<String, Object> hashTable) {
        Item item = new Item();
        populateFromHashTable(hashTable, item);
        return item;
    }

    protected void populateFromHashTable(Hashtable<String, Object> hashTable, Item item) {
        long id = (Long) hashTable.get(ID);
        long characterId = (Long) hashTable.get(CHARACTER_ID);
        String name = (String) hashTable.get(NAME);
        double weight = ((Double) hashTable.get(WEIGHT));
        int quantity = ((Long) hashTable.get(QUANTITY)).intValue();
        boolean isContained = ((Long) hashTable.get(IS_CONTAINED)).intValue() != 0;

        item.setId(id);
        item.setCharacterID(characterId);
        item.setName(name);
        item.setWeight(weight);
        item.setQuantity(quantity);
        item.setContained(isContained);
    }

    @Override
    protected ContentValues getContentValues(OwnedObject<Long, Item> rowData) {
        Item item = rowData.getObject();
        ContentValues values = new ContentValues();
        if (isIdSet(item)) {
            values.put(ID, item.getId());
        }
        values.put(CHARACTER_ID, rowData.getOwnerId());
        values.put(NAME, item.getName());
        values.put(WEIGHT, item.getWeight());
        values.put(QUANTITY, item.getQuantity());
        values.put(IS_CONTAINED, item.isContained());
        return values;
    }
}
