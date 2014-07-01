package com.lateensoft.pathfinder.toolkit.db.dao.table;

import android.content.ContentValues;
import android.content.Context;
import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.db.dao.OwnedIdentifiableTableDAO;
import com.lateensoft.pathfinder.toolkit.db.dao.OwnedObject;
import com.lateensoft.pathfinder.toolkit.db.dao.Table;
import com.lateensoft.pathfinder.toolkit.model.character.items.Item;
import com.lateensoft.pathfinder.toolkit.model.character.items.Size;
import com.lateensoft.pathfinder.toolkit.model.character.items.Weapon;
import com.lateensoft.pathfinder.toolkit.model.character.items.WeaponType;
import org.jetbrains.annotations.Nullable;

import java.util.Hashtable;
import java.util.List;

public class WeaponDAO extends OwnedIdentifiableTableDAO<Long, Weapon> {

    protected static final String TABLE = "Weapon";
    protected static final String ID = "item_id";
    private static final String TOTAL_ATTACK_BONUS = "TotalAttackBonus";
    private static final String DAMAGE = "Damage";
    private static final String CRITICAL = "Critical";
    private static final String RANGE = "Range";
    private static final String SPEC_PROPERTIES = "SpecialProperties";
    private static final String AMMO = "Ammunition";
    private static final String TYPE = "Type";
    private static final String SIZE = "Size";

    private ItemDAO itemDAO;

    public WeaponDAO(Context context) {
        super(context);
        itemDAO = new ItemDAO(context);
    }

    @Override
    protected Table initTable() {
        return new Table(TABLE,
            ID, TOTAL_ATTACK_BONUS, DAMAGE, CRITICAL, RANGE,
                SPEC_PROPERTIES, AMMO, TYPE, SIZE);
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
    public Long add(OwnedObject<Long, Weapon> rowData) throws DataAccessException {
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
    public void update(OwnedObject<Long, Weapon> rowData) throws DataAccessException {
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
    protected Weapon buildFromHashTable(Hashtable<String, Object> hashTable) {
        Weapon weapon = new Weapon();
        itemDAO.populateFromHashTable(hashTable, weapon);
        populateFromHashTable(hashTable, weapon);
        return weapon;
    }

    protected void populateFromHashTable(Hashtable<String, Object> hashTable, Weapon weapon) {
        int totalAttackBonus = ((Long) hashTable.get(TOTAL_ATTACK_BONUS)).intValue();
        String damage = (String) hashTable.get(DAMAGE);
        String critical = (String) hashTable.get(CRITICAL);
        int range = ((Long) hashTable.get(RANGE)).intValue();
        String specialProperties = (String) hashTable.get(SPEC_PROPERTIES);
        int ammunition = ((Long) hashTable.get(AMMO)).intValue();
        String type = (String) hashTable.get(TYPE);
        String size = (String) hashTable.get(SIZE);

        weapon.setTotalAttackBonus(totalAttackBonus);
        weapon.setDamage(damage);
        weapon.setCritical(critical);
        weapon.setRange(range);
        weapon.setSpecialProperties(specialProperties);
        weapon.setAmmunition(ammunition);
        weapon.setType(WeaponType.forKey(type));
        weapon.setSize(Size.forKey(size));
    }

    @Override
    protected ContentValues getContentValues(OwnedObject<Long, Weapon> rowData) {
        Weapon entity = rowData.getObject();
        ContentValues values = new ContentValues();
        values.put(ID, entity.getId());
        values.put(TOTAL_ATTACK_BONUS, entity.getTotalAttackBonus());
        values.put(DAMAGE, entity.getDamage());
        values.put(CRITICAL, entity.getCritical());
        values.put(RANGE, entity.getRange());
        values.put(SPEC_PROPERTIES, entity.getSpecialProperties());
        values.put(AMMO, entity.getAmmunition());
        values.put(TYPE, entity.getType().getKey());
        values.put(SIZE, entity.getSize().getKey());
        return values;
    }
}
