package com.lateensoft.pathfinder.toolkit.repository;

import android.content.ContentValues;
import android.database.Cursor;

import com.lateensoft.pathfinder.toolkit.repository.PTTableAttribute.SQLDataType;
import com.lateensoft.pathfinder.toolkit.stats.PTSave;
import com.lateensoft.pathfinder.toolkit.stats.PTStat;

public class PTSaveRepository extends PTBaseRepository<PTSave> {
	static final String TABLE = "save";
	
	public PTSaveRepository() {
		super();
		PTTableAttribute ID = new PTTableAttribute("save_id", SQLDataType.INTEGER, true);
		PTTableAttribute CHARACTER_ID = new PTTableAttribute("character_id", SQLDataType.INTEGER);
		PTTableAttribute NAME = new PTTableAttribute("Name", SQLDataType.TEXT);
		PTTableAttribute BASEVALUE = new PTTableAttribute("BaseValue", SQLDataType.INTEGER);
		PTTableAttribute TOTAL = new PTTableAttribute("Total", SQLDataType.INTEGER);
		PTTableAttribute ABILITY_MOD = new PTTableAttribute("AbilityMod", SQLDataType.INTEGER);
		PTTableAttribute MAGIC_MOD = new PTTableAttribute("MagicMod", SQLDataType.INTEGER);
		PTTableAttribute MISC_MOD = new PTTableAttribute("MiscMod", SQLDataType.INTEGER);
		PTTableAttribute TEMP_MOD = new PTTableAttribute("TempMod", SQLDataType.INTEGER);
		PTTableAttribute[] attributes = {ID, CHARACTER_ID, NAME, BASEVALUE, TOTAL, ABILITY_MOD, MAGIC_MOD,
				MISC_MOD, TEMP_MOD};
		mTableAttributeSet = new PTTableAttributeSet(TABLE, attributes);
	}

	protected PTSave buildTable(Cursor cursor) {
		int characterIdIndex = cursor.getColumnIndex(CHARACTER_ID);
		int abilityModIndex = cursor.getColumnIndex(ABILITY_MOD);
		int magicModIndex = cursor.getColumnIndex(MAGIC_MOD);
		int miscModIndex = cursor.getColumnIndex(MISC_MOD);
		int tempModIndex = cursor.getColumnIndex(TEMP_MOD);
		
		int baseId = cursor.getInt(baseIdIndex);
		PTStat baseStat = statRepo.query(baseId);
		String name = baseStat.getName();
		int baseValue = baseStat.getBaseValue();
		int abilityMod = cursor.getInt(abilityModIndex);
		int magicMod = cursor.getInt(magicModIndex);
		int miscMod = cursor.getInt(miscModIndex);
		int tempMod = cursor.getInt(tempModIndex);
		
		PTSave save = new PTSave(name, baseValue, abilityMod, magicMod, miscMod, tempMod);
		return save;
	}

	@Override
	protected ContentValues getContentValues(PTSave object) {
		ContentValues values = new ContentValues();
		values.put(TOTAL, object.getTotal());
		values.put(ABILITY_MOD, object.getAbilityMod());
		values.put(MAGIC_MOD, object.getMagicMod());
		values.put(MISC_MOD, object.getMiscMod());
		values.put(TEMP_MOD, object.getTempMod());
		return values;
	}

	@Override
	protected PTSave buildFromCursor(Cursor cursor) {
		// TODO Auto-generated method stub
		return null;
	}
}
