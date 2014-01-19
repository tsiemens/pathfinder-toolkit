package com.lateensoft.pathfinder.toolkit.db.repository;

import java.util.Hashtable;
import java.util.Locale;

import android.content.ContentValues;
import android.database.Cursor;

import com.lateensoft.pathfinder.toolkit.db.repository.PTTableAttribute.SQLDataType;
import com.lateensoft.pathfinder.toolkit.model.character.items.PTArmor;

public class PTArmorRepository extends PTBaseRepository<PTArmor> {

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
	
	private PTItemRepository m_itemRepo;
	
	public PTArmorRepository() {
		super();
		m_itemRepo = new PTItemRepository();
		
		PTTableAttribute id = new PTTableAttribute(ID, SQLDataType.INTEGER, true);
		PTTableAttribute worn = new PTTableAttribute(WORN, SQLDataType.INTEGER);
		PTTableAttribute acBonus = new PTTableAttribute(AC_BONUS, SQLDataType.INTEGER);
		PTTableAttribute checkPen = new PTTableAttribute(CHECK_PEN, SQLDataType.INTEGER);
		PTTableAttribute maxDex = new PTTableAttribute(MAX_DEX, SQLDataType.INTEGER);
		PTTableAttribute spellFail = new PTTableAttribute(SPELL_FAIL, SQLDataType.INTEGER);
		PTTableAttribute speed = new PTTableAttribute(SPEED, SQLDataType.INTEGER);
		PTTableAttribute specProp = new PTTableAttribute(SPEC_PROPERTIES, SQLDataType.TEXT);
		PTTableAttribute size = new PTTableAttribute(SIZE, SQLDataType.TEXT);
		
		PTTableAttribute[] columns = {id, worn, acBonus, checkPen, maxDex, spellFail, speed, specProp, size};
		m_tableInfo = new PTTableInfo(TABLE, columns);
	}
	
	public void populateFromHashTable(Hashtable<String, Object> hashTable, PTArmor armor) {
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
	public long insert(PTArmor object) {
		m_itemRepo.insert(object);
		return super.insert(object);
	}
	
	@Override
	public PTArmor query(long ... id) {
		Locale l = null;
		String selector = String.format(l, "%s.%s=%d AND "
				+"%s.%s=%s.%s", 
				TABLE, ID, id,
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
	public int update(PTArmor object) {
		m_itemRepo.update(object);
		return super.update(object);
	}
	
	@Override
	public int delete(long ... id) {
		return m_itemRepo.delete(id);
	}
	
	@Override
	public int delete(PTArmor object) {
		return delete(object.getID());
	}
	
	/**
	 * Builds a PTArmor from a combined armor/item hashtable
	 */
	@Override
	protected PTArmor buildFromHashTable(Hashtable<String, Object> hashTable) {
		PTArmor armor = new PTArmor();
		m_itemRepo.populateFromHashTable(hashTable, armor);
		populateFromHashTable(hashTable, armor);
		return armor;
	}

	@Override
	protected ContentValues getContentValues(PTArmor object) {
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
	 * @return Array of PTItem, ordered alphabetically by name
	 */
	public PTArmor[] querySet(long characterId) {
		String table = PTItemRepository.TABLE + ", " + m_tableInfo.getTable();
		Locale l = null;
		String selector = String.format(l, "%s=%d AND %s.%s=%s.%s",
				CHARACTER_ID, characterId,
				TABLE, ID, PTItemRepository.TABLE, PTItemRepository.ID);
		String orderBy = PTItemRepository.NAME + " ASC";
		String[] columns = m_itemRepo.getCombinedTableColumns(m_tableInfo.getColumns());
		
		Cursor cursor = getDatabase().query(true, table, columns, selector, 
				null, null, null, orderBy, null);
		
		PTArmor[] armors = new PTArmor[cursor.getCount()];
		cursor.moveToFirst();
		int i = 0;
		while (!cursor.isAfterLast()) {
			Hashtable<String, Object> hashTable =  m_itemRepo.getTableOfValues(cursor);
			hashTable.putAll(getTableOfValues(cursor));
			armors[i] = buildFromHashTable(hashTable);
			cursor.moveToNext();
			i++;
		}
		return armors;
	}
}
