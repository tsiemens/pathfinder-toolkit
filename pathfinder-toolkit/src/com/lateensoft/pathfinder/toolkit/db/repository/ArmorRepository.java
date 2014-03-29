package com.lateensoft.pathfinder.toolkit.db.repository;

import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.db.repository.TableAttribute.SQLDataType;
import com.lateensoft.pathfinder.toolkit.model.character.items.Armor;

public class ArmorRepository extends BaseRepository<Armor> {

	public static final String TABLE = "Armor";
	public static final String ID = "item_id";
	private static final String WORN = "Worn";
	private static final String AC_BONUS = "ACBonus";
	private static final String CHECK_PEN = "CheckPen";
	private static final String MAX_DEX = "MaxDex";
	private static final String SPELL_FAIL = "SpellFail";
	private static final String SPEED = "Speed";
	private static final String SPEC_PROPERTIES = "SpecialProperties";
	private static final String SIZE = "Size";
	
	private ItemRepository m_itemRepo;
	
	public ArmorRepository() {
		super();
		m_itemRepo = new ItemRepository();
		
		TableAttribute id = new TableAttribute(ID, SQLDataType.INTEGER, true);
		TableAttribute worn = new TableAttribute(WORN, SQLDataType.INTEGER);
		TableAttribute acBonus = new TableAttribute(AC_BONUS, SQLDataType.INTEGER);
		TableAttribute checkPen = new TableAttribute(CHECK_PEN, SQLDataType.INTEGER);
		TableAttribute maxDex = new TableAttribute(MAX_DEX, SQLDataType.INTEGER);
		TableAttribute spellFail = new TableAttribute(SPELL_FAIL, SQLDataType.INTEGER);
		TableAttribute speed = new TableAttribute(SPEED, SQLDataType.INTEGER);
		TableAttribute specProp = new TableAttribute(SPEC_PROPERTIES, SQLDataType.TEXT);
		TableAttribute size = new TableAttribute(SIZE, SQLDataType.TEXT);
		
		TableAttribute[] columns = {id, worn, acBonus, checkPen, maxDex, spellFail, speed, specProp, size};
		m_tableInfo = new TableInfo(TABLE, columns);
	}
	
	public void populateFromHashTable(Hashtable<String, Object> hashTable, Armor armor) {
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
	public long insert(Armor object) {
		m_itemRepo.insert(object);
		return super.insert(object);
	}
	
	@Override
	public Armor query(long ... ids) {
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
	public int update(Armor object) {
		m_itemRepo.update(object);
		return super.update(object);
	}
	
	@Override
	public int delete(long ... id) {
		return m_itemRepo.delete(id);
	}
	
	@Override
	public int delete(Armor object) {
		return delete(object.getID());
	}
	
	/**
	 * Builds a Armor from a combined armor/item hashtable
	 */
	@Override
	protected Armor buildFromHashTable(Hashtable<String, Object> hashTable) {
		Armor armor = new Armor();
		m_itemRepo.populateFromHashTable(hashTable, armor);
		populateFromHashTable(hashTable, armor);
		return armor;
	}

	@Override
	protected ContentValues getContentValues(Armor object) {
		ContentValues values = new ContentValues();
		values.put(ID, object.getID());
		values.put(WORN, object.isWorn());
		values.put(AC_BONUS, object.getACBonus());
		values.put(CHECK_PEN, object.getCheckPen());
		values.put(MAX_DEX, object.getMaxDex());
		values.put(SPELL_FAIL, object.getSpellFail());
		values.put(SPEED, object.getSpeed());
		values.put(SPEC_PROPERTIES, object.getSpecialProperties());
		values.put(SIZE, object.getSize());
		return values;
	}
	
	/**
	 * Returns all items for the character with characterId
	 * @param characterId
	 * @return Array of Item, ordered alphabetically by name
	 */
	public List<Armor> querySet(long characterId) {
		String table = ItemRepository.TABLE + ", " + m_tableInfo.getTable();
		Locale l = null;
		String selector = String.format(l, "%s=%d AND %s.%s=%s.%s",
				CHARACTER_ID, characterId,
				TABLE, ID, ItemRepository.TABLE, ItemRepository.ID);
		String orderBy = ItemRepository.NAME + " ASC";
		String[] columns = m_itemRepo.getCombinedTableColumns(m_tableInfo.getColumns());
		
		Cursor cursor = getDatabase().query(true, table, columns, selector, 
				null, null, null, orderBy, null);
		
		List<Armor> armors = Lists.newArrayListWithCapacity(cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Hashtable<String, Object> hashTable =  m_itemRepo.getTableOfValues(cursor);
			hashTable.putAll(getTableOfValues(cursor));
			armors.add(buildFromHashTable(hashTable));
			cursor.moveToNext();
		}
		return armors;
	}
	
	/**
	 * @param characterId
	 * @return the max dex permitted out of all worn armors
	 */
	public int getMaxDex(long characterId) {
		List<Armor> armors = querySet(characterId);
		int maxDex = Integer.MAX_VALUE;
		for (Armor armor : armors) {
			if (armor.isWorn() && armor.getMaxDex() < maxDex) {
				maxDex = armor.getMaxDex();
			}
		}
		return maxDex;
	}
	
	/**
	 * @param characterId
	 * @return the total armor check penalty (negative) of all worn armor
	 */
	public int getArmorCheckPenalty(long characterId) {
		String table = ItemRepository.TABLE + ", " + m_tableInfo.getTable();
		Locale l = null;
		String selector = String.format(l, "%s=%d AND %s.%s=%s.%s AND %s<>0",
				CHARACTER_ID, characterId,
				TABLE, ID, ItemRepository.TABLE, ItemRepository.ID,
				WORN);
		String[] columns = {"SUM("+CHECK_PEN+")"};
		
		Cursor cursor = getDatabase().query(true, table, columns, selector, 
				null, null, null, null, null);
		
		cursor.moveToFirst();
		if (!cursor.isAfterLast()) {
			return cursor.getInt(0);
		}
		return 0;
	}
}
