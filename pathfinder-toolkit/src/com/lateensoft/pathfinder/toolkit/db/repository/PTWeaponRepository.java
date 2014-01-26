package com.lateensoft.pathfinder.toolkit.db.repository;

import java.util.Hashtable;
import java.util.Locale;

import android.content.ContentValues;
import android.database.Cursor;

import com.lateensoft.pathfinder.toolkit.db.repository.PTTableAttribute.SQLDataType;
import com.lateensoft.pathfinder.toolkit.model.character.items.PTWeapon;

public class PTWeaponRepository extends PTBaseRepository<PTWeapon> {

	public static final String TABLE = "Weapon";
	public static final String ID = "item_id";
	private static final String TOTAL_ATTACK_BONUS = "TotalAttackBonus";
	private static final String DAMAGE = "Damage";
	private static final String CRITICAL = "Critical";
	private static final String RANGE = "Range";
	private static final String SPEC_PROPERTIES = "SpecialProperties";
	private static final String AMMO = "Ammunition";
	private static final String TYPE = "Type";
	private static final String SIZE = "Size";
	
	private PTItemRepository m_itemRepo;
	
	public PTWeaponRepository() {
		super();
		m_itemRepo = new PTItemRepository();
		
		PTTableAttribute id = new PTTableAttribute(ID, SQLDataType.INTEGER, true);
		PTTableAttribute totalAttackB = new PTTableAttribute(TOTAL_ATTACK_BONUS, SQLDataType.INTEGER);
		PTTableAttribute damage = new PTTableAttribute(DAMAGE, SQLDataType.TEXT);
		PTTableAttribute crit = new PTTableAttribute(CRITICAL, SQLDataType.TEXT);
		PTTableAttribute range = new PTTableAttribute(RANGE, SQLDataType.INTEGER);
		PTTableAttribute specProp = new PTTableAttribute(SPEC_PROPERTIES, SQLDataType.TEXT);
		PTTableAttribute ammo = new PTTableAttribute(AMMO, SQLDataType.INTEGER);
		PTTableAttribute type = new PTTableAttribute(TYPE, SQLDataType.TEXT);
		PTTableAttribute size = new PTTableAttribute(SIZE, SQLDataType.TEXT);
		
		PTTableAttribute[] columns = {id, totalAttackB, damage, crit, range, specProp, ammo, type, size};
		m_tableInfo = new PTTableInfo(TABLE, columns);
	}
	
	public void populateFromHashTable(Hashtable<String, Object> hashTable, PTWeapon weapon) {
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
	public long insert(PTWeapon object) {
		m_itemRepo.insert(object);
		return super.insert(object);
	}
	
	@Override
	public PTWeapon query(long ... ids) {
		Locale l = null;
		String selector = String.format(l, "%s.%s=%d AND "
				+"%s.%s=%s.%s", 
				TABLE, ID, ids[0],
				TABLE, ID, PTItemRepository.TABLE, PTItemRepository.ID);
		
		String table = m_tableInfo.getTable()+", "+PTItemRepository.TABLE;
		String[] columns = m_itemRepo.getCombinedTableColumns(m_tableInfo.getColumns());
		Cursor cursor = getDatabase().query(table, columns, selector);
		cursor.moveToFirst();
		if (!cursor.isAfterLast()) {
			Hashtable<String, Object> hashTable =  m_itemRepo.getTableOfValues(cursor);
			hashTable.putAll(getTableOfValues(cursor));
			return buildFromHashTable(hashTable);
		} else {
			return null;
		}
	}
	
	@Override
	public int update(PTWeapon object) {
		m_itemRepo.update(object);
		return super.update(object);
	}
	
	@Override
	public int delete(long ... id) {
		return m_itemRepo.delete(id);
	}
	
	@Override
	public int delete(PTWeapon object) {
		return delete(object.getID());
	}
	
	/**
	 * Builds a PTWeapon from a combined weapon/item hashtable
	 */
	@Override
	protected PTWeapon buildFromHashTable(Hashtable<String, Object> hashTable) {
		PTWeapon weapon = new PTWeapon();
		m_itemRepo.populateFromHashTable(hashTable, weapon);
		populateFromHashTable(hashTable, weapon);
		return weapon;
	}

	@Override
	protected ContentValues getContentValues(PTWeapon object) {
		ContentValues values = new ContentValues();
		values.put(ID, object.getID());
		values.put(TOTAL_ATTACK_BONUS, object.getTotalAttackBonus());
		values.put(DAMAGE, object.getDamage());
		values.put(CRITICAL, object.getCritical());
		values.put(RANGE, object.getRange());
		values.put(SPEC_PROPERTIES, object.getSpecialProperties());
		values.put(AMMO, object.getAmmunition());
		values.put(TYPE, object.getType());
		values.put(SIZE, object.getSize());
		return values;
	}
	
	/**
	 * Returns all items for the character with characterId
	 * @param characterId
	 * @return Array of PTItem, ordered alphabetically by name
	 */
	public PTWeapon[] querySet(long characterId) {
		String table = PTItemRepository.TABLE + ", " + m_tableInfo.getTable();
		Locale l = null;
		String selector = String.format(l, "%s=%d AND %s.%s=%s.%s",
				CHARACTER_ID, characterId,
				TABLE, ID, PTItemRepository.TABLE, PTItemRepository.ID);
		String orderBy = PTItemRepository.NAME + " ASC";
		String[] columns = m_itemRepo.getCombinedTableColumns(m_tableInfo.getColumns());
		
		Cursor cursor = getDatabase().query(true, table, columns, selector, 
				null, null, null, orderBy, null);
		
		PTWeapon[] weapons = new PTWeapon[cursor.getCount()];
		cursor.moveToFirst();
		int i = 0;
		while (!cursor.isAfterLast()) {
			Hashtable<String, Object> hashTable =  m_itemRepo.getTableOfValues(cursor);
			hashTable.putAll(getTableOfValues(cursor));
			weapons[i] = buildFromHashTable(hashTable);
			cursor.moveToNext();
			i++;
		}
		return weapons;
	}

}
