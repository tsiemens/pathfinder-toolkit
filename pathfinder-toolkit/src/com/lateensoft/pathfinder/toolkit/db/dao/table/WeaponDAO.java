package com.lateensoft.pathfinder.toolkit.db.dao.table;

import android.content.ContentValues;
import com.lateensoft.pathfinder.toolkit.dao.DataAccessException;
import com.lateensoft.pathfinder.toolkit.db.dao.Table;
import com.lateensoft.pathfinder.toolkit.db.dao.WeakIdentifiableTableDAO;
import com.lateensoft.pathfinder.toolkit.model.character.items.Weapon;
import org.jetbrains.annotations.Nullable;

import java.util.Hashtable;

public class WeaponDAO extends WeakIdentifiableTableDAO<Long, Weapon> {

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

    private ItemDAO itemDAO = new ItemDAO();

    @Override
    protected Table initTable() {
        return new Table(TABLE,
            ID, TOTAL_ATTACK_BONUS, DAMAGE, CRITICAL, RANGE,
                SPEC_PROPERTIES, AMMO, TYPE, SIZE);
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
    public Long add(Long characterId, Weapon entity) throws DataAccessException {
        itemDAO.add(characterId, entity);
        return super.add(characterId, entity);
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
        weapon.setType(type);
        weapon.setSize(size);
    }

    @Override
    protected ContentValues getContentValues(Long characterId, Weapon entity) {
        ContentValues values = new ContentValues();
        values.put(ID, entity.getId());
        values.put(TOTAL_ATTACK_BONUS, entity.getTotalAttackBonus());
        values.put(DAMAGE, entity.getDamage());
        values.put(CRITICAL, entity.getCritical());
        values.put(RANGE, entity.getRange());
        values.put(SPEC_PROPERTIES, entity.getSpecialProperties());
        values.put(AMMO, entity.getAmmunition());
        values.put(TYPE, entity.getType());
        values.put(SIZE, entity.getSize());
        return values;
    }
}
