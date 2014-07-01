package com.lateensoft.pathfinder.toolkit.db.repository;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.db.repository.TableAttribute.SQLDataType;
import com.lateensoft.pathfinder.toolkit.model.character.items.Item;

public class ItemRepository extends BaseRepository<Item> {

    public static final String TABLE = "Item";
    public static final String ID = "item_id";
    public static final String NAME = "Name";
    private static final String WEIGHT = "Weight";
    private static final String QUANTITY = "Quantity";
    private static final String IS_CONTAINED = "IsContained";
    
    public ItemRepository() {
        super();
        TableAttribute id = new TableAttribute(ID, SQLDataType.INTEGER, true);
        TableAttribute characterId = new TableAttribute(CHARACTER_ID, SQLDataType.INTEGER);
        TableAttribute name = new TableAttribute(NAME, SQLDataType.TEXT);
        TableAttribute weight = new TableAttribute(WEIGHT, SQLDataType.REAL);
        TableAttribute quantity = new TableAttribute(QUANTITY, SQLDataType.INTEGER);
        TableAttribute isContained = new TableAttribute(IS_CONTAINED, SQLDataType.INTEGER);
        
        TableAttribute[] columns = {id, characterId, name, weight, quantity, isContained};
        m_tableInfo = new TableInfo(TABLE, columns);
    }
    
    public void populateFromHashTable(Hashtable<String, Object> hashTable, Item item) {
        long id = (Long) hashTable.get(ID);
        long characterId = (Long) hashTable.get(CHARACTER_ID);
        String name = (String) hashTable.get(NAME);
        double weight = ((Double) hashTable.get(WEIGHT)).doubleValue();
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
    protected Item buildFromHashTable(Hashtable<String, Object> hashTable) {
        Item item = new Item();
        populateFromHashTable(hashTable, item);
        return item;
    }

    @Override
    protected ContentValues getContentValues(Item object) {
        ContentValues values = new ContentValues();
        if (isIDSet(object)) { 
            values.put(ID, object.getId());
        }
        values.put(CHARACTER_ID, object.getCharacterID());
        values.put(NAME, object.getName());
        values.put(WEIGHT, object.getWeight());
        values.put(QUANTITY, object.getQuantity());
        values.put(IS_CONTAINED, object.isContained());
        return values;
    }
    
    /**
     * Returns all items for the character with characterId
     * @param characterId
     * @return Array of Item, ordered alphabetically by name
     */
    public List<Item> querySet(long characterId) {
        // TODO replace with constants from those tables
        String table = m_tableInfo.getTable();
        Locale l = null;
        String selector = String.format(l, "%s=%d AND NOT EXISTS ("
                +"SELECT * FROM %s WHERE %s=%s ) "
                +"AND NOT EXISTS ("
                        +"SELECT * FROM %s WHERE %s=%s )", 
                CHARACTER_ID, characterId,
                "Armor", "Armor.item_id",  table+"."+ID,
                WeaponRepository.TABLE,
                WeaponRepository.TABLE+"."+ WeaponRepository.ID, table+"."+ID);
        String orderBy = NAME + " ASC";
        String[] columns = m_tableInfo.getColumns();
        Cursor cursor = getDatabase().query(true, table, columns, selector, 
                null, null, null, orderBy, null);
        
        List<Item> items = Lists.newArrayListWithCapacity(cursor.getCount());
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Hashtable<String, Object> hashTable =  getTableOfValues(cursor);
            items.add(buildFromHashTable(hashTable));
            cursor.moveToNext();
        }
        return items;
    }

    public String[] getCombinedTableColumns(String[] subTableCols) {
        String[] itemCols = m_tableInfo.getColumns();
        int idColIndex;
        for (idColIndex = 0; idColIndex < subTableCols.length; idColIndex++) {
            if (subTableCols[idColIndex].contentEquals(ID)) {
                break;
            }
        }
        
        boolean foundId = false;
        ArrayList<String> combinedCols = new ArrayList<String>(subTableCols.length + itemCols.length -1);
        for (int i = 0; i < itemCols.length; i++) {
            if (foundId == false && itemCols[i].contentEquals(ID)) {
                combinedCols.add(ItemRepository.TABLE+"."+itemCols[i]+" "+itemCols[i]);
                foundId = true;
            } else {
                combinedCols.add(itemCols[i]);
            }
        }
        
        for (int i = 0; i < subTableCols.length; i++) {
            if (i != idColIndex) {
                combinedCols.add(subTableCols[i]);
            }
        };
        
        String[] colsArray = new String[combinedCols.size()];
        combinedCols.toArray(colsArray);
        return colsArray;
    }
}
