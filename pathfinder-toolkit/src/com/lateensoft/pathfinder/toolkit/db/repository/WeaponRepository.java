package com.lateensoft.pathfinder.toolkit.db.repository;

import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.db.repository.TableAttribute.SQLDataType;
import com.lateensoft.pathfinder.toolkit.model.character.items.Weapon;

public class WeaponRepository extends BaseRepository<Weapon> {

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
	
	private ItemRepository m_itemRepo;
	
	public WeaponRepository() {
		super();
		m_itemRepo = new ItemRepository();
		
		TableAttribute id = new TableAttribute(ID, SQLDataType.INTEGER, true);
		TableAttribute totalAttackB = new TableAttribute(TOTAL_ATTACK_BONUS, SQLDataType.INTEGER);
		TableAttribute damage = new TableAttribute(DAMAGE, SQLDataType.TEXT);
		TableAttribute crit = new TableAttribute(CRITICAL, SQLDataType.TEXT);
		TableAttribute range = new TableAttribute(RANGE, SQLDataType.INTEGER);
		TableAttribute specProp = new TableAttribute(SPEC_PROPERTIES, SQLDataType.TEXT);
		TableAttribute ammo = new TableAttribute(AMMO, SQLDataType.INTEGER);
		TableAttribute type = new TableAttribute(TYPE, SQLDataType.TEXT);
		TableAttribute size = new TableAttribute(SIZE, SQLDataType.TEXT);
		
		TableAttribute[] columns = {id, totalAttackB, damage, crit, range, specProp, ammo, type, size};
		m_tableInfo = new TableInfo(TABLE, columns);
	}
	
	public void populateFromHashTable(Hashtable<String, Object> hashTable, Weapon weapon) {
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
	public long insert(Weapon object) {
		m_itemRepo.insert(object);
		return super.insert(object);
	}
	
	@Override
	public Weapon query(long ... ids) {
		Locale l = null;
		String selector = String.format(l, "%s.%s=%d AND "
				+"%s.%s=%s.%s", 
				TABLE, ID, ids[0],
				TABLE, ID, ItemRepository.TABLE, ItemRepository.ID);
		
		String table = m_tableInfo.getTable()+", "+ ItemRepository.TABLE;
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
	public int update(Weapon object) {
		m_itemRepo.update(object);
		return super.update(object);
	}
	
	@Override
	public int delete(long ... id) {
		return m_itemRepo.delete(id);
	}
	
	@Override
	public int delete(Weapon object) {
		return delete(object.getID());
	}
	
	/**
	 * Builds a Weapon from a combined weapon/item hashtable
	 */
	@Override
	protected Weapon buildFromHashTable(Hashtable<String, Object> hashTable) {
		Weapon weapon = new Weapon();
		m_itemRepo.populateFromHashTable(hashTable, weapon);
		populateFromHashTable(hashTable, weapon);
		return weapon;
	}

	@Override
	protected ContentValues getContentValues(Weapon object) {
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
	 * @return Array of Item, ordered alphabetically by name
	 */
	public List<Weapon> querySet(long characterId) {
		String table = ItemRepository.TABLE + ", " + m_tableInfo.getTable();
		Locale l = null;
		String selector = String.format(l, "%s=%d AND %s.%s=%s.%s",
				CHARACTER_ID, characterId,
				TABLE, ID, ItemRepository.TABLE, ItemRepository.ID);
		String orderBy = ItemRepository.NAME + " ASC";
		String[] columns = m_itemRepo.getCombinedTableColumns(m_tableInfo.getColumns());
		
		Cursor cursor = getDatabase().query(true, table, columns, selector, 
				null, null, null, orderBy, null);
		
		List<Weapon> weapons = Lists.newArrayListWithCapacity(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Hashtable<String, Object> hashTable =  m_itemRepo.getTableOfValues(cursor);
			hashTable.putAll(getTableOfValues(cursor));
			weapons.add(buildFromHashTable(hashTable));
			cursor.moveToNext();
		}
		return weapons;
	}

}
