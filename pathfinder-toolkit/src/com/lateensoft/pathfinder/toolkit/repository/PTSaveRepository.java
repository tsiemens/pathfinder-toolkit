package com.lateensoft.pathfinder.toolkit.repository;

import android.content.ContentValues;
import android.database.Cursor;

import com.lateensoft.pathfinder.toolkit.stats.PTSave;
import com.lateensoft.pathfinder.toolkit.stats.PTStat;

public class PTSaveRepository extends PTBaseRepository<PTSave> {
	static final String TABLE = "save";
	static final String ID = "save_id";
	static final String STAT_ID = "stat_id";
	static final String TOTAL = "Total";
	static final String ABILITY_MOD = "AbilityMod";
	static final String MAGIC_MOD = "MagicMod";
	static final String MISC_MOD = "MiscMod";
	static final String TEMP_MOD = "TempMod";
	static final String[] COLUMNS = {ID, STAT_ID, TOTAL, ABILITY_MOD, MAGIC_MOD, MISC_MOD, TEMP_MOD};
	
	public PTSaveRepository() {
		super();
	}

	@Override
	protected PTSave buildFromCursor(Cursor cursor) {
		PTStatRepository statRepo = new PTStatRepository();
		int baseIdIndex = cursor.getColumnIndex(STAT_ID);
		//int totalIndex = cursor.getColumnIndex(TOTAL);
		int abilityModIndex = cursor.getColumnIndex(ABILITY_MOD);
		int magicModIndex = cursor.getColumnIndex(MAGIC_MOD);
		int miscModIndex = cursor.getColumnIndex(MISC_MOD);
		int tempModIndex = cursor.getColumnIndex(TEMP_MOD);
		
		int baseId = cursor.getInt(baseIdIndex);
		PTStat baseStat = statRepo.query(baseId);
		String name = baseStat.getName();
		int baseValue = baseStat.getBaseValue();
		//int total = cursor.getInt(totalIndex);
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
	protected void baseUpdate(PTSave object) {
		PTStat stat = (PTStat) object;
		PTStatRepository repo = new PTStatRepository();
		repo.update(stat);
	}

	@Override
	protected String TABLE() {
		return TABLE;
	}

	@Override
	protected String[] COLUMNS() {
		return COLUMNS;
	}

	@Override
	protected String ID() {
		return ID;
	}	
}
